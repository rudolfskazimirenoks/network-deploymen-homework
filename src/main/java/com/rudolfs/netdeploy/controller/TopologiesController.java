package com.rudolfs.netdeploy.controller;

import com.rudolfs.netdeploy.data.DeviceNode;
import com.rudolfs.netdeploy.service.DefaultDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/topologies")
public class TopologiesController {

    @Autowired
    private DefaultDeviceService defaultDeviceService;

    @GetMapping()
    public ResponseEntity<List<DeviceNode>> getTopologies() {
        List<DeviceNode> deviceNodes = defaultDeviceService.getTopologies();
        return ResponseEntity.ok().body(deviceNodes);
    }
}
