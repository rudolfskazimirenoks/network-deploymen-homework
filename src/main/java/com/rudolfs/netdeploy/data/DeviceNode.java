package com.rudolfs.netdeploy.data;

import java.util.List;

public class DeviceNode {

    private String macAddress;
    private List<DeviceNode> downlinkDevices;

    public DeviceNode(String macAddress, List<DeviceNode> downlinkDevices) {
        this.macAddress = macAddress;
        this.downlinkDevices = downlinkDevices;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public List<DeviceNode> getDownlinkDevices() {
        return downlinkDevices;
    }
}
