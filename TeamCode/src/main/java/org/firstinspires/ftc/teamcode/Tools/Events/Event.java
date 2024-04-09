package org.firstinspires.ftc.teamcode.Tools.Events;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class Event<T> {
    private final List<EventSubscriber<T>> _subscribers = new ArrayList();

    public void Invoke(T arg){
        for(EventSubscriber<T> i : _subscribers)
            i.run(arg);
    }

    public void sub(EventSubscriber<T> subscriber){
        _subscribers.add(subscriber);
    }

    @FunctionalInterface
    public interface EventSubscriber<I>{
        void run(I args);
    }
}
