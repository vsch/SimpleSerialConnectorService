package com.vladsch.plugins.simpleserialconnectorservice;

import org.jetbrains.annotations.Nls;

/**
 * @author Dmitry_Cherkas
 */
public class SerialMonitorException extends Exception {

    public SerialMonitorException(@Nls String message) {
        super(message);
    }

    public SerialMonitorException(@Nls String message, Throwable cause) {
        super(message, cause);
    }
}
