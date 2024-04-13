package org.firstinspires.ftc.teamcode.Tools.Color;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;

import org.firstinspires.ftc.teamcode.Tools.Configs.Configs;
import org.firstinspires.ftc.teamcode.Tools.UpdateHandler.IHandlered;
import org.firstinspires.ftc.teamcode.Tools.UpdateHandler.UpdateHandler;

public class ColorSensor implements IHandlered {
    private static final I2cAddr _addr = new I2cAddr(Configs.Intake.ColorSensorAddr);

    private final I2cDevice _device;

    private Color _color;

    public ColorSensor(I2cDevice device){
        UpdateHandler.AddHandlered(this);

        _device = device;
    }

    private void write(byte[] buffer){
        _device.enableI2cWriteMode(_addr, 0, buffer.length);
        _device.copyBufferIntoWriteBuffer(buffer);
    }

    private byte[] read(int length){
        _device.enableI2cReadMode(_addr, 0, length);

        return _device.getCopyOfReadBuffer();
    }

    @Override
    public void Start() {
        write(new byte[]{0x00});
    }

    @Override
    public void Update() {
        write(new byte[]{0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B});
        byte[] buf = read(6);

        _color = Color.ofBytes(buf);
    }

    public Color getColor(){
        return _color;
    }
}
