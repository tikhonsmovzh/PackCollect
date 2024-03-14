package org.firstinspires.ftc.teamcode.Tools.Timers;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Tools.UpdateHandler.IHandlered;
import org.firstinspires.ftc.teamcode.Tools.UpdateHandler.UpdateHandler;

public class Timer implements IHandlered {
    private ElapsedTime _timer = new ElapsedTime();
    private boolean _isActive = false;
    private double _time;

    private Runnable _action;

    public Timer(){
        _timer.reset();
        UpdateHandler.AddHandlered(this);
    }

    public void Start(double time, Runnable action){
        _timer.reset();
        _isActive = true;
        _time = time;

        _action = action;
    }

    @Override
    public void Update(){
        if(_isActive && _timer.seconds() > _time){
            StopAndRun();
        }
    }

    @Override
    public void Stop(){
        _isActive = false;
    }
    public void StopAndRun(){
        Stop();
        _action.run();
    }

    public boolean IsActive(){
        return _isActive;
    }
}
