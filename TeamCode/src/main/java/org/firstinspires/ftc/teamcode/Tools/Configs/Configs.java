package org.firstinspires.ftc.teamcode.Tools.Configs;

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
    public static class Gyroscope{
        public static double MergerCoefSeconds = 0.7;
        public static long Iterations = 10;
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
        public static double SeparatorP = 1, SeparatorI = 0, SeparatorD = 0;

        public static double Shift = 120;

        public static double RedVoltage = 0.2, BlueVoltage = 0.6, PuckDetectSensitivity = 0.1;
        public static double PuckDetectDelaySec = 0.5;

        public static double ThresholdAmps = 1, ReversTimeSec = 0.5, ThresholdSensitivity = 15;

        public static double ClampRadius = 10;

        public static double ClampClamped = 0.5, ClampRealise = 0;
    }

    @Config
    public static class Automatic{
        public static double TurnSensitivity = Math.PI / 3
    }
}
