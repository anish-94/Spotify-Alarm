package com.example.anish.spotifyalarm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.DateFormat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.provider.AlarmClock;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
//import android.text.format.DateFormat;
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

import com.example.anish.spotifyalarm.AlarmReceiver;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button AlarmButton, TimePicker;
    private EditText setTime;
    private int iHour, iMin, mHour, mMin;
    private Calendar cal;
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
        setTime = (EditText) findViewById(R.id.set_time_box);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        TimePicker.setOnClickListener(this);
        AlarmButton.setOnClickListener(this);

    }

    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
        if(v == TimePicker) {
            iHour = c.get(Calendar.HOUR_OF_DAY);
            iMin = c.get(Calendar.MINUTE);

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
            TimePickerDialog timeDialog = new TimePickerDialog(v.getContext(), R.style.TimePickerTheme, mTimeListener, mHour, mMin, false);
            timeDialog.show();
            Log.d(TAG, "Hello, we trying for " + cur);
        }

        if(v == AlarmButton) {
            selTime = setTime.getText().toString();
            Log.d(TAG, "Hello, we made an alarm for " + selTime);
            setAlarm(mHour, mMin, c);
        }
    }

    private void setAlarm(int hour, int min, Calendar cal) {
        Log.d(TAG, "Hello, we made it for " + hour + min);
        cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        final Intent intent = new Intent(this.context, AlarmReceiver.class);

        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, 0);
        intent.putExtra("extra", "yes");
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() , pendingIntent);
        Toast.makeText(this, "Alarm set", Toast.LENGTH_LONG).show();
  }

    private void cancelAlarm() {

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
