package org.firstinspires.ftc.teamcode.Utils;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;

import java.util.List;

public class Devices {
    private static HardwareMap _hardwareDevices;

    public static DcMotorEx LeftDrive, RightDrive;

    public static DcMotorEx OdometerXLeft, OdometerY, OdometerXRight;

    public static DcMotorEx BrushesMotor, SeparatorMotor;

    public static WebcamName Camera;

    public static IMU IMU;

    public static AdafruitI2cColorSensor FloorSensor, PuckSensor;
    public static Servo Clamp;

    public static List<LynxModule> Hubs;
    public static VoltageSensor VoltageSensor;
    public static DigitalChannel StartButton, RightRightButton, RightLeftButton, LeftRightButton, LeftLeftButton;

    public static I2cSonar SonarLeft, SonarRight;

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

        if(Configs.GeneralSettings.IsUseCamera)
            Camera = map.get(WebcamName.class, "Webcam 1");

        IMU = map.get(IMU.class, "imu");

        PuckSensor = map.get(AdafruitI2cColorSensor.class, "puckSensor");

        FloorSensor = map.get(AdafruitI2cColorSensor.class, "floorSensor");

        Clamp = map.get(Servo.class, "clamp");

        Hubs = map.getAll(LynxModule.class);

        VoltageSensor = map.get(VoltageSensor.class, "Control Hub");

        StartButton = map.get(DigitalChannel.class, "startButton");

        RightLeftButton = map.get(DigitalChannel.class, "rightLeftButton");
        RightRightButton = map.get(DigitalChannel.class, "rightRightButton");

        LeftRightButton = map.get(DigitalChannel.class, "leftRightButton");
        LeftLeftButton = map.get(DigitalChannel.class, "leftLeftButton");

        SonarRight = map.get(I2cSonar.class, "sonarRight");
        SonarRight = map.get(I2cSonar.class, "sonarLeft");

        _hardwareDevices = map;
    }
}
