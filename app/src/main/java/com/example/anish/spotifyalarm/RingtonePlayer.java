package com.example.anish.spotifyalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by anish on 12/15/17.
 */

public class RingtonePlayer extends Service {

    private boolean isRunning = false;
    private Context context;
    MediaPlayer mMediaPlayer;
    private int startId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("MyActivity", "In the ringtone player");
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        final NotificationManager mNM = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        Intent int1 = new Intent (this.getApplicationContext(), MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, int1, 0);

/*        Notification mNotify  = new Notification.Builder(this)
                .setContentTitle("Alarm" + "!")
                .setContentText("Click me!")
    //            .setSmallIcon(R.drawable.ic_action_call)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();
*/
        String state = intent.getExtras().getString("extra");
        Log.d("MainActivity", "Made it to state with " + state);

        if(state.equals("yes")) {
            startId = 1;
        }
        else {
            startId = 0;
        }

        if(!this.isRunning && startId == 1) {
            Log.e("MainActivity", "We making sound");

            mMediaPlayer = MediaPlayer.create(this, R.raw.beatit);
            mMediaPlayer.start();

//            mNM.notify(0, mNotify);
        }

        else if (!this.isRunning && startId == 0){
            Log.e("if there was not sound ", " and you want end");

            this.isRunning = false;
            this.startId = 0;

        }

        else if (this.isRunning && startId == 1){
            Log.e("if there is sound ", " and you want start");

            this.isRunning = true;
            this.startId = 0;

        }
        else {
            Log.e("if there is sound ", " and you want end");

            mMediaPlayer.stop();
            mMediaPlayer.reset();

            this.isRunning = false;
            this.startId = 0;
        }

        return START_NOT_STICKY;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning = false;
    }

}
