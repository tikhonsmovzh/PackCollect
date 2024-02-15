package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Tools.Motor.Motor;
import org.firstinspires.ftc.teamcode.Tools.Motor.MotorsHandler;
import org.firstinspires.ftc.teamcode.Tools.Motor.ReductorType;
import org.firstinspires.ftc.teamcode.Tools.Timers.Timer;
import org.firstinspires.ftc.teamcode.Tools.Timers.TimerHandler;
import org.firstinspires.ftc.teamcode.Tools.ToolTelemetry;


@TeleOp
public class MotorTestOpMode extends LinearOpMode {
    Runnable timerAction = null;

    @Override
    public void runOpMode() throws InterruptedException {
        MotorsHandler handler = new MotorsHandler();
        TimerHandler timersHandler = new TimerHandler();

        Timer timer1 = new Timer(), timer2 = new Timer(), timer3 = new Timer();

        Motor m = new Motor(hardwareMap.get(DcMotorEx.class, "leftmotor"), ReductorType.TWENTY);

        ToolTelemetry.SetTelemetry(telemetry);

        waitForStart();
        resetRuntime();

        timerAction = ()->{
            m.setPower(1);
            timer2.Start(3000, ()->{
                m.setPower(0.4);
                timer3.Start(3000, ()->{
                    m.setPower(0.1);
                    timer1.Start(3000, timerAction);
                });
            });
        };

        timer1.Start(0, timerAction);

        handler.Start();

        while (opModeIsActive()){
            handler.Update();
            ToolTelemetry.Update();
            timersHandler.Update();
        }
    }
}
