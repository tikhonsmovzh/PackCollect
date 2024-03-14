package org.firstinspires.ftc.teamcode.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Modules.Manager.Module;

import java.util.ArrayList;

public class AutonomCollector extends BaseCollector {
    private static ArrayList<Class<?>> _annotatedClass;

    public AutonomCollector(LinearOpMode robot) {
        super(robot);

        InitAll();
    }
}