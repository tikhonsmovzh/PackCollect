package org.firstinspires.ftc.teamcode.Tools;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import java.util.List;

public class Devices {
    private static HardwareMap _hardwareDevices;

    public static DcMotorEx LeftDrive, RightDrive;

    public static DcMotorEx OdometerXLeft, OdometerY, OdometerXRight;

    public static DcMotorEx BrushMotor, SeparatorMotor;

    public static WebcamName Camera;

    public static IMU IMU;

    public static AnalogInput PuckSensor;
    public static Servo Clamp;

    public static List<LynxModule> Hubs;
    public static VoltageSensor VoltageSensor;

    public static void Init(HardwareMap map){
        if(_hardwareDevices != null)
            return;

        LeftDrive = map.get(DcMotorEx.class, "leftDrive");
        RightDrive = map.get(DcMotorEx.class, "rightDrive");

        BrushMotor = map.get(DcMotorEx.class, "odometerXRightBrush");
        OdometerXLeft = map.get(DcMotorEx.class, "odometerXLeft");
        OdometerY = map.get(DcMotorEx.class, "odometrYLED");
        OdometerXRight = map.get(DcMotorEx.class, "odometerXRightBrush");

        SeparatorMotor = map.get(DcMotorEx.class, "separatorMotor");

        Camera = map.get(WebcamName.class, "Webcam 1");

        IMU = map.get(IMU.class, "imu");

        PuckSensor = map.get(AnalogInput.class, "pixelSensor");
        Clamp = map.get(Servo.class, "clamp");

        Hubs = map.getAll(LynxModule.class);

        VoltageSensor = map.get(VoltageSensor.class, "Control Hub");

        _hardwareDevices = map;
    }
}
