package org.firstinspires.ftc.teamcode.GameManagement;

public class GameManagement {
    /*@OnCreate
    public static void Create(Context context){
        Devices.Init(OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).getHardwareMap());

        DigitalChannel chanel = Devices.StartButton;
        chanel.setMode(DigitalChannel.Mode.INPUT);

        ColorSensor colorSensor = new ColorSensor(Devices.FloorSensor, false);

        new Thread(()->{
            while (true){
                if (!chanel.getState()) {
                    //colorSensor.Update();

                    //Intake.GetColorType(colorSensor.getColor(), Configs.Intake.FloorDetectSensitivity);

                    OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).initOpMode("ModuleTest");
                }
            }
        }).start();
    }*/
}
