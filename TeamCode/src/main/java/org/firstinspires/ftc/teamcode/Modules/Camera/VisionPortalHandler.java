package org.firstinspires.ftc.teamcode.Modules.Camera;

import com.acmerobotics.dashboard.FtcDashboard;

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.Devices;
import org.firstinspires.ftc.teamcode.Utils.Units.Vector2;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;

@Module
public class VisionPortalHandler implements IRobotModule {
    private VisionPortal _visualPortal;
    private PuckDetections _processor;

    @Override
    public void Init(BaseCollector collector){
        _processor = new PuckDetections();

        _visualPortal = new VisionPortal.Builder().setCamera(Devices.Camera).addProcessor((VisionProcessor) _processor).build();

        if(Configs.GeneralSettings.TelemetryOn)
            FtcDashboard.getInstance().startCameraStream(_processor, 10);
    }

    @Override
    public void Stop(){
        while (_visualPortal.getCameraState() == VisionPortal.CameraState.OPENING_CAMERA_DEVICE);

        _visualPortal.close();
        FtcDashboard.getInstance().stopCameraStream();
    }

    public int GetRedPucks(){
        return _processor.RedCount.get();
    }


    public int GetBluePucks(){
        return _processor.BlueCount.get();
    }

    public Vector2 GetRedConcentration(){
        return _processor.RedConcentrationPos.get();
    }

    public Vector2 GetBlueConcentration(){
        return _processor.BlueConcentrationPos.get();
    }

    public boolean IsCameraOpened(){
        return _visualPortal.getCameraState() == VisionPortal.CameraState.CAMERA_DEVICE_READY;
    }
}
