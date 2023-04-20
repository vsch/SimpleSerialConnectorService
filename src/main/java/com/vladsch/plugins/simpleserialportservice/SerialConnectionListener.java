package com.vladsch.plugins.simpleserialportservice;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface SerialConnectionListener {

    enum PortStatus {DISCONNECTED, CONNECTING, CONNECTED, FAILURE}

    void updateStatus(@NotNull SerialConnectionListener.PortStatus status);
}
