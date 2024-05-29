package org.firstinspires.ftc.teamcode.Modules.PidRunner;

import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Modules.PidRunner.EndState.AutomaticStates;

@Module
public class PidRouteManager implements IRobotModule {
    private AutomaticStates _currentState;
    private AutomaticStates.IRouteAction _currentStateAction;
    private ElapsedTime _gameTime = new ElapsedTime();
    private BaseCollector _collector;

    @Override
    public void Init(BaseCollector collector) {
        _collector = collector;
    }

    @Override
    public void Start() {
        SwapState(AutomaticStates.PUCK_DETECT);
        _gameTime.reset();
    }

    @Override
    public void LateUpdate() {
        _currentStateAction.Update();

        if (_currentStateAction.IsEnd()) ;
        {
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