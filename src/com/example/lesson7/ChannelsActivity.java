package com.example.lesson7;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.Vector;

public class ChannelsActivity extends Activity implements IEventHadler
{
    public ChannelsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channels);
        Database.init(this);
        Database.gi().addEventListener(this);

        Vector<Channel> e = new Vector<Channel>();

        adapter = new ChannelsAdapter(this, e, this);
        ListView list_view = (ListView) findViewById(R.id.channelsList);
        list_view.setAdapter(adapter);

        Reloader r = new Reloader(e);
        r.start(this);
    }

    @Override
    public void handleEvent(Event e) {
        //Console.print("Message: good");
    }

    public void gotoAddChannel(View v)
    {
        Intent intent = new Intent(this, ChannelEditActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        adapter.channels.clear();

        Cursor sth = Database.gi().query("select * from channels");
        while (sth.moveToNext())
        {
            Console.print(sth.getString(0)+" "+sth.getString(1)+" "+sth.getString(2));
            Channel channel = new Channel(sth.getString(2));
            channel.id_channel = Integer.parseInt(sth.getString(0));
            channel.title = sth.getString(1);
            adapter.channels.add(channel);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
}
