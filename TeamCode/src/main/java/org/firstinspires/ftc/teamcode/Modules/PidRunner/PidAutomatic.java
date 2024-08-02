package org.firstinspires.ftc.teamcode.Modules.PidRunner;

import static java.lang.Math.PI;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Drivetrain;
import org.firstinspires.ftc.teamcode.Modules.Gyroscope;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Modules.Odometry;
import org.firstinspires.ftc.teamcode.Utils.StaticTelemetry;
import org.firstinspires.ftc.teamcode.Utils.Units.Angle;
import org.firstinspires.ftc.teamcode.Utils.Battery;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.PID.PIDF;
import org.firstinspires.ftc.teamcode.Utils.Units.Vector2;


@Module
public class PidAutomatic implements IRobotModule {
    private Gyroscope _gyro;
    private Drivetrain _driverTrain;
    private Angle _targetAngle;
    private boolean _isRotate = false;

    @Override
    public void Init(BaseCollector collector) {
        _gyro = collector.GetModule(Gyroscope.class);
        _driverTrain = collector.GetModule(Drivetrain.class);
    }

    public void SetSpeed(double speed) {
        _PIDFTurn.SetLimitU(speed);
    }

    private final PIDF _PIDFTurn = new PIDF(Configs.AutomaticRotatePid.PidRotateP, Configs.AutomaticRotatePid.PidRotateI, Configs.AutomaticRotatePid.PidRotateD, Configs.DriveTrainWheels.speed, 1);

    public void Drive(double speed, double rotate){
        _isRotate = false;
        _driverTrain.Drive(speed, rotate);
    }

    public void RotateTo(Angle angle){
        _isRotate = true;
        _targetAngle = angle;

        _PIDFTurn.Reset();
        _PIDFTurn.Update(Angle.Minus(_gyro.GetAngle(), angle).getRadian());
    }

    public void Rotate(Angle angle){
        _isRotate = true;

        _targetAngle = Angle.Plus(angle, _targetAngle == null ? Angle.ofDegree(0) : _targetAngle);

        _PIDFTurn.Reset();
        _PIDFTurn.Update(Angle.Minus(_gyro.GetAngle(), _targetAngle).getRadian());
    }

    @Override
    public void Update() {
        _PIDFTurn.UpdateCoefs(Configs.AutomaticRotatePid.PidRotateP, Configs.AutomaticRotatePid.PidRotateI, Configs.AutomaticRotatePid.PidRotateD);

        if(_targetAngle != null)
            StaticTelemetry.AddLine("target = " + _targetAngle.getDegree());

        if(!_isRotate)
            return;

        if(Math.abs(_PIDFTurn.Err) < Configs.Automatic.TurnSensitivity) {
            _isRotate = false;
            Drive(_rotatableForwardSpeed, 0);
            return;
        }

        _driverTrain.Drive(_rotatableForwardSpeed, _PIDFTurn.Update(Angle.Minus(_gyro.GetAngle(), _targetAngle).getRadian()));
    }

    private double _rotatableForwardSpeed = 0;

    public void RotatableForwardSpeed(double speed){
        _rotatableForwardSpeed = speed;
    }

    public boolean isMovedEnd(){
        return !_isRotate;
    }
}