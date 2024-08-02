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
import org.firstinspires.ftc.teamcode.Utils.Devices;
import org.firstinspires.ftc.teamcode.Utils.I2cSonar;
import org.firstinspires.ftc.teamcode.Utils.PID.PIDF;
import org.firstinspires.ftc.teamcode.Utils.StaticTelemetry;
import org.firstinspires.ftc.teamcode.Utils.Units.Angle;

public class PuckRunState implements AutomaticStates.IRouteAction {
    private VisionPortalHandler _cameraHandler;
    private Gyroscope _gyro;
    private PidAutomatic _runner;
    private double _oldCenter = 0;

    private boolean _isEnd = false;
    private ElapsedTime _timer = new ElapsedTime();

    private I2cSonar _sonarRight, _sonarLeft;

    @Override
    public void Init(BaseCollector collector) {
        _cameraHandler = collector.GetModule(VisionPortalHandler.class);
        _runner = collector.GetModule(PidAutomatic.class);
        _gyro = collector.GetModule(Gyroscope.class);

        _sonarRight = Devices.SonarRight;
        _sonarLeft = Devices.SonarLeft;
    }

    @Override
    public void Start() {
        _timer.reset();
        _runner.RotatableForwardSpeed(Configs.PuckRunState.speed);
    }

    @Override
    public void Update() {
        if (_isEnd)
            return;

        int minSonarDist = Math.min(_sonarRight.getDistCm(), _sonarLeft.getDistCm());

        if (minSonarDist != Configs.SonarDefend.DeadDist && minSonarDist < Configs.SonarDefend.TriggerDist)
            _runner.RotatableForwardSpeed(Configs.PuckRunState.speed * ((double) minSonarDist / Configs.SonarDefend.TriggerDist));

        if (Configs.GeneralSettings.IsUseCamera) {
            double center = GameData.StartPosition == StartRobotPosition.BLUE ? _cameraHandler.GetBlueConcentration().X : _cameraHandler.GetRedConcentration().X;

            if (Math.abs(center) >= 280)
                center = -_oldCenter;
            else
                _oldCenter = center;

            _runner.RotateTo(Angle.Plus(Angle.ofDegree(center * -Configs.PuckRunState.CameraP / 2), _gyro.GetAngle()));
        }

        if ((!Configs.GeneralSettings.IsUseCamera || _cameraHandler.GetBluePucks() <= 1) && _timer.seconds() > 5)
            _isEnd = true;
    }

    @Override
    public boolean IsEnd() {
        return _isEnd;
    }
}
