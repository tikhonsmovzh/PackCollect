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
import org.firstinspires.ftc.teamcode.GameManagement.GameData;
import org.firstinspires.ftc.teamcode.Utils.PID.PIDF;
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
            Color type = GetColorType(_puckSensor.getColor(), Configs.Intake.PuckDetectSensitivity);

            boolean detectet = false;

            if(type.equals(Color.RED)) {
                _puckDetectDelay.reset();
                _redCounter++;

                detectet = true;

                puckEatEvent.Invoke(new PuckEatEvent(Color.RED, _redCounter));
            } else if(type.equals(Color.BLUE)) {
                _puckDetectDelay.reset();
                _blueCounter++;

                detectet = true;

                puckEatEvent.Invoke(new PuckEatEvent(Color.BLUE, _blueCounter));
            }

            if(detectet){
                if(type.equals(GameData.StartPosition.Color))
                    _targetSeparatorPosition += Configs.Intake.Shift;
                else
                    _targetSeparatorPosition -= Configs.Intake.Shift;
            }
        }

        Color floorType = GetColorType(_floorSensor.getColor(), Configs.Intake.FloorDetectSensitivity);

        if (floorType.equals(GameData.StartPosition.Color))
            _clampServo.setPosition(Configs.Intake.ClampRealise);
        else
            _clampServo.setPosition(Configs.Intake.ClampClamped);

        if (_brushesMotor.getCurrent(CurrentUnit.AMPS) > Configs.Intake.BrushCurrentDefend && !_brushReversTimer.IsActive()) {
            _brushesMotor.setPower(-Configs.Intake.BrushPower);

            _brushReversTimer.Start(Configs.Intake.BrushDefendReverseTime, () -> {
                _brushesMotor.setPower(Configs.Intake.BrushPower);
                _brushReversTimer.Start(Configs.Intake.DefendReversDelay, () -> {
                });
            });
        }
    }


    public static Color GetColorType(Color color, int sensitivity){
        if(color.R - Math.max(color.G, color.B) > sensitivity)
            return Color.RED;

        if(color.B - Math.max(color.G, color.R) > sensitivity)
            return Color.BLUE;

        return Color.WHITE;
    }

    @Override
    public void Start() {
        _brushesMotor.setPower(Configs.Intake.BrushPower);
    }
}
