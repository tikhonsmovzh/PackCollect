package org.firstinspires.ftc.teamcode.Tests.Modules;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.LinearsOpMode.LinearOpModeBase;

@TeleOp
public class DriveImuTest extends LinearOpModeBase {
    @Override
    protected BaseCollector GetCollector() {
        BaseCollector collector = new BaseCollector(this);

        collector.InitOne(DriveTrainImuTest.class);

        return collector;
    }
}
