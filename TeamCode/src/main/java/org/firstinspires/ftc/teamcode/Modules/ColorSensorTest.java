package org.firstinspires.ftc.teamcode.Modules;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Tools.Color.ColorSensor;
import org.firstinspires.ftc.teamcode.Tools.Devices;
import org.firstinspires.ftc.teamcode.Tools.StaticTelemetry;

@Module
public class ColorSensorTest implements IRobotModule {
    private ColorSensor _sensor;

    @Override
    public void Init(BaseCollector collector) {
        _sensor = new ColorSensor(Devices.PuckSensor);
    }

    @Override
    public void Update() {
        StaticTelemetry.AddLine("color = " + _sensor.getColor());
    }
}
