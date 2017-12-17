package com.example.anish.spotifyalarm

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.support.v4.content.WakefulBroadcastReceiver
import android.util.Log
import android.widget.Toast

/**
 * Created by anish on 12/14/17.
 */

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val state = intent.extras!!.getString("extra")
        val serviceIntent = Intent(context, RingtonePlayer::class.java)
        serviceIntent.putExtra("extra", state)
        context.startService(serviceIntent)
        Log.e("MyActivity", "In the receiver with " + state!!)
    }

}
