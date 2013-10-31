package com.example.lesson7;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.Vector;

public class Database extends SQLiteOpenHelper implements IEventDispatcher, IEventHadler
{
    private static final String DATABASE_NAME = "rssreader1";
    private static final int DATABASE_VERSION = 3;

    private static Database instance = null;
    private SQLiteDatabase connection;

    public EventDispatcher event_pull;

    public Database(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        event_pull = new EventDispatcher();
    }

    public static void init(Context context)
    {
        if (instance == null)
        {
            instance = new Database(context);
        }
    }

    public static Database gi()
    {
        return instance;
    }

    public void exec(String sql)
    {
        try {
            connection = getWritableDatabase();
            connection.execSQL(sql);
        }
        catch (Exception e)
        {
            Console.print("Message: "+e.toString()+" connection = "+connection);
            dispatchEvent(new Event(this, Event.ERROR));
        }
    }

    public Cursor query(String sql)
    {
        try {
            connection = getWritableDatabase();
            Cursor c = connection.rawQuery(sql, null);
            return c;
        }
        catch (Exception e)
        {
            Console.print("Message: "+e.toString()+" connection = "+connection);
            dispatchEvent(new Event(this, Event.ERROR));
            return null;
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Console.print("onCreate");
        db.execSQL("create table channels(id_channel integer primary key autoincrement, name text not null, link text not null)");
        db.execSQL("create table entries(id_entry integer primary key autoincrement, id_channel integer, name text not null, link text not null, description text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    @Override
    public void addEventListener(IEventHadler listener) {
         event_pull.addEventListener(listener);
    }

    @Override
    public void removeEventListener(IEventHadler listener) {
         event_pull.removeEventListener(listener);
    }

    @Override
    public void dispatchEvent(Event e) {
        event_pull.dispatchEvent(e);
    }

    public void updateChannel(Channel channel)
    {
        exec("delete from entries where id_channel = "+channel.id_channel);
        XmlLoader loader = new XmlLoader(channel);
        loader.addEventListener(this);
        loader.start();
    }

    @Override
    public void handleEvent(Event e)
    {
        if (e.type == Event.COMPLETE)
        {
            Vector<Entry> v = (Vector<Entry>) e.data.get("ENTRIES");
            Channel channel = (Channel) e.data.get("CHANNEL");

            for (int i = 0; i < v.size(); ++i)
            {
                Entry entry = v.get(i);
                //Console.print("ENTRY "+entry.title);
                exec("insert into entries values(null, "+channel.id_channel+","+ DatabaseUtils.sqlEscapeString(entry.title)+","+DatabaseUtils.sqlEscapeString(entry.link)+","+DatabaseUtils.sqlEscapeString(entry.description)+")");
            }
        }
    }
}
