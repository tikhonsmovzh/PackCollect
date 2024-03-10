package org.firstinspires.ftc.teamcode.Tools.Timers;

import com.qualcomm.robotcore.util.ElapsedTime;

public class Timer {
    private ElapsedTime _timer = new ElapsedTime();
    private boolean _isActive = false;
    private double _time;

    private Runnable _action;

    public Timer(){
        _timer.reset();
        TimerHandler.AddTimer(this);
    }

    public void Start(double time, Runnable action){
        _timer.reset();
        _isActive = true;
        _time = time;

        _action = action;
    }

    public void Update(){
        if(_isActive && _timer.seconds() > _time){
            StopAndRun();
        }
    }

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
