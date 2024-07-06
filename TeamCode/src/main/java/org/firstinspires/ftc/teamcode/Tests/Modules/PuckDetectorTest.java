package org.firstinspires.ftc.teamcode.Tests.Modules;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Camera.VisionPortalHandler;
import org.firstinspires.ftc.teamcode.Modules.Intake;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.EndState.AutomaticStates;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.EndState.PuckDetectState;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.EndState.PuckRunState;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.PidAutomatic;

public class PuckDetectorTest implements IRobotModule {
    private AutomaticStates.IRouteAction _action;

    @Override
    public void Init(BaseCollector collector) {
        //collector.GetModule(Intake.class);

        _action = new PuckDetectState();
        _action.Init(collector);
    }

    @Override
    public void LateStart() {
        _action.Start();
    }

    @Override
    public void LateUpdate() {
        if(!_action.IsEnd())
            _action.Update();
    }
}
