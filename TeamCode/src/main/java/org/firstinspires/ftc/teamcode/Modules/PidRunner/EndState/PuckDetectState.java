package org.firstinspires.ftc.teamcode.Modules.PidRunner.EndState;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.GameManagement.GameData;
import org.firstinspires.ftc.teamcode.GameManagement.StartRobotPosition;
import org.firstinspires.ftc.teamcode.Modules.Camera.VisionPortalHandler;
import org.firstinspires.ftc.teamcode.Modules.Gyroscope;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.PidAutomatic;
import org.firstinspires.ftc.teamcode.Utils.Units.Angle;

public class PuckDetectState implements AutomaticStates.IRouteAction {
    private VisionPortalHandler _cameraHandler;
    private Gyroscope _gyro;
    private PidAutomatic _runner;
    private int[] _pucks = new int[360];

    private Angle _startAngle;
    private boolean _isEnd = false;

    @Override
    public void Init(BaseCollector collector) {
        _cameraHandler = collector.GetModule(VisionPortalHandler.class);
        _gyro = collector.GetModule(Gyroscope.class);
        _runner = collector.GetModule(PidAutomatic.class);
    }

    @Override
    public void Start() {
        _startAngle = _gyro.GetAngle();
    }

    @Override
    public void Update() {
        _pucks[(int) _gyro.GetAngle().getDegree()] = GameData.StartPosition == StartRobotPosition.BLUE ? _cameraHandler.GetBluePucks() : _cameraHandler.GetRedPucks();

        if(Math.abs(Angle.Minus(_gyro.GetAngle(), _startAngle).getDegree()) < 2)
        {
            int maxIndex = 0;

            for(int i = 0; i < _pucks.length; i++)
                if(_pucks[i] > _pucks[maxIndex])
                    maxIndex = i;

            _runner.TurnTo(Angle.ofDegree(maxIndex));

            _isEnd = true;
        }
        else
            _runner.Turn(Angle.ofDegree(1));
    }

    @Override
    public boolean IsEnd() {
        return _isEnd;
    }
}
