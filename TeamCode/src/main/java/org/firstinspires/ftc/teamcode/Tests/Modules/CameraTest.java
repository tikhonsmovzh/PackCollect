package org.firstinspires.ftc.teamcode.Tests.Modules;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Camera.VisionPortalHandler;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Utils.StaticTelemetry;

public class CameraTest implements IRobotModule {
    private VisionPortalHandler _cameraHandler;

    @Override
    public void Init(BaseCollector collector) {
        _cameraHandler = collector.GetModule(VisionPortalHandler.class);
    }

    @Override
    public void Update() {
        StaticTelemetry.AddLines(
                "redPucks = " + _cameraHandler.GetRedPucks(),
                "bluePucks = " + _cameraHandler.GetBluePucks(),
                "redCenter = " + _cameraHandler.GetRedConcentration().X,
                "blueCenter = " + _cameraHandler.GetBlueConcentration().X,
                "blue perc = " + _cameraHandler.GetBluePerc());
    }
}
