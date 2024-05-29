package org.firstinspires.ftc.teamcode.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Collectors.AutonomCollector;
import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.GameManagment.StartRobotPosition;
import org.firstinspires.ftc.teamcode.GameManagment.GameData;

@TeleOp
public class AutoOpModeRed extends LinearOpModeBase {
    @Override
    protected BaseCollector GetCollector() {
        GameData.StartPosition = StartRobotPosition.RED;
        return new AutonomCollector(this);
    }
}
