package com.example.lesson7;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Vector;

public class ChannelsAdapter extends ArrayAdapter<Channel>
{
    private final Context context;
    public Vector<Channel> channels;
    private ChannelsActivity program;

    public ChannelsAdapter(Context context, Vector<Channel> channels, ChannelsActivity program)
    {
        super(context, R.layout.entry, channels);
        this.context = context;
        this.channels = channels;
        this.program = program;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View entryView = inflater.inflate(R.layout.channel, parent, false);

        TextView channelName = (TextView) entryView.findViewById(R.id.channelName);
        TextView channelLink = (TextView) entryView.findViewById(R.id.channelLink);

        final Channel channel = channels.get(index);

        channelName.setText(channel.title);
        channelLink.setText(channel.url);


        entryView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(program, ArticlesActivity.class);
                intent.putExtra("ID_CHANNEL", channel.id_channel);
                intent.putExtra("URL", channel.url);
                program.startActivity(intent);
            }
        });

        entryView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                Intent intent = new Intent(program, ChannelEditActivity.class);
                intent.putExtra("ID_CHANNEL", channel.id_channel);
                program.startActivity(intent);
                return true;
            }
        });

        return entryView;
    }
}
