package com.vladsch.plugins.simpleserialconnectorservice;

import com.intellij.openapi.Disposable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public interface JsscSerialService extends Disposable {
    /**
     * Get instance of the service
     * 
     * @return an instance of the JsscSerialService
     */
    static @NotNull JsscSerialService getInstance() {
        return JsscSerialServiceImpl.getInstance();
    }

    /**
     * Get the available serial port names.
     *
     * @param filtered if true and OS is Mac OSX, will filter out all /dev/tty ports. Ignored for other OS variants
     *
     * @return string list of available ports
     */
    @NotNull
    List<String> getPortNames(boolean filtered);

    /**
     * Test if the given port name is valid for connection
     *
     * @param portName name of the port
     * @param filtered if true and OS is Mac OSX, will filter out all /dev/tty ports before testing. Ignored for other OS variants
     *
     * @return true if the port is a valid port
     */
    boolean isPortValid(String portName, boolean filtered);

    /**
     * Test if the given port is connected
     *
     * @param portName name of the port
     *
     * @return true if the port is connected
     */
    boolean isConnected(String portName);

    /**
     * Connect the given port with provided configuration settings
     *
     * @param settings        port profile settings
     * @param dataListener    received data callback
     * @param connectListener port connection status listener
     *
     * @throws SerialMonitorException if there are any issues
     */
    void connect(SerialPortProfile settings, Consumer<byte[]> dataListener, SerialConnectionListener connectListener) throws SerialMonitorException;

    /**
     * Closes the given port connection
     * 
     * @param portName name of previously opened port
     *                 
     * @throws SerialMonitorException if there are any issues
     */
    void close(String portName) throws SerialMonitorException;

    /**
     * Write given byte array to the previously opened port
     * 
     * @param portName  name of the port
     * @param bytes     bytes to write out
     *              
     * @throws SerialMonitorException if there are any issues
     */
    void write(@NotNull String portName, byte[] bytes) throws SerialMonitorException;

    @Override
    void dispose();
}
