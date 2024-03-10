package org.firstinspires.ftc.teamcode.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Collectors.AutonomCollector;
import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.StartRobotPosition;
import org.firstinspires.ftc.teamcode.Tools.GameData;

@TeleOp
public class AutoOpModeBlue extends LinearOpModeBase {
    @Override
    protected BaseCollector GetCollector() {
        GameData.StartPosition = StartRobotPosition.BLUE;
        return new AutonomCollector(this);
    }
}
