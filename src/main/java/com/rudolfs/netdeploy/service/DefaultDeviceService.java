package com.rudolfs.netdeploy.service;

import com.rudolfs.netdeploy.data.*;
import com.rudolfs.netdeploy.exceptions.*;
import com.rudolfs.netdeploy.storage.DeviceStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Service
public class DefaultDeviceService implements DeviceService {

    @Autowired
    private DeviceStorage deviceStorage;

    @Override
    public DeviceOutput registerDevice(DeviceInput deviceInput) {
        var deviceType = DeviceType.parse(deviceInput.getDeviceType())
                .orElseThrow(DeviceTypeNotFoundException::new);

        var macAddress = deviceInput.getMacAddress();
        validateMacAddress(macAddress);

        var uplinkMacAddress = deviceInput.getUplinkMacAddress();
        if (!hasText(uplinkMacAddress)) {
            var device = deviceStorage.registerDevice(macAddress, deviceType, null);
            return toDeviceOutput(device);
        }

        var uplinkDevice = deviceStorage.findDevice(uplinkMacAddress)
                .orElseThrow(UplinkDeviceNotFoundException::new);
        var device = deviceStorage.registerDevice(macAddress, deviceType, uplinkDevice);
        return toDeviceOutput(device);
    }

    @Override
    public DeviceOutput findDevice(String macAddress) {
        return deviceStorage.findDevice(macAddress)
                .map(this::toDeviceOutput)
                .orElseThrow(DeviceNotFoundException::new);
    }

    @Override
    public List<DeviceOutput> getAllDevices() {
        return deviceStorage.getAllDevices()
                .stream()
                .sorted()
                .map(this::toDeviceOutput)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceNode> getTopologies() {
        return deviceStorage.getNetworkDeployment()
                .stream()
                .map(this::toDeviceNode)
                .toList();
    }

    @Override
    public DeviceNode getTopologyByDevice(String macAddress) {
        return deviceStorage.findDevice(macAddress)
                .map(this::toDeviceNode)
                .orElseThrow(DeviceNotFoundException::new);
    }

    protected DeviceOutput toDeviceOutput(Device device) {
        return new DeviceOutput(device.getMacAddress(), device.getDeviceType());
    }

    protected void validateMacAddress(String macAddress) {
        if (!hasText(macAddress)) {
            throw new MacAddressNotFounException();
        }

        deviceStorage.findDevice(macAddress)
                .ifPresent(device -> {
                    throw new DeviceAlreadyExistsException();
                });
    }

    protected DeviceNode toDeviceNode(Device device) {
        var downlinkDevices = device.getDownlinkDevices();
        if (downlinkDevices.isEmpty()) {
            return new DeviceNode(device.getMacAddress(), Collections.emptyList());
        }

        var deviceNodes = downlinkDevices.stream()
                .map(this::toDeviceNode)
                .toList();
        return new DeviceNode(device.getMacAddress(), deviceNodes);
    }
}
