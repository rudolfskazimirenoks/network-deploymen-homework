package com.rudolfs.netdeploy.controller;

import com.rudolfs.netdeploy.data.DeviceInput;
import com.rudolfs.netdeploy.data.DeviceOutput;
import com.rudolfs.netdeploy.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping()
    public ResponseEntity<DeviceOutput> registerDevice(@RequestBody DeviceInput deviceInput) {
        var registeredDevice = deviceService.registerDevice(deviceInput);
        return ResponseEntity.status(CREATED).body(registeredDevice);
    }

    @GetMapping("/{macAddress}")
    public ResponseEntity<DeviceOutput> getDeviceByMacAddress(@PathVariable("macAddress") String macAddress) {
        var device = deviceService.findDevice(macAddress);
        return ResponseEntity.ok().body(device);
    }
}
