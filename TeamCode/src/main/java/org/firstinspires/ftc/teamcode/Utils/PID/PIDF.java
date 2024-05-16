package org.firstinspires.ftc.teamcode.Utils.PID;

import static java.lang.Math.signum;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Utils.UpdateHandler.IHandlered;
import org.firstinspires.ftc.teamcode.Utils.UpdateHandler.UpdateHandler;

public class PIDF implements IHandlered {
    private double _pCoef;
    private double _dCoef;
    private double _iCoef;
    private final double _limitI;
    private double _integrall = 0;
    private double _errOld = 0;
    public double Err = 0;
    private double _limitU = 0;
    private ElapsedTime _time = new ElapsedTime();
    private double _gCof = 0, _fCof = 0;

    public PIDF(double pCof, double iCof, double dCof, double gCof, double fCof, double limitU, double limitI){
        this(pCof, iCof, dCof, limitU, limitI);

        _gCof = gCof;
        _fCof = fCof;
    }

    public PIDF(double pCof, double iCof, double dCof, double limitU, double limitI){
        this(pCof, iCof, dCof, limitI);

        _limitU = limitU;
    }

    public PIDF(double pCof, double iCof, double dCof, double limitI) {
        _pCoef = pCof;
        _iCoef = iCof;
        _dCoef = dCof;
        _limitI = limitI;

        _limitU = 1;

        UpdateHandler.AddHandlered(this);
    }

    public void Reset() {
        _integrall = 0;
        _errOld = 0;
        Err = 0;
    }
    
    public void UpdateCoefs(double pCoef, double iCoef, double dCoef){
        _pCoef = pCoef;
        _iCoef = iCoef;
        _dCoef = dCoef;
    }

    public void UpdateCoefs(double pCoef, double iCoef, double dCoef, double gCof, double fCof){
        UpdateCoefs(pCoef, iCoef, dCoef);
        _gCof = gCof;
        _fCof = fCof;
    }

    @Override
    public void Start(){
        _time.reset();
    }

    public double Update(double error, double target){
        return Update(error) + target * _fCof;
    }

    public double Update(double error) {
        _integrall += error;

        if(Math.abs(_integrall) >= _limitI)
            _integrall = signum(_integrall) * _limitI;

        double u = error * _pCoef + (_integrall * _iCoef * _time.seconds()) + (error - _errOld) * (_dCoef / _time.seconds()) + -_gCof;

        Err = error;

        _errOld = error;

        if(Math.abs(u) > _limitU)
            return _limitU * Math.signum(u);

        _time.reset();

        return u;
    }

    public void SrtLimitU(double u){
        _limitU = u;
    }
}