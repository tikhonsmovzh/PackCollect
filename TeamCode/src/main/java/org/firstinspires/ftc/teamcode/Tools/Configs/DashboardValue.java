package org.firstinspires.ftc.teamcode.Tools.Configs;

import com.acmerobotics.dashboard.config.ValueProvider;

public class DashboardValue <T> {
    private T _value;

    private String _key;

    public DashboardValue(T value){
        _value = value;
    }

    public ValueProvider GetProvider(){
        return new Provider();
    }

    public void DataInit(String key){
        _key = key;

        _value = DataManager.Read(_key, _value);
    }

    public T Get() {
        return _value;
    }

    public void NotSaveSet(T value){
        _value = value;
    }

    private class Provider implements ValueProvider<T> {
        @Override
        public T get() {
            return _value;
        }

        @Override
        public void set(T value) {
            _value = value;

            DataManager.Add(_key, _value);
        }
    }
}
