package org.firstinspires.ftc.teamcode.Modules.PidRunner.EndState;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.GameManagment.GameData;
import org.firstinspires.ftc.teamcode.GameManagment.StartRobotPosition;
import org.firstinspires.ftc.teamcode.Modules.Camera.VisionPortalHandler;
import org.firstinspires.ftc.teamcode.Modules.Gyroscope;
import org.firstinspires.ftc.teamcode.Modules.Odometry;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.PidAutomatic;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.Devices;
import org.firstinspires.ftc.teamcode.Utils.I2cSonar;
import org.firstinspires.ftc.teamcode.Utils.PID.PIDF;
import org.firstinspires.ftc.teamcode.Utils.Units.Angle;
import org.firstinspires.ftc.teamcode.Utils.Units.Vector2;

import java.util.Random;

public class HomeBackState implements AutomaticStates.IRouteAction {
    private boolean _isEnd = false;
    private PidAutomatic _runner;

    private I2cSonar _sonarRight, _sonarLeft;
    private DigitalChannel _leftLeftButton, _leftRightButton, _rightRightButton, _rightLeftButton;

    private Odometry _odometry;

    @Override
    public void Init(BaseCollector collector) {
        _runner = collector.GetModule(PidAutomatic.class);
        _odometry = collector.GetModule(Odometry.class);

        _sonarRight = Devices.SonarRight;
        _sonarLeft = Devices.SonarLeft;

        _leftLeftButton = Devices.LeftLeftButton;
        _leftRightButton = Devices.LeftRightButton;
        _rightRightButton = Devices.RightRightButton;
        _rightLeftButton = Devices.RightLeftButton;
    }

    @Override
    public void Start() {
        if(!Configs.GeneralSettings.IsUseOdometers) {
            _runner.RotateTo(Angle.ofDegree(180));

            _runner.RotatableForwardSpeed(Configs.PuckRunState.speed);

            return;
        }

        Vector2 dif = Vector2.Minus(_odometry.Position, new Vector2(0, 0));

        _runner.RotateTo(Angle.ofRadian(Math.atan2(dif.Y,  dif.X)));
    }
    @Override
    public void Update() {
        if(!_runner.isMovedEnd())
            return;

        int minSonarDist = Math.min(_sonarRight.getDistCm(), _sonarLeft.getDistCm());

        if (minSonarDist != Configs.SonarDefend.DeadDist && minSonarDist < Configs.SonarDefend.TriggerDist)
            _runner.RotatableForwardSpeed(Configs.PuckRunState.speed * ((double) minSonarDist / Configs.SonarDefend.TriggerDist));

        if(_isEnd)
            return;

        if(!Configs.GeneralSettings.IsUseOdometers) {
            if (!_leftLeftButton.getState() || !_leftRightButton.getState() || !_rightLeftButton.getState() || !_rightRightButton.getState()) {
                if (_isMoveToHome) {
                    _runner.RotateTo(Angle.ofDegree(0));
                    _runner.RotatableForwardSpeed(Configs.PuckRunState.speed);

                    _isEnd = true;

                    return;
                }

                _runner.RotateTo(Angle.ofDegree(180));
                _runner.RotatableForwardSpeed(Configs.PuckRunState.speed);

                _isMoveToHome = true;
            }

            return;
        }

        if(_runner.isMovedEnd())
            _runner.RotatableForwardSpeed(Configs.PuckRunState.speed);
    }

    private boolean _isMoveToHome = false;

    @Override
    public boolean IsEnd() {
        return _isEnd && _runner.isMovedEnd();
    }
}
