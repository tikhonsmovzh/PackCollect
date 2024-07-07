package org.firstinspires.ftc.teamcode.LinearsOpMode;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.ftccommon.external.OnCreate;
import org.firstinspires.ftc.teamcode.Collectors.AutonomCollector;
import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.GameManagment.GameData;
import org.firstinspires.ftc.teamcode.GameManagment.StartRobotPosition;

@TeleOp
public class AutoOpModeBlue extends LinearOpModeBase {
    @Override
    protected BaseCollector GetCollector() {
        GameData.StartPosition = StartRobotPosition.BLUE;
        IsStarted = true;
        return new AutonomCollector(this);
    }
}
