package com.example.lesson7;

public interface IEventDispatcher
{
    void addEventListener(IEventHadler listener);
    void removeEventListener(IEventHadler listener);
    void dispatchEvent(Event e);
}
