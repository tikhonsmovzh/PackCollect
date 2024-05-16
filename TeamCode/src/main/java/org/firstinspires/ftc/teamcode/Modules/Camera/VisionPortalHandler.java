package org.firstinspires.ftc.teamcode.Modules.Camera;

import com.acmerobotics.dashboard.FtcDashboard;

import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Utils.Devices;
import org.firstinspires.ftc.vision.VisionPortal;

@Module
public class VisionPortalHandler implements IRobotModule {
    private VisionPortal _visualPortal;
    //private CameraStreamSource _processor;

    @Override
    public void Init(BaseCollector collector){
      //  _processor = new PuckDetections();

        _visualPortal = new VisionPortal.Builder().setCamera(Devices.Camera).build();//.addProcessor((VisionProcessor) _processor).build();

        //if(Configs.GeneralSettings.TelemetryOn)
        //    FtcDashboard.getInstance().startCameraStream(_processor, 10);
    }

    @Override
    public void Stop(){
        while (_visualPortal.getCameraState() == VisionPortal.CameraState.OPENING_CAMERA_DEVICE);

        _visualPortal.close();
        FtcDashboard.getInstance().stopCameraStream();
    }

    public boolean IsCameraOpened(){
        return _visualPortal.getCameraState() == VisionPortal.CameraState.CAMERA_DEVICE_READY;
    }
}
