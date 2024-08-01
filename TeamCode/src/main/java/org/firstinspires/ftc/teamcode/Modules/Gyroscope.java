package org.firstinspires.ftc.teamcode.Modules;

import static org.firstinspires.ftc.teamcode.Utils.Units.Angle.ChopAngle;
import static java.lang.Math.signum;
import static java.lang.Math.toDegrees;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.ExponentialFilter;
import org.firstinspires.ftc.teamcode.Utils.Units.Angle;
import org.firstinspires.ftc.teamcode.Utils.Devices;
import org.firstinspires.ftc.teamcode.Utils.StaticTelemetry;

@Module
public class Gyroscope implements IRobotModule {
    private IMU _imu;
    private Odometry _odometrs;

    private double _allRadians, _oldOdometer;

    private static double _startRotateRadian;

    private long _iterations = 0;
    private ExponentialFilter _margeFilter = new ExponentialFilter(Configs.Gyroscope.MergerCoefSeconds);

    @Override
    public void Init(BaseCollector collector) {
        _imu = Devices.IMU;
        //_odometrs = collector.GetModule(Odometry.class);

        _imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.UP)));

        Reset();
    }

    public Angle GetAngle() {
        return Angle.ofRadian(_allRadians);
    }

    @Override
    public void Update() {
        _allRadians = ChopAngle(_imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) - _startRotateRadian);

        /*_margeFilter.UpdateCoef(Configs.Gyroscope.MergerCoefSeconds);

        if (Configs.GeneralSettings.IsUseOdometers) {
            double odometerTurn = ChopAngle(ChopAngle((-_odometrs.GetOdometerXLeft() / Configs.Odometry.RadiusOdometrXLeft + _odometrs.GetOdometerXRight() / Configs.Odometry.RadiusOdometrXRight) / 2));

            if(_iterations % Configs.Gyroscope.Iterations == 0){
                double gyroRotate = ChopAngle(ChopAngle(_imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) - _startRotateRadian));

                _allRadians = _margeFilter.UpdateRaw(odometerTurn, odometerTurn - gyroRotate);
            }
            else
                _allRadians += ChopAngle(odometerTurn - _oldOdometer);

            _oldOdometer = odometerTurn;
        } else {
            _allRadians = ChopAngle(_imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) - _startRotateRadian);
        }

        _allRadians = ChopAngle(_allRadians);

        _iterations++;*/
    }

    public void Reset() {
        _startRotateRadian = _imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        //_margeFilter.Reset();
    }
}
