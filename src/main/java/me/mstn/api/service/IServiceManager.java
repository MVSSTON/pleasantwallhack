package me.mstn.api.service;

public interface IServiceManager {

    <T> T getService(Class<T> serviceClass);

    <T> void add(Class<T> serviceClass, T service);

}
