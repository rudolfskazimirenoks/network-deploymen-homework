package com.rudolfs.netdeploy.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ResponseStatus(value = UNPROCESSABLE_ENTITY, reason = "Invalid device type provided")
public class DeviceTypeNotFoundException extends RuntimeException {
}
