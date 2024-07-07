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
    public static class AutomaticRotatePid{
        public static double PidRotateP = 1.1, PidRotateI = 0, PidRotateD = 0.1;
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

        public static int PuckDetectSensitivity = 25, FloorDetectSensitivity = 25;
        public static double PuckDetectDelaySec = 0.3;

        public static double ThresholdAmps = 1.7, ReversTimeSec = 0.5;

        public static double ClampClamped = 0.175, ClampRealise = 0;

        public static double BrushCurrentDefend = 0.8, BrushDefendReverseTime = 2, BrushPower = 0.75;

        public static double DefendReversDelay = 0.6;
    }

    @Config
    public static class Automatic{
        public static double TurnSensitivity = 0.1;
        public static double EndStateSwapDelay = 0.5, DefendTime = 0.5;
    }

    @Config
    public static class Camera{
        public static int ksize = 6;

        public static double hRedDown = 168;
        public static double cRedDown = 105;
        public static double vRedDowm = 25;
        public static double hRedUp = 179;
        public static double cRedUp = 255;
        public static double vRedUp = 80;

        public static double hBlueDown = 103;
        public static double cBlueDown = 105;
        public static double vBlueDowm = 80;
        public static double hBlueUp = 106;
        public static double cBlueUp = 255;
        public static double vBlueUp = 150;

        public static double CompressionCoef = 0.15;

        public static double MinBlueSimilarity = -1000, MaxBlueSimilarity = 1000;
        public static double MinRedSimilarity = -5000, MaxRedSimilarity = 5000;

        public static int ThreadCount = 7, ImgSizeY = 210;
    }

    @Config
    public static class PuckRunState{
        public static double CameraP = 0.095, speed = -0.9;
    }

    @Config
    public static class PuckDetectState{
        public static int StepRot = 8;
        public static int Range = 180;

        public static double DetectTimeSec = 0.03;
    }
}
