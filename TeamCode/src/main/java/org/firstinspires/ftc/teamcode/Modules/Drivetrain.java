package org.firstinspires.ftc.teamcode.Modules;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Utils.Devices;

@Module
public class Drivetrain implements IRobotModule {
    private DcMotorEx _leftDrive, _rightDrive;

    @Override
    public void Init(BaseCollector collector) {
        _leftDrive = Devices.LeftDrive;
        _rightDrive = Devices.RightDrive;

        _leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        _rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        _rightDrive.setDirection(REVERSE);
    }

    public void Drive(double speed, double rotate) {
        _leftDrive.setPower(speed - rotate);
        _rightDrive.setPower(speed + rotate);
    }

    @Override
    public void Stop() {
        _leftDrive.setPower(0);
        _rightDrive.setPower(0);
    }
}
