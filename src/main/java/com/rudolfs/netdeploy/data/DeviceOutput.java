package com.rudolfs.netdeploy.data;

public class DeviceOutput {

    private String macAddress;
    private DeviceType deviceType;

    public DeviceOutput(String macAddress, DeviceType deviceType) {
        this.macAddress = macAddress;
        this.deviceType = deviceType;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }
}
