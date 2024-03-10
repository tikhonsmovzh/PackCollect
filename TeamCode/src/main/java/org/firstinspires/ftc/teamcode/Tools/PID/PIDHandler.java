package org.firstinspires.ftc.teamcode.Tools.PID;

import org.firstinspires.ftc.teamcode.Tools.Timers.Timer;

import java.util.ArrayList;

public class PIDHandler {
    private static ArrayList<PIDF> _pids = new ArrayList<>();

    public static void AddPid(PIDF pid){
        _pids.add(pid);
    }

    public static void Start(){
        for (PIDF i : _pids)
            i.Start();
    }

    public PIDHandler(){
        _pids.clear();
    }
}
