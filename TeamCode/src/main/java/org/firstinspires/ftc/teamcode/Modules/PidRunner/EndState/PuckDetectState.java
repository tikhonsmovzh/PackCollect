package org.firstinspires.ftc.teamcode.Modules.PidRunner.EndState;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.GameManagment.GameData;
import org.firstinspires.ftc.teamcode.GameManagment.StartRobotPosition;
import org.firstinspires.ftc.teamcode.Modules.Camera.VisionPortalHandler;
import org.firstinspires.ftc.teamcode.Modules.Gyroscope;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.PidAutomatic;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.StaticTelemetry;
import org.firstinspires.ftc.teamcode.Utils.Units.Angle;

import java.util.Arrays;
import java.util.Random;

public class PuckDetectState implements AutomaticStates.IRouteAction {
    private VisionPortalHandler _cameraHandler;
    private Gyroscope _gyro;
    private PidAutomatic _runner;
    private final int[] _bluePucks = new int[Configs.PuckDetectState.Range / Configs.PuckDetectState.StepRot + 1];
    private final int[] _redPucks = new int[Configs.PuckDetectState.Range / Configs.PuckDetectState.StepRot + 1];
    private boolean _isEnd = false;

    private int _currentRot = 0;
    private Angle _startAngle;
    private ElapsedTime _detectTimer = new ElapsedTime();
    private Random _random = new Random(51356483);

    @Override
    public void Init(BaseCollector collector) {
        _random.setSeed(5);
        _cameraHandler = collector.GetModule(VisionPortalHandler.class);
        _runner = collector.GetModule(PidAutomatic.class);
        _gyro = collector.GetModule(Gyroscope.class);
    }

    @Override
    public void Start() {
        _runner.RotatableForwardSpeed(0);
        _startAngle = _gyro.GetAngle();

        _detectTimer.reset();
        _currentRot = 0;
    }

    @Override
    public void Update() {
        if(_isEnd || _detectTimer.seconds() < Configs.PuckDetectState.DetectTimeSec)
            return;

        /*if(_random.nextInt() % 2 == 1) {
            _runner.RotateTo(Angle.ofDegree(_random.nextDouble()));
            _isEnd = true;
            return;
        }*/
        if(!Configs.GeneralSettings.IsUseCamera){
            _runner.RotateTo(Angle.ofDegree(_random.nextDouble()));
            _isEnd = true;
            return;
        }

        if(_currentRot > Configs.PuckDetectState.Range / Configs.PuckDetectState.StepRot) {
            _isEnd = true;

            if((!checkAll(_bluePucks) && !checkAll(_redPucks)))
                _runner.RotateTo(Angle.ofDegree(_random.nextDouble()));

            if(GameData.StartPosition == StartRobotPosition.BLUE) {
                if(checkAll(_bluePucks))
                    _runner.RotateTo(Angle.Plus(Angle.ofDegree(getMaxIndex(_bluePucks) * Configs.PuckDetectState.StepRot), _startAngle));
                else
                    _runner.RotateTo(Angle.Plus(Angle.ofDegree(getMaxIndex(_redPucks) * Configs.PuckDetectState.StepRot), _startAngle));
            }
            else {
                if(checkAll(_redPucks))
                    _runner.RotateTo(Angle.Plus(Angle.ofDegree(getMaxIndex(_redPucks) * Configs.PuckDetectState.StepRot), _startAngle));
                else
                    _runner.RotateTo(Angle.Plus(Angle.ofDegree(getMaxIndex(_bluePucks) * Configs.PuckDetectState.StepRot), _startAngle));
            }

            return;
        }


        _bluePucks[_currentRot] = _cameraHandler.GetBluePucks();
        _redPucks[_currentRot] = _cameraHandler.GetRedPucks();

        _runner.Rotate(Angle.ofDegree(Configs.PuckDetectState.StepRot));
        _currentRot++;

        _detectTimer.reset();
    }

    @Override
    public boolean IsEnd() {
        return _isEnd && _runner.isMovedEnd();
    }

    private boolean checkAll(int[] pucks){
        for(int i = 0; i < pucks.length - 1; i++)
            if(pucks[i] == pucks[i + 1])
                return false;

        return true;
    }

    private int getMaxIndex(int[] pucks){
        int maxI = 0;

        for (int i = 1; i < pucks.length; i++)
            if (pucks[i] > pucks[maxI])
                maxI = i;

        return maxI;
    }
}
