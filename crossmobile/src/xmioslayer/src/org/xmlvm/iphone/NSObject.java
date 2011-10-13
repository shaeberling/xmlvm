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

package org.xmlvm.iphone;

import org.crossmobile.ios2a.UIRunner;

public class NSObject {

    public static <A> void performSelector(final NSSelector<A> selector, A arg, double delay) {
        NSTimer.scheduledTimerWithTimeInterval((float) delay, new NSTimerDelegate<A>() {

            public void timerEvent(A userInfo) {
                selector.invokeWithArgument(userInfo);
            }
        }, arg, false);
    }

    public static <A> void performSelectorOnMainThread(NSSelector<A> selector, final A arg, boolean waitUntilDone) {
        if (waitUntilDone)
            UIRunner.runSynced(new SelectorLauncher<A>(selector, arg));
        else
            UIRunner.runFree(new SelectorLauncher<A>(selector, arg));
    }

    private static class SelectorLauncher<A> extends UIRunner {

        private final NSSelector selector;
        private final Object arg;

        public SelectorLauncher(NSSelector<A> selector, A arg) {
            this.selector = selector;
            this.arg = arg;
        }

        @Override
        public void exec() {
            selector.invokeWithArgument(arg);
        }
    };

    public NSObject retain() {
        return this;
    }

    public void release() {
    }

    public void dealloc() {
    }
}
