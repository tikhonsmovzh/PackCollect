package org.firstinspires.ftc.teamcode.Modules;

import static java.lang.Math.signum;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Utils.Units.Angle;
import org.firstinspires.ftc.teamcode.Utils.Devices;
import org.firstinspires.ftc.teamcode.Utils.StaticTelemetry;

@Module
public class Gyroscope implements IRobotModule {
    private IMU _imu;
    private Angle _rotate, _startRotateRadian;


    @Override
    public void Init(BaseCollector collector) {
        _imu = Devices.IMU;
        _imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.UP)));
    }

    @Override
    public void Start() {
        Reset();
    }

    public Angle GetAngle() {
        return _rotate;
    }

    @Override
    public void Update() {
        _rotate = Angle.Minus(Angle.ofRadian(_imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)), _startRotateRadian);
        
        StaticTelemetry.AddLine("rotate = " + _rotate.getRadian());
    }

    public void Reset() {
        _startRotateRadian = Angle.ofRadian(_imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
    }
}
