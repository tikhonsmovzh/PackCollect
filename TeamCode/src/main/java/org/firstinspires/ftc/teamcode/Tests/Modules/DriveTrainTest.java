package org.firstinspires.ftc.teamcode.Tests.Modules;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Gyroscope;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Utils.Devices;
import org.firstinspires.ftc.teamcode.Utils.PID.PIDF;

public class DriveTrainTest implements IRobotModule {
    private PIDF _turnPid = new PIDF(0.4, 0, 0, 0);

    private Gyroscope _gyro;

    @Override
    public void Init(BaseCollector collector) {
        _gyro = collector.GetModule(Gyroscope.class);
    }

    @Override
    public void Update() {
        double u = _turnPid.Update(_gyro.GetAngle().getRadian());

        Devices.LeftDrive.setPower(u - 0.9);
        Devices.RightDrive.setPower(u + 0.9);
    }
}
