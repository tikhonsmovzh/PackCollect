package org.firstinspires.ftc.teamcode.Utils;


import com.acmerobotics.dashboard.canvas.Canvas;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Utils.Color.Color;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.Units.Vector2;

public class StaticTelemetry {
    private static Telemetry _telemetry;

    private static TelemetryPacket _packet = new TelemetryPacket();
    private static TelemetryPacket _threadPacket = null;

    public static void Update() {
        if (!Configs.GeneralSettings.TelemetryOn)
            return;

        _telemetry.update();

        _threadPacket = _packet;

        FtcDashboard.getInstance().sendTelemetryPacket(_threadPacket);
        _packet = new TelemetryPacket();
    }

    public static void Init(LinearOpMode opMode){
        _telemetry = opMode.telemetry;

        //new Thread(()->{
        //    while (opMode.opModeIsActive() || opMode.opModeInInit()) {
        //        if (_threadPacket == null)
        //            return;

        //        FtcDashboard.getInstance().sendTelemetryPacket(_threadPacket);
        //
        //        _threadPacket = null;
        //    }
        //}).start();
    }

    public static Canvas GetCanvas() {
        return _packet.fieldOverlay();
    }

    public static void DrawCircle(Vector2 pos, double radius, Color color) {
        DrawCircle(pos, radius, color.toString());
    }

    public static void DrawCircle(Vector2 pos, double radius, String color) {
        if (Configs.GeneralSettings.TelemetryOn) {
            _packet.fieldOverlay().setFill(color);
            _packet.fieldOverlay().setRotation(0);
            _packet.fieldOverlay().fillCircle(DistanceUnit.INCH.fromCm(pos.X), DistanceUnit.INCH.fromCm(pos.Y), DistanceUnit.INCH.fromCm(radius));
        }
    }

    public static void DrawRect(Vector2 pos, Vector2 size, double rotate, Color color) {
        DrawRect(pos, size, rotate, color.toString());
    }

    public static void DrawRect(Vector2 pos, Vector2 size, double rotate, String color) {
        if (Configs.GeneralSettings.TelemetryOn) {
            _packet.fieldOverlay().setFill(color);
            _packet.fieldOverlay().setRotation(rotate);
            _packet.fieldOverlay().fillRect(DistanceUnit.INCH.fromCm(pos.X), DistanceUnit.INCH.fromCm(pos.Y), DistanceUnit.INCH.fromCm(size.X), DistanceUnit.INCH.fromCm(size.Y));
        }
    }

    public static void AddLine(String str) {
        if (Configs.GeneralSettings.TelemetryOn) {
            _telemetry.addLine(str);
            _packet.addLine(str);
        }
    }

    public static void AddLines(String... strs) {
        if (Configs.GeneralSettings.TelemetryOn) {
            for (String i : strs)
                AddLine(i + " ");
        }
    }

    public static void AddVal(String name, Object val) {
        if (Configs.GeneralSettings.TelemetryOn) {
            _telemetry.addData(name, val);
            _packet.put(name, val);
        }
    }
}