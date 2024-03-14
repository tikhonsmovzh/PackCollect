package org.firstinspires.ftc.teamcode.Tools.Units;

import static java.lang.Math.PI;
import static java.lang.Math.signum;

import androidx.annotation.NonNull;

public class Angle {
    protected double _radianAngle;
    
    public void setRadian(double angle){
        _radianAngle = angle;
    }
    
    public void setDegree(double angle){
        _radianAngle = Math.toRadians(angle);
    }
    
    public double getRadian(){
        return _radianAngle;
    }
    
    public  double getDegree(){
        return Math.toDegrees(_radianAngle);
    }

    @NonNull
    @Override
    public String toString() {
        return Double.toString(getDegree());
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Angle(_radianAngle);
    }
    
    private Angle(double a){
        _radianAngle = a;
    }

    public static double ChopAngle(double angle){
        while (Math.abs(angle) > PI){
            angle -= 2 * PI * signum(angle);
        }

        return angle;
    }

    public static Angle ChopAngle(Angle angle){
        return new Angle(ChopAngle(angle.getRadian()));
    }

    public static Angle Minus(Angle angl1, Angle angl2){
        return new Angle(ChopAngle(angl1.getRadian() - angl2.getRadian()));
    }

    public static Angle ofRadian(double rad){
        return new Angle(rad);
    }

    public static Angle ofDegree(double deg){
        return new Angle(Math.toRadians(deg));
    }
}
