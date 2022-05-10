package com.lhd.log_module;

public interface LogEngine {
    default void initialize() {
    }

    void log(String message);
}
