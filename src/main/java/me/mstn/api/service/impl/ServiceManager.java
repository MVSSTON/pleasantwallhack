package me.mstn.api.service.impl;

import me.mstn.api.service.IServiceManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceManager implements IServiceManager {

    private final Map<Class<?>, Object> map = new ConcurrentHashMap<>();

    @Override
    public <T> T getService(Class<T> serviceClass) {
        return (T) map.get(serviceClass);
    }

    @Override
    public <T> void add(Class<T> serviceClass, T service) {
        map.put(serviceClass, service);
    }

}