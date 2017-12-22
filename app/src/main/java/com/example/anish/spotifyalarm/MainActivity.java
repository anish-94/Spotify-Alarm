package com.example.anish.spotifyalarm;

import java.util.Calendar;
import java.text.DateFormat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import org.w3c.dom.Text;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    public final static String STATUS = "com.example.anish.spotifyalarm.STATUS";
    public final static String QUERY = "com.example.anish.spotifyalarm.QUERY";
    public final static String TRACK = "com.example.anish.spotifyalarm.TRACK";


    private Button AlarmButton, TimePicker, CancelButton, PlayButton, SearchButton;
    private EditText setTime, setSong;
    private int iHour, iMin, mHour, mMin;
    private DateFormat pickTime;
    private AlarmManager alarmManager;

    PendingIntent pendingIntent;
    MainActivity curInst;
    Context context;

    public String cur, selTime;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;
        final Calendar c = Calendar.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);

        AlarmButton = (Button) findViewById(R.id.alarm_button);
        TimePicker = (Button) findViewById(R.id.time_button);
        CancelButton = (Button) findViewById(R.id.cancel_button);
        PlayButton = (Button) findViewById(R.id.play_button);
        SearchButton = (Button) findViewById(R.id.song_search);

        setTime = (EditText) findViewById(R.id.set_time_box);
        setSong = (EditText) findViewById(R.id.set_song_box);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        TimePicker.setOnClickListener(this);
        AlarmButton.setOnClickListener(this);
        CancelButton.setOnClickListener(this);
        PlayButton.setOnClickListener(this);
        SearchButton.setOnClickListener(this);

    }

    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();

        Bundle extras = new Bundle();

        if(v == TimePicker) {
            iHour = c.get(Calendar.HOUR_OF_DAY);
            iMin = c.get(Calendar.MINUTE);
            Log.e("MainActivity", "Timer before " + c.getTime());


            TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if(view.isShown()) {
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, 0);
                        pickTime = DateFormat.getTimeInstance();
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMin = c.get(Calendar.MINUTE);
                        cur = pickTime.format(c.getTime());
                        setTime.setText(cur);

                    }
                }
            };
            TimePickerDialog timeDialog = new TimePickerDialog
                    (v.getContext(), R.style.TimePickerTheme, mTimeListener, mHour, mMin, false);
            timeDialog.show();
            Log.d(TAG, "Hello, we trying for " + cur);
        }

        final Intent intent = new Intent(this.context, AlarmReceiver.class);
        if(v == AlarmButton) {
            selTime = setTime.getText().toString();
            Log.d(TAG, "Hello, we made an alarm for " + selTime);
            setAlarm(mHour, mMin, c, intent);
        }

        if(v == CancelButton) {
            cancelAlarm(intent);
        }

        final Intent playInt = new Intent(this.context, SpotUtils.class);
        if(v == PlayButton) {
            extras.putString("status", "play");
            playInt.putExtras(extras);
            startActivity(playInt);
        }

        Intent searchInt = new Intent(this.context, SpotUtils.class);
        if(v == SearchButton) {
            extras.putString("status", "search");
            extras.putString("query", setSong.getText().toString());
            searchInt.putExtras(extras);

            Log.d(TAG, extras.getString("status"));
            Log.d(TAG, extras.getString("query"));


            startActivity(searchInt);
        }
    }

    private void setAlarm(int hour, int min, Calendar cal, Intent intent) {
        Log.d(TAG, "Hello, we made it for " + hour + min);

        Bundle extras = new Bundle();
        extras.putString("status", "play");
        intent.putExtras(extras);

        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.SECOND, -5);
        cal.set(Calendar.MILLISECOND, 0);

        if ( System.currentTimeMillis() > cal.getTimeInMillis()) {
            cal.add(Calendar.DAY_OF_WEEK, 1);
            Log.d("MainActivity", "Time already happened today");
        }

        Log.d("MainActivity", "Hello - timer after - " + cal.getTime());

        pendingIntent = PendingIntent.getBroadcast
                (MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() , pendingIntent);
        Toast.makeText(this, "Alarm set", Toast.LENGTH_LONG).show();
  }

    private void cancelAlarm(Intent intent) {
        Log.e("MainActivity", "Canceling alarm");
        intent.putExtra("extra", "no");
        sendBroadcast(intent);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm canceled", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        curInst = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("MyActivity", "on Destroy");
    }
}
