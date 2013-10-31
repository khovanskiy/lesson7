package com.example.lesson7;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Vector;

public class Reloader extends BroadcastReceiver
{
    private static Vector<Channel> list;

    public Reloader()
    {
        super();
    }

    public Reloader(Vector<Channel> list)
    {
        super();
        this. list = list;
    }

    @Override
    public void onReceive(Context context, Intent broadcastIntent)
    {
        //Console.print("onReceive");
        /*if (broadcastIntent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED"))
        {
            //Reloader.this.start(context);
        } */
        Intent intent = new Intent(context, MyService.class);
        context.startService(intent);
    }

    public void start(Context context)
    {
        Console.print("Start reloader");
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Reloader.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 5000, pi);
    }
}
