package org.firstinspires.ftc.teamcode.Modules.PidRunner.EndState;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.GameManagment.GameData;
import org.firstinspires.ftc.teamcode.GameManagment.StartRobotPosition;
import org.firstinspires.ftc.teamcode.Modules.Camera.VisionPortalHandler;
import org.firstinspires.ftc.teamcode.Modules.Gyroscope;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.PidAutomatic;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.PID.PIDF;
import org.firstinspires.ftc.teamcode.Utils.StaticTelemetry;
import org.firstinspires.ftc.teamcode.Utils.Units.Angle;

public class PuckRunState implements AutomaticStates.IRouteAction {
    private VisionPortalHandler _cameraHandler;
    private Gyroscope _gyro;
    private PidAutomatic _runner;
    private double _oldCenter = 0;

    private boolean _isEnd = false;

    @Override
    public void Init(BaseCollector collector) {
        _cameraHandler = collector.GetModule(VisionPortalHandler.class);
        _runner = collector.GetModule(PidAutomatic.class);
        _gyro = collector.GetModule(Gyroscope.class);
    }

    @Override
    public void Start() {
    }

    @Override
    public void Update() {
        if(_isEnd)
            return;

        double center = _cameraHandler.GetBlueConcentration().X;

        if (Math.abs(center) >= 280)
            center = -_oldCenter;
        else
            _oldCenter = center;

        _runner.RotatableForwardSpeed(Configs.PuckRunState.speed);
        _runner.RotateTo(Angle.Plus(Angle.ofDegree(center * -Configs.PuckRunState.CameraP / 2), _gyro.GetAngle()));

        StaticTelemetry.AddLine("pucks = " + _cameraHandler.GetBluePucks());

        if(_cameraHandler.GetBluePucks() <= 30000 || _cameraHandler.GetBluePucks() > 600000)
            _isEnd = true;
    }

    @Override
    public boolean IsEnd() {
        return _isEnd;
    }
}
