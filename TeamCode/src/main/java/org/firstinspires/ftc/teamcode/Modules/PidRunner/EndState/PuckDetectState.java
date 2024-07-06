package org.firstinspires.ftc.teamcode.Modules.PidRunner.EndState;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.GameManagement.GameData;
import org.firstinspires.ftc.teamcode.GameManagement.StartRobotPosition;
import org.firstinspires.ftc.teamcode.Modules.Camera.VisionPortalHandler;
import org.firstinspires.ftc.teamcode.Modules.Gyroscope;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.PidAutomatic;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.StaticTelemetry;
import org.firstinspires.ftc.teamcode.Utils.Units.Angle;

import java.util.Arrays;

public class PuckDetectState implements AutomaticStates.IRouteAction {
    private VisionPortalHandler _cameraHandler;
    private Gyroscope _gyro;
    private PidAutomatic _runner;
    private int[] _pucks = new int[Configs.PuckDetectState.Range / Configs.PuckDetectState.StepRot + 1];
    private boolean _isEnd = false;

    private int _currentRot = 0;
    private Angle _startAngle;

    @Override
    public void Init(BaseCollector collector) {
        _cameraHandler = collector.GetModule(VisionPortalHandler.class);
        _runner = collector.GetModule(PidAutomatic.class);
        _gyro = collector.GetModule(Gyroscope.class);
    }

    @Override
    public void Start() {
        _runner.RotatableForwardSpeed(0);
        _startAngle = _gyro.GetAngle();
    }

    @Override
    public void Update() {
        if(_isEnd)
            return;

        if(_currentRot > Configs.PuckDetectState.Range / Configs.PuckDetectState.StepRot) {
            _isEnd = true;

            int maxI = 0;

            for (int i = 1; i < _pucks.length; i++)
                if (_pucks[i] > _pucks[maxI])
                    maxI = i;

            _runner.RotateTo(Angle.Plus(Angle.ofDegree(maxI * Configs.PuckDetectState.StepRot), _startAngle));

            return;
        }

        int bluePucks =  _cameraHandler.GetBluePucks();

        if(bluePucks < 600000 && bluePucks > 30000)
            _pucks[_currentRot] = bluePucks;
        else
            _pucks[_currentRot] = 0;

        _runner.Rotate(Angle.ofDegree(Configs.PuckDetectState.StepRot));
        _currentRot++;
    }

    @Override
    public boolean IsEnd() {
        return _isEnd && _runner.isMovedEnd();
    }
}
