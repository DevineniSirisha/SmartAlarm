package com.example.s531505.smartalarm;


import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class AlarmRecieverActivity extends Activity implements
        SensorEventListener, OnInitListener {

    private MediaPlayer mMediaPlayer;
    private PowerManager.WakeLock mWakeLock;
    TextToSpeech talker;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final static String TAG = "SmartAlarm";
    private float mLimit = 2;
    private float mLastValues[] = new float[3 * 2];
    private float mScale[] = new float[2];
    private float mYOffset;

    private float mLastDirections[] = new float[3 * 2];
    private float mLastExtremes[][] = { new float[3 * 2], new float[3 * 2] };
    private float mLastDiff[] = new float[3 * 2];
    private int mLastMatch = -1;
    private int totalSteps = 0;
    private TextView tv;
    private int maxSteps = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pedometer Stuff
        int h = 480;
        mYOffset = h * 0.5f;
        mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
        tv = (TextView) findViewById(R.id.stepTextView);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Alarm : my wakeup alarm");
        mWakeLock.acquire();
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.alarm);

        // TTS
        talker = new TextToSpeech(this, this);

        Button stopAlarm = (Button) findViewById(R.id.btnStopAlarm);
        stopAlarm.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                getPreferences(MODE_PRIVATE).edit().putString("hour", "-")
                        .commit();
                getPreferences(MODE_PRIVATE).edit().putString("minute", "-")
                        .commit();

                mMediaPlayer.stop();
                finish();

                return true;
            }
        });
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelerometer = mSensorManager
                .getDefaultSensor((Sensor.TYPE_ACCELEROMETER));

        mSensorManager.registerListener((SensorEventListener) this,
                mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        playSound(this, getAlarmUri());

    }

    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                totalSteps = 0;
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("AlarmReciever", "No Audio Files Found");
            e.printStackTrace();
        } catch (Exception e) {
            Log.i("AlarmReciever", "No Audio Files Found");
            e.printStackTrace();
        }
    }

    private Uri getAlarmUri() {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }

    @Override
    protected void onDestroy() {
        if (talker != null) {
            talker.stop();
            talker.shutdown();
        }
        super.onDestroy();
        getPreferences(MODE_PRIVATE).edit().putString("hour", "-").commit();
        getPreferences(MODE_PRIVATE).edit().putString("minute", "-").commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferences(MODE_PRIVATE).edit().putString("hour", "-").commit();
        getPreferences(MODE_PRIVATE).edit().putString("minute", "-").commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mWakeLock.isHeld())
            mWakeLock.release();
        getPreferences(MODE_PRIVATE).edit().putString("hour", "-").commit();
        getPreferences(MODE_PRIVATE).edit().putString("minute", "-").commit();
        mSensorManager.unregisterListener((SensorEventListener) this);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
            } else {
                int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
                if (j == 1) {
                    float vSum = 0;
                    for (int i = 0; i < 3; i++) {
                        final float v = mYOffset + event.values[i] * mScale[j];
                        vSum += v;
                    }
                    int k = 0;
                    float v = vSum / 3;

                    float direction = (v > mLastValues[k] ? 1
                            : (v < mLastValues[k] ? -1 : 0));
                    if (direction == -mLastDirections[k]) {
                        int extType = (direction > 0 ? 0 : 1);
                        mLastExtremes[extType][k] = mLastValues[k];
                        float diff = Math.abs(mLastExtremes[extType][k]
                                - mLastExtremes[1 - extType][k]);

                        if (diff > mLimit) {

                            boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
                            boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
                            boolean isNotContra = (mLastMatch != 1 - extType);

                            if (isAlmostAsLargeAsPrevious
                                    && isPreviousLargeEnough && isNotContra) {
                                Log.i(TAG, "step");
                                totalSteps++;
                                tv = (TextView) findViewById(R.id.stepTextView);
                                tv.setText("Steps Taken: "
                                        + Integer.toString(totalSteps));
                                if (totalSteps > maxSteps) {
                                    getPreferences(MODE_PRIVATE).edit()
                                            .putString("hour", "-").commit();
                                    getPreferences(MODE_PRIVATE).edit()
                                            .putString("minute", "-").commit();

                                    mMediaPlayer.stop();
                                    finish();
                                }
                                mLastMatch = extType;
                            } else {
                                mLastMatch = -1;
                            }
                        }
                        mLastDiff[k] = diff;
                    }
                    mLastDirections[k] = direction;
                    mLastValues[k] = v;
                }
            }
        }
    }

    @Override
    public void onInit(int status) {

        say("");

    }

    public void say(String text2say) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        if (hour == 0)
            hour = 12;
        String amPm = c.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm";
        StringBuffer sbSay = new StringBuffer();
        sbSay.append(String.valueOf(hour)).append(" ")
                .append(String.valueOf(min)).append("   ").append(amPm);

        talker.speak("The time now is"+sbSay.toString(), TextToSpeech.QUEUE_ADD, null);
        talker.playSilence(1000, TextToSpeech.QUEUE_ADD, null);
        talker.speak("Please walk 20 steps to silence the alarm", TextToSpeech.QUEUE_ADD, null);
    }
}
