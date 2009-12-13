/*
 * Copyright (c) 2004-2009 XMLVM --- An XML-based Programming Language
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 675 Mass
 * Ave, Cambridge, MA 02139, USA.
 * 
 * For more information, visit the XMLVM Home Page at http://www.xmlvm.org
 */

package org.xmlvm.proc.out;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xmlvm.Log;
import org.xmlvm.main.Arguments;
import org.xmlvm.proc.XmlvmResource;
import org.xmlvm.proc.in.file.XmlvmFile;

import com.android.dx.cf.code.ConcreteMethod;
import com.android.dx.cf.code.Ropper;
import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.direct.StdAttributeFactory;
import com.android.dx.cf.iface.Method;
import com.android.dx.cf.iface.MethodList;
import com.android.dx.dex.cf.CfTranslator;
import com.android.dx.dex.code.CodeAddress;
import com.android.dx.dex.code.CstInsn;
import com.android.dx.dex.code.DalvCode;
import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.DalvInsnList;
import com.android.dx.dex.code.Dop;
import com.android.dx.dex.code.Dops;
import com.android.dx.dex.code.LocalSnapshot;
import com.android.dx.dex.code.LocalStart;
import com.android.dx.dex.code.PositionList;
import com.android.dx.dex.code.RopTranslator;
import com.android.dx.dex.code.SimpleInsn;
import com.android.dx.rop.code.AccessFlags;
import com.android.dx.rop.code.DexTranslationAdvice;
import com.android.dx.rop.code.LocalVariableExtractor;
import com.android.dx.rop.code.LocalVariableInfo;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.rop.code.TranslationAdvice;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstType;
import com.android.dx.ssa.Optimizer;
import com.android.dx.util.ExceptionWithContext;

/**
 * This OutputProcess emits XMLVM code containing register-based DEX
 * instructions (XMLVM-DEX).
 * <p>
 * TODO(Sascha): Work in progress!
 */
public class DEXmlvmOutputProcess extends OutputProcess<JavaByteCodeOutputProcess> {
    private static final String    REG_PREFIX  = "reg";
    private static final Namespace NS_XMLVM    = XmlvmResource.xmlvmNamespace;
    private static final Namespace NS_DEX      = Namespace.getNamespace("dex",
                                                       "http://xmlvm.org/dex");

    private List<OutputFile>       outputFiles = new ArrayList<OutputFile>();

    public DEXmlvmOutputProcess(Arguments arguments) {
        super(arguments);
        addSupportedInput(JavaByteCodeOutputProcess.class);
    }

    @Override
    public List<OutputFile> getOutputFiles() {
        return outputFiles;
    }

    @Override
    public boolean process() {
        List<JavaByteCodeOutputProcess> preprocesses = preprocess();

        for (JavaByteCodeOutputProcess process : preprocesses) {
            for (OutputFile preOutputFile : process.getOutputFiles()) {
                OutputFile outputFile = generateDEXmlvmFile(preOutputFile);
                if (outputFile == null) {
                    return false;
                }
                outputFiles.add(outputFile);
            }
        }
        return true;
    }

    private OutputFile generateDEXmlvmFile(OutputFile classFile) {
        if (!classFile.getFullPath().startsWith(arguments.option_out())) {
            Log.error("DexOutputProcess: Something is wrong with the class output path.");
            return null;
        }
        String relativePath = classFile.getFullPath()
                .substring(arguments.option_out().length() + 1);

        // Remove a starting slash or backslash.
        if (relativePath.startsWith("/") || relativePath.startsWith("\\")) {
            relativePath = relativePath.substring(1);
        }
        Log.debug("DExing:" + relativePath);

        DirectClassFile directClassFile = new DirectClassFile(classFile.getDataAsBytes(),
                relativePath, true);
        Document document = createDocument();
        process(directClassFile, document.getRootElement());

        OutputFile result = new OutputFile();
        result.setLocation(arguments.option_out());
        result.setFileName(classFile.getFileName() + XmlvmFile.XMLVM_ENDING);
        result.setData(documentToString(document));

        return result;
    }

    /**
     * Creates a basic XMLVM document.
     */
    private static Document createDocument() {
        Element root = new Element("xmlvm", NS_XMLVM);
        root.addNamespaceDeclaration(NS_DEX);
        Document document = new Document();
        document.addContent(root);
        return document;
    }

    /**
     * Process the given Java Class file and add the classes to the given root.
     * 
     * @param cf
     *            the class file to process
     * @param root
     *            the root element to append the classes to
     */
    private void process(DirectClassFile cf, Element root) {
        cf.setAttributeFactory(StdAttributeFactory.THE_ONE);
        cf.getMagic();

        CstType thisClass = cf.getThisClass();
        Element classElement = processClass(thisClass, root);
        MethodList methods = cf.getMethods();
        int sz = methods.size();

        for (int i = 0; i < sz; i++) {
            Method one = methods.get(i);
            try {
                processMethod(one, cf, classElement);
            } catch (RuntimeException ex) {
                String msg = "...while processing " + one.getName().toHuman() + " "
                        + one.getDescriptor().toHuman();
                throw ExceptionWithContext.withContext(ex, msg);
            }
        }
    }

    /**
     * Creates an XMLVM element for the given type and appends it to the given
     * root element.
     * 
     * @param type
     *            the class type to create the element for
     * @param root
     *            the root element to append the generated element to
     * @return the generated element
     */
    private static Element processClass(CstType type, Element root) {
        Element classElement = new Element("class", NS_XMLVM);
        classElement.setAttribute("name", type.getClassType().getClassName());
        root.addContent(classElement);
        return classElement;
    }

    /**
     * Creates an XMLVM element for the given method and appends it to the given
     * class element.
     * <p>
     * This method is roughly based on
     * {@link CfTranslator#translate(String, byte[], com.android.dx.dex.cf.CfOptions)}
     * 
     * @param method
     *            the method to create the element for
     * @param classElement
     *            the class element to append the generated element to
     * @param cf
     *            the class file where this method was originally defined in
     */
    private static void processMethod(Method method, DirectClassFile cf, Element classElement) {
        final boolean localInfo = true;
        final int positionInfo = PositionList.NONE;

        CstMethodRef meth = new CstMethodRef(method.getDefiningClass(), method.getNat());

        // Extract flags for this method.
        int accessFlags = method.getAccessFlags();
        boolean isStatic = AccessFlags.isStatic(accessFlags);
        boolean isPrivate = AccessFlags.isPrivate(accessFlags);
        boolean isNative = AccessFlags.isNative(accessFlags);
        boolean isAbstract = AccessFlags.isAbstract(accessFlags);
        boolean isConstructor = meth.isInstanceInit() || meth.isClassInit();

        // Create XMLVM element for this method
        Element methodElement = new Element("method");
        methodElement.setAttribute("name", method.getName().getString());
        classElement.addContent(methodElement);

        // Set class attributes.
        setAttributeIfTrue(methodElement, "isStatic", isStatic);
        setAttributeIfTrue(methodElement, "isPrivate", isPrivate);
        setAttributeIfTrue(methodElement, "isNative", isNative);
        setAttributeIfTrue(methodElement, "isAbstract", isAbstract);
        setAttributeIfTrue(methodElement, "isConstructor", isConstructor);
        setAttributeIfTrue(methodElement, "isStatic", isStatic);

        // Create signature element.
        Element signatureElement = new Element("signature", NS_XMLVM);
        methodElement.addContent(signatureElement);
        // TODO(Sascha): Add return type.

        // Create code element.
        Element codeElement = new Element("code", NS_XMLVM);
        codeElement.setAttribute("language", "DEX");
        methodElement.addContent(codeElement);

        if (isNative || isAbstract) {
            // There's no code for native or abstract methods.
        } else {
            ConcreteMethod concrete = new ConcreteMethod(method, cf,
                    (positionInfo != PositionList.NONE), localInfo);

            TranslationAdvice advice = DexTranslationAdvice.THE_ONE;

            RopMethod rmeth = Ropper.convert(concrete, advice);
            int paramSize = meth.getParameterWordCount(isStatic);

            String canonicalName = method.getDefiningClass().getClassType().getDescriptor() + "."
                    + method.getName().getString();
            System.out.println("\n\nMethod: " + canonicalName);

            // Optimize
            rmeth = Optimizer.optimize(rmeth, paramSize, isStatic, localInfo, advice);

            LocalVariableInfo locals = null;

            if (localInfo) {
                locals = LocalVariableExtractor.extract(rmeth);
            }

            DalvCode code = RopTranslator.translate(rmeth, positionInfo, locals, paramSize);
            DalvCode.AssignIndicesCallback callback = new DalvCode.AssignIndicesCallback() {
                public int getIndex(Constant cst) {
                    // Everything is at index 0!
                    return 0;
                }
            };
            code.assignIndices(callback);

            DalvInsnList instructions = code.getInsns();
            for (int j = 0; j < instructions.size(); ++j) {
                processInstruction(instructions.get(j), codeElement, signatureElement);
            }
        }
    }

    /**
     * Creates an element for the given instruction and puts it into the given
     * code element. It is possible that no element is added for the given
     * instruction.
     * 
     * @param instruction
     *            the instruction to process
     * @param codeElement
     *            the element to add the instruction element to
     * @param signatureElement
     *            the signature element to add signature parameters to
     */
    private static void processInstruction(DalvInsn instruction, Element codeElement,
            Element signatureElement) {

        Element dexInstruction = null;
        if (instruction instanceof CodeAddress) {
            // Ignore.
        } else if (instruction instanceof LocalSnapshot) {
            // Ingore.
        } else if (instruction instanceof LocalStart) {
            LocalStart localStart = (LocalStart) instruction;
            dexInstruction = new Element("local-start", NS_DEX);
            dexInstruction.setAttribute("register", registerNameFormat(localStart.getLocal()
                    .regString()));
            dexInstruction.setAttribute("type", localStart.getLocal().getTypeBearer().toHuman());
            dexInstruction.setAttribute("argument", localStart.getLocal().getLocalItem().getName()
                    .toHuman());
            Element signatureParameterElement = new Element("param", NS_XMLVM);
            signatureParameterElement.setAttribute("type", localStart.getLocal().getTypeBearer()
                    .getType().toHuman());
            signatureElement.addContent(signatureParameterElement);
        } else if (instruction instanceof SimpleInsn) {
            SimpleInsn simpleInsn = (SimpleInsn) instruction;
            dexInstruction = new Element(sanitizeInstructionName(simpleInsn.getOpcode().getName()),
                    NS_DEX);

            RegisterSpecList registers = simpleInsn.getRegisters();
            for (int i = 0; i < registers.size(); ++i) {
                String type = registers.get(i).getTypeBearer().toHuman();
                String name = registerNameFormat(registers.get(i).regString());
                dexInstruction.setAttribute(name, type);
            }
        } else if (instruction instanceof CstInsn) {
            CstInsn cstInsn = (CstInsn) instruction;
            if (isInvokeInstruction(cstInsn)) {
                dexInstruction = processInvokeInstruction(cstInsn);
            } else {
                dexInstruction = new Element(
                        sanitizeInstructionName(cstInsn.getOpcode().getName()), NS_DEX);
                dexInstruction.setAttribute("type", cstInsn.getConstant().typeName());
                dexInstruction.setAttribute("value", cstInsn.getConstant().toHuman());
            }
            processRegisters(cstInsn.getRegisters(), dexInstruction);
        } else {
            System.out.print(">>> Unknown instruction: ");
            System.out.print("(" + instruction.getClass().getName() + ") ");
            System.out.print(instruction.listingString("", 0, true));
            return;
        }
        System.out.print("(" + instruction.getClass().getName() + ") ");
        System.out.print(instruction.listingString("", 0, true));
        if (dexInstruction != null) {
            codeElement.addContent(dexInstruction);
        }
    }

    /**
     * Takes the registers given and appends corresponding attributes to the
     * given element.
     */
    private static void processRegisters(RegisterSpecList registers, Element element) {
        for (int i = 0; i < registers.size(); ++i) {
            String type = registers.get(i).getTypeBearer().toHuman();
            String name = registerNameFormat(registers.get(i).regString());
            element.setAttribute(name, type);
        }
    }

    /**
     * Returns whether the given instruction is an invoke instruction that can
     * be handled by {@link #processInvokeInstruction(CstInsn)}.
     */
    private static boolean isInvokeInstruction(CstInsn cstInsn) {
        Dop[] invokeInstructions = { Dops.INVOKE_VIRTUAL, Dops.INVOKE_STATIC, Dops.INVOKE_DIRECT };
        for (Dop dop : invokeInstructions) {
            if (dop.equals(cstInsn.getOpcode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an element representing the given invoke instruction.
     */
    private static Element processInvokeInstruction(CstInsn cstInsn) {
        Element result = new Element(sanitizeInstructionName(cstInsn.getOpcode().getName()), NS_DEX);
        CstMethodRef methodRef = (CstMethodRef) cstInsn.getConstant();
        result.setAttribute("class-type", methodRef.getDefiningClass().toHuman());
        result.setAttribute("method", methodRef.getNat().getName().toHuman());
        result.addContent(processSignature(methodRef));
        return result;
    }

    /**
     * Processes the signature of the given method reference and returns a
     * corresponding element.
     */
    private static Element processSignature(CstMethodRef methodRef) {
        Element result = new Element("signature", NS_XMLVM);
        // TODO(Sascha): Implement
        return result;
    }

    private static String sanitizeInstructionName(String rawName) {
        return rawName.replaceAll("/", "-");
    }

    /**
     * Sets the given attribute in the given element if the value is true only.
     * Otherwise, nothing changes.
     */
    private static void setAttributeIfTrue(Element element, String attributeName, boolean value) {
        if (value) {
            element.setAttribute(attributeName, Boolean.toString(value));
        }
    }

    /**
     * Normalizes the register format given (like v0, v2, v3) to the form of
     * reg0, reg, reg2.
     * 
     * @param vFormat
     *            the register name in v-format
     * @return the normalized register name in reg-format
     */
    private static String registerNameFormat(String vFormat) throws RuntimeException {
        if (!vFormat.startsWith("v")) {
            throw new RuntimeErrorException(new Error(
                    "Register name doesn't start with 'v' prefix: " + vFormat));
        }
        try {
            int registerNumber = Integer.parseInt(vFormat.substring(1));
            return REG_PREFIX + registerNumber;
        } catch (NumberFormatException ex) {
            throw new RuntimeErrorException(new Error(
                    "Couldn't extract register number from register name: " + vFormat, ex));
        }
    }

    /**
     * Converts a {@link Document} into XML text.
     */
    private String documentToString(Document document) {
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        StringWriter writer = new StringWriter();
        try {
            outputter.output(document, writer);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return writer.toString();
    }
}
