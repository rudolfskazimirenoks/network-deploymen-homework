package com.rudolfs.netdeploy.service;

import com.rudolfs.netdeploy.data.*;
import com.rudolfs.netdeploy.exceptions.*;
import com.rudolfs.netdeploy.storage.DefaultDeviceStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Service
public class DefaultDeviceService implements DeviceService {

    @Autowired
    private DefaultDeviceStorage deviceStorage;

    @Override
    public DeviceOutput registerDevice(DeviceInput deviceInput) {
        DeviceType deviceType = DeviceType.parse(deviceInput.getDeviceType())
                .orElseThrow(DeviceTypeNotFoundException::new);

        String macAddress = deviceInput.getMacAddress();
        validateMacAddress(macAddress);

        String uplinkMacAddress = deviceInput.getUplinkMacAddress();
        if (!hasText(uplinkMacAddress)) {
            Device device = deviceStorage.registerDevice(macAddress, deviceType, null);
            return toDeviceOutput(device);
        }

        Device uplinkDevice = deviceStorage.findDevice(uplinkMacAddress)
                .orElseThrow(UplinkDeviceNotFoundException::new);
        Device device = deviceStorage.registerDevice(macAddress, deviceType, uplinkDevice);
        return toDeviceOutput(device);
    }

    @Override
    public DeviceOutput findDevice(String macAddress) {
        Optional<Device> device = deviceStorage.findDevice(macAddress);

        return device
                .map(this::toDeviceOutput)
                .orElseThrow(DeviceNotFoundException::new);
    }

    @Override
    public List<DeviceOutput> getAllDevices() {
        return deviceStorage.getAllDevices().stream()
                .sorted()
                .map(this::toDeviceOutput)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceNode> getTopologies() {
        List<Device> devices = deviceStorage.getNetworkDeployment();
        return devices.stream()
                .map(this::toDeviceNode)
                .toList();
    }

    @Override
    public DeviceNode getTopologyByDevice(String macAddress) {
        Optional<Device> device = deviceStorage.findDevice(macAddress);
        return device
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
        List<Device> downlinkDevices = device.getDownlinkDevices();
        if (downlinkDevices.isEmpty()) {
            return new DeviceNode(device.getMacAddress(), Collections.emptyList());
        }

        List<DeviceNode> deviceNodes = downlinkDevices.stream()
                .map(this::toDeviceNode)
                .toList();
        return new DeviceNode(device.getMacAddress(), deviceNodes);
    }
}
