package org.firstinspires.ftc.teamcode.Tests.Modules;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.LinearsOpMode.LinearOpModeBase;
import org.firstinspires.ftc.teamcode.Modules.Intake;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.PidAutomatic;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.PidRouteManager;
import org.firstinspires.ftc.teamcode.Utils.Color.ColorSensor;

@TeleOp
public class ModuleTest extends LinearOpModeBase {
    @Override
    protected BaseCollector GetCollector() {
        BaseCollector collector = new BaseCollector(this);

        collector.InitOne(Intake.class);
        //collector.InitOne(DriveTrainTest.class);
        collector.InitOne(PidAutomatic.class);
        collector.InitOne(PidRouteManager.class);

        //collector.InitOne(PuckDetectorTest.class);

        //collector.InitOne(CameraTest.class);

        return collector;
    }
}
