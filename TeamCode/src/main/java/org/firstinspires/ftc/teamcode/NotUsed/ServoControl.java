package org.firstinspires.ftc.teamcode.NotUsed;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class ServoControl {
    Servo servo;
    private double servoRangeDeg = 180;
    private double servoSekDeg = 180;
    private double targetDegree = 90;
    private double nowDegree = 90;
    ElapsedTime servotime = new ElapsedTime();

    public ServoControl(Servo servo, double sDr, double sdk) {

        this.servo = servo;
        servoRangeDeg = sDr;
        servoSekDeg = 60 / sdk;
    }

    public void setPositionDegres(double deg) {
        deg = deg / servoRangeDeg;
        servo.setPosition(deg);
        servotime.reset();
    }

    public double getTargetDegree() {
        return targetDegree;
    }

    public double getNowDegree() {
        if (!atg) {
            nowDegree += nowDegree + servoSekDeg * servotime.seconds();
            servotime.reset();
        }
        return nowDegree;
    }

    private boolean atg = true;

    public double achiveTargDegree() {

        if (nowDegree >= targetDegree) {
            atg = true;
        } else {
            atg = false;
        }

        return nowDegree;
    }}