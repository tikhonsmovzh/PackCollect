package org.firstinspires.ftc.teamcode.Modules.PidRunner;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.EndState.AutomaticStates;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.Devices;
import org.firstinspires.ftc.teamcode.Utils.StaticTelemetry;
import org.firstinspires.ftc.teamcode.Utils.Timers.Timer;

@Module
public class PidRouteManager implements IRobotModule {
    private AutomaticStates _currentState;
    private AutomaticStates.IRouteAction _currentStateAction;
    private ElapsedTime _gameTime = new ElapsedTime();
    private BaseCollector _collector;

    private ElapsedTime _lastSwapTime = new ElapsedTime();

    private PidAutomatic _runner;

    private DigitalChannel _leftLeftButton, _leftRightButton, _rightRightButton, _rightLeftButton;

    private boolean _isDefended = false;
    private Timer _defendTimer = new Timer();

    @Override
    public void Init(BaseCollector collector) {
        _collector = collector;

        _runner = collector.GetModule(PidAutomatic.class);

        _leftLeftButton = Devices.LeftLeftButton;
        _leftRightButton = Devices.LeftRightButton;
        _rightRightButton = Devices.RightRightButton;
        _rightLeftButton = Devices.RightLeftButton;
    }

    @Override
    public void Start() {
        SwapState(AutomaticStates.PUCK_DETECT);
        _gameTime.reset();
        _lastSwapTime.reset();
    }

    @Override
    public void LateUpdate() {
        StaticTelemetry.AddLine("state = " + _currentState.name());

        if((!_rightLeftButton.getState() || !_rightRightButton.getState()) && (!_leftLeftButton.getState() || !_leftRightButton.getState())){
            _isDefended = true;

            _runner.Drive(0.5, 0);

            _defendTimer.Start(Configs.Automatic.DefendTime, ()->_isDefended = false);
        }

        if(!_leftLeftButton.getState() || !_leftRightButton.getState()){
            _isDefended = true;

            _runner.Drive(0.2, -0.5);

            _defendTimer.Start(Configs.Automatic.DefendTime, ()->_isDefended = false);
        }

        if(!_rightLeftButton.getState() || !_rightRightButton.getState()){
            _isDefended = true;

            _runner.Drive(0.2, 0.5);

            _defendTimer.Start(Configs.Automatic.DefendTime, ()->_isDefended = false);
        }

        if(_isDefended)
            return;

        _currentStateAction.Update();

        if (_currentStateAction.IsEnd() && _lastSwapTime.seconds() > Configs.Automatic.EndStateSwapDelay)
        {
            _lastSwapTime.reset();

            //if(_gameTime.seconds() > 90)
            //    SwapState(AutomaticStates.BACK_TO_HOME);

            switch (_currentState) {
                case PUCK_DETECT:
                    SwapState(AutomaticStates.RUN_TO_PUCK);
                    break;

                case RUN_TO_PUCK:
                    SwapState(AutomaticStates.PUCK_DETECT);
            }
        }
    }

    private void SwapState(AutomaticStates state) {
        _currentState = state;
        _currentStateAction = _currentState.GetNewActionInstance();
        _currentStateAction.Init(_collector);
        _currentStateAction.Start();
    }
}