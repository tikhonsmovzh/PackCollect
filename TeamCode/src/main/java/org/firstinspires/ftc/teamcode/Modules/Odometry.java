package org.firstinspires.ftc.teamcode.Modules;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Utils.Units.Angle;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.Devices;
import org.firstinspires.ftc.teamcode.Utils.Motor.EncoderControl;
import org.firstinspires.ftc.teamcode.Utils.Units.Vector2;


@Module
public class Odometry implements IRobotModule {
    private EncoderControl _odometerY, _odometerXLeft, _odometerXRight;

    private double _oldOdometrXLeft, _oldOdometrXRight, _oldOdometrY;
    public Vector2 Position = new Vector2(), ShiftPosition = new Vector2();
    private Gyroscope _gyro;
    private Angle _oldRotate = Angle.ofRadian(0);

    @Override
    public void Init(BaseCollector collector) {
        _odometerXLeft = new EncoderControl(Devices.OdometerXLeft, Configs.Odometry.EncoderconstatOdometr, Configs.Odometry.DiametrOdometr);
        _odometerY = new EncoderControl(Devices.OdometerY, Configs.Odometry.EncoderconstatOdometr, Configs.Odometry.DiametrOdometr);
        _odometerXRight = new EncoderControl(Devices.OdometerXRight, Configs.Odometry.EncoderconstatOdometr, Configs.Odometry.DiametrOdometr);

        Devices.OdometerY.setDirection(DcMotorSimple.Direction.FORWARD);

        _gyro = collector.GetModule(Gyroscope.class);
    }

    @Override
    public void Start() {
        Reset();

        _odometerY.Start();
        _odometerXRight.Start();
        _odometerXLeft.Start();
    }

    public double GetSpeedOdometerXLeft() {
        return _odometerXLeft.GetVelocity();
    }

    public double GetSpeedOdometerXRight() {
        return -_odometerXRight.GetVelocity();
    }

    public double GetSpeedOdometerY() {
        return _odometerY.GetVelocity();
    }

    public double GetOdometerXLeft() {
        return _odometerXLeft.GetPosition();
    }

    public double GetOdometerXRight() {
        return -_odometerXRight.GetPosition();
    }

    public double GetOdometerY() {
        return _odometerY.GetPosition();
    }

    public void Reset() {
        _odometerXLeft.Reset();
        _odometerXRight.Reset();
        _odometerY.Reset();
    }

    @Override
    public void Update() {
        _odometerY.Update();
        _odometerXRight.Update();
        _odometerXLeft.Update();

        double odometrXLeft = GetOdometerXLeft(), odometrY = GetOdometerY(), odometrXRight = GetOdometerXRight();
        double odometrSpeedXLeft = GetSpeedOdometerXLeft(), odometrSpeedY = GetSpeedOdometerY(), odometrSpeedXRight = GetSpeedOdometerXRight();

        double deltaX = (odometrXLeft - _oldOdometrXLeft + odometrXRight - _oldOdometrXRight) / 2;

        double deltaY = (odometrY - _oldOdometrY) - Configs.Odometry.RadiusOdometrY * Angle.Minus(_gyro.GetAngle(), _oldRotate).getRadian();

        _oldOdometrXLeft = odometrXLeft;
        _oldOdometrXRight = odometrXRight;
        _oldOdometrY = odometrY;

        _oldRotate = _gyro.GetAngle();

        ShiftPosition = new Vector2(deltaX *
                cos(-_gyro.GetAngle().getRadian()) +
                deltaY * sin(-_gyro.GetAngle().getRadian()),
                -deltaX * sin(-_gyro.GetAngle().getRadian()) +
                        deltaY * cos(-_gyro.GetAngle().getRadian()));

        Position = Vector2.Plus(ShiftPosition, Position);
    }
}
