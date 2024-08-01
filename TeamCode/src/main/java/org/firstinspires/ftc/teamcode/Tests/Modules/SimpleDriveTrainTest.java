package org.firstinspires.ftc.teamcode.Tests.Modules;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.LinearsOpMode.LinearOpModeBase;
import org.firstinspires.ftc.teamcode.Modules.Drivetrain;
import org.firstinspires.ftc.teamcode.Modules.Intake;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.PidAutomatic;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.PidRouteManager;

public class SimpleDriveTrainTest extends LinearOpModeBase implements IRobotModule {
    private Drivetrain _driveTrain;

    @Override
    public void Init(BaseCollector collector) {
        _driveTrain = collector.GetModule(Drivetrain.class);
    }

    @Override
    public void Start() {
        _driveTrain.Drive(0.9, 0);
    }
}
