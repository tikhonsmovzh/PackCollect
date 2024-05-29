package org.firstinspires.ftc.teamcode.Modules.PidRunner;

import static java.lang.Math.PI;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Drivetrain;
import org.firstinspires.ftc.teamcode.Modules.Gyroscope;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Modules.Odometry;
import org.firstinspires.ftc.teamcode.Utils.Units.Angle;
import org.firstinspires.ftc.teamcode.Utils.Battery;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.PID.PIDF;
import org.firstinspires.ftc.teamcode.Utils.Units.Vector2;


@Module
public class PidAutomatic implements IRobotModule {
    private Odometry _odometry;
    private Gyroscope _gyro;
    private Drivetrain _driverTrain;

    @Override
    public void Init(BaseCollector collector) {
        _odometry = collector.GetModule(Odometry.class);
        _gyro = collector.GetModule(Gyroscope.class);
        _driverTrain = collector.GetModule(Drivetrain.class);
    }

    public void SetSpeed(double speed) {
        _PIDFTurn.SrtLimitU(speed);
        _PIDFForward.SrtLimitU(speed);
    }

    private final PIDF _PIDFForward = new PIDF(Configs.AutomaticForwardPid.PidForwardP, Configs.AutomaticForwardPid.PidForwardI, Configs.AutomaticForwardPid.PidForwardD, Configs.DriveTrainWheels.speed, 1);
    private final PIDF _PIDFTurn = new PIDF(Configs.AutomaticRotatePid.PidRotateP, Configs.AutomaticRotatePid.PidRotateI, Configs.AutomaticRotatePid.PidRotateD, Configs.DriveTrainWheels.speed, 1);

    public void Move(Vector2 moved) {
        MoveToPoint(Vector2.Plus(moved, _targetPosition));
    }

    private double _targetAngle = 0;

    public void TurnTo(Angle angle){
        _targetAngle = angle.getRadian();
    }

    public void Turn(Angle angle){
        _targetAngle = Angle.Plus(Angle.ofRadian(_targetAngle), angle).getRadian();
    }

    public void MoveToPoint(Vector2 moved) {
        _targetPosition = moved;

        Vector2 err = Vector2.Minus(_targetPosition, _odometry.Position);

        _targetAngle = Math.atan2(err.Y, err.X);

        _PIDFForward.Reset();

        _PIDFForward.Update(err.Abs());
    }

    private Vector2 _targetPosition = new Vector2();

    public boolean isMovedEnd() {
        return Math.abs(_PIDFForward.Err) < 10d && Math.abs(_PIDFTurn.Err) < PI / 10;
    }

    @Override
    public void Update() {
        _PIDFForward.UpdateCoefs(Configs.AutomaticForwardPid.PidForwardP, Configs.AutomaticForwardPid.PidForwardI, Configs.AutomaticForwardPid.PidForwardD);
        _PIDFTurn.UpdateCoefs(Configs.AutomaticRotatePid.PidRotateP, Configs.AutomaticRotatePid.PidRotateI, Configs.AutomaticRotatePid.PidRotateD);

        if (Configs.GeneralSettings.IsAutonomEnable) {
            Vector2 dif = Vector2.Minus(_targetPosition, _odometry.Position);
            double len = dif.Abs();

            _driverTrain.Drive(
                    Math.abs(_PIDFTurn.Err) < Configs.Automatic.TurnSensitivity || (len < Configs.Automatic.LengthSensitivity && Math.abs(_PIDFTurn.Err) < Configs.Automatic.TurnSensitivity + PI) ? _PIDFForward.Update(len) / Battery.ChargeDelta : 0,
                    _PIDFTurn.Update(Angle.ChopAngle(_gyro.GetAngle().getRadian() - _targetAngle)) / Battery.ChargeDelta);
        }
    }

    @Override
    public void Start() {
        _PIDFForward.Start();
        _PIDFTurn.Start();
    }
}