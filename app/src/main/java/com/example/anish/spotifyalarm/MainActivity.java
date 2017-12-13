package com.example.anish.spotifyalarm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.DateFormat;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.provider.AlarmClock;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
//import android.text.format.DateFormat;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.w3c.dom.Text;
import com.example.anish.spotifyalarm.AlarmActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public AlarmActivity mAlarmActivity;

    private Button AlarmButton, TimePicker;
    private EditText setTime;
    private int mHour, mMin;
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        AlarmButton = (Button) findViewById(R.id.alarm_button);
        TimePicker = (Button) findViewById(R.id.time_button);
        setTime = (EditText) findViewById(R.id.set_time_box);

        TimePicker.setOnClickListener(this);

    /*    AlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hourTime = setTime.getText().toString();

                if(!TextUtils.isEmpty(hourTime)) {
                    setAlarm(hourTime);
                }
            }
        }); */
    }

    public void onClick(View v) {
        if(v == TimePicker) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMin = c.get(Calendar.MINUTE);

            TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if(view.isShown()) {
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, 0);
                        DateFormat pickTime = DateFormat.getTimeInstance();
                        String cur = pickTime.format(c.getTime());
                        setTime.setText(cur);
                    }
                }
            };
            TimePickerDialog timeDialog = new TimePickerDialog(v.getContext(), R.style.TimePickerTheme, mTimeListener, mHour, mMin, false);

 /*           TimePickerDialog timeDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            setTime.setText(hourOfDay + ":" + minute);
                        }
            }, mHour, mMin, false); */
            timeDialog.show();
        }

        if(v == AlarmButton) {
            mAlarmActivity.setAlarm(mHour, mMin);
        }
    }

//    private void setAlarm(int hour, int min) {
//  }

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
}
