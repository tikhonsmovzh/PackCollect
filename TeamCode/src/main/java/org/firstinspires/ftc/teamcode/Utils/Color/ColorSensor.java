package org.firstinspires.ftc.teamcode.Utils.Color;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.teamcode.Utils.StaticTelemetry;
import org.firstinspires.ftc.teamcode.Utils.UpdateHandler.IHandlered;
import org.firstinspires.ftc.teamcode.Utils.UpdateHandler.UpdateHandler;

public class ColorSensor implements IHandlered {
    private Color _color = new Color(255, 0, 0);

    private AdafruitI2cColorSensor _sensor;

    public ColorSensor(AdafruitI2cColorSensor sensor){
        _sensor = sensor;

        //_sensor.setGain(2);

        //if (_sensor instanceof SwitchableLight) {
        //    ((SwitchableLight)_sensor).enableLight(true);
        //}

        UpdateHandler.AddHandlered(this);
    }

    @Override
    public void Update() {
        //NormalizedRGBA color = _sensor.getNormalizedColors();

        //StaticTelemetry.AddLine(color.red + " " + color.green + " " + color.blue + " " + color);

        //_color = new Color((int)(color.red * 255), (int)(color.green * 255), (int)(color.blue * 255));
    }

    public Color getColor(){
        return _color;
    }
}
