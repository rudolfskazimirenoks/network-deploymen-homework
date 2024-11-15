package com.rudolfs.netdeploy.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND, reason = "Device not found")
public class DeviceNotFoundException extends RuntimeException {
}
