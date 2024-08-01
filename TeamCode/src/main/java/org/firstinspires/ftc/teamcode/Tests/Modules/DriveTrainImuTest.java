package org.firstinspires.ftc.teamcode.Tests.Modules;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Drivetrain;
import org.firstinspires.ftc.teamcode.Modules.Gyroscope;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Utils.PID.PIDF;

public class DriveTrainImuTest implements IRobotModule {
    private Drivetrain _driveTrain;
    private Gyroscope _gyro;
    private PIDF _rotPid = new PIDF(0.05, 0, 0, 0);

    @Override
    public void Init(BaseCollector collector) {
        _driveTrain = collector.GetModule(Drivetrain.class);
        _gyro = collector.GetModule(Gyroscope.class);
    }

    @Override
    public void Update() {
        _driveTrain.Drive(0.9, _rotPid.Update(_gyro.GetAngle().getDegree()));
    }
}
