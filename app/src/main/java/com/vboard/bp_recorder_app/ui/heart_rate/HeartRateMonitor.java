package com.vboard.bp_recorder_app.ui.heart_rate;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.vboard.bp_recorder_app.R;
import com.vboard.bp_recorder_app.utils.ImageProcessing;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jcs on 13/2/18.
 */

public class HeartRateMonitor extends AppCompatActivity {

    static int progress = 0;
    static CountDownTimer timer = null;
    static ProgressBar heart_progressbar;

    private static final String TAG = "HeartRateMonitor";
    private static final AtomicBoolean processing = new AtomicBoolean(false);

    private static SurfaceView preview = null;
    private static SurfaceHolder previewHolder = null;
    private static Camera camera = null;
    private static View image = null;
    private static TextView text = null;

    private static PowerManager.WakeLock wakeLock = null;

    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];

    public static enum TYPE {
        GREEN, RED
    }

    ;

    private static TYPE currentType = TYPE.GREEN;

    public static TYPE getCurrent() {
        return currentType;
    }


    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;

    public static String heartRate;
    private static boolean calculatingTHR = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heartratemonitor);

        preview = (SurfaceView) findViewById(R.id.preview);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        image = findViewById(R.id.image);
        text = (TextView) findViewById(R.id.text);
        heart_progressbar = findViewById(R.id.progress_bar);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");



         timer = new CountDownTimer(20000, 20000) {
            @Override
            public void onTick(long l) {

                progress = progress+1;
                heart_progressbar.setProgress(progress);
                Log.e("TAG", "onTick: progress is"+progress);

            }

            @Override
            public void onFinish() {

            }
        };


    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();

        wakeLock.acquire();

        camera = Camera.open();

        startTime = System.currentTimeMillis();

    }

    @Override
    public void onPause() {
        super.onPause();

        wakeLock.release();

        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private static Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {

            if (data == null) throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();

            if (!processing.compareAndSet(false, true)) return;

            int width = size.width;
            int height = size.height;

            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);








            if (imgAvg == 0 || imgAvg < 200) {
                progress =0;
                timer.cancel();
                processing.set(false);
                return;
            }

            timer.start();




            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                Log.e(TAG, "onPreviewFrame: avg array count" + i);
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED;
                if (newType != currentType) {
                    beats++;
                    Log.e(TAG, "onPreviewFrame: beats" + beats);
                    Log.e(TAG, "onPreviewFrame: beatsarray length" + beatsArray.length);


                }
            } else if (imgAvg > rollingAverage) {
                newType = TYPE.GREEN;
            }

            if (averageIndex == averageArraySize) averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            // Transitioned from one state to another to the same
            if (newType != currentType) {
                Log.e(TAG, "onPreviewFrame: currenttype is " + currentType);
                currentType = newType;
                image.postInvalidate();
            }

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;

            if (totalTimeInSecs >= 10) {

                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180) {

                    startTime = System.currentTimeMillis();
                    beats = 0;
                    heart_progressbar.setProgress(0);
                    processing.set(false);
                    return;
                }

                if (beatsIndex == beatsArraySize) beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;
                Log.e(TAG, "onPreviewFrame: beatsIndex" + beatsIndex);


                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                        Log.e(TAG, "onPreviewFrame: beatsarraycount" + beatsArrayCnt);


                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                heartRate = String.valueOf(beatsAvg);
                text.setText(heartRate);
                startTime = System.currentTimeMillis();
                beats = 0;
                stopCam();
            }
            processing.set(false);


        }
    };

    public static void stopCam() {
        camera.stopPreview();
        if (calculatingTHR) {
            setCard();
        }
    }

    private static void setCard() {

        int index = 0;
        int workoutHeartRate = Integer.parseInt(heartRate);

        for (double i = 0.6; i < 1; i = i + 0.1) {
            //actual Karvonen's formula: (workoutHeartRate > heartRateReserve*i + restingHeartRate)


        }


    }

    public static void startCam() {
        camera.startPreview();
        startTime = System.currentTimeMillis();
    }

    public void finish() {
        Intent data = new Intent();
        data.putExtra("restingHeartRate", heartRate);
        setResult(RESULT_OK, data);
        super.finish();
    }


    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                Log.e("surfaceCallbackDemo", "Exception in setPreviewDisplay()", t);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
                Log.d(TAG, "Using width=" + size.width + " height=" + size.height);
            }
            camera.setParameters(parameters);
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };


    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea < resultArea) result = size;
                }
            }
        }

        return result;
    }

}
