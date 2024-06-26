package org.firstinspires.ftc.teamcode.Utils.Configs;

import com.acmerobotics.dashboard.config.Config;

public class Configs {
    @Config
    public static class GeneralSettings {
        public static boolean IsAutonomEnable = true;

        public static boolean IsUseOdometers = true;

        public static boolean IsCachinger = true;

        public static boolean TelemetryOn = true;
    }

    @Config
    public static class Odometry{
        public static double RadiusOdometrXLeft = 15.117, RadiusOdometrXRight = 15.315, RadiusOdometrY = 16.8609;

        public static double DiametrOdometr = 4.8, EncoderconstatOdometr = 8192;
    }

    @Config
    public static class AutomaticForwardPid{
        public static double PidForwardP = 0.03, PidForwardI = 0, PidForwardD = 0;
    }

    @Config
    public static class AutomaticRotatePid{
        public static double PidRotateP = 2, PidRotateI = 0, PidRotateD = 0.5;
    }

    @Config
    public static class DriveTrainWheels {
        public static double wheelDiameter = 9.6, encoderconstat = 480d / (26d / 22d), speed = 0.5;
        public static double Radius = 15.7;
    }

    @Config
    public static class Motors{
        public static double DefultP = 0.000001;
        public static double DefultI = 0;
        public static double DefultD = 5;
        public static double DefultF = 0.5;
    }

    @Config
    public static class Battery{
        public static double CorrectCharge = 14;
    }

    @Config
    public static class Intake{
        public static double SeparatorP = 0.015, SeparatorI = 0, SeparatorD = 0;

        public static int Shift = 240;

        public static int PuckDetectSensitivity = 1, FloorDetectSensitivity = 1;
        public static double PuckDetectDelaySec = 0.3;

        public static double ThresholdAmps = 15, ReversTimeSec = 0.5;

        public static double ClampClamped = 1, ClampRealise = 0.7;

        public static double BrushCurrentDefend = 1.3, BrushDefendReverseTime = 2, BrushPower = 1;

        public static double DefendReversDelay = 0.6;
    }

    @Config
    public static class Automatic{
        public static double TurnSensitivity = Math.PI / 3;
        public static double LengthSensitivity = 5;


    }

    @Config
    public static class Camera{
        public static int ksize = 22;

        public static double hRedDown = 4;
        public static double cRedDown = 127.7;
        public static double vRedDowm = 154.4;
        public static double hRedUp = 30;
        public static double cRedUp = 255;
        public static double vRedUp = 255;

        public static double hBlueDown = 95;
        public static double cBlueDown = 100;
        public static double vBlueDowm = 0;
        public static double hBlueUp = 255;
        public static double cBlueUp = 255;
        public static double vBlueUp = 255;
    }
}
