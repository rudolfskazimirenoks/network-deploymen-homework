package com.rudolfs.netdeploy.controller;

import com.rudolfs.netdeploy.data.DeviceInput;
import com.rudolfs.netdeploy.data.DeviceOutput;
import com.rudolfs.netdeploy.service.DefaultDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DefaultDeviceService defaultDeviceService;

    @PostMapping()
    public ResponseEntity<DeviceOutput> registerDevice(@RequestBody DeviceInput deviceInput) {
        DeviceOutput registeredDevice = defaultDeviceService.registerDevice(deviceInput);
        return ResponseEntity.status(CREATED).body(registeredDevice);
    }

    @GetMapping("/{macAddress}")
    public ResponseEntity<DeviceOutput> getDeviceByMacAddress(@PathVariable("macAddress") String macAddress) {
        DeviceOutput device = defaultDeviceService.findDevice(macAddress);
        return ResponseEntity.ok().body(device);
    }
}
