package org.firstinspires.ftc.teamcode.Utils.UpdateHandler;

import java.util.ArrayList;
import java.util.List;

public class UpdateHandler {
    private static List<IHandlered> _hadlers = new ArrayList<>();

    public static void AddHandlered(IHandlered handlered){
        _hadlers.add(handlered);
    }

    public UpdateHandler(){
        _hadlers.clear();
    }

    public void Start(){
        for (IHandlered i : _hadlers)
            if(i != null)
                i.Start();

        for (IHandlered i : _hadlers)
            if(i != null)
                i.LateStart();
    }

    public void Update(){
        for (IHandlered i : _hadlers)
            i.Update();

        for (IHandlered i : _hadlers)
            i.LateUpdate();
    }

    public void Stop(){
        for (IHandlered i : _hadlers)
            i.Stop();

        for (IHandlered i : _hadlers)
            i.LateStop();
    }
}
