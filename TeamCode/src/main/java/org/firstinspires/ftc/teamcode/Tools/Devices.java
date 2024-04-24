package org.firstinspires.ftc.teamcode.Tools;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorMRColor;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import java.util.List;

public class Devices {
    private static HardwareMap _hardwareDevices;

    public static DcMotorEx LeftDrive, RightDrive;

    public static DcMotorEx OdometerXLeft, OdometerY, OdometerXRight;

    public static DcMotorEx BrushesMotor, SeparatorMotor;

    public static WebcamName Camera;

    public static IMU IMU;

    public static I2cDevice PuckSensor, FloorSensorLeft, FloorSensorRight;
    public static Servo Clamp;

    public static List<LynxModule> Hubs;
    public static VoltageSensor VoltageSensor;


    public static void Init(HardwareMap map){
        if(_hardwareDevices != null)
            return;

        LeftDrive = map.get(DcMotorEx.class, "leftDrive");
        RightDrive = map.get(DcMotorEx.class, "rightDrive");

        BrushesMotor = map.get(DcMotorEx.class, "Brush");
        OdometerXLeft = map.get(DcMotorEx.class, "leftDrive");
        OdometerY = map.get(DcMotorEx.class, "rightDrive");
        OdometerXRight = map.get(DcMotorEx.class, "Brush");

        SeparatorMotor = map.get(DcMotorEx.class, "separatorMotor");

        //Camera = map.get(WebcamName.class, "Webcam 1");


        IMU = map.get(IMU.class, "imu");

        PuckSensor = map.get(I2cDevice.class, "puckSensor");

        FloorSensorLeft = map.get(I2cDevice.class, "floorSensorLeft");
        FloorSensorRight = map.get(I2cDevice.class, "floorSensorRight");

        Clamp = map.get(Servo.class, "clamp");

        Hubs = map.getAll(LynxModule.class);

        VoltageSensor = map.get(VoltageSensor.class, "Control Hub");

        _hardwareDevices = map;
    }
}
