package com.example.anish.spotifyalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by anish on 12/17/17.
 */

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val state = intent.extras!!.getString("extra")
        val serviceIntent = Intent(context, SpotUtils::class.java)
        serviceIntent.putExtra("extra", state)
        context.startActivity(serviceIntent)
        Log.e("MyActivity", "In the receiver with " + state!!)
    }

}
