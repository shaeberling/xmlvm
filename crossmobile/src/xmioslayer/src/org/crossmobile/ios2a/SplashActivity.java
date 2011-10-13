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

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public abstract class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.current = this;
        setContentView(getSplashResource());
        try {
            System.getProperties().load(FileBridge.getInputFileStream(FileBridge.BUNDLEPREFIX + "/crossmobile.properties"));
        } catch (Exception ex) {
            LifeCycle.finishWithError("Corrupted CrossMobile application, missing crossmobile.properties file", ex);
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(getSplashViewID()).post(new Runnable() {

            public void run() {
                IOSView.updateBarMetrics(SplashActivity.this);
                new Thread() {

                    @Override
                    public void run() {
                        int wait = 1000;
                        try {
                            wait = Integer.parseInt(System.getProperty("xm.splash.delay"));
                            if (wait < 0)
                                wait = 0;
                        } catch (NumberFormatException ex) {
                        }
                        try {
                            Thread.sleep(wait);
                        } catch (InterruptedException ex) {
                        }
                        finish();
                    }
                }.start();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        startActivity(new Intent(getMainActivity()));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    protected abstract int getSplashResource();

    protected abstract int getSplashViewID();

    protected abstract String getMainActivity();
}
