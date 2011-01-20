/* Copyright (c) 2002-2011 by XMLVM.org
 *
 * Project Info:  http://www.xmlvm.org
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.xmlvm.proc.out;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.RuntimeErrorException;

import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.xmlvm.Log;
import org.xmlvm.main.Arguments;
import org.xmlvm.main.Targets;
import org.xmlvm.proc.DelayedXmlvmSerializationProvider;
import org.xmlvm.proc.ResourceCache;
import org.xmlvm.proc.XmlvmProcess;
import org.xmlvm.proc.XmlvmProcessImpl;
import org.xmlvm.proc.XmlvmResource;
import org.xmlvm.proc.XmlvmResource.Type;
import org.xmlvm.proc.XmlvmResourceProvider;
import org.xmlvm.proc.in.InputProcess.ClassInputProcess;
import org.xmlvm.refcount.InstructionProcessor;
import org.xmlvm.refcount.ReferenceCounting;
import org.xmlvm.refcount.ReferenceCountingException;
import org.xmlvm.util.universalfile.FileSuffixFilter;
import org.xmlvm.util.universalfile.UniversalFile;
import org.xmlvm.util.universalfile.UniversalFileCreator;
import org.xmlvm.util.universalfile.UniversalFileFilter;

import com.android.dx.cf.attrib.AttRuntimeInvisibleAnnotations;
import com.android.dx.cf.attrib.BaseAnnotations;
import com.android.dx.cf.code.ConcreteMethod;
import com.android.dx.cf.code.Ropper;
import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.direct.StdAttributeFactory;
import com.android.dx.cf.iface.AttributeList;
import com.android.dx.cf.iface.Field;
import com.android.dx.cf.iface.FieldList;
import com.android.dx.cf.iface.Method;
import com.android.dx.cf.iface.MethodList;
import com.android.dx.cf.iface.ParseException;
import com.android.dx.dex.cf.CfTranslator;
import com.android.dx.dex.code.ArrayData;
import com.android.dx.dex.code.CatchHandlerList;
import com.android.dx.dex.code.CatchTable;
import com.android.dx.dex.code.CatchTable.Entry;
import com.android.dx.dex.code.CodeAddress;
import com.android.dx.dex.code.CstInsn;
import com.android.dx.dex.code.DalvCode;
import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.DalvInsnList;
import com.android.dx.dex.code.Dop;
import com.android.dx.dex.code.Dops;
import com.android.dx.dex.code.HighRegisterPrefix;
import com.android.dx.dex.code.LocalSnapshot;
import com.android.dx.dex.code.LocalStart;
import com.android.dx.dex.code.OddSpacer;
import com.android.dx.dex.code.PositionList;
import com.android.dx.dex.code.RopTranslator;
import com.android.dx.dex.code.SimpleInsn;
import com.android.dx.dex.code.SwitchData;
import com.android.dx.dex.code.TargetInsn;
import com.android.dx.rop.annotation.Annotation;
import com.android.dx.rop.code.AccessFlags;
import com.android.dx.rop.code.DexTranslationAdvice;
import com.android.dx.rop.code.LocalVariableExtractor;
import com.android.dx.rop.code.LocalVariableInfo;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.code.TranslationAdvice;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstMemberRef;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.cst.CstUtf8;
import com.android.dx.rop.cst.TypedConstant;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.TypeList;
import com.android.dx.ssa.Optimizer;
import com.android.dx.util.ExceptionWithContext;
import com.android.dx.util.IntList;

/**
 * This OutputProcess emits XMLVM code containing register-based DEX
 * instructions (XMLVM-DEX).
 * <p>
 * Android's own DX compiler tool is used to parse class files and to create the
 * register-based DEX code in-memory which is then converted to XML.
 */
public class DEXmlvmOutputProcess extends XmlvmProcessImpl<XmlvmProcess<?>> implements
        XmlvmResourceProvider {

    /**
     * Pair of type name and its super type name.
     */
    private static class TypePlusSuperType {
        public final String typeName;
        public final String superTypeName;


        public TypePlusSuperType(String typeName, String superTypeName) {
            this.typeName = typeName;
            this.superTypeName = superTypeName;
        }
    }


    /**
     * A little helper class that contains package- and class name.
     */
    private static class PackagePlusClassName {
        public String packageName = "";
        public String className   = "";


        public PackagePlusClassName(String className) {
            this.className = className;
        }

        public PackagePlusClassName(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
        }

        @Override
        public String toString() {
            if (packageName.isEmpty()) {
                return className;
            } else {
                return packageName + "." + className;
            }
        }
    }


    /**
     * Little helper class for keeping a target address and the info about
     * whether this target should split a try-catch block.
     */
    private static class Target {
        int     address;
        boolean requiresSplit;


        public Target(int address, boolean requiresSplit) {
            this.address = address;
            this.requiresSplit = requiresSplit;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Target) {
                Target otherTarget = (Target) obj;
                return this.address == otherTarget.address;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return address;
        }
    }


    private static final String                     TAG                     = DEXmlvmOutputProcess.class
                                                                                    .getSimpleName();
    private static final boolean                    LOTS_OF_DEBUG           = false;
    private static final boolean                    REF_LOGGING             = true;

    private static final String                     JLO                     = "java.lang.Object";
    private static final String                     DEXMLVM_ENDING          = ".dexmlvm";
    private static final Namespace                  NS_XMLVM                = XmlvmResource.nsXMLVM;
    private static final Namespace                  NS_DEX                  = Namespace
                                                                                    .getNamespace(
                                                                                            "dex",
                                                                                            "http://xmlvm.org/dex");
    private static final String                     BIN_PROXIES_PATH        = "bin-proxies";
    private static final String                     BIN_PROXIES_ONEJAR_PATH = "/lib/proxies-java.jar";
    private static final UniversalFile              RED_LIST_FILE           = UniversalFileCreator
                                                                                    .createFile(
                                                                                            "/redlist.txt",
                                                                                            "lib/redlist.txt");

    private static final Map<String, UniversalFile> proxies                 = initializeProxies();
    private static final long                       proxiesLastModified     = getLastModifiedProxy();

    /**
     * Green classes are classes that are OK to translate. Red classes are
     * excluded from the compilation.
     */
    private static Set<String>                      redTypes                = null;

    private List<OutputFile>                        outputFiles             = new ArrayList<OutputFile>();
    private List<XmlvmResource>                     generatedResources      = new ArrayList<XmlvmResource>();

    private Element                                 lastDexInstruction      = null;

    private ResourceCache                           cache                   = ResourceCache
                                                                                    .getCache(DEXmlvmOutputProcess.class
                                                                                            .getName());
    private List<OutputFile>                        filesFromCache          = new ArrayList<OutputFile>();


    public DEXmlvmOutputProcess(Arguments arguments) {
        this(arguments, true);
    }

    public DEXmlvmOutputProcess(Arguments arguments, boolean useRedList) {
        super(arguments);

        // We can either read class files directly or use the
        // JavaByteCodeOutputProcess to use generated bytecode as the input.
        addSupportedInput(ClassInputProcess.class);
        addSupportedInput(JavaByteCodeOutputProcess.class);

        // Red type elimination should only be performed when load_dependencies
        // is enabled or we are generating c wrappers.
        if (redTypes == null
                && useRedList
                && (arguments.option_load_dependencies() && !arguments
                        .option_disable_load_dependencies())
                || arguments.option_target() == Targets.GENCWRAPPERS) {
            redTypes = initializeRedList(RED_LIST_FILE, proxies.keySet());
        }
    }

    @Override
    public List<OutputFile> getOutputFiles() {
        return outputFiles;
    }

    @Override
    public boolean process() {
        generatedResources.clear();
        for (XmlvmProcess<?> preprocess : preprocess()) {
            for (OutputFile preOutputFile : preprocess.getOutputFiles()) {
                String resourceName = preOutputFile.getOrigin();
                long lastModified = preOutputFile.getLastModified();

                // If the proxies change, we invalidate the cache.
                lastModified = Math.max(lastModified, proxiesLastModified);

                OutputFile outputFile = null;

                // Check whether we can get the file from memory or disk cache.
                if (!arguments.option_no_cache() && cache.contains(resourceName, lastModified)) {
                    Log.debug(TAG, "Getting resource from cache: " + resourceName);
                    outputFile = new OutputFile(cache.get(resourceName, lastModified), lastModified);
                    filesFromCache.add(outputFile);
                } else {
                    outputFile = generateDEXmlvmFile(preOutputFile);
                    if (outputFile != null && !arguments.option_no_cache()) {
                        cache.put(resourceName, lastModified, outputFile.getDataAsBytes());
                    }
                }
                if (outputFile != null) {
                    outputFile.setOrigin(preOutputFile.getOrigin());
                    outputFiles.add(outputFile);
                }
            }
        }
        return true;
    }

    @Override
    public List<XmlvmResource> getXmlvmResources() {
        addResourcesFromCachedFiles();
        return generatedResources;
    }

    private void addResourcesFromCachedFiles() {
        for (OutputFile cachedFile : filesFromCache) {
            generatedResources.add(XmlvmResource.fromFile(cachedFile));
        }
    }

    private OutputFile generateDEXmlvmFile(final OutputFile classFile) {
        return generateDEXmlvmFile(classFile, false);
    }

    @SuppressWarnings("unchecked")
    private OutputFile generateDEXmlvmFile(final OutputFile classFile, boolean proxy) {
        Log.debug(TAG, "DExing:" + classFile.getFileName());

        DirectClassFile directClassFile = new DirectClassFile(classFile.getDataAsBytes(),
                classFile.getFileName(), false);
        directClassFile.setAttributeFactory(StdAttributeFactory.THE_ONE);
        try {
            directClassFile.getMagic();
        } catch (ParseException ex) {
            Log.debug(TAG, "Could not parse class.");
            return null;
        }

        String packagePlusClassName = directClassFile.getThisClass().getClassType().toHuman();

        // If a proxy exists for this type, we use it instead of the file we got
        // originally.
        if (!proxy && proxies.containsKey(packagePlusClassName)) {
            UniversalFile proxyUniversalFile = proxies.get(packagePlusClassName);
            OutputFile proxyResult = new OutputFile(proxyUniversalFile);
            proxyResult.setFileName(classFile.getFileName());
            return generateDEXmlvmFile(proxyResult, true);
        }

        // We want to prevent "red" classes from being loaded. If the there is a
        // green class list, and this process is run by a library loaded, then
        // we expect the class to be a library class. Hence, it must be in the
        // green class list. If it's not, we discard it.
        if (isRedType(packagePlusClassName)) {
            return null;
        }

        // This is for auxiliary analysis. We record all the types that are
        // referenced.
        Set<String> referencedTypes = new HashSet<String>();
        final Document document = createDocument();

        TypePlusSuperType type = process(directClassFile, document.getRootElement(),
                referencedTypes);
        String className = type.typeName.replace('.', '_');

        String jClassName = document.getRootElement().getChild("class", InstructionProcessor.vm)
                .getAttributeValue("name");

        List<Element> methods = (List<Element>) document.getRootElement()
                .getChild("class", InstructionProcessor.vm)
                .getChildren("method", InstructionProcessor.vm);

        if (arguments.option_enable_ref_counting()) {
            if (REF_LOGGING) {
                Log.debug(TAG + "-ref", "Processing class: " + jClassName);
            }

            // We now need to mark up the code with retains/releases.
            ReferenceCounting refCounting = new ReferenceCounting();
            for (Element e : methods) {
                if (REF_LOGGING) {
                    Log.debug(TAG + "-ref", "Processing method: " + e.getAttributeValue("name"));
                }

                try {
                    refCounting.process(e);
                } catch (ReferenceCountingException ex) {
                    Log.error(TAG + "-ref", "Processing method: " + e.getAttributeValue("name"));
                    Log.error(TAG + "-ref", "Failed while processing: " + ex.getMessage() + " in "
                            + jClassName);
                    return null;
                } catch (DataConversionException ex) {
                    Log.error(TAG + "-ref", "Processing method: " + e.getAttributeValue("name"));
                    Log.error(TAG + "-ref", "Failed while processing: " + ex.getMessage() + " in "
                            + jClassName);
                    return null;
                }
                if (REF_LOGGING) {
                    Log.debug(TAG + "-ref", "Done with " + e.getAttributeValue("name"));
                }
            }

            if (REF_LOGGING) {
                Log.debug(TAG + "-ref", "Done processing methods!");
            }
        }

        // Make sure no red classes are in the list of referenced types.
        Set<String> filteredReferencesTypes = new HashSet<String>();
        for (String referencedType : referencedTypes) {
            if (!isRedType(referencedType)) {
                filteredReferencesTypes.add(referencedType);
            }
        }
        addReferences(document, filteredReferencesTypes);
        generatedResources.add(new XmlvmResource(Type.DEX, document));
        String fileName = className + DEXMLVM_ENDING;

        // Some processes depending on this processor don't actually need the
        // String version of the DEXMLVM files. As generating them takes some
        // time, we want to make sure we only generate it when necessary.
        OutputFile result = new OutputFile(new DelayedXmlvmSerializationProvider(document));

        result.setLocation(arguments.option_out());
        result.setFileName(fileName);

        return result;
    }

    /**
     * Adds the given set of references to the given XMLVM document.
     */
    private static void addReferences(Document xmlvmDocument, Collection<String> referencedTypes) {
        Element references = new Element("references", NS_XMLVM);
        for (String referencedType : referencedTypes) {
            Element reference = new Element("reference", NS_XMLVM);
            reference.setAttribute("name", referencedType);
            references.addContent(reference);
        }
        xmlvmDocument.getRootElement().addContent(references);
    }

    /**
     * Returns whether the given class is a red class. This method will return
     * false, if the config file has not been provided.
     * 
     * @param packagePlusClassName
     *            e.g. "java.lang.Object".
     * @return whether the class is a red class, that should be avoided.
     */
    private boolean isRedType(String packagePlusClassName) {
        // In case packagePlusClassName is an array, perform the red-class-test
        // on the base type
        int i = packagePlusClassName.indexOf('[');
        String baseType = i == -1 ? packagePlusClassName : packagePlusClassName.substring(0, i);
        return redTypes != null && redTypes.contains(baseType);
    }

    private static Set<String> initializeRedList(UniversalFile redListFile, Set<String> proxies) {
        try {
            Set<String> result = new HashSet<String>();
            BufferedReader reader;
            reader = new BufferedReader(new StringReader(redListFile.getFileAsString()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }

            // Red types might be replaced by proxies, in which case they are
            // no longer red.
            for (String proxyTypeName : proxies) {
                result.remove(proxyTypeName);
            }
            return result;
        } catch (IOException e) {
            Log.error(
                    TAG,
                    "Problem reading red file: " + redListFile.getAbsolutePath() + ": "
                            + e.getMessage());
        }
        return null;
    }

    private static Map<String, UniversalFile> initializeProxies() {
        Map<String, UniversalFile> result = new HashMap<String, UniversalFile>();
        UniversalFile basePath = UniversalFileCreator.createDirectory(BIN_PROXIES_ONEJAR_PATH,
                BIN_PROXIES_PATH);

        final String classEnding = ".class";
        UniversalFileFilter classFilter = new FileSuffixFilter(classEnding);
        for (UniversalFile proxyFile : basePath.listFilesRecursively(classFilter)) {
            String proxyFileName = proxyFile.getRelativePath(basePath.getAbsolutePath());
            String proxyTypeName = proxyFileName.substring(0,
                    proxyFileName.length() - (classEnding.length())).replace(File.separatorChar,
                    '.');
            result.put(proxyTypeName, proxyFile);
        }
        return result;
    }

    private static long getLastModifiedProxy() {
        long result = 0;
        for (UniversalFile proxyFile : proxies.values()) {
            long lastModified = proxyFile.getLastModified();
            if (lastModified > result) {
                result = lastModified;
            }
        }
        return result;
    }

    /**
     * Converts a class name in the form of a/b/C into a
     * {@link PackagePlusClassName} object.
     * 
     */
    private static PackagePlusClassName parseClassName(String packagePlusClassName) {
        int lastSlash = packagePlusClassName.lastIndexOf('/');
        if (lastSlash == -1) {
            return new PackagePlusClassName(packagePlusClassName);
        }

        String className = packagePlusClassName.substring(lastSlash + 1);
        String packageName = packagePlusClassName.substring(0, lastSlash).replace('/', '.');

        return new PackagePlusClassName(packageName, className);
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
     * @param referencedTypes
     *            will be filled with the types references in this class file
     * @return the class name for the DEXMLVM file
     */
    private TypePlusSuperType process(DirectClassFile cf, Element root, Set<String> referencedTypes) {
        boolean skeletonOnly = hasSkeletonOnlyAnnotation(cf.getAttributes())
                && !arguments.option_gen_wrapper();
        Element classElement = processClass(cf, root, referencedTypes);
        processFields(cf.getFields(), classElement, skeletonOnly);

        MethodList methods = cf.getMethods();
        int sz = methods.size();

        for (int i = 0; i < sz; i++) {
            Method one = methods.get(i);

            if (hasIgnoreAnnotation(one.getAttributes())) {
                // If this method has the @XMLVMIgnore annotation, we just
                // simply ignore it.
                continue;
            }

            if (skeletonOnly
                    && (one.getAccessFlags() & (AccessFlags.ACC_PRIVATE | AccessFlags.ACC_SYNTHETIC)) != 0) {
                // We only want to generate skeletons. This method is private or
                // synthetic so simply ignore it.
                continue;
            }

            try {
                processMethod(one, cf, classElement, referencedTypes, skeletonOnly);
            } catch (RuntimeException ex) {
                String msg = "...while processing " + one.getName().toHuman() + " "
                        + one.getDescriptor().toHuman();
                throw ExceptionWithContext.withContext(ex, msg);
            }
        }

        String className = classElement.getAttributeValue("name");
        String superClassName = classElement.getAttributeValue("extends");
        return new TypePlusSuperType(className, superClassName);
    }

    /**
     * Creates an XMLVM element for the given type and appends it to the given
     * root element.
     * 
     * @param cf
     *            the {@link DirectClassFile} instance of this class
     * @param root
     *            the root element to append the generated element to
     * @param referencedTypes
     *            will be filled with the types references in this class file
     * @return the generated element
     */
    private static Element processClass(DirectClassFile cf, Element root,
            Set<String> referencedTypes) {
        Element classElement = new Element("class", NS_XMLVM);
        CstType type = cf.getThisClass();
        PackagePlusClassName parsedClassName = parseClassName(type.getClassType().getClassName());
        classElement.setAttribute("name", parsedClassName.className);
        classElement.setAttribute("package", parsedClassName.packageName);
        String superClassName = "";

        // This can happen for java.lang.Object.
        if (cf.getSuperclass() != null) {
            superClassName = parseClassName(cf.getSuperclass().getClassType().getClassName())
                    .toString();
        }

        classElement.setAttribute("extends", superClassName);
        referencedTypes.add(superClassName);

        processAccessFlags(cf.getAccessFlags(), classElement);

        TypeList interfaces = cf.getInterfaces();
        if (interfaces.size() > 0) {
            String interfaceList = "";
            for (int i = 0; i < interfaces.size(); ++i) {
                if (i > 0) {
                    interfaceList += ",";
                }
                String interfaceName = parseClassName(interfaces.getType(i).getClassName())
                        .toString();
                interfaceList += interfaceName;
                referencedTypes.add(interfaceName);
            }
            classElement.setAttribute("interfaces", interfaceList);
        }

        root.addContent(classElement);
        return classElement;
    }

    /**
     * Processes the fields and adds corresponding elements to the class
     * element.
     * 
     * @param skeletonOnly
     */
    private void processFields(FieldList fieldList, Element classElement, boolean skeletonOnly) {
        for (int i = 0; i < fieldList.size(); ++i) {
            Field field = fieldList.get(i);
            if (hasIgnoreAnnotation(field.getAttributes())) {
                // If this field has the @XMLVMIgnore annotation, we just
                // simply ignore it.
                continue;
            }
            if (skeletonOnly
                    && (field.getAccessFlags() & (AccessFlags.ACC_PRIVATE | AccessFlags.ACC_SYNTHETIC)) != 0) {
                // This field is private or synthetic and we want to generate
                // only a skeleton, so we just simply ignore it.
                continue;
            }
            Element fieldElement = new Element("field", NS_XMLVM);
            fieldElement.setAttribute("name", field.getName().toHuman());
            String fieldType = field.getNat().getFieldType().toHuman();
            if (isRedType(fieldType)) {
                fieldType = JLO;
            }
            fieldElement.setAttribute("type", fieldType);
            TypedConstant value = field.getConstantValue();
            if (value != null) {
                fieldElement.setAttribute("value", value.toHuman());
            }
            processAccessFlags(field.getAccessFlags(), fieldElement);
            classElement.addContent(fieldElement);
        }
    }

    /**
     * Debugging use: Builds a catch-table in XML.
     */
    private void processCatchTable(CatchTable catchTable, Element codeElement) {
        if (catchTable.size() == 0) {
            return;
        }

        Element catchTableElement = new Element("catches", NS_DEX);

        for (int i = 0; i < catchTable.size(); ++i) {
            Entry entry = catchTable.get(i);
            Element entryElement = new Element("entry", NS_DEX);
            entryElement.setAttribute("start", String.valueOf(entry.getStart()));
            entryElement.setAttribute("end", String.valueOf(entry.getEnd()));

            CatchHandlerList catchHandlers = entry.getHandlers();
            for (int j = 0; j < catchHandlers.size(); ++j) {
                com.android.dx.dex.code.CatchHandlerList.Entry handlerEntry = catchHandlers.get(j);
                String exceptionType = handlerEntry.getExceptionType().toHuman();

                // We can remove the exception because a red type exception
                // will never be created or thrown.
                // This change is in sync with the one in processMethod.
                if (!isRedType(exceptionType)) {
                    Element handlerElement = new Element("handler", NS_DEX);
                    handlerElement.setAttribute("type", exceptionType);
                    handlerElement
                            .setAttribute("target", String.valueOf(handlerEntry.getHandler()));
                    entryElement.addContent(handlerElement);
                }
            }
            catchTableElement.addContent(entryElement);
        }

        codeElement.addContent(catchTableElement);
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
     * @param referencedTypes
     *            will be filled with the types references in this class file
     */
    private void processMethod(Method method, DirectClassFile cf, Element classElement,
            Set<String> referencedTypes, boolean skeletonOnly) {
        final boolean localInfo = true;
        final int positionInfo = PositionList.LINES;

        CstMethodRef meth = new CstMethodRef(method.getDefiningClass(), method.getNat());

        // Extract flags for this method.
        int accessFlags = method.getAccessFlags();
        if (skeletonOnly) {
            // Only generate a skeleton for this class. We do this by
            // artificially making the method abstract.
            accessFlags |= AccessFlags.ACC_ABSTRACT;
        }
        boolean isNative = AccessFlags.isNative(accessFlags);
        boolean isStatic = AccessFlags.isStatic(accessFlags);
        boolean isAbstract = AccessFlags.isAbstract(accessFlags);

        // Create XMLVM element for this method
        Element methodElement = new Element("method", NS_XMLVM);
        methodElement.setAttribute("name", method.getName().getString());
        classElement.addContent(methodElement);

        // Set the access flag attrobutes for this method.
        processAccessFlags(accessFlags, methodElement);

        // Create signature element.
        methodElement.addContent(processSignature(meth, referencedTypes));

        // Create code element.
        Element codeElement = new Element("code", NS_DEX);
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
            if (LOTS_OF_DEBUG) {
                System.out.println("\n\nMethod: " + canonicalName);
            }

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
            codeElement.setAttribute("register-size",
                    String.valueOf(instructions.getRegistersSize()));
            processLocals(instructions.getRegistersSize(), isStatic,
                    parseClassName(cf.getThisClass().getClassType().getClassName()).toString(),
                    meth.getPrototype().getParameterTypes(), codeElement);
            Map<Integer, SwitchData> switchDataBlocks = extractSwitchData(instructions);
            Map<Integer, ArrayData> arrayData = extractArrayData(instructions);
            CatchTable catches = code.getCatches();
            processCatchTable(catches, codeElement);
            Map<Integer, Target> targets = extractTargets(instructions, catches);

            // For each entry in the catch table, we create a try-catch element,
            // including the try and all the catch children and append it to the
            // code element. We store the try elements in a list, in order to
            // append the matching instructions to them as they are processed.
            List<Element> tryElements = new ArrayList<Element>();
            Map<Integer, Element> tryCatchElements = new HashMap<Integer, Element>();
            for (int i = 0; i < catches.size(); ++i) {
                Element tryCatchElement = new Element("try-catch", NS_DEX);
                Element tryElement = new Element("try", NS_DEX);
                tryCatchElement.addContent(tryElement);
                tryElements.add(tryElement);

                // For each handler create a catch element as the child of the
                // try-catch element.
                CatchHandlerList handlers = catches.get(i).getHandlers();
                for (int j = 0; j < handlers.size(); ++j) {
                    String exceptionType = handlers.get(j).getExceptionType().toHuman();

                    // We can remove the exception because a red type exception
                    // will never be created or thrown.
                    // This change is in sync with the one in processCatchTable
                    if (!isRedType(exceptionType)) {
                        Element catchElement = new Element("catch", NS_DEX);
                        catchElement.setAttribute("exception-type", exceptionType);
                        catchElement.setAttribute("target",
                                String.valueOf(handlers.get(j).getHandler()));
                        tryCatchElement.addContent(catchElement);
                    }
                }
                tryCatchElements.put(catches.get(i).getStart(), tryCatchElement);
            }

            Element lastTryCatchElement = null;

            // Used inside processInstruction to mark source file lines as
            // already added, so they don't get added twice.
            List<Integer> sourceLinesAlreadyPut = new ArrayList<Integer>();
            // Process every single instruction of this method. Either add it do
            // the main code element, or to a try-catch block.
            for (int i = 0; i < instructions.size(); ++i) {
                Element instructionParent = codeElement;
                DalvInsn instruction = instructions.get(i);
                int address = instruction.getAddress();

                // Determine whether to add the next instruction to the
                // codeElement or to a try block.
                Entry currentCatch = null;
                int tryElementIndex = 0;
                for (tryElementIndex = 0; tryElementIndex < catches.size(); ++tryElementIndex) {
                    if (isInstructionInCatchRange(instruction, catches.get(tryElementIndex))) {
                        instructionParent = tryElements.get(tryElementIndex);
                        currentCatch = catches.get(tryElementIndex);
                        break;
                    }
                }

                // Adds a label element for each target we extracted earlier.
                if (targets.containsKey(address)) {
                    Element labelElement = new Element("label", NS_DEX);
                    labelElement.setAttribute("id", String.valueOf(address));

                    if (currentCatch != null) {
                        // Labels at the beginning of a try block need to be
                        // moved in front of it.
                        if (currentCatch.getStart() == address) {
                            codeElement.addContent(labelElement);
                        } else if (targets.get(address).requiresSplit) {
                            // If we got here, it means that there is a target,
                            // that is a catch-handler target and it is inside a
                            // try block. We have to avoid this. So the way we
                            // solve it is by splitting up the try block into
                            // two, and adding the label in between.

                            // First, add the label to the codeElement, so that
                            // it is outside the try-catch block.
                            codeElement.addContent(labelElement);

                            // Then, make a copy of the previous try-catch
                            // block, make sure its try block is empty and add
                            // it. Then replace the previous try element in the
                            // list so the next instructions can be added to it
                            // instead of the previous one.
                            Element secondTryCatchElement = (Element) lastTryCatchElement.clone();
                            Element secondTry = secondTryCatchElement.getChild("try", NS_DEX);
                            secondTry.removeContent();
                            codeElement.addContent(secondTryCatchElement);
                            tryElements.set(tryElementIndex, secondTry);
                        } else {
                            instructionParent.addContent(labelElement);
                        }
                    } else {
                        instructionParent.addContent(labelElement);
                    }
                    targets.remove(address);
                }

                // Position the try-catch elements correctly inside the
                // codeElement.
                if (tryCatchElements.containsKey(address)) {
                    Element tryCatchElement = tryCatchElements.get(address);
                    codeElement.addContent(tryCatchElement);
                    tryCatchElements.remove(address);
                    lastTryCatchElement = tryCatchElement;
                }

                processInstruction(instruction, instructionParent, switchDataBlocks, arrayData,
                        sourceLinesAlreadyPut, referencedTypes);
            }
        }
    }

    /**
     * Returns whether the given instruction is part of the given catch block.
     */
    private static boolean isInstructionInCatchRange(DalvInsn instruction, Entry catchEntry) {
        return instruction.getAddress() >= catchEntry.getStart()
                && instruction.getAddress() < catchEntry.getEnd();
    }

    /**
     * Sets attributes in the element according to the access flags given.
     */
    private static void processAccessFlags(int accessFlags, Element element) {
        boolean isStatic = AccessFlags.isStatic(accessFlags);
        boolean isPrivate = AccessFlags.isPrivate(accessFlags);
        boolean isPublic = AccessFlags.isPublic(accessFlags);
        boolean isNative = AccessFlags.isNative(accessFlags);
        boolean isAbstract = AccessFlags.isAbstract(accessFlags);
        boolean isSynthetic = AccessFlags.isSynthetic(accessFlags);
        boolean isInterface = AccessFlags.isInterface(accessFlags);

        setAttributeIfTrue(element, "isStatic", isStatic);
        setAttributeIfTrue(element, "isPrivate", isPrivate);
        setAttributeIfTrue(element, "isPublic", isPublic);
        setAttributeIfTrue(element, "isNative", isNative);
        setAttributeIfTrue(element, "isAbstract", isAbstract);
        setAttributeIfTrue(element, "isSynthetic", isSynthetic);
        setAttributeIfTrue(element, "isInterface", isInterface);
    }

    /**
     * Adds local {@code var} elements to the {@code code} element for each
     * parameter and the {@code this} reference, if applicable.
     */
    private void processLocals(int registerSize, boolean isStatic, String classType,
            StdTypeList parameterTypes, Element codeElement) {

        // The parameters are stored in the last N registers.
        // If the method is not static, the reference to "this" is stored right
        // before the parameters.
        List<Element> varElements = new ArrayList<Element>();

        // We go through the list of parameters backwards, as we need to change
        // the indexes, depending on whether we find category 2 types. In the
        // end, the list is reverted.
        int j = 0;
        for (int i = parameterTypes.size() - 1; i >= 0; --i, ++j) {
            com.android.dx.rop.type.Type paramType = parameterTypes.get(i);
            Element varElement = new Element("var", NS_DEX);
            if (paramType.isCategory2()) {
                j++;
            }
            varElement.setAttribute("name", "var-register-" + (registerSize - 1 - j));
            varElement.setAttribute("register", String.valueOf(registerSize - 1 - j));
            varElement.setAttribute("param-index", String.valueOf(i));
            String localsType = paramType.getType().toHuman();

            // For red locals, we set them to object.
            if (isRedType(localsType)) {
                localsType = JLO;
            }
            varElement.setAttribute("type", localsType);
            varElements.add(varElement);
        }

        // Add the 'this' reference right before the parameters, if the method
        // is not static.
        if (!isStatic) {
            Element thisVarElement = new Element("var", NS_DEX);
            thisVarElement.setAttribute("name", "this");
            thisVarElement.setAttribute("register", String.valueOf(registerSize - j - 1));
            thisVarElement.setAttribute("type", classType);
            varElements.add(thisVarElement);
        }

        // Reverse the list and append it to the code element.
        Collections.reverse(varElements);
        for (Element varElement : varElements) {
            codeElement.addContent(varElement);
        }
    }

    /**
     * Extracts targets that are being jumped to, so we can later add labels at
     * the corresponding positions when generating the code.
     * 
     * @return a set containing the addresses of all jump targets
     */
    private static Map<Integer, Target> extractTargets(DalvInsnList instructions, CatchTable catches) {
        Map<Integer, Target> targets = new HashMap<Integer, Target>();

        // First, add non-catch targets.
        for (int i = 0; i < instructions.size(); ++i) {
            // If the target is generic, we have to assume it might jump into a
            // catch block, so we require splitting.
            if (instructions.get(i) instanceof TargetInsn) {
                TargetInsn targetInsn = (TargetInsn) instructions.get(i);
                targets.put(targetInsn.getTargetAddress(), new Target(
                        targetInsn.getTargetAddress(), true));
            } else if (instructions.get(i) instanceof SwitchData) {
                // Switches should always be within the same block, no no
                // splitting should be required.
                SwitchData switchData = (SwitchData) instructions.get(i);
                CodeAddress[] caseTargets = switchData.getTargets();
                for (CodeAddress caseTarget : caseTargets) {
                    targets.put(caseTarget.getAddress(), new Target(caseTarget.getAddress(), false));
                }
            }
        }

        // Then, add all catch-handler targets. We need this info, so using
        // Map.put will potentially override an existing target, so the
        // information about a potential catch-handler target is not lost.
        for (int i = 0; i < catches.size(); ++i) {
            CatchHandlerList handlers = catches.get(i).getHandlers();
            for (int j = 0; j < handlers.size(); ++j) {
                int handlerAddress = handlers.get(j).getHandler();
                targets.put(handlerAddress, new Target(handlerAddress, true));
            }
        }

        return targets;
    }

    /**
     * Extracts all {@link SwitchData} pseudo-instructions from the given list
     * of instructions.
     * 
     * @param instructions
     *            the list of instructions from where to extract
     * @return a map containing all found {@link SwitchData} instructions,
     *         indexed by address.
     */
    private static Map<Integer, SwitchData> extractSwitchData(DalvInsnList instructions) {
        Map<Integer, SwitchData> result = new HashMap<Integer, SwitchData>();
        for (int i = 0; i < instructions.size(); ++i) {
            if (instructions.get(i) instanceof SwitchData) {
                SwitchData switchData = (SwitchData) instructions.get(i);
                result.put(switchData.getAddress(), switchData);
            }
        }
        return result;
    }

    /**
     * Extracts all {@link ArrayData} pseudo-instructions from the given list of
     * instructions.
     * 
     * @param instructions
     *            the list of instructions from where to extract
     * @return a map containing all found {@link ArrayData} instructions,
     *         indexed by address.
     */
    private static Map<Integer, ArrayData> extractArrayData(DalvInsnList instructions) {
        Map<Integer, ArrayData> result = new HashMap<Integer, ArrayData>();
        for (int i = 0; i < instructions.size(); ++i) {
            if (instructions.get(i) instanceof ArrayData) {
                ArrayData arrayData = (ArrayData) instructions.get(i);
                result.put(arrayData.getAddress(), arrayData);
            }
        }
        return result;
    }

    /**
     * Creates an element for the given instruction and puts it into the given
     * code element. It is possible that no element is added for the given
     * instruction.
     * 
     * @param instruction
     *            the instruction to process
     * @param parentElement
     *            the element to add the instruction element to
     * @param switchDataBlocks
     *            the switch data blocks
     * @param arrayData
     *            the array data
     * @param sourceLinesAlreadyPut
     *            a bin for putting used source lines number in.
     * @param referencedTypes
     *            will be filled with the types references in this class file
     */
    private void processInstruction(DalvInsn instruction, Element parentElement,
            Map<Integer, SwitchData> switchDataBlocks, Map<Integer, ArrayData> arrayData,
            List<Integer> sourceLinesAlreadyPut, Set<String> referencedTypes) {
        Element dexInstruction = null;
        String opname = instruction.getOpcode().getName();

        if (opname.equals("instance-of") || opname.equals("const-class")) {
            CstInsn isaInsn = (CstInsn) instruction;
            referencedTypes.add(isaInsn.getConstant().toHuman());
        }
        RegisterSpecList registers = instruction.getRegisters();
        for (int i = 0; i < registers.size(); ++i) {
            RegisterSpec register = registers.get(i);
            String descriptor = register.getType().getDescriptor();
            String registerType = register.getType().toHuman();
            // Sometimes a register type name starts with some info about the
            // register. We need to cut this out.
            if (descriptor.startsWith("N")) {
                referencedTypes.add(registerType.substring(registerType.indexOf('L') + 1));
            } else {
                referencedTypes.add(registerType);
            }
        }

        if (instruction instanceof CodeAddress) {
            // We put debug information about source code positions into the
            // code so that we can control the debugger.
            SourcePosition sourcePosition = instruction.getPosition();
            CstUtf8 sourceFile = sourcePosition.getSourceFile();
            int sourceLine = sourcePosition.getLine();
            if (sourceFile != null && !sourceLinesAlreadyPut.contains(sourceLine)) {
                dexInstruction = new Element("source-position", NS_XMLVM);
                dexInstruction.setAttribute("file", sourceFile.toHuman());
                dexInstruction.setAttribute("line", String.valueOf(sourceLine));
                sourceLinesAlreadyPut.add(sourceLine);
            }
        } else if (instruction instanceof LocalSnapshot) {
            // Ignore.
        } else if (instruction instanceof OddSpacer) {
            // Ignore NOPs.
        } else if (instruction instanceof SwitchData) {
            // Ignore here because we already processes these and they were
            // given to this method as an argument.
        } else if (instruction instanceof LocalStart) {
            // As we extract the locals information up-front we don't need to
            // handle local-start.
        } else if (instruction instanceof ArrayData) {
            // Ignore here because we already processed these and they were
            // given to this method as an argument.
        } else if (instruction instanceof SimpleInsn) {
            SimpleInsn simpleInsn = (SimpleInsn) instruction;
            String instructionName = simpleInsn.getOpcode().getName();

            // If this is a move-result instruction, we don't add it
            // explicitly, but instead add the result register to the previous
            // invoke instruction's return.
            if (instructionName.startsWith("move-result")) {
                // Sanity Check
                if (simpleInsn.getRegisters().size() != 1) {
                    Log.error(TAG, "DEXmlvmOutputProcess: Register Size doesn't fit 'move-result'.");
                    System.exit(-1);
                }
                Element moveInstruction = new Element("move-result", NS_DEX);
                addRegistersAsAttributes(registers, moveInstruction);
                lastDexInstruction.addContent(moveInstruction);
            } else {
                dexInstruction = new Element(sanitizeInstructionName(instructionName), NS_DEX);
                addRegistersAsAttributes(registers, dexInstruction);

                // For simple instructions with only one register, we also add
                // the type of the register. This includes the return
                // instructions.
                if (registers.size() == 1) {
                    dexInstruction.setAttribute("class-type", registers.get(0).getType().toHuman());
                }
            }
        } else if (instruction instanceof CstInsn) {
            CstInsn cstInsn = (CstInsn) instruction;
            if (isInvokeInstruction(cstInsn)) {
                dexInstruction = processInvokeInstruction(cstInsn, referencedTypes);
            } else {
                dexInstruction = new Element(
                        sanitizeInstructionName(cstInsn.getOpcode().getName()), NS_DEX);
                Constant constant = cstInsn.getConstant();
                // TODO hack
                String type = constant.typeName();
                String name = "kind";
                if (!type.equals("field") && !type.equals("known-null") && !type.equals("type")
                        && !type.equals("string")) {
                    name = "type";
                }
                dexInstruction.setAttribute(name, constant.typeName());
                if (constant instanceof CstMemberRef) {
                    CstMemberRef memberRef = (CstMemberRef) constant;
                    String definingClassType = memberRef.getDefiningClass().getClassType()
                            .toHuman();

                    dexInstruction.setAttribute("class-type", definingClassType);
                    referencedTypes.add(definingClassType);
                    CstNat nameAndType = memberRef.getNat();
                    String memberType = nameAndType.getFieldType().getType().toHuman();
                    dexInstruction.setAttribute("member-type", memberType);
                    referencedTypes.add(memberType);
                    String memberName = nameAndType.getName().toHuman();
                    dexInstruction.setAttribute("member-name", memberName);

                    // if this is a member access to a red class, we need to
                    // eliminate it.
                    if (isRedType(definingClassType) || isRedType(memberType)) {
                        dexInstruction = createAssertElement(definingClassType + "," + memberType,
                                memberName);
                    }
                } else if (constant instanceof CstString) {
                    CstString cstString = (CstString) constant;
                    String valueOriginal = cstString.getString().getString();
                    String value = "";
                    // Convert special characters in string to octal notation
                    for (int i = 0; i < valueOriginal.length(); i++) {
                        char ch = valueOriginal.charAt(i);
                        value += (ch < ' ' || ch > 'z') ? String.format("\\%03o", new Integer(ch))
                                : ch;
                    }
                    dexInstruction.setAttribute("value", value);
                } else {
                    // These are CstInsn instructions that we need to remove, if
                    // their constant is a red type.
                    List<String> instructionsToCheck = Arrays.asList(new String[] { "new-instance",
                            "instance-of", "check-cast", "const-class", "new-array" });
                    if (instructionsToCheck.contains(opname) && isRedType(constant.toHuman())) {
                        dexInstruction = createAssertElement(constant.toHuman(), opname);
                    } else {
                        dexInstruction.setAttribute("value", constant.toHuman());
                    }
                }
                if (cstInsn.getOpcode().getName().startsWith("filled-new-array")) {
                    addRegistersAsChildren(cstInsn.getRegisters(), dexInstruction);
                } else {
                    addRegistersAsAttributes(cstInsn.getRegisters(), dexInstruction);
                }
            }
        } else if (instruction instanceof TargetInsn) {
            TargetInsn targetInsn = (TargetInsn) instruction;
            String instructionName = targetInsn.getOpcode().getName();
            dexInstruction = new Element(sanitizeInstructionName(instructionName), NS_DEX);
            addRegistersAsAttributes(targetInsn.getRegisters(), dexInstruction);

            if (instructionName.equals("packed-switch") || instructionName.equals("sparse-switch")) {
                SwitchData switchData = switchDataBlocks.get(targetInsn.getTargetAddress());
                if (switchData == null) {
                    Log.error(TAG, "DEXmlvmOutputProcess: Couldn't find SwitchData block.");
                    System.exit(-1);
                }
                IntList cases = switchData.getCases();
                CodeAddress[] caseTargets = switchData.getTargets();

                // Sanity check.
                if (cases.size() != caseTargets.length) {
                    Log.error(TAG,
                            "DEXmlvmOutputProcess: SwitchData size mismatch: cases vs targets.");
                    System.exit(-1);
                }

                for (int i = 0; i < cases.size(); ++i) {
                    Element caseElement = new Element("case", NS_DEX);
                    caseElement.setAttribute("key", String.valueOf(cases.get(i)));
                    caseElement.setAttribute("label", String.valueOf(caseTargets[i].getAddress()));
                    dexInstruction.addContent(caseElement);
                }
            } else if (instructionName.equals("fill-array-data")) {
                ArrayList<Constant> data = arrayData.get(targetInsn.getTargetAddress()).getValues();
                for (Constant c : data) {
                    Element constant = new Element("constant", NS_DEX);
                    constant.setAttribute("value", c.toHuman());
                    dexInstruction.addContent(constant);
                }
            } else {
                dexInstruction
                        .setAttribute("target", String.valueOf(targetInsn.getTargetAddress()));
            }
        } else if (instruction instanceof HighRegisterPrefix) {
            HighRegisterPrefix highRegisterPrefix = (HighRegisterPrefix) instruction;
            SimpleInsn[] moveInstructions = highRegisterPrefix.getMoveInstructions();
            for (SimpleInsn moveInstruction : moveInstructions) {
                processInstruction(moveInstruction, parentElement, switchDataBlocks, arrayData,
                        sourceLinesAlreadyPut, referencedTypes);
            }
        } else {
            System.err.print(">>> Unknown instruction: ");
            System.err.print("(" + instruction.getClass().getName() + ") ");
            System.err.print(instruction.listingString("", 0, true));
            System.exit(-1);
        }
        if (LOTS_OF_DEBUG) {
            System.out.print("(" + instruction.getClass().getName() + ") ");
            System.out.print(instruction.listingString("", 0, true));
        }
        if (dexInstruction != null) {
            // if (instruction.hasAddress()) {
            // dexInstruction.setAttribute("DEBUG-ADDRESS",
            // String.valueOf(instruction.getAddress()));
            // }
            parentElement.addContent(dexInstruction);
            lastDexInstruction = dexInstruction;
        }
    }

    /**
     * Takes the registers given and appends corresponding attributes to the
     * given element.
     */
    private static void addRegistersAsAttributes(RegisterSpecList registers, Element element) {
        final String[] REGISTER_NAMES = { "vx", "vy", "vz" };

        // Sanity check.
        if (registers.size() > 3) {
            Log.error(TAG, "DEXmlvmOutputProcess.processRegisters: Too many registers.");
            System.exit(-1);
        }
        for (int i = 0; i < registers.size(); ++i) {
            element.setAttribute(REGISTER_NAMES[i],
                    String.valueOf(registerNumber(registers.get(i).regString())));
            element.setAttribute(REGISTER_NAMES[i] + "-type", registers.get(i).getType().toHuman());
        }
    }

    /**
     * Takes the registers given and appends corresponding child tags to the
     * given element.
     */
    private static void addRegistersAsChildren(RegisterSpecList registers, Element element) {
        for (int i = 0; i < registers.size(); ++i) {
            Element reg = new Element("value", NS_DEX);
            reg.setAttribute("register", "" + registerNumber(registers.get(i).regString()));
            reg.setAttribute("type", registers.get(i).getType().toHuman());
            element.addContent(reg);
        }
    }

    /**
     * Returns whether the given instruction is an invoke instruction that can
     * be handled by {@link #processInvokeInstruction(CstInsn)}.
     */
    private static boolean isInvokeInstruction(CstInsn cstInsn) {
        final Dop[] invokeInstructions = { Dops.INVOKE_VIRTUAL, Dops.INVOKE_VIRTUAL_RANGE,
                Dops.INVOKE_STATIC, Dops.INVOKE_STATIC_RANGE, Dops.INVOKE_DIRECT,
                Dops.INVOKE_DIRECT_RANGE, Dops.INVOKE_INTERFACE, Dops.INVOKE_INTERFACE_RANGE,
                Dops.INVOKE_SUPER, Dops.INVOKE_SUPER_RANGE };
        for (Dop dop : invokeInstructions) {
            if (dop.equals(cstInsn.getOpcode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether the given instruction is an invoke-static instruction.
     */
    private static boolean isInvokeStaticInstruction(CstInsn cstInsn) {
        final Dop[] staticInvokeInstructions = { Dops.INVOKE_STATIC, Dops.INVOKE_STATIC_RANGE };
        for (Dop dop : staticInvokeInstructions) {
            if (dop.equals(cstInsn.getOpcode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an element representing the given invoke instruction.
     */
    private Element processInvokeInstruction(CstInsn cstInsn, Set<String> referencedTypes) {
        Element result = new Element(sanitizeInstructionName(cstInsn.getOpcode().getName()), NS_DEX);
        CstMethodRef methodRef = (CstMethodRef) cstInsn.getConstant();
        String classType = methodRef.getDefiningClass().toHuman();
        String methodName = methodRef.getNat().getName().toHuman();

        // Optimization: If the red/green class optimization is enabled, and the
        // class we are about to call is a red class, we remove the call and
        // replace it with an assert.
        if (isRedType(classType)) {
            return createAssertElement(classType, methodName);
        }
        referencedTypes.add(classType);
        result.setAttribute("class-type", classType);
        result.setAttribute("method", methodName);
        RegisterSpecList registerList = cstInsn.getRegisters();
        List<RegisterSpec> registers = new ArrayList<RegisterSpec>();
        if (isInvokeStaticInstruction(cstInsn)) {
            if (registerList.size() > 0) {
                registers.add(registerList.get(0));
            }
        } else {
            // For non-static invoke instruction, the first register is the
            // instance the method is called on.
            result.setAttribute("register",
                    String.valueOf(registerNumber(registerList.get(0).regString())));
        }

        // Adds the rest of the registers, if any.
        for (int i = 1; i < registerList.size(); ++i) {
            registers.add(registerList.get(i));
        }

        result.addContent(processParameterList(methodRef, registers));
        return result;
    }

    /**
     * Processes the signature of the given method reference and returns a
     * corresponding element.
     */
    private Element processParameterList(CstMethodRef methodRef, List<RegisterSpec> registers) {
        Element result = new Element("parameters", NS_DEX);
        Prototype prototype = methodRef.getPrototype();
        StdTypeList parameters = prototype.getParameterTypes();

        // Sanity check.
        if (parameters.size() != registers.size()) {
            Log.error(TAG, "DEXmlvmOutputProcess.processParameterList: Size mismatch: "
                    + "registers vs parameters");
            System.exit(-1);
        }

        for (int i = 0; i < parameters.size(); ++i) {
            Element parameterElement = new Element("parameter", NS_DEX);
            String parameterType = parameters.get(i).toHuman();
            parameterElement.setAttribute("type", parameterType);
            if (isRedType(parameterType)) {
                parameterElement.setAttribute("isRedType", "true");
            }
            parameterElement.setAttribute("register",
                    String.valueOf(registerNumber(registers.get(i).regString())));
            result.addContent(parameterElement);
        }
        Element returnElement = new Element("return", NS_DEX);
        String returnType = prototype.getReturnType().getType().toHuman();
        if (isRedType(returnType)) {
            returnType = JLO;
        }
        returnElement.setAttribute("type", returnType);
        result.addContent(returnElement);
        return result;
    }

    /**
     * Processes the signature of the given method reference and returns a
     * corresponding element. It uses 'registers' to add register. The parameter
     * types are added to the list of referenced types because they will be
     * needed for the reflection API.
     */
    private Element processSignature(CstMethodRef methodRef, Set<String> referencedTypes) {
        Prototype prototype = methodRef.getPrototype();
        StdTypeList parameters = prototype.getParameterTypes();

        HashSet<String> bad = new HashSet<String>();
        for (String t : new String[] { "char", "float", "double", "int", "boolean", "short",
                "byte", "float", "long" }) {
            bad.add(t);
        }

        Element result = new Element("signature", NS_XMLVM);
        for (int i = 0; i < parameters.size(); ++i) {
            Element parameterElement = new Element("parameter", NS_XMLVM);
            String parameterType = parameters.get(i).toHuman();
            parameterElement.setAttribute("type", parameterType);
            if (isRedType(parameterType)) {
                parameterElement.setAttribute("isRedType", "true");
            } else {
                int j = parameterType.indexOf('[');
                if (j != -1) {
                    // Remove array type
                    parameterType = parameterType.substring(0, j);
                }
                // Ignore primitive types
                if (!bad.contains(parameterType)) {
                    referencedTypes.add(parameterType);
                }
            }
            result.addContent(parameterElement);
        }
        Element returnElement = new Element("return", NS_XMLVM);
        String returnType = prototype.getReturnType().getType().toHuman();
        if (isRedType(returnType)) {
            returnType = JLO;
        }

        returnElement.setAttribute("type", returnType);
        result.addContent(returnElement);
        return result;
    }

    /**
     * Makes sure the instruction name is valid as an XML tag name.
     */
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
     * Extracts the number out of the register name of the format (v0, v1, v2,
     * etc).
     * 
     * @param vFormat
     *            the register name in v-format
     * @return the extracted register number
     */
    private static int registerNumber(String vFormat) throws RuntimeException {
        if (!vFormat.startsWith("v")) {
            throw new RuntimeErrorException(new Error(
                    "Register name doesn't start with 'v' prefix: " + vFormat));
        }
        try {
            int registerNumber = Integer.parseInt(vFormat.substring(1));
            return registerNumber;
        } catch (NumberFormatException ex) {
            throw new RuntimeErrorException(new Error(
                    "Couldn't extract register number from register name: " + vFormat, ex));
        }
    }

    /**
     * Returns true if annotation {@link org.xmlvm.XMLVMIgnore} is found.
     */
    private static boolean hasIgnoreAnnotation(AttributeList attrs) {
        BaseAnnotations a = (BaseAnnotations) attrs
                .findFirst(AttRuntimeInvisibleAnnotations.ATTRIBUTE_NAME);
        if (a != null) {
            for (Annotation an : a.getAnnotations().getAnnotations()) {
                if (an.getType().getClassType().getClassName().equals("org/xmlvm/XMLVMIgnore")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if annotation {@link org.xmlvm.XMLVMSkeletonOnly} is found.
     */
    private static boolean hasSkeletonOnlyAnnotation(AttributeList attrs) {
        BaseAnnotations a = (BaseAnnotations) attrs
                .findFirst(AttRuntimeInvisibleAnnotations.ATTRIBUTE_NAME);
        if (a != null) {
            for (Annotation an : a.getAnnotations().getAnnotations()) {
                if (an.getType().getClassType().getClassName()
                        .equals("org/xmlvm/XMLVMSkeletonOnly")) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Element createAssertElement(String typeName, String memberName) {
        Element assertElement = new Element("assert-red-class", NS_XMLVM);
        assertElement.setAttribute("type", typeName);
        assertElement.setAttribute("member", memberName);
        return assertElement;
    }
}
