package org.firstinspires.ftc.teamcode.GameManagment;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.ftccommon.external.OnCreate;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Modules.Intake;
import org.firstinspires.ftc.teamcode.Utils.Color.ColorSensor;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.Devices;
import org.firstinspires.ftc.teamcode.Utils.UpdateHandler.UpdateHandler;

public class GameManagment {
    /*@OnCreate
    public static void Create(Context context){
        Devices.Init(OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).getHardwareMap());

        DigitalChannel chanel = Devices.StartButton;
        chanel.setMode(DigitalChannel.Mode.INPUT);

        ColorSensor colorSensor = new ColorSensor(Devices.FloorSensor, false);

        new Thread(()->{
            while (true){
                if (!chanel.getState()) {
                    //colorSensor.Update();

                    //Intake.GetColorType(colorSensor.getColor(), Configs.Intake.FloorDetectSensitivity);

                    OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).initOpMode("ModuleTest");
                }
            }
        }).start();
    }*/
}
