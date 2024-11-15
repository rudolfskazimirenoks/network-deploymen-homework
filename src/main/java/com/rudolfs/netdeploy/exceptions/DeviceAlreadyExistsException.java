package com.rudolfs.netdeploy.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(value = CONFLICT, reason = "Device already exists")
public class DeviceAlreadyExistsException extends RuntimeException {
}
