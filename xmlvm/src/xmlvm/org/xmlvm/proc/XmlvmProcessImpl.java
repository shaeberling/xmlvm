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

package org.xmlvm.proc;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.xmlvm.Log;
import org.xmlvm.main.Arguments;
import org.xmlvm.proc.out.ClassToXmlvmProcess;
import org.xmlvm.proc.out.DEXmlvmOutputProcess;
import org.xmlvm.proc.out.ExeToXmlvmProcess;
import org.xmlvm.proc.out.XmlvmToXmlvmProcess;

/**
 * A collection of possible process IDs.
 */
enum XmlvmProcessId {
    XMLVM_JVM("xmlvmjvm"), XMLVM_CLR("xmlvmclr"), XMLVM_CLR_DFA("xmlvmclrdfa"), CLASS("class"), EXE(
            "exe"), JS("js"), JAVA("java"), OBJC("objc"), CPP("cpp"), PYTHON("python"), IPHONE("iphone"), QOOXDOO(
            "qooxdoo");
    String name;


    private XmlvmProcessId(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}


/**
 * Common implementation for all XMLVM Processes. Actual processes extend this
 * class.
 */
public abstract class XmlvmProcessImpl<T> implements XmlvmProcess<T> {

    private static final String            TAG             = "XmlvmProcessImpl";

    private List<XmlvmProcess<?>>          preprocesses    = new ArrayList<XmlvmProcess<?>>();

    /**
     * This list contains all the supported input processes.
     */
    protected List<Class<XmlvmProcess<?>>> supportedInputs = new ArrayList<Class<XmlvmProcess<?>>>();

    protected Arguments                    arguments;

    protected boolean                      isProcessed     = false;


    public XmlvmProcessImpl(Arguments arguments) {
        Log.debug("Instantiated: " + this.getClass().getName());
        this.arguments = arguments;
    }

    @Override
    public abstract boolean process();

    @Override
    public boolean postProcess() {
        return true;
    }

    @Override
    public List<Class<XmlvmProcess<?>>> getSupportedInputs() {
        return supportedInputs;
    }

    @SuppressWarnings("unchecked")
    protected void addSupportedInput(Class<?> inputProcessClass) {
        try {
            supportedInputs.add((Class<XmlvmProcess<?>>) inputProcessClass);
        } catch (ClassCastException ex) {
            ex.printStackTrace();
            Log.error("You tried to add a supported input that is not of the same type as the "
                    + "generic type you've specified in the process.");
        }
    }

    /**
     * Adds all processes that emit XMLVM as potential input processes.
     */
    protected void addAllXmlvmEmittingProcessesAsInput() {
        if (!arguments.option_use_jvm()) {
            addSupportedInput(DEXmlvmOutputProcess.class);
        } else {
            addSupportedInput(ClassToXmlvmProcess.class);
        }
        addSupportedInput(ExeToXmlvmProcess.class);
        addSupportedInput(XmlvmToXmlvmProcess.class);
    }

    @Override
    public List<XmlvmProcess<?>> createInputInstances() {
        List<XmlvmProcess<?>> result = new ArrayList<XmlvmProcess<?>>();
        for (Class<XmlvmProcess<?>> supportedClass : getSupportedInputs()) {
            try {
                XmlvmProcess<?> process = (XmlvmProcess<?>) supportedClass.getConstructors()[0]
                        .newInstance(arguments);
                result.add(process);
                // Add this process to the list of pre-processes.
                addPreprocess(process);
            } catch (InstantiationException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean supportsAsInput(XmlvmProcess<?> process) {
        for (Class<XmlvmProcess<?>> supportedClass : getSupportedInputs()) {
            if (isOfType(process.getClass(), supportedClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether this process is a sub-class of a direct or indirect class
     * of the type given.
     */
    public boolean isOfSuperType(Class<?> type) {
        return isOfType(this.getClass(), type);
    }

    /**
     * Returns true, if 'b' is either a direct or indirect superclass of 'a'.
     */
    private static boolean isOfType(Class<?> a, Class<?> b) {
        if (a == null) {
            return false;
        }
        if (a.equals(b)) {
            return true;
        } else {
            return isOfType(a.getSuperclass(), b);
        }
    }

    @Override
    public void addPreprocess(XmlvmProcess<?> xmlvmProcess) {
        Log.debug("Adding preprocess " + xmlvmProcess.getClass().getName() + " to process "
                + this.getClass().getName());
        preprocesses.add(xmlvmProcess);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> preprocess() {
        for (XmlvmProcess<?> process : preprocesses) {
            // We test whether the pre-process type is a sub-type of the given
            // generic type of the process.
            try {
                ((T) process).toString();
            } catch (ClassCastException ex) {
                Log.error("A preprocess is not of the given generic type: "
                        + process.getClass().getName());
                return null;
            }
            if (process.isActive()) {
                // TODO(haeberling): Maybe replace by preprocess? This way
                // processes don't have to call preprocess themselves
                // anymore.
                process.process();
                if (process instanceof XmlvmProcessImpl) {
                    // Mark the process as processed.
                    ((XmlvmProcessImpl) process).isProcessed = true;
                } else {
                    Log.error(TAG, "Internal Error: Preprocess found that is not an "
                            + "XmlvmProcessImpl instance.");
                }
            }
        }
        return (List<T>) preprocesses;
    }

    @Override
    public boolean postProcessPreProcesses() {
        for (XmlvmProcess<?> process : preprocesses) {
            if (process.isProcessed()) {
                if (!process.postProcessPreProcesses()) {
                    return false;
                }
            }
        }
        return postProcess();
    }

    @Override
    public boolean isActive() {
        // A process is active only when at least one of his preprocesses is
        // active.
        for (XmlvmProcess<?> preprocess : preprocesses) {
            if (preprocess.isActive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isProcessed() {
        return isProcessed;
    }
}
