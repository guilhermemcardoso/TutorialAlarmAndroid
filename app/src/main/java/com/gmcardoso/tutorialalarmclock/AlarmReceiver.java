package com.gmcardoso.tutorialalarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by guilherme on 12/06/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("We are in the receiver", "top!!");

        String extras = intent.getExtras().getString("extra");

        Log.i("The EXTRA is ", extras);

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);

        serviceIntent.putExtra("extra", extras);

        context.startService(serviceIntent);

    }
}
