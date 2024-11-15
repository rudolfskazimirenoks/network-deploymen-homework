package com.rudolfs.netdeploy.controller;

import com.rudolfs.netdeploy.data.DeviceOutput;
import com.rudolfs.netdeploy.service.DefaultDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DevicesController {

    @Autowired
    private DefaultDeviceService defaultDeviceService;

    @GetMapping()
    public ResponseEntity<List<DeviceOutput>> getAllDevices() {
        List<DeviceOutput> devices = defaultDeviceService.getAllDevices();
        return ResponseEntity.ok().body(devices);
    }
}
