/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.s531505.smartalarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;


import com.example.s531505.smartalarm.dbhelper.DatabaseHelper;
import com.example.s531505.smartalarm.dbhelper.model.SmartAlarm;

import java.util.List;

/**
 * Class to process location results.
 */
class LocationResultHelper {

    final static String KEY_LOCATION_UPDATES_RESULT = "location-update-result";

    final private static String PRIMARY_CHANNEL = "default";


    private Context mContext;
    private List<Location> mLocations;
    private NotificationManager mNotificationManager;
    private DatabaseHelper db;

    LocationResultHelper(Context context, List<Location> locations) {
        mContext = context;
        mLocations = locations;
        db = new DatabaseHelper(context);

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(PRIMARY_CHANNEL,
                    context.getString(R.string.default_channel), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLightColor(Color.GREEN);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getNotificationManager().createNotificationChannel(channel);
        }

    }

    /**
     * Returns the title for reporting about a list of {@link Location} objects.
     */
    private String getLocationResultTitle() {
        SmartAlarm smartAlarm = db.getFirst();
//        String numLocationsReported = mContext.getResources().getQuantityString(
//                R.plurals.num_locations_reported, mLocations.size(), mLocations.size());
//        return numLocationsReported + ": " + DateFormat.getDateTimeInstance().format(new Date());
        return smartAlarm != null ? "Alarm : " + smartAlarm.getName() : "";
    }

    private String getLocationResultText() {
        SmartAlarm smartAlarm = db.getFirst();
//        if (mLocations.isEmpty()) {
//            return mContext.getString(R.string.unknown_location);
//        }
//        StringBuilder sb = new StringBuilder();
//        for (Location location : mLocations) {
//            sb.append("(");
//            sb.append(location.getLatitude());
//            sb.append(", ");
//            sb.append(location.getLongitude());
//            sb.append(")");
//            sb.append("\n");
//        }
//        return sb.toString();
        return smartAlarm != null ? "" + smartAlarm.getNotes() : "";
    }

    /**
     * Saves location result as a string to {@link android.content.SharedPreferences}.
     */
    void saveResults() {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(KEY_LOCATION_UPDATES_RESULT, getLocationResultTitle() + "\n" +
                        getLocationResultText())
                .apply();
    }

    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    static String getSavedLocationResult(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_UPDATES_RESULT, "");
    }

    public boolean checkLocationDistance(Location mylocation) {
        SmartAlarm smartAlarm = db.getFirst();
        boolean canShowNotification = false;
        if (smartAlarm != null && smartAlarm.getLatitude() != 0) {
            Location dest_location = new Location("");
            dest_location.setLatitude(smartAlarm.getLatitude());
            dest_location.setLongitude(smartAlarm.getLongitude());
            float distance = mylocation.distanceTo(dest_location);//in meters
            Log.e(" distance ", distance + " in mtrs");
            // return distance;
            if (distance <= smartAlarm.getRadius()) {
                canShowNotification = true;
            }
        }
        return canShowNotification;
    }

    /**
     * Get the notification mNotificationManager.
     * <p>
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) mContext.getSystemService(
                    Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    /**
     * Displays a notification with the location results.
     */
    void showNotification() {
        Intent notificationIntent = new Intent(mContext, LocationActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(LocationActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification.Builder notificationBuilder = new Notification.Builder(mContext,
                    PRIMARY_CHANNEL)
                    .setContentTitle(getLocationResultTitle())
                    .setContentText(getLocationResultText())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setContentIntent(notificationPendingIntent);

            getNotificationManager().notify(0, notificationBuilder.build());
        } else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, PRIMARY_CHANNEL)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getLocationResultTitle())
                    .setContentText(getLocationResultText())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

            notificationManager.notify(0, mBuilder.build());
        }

    }
}
