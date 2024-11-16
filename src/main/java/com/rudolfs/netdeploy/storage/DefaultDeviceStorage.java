package com.rudolfs.netdeploy.storage;

import com.rudolfs.netdeploy.data.Device;
import com.rudolfs.netdeploy.data.DeviceType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class DefaultDeviceStorage implements DeviceStorage {

    private final List<Device> networkDeployment = new ArrayList<>();

    @Override
    public Device registerDevice(String macAddress, DeviceType deviceType, Device uplinkDevice) {
        var device = new Device(macAddress, deviceType);
        if (uplinkDevice == null) {
            networkDeployment.add(device);
            return device;
        }

        uplinkDevice.registerDownlinkDevice(device);
        return device;
    }

    @Override
    public Optional<Device> findDevice(String macAddress) {
        return networkDeployment.stream()
                .map(device -> findDevice(device, macAddress))
                .filter(Optional::isPresent)
                .flatMap(Optional::stream)
                .findFirst();
    }

    @Override
    public List<Device> getAllDevices() {
        return getAllDevices(networkDeployment);
    }

    @Override
    public List<Device> getNetworkDeployment() {
        return networkDeployment;
    }

    private List<Device> getAllDevices(List<Device> devices) {
        if (devices.isEmpty()) {
            return devices;
        }

        var downlinkDevices = devices.stream()
                .flatMap(device -> getAllDevices(device.getDownlinkDevices()).stream())
                .toList();

        return Stream.concat(devices.stream(), downlinkDevices.stream())
                .toList();
    }

    private Optional<Device> findDevice(Device device, String macAddress) {
        if (macAddress.equals(device.getMacAddress())) {
            return Optional.of(device);
        }

        var downlinkDevices = device.getDownlinkDevices();
        if (downlinkDevices.isEmpty()) {
            return Optional.empty();
        }

        return downlinkDevices.stream()
                .map(downlinkDevice -> findDevice(downlinkDevice, macAddress))
                .filter(Optional::isPresent)
                .flatMap(Optional::stream)
                .findFirst();
    }
}
