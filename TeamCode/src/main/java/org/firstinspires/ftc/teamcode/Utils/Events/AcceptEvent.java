package org.firstinspires.ftc.teamcode.Utils.Events;

import java.util.ArrayList;
import java.util.List;

public class AcceptEvent<T> {
    private List<AcceptEventSubscriber<T>> _subscribers = new ArrayList<>();

    public boolean Invoke(T arg){
        for(AcceptEventSubscriber<T> i : _subscribers)
            if(!i.run(arg))
                return false;

        return true;
    }

    public void sub(AcceptEventSubscriber<T> subscriber){
        _subscribers.add(subscriber);
    }

    @FunctionalInterface
    public interface AcceptEventSubscriber<I>{
        boolean run(I args);
    }
}
