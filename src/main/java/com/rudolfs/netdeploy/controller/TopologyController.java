package com.rudolfs.netdeploy.controller;


import com.rudolfs.netdeploy.data.DeviceNode;
import com.rudolfs.netdeploy.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topology")
public class TopologyController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/{macAddress}")
    public ResponseEntity<DeviceNode> getTopologyByDevice(@PathVariable("macAddress") String macAddress) {
        var deviceNode = deviceService.getTopologyByDevice(macAddress);
        return ResponseEntity.ok().body(deviceNode);
    }
}
