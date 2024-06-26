package org.firstinspires.ftc.teamcode.Tests;

import static com.qualcomm.hardware.ams.AMSColorSensor.AMS_TCS34725_ADDRESS;
import static com.qualcomm.hardware.ams.AMSColorSensor.AMS_TCS34725_ID;
import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.teamcode.Tests.DeviceTest.DeviceType.ANALOG_INPUT;
import static org.firstinspires.ftc.teamcode.Tests.DeviceTest.DeviceType.BATTERY_VOLTAGE;
import static org.firstinspires.ftc.teamcode.Tests.DeviceTest.DeviceType.COLOR_SENSOR;
import static org.firstinspires.ftc.teamcode.Tests.DeviceTest.DeviceType.DC_MOTOR;
import static org.firstinspires.ftc.teamcode.Tests.DeviceTest.DeviceType.DIGITAL_CHANNEL;
import static org.firstinspires.ftc.teamcode.Tests.DeviceTest.DeviceType.DISTANCE_SENSOR;
import static org.firstinspires.ftc.teamcode.Tests.DeviceTest.DeviceType.GYRO;
import static org.firstinspires.ftc.teamcode.Tests.DeviceTest.DeviceType.NONE;
import static org.firstinspires.ftc.teamcode.Tests.DeviceTest.DeviceType.SERVO;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.hardware.ams.AMSColorSensorImpl;
import com.qualcomm.hardware.lynx.LynxI2cDeviceSynchV2;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDeviceWithParameters;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import java.lang.reflect.Field;
import java.util.Arrays;


@Config
@TeleOp
//@Disabled
public class DeviceTest extends LinearOpMode {

    public static String deviceName = "";

    public enum DeviceType {
        DC_MOTOR, DIGITAL_CHANNEL, ANALOG_INPUT, SERVO, GYRO, BATTERY_VOLTAGE, COLOR_SENSOR, DISTANCE_SENSOR, NONE
    }

    public static DeviceType deviceType = DeviceType.NONE;

    private DeviceType getDeviceClass(HardwareDevice hardwareDevice) {
        if (hardwareDevice instanceof DcMotorEx) return DC_MOTOR;
        if (hardwareDevice instanceof DigitalChannel) return DIGITAL_CHANNEL;
        if (hardwareDevice instanceof VoltageSensor) return BATTERY_VOLTAGE;
        if (hardwareDevice instanceof AnalogInput) return ANALOG_INPUT;
        if (hardwareDevice instanceof Servo) return SERVO;
        if (hardwareDevice instanceof IMU) return GYRO;
        if (hardwareDevice instanceof ColorSensor) return COLOR_SENSOR;
        if (hardwareDevice instanceof DistanceSensor) return DISTANCE_SENSOR;
        return NONE;
    }

    public static boolean sendValue = true;
    public static double valueToSend = 0;

    @Override
    public void runOpMode(){


        //AMSColorSensor.AMS_TCS34725_ID = 0x4D;


        Field IDfield = null;

        try {
            IDfield = AMSColorSensor.class.getDeclaredField("AMS_TCS34725_ID");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        IDfield.setAccessible(true);


        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        hardwareMap.getAll(IMU.class).forEach(imu -> imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(new Quaternion()))));
        //OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).startActiveOpMode();
        telemetry.addLine("All devices:");
        telemetry.addLine(Arrays.toString(hardwareMap.getAllNames(HardwareDevice.class).toArray()));
        telemetry.update();
        waitForStart();
        telemetry.update();
        while (opModeIsActive()) {
            try {
                HardwareDevice hardwareDevice = hardwareMap.get(deviceName);
                deviceType = getDeviceClass(hardwareDevice);
                telemetry.addData("Device type", deviceType);
                telemetry.addLine(hardwareDevice.getConnectionInfo());
                telemetry.addData("send value?", sendValue);
                switch (deviceType) {
                    case DC_MOTOR:
                        DcMotorEx motor = (DcMotorEx) hardwareDevice;
                        motor.setDirection(DcMotorSimple.Direction.FORWARD);
                        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                        telemetry.addData("encoder pos", motor.getCurrentPosition());
                        telemetry.addData("encoder vel", motor.getVelocity());
                        telemetry.addData("motor current", motor.getCurrent(CurrentUnit.AMPS));
                        motor.setPower(valueToSend);
                        break;
                    case SERVO:
                        Servo servo = (Servo) hardwareDevice;
                        servo.setPosition(valueToSend);
                        break;
                    case DIGITAL_CHANNEL:
                        DigitalChannel digitalChannel = (DigitalChannel) hardwareDevice;
                        digitalChannel.setMode(DigitalChannel.Mode.INPUT);
                        telemetry.addData("State", digitalChannel.getState());
                        break;
                    case ANALOG_INPUT:
                        AnalogInput analogInput = (AnalogInput) hardwareDevice;
                        telemetry.addData("Voltage", analogInput.getVoltage());
                        break;
                    case GYRO:
                        IMU imu = (IMU) hardwareDevice;
                        YawPitchRollAngles ypra = imu.getRobotYawPitchRollAngles();
                        telemetry.addLine("NOTE: REV IMU orietnation may be off");
                        telemetry.addLine("Angle units are Degrees");
                        telemetry.addData("yaw", ypra.getYaw(DEGREES));
                        telemetry.addData("pitch", ypra.getPitch(DEGREES));
                        telemetry.addData("roll", ypra.getRoll(DEGREES));
                        break;
                    case BATTERY_VOLTAGE:
                        VoltageSensor voltageSensor = (VoltageSensor) hardwareDevice;
                        telemetry.addData("Voltage", voltageSensor.getVoltage());
                        break;
                    case COLOR_SENSOR:
                        AMSColorSensor.Parameters parameters = new AMSColorSensor.Parameters(AMS_TCS34725_ADDRESS, 0x4D);
                        ColorSensor colorSensor = hardwareMap.get(ColorSensor.class, deviceName);
                        try {
                            Field paramField = I2cDeviceSynchDeviceWithParameters.class.getDeclaredField("parameters");
                            telemetry.addLine("OK");
                            try {
                                paramField.setAccessible(true);
                                paramField.set(colorSensor, parameters);
                                telemetry.addLine("Changed");
                            } catch (IllegalAccessException e) {
                                telemetry.addLine("IllegalAccessException");
                            }
                        }  catch (NoSuchFieldException e) {
                            telemetry.addLine("ERROR");
                        }
                        telemetry.addData("red", colorSensor.red());
                        telemetry.addData("green", colorSensor.green());
                        telemetry.addData("blue", colorSensor.blue());
                        telemetry.addData("alpha", colorSensor.alpha());
                        telemetry.addData("argb code", Integer.toHexString(colorSensor.argb()));
                        break;
                    case DISTANCE_SENSOR:
                        DistanceSensor distanceSensor = (DistanceSensor) hardwareDevice;
                        telemetry.addData("range (cm)", distanceSensor.getDistance(DistanceUnit.CM));
                        break;
                    case NONE:
                    default:
                        break;
                }
                if (sendValue) telemetry.addData("sent value", valueToSend);
            } catch (ClassCastException e) {
                telemetry.addLine("ERR: wrong device type selected");
            } catch (IllegalArgumentException e) {
                telemetry.addLine("Device not found");
            }
            telemetry.update();
        }
    }
}
