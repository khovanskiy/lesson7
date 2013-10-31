package com.example.lesson7;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ChannelEditActivity extends Activity
{
    private int id_channel = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        id_channel = getIntent().getIntExtra("ID_CHANNEL", 0);

        if (id_channel != 0)
        {
            Cursor c = Database.gi().query("select * from channels where id_channel = "+id_channel);
            c.moveToNext();
            EditText name = (EditText)findViewById(R.id.editChannelName);
            name.setText(c.getString(1));
            EditText url = (EditText)findViewById(R.id.editChannelUrl);
            url.setText(c.getString(2));
            ((Button)findViewById(R.id.deleteButton)).setEnabled(true);
        }
        else
        {
            ((Button)findViewById(R.id.deleteButton)).setEnabled(false);
        }
    }

    public void onSaveButtonClicked(View v)
    {
        EditText name = (EditText)findViewById(R.id.editChannelName);
        EditText url = (EditText)findViewById(R.id.editChannelUrl);

        Cursor c = Database.gi().query("select * from channels where id_channel = "+id_channel);
        if (c.getCount() == 0)
        {
            Database.gi().exec("insert into channels values(null,'" + (name.getText().toString()) + "','" + (url.getText().toString()) + "')");
            Toast t = Toast.makeText(this, "New channel is added", 3000);
            t.show();
            c.close();
            finish();
        }
        else
        {
            Database.gi().exec("update channels set name = '"+(name.getText().toString())+"', link = '"+(url.getText().toString())+"' where id_channel = "+id_channel);
            Toast t = Toast.makeText(this, "Channel is updated", 3000);
            t.show();
            c.close();
        }
    }

    public void onDeleteButtonClicked(View v)
    {
        Database.gi().exec("delete from channels where id_channel = "+id_channel);
        Toast t = Toast.makeText(this, "Channel is deleted", 3000);
        t.show();
        finish();
    }
}
