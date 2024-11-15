package com.rudolfs.netdeploy.data;

import java.util.ArrayList;
import java.util.List;

public class Device implements Comparable<Device> {

    private String macAddress;
    private DeviceType deviceType;
    private Device uplinkDevice;
    private List<Device> downlinkDevices;

    public Device(String macAddress, DeviceType deviceType, Device uplinkDevice) {
        this.macAddress = macAddress;
        this.deviceType = deviceType;
        this.uplinkDevice = uplinkDevice;
        downlinkDevices = new ArrayList<>();
    }

    public void registerDownlinkDevice(Device device) {
        downlinkDevices.add(device);
    }

    public String getMacAddress() {
        return macAddress;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public List<Device> getDownlinkDevices() {
        return downlinkDevices;
    }

    public Device getUplinkDevice() {
        return uplinkDevice;
    }

    @Override
    public int compareTo(Device otherDevice) {
        DeviceType thisType = getDeviceType();
        DeviceType otherType = otherDevice.getDeviceType();

        if (thisType.equals(otherType)) {
            return getMacAddress().compareTo(otherDevice.getMacAddress());
        }

        return Integer.compare(thisType.getId(), otherType.getId());
    }
}
