package org.firstinspires.ftc.teamcode.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Tools.ToolTelemetry;

public class LinearOpModeBase extends LinearOpMode {

    protected BaseCollector GetCollector(){
        return null;
    }
    protected boolean IsStarted = false;

    @Override
    public void runOpMode() {
        try {
            ToolTelemetry.SetTelemetry(telemetry);
            BaseCollector _collector = GetCollector();

            if(IsStarted)
                OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).startActiveOpMode();

            waitForStart();
            resetRuntime();
            _collector.Start();

            while (opModeIsActive()) {
                _collector.Update();
                ToolTelemetry.Update();
            }

            _collector.Stop();
        }
        catch (Exception e){
            ToolTelemetry.AddLine(e.getMessage());

            for(StackTraceElement i : e.getStackTrace())
                ToolTelemetry.AddLine(i.getClassName());

            ToolTelemetry.Update();

            throw e;
        }
    }
}
