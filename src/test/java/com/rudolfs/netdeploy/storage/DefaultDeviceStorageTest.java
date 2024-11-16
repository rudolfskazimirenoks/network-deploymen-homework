package com.rudolfs.netdeploy.storage;

import org.junit.jupiter.api.Test;

import static com.rudolfs.netdeploy.data.DeviceType.*;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultDeviceStorageTest {

    private final DeviceStorage target = new DefaultDeviceStorage();

    @Test
    public void testRegisterDevice() {
        var resultRoot = target.registerDevice("abc123", GATEWAY, null);
        var resultNode = target.registerDevice("abc124", SWITCH, resultRoot);
        var resultList = target.getNetworkDeployment();
        assertAll(
                () -> assertTrue(resultList.contains(resultRoot)),
                () -> assertEquals("abc123", resultRoot.getMacAddress()),
                () -> assertEquals(GATEWAY, resultRoot.getDeviceType()),
                () -> assertTrue(resultRoot.getDownlinkDevices().contains(resultNode)),
                () -> assertFalse(resultList.contains(resultNode)),
                () -> assertEquals("abc124", resultNode.getMacAddress()),
                () -> assertEquals(SWITCH, resultNode.getDeviceType()),
                () -> assertTrue(resultNode.getDownlinkDevices().isEmpty())
        );
    }

    @Test
    public void testFindDevice() {
        var rootDevice = target.registerDevice("abc123", GATEWAY, null);
        var middleDevice = target.registerDevice("abc124", SWITCH, rootDevice);
        var nodeDevice = target.registerDevice("abc125", ACCESS_POINT, middleDevice);

        var resultFound = target.findDevice("abc125");
        var resultNotFound = target.findDevice("abc126");
        assertAll(
                () -> assertTrue(resultFound.isPresent()),
                () -> assertEquals(nodeDevice, resultFound.get()),
                () -> assertFalse(resultNotFound.isPresent())
        );
    }

    @Test
    public void testGetAllDevices() {
        var rootDevice = target.registerDevice("abc123", GATEWAY, null);
        var middleDevice = target.registerDevice("abc124", SWITCH, rootDevice);
        var nodeDevice = target.registerDevice("abc125", ACCESS_POINT, middleDevice);
        var networkDeployment = target.getNetworkDeployment();

        var result = target.getAllDevices();
        assertAll(
                () -> assertEquals(1, networkDeployment.size()),
                () -> assertEquals(3, result.size()),
                () -> assertTrue(result.contains(rootDevice)),
                () -> assertTrue(result.contains(middleDevice)),
                () -> assertTrue(result.contains(nodeDevice))
        );
    }
}
