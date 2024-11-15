package com.rudolfs.netdeploy.data;

public class DeviceInput {

    private String macAddress;
    private String deviceType;
    private String uplinkMacAddress;

    public DeviceInput(String macAddress, String deviceType, String uplinkDevice) {
        this.macAddress = macAddress;
        this.deviceType = deviceType;
        this.uplinkMacAddress = uplinkDevice;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getUplinkMacAddress() {
        return uplinkMacAddress;
    }
}
