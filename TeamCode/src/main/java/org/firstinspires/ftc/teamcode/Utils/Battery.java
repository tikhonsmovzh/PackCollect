package org.firstinspires.ftc.teamcode.Utils;

import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.UpdateHandler.IHandlered;
import org.firstinspires.ftc.teamcode.Utils.UpdateHandler.UpdateHandler;


public class Battery implements IHandlered {
    public static double ChargeDelta = 1;
    public static double Voltage = 1;

    private VoltageSensor _voltageSensor;

    public Battery(BaseCollector collector){
        _voltageSensor = Devices.VoltageSensor;
        UpdateHandler.AddHandlered(this);
    }

    @Override
    public void Update(){
        Voltage = _voltageSensor.getVoltage();
        ChargeDelta = Voltage / Configs.Battery.CorrectCharge;
    }
}
