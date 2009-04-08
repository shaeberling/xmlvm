package org.xmlvm.asokoban;

//import org.openintents.hardware.SensorManagerSimulator;
//import org.openintents.provider.Hardware;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class ASokoban extends Activity implements SensorListener {
    private static final float movingThreshold = 1.7f;

    private GameView           gameView;
    private SensorManager      sensorManager;
    private int                currentLevel;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the orientation to landscape programmatically.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // No title bar.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Switch to fullscreen view, getting rid of the status bar as well.
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        currentLevel = 0;
        loadLevel();
        SensorManager sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        // ----------------------------------------------------------------------------------
        /*
         * Hardware.mContentResolver = getContentResolver(); sensorManager =
         * (SensorManager) new SensorManagerSimulator( (SensorManager)
         * getSystemService(SENSOR_SERVICE)); // If the emulator is used, use
         * the OpenIntent simulator to simulate sensor // changes. if
         * (isEmulator()) { Intent intent = new Intent(Intent.ACTION_VIEW,
         * Hardware.Preferences.CONTENT_URI); startActivity(intent);
         * sensorManager.unregisterListener(this);
         * SensorManagerSimulator.connectSimulator(); }
         */
        // ---------------------------------------------------------------------
        sensorManager.registerListener(this, SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_FASTEST);

    }

    public void loadLevel() {
        showDialog(0);
        gameView = new GameView(this, currentLevel);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return new AlertDialog.Builder(ASokoban.this).setTitle("Level: " + (currentLevel + 1))
                .setPositiveButton("OK", null).create();
    }

    public void onSensorChanged(int sensor, float[] values) {
        if (gameView.getGameController().levelFinished()) {
            currentLevel++;
            loadLevel();
            return;
        }
        float x = -values[1];
        float y = -values[0];
        gameView.getMover().setMovingSpeed(x, y);
        if (gameView.isMoving()) {
            return;
        }
        int dx = 0;
        int dy = 0;
        if (Math.abs(x) > Math.abs(y)) {
            if (x > movingThreshold)
                dx = 1;
            if (x < -movingThreshold)
                dx = -1;
        } else {
            if (y > movingThreshold)
                dy = 1;
            if (y < -movingThreshold)
                dy = -1;
        }
        if (Math.abs(dx) > 0 || Math.abs(dy) > 0) {
            gameView.getGameController().move(dx, dy);
        }
    }

    /*
     * @Override protected void onResume() { super.onResume();
     * sensorManager.registerListener(this, SensorManager.SENSOR_ACCELEROMETER,
     * SensorManager.SENSOR_DELAY_FASTEST); }
     * 
     * @Override protected void onStop() {
     * sensorManager.unregisterListener(this); super.onStop(); }
     */

    public void onAccuracyChanged(int sensor, int accuracy) {
    }

    /*
     * @Override public boolean onTrackballEvent(MotionEvent event) { if
     * (gameView.isMoving()) { return false; } if (event.getAction() ==
     * MotionEvent.ACTION_MOVE) { if (event.getHistorySize() > 0) { float dx =
     * event.getX() - event.getHistoricalX(0); float dy = event.getY() -
     * event.getHistoricalY(0); if (Math.abs(dy) > Math.abs(dx)) { dy = dy > 0 ?
     * 1 : -1; dx = 0; } else { dy = 0; dx = dx > 0 ? 1 : -1; }
     * gameView.getGameController().move((int) dx, (int) dy); } return true; }
     * return false; }
     */

    /**
     * Try to figure out whether this app is running on the emulator.
     */
    /*
     * public static boolean isEmulator() { return
     * Build.DEVICE.equals("generic") && Build.BRAND.equals("generic") &&
     * Build.PRODUCT.equals("generic"); }
     */
}