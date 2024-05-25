package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Utils.Color.Color;
import org.firstinspires.ftc.teamcode.Utils.Color.ColorSensor;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.Devices;
import org.firstinspires.ftc.teamcode.Utils.Events.Event;
import org.firstinspires.ftc.teamcode.Utils.GameData;
import org.firstinspires.ftc.teamcode.Utils.PID.PIDF;
import org.firstinspires.ftc.teamcode.Utils.StaticTelemetry;
import org.firstinspires.ftc.teamcode.Utils.Timers.Timer;

@Module
public class Intake implements IRobotModule {
    private DcMotorEx _separatorMotor;
    private ColorSensor _puckSensor, _floorSensor;
    private int _targetSeparatorPosition;
    private final ElapsedTime _puckDetectDelay = new ElapsedTime();
    private final PIDF _posPid = new PIDF(Configs.Intake.SeparatorP, Configs.Intake.SeparatorI, Configs.Intake.SeparatorD, 0.75, 1);
    private final ElapsedTime _thresholdTimer = new ElapsedTime();
    private int _redCounter, _blueCounter;
    private Servo _clampServo;
    private DcMotorEx _brushesMotor;
    private Timer _brushReversTimer = new Timer();

    public class PuckEatEvent {
        public PuckEatEvent(Color color, int counter) {
            this.Color = color;
            CountEatenPucks = counter;
        }

        public Color Color;
        public int CountEatenPucks;
    }

    public Event<PuckEatEvent> puckEatEvent = new Event<>();

    @Override
    public void Init(BaseCollector collector) {
        _separatorMotor = Devices.SeparatorMotor;

        _puckSensor = new ColorSensor(Devices.PuckSensor);
        _floorSensor = new ColorSensor(Devices.FloorSensor);

        _separatorMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        Reset();

        _clampServo = Devices.Clamp;

        _brushesMotor = Devices.BrushesMotor;
    }

    public void Reset() {
        _separatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _separatorMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        _redCounter = 0;
        _blueCounter = 0;
    }

    public int GetRedCounter() {
        return _redCounter;
    }

    public int GetBlueCounter() {
        return _blueCounter;
    }

    @Override
    public void Update() {
        _posPid.UpdateCoefs(Configs.Intake.SeparatorP, Configs.Intake.SeparatorI, Configs.Intake.SeparatorD);


        if (_thresholdTimer.seconds() > Configs.Intake.ReversTimeSec) {
            _separatorMotor.setPower(_posPid.Update((double) _separatorMotor.getCurrentPosition() - _targetSeparatorPosition));

            if (_separatorMotor.getCurrent(CurrentUnit.AMPS) > Configs.Intake.ThresholdAmps && _thresholdTimer.seconds() > Configs.Intake.DefendReversDelay + Configs.Intake.ReversTimeSec ) {
                _separatorMotor.setPower(-Math.signum(_posPid.Err));
                _thresholdTimer.reset();
            }
        }

        if (_puckDetectDelay.seconds() > Configs.Intake.PuckDetectDelaySec) {
            if (_puckSensor.getColor().equals(new Color(Configs.Intake.RRedPuck, Configs.Intake.GRedPuck, Configs.Intake.BRedPuck), Configs.Intake.PuckDetectSensitivity)) {
                _targetSeparatorPosition += Configs.Intake.Shift;
                _puckDetectDelay.reset();
                _redCounter++;

                puckEatEvent.Invoke(new PuckEatEvent(Color.RED, _redCounter));
            } else if (_puckSensor.getColor().equals(new Color(Configs.Intake.RBluePuck, Configs.Intake.GBluePuck, Configs.Intake.BBluePuck), Configs.Intake.PuckDetectSensitivity)) {
                _targetSeparatorPosition -= Configs.Intake.Shift;
                _puckDetectDelay.reset();
                _blueCounter++;

                puckEatEvent.Invoke(new PuckEatEvent(Color.BLUE, _blueCounter));
            }
        }

        Color floorColor = GameData.StartPosition == StartRobotPosition.RED ?
                new Color(Configs.Intake.RRedFloor, Configs.Intake.GRedFloor, Configs.Intake.BRedFloor) :
                new Color(Configs.Intake.RBlueFloor, Configs.Intake.GBlueFloor, Configs.Intake.BBlueFloor);

        if (_floorSensor.getColor().equals(floorColor, Configs.Intake.FloorDetectSensitivity))
            _clampServo.setPosition(Configs.Intake.ClampRealise);
        else
            _clampServo.setPosition(Configs.Intake.ClampClamped);

        StaticTelemetry.AddVal("amp", _brushesMotor.getCurrent(CurrentUnit.AMPS));

        /*if (_brushesMotor.getCurrent(CurrentUnit.AMPS) > Configs.Intake.BrushCurrentDefend && !_brushReversTimer.IsActive()) {
            _brushesMotor.setPower(-Configs.Intake.BrushPower);

            _brushReversTimer.Start(Configs.Intake.BrushDefendReverseTime, () -> {
                _brushesMotor.setPower(Configs.Intake.BrushPower);
                _brushReversTimer.Start(Configs.Intake.DefendReversDelay, () -> {
                });
            });
        }*/
    }

    @Override
    public void Start() {
        //_brushesMotor.setPower(Configs.Intake.BrushPower);
    }
}
