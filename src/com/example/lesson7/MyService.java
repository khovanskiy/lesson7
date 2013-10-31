package com.example.lesson7;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Vector;

public class MyService extends IntentService
{
    public MyService()
    {
        super("My RSS service");
    }

    public void onCreate()
    {
        super.onCreate();

    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        //Console.print("onHandleIntent");
        Cursor sth = Database.gi().query("select * from channels");
        while (sth.moveToNext())
        {
            Channel channel = new Channel(sth.getString(2));
            channel.id_channel = Integer.parseInt(sth.getString(0));
            channel.title = sth.getString(1);
            Database.gi().updateChannel(channel);
        }
    }
}
