package org.firstinspires.ftc.teamcode.Tools.Motor;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Tools.UpdateHandler.IHandlered;
import org.firstinspires.ftc.teamcode.Tools.UpdateHandler.UpdateHandler;

public class EncoderControl implements IHandlered {
    private final DcMotorEx _encoder;
    private final VelocityControl _velControl;
    private final double _ticks, _diameter;
    private double _startPos = 0, _pos;

    public EncoderControl(DcMotorEx encoder, ReductorType type, double diameter){
        this(encoder, type.Ticks, diameter);
        UpdateHandler.AddHandlered(this);
    }

    public EncoderControl(DcMotorEx encoder, double ticksInEncoder, double diameter){
        _encoder = encoder;
        _velControl = new VelocityControl(encoder);

        _diameter = diameter;

        _ticks = ticksInEncoder;
    }

    public double GetPosition(){
        return _pos / _ticks * Math.PI * _diameter;
    }

    public double GetVelocity(){
        return _velControl.GetVelocity() / _ticks * Math.PI * _diameter;
    }

    @Override
    public void Update(){
        _pos = _encoder.getCurrentPosition() - _startPos;
    }

    public void Reset(){
        _startPos = _encoder.getCurrentPosition();
    }
}
