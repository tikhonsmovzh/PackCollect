package org.firstinspires.ftc.teamcode.Utils;

import com.qualcomm.robotcore.util.ElapsedTime;

public class ExponentialFilter {
    private double _coef;
    private ElapsedTime _time = new ElapsedTime();

    public ExponentialFilter(double coef){
        _coef = coef;
    }

    public void Reset(){
        _time.reset();
    }

    public void UpdateCoef(double coef){
        _coef = coef;
    }

    public double UpdateRaw(double val, double delta){
        double result = val + delta * (_time.seconds() / (_coef + _time.seconds()));

        _time.reset();

        return result;
    }

    public double Update(double val1, double val2){
        return UpdateRaw(val1, val1 - val2);
    }
}
