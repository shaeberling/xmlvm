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

package org.crossmobile.source;

import java.io.File;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.out.JavaOut;
import org.crossmobile.source.out.COut;

public class Main {

    private static boolean      printProgress = false;
    //
    private static final String inputpath     = "/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS4.3.sdk/System/Library/Frameworks";
    // private static final String inputpath = System.getProperty("user.home") +
    // File.separator + "/Works/Development/Mobile/SDK/CrossMobile/Frameworks";
    private static final String outputpath    = System.getProperty("user.home") + File.separator
                                                      + "output";


    public static void main(String[] args) {
        CLibrary library = CLibrary.construct("org.xmlvm.ios", new File(inputpath), printProgress);
        if (printProgress)
            System.out.println("Output");
        JavaOut out = new JavaOut(outputpath);
        out.generate(library);
        out.report();

        COut cout = new COut(outputpath);
        cout.generate(library);

    }
}
