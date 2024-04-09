package org.firstinspires.ftc.teamcode.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Modules.Manager.BulkInit;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;
import org.firstinspires.ftc.teamcode.Modules.Manager.Module;
import org.firstinspires.ftc.teamcode.Tools.Battery;
import org.firstinspires.ftc.teamcode.Tools.Devices;
import org.firstinspires.ftc.teamcode.Tools.UpdateHandler.UpdateHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

public class BaseCollector {
    public LinearOpMode Robot;
    public ElapsedTime Time;
    private final Battery _battery;

    private static ArrayList<Class<?>> _annotatedClass;

    private UpdateHandler _updateHandler;

    private Map<Class<?>, IRobotModule> _modules = new HashMap<>();

    public BaseCollector(LinearOpMode robot) {
        Robot = robot;

        _modules.clear();

        Devices.Init(robot.hardwareMap);

        _updateHandler = new UpdateHandler();
        _battery = new Battery(this);
        Time = new ElapsedTime();

        if (_annotatedClass == null)
            _annotatedClass = GetAnnotatedClasses(Module.class);
    }

    public void InitAll(){
        AddAdditionModules(_annotatedClass);
    }

    private static ArrayList<Class<?>> _classes;

    public static ArrayList<Class<?>> GetAnnotatedClasses(Class<? extends Annotation> annotation) {
        if (_classes == null) {
            List<String> classNames;

            try {
                DexFile dexFile = new DexFile(AppUtil.getInstance().getActivity().getPackageCodePath());

                classNames = Collections.list(dexFile.entries());
            } catch (Exception e) {
                throw new RuntimeException("not successful search file names");
            }

            _classes = new ArrayList<>();

            for (String i : classNames) {
                if (!i.contains("team18742"))
                    continue;

                try {
                    _classes.add(Class.forName(i));
                } catch (Exception e) {
                    throw new RuntimeException("not successful find " + i + " class");
                }
            }

            for (Class<?> i : _classes) {
                for (Method j : i.getMethods())
                    if (j.isAnnotationPresent(BulkInit.class)) {
                        try {
                            j.invoke(null);
                        } catch (Exception e) {
                            throw new RuntimeException("failed to invoke method: " + j.getName());
                        }
                    }
            }
        }

        ArrayList<Class<?>> result = new ArrayList<>();

        for (Class<?> i : _classes)
            if (i.isAnnotationPresent(annotation)) {
                result.add(i);
            }

        return result;
    }

    public void Start() {
        Time.reset();

        _updateHandler.Start();
    }

    public void Update() {
        _updateHandler.Update();
    }

    public void Stop() {
        _updateHandler.Stop();
    }

    protected void AddAdditionModules(ArrayList<Class<?>> modules) {
        for (Class<?> i : modules) {
            if(!_modules.containsKey(i))
                InitOne(i);
        }
    }

    private IRobotModule Instance(Class<?> type){
        Object instance;

        try {
            instance = type.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("not correct constructor in module " + type.getName());
        }

        if (instance instanceof IRobotModule)
            return (IRobotModule) instance;

        throw new RuntimeException(type.getName() + "instance not instanceof IRobotModule");
    }

    private boolean IsContains(Class<?> clazz) {
        for (Class<?> i : _classes) {
            if (i == clazz)
                return true;
        }

        return false;
    }

    public <T extends IRobotModule> T GetModule(Class<T> type) {
        if(_modules.containsKey(type))
            return (T) _modules.get(type);

        if(IsContains(type)) {
            InitOne(type);

            return (T) _modules.get(type);
        }

        throw new RuntimeException("not found " + type.getName() + " module");
    }

    public void InitOne(Class<?> clazz){
        IRobotModule inst = Instance(clazz);

        _modules.put(clazz, inst);

        inst.Init(this);
    }
}
