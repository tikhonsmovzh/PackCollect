package org.firstinspires.ftc.teamcode.Utils.Color;

import static com.qualcomm.hardware.ams.AMSColorSensor.AMS_TCS34725_ADDRESS;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDeviceWithParameters;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Utils.StaticTelemetry;
import org.firstinspires.ftc.teamcode.Utils.UpdateHandler.IHandlered;
import org.firstinspires.ftc.teamcode.Utils.UpdateHandler.UpdateHandler;

import java.lang.reflect.Field;

public class ColorSensor implements IHandlered {
    private Color _color = new Color(255, 0, 0);

    private AdafruitI2cColorSensor _sensor;

    public ColorSensor(AdafruitI2cColorSensor sensor, boolean addToUpdate){
        _sensor = sensor;

        try {
            AMSColorSensor.class.getDeclaredField("AMS_TCS34725_ID").setAccessible(true);

            AMSColorSensor.Parameters parameters = new AMSColorSensor.Parameters(AMS_TCS34725_ADDRESS, 0x4D);

            Field paramField = I2cDeviceSynchDeviceWithParameters.class.getDeclaredField("parameters");

            paramField.setAccessible(true);

            try {
                paramField.set(_sensor, parameters);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            _sensor.initialize();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("color sensor hack not successful");
        }
        _sensor.setGain(2);

        if (_sensor instanceof SwitchableLight) {
            ((SwitchableLight) _sensor).enableLight(true);
        }

        if(addToUpdate)
            UpdateHandler.AddHandlered(this);
    }

    public ColorSensor(AdafruitI2cColorSensor sensor) {
        this(sensor, true);
    }

    @Override
    public void Update() {
        NormalizedRGBA color = _sensor.getNormalizedColors();

        _color = new Color(
                Range.clip((int) (color.red * Color.Scale), Color.Min, Color.Max),
                Range.clip((int) (color.green * Color.Scale), Color.Min, Color.Max),
                Range.clip((int) (color.blue * Color.Scale), Color.Min, Color.Max));
    }

    public Color getColor() {
        return _color;
    }
}
