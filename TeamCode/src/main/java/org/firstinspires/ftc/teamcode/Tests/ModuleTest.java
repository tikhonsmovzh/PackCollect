package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.LinearsOpMode.LinearOpModeBase;
import org.firstinspires.ftc.teamcode.Modules.Camera.VisionPortalHandler;
import org.firstinspires.ftc.teamcode.Modules.ColorSensorTest;
import org.firstinspires.ftc.teamcode.Modules.Intake;

@TeleOp
public class ModuleTest extends LinearOpModeBase {
    @Override
    protected BaseCollector GetCollector() {
        BaseCollector collector = new BaseCollector(this);

        collector.InitOne(Intake.class);

        return collector;
    }
}
