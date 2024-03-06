package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Camera.PuckDetections;
import org.firstinspires.ftc.teamcode.Modules.Camera.VisionPortalHandler;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Tools.Devices;
import org.firstinspires.ftc.teamcode.Tools.ToolTelemetry;

@TeleOp
public class CameraTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        try {
            ToolTelemetry.SetTelemetry(telemetry);

            IRobotModule camera = new VisionPortalHandler();

            Devices.Camera = hardwareMap.get(WebcamName.class, "Webcam 1");

            camera.Init(null);

            waitForStart();
            resetRuntime();

            camera.Start();

            while (opModeIsActive()) {
                camera.Update();
            }

            camera.Stop();
        }
        catch (Exception e){
            ToolTelemetry.AddLine(e.getMessage());

            for(StackTraceElement i : e.getStackTrace())
                ToolTelemetry.AddLine(i.getClassName());

            ToolTelemetry.Update();

            throw e;
        }
    }
}
