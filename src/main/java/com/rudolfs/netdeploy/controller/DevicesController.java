package com.rudolfs.netdeploy.controller;

import com.rudolfs.netdeploy.data.DeviceOutput;
import com.rudolfs.netdeploy.service.DeviceService;
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
    private DeviceService deviceService;

    @GetMapping()
    public ResponseEntity<List<DeviceOutput>> getAllDevices() {
        var devices = deviceService.getAllDevices();
        return ResponseEntity.ok().body(devices);
    }
}
