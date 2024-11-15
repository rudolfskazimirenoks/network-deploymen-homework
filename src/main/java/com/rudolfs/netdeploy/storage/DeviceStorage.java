package com.rudolfs.netdeploy.storage;

import com.rudolfs.netdeploy.data.Device;
import com.rudolfs.netdeploy.data.DeviceType;

import java.util.List;
import java.util.Optional;

public interface DeviceStorage {

    public Device registerDevice(String macAddress, DeviceType deviceType, Device uplinkDevice);

    public Optional<Device> findDevice(String macAddress);

    public List<Device> getAllDevices();

    public List<Device> getNetworkDeployment();
}
