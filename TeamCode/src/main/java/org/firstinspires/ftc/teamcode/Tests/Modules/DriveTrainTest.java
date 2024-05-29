package org.firstinspires.ftc.teamcode.Tests.Modules;

import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Utils.Devices;

public class DriveTrainTest implements IRobotModule {
    @Override
    public void Start() {
        Devices.RightDrive.setPower(1);
        Devices.LeftDrive.setPower(1);
    }
}
