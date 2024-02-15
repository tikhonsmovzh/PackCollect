package org.firstinspires.ftc.teamcode.Modules.Manager;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;

public interface IRobotModule {
    public default void Init(BaseCollector collector){}

    public default void Start(){}

    public default void Update(){}

    public default void LastUpdate(){}

    public default void Stop(){}
}
