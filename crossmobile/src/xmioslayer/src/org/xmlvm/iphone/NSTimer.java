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

import org.crossmobile.ios2a.MainActivity;
import org.crossmobile.ios2a.LifeCycle;

public class NSTimer<A> extends NSObject {

    private final Thread thread;

    @SuppressWarnings({"CallToThreadStartDuringObjectConstruction", "LeakingThisInConstructor"})
    private NSTimer(final float timerInterval, final NSTimerDelegate<A> target, final A userInfo, final boolean repeats) {
        final Runnable tick = new Runnable() {

            public void run() {
                target.timerEvent(userInfo);
            }
        };
        thread = new Thread() {

            @Override
            @SuppressWarnings({"SleepWhileHoldingLock", "SleepWhileInLoop"})
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep((int) (timerInterval * 1000));
                    } catch (InterruptedException e) {
                        break;
                    }
                    MainActivity.current.runOnUiThread(tick);
                    if (!repeats)
                        break;
                }
                LifeCycle.unregister(NSTimer.this);
            }
        };
        LifeCycle.register(this);
        thread.start();
    }

    public static <A> NSTimer scheduledTimerWithTimeInterval(float seconds, NSTimerDelegate<A> target, A userinfo, boolean repeats) {
        return new NSTimer<A>(seconds, target, userinfo, repeats);
    }

    public void invalidate() {
        thread.interrupt();
    }
}
