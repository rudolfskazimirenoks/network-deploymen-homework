package com.rudolfs.netdeploy.data;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Optional;

public enum DeviceType {

    GATEWAY(1, "Gateway", "gateway"),
    SWITCH(2, "Switch", "switch"),
    ACCESS_POINT(3, "Access point", "point");

    private int id;
    private String name;
    private String inputValue;

    private DeviceType(int id, String name, String inputValue) {
        this.id = id;
        this.name = name;
        this.inputValue = inputValue;
    }

    public int getId() {
        return id;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public String getInputValue() {
        return inputValue;
    }

    public static Optional<DeviceType> parse(String name) {
        return Arrays.stream(DeviceType.values())
                .filter(type -> type.getInputValue().equalsIgnoreCase(name))
                .findFirst();
    }
}
