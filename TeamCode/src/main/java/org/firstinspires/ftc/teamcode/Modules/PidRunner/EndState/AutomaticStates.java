package org.firstinspires.ftc.teamcode.Modules.PidRunner.EndState;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;

public enum AutomaticStates {
    PUCK_DETECT(PuckDetectState.class),
    RUN_TO_PUCK(PuckRunState.class);
    //BACK_TO_HOME();

    private final Class<? extends IRouteAction> _actionClass;

    private AutomaticStates(Class<? extends IRouteAction> actionClass){
        _actionClass = actionClass;
    }

    public IRouteAction GetNewActionInstance(){
        try {
            return _actionClass.newInstance();
        }
        catch (Exception e){
            throw new RuntimeException("constructor in " + _actionClass.getSimpleName() + " is invalid arguments");
        }
    }

    public interface IRouteAction{
        public void Init(BaseCollector collector);
        public void Start();
        public void Update();
        public boolean IsEnd();
    }
}
