package org.firstinspires.ftc.teamcode.Modules.Manager;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Utils.UpdateHandler.IHandlered;

public interface IRobotModule extends IHandlered {
    public default void Init(BaseCollector collector){}
}
