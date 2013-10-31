package com.example.lesson7;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStreamReader;
import java.io.Reader;

public class XmlLoader extends Thread implements IEventDispatcher
{
    private Channel channel;
    ArticlesActivity program;
    private HttpClient client = new DefaultHttpClient();
    private EventDispatcher event_pull;

    public XmlLoader(Channel channel)
    {
        this.channel = channel;
        event_pull = new EventDispatcher();
    }

    @Override
    public void run() {
        super.run();
        try {
            HttpGet request = new HttpGet(channel.url);
            HttpResponse response = client.execute(request);
            HttpEntity entiny = response.getEntity();
            Reader reader = new InputStreamReader(entiny.getContent(), EntityUtils.getContentCharSet(entiny));
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            InputSource is = new InputSource(reader);
            RssParser rp = new RssParser();
            parser.parse(is, rp);

            Event de = new Event(this, Event.COMPLETE);
            de.data.put("CHANNEL", channel);
            de.data.put("ENTRIES", rp.getEntries());
            dispatchEvent(de);
        }
        catch (Exception e)
        {
            Console.print("ERROR: "+e.getMessage());
            dispatchEvent(new Event(this, Event.ERROR));
        }
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
}
