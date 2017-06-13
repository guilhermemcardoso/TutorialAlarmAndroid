package com.gmcardoso.tutorialalarmclock;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import java.util.Calendar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static final String ALARM_ON = "Alarm ON";
    private static final String ALARM_OFF = "Alarm OFF";

    private AlarmManager alarmManager;
    private TimePicker timePicker;
    private TextView statusAlarmText;
    private TextView setAlarmText;
    private TextView cancelAlarmText;
    private Context context;
    private PendingIntent pendingIntent;
    private Calendar calendar;

    @Override
    protected void onResume() {
        super.onResume();

        if(isMyServiceRunning(RingtonePlayingService.class)) {
            Log.i("ON RESUME", "VOLTOU!");
        } else {
            Log.i("ON RESUME", "extra igual a NULL");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        this.context = this;

        Log.i("ON CREATE", "estou no onCreate da activity");

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        statusAlarmText = (TextView) findViewById(R.id.status);

        final Intent alarmIntent = new Intent(this.context, AlarmReceiver.class);

        setAlarmText = (TextView) findViewById(R.id.set_alarm);

        setAlarmText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                int hour, minute;
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
//                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getMinute());
//                    hour = timePicker.getHour();
//                    minute = timePicker.getMinute();
//                } else {
//                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
//                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentMinute());
//                    hour = timePicker.getCurrentHour();
//                    minute = timePicker.getCurrentMinute();
//                }
//
//                String hour_string, minute_string;
//                hour_string = String.valueOf(hour);
//                minute_string = String.valueOf(minute);

                calendar = Calendar.getInstance();

                setStatus(ALARM_ON);

                alarmIntent.putExtra("extra", "on");

                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                        alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 5000, pendingIntent);
            }
        });

        cancelAlarmText = (TextView) findViewById(R.id.cancel_alarm);
        cancelAlarmText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatus(ALARM_OFF);

                alarmManager.cancel(pendingIntent);

                alarmIntent.putExtra("extra", "off");

                sendBroadcast(alarmIntent);
            }
        });

    }

    public void setStatus(String status) {
        statusAlarmText.setText(status);
    }

    private boolean isMyServiceRunning(Class serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

}
