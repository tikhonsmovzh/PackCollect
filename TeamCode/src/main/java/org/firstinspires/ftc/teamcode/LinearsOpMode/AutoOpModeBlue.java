package org.firstinspires.ftc.teamcode.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Collectors.AutonomCollector;
import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;

@TeleOp
public class AutoOpModeBlue extends LinearOpModeBase {
    @Override
    protected BaseCollector GetCollector() {
        return new AutonomCollector(this);
    }
}
