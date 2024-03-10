package org.firstinspires.ftc.teamcode.Modules.PidRunner;

import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;

import java.util.ArrayList;
import java.util.List;

@Module
public class PidRouteManager implements IRobotModule {
    private List<Runnable> _route;

    private int _currentRouteAction = 0;
    private double _waitTime = -1;

    private PidAutomatic _automatic;
    private final ElapsedTime _timer = new ElapsedTime();

    @Override
    public void Init(BaseCollector collector) {
        _automatic = collector.GetModule(PidAutomatic.class);

        _route = new ArrayList<>();
    }

    private void Wait(double sleep) {
        _timer.reset();
        _waitTime = sleep;
    }


    @Override
    public void LastUpdate() {
        if (_automatic.isMovedEnd() && _timer.milliseconds() > _waitTime) {
            if (_currentRouteAction < _route.size()) {
                _route.get(_currentRouteAction).run();

                _currentRouteAction++;
            }
        }
    }
}

