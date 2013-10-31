package com.example.lesson7;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: vvvVI_000
 * Date: 31.10.13
 * Time: 22:40
 * To change this template use File | Settings | File Templates.
 */
public class DataEvent extends Event
{
    public HashMap<String, Object> data;

    public DataEvent(IEventDispatcher target, String type)
    {
        super(target, type);
        data = new HashMap<String, Object>();
    }
}
