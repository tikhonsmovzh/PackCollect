package org.firstinspires.ftc.teamcode.Utils.Motor;

public enum ReductorType {
    SIXTY(1440),
    TWENTY(480);

    private ReductorType(int ticks){
        Ticks = ticks;
    }

    public int Ticks;
}
