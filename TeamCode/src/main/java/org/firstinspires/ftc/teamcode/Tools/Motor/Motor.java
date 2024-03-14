package org.firstinspires.ftc.teamcode.Tools.Motor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Tools.Battery;
import org.firstinspires.ftc.teamcode.Tools.Configs.Configs;
import org.firstinspires.ftc.teamcode.Tools.PID.PIDF;
import org.firstinspires.ftc.teamcode.Tools.UpdateHandler.IHandlered;
import org.firstinspires.ftc.teamcode.Tools.UpdateHandler.UpdateHandler;


public class Motor implements IHandlered {
    public final DcMotorEx Motor;

    private PIDF _velocityPid;

    private final ReductorType _encoderType;

    private boolean _isCustomPid = false;

    private final VelocityControl _velControl;

    public Motor(DcMotorEx motor, ReductorType type, PIDF pid){
        this(motor, type);

        _velocityPid = pid;

        _isCustomPid = true;
    }

    public Motor(DcMotorEx motor, ReductorType type){
        Motor = motor;

        _encoderType = type;

        _velocityPid = new PIDF(Configs.Motors.DefultP, Configs.Motors.DefultI, Configs.Motors.DefultD, 0, Configs.Motors.DefultF, 50, 0);

        UpdateHandler.AddHandlered(this);

        _velControl = new VelocityControl(motor);
    }

    public void setDirection(DcMotorSimple.Direction dir){
        Motor.setDirection(dir);
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior power){
        Motor.setZeroPowerBehavior(power);
    }

    public void setMode(DcMotor.RunMode mode){
        Motor.setMode(mode);
    }

    public double getCurrentPosition(){
        return Motor.getCurrentPosition();
    }

    @Override
    public void Update(){
        if(!_isCustomPid)
            _velocityPid.UpdateCoefs(Configs.Motors.DefultP, Configs.Motors.DefultI, Configs.Motors.DefultD, 0, Configs.Motors.DefultF);

        double pidSpeed = _velocityPid.Update(_targetEncoderSpeed - _velControl.GetVelocity(), _targetEncoderSpeed);

        Motor.setPower(pidSpeed / Battery.ChargeDelta);
    }

    private double _targetEncoderSpeed = 0;

    public void setPower(double speed){
        setEncoderPower(speed * _encoderType.Ticks);
    }

    public void setEncoderPower(double speed){
        _targetEncoderSpeed = speed;
    }
    
    public VelocityControl GetVelocityController(){
        return _velControl;
    }
}
