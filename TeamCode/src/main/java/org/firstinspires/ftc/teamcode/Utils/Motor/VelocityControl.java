package org.firstinspires.ftc.teamcode.Utils.Motor;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Utils.UpdateHandler.IHandlered;
import org.firstinspires.ftc.teamcode.Utils.UpdateHandler.UpdateHandler;

public class VelocityControl implements IHandlered {
    private final DcMotorEx _encoder;
    private final ElapsedTime _deltaTime = new ElapsedTime();

    private double _oldPosition = 0, _speed = 0;

    public double GetVelocity() {
        return _speed;
    }

    public VelocityControl(DcMotorEx encoder) {
        _encoder = encoder;
        UpdateHandler.AddHandlered(this);
    }


    private double mathSpeed = 0d;

    public void Update() {
        double encoderPosition = _encoder.getCurrentPosition();

        double hardwareSpeed = _encoder.getVelocity();

        if (_deltaTime.seconds() > 0.085) {
            mathSpeed = (encoderPosition - _oldPosition) / _deltaTime.seconds();
            _deltaTime.reset();
            _oldPosition = encoderPosition;
        }
        _speed = hardwareSpeed + Math.round((mathSpeed - hardwareSpeed) / (double) 0x10000) * (double) 0x10000;
    }

    public void Start() {
        _deltaTime.reset();
    }
}
