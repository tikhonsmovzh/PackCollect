package org.firstinspires.ftc.teamcode.Modules.Camera;

import com.acmerobotics.dashboard.FtcDashboard;

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Tools.Devices;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;

@Module
public class VisionPortalHandler implements IRobotModule {
    private VisionPortal _visualPortal;

    @Override
    public void Init(BaseCollector collector){
        _visualPortal = new VisionPortal.Builder().addProcessors().setCamera(Devices.Camera).build();
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
