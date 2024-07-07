package org.firstinspires.ftc.teamcode.GameManagment;

import android.content.Context;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.message.redux.StopOpMode;
import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.ftccommon.external.OnCreate;
import org.firstinspires.ftc.ftccommon.external.OnCreateEventLoop;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Modules.Intake;
import org.firstinspires.ftc.teamcode.Utils.Color.ColorSensor;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.Devices;
import org.firstinspires.ftc.teamcode.Utils.UpdateHandler.UpdateHandler;

import java.lang.annotation.Target;

@TeleOp
@Disabled
public class GameManagment extends LinearOpMode {
    private static Thread _thread;

    private static final String OpModeName = "ModuleTest";

    @OnCreate
    public static void Create(Context context){
        _thread = new Thread(()->{
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {  }

            Devices.Init(OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).getHardwareMap());

            while (true)
                if(!Devices.StartButton.getState()) {
                    OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).initOpMode(OpModeName);
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
