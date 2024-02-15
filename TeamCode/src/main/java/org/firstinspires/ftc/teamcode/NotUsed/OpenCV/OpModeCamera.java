package org.firstinspires.ftc.teamcode.NotUsed.OpenCV;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

@Disabled
@TeleOp(name = "Test Pipeline OpMode")
public class OpModeCamera extends LinearOpMode {
    VisionPortal visionPortal;


    TestVisionProcessor pipeLine = new TestVisionProcessor();

    @Override
    public void runOpMode() throws InterruptedException {
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(pipeLine)
                .build();

        visionPortal.setProcessorEnabled(pipeLine, false);

        waitForStart();

        visionPortal.setProcessorEnabled(pipeLine, true);

        while (opModeIsActive()) {
            telemetry.addData("number of parking", pipeLine.pos);//тихон чек!!!!!!!!!!!!!!!!11
            telemetry.addData("team", pipeLine.team);
            telemetry.addData("camera", visionPortal.getCameraState());
           // telemetry.addData("hcv:", visionPortal.9)
            telemetry.update();
        }


        visionPortal.close();
    }
}
