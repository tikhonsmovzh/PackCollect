package org.firstinspires.ftc.teamcode.GameManagement;

import org.firstinspires.ftc.teamcode.Utils.Color.Color;
import org.firstinspires.ftc.teamcode.Utils.Units.Vector2;

public enum StartRobotPosition {
    RED(new Vector2(), 0, org.firstinspires.ftc.teamcode.Utils.Color.Color.RED),
    BLUE(new Vector2(), 0, org.firstinspires.ftc.teamcode.Utils.Color.Color.BLUE);

    private StartRobotPosition(Vector2 vector, double rotation, Color color) {
        Position = vector;
        Rotation = rotation;
        Color = color;
    }

    public Vector2 Position;
    public double Rotation;
    public Color Color;
}
