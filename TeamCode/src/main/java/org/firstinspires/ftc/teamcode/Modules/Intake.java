package org.firstinspires.ftc.teamcode.Modules;


import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Tools.Configs.Configs;
import org.firstinspires.ftc.teamcode.Tools.Devices;
import org.firstinspires.ftc.teamcode.Tools.GameData;
import org.firstinspires.ftc.teamcode.Tools.PIDF;
import org.firstinspires.ftc.teamcode.Tools.Vector2;

@Module
public class Intake implements IRobotModule {
    private DcMotorEx _separatorMotor;
    private AnalogInput _puckSensor;
    private double _targetSeparatorPosition;
    private final ElapsedTime _puckDetectDelay = new ElapsedTime();
    private final PIDF _posPid = new PIDF(Configs.Intake.SeparatorP, Configs.Intake.SeparatorI, Configs.Intake.SeparatorD, 1, 1);
    private final ElapsedTime _thresholdTimer = new ElapsedTime();
    private int _redCounter, _blueCounter;
    private Odometry _odometry;
    private Servo _clampServo;

    @Override
    public void Init(BaseCollector collector) {
        _separatorMotor = Devices.SeparatorMotor;
        _puckSensor = Devices.PuckSensor;

        _separatorMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        Reset();

        _odometry = collector.GetModule(Odometry.class);

        _clampServo = Devices.Clamp;
    }

    public void Reset() {
        _separatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _separatorMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        _redCounter = 0;
        _blueCounter = 0;
    }

    public int GetRedCounter(){
        return _redCounter;
    }

    public int GetBlueCounter(){
        return _blueCounter;
    }

    @Override
    public void Update() {
        _posPid.UpdateCoefs(Configs.Intake.SeparatorP, Configs.Intake.SeparatorI, Configs.Intake.SeparatorD);

        if(_thresholdTimer.seconds() > Configs.Intake.ReversTimeSec) {
            _separatorMotor.setPower(_posPid.Update(_targetSeparatorPosition - _separatorMotor.getCurrentPosition()));

            if(_separatorMotor.getCurrent(CurrentUnit.AMPS) > Configs.Intake.ThresholdAmps && Math.abs(_posPid.Err) > Configs.Intake.ThresholdSensitivity) {
                _separatorMotor.setPower(Math.signum(_posPid.Err));
                _thresholdTimer.reset();
            }
        }

        if(_puckDetectDelay.seconds() > Configs.Intake.PuckDetectDelaySec) {
            double puckDetect = _puckSensor.getVoltage();

            if (Math.abs(puckDetect - Configs.Intake.RedVoltage) < Configs.Intake.PuckDetectSensitivity) {
                _targetSeparatorPosition += Configs.Intake.Shift;
                _puckDetectDelay.reset();
                _redCounter++;
            } else if (Math.abs(puckDetect - Configs.Intake.BlueVoltage) < Configs.Intake.PuckDetectSensitivity) {
                _targetSeparatorPosition -= Configs.Intake.Shift;
                _puckDetectDelay.reset();
                _blueCounter++;
            }
        }

        if(Vector2.Minus(_odometry.Position, GameData.StartPosition.Position).Abs() < Configs.Intake.ClampRadius)
            _clampServo.setPosition(Configs.Intake.ClampRealise);
        else
            _clampServo.setPosition(Configs.Intake.ClampClamped);
    }
}
