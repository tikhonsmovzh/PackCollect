package org.firstinspires.ftc.teamcode.Tests;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.LinearsOpMode.LinearOpModeBase;
import org.firstinspires.ftc.teamcode.Modules.Camera.VisionPortalHandler;

public class ModuleTest extends LinearOpModeBase {
    @Override
    protected BaseCollector GetCollector() {
        BaseCollector collector = new BaseCollector(this);

        collector.InitOne(VisionPortalHandler.class);

        return collector;
    }
}
