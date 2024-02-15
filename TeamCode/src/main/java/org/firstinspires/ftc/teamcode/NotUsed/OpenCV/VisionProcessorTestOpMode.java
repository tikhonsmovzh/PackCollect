package org.firstinspires.ftc.teamcode.NotUsed.OpenCV;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

@Disabled
@TeleOp
public class VisionProcessorTestOpMode extends LinearOpMode {

    VisionPortal visionPortal;
    @Override
    public void runOpMode() throws InterruptedException {
       visionPortal = new VisionPortal.Builder()
               .addProcessor(new TestVisionProcessor())
               .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
               .build();
    }
}
