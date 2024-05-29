package org.firstinspires.ftc.teamcode.Modules.PidRunner.EndState;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.GameManagement.GameData;
import org.firstinspires.ftc.teamcode.GameManagement.StartRobotPosition;
import org.firstinspires.ftc.teamcode.Modules.Camera.VisionPortalHandler;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.PidAutomatic;
import org.firstinspires.ftc.teamcode.Utils.Units.Angle;

public class PuckRunState implements AutomaticStates.IRouteAction {
    private VisionPortalHandler _cameraHandler;
    private PidAutomatic _runner;

    @Override
    public void Init(BaseCollector collector) {
        _cameraHandler = collector.GetModule(VisionPortalHandler.class);
        _runner = collector.GetModule(PidAutomatic.class);
    }

    @Override
    public void Start() {}

    @Override
    public void Update() {
        _runner.Turn(Angle.ofDegree(GameData.StartPosition == StartRobotPosition.RED ? _cameraHandler.GetRedConcentration().X : _cameraHandler.GetBlueConcentration().X));
    }

    @Override
    public boolean IsEnd() {
        return false;
    }
}
