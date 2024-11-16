package com.rudolfs.netdeploy.controller;

import com.rudolfs.netdeploy.data.DeviceNode;
import com.rudolfs.netdeploy.service.DeviceService;
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
    private DeviceService deviceService;

    @GetMapping()
    public ResponseEntity<List<DeviceNode>> getTopologies() {
        var deviceNodes = deviceService.getTopologies();
        return ResponseEntity.ok().body(deviceNodes);
    }
}
