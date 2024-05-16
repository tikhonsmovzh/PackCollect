package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.hardware.lynx.LynxModule;


import org.firstinspires.ftc.teamcode.Collectors.BaseCollector;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;
import org.firstinspires.ftc.teamcode.Utils.Devices;

import java.util.List;

@Module
public class Cacher implements IRobotModule {
    private List<LynxModule> _hubs;

    @Override
    public void Init(BaseCollector collector){
        _hubs = Devices.Hubs;

        for(LynxModule i: _hubs)
            i.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
    }

    @Override
    public void Update(){
        if(Configs.GeneralSettings.IsCachinger)
            for(LynxModule i: _hubs)
                i.getBulkData();
    }
}
