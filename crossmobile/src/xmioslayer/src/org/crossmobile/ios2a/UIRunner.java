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

package org.crossmobile.ios2a;

public abstract class UIRunner implements Runnable {

    private boolean didRun = false; // by default set this thread not to be the same with the caller. If it indeed is, since the runOnUiThread calls directly the "run()" method, this variable is set immediately to "true"

    public static void runSynced(UIRunner runner) {
        synchronized (runner) {
            try {
                if (MainActivity.current != null) { // Run only if still active
                    MainActivity.current.runOnUiThread(runner);
                    if (!runner.didRun())
                        runner.wait();
                }
            } catch (InterruptedException ex) {
            }
        }
    }

    public static void runFree(UIRunner runner) {
        if (MainActivity.current != null) // Run only if still active
            MainActivity.current.runOnUiThread(runner);
    }

    @Override
    public final void run() {
        synchronized (this) {
            exec();
            didRun = true;
            notifyAll();
        }
    }

    public boolean didRun() {
        return didRun;
    }

    public abstract void exec();
}
