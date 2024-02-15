package org.firstinspires.ftc.teamcode.Tools;

import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Tools.Configs.Configs;


public class Battery {
    public static double ChargeDelta = 1;
    public static double Voltage = 1;

    private VoltageSensor _voltageSensor;

    public Battery(BaseCollector collector){
        _voltageSensor = Devices.VoltageSensor;
    }

    public void Update(){
        Voltage = _voltageSensor.getVoltage();
        ChargeDelta = Voltage / Configs.Battery.CorrectCharge;
    }
}
