package com.yy.my_tutor.config;

public enum RedisDBType {
    PRIMARY("primary"),
    SECONDARY("secondary");

    private String name;

    private RedisDBType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
