package com.example.anish.spotifyalarm;

import android.content.BroadcastReceiver;
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

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {

        String state = intent.getExtras().getString("extra");
        Intent serviceIntent = new Intent(context, RingtonePlayer.class);

//        serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        serviceIntent.putExtra("extra", state);

        context.startService(serviceIntent);
        Log.e("MyActivity", "In the receiver with " + state);
    }

}
