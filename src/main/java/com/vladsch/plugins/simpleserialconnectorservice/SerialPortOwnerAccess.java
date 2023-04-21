package com.vladsch.plugins.simpleserialconnectorservice;

import org.jetbrains.annotations.NotNull;

public interface SerialPortOwnerAccess {
    /**
     * Get the portName for this owner access 
     * 
     * @return portName
     */
    @NotNull
    String getPortName();

    /**
     * Test if the owned port is still connected
     *
     * @return true if the port is connected
     */
    boolean isConnected();

    /**
     * Write given byte array to the port for which this
     *
     * @param bytes bytes to write out
     *
     * @throws SerialMonitorException if there are any issues
     */
    void write(byte[] bytes) throws SerialMonitorException;
}
