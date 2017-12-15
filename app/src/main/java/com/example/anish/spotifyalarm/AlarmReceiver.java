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
        Log.e("MyActivity", "In the receiver with " + state);

        Intent serviceIntent = new Intent(context,RingtonePlayer.class);
        serviceIntent.putExtra("extra", state);

        context.startService(serviceIntent);

/*        Log.d(TAG, "Made it to receiver");
        Toast.makeText(context, "WAKE UP!", Toast.LENGTH_LONG).show();
        MainActivity.getTextView2().setText("WAKE UP!!");
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();
*/
    }

}
