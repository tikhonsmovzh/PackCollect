package org.firstinspires.ftc.teamcode.Modules;

import org.firstinspires.ftc.teamcode.Tools.Units.Vector2;

public enum StartRobotPosition {
    RED(new Vector2(), 0),
    BLUE(new Vector2(), 0);

    private StartRobotPosition(Vector2 vector, double rotation) {
        Position = vector;
        Rotation = rotation;
    }

    public Vector2 Position;
    public double Rotation;
}
