package org.firstinspires.ftc.teamcode.Tests.Modules;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Camera.VisionPortalHandler;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.EndState.AutomaticStates;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.EndState.PuckRunState;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.PidAutomatic;

public class PuckDetectorTest implements IRobotModule {
    private AutomaticStates.IRouteAction _action;

    @Override
    public void Init(BaseCollector collector) {
        _action = new PuckRunState();
        _action.Init(collector);
    }

    @Override
    public void Start() {
        _action.Start();
    }

    @Override
    public void Update() {
        _action.Update();
    }
}
