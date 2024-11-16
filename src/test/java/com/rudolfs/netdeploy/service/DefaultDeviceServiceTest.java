package com.rudolfs.netdeploy.service;

import com.rudolfs.netdeploy.data.Device;
import com.rudolfs.netdeploy.data.DeviceInput;
import com.rudolfs.netdeploy.exceptions.*;
import com.rudolfs.netdeploy.storage.DeviceStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.rudolfs.netdeploy.data.DeviceType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class DefaultDeviceServiceTest {

    @Mock
    private DeviceStorage deviceStorage;

    @InjectMocks
    private DefaultDeviceService target;

    @Test
    public void testRegisterDevice_whenDeviceTypeUnknown() {
        var deviceInput = new DeviceInput("", "unknown", "");

        assertThrows(DeviceTypeNotFoundException.class, () -> target.registerDevice(deviceInput));
    }

    @Test
    public void testRegisterDevice_whenMacAddressIsEmpty() {
        var deviceInput = new DeviceInput("", "point", "");

        assertThrows(MacAddressNotFounException.class, () -> target.registerDevice(deviceInput));
    }

    @Test
    public void testRegisterDevice_whenMacAddressAlreadyExists() {
        var deviceInput = new DeviceInput("abc123", "point", "");

        doReturn(Optional.of(new Device("abc123", SWITCH))).when(deviceStorage).findDevice("abc123");

        assertThrows(DeviceAlreadyExistsException.class, () -> target.registerDevice(deviceInput));
    }

    @Test
    public void testRegisterDevice_whenNoUplinkDeviceProvided() {
        var deviceInput = new DeviceInput("abc123", "point", "");
        var device = new Device("abc123", ACCESS_POINT);

        doReturn(Optional.empty()).when(deviceStorage).findDevice("abc123");
        doReturn(device).when(deviceStorage).registerDevice("abc123", ACCESS_POINT, null);

        var result = target.registerDevice(deviceInput);
        assertAll(
                () -> assertEquals("abc123", result.getMacAddress()),
                () -> assertEquals(ACCESS_POINT, result.getDeviceType())
        );
    }

    @Test
    public void testRegisterDevice_whenUplinkDeviceDoesNotExist() {
        var deviceInput = new DeviceInput("abc124", "point", "abc123");

        doReturn(Optional.empty()).when(deviceStorage).findDevice("abc124");
        doReturn(Optional.empty()).when(deviceStorage).findDevice("abc123");

        assertThrows(UplinkDeviceNotFoundException.class, () -> target.registerDevice(deviceInput));
    }

    @Test
    public void testRegisterDevice_whenDeviceHasUplinkDevice() {
        var deviceInput = new DeviceInput("abc124", "point", "abc123");
        var deviceRoot = new Device("abc123", SWITCH);
        var deviceNode = new Device("abc124", ACCESS_POINT);

        doReturn(Optional.empty()).when(deviceStorage).findDevice("abc124");
        doReturn(Optional.of(deviceRoot)).when(deviceStorage).findDevice("abc123");
        doReturn(deviceNode).when(deviceStorage).registerDevice("abc124", ACCESS_POINT, deviceRoot);

        var result = target.registerDevice(deviceInput);
        assertAll(
                () -> assertEquals("abc124", result.getMacAddress()),
                () -> assertEquals(ACCESS_POINT, result.getDeviceType())
        );
    }

    @Test
    public void testFindDevice_whenDeviceFound() {
        var device = new Device("abc123", ACCESS_POINT);

        doReturn(Optional.of(device)).when(deviceStorage).findDevice("abc123");

        var result = target.findDevice("abc123");
        assertAll(
                () -> assertEquals("abc123", result.getMacAddress()),
                () -> assertEquals(ACCESS_POINT, result.getDeviceType())
        );
    }

    @Test
    public void testFindDevice_whenDeviceNotFound() {
        doReturn(Optional.empty()).when(deviceStorage).findDevice("abc123");

        assertThrows(DeviceNotFoundException.class, () -> target.findDevice("abc123"));
    }

    @Test
    public void testGetAllDevices() {
        var deviceOne = new Device("abc123", SWITCH);
        var deviceTwo = new Device("abc124", ACCESS_POINT);
        var deviceThree = new Device("abc125", GATEWAY);
        var deviceFour = new Device("abc121", GATEWAY);

        doReturn(List.of(deviceOne, deviceTwo, deviceThree, deviceFour)).when(deviceStorage).getAllDevices();

        var result = target.getAllDevices();
        assertAll(
                () -> assertEquals(4, result.size()),
                () -> assertEquals("abc121", result.get(0).getMacAddress()),
                () -> assertEquals(GATEWAY, result.get(0).getDeviceType()),
                () -> assertEquals("abc125", result.get(1).getMacAddress()),
                () -> assertEquals(GATEWAY, result.get(1).getDeviceType()),
                () -> assertEquals("abc123", result.get(2).getMacAddress()),
                () -> assertEquals(SWITCH, result.get(2).getDeviceType()),
                () -> assertEquals("abc124", result.get(3).getMacAddress()),
                () -> assertEquals(ACCESS_POINT, result.get(3).getDeviceType())
        );
    }

    @Test
    public void testGetTopologies() {
        var rootDevice = new Device("abc123", SWITCH);
        var nodeDevice = new Device("abc124", GATEWAY);
        rootDevice.registerDownlinkDevice(nodeDevice);

        doReturn(List.of(rootDevice)).when(deviceStorage).getNetworkDeployment();

        var result = target.getTopologies();
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("abc123", result.get(0).getMacAddress()),
                () -> assertEquals(1, result.get(0).getDownlinkDevices().size()),
                () -> assertEquals("abc124", result.get(0).getDownlinkDevices().get(0).getMacAddress()),
                () -> assertTrue(result.get(0).getDownlinkDevices().get(0).getDownlinkDevices().isEmpty())
        );
    }

    @Test
    public void testGetTopologyByDevice_whenDeviceFound() {
        var device = new Device("abc123", GATEWAY);

        doReturn(Optional.of(device)).when(deviceStorage).findDevice("abc123");

        var result = target.getTopologyByDevice("abc123");
        assertAll(
                () -> assertEquals("abc123", result.getMacAddress()),
                () -> assertTrue(result.getDownlinkDevices().isEmpty())
        );
    }

    @Test
    public void testGetTopologyByDevice_whenNoDeviceFound() {
        doReturn(Optional.empty()).when(deviceStorage).findDevice("abc123");

        assertThrows(DeviceNotFoundException.class, () -> target.getTopologyByDevice("abc123"));
    }
}
