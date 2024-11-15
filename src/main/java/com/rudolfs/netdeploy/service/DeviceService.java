package com.rudolfs.netdeploy.service;

import com.rudolfs.netdeploy.data.DeviceInput;
import com.rudolfs.netdeploy.data.DeviceNode;
import com.rudolfs.netdeploy.data.DeviceOutput;

import java.util.List;

public interface DeviceService {

    public DeviceOutput registerDevice(DeviceInput deviceInput);

    public DeviceOutput findDevice(String macAddress);

    public List<DeviceOutput> getAllDevices();

    public List<DeviceNode> getTopologies();

    public DeviceNode getTopologyByDevice(String macAddress);
}
