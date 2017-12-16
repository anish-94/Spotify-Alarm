package com.example.anish.spotifyalarm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by anish on 12/14/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

//    private final String mPackage = "com.example.anish.spotifyalarm";
//    private final String mClass = "RingtonePlayer";

    @Override
    public void onReceive(final Context context, Intent intent) {

        String state = intent.getExtras().getString("extra");
        Intent serviceIntent = new Intent(context, RingtonePlayer.class);
        serviceIntent.putExtra("extra", state);
//        serviceIntent.setComponent(new ComponentName(mPackage, mPackage+mClass));
        context.startService(serviceIntent);
        Log.e("MyActivity", "In the receiver with " + state);
    }

}
