package com.rudolfs.netdeploy.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ResponseStatus(value = UNPROCESSABLE_ENTITY, reason = "No device found for provided uplink MAC address")
public class UplinkDeviceNotFoundException extends RuntimeException {
}
