package com.gmcardoso.tutorialalarmclock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by guilherme on 13/06/17.
 */

public class RingtonePlayingService extends Service {

    MediaPlayer mMediaPlayer;
    int startId;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {



        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("SERVICE ", "StartCommand");

        String state = intent.getExtras().getString("extra");

        Log.i("SERVICE ", " with extra " + state);



        assert state != null;
        if(state.equals("on")) {
            startId = 1;
        } else {
            startId = 0;
        }


        if(!this.isRunning && startId == 1) {

            Log.i("There is no music", " and you want start");
            mMediaPlayer = MediaPlayer.create(this, R.raw.supermario);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();

            this.isRunning = true;
            this.startId = 0;

            //Notifications
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);

            Intent mainIntent = new Intent(this.getApplicationContext(), MainActivity.class);
            mainIntent.putExtra("main", "ACORDOU");

            PendingIntent pendingMainIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);

            Notification notification = new Notification.Builder(this)
                    .setContentTitle("An alarm is going off")
                    .setContentText("Click me!")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingMainIntent)
                    .setAutoCancel(true)
                    .build();


            notificationManager.notify(0, notification);

        } else if(this.isRunning && startId == 0) {

            Log.i("There is music", " and you want end");
            mMediaPlayer.stop();
            mMediaPlayer.reset();

            this.isRunning = false;
            this.startId = 1;

        } else if(!this.isRunning && startId == 0) {

            Log.i("There is no music", " and you want end");

            this.isRunning = false;
            this.startId = 0;
        } else if(this.isRunning && startId == 1) {

            Log.i("There is music", " and you want start");

            this.isRunning = true;
            this.startId = 1;
        } else {

            Log.i("Else", " and somehow you reached here");
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        Log.i("on Destroy called", "ta dáááá");
        super.onDestroy();
        this.isRunning = false;
    }
}
