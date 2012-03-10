/* Copyright (c) 2011 by crossmobile.org
 *
 * CrossMobile is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2.
 *
 * CrossMobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CrossMobile; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.crossmobile.source.ctype;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.crossmobile.source.guru.Advisor;
import org.crossmobile.source.guru.Oracle;
import org.crossmobile.source.guru.Reporter;
import org.crossmobile.source.utils.FieldHolder;
import org.crossmobile.source.utils.FileUtils;

public class CLibrary implements FieldHolder, Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, CObject> objects = new HashMap<String, CObject>();
    private final Set<CEnum> enums = new LinkedHashSet<CEnum>();
    private final Set<CStruct> structs = new LinkedHashSet<CStruct>();
    private final Set<CFunction> functions = new LinkedHashSet<CFunction>();
    private final Set<CArgument> externals = new LinkedHashSet<CArgument>();
    private String currentFile;
    private String currentFramework;
    private final String packagename;

    public static CLibrary construct(String pack, File sdkpath, boolean debug) {
        CLibrary library = new CLibrary(pack);
        for (File framework : sdkpath.listFiles()) {
            library.currentFramework = framework.getName();
            if (library.currentFramework.endsWith(".framework"))
                library.currentFramework = library.currentFramework.substring(0, library.currentFramework.length() - ".framework".length());

            if (debug)
                System.out.println("Parsing " + library.currentFramework + " framework");
            if (new File(framework + File.separator + "Headers").isDirectory())
                for (File f : new File(framework, "Headers").listFiles())
                    if (f.getPath().toLowerCase().endsWith(".h"))
                        library.addFile(f.getPath());
        }
        if (debug)
            System.out.println("Finalize");
        library.finalizeLibrary();
        return library;
    }

    public CLibrary(String packagename) {
        this.packagename = packagename;
    }

    public synchronized void addFile(String filename) {
        String data = FileUtils.getFile(filename);
        if (data == null)
            return;
        this.currentFile = filename;
        Reporter.setFile(filename);
        CAny.parse(this, Advisor.convertData(data));
        this.currentFile = null;
    }

    public CObject getObject(String name) {
        return getObject(name, false);
    }

    public CObject getInterface(String name) {
        return getObject(name, true);
    }

    private CObject getObject(String name, boolean isProtocol) {
        name = Oracle.nameBeautifier(new CType(name).getProcessedName());
        CObject obj = objects.get(name);
        if (obj != null)
            return obj;
        obj = new CObject(this, name, isProtocol);
        objects.put(name, obj);
        return obj;
    }
    
    public CObject getObjectIfPresent(String name){
        name = Oracle.nameBeautifier(new CType(name).getProcessedName());
        CObject obj = objects.get(name);
        if(obj!=null)
            return obj;
        return null;
    }

    public Set<CEnum> getEnums() {
        return enums;
    }

    public Set<CArgument> getExternals() {
        return externals;
    }

    public Set<CFunction> getFunctions() {
        return functions;
    }

    public Set<CStruct> getStructs() {
        return structs;
    }

    public String getCurrentFile() {
        return currentFile;
    }

    public String getPackagename() {
        return packagename;
    }

    public void finalizeLibrary() {
        Reporter.setFile(null);
        Advisor.addDefinedObjects(this);
        CType.finalizeTypedefs();
        Oracle.positionObjects(this);
        for (CObject o : objects.values()) {
            Reporter.setObject(o);
            o.finalizeObject();
        }
    }

    public Iterable<CObject> getObjects() {
        return objects.values();
    }

    void addCFunction(CFunction cFunction) {
        functions.add(cFunction);
    }

    @Override
    public void addCArgument(CArgument arg) {
        externals.add(arg);
    }

    public String getCurrentFrameWork() {
        return currentFramework;
    }
}
