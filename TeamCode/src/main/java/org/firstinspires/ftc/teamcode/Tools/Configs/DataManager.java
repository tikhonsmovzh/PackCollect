package org.firstinspires.ftc.teamcode.Tools.Configs;

import android.content.Context;
import android.content.SharedPreferences;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Modules.Manager.IRobotModule;

public class DataManager {
    private static SharedPreferences _data;

    public static void AddDouble(String key, double data){
       AddLong(key, Double.doubleToRawLongBits(data));
    }

    public static void AddInt(String key, int data){
        if(_data == null)
            _data = AppUtil.getInstance().getActivity().getPreferences(Context.MODE_PRIVATE);

        _data.edit().putInt(key, data).apply();
    }

    public static void AddLong(String key, long data){
        if(_data == null)
            _data = AppUtil.getInstance().getActivity().getPreferences(Context.MODE_PRIVATE);

        _data.edit().putLong(key, data).apply();
    }

    public static void AddBoolean(String key, boolean data){
        if(_data == null)
            _data = AppUtil.getInstance().getActivity().getPreferences(Context.MODE_PRIVATE);

        _data.edit().putBoolean(key, data).apply();
    }

    public static double ReadDouble(String key, double defaultVal){
        return Double.longBitsToDouble(ReadLong(key, Double.doubleToRawLongBits(defaultVal)));
    }

    public static int ReadInt(String key, int defaultVal){
        if(_data == null)
            _data = AppUtil.getInstance().getActivity().getPreferences(Context.MODE_PRIVATE);

        return _data.getInt(key, defaultVal);
    }

    public static long ReadLong(String key, long defaultVal){
        if(_data == null)
            _data = AppUtil.getInstance().getActivity().getPreferences(Context.MODE_PRIVATE);

        return _data.getLong(key, defaultVal);
    }

    public static boolean ReadBoolean(String key, boolean defaultVal){
        if(_data == null)
            _data = AppUtil.getInstance().getActivity().getPreferences(Context.MODE_PRIVATE);

        return _data.getBoolean(key, defaultVal);
    }

    public static void Add(String key, Object val){
        if(val instanceof Integer)
            AddInt(key, (Integer) val);
        else if (val instanceof Boolean)
            AddBoolean(key, (Boolean) val);
        else if(val instanceof Double)
            AddDouble(key, (Double)val);
        else if (val instanceof Long)
            AddLong(key, (Long) val);
    }

    public static <T> T Read(String key, T defult){
        if(defult instanceof Integer)
            return (T)(Integer)ReadInt(key, (Integer) defult); // what???
        else if(defult instanceof Boolean)
            return (T)(Boolean)ReadBoolean(key, (Boolean) defult);
        else if(defult instanceof Double)
            return (T)(Double)ReadDouble(key, (Double) defult);
        else if(defult instanceof Long)
            return (T)(Long)ReadLong(key, (Long) defult);

        return defult;
    }
}
