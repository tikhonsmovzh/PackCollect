package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Tools.Configs.Configs;
import org.firstinspires.ftc.teamcode.Tools.Devices;

public class Bread extends LinearOpMode {
    private void drive(double speed, double rotate){
        Devices.RightDrive.setPower(speed + rotate);
        Devices.LeftDrive.setPower(speed - rotate);
    }

    private int _pos = 0;
    private ElapsedTime _separatorTime = new ElapsedTime();

    private void updateSeparator(){
        if(_separatorTime.seconds() > Configs.Intake.PuckDetectDelaySec) {
            double puckDetect = Devices.PuckSensor.getVoltage();

            if (Math.abs(puckDetect - Configs.Intake.RedVoltage) < Configs.Intake.PuckDetectSensitivity) {
                _pos += Configs.Intake.Shift;
                Devices.SeparatorMotor.setTargetPosition(_pos);
                _separatorTime.reset();
            } else if (Math.abs(puckDetect - Configs.Intake.BlueVoltage) < Configs.Intake.PuckDetectSensitivity) {
                _pos -= Configs.Intake.Shift;
                Devices.SeparatorMotor.setTargetPosition(_pos);
                _separatorTime.reset();
            }
        }
    }

    private void run(double speed, double speedRotate, double time){
        drive(speed, speedRotate);

        ElapsedTime dTime = new ElapsedTime();
        dTime.reset();

        while (dTime.seconds() < time && opModeIsActive())
            updateSeparator();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        Devices.Init(this.hardwareMap);

        Devices.LeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Devices.RightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Devices.RightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        Devices.SeparatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Devices.SeparatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Devices.SeparatorMotor.setTargetPosition(_pos);

        waitForStart();
        resetRuntime();

        // тут тупо таймарами типо run(скорость вперёд, скорость вращения, время)
    }
}
