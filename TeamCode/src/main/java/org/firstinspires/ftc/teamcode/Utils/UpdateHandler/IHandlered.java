package org.firstinspires.ftc.teamcode.Utils.UpdateHandler;

public interface IHandlered {
    default void Start(){}
    default void Update(){}
    default void LateUpdate(){}
    default void LateStart(){}
    default void Stop(){}
    default void LateStop(){}
}
