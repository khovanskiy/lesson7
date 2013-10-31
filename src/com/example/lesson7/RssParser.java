package com.example.lesson7;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Vector;

class RssParser extends DefaultHandler
{
    private Vector<Entry> entries;
    private Entry current = null;
    private boolean shouldDefine = true;
    private int type = -1;
    private int t = -1;
    private StringBuilder sb;
    private String tag = "";

    public RssParser()
    {
        entries = new Vector<Entry>();
        sb = new StringBuilder();
    }

    public Vector<Entry> getEntries()
    {
        return entries;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        if (shouldDefine)
        {
            if (qName.equals("channel"))
            {
                type = 0;
                shouldDefine = false;
                //Console.print("It is a channel");
            }
            else if (qName.equals("feed"))
            {
                type = 1;
                shouldDefine = false;
                //Console.print("It is a feed");
            }
        }
        else
        {
            //Console.print("<"+qName+">");
            if (current == null)
            {
                if (qName.equals("item") || qName.equals("entry"))
                {
                    current = new Entry();
                    //Console.print("==========NEW ITEM=============");
                }
            }
            else
            {
                sb = new StringBuilder();
                if (qName.equals("link"))
                {
                    if (type == 1) //feed
                    {
                        current.link = attributes.getValue("href");
                    }
                }
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String text = new String(ch, start, length);
        //Console.print("Text: "+text);
        sb.append(text);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (current != null)
        {
            if (qName.equals("title"))
            {
                current.title = sb.toString();
            }
            else if (qName.equals("link") && type != 1)
            {
                current.link = sb.toString();
            }
            else if( qName.equals("description") || qName.equals("summary"))
            {
                current.description = sb.toString();
            }
        }
        if (qName.equals("item") || qName.equals("entry"))
        {
            //Console.print("</"+qName+">");
            entries.add(current);
            current = null;
        }
    }
}
