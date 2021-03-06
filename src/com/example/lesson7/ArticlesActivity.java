package com.example.lesson7;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Vector;

public class ArticlesActivity extends Activity implements IEventHadler
{
    public ArticlesAdapter adapter;

    private int id_channel = 0;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Vector<Entry> e = new Vector<Entry>();
        adapter = new ArticlesAdapter(this, e, this);
        ListView list_view = (ListView) findViewById(R.id.listView);
        list_view.setAdapter(adapter);

        id_channel = getIntent().getIntExtra("ID_CHANNEL", 0);
    }

    public void onSyncButtonClicked(View v)
    {
        Cursor c = Database.gi().query("select * from channels where id_channel = "+id_channel);
        if (c.getCount() != 0)
        {
            c.moveToNext();
            Channel channel = new Channel(c.getString(2));
            channel.id_channel = Integer.parseInt(c.getString(0));
            channel.title = c.getString(1);

            adapter.entries.clear();

            Console.print("Update =  "+channel.url);
            Database.gi().addEventListener(this);
            Database.gi().updateChannel(channel);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(ProgressBar.VISIBLE);
                }
            });
        }
        else
        {
            Console.print("Not found");
        }
        c.close();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        adapter.entries.clear();

        Cursor sth = Database.gi().query("select * from entries where id_channel = "+id_channel);
        while (sth.moveToNext())
        {
            Entry entry = new Entry();
            entry.title = sth.getString(2);
            entry.link = sth.getString(3);
            entry.description = sth.getString(4);
            adapter.entries.add(entry);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void onEntries(Vector<Entry> entries)
    {
        //Console.print();
        for (int i = 0; i < entries.size(); i++)
        {
            adapter.entries.add(entries.get(i));
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void handleEvent(Event e)
    {
        if (e.type == Event.COMPLETE)
        {
            Vector<Entry> v = (Vector<Entry>) e.data.get("ENTRIES");
            onEntries(v);
            Database.gi().removeEventListener(this);
        }
    }
}
