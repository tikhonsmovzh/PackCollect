package org.firstinspires.ftc.teamcode.Tests.Modules;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Drivetrain;
import org.firstinspires.ftc.teamcode.Modules.Gyroscope;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Utils.Devices;
import org.firstinspires.ftc.teamcode.Utils.PID.PIDF;

public class DriveTrainTest implements IRobotModule {
    private Gamepad _gamepad;
    private Drivetrain _driveTrain;

    @Override
    public void Init(BaseCollector collector) {
        _gamepad = collector.Robot.gamepad1;
        _driveTrain = collector.GetModule(Drivetrain.class);
    }


    @Override
    public void Update() {
        _driveTrain.Drive(_gamepad.left_stick_y, _gamepad.right_stick_x);
    }
}
