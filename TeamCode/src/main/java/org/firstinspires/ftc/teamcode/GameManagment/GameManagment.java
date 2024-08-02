package org.firstinspires.ftc.teamcode.GameManagment;

import static com.qualcomm.hardware.ams.AMSColorSensor.AMS_TCS34725_ADDRESS;

import android.content.Context;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.message.redux.StopOpMode;
import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDeviceWithParameters;

import org.firstinspires.ftc.ftccommon.external.OnCreate;
import org.firstinspires.ftc.ftccommon.external.OnCreateEventLoop;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Modules.Intake;
import org.firstinspires.ftc.teamcode.Utils.Color.Color;
import org.firstinspires.ftc.teamcode.Utils.Color.ColorSensor;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.Devices;
import org.firstinspires.ftc.teamcode.Utils.UpdateHandler.UpdateHandler;

import java.lang.annotation.Target;
import java.lang.reflect.Field;

@TeleOp
@Disabled
public class GameManagment extends LinearOpMode {
    private static Thread _thread;

    private static final String RedOpModeName = "AutoOpModeRed", BlueOpModeName = "AutoOpModeBlue";

    @OnCreate
    public static void Create(Context context) {
        _thread = new Thread(() -> {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
            }

            Devices.Init(OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).getHardwareMap());

            try {
                AMSColorSensor.class.getDeclaredField("AMS_TCS34725_ID").setAccessible(true);

                AMSColorSensor.Parameters parameters = new AMSColorSensor.Parameters(AMS_TCS34725_ADDRESS, 0x4D);

                Field paramField = I2cDeviceSynchDeviceWithParameters.class.getDeclaredField("parameters");

                paramField.setAccessible(true);

                try {
                    paramField.set(Devices.FloorSensor, parameters);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                Devices.FloorSensor.initialize();
            } catch (NoSuchFieldException e) {
            }

            while (true)
                if (!Devices.StartButton.getState()) {
                    Color color = Intake.GetColorType(new Color(Devices.FloorSensor.red(), Devices.FloorSensor.green(),Devices.FloorSensor.blue()), Configs.Intake.FloorDetectSensitivity);

                    if(color.equals(Color.RED))
                        OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).initOpMode(RedOpModeName);
                    else
                        OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).initOpMode(BlueOpModeName);

                    OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).startActiveOpMode();
                    return;
                }
        });

        _thread.start();
    }

    @Override
    public void runOpMode() throws InterruptedException {

    }
}
