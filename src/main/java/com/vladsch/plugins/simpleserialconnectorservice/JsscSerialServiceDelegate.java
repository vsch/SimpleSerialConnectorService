package com.vladsch.plugins.simpleserialconnectorservice;

import com.intellij.plugins.serialmonitor.SerialProfileService.NewLine;
import com.intellij.plugins.serialmonitor.SerialProfileService.Parity;
import com.intellij.plugins.serialmonitor.SerialProfileService.StopBits;
import com.vladsch.plugins.simpleserialconnectorservice.SerialConnectionListener.PortStatus;
import jssc.SerialNativeInterface;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JsscSerialServiceDelegate implements JsscSerialService {
    static boolean haveSerialMonitor = true;

    static @Nullable JsscSerialService getInstance() {
        // try to get the serial monitor JsscSerialService
        if (haveSerialMonitor) {
//            com.intellij.plugins.serialmonitor.service.JsscSerialService jsscSerialService = ApplicationManager.getApplication().getService(com.intellij.plugins.serialmonitor.service.JsscSerialService.class);
            com.intellij.plugins.serialmonitor.service.JsscSerialService jsscSerialService = com.intellij.plugins.serialmonitor.service.JsscSerialService.getInstance();

            if (jsscSerialService != null) {
                // build a delegate
                return new JsscSerialServiceDelegate(jsscSerialService);
            }
            haveSerialMonitor = false;
        }
        return null;
    }

    private final com.intellij.plugins.serialmonitor.service.JsscSerialService myJsscSerialService;

    private JsscSerialServiceDelegate(com.intellij.plugins.serialmonitor.service.JsscSerialService jsscSerialService) {
        myJsscSerialService = jsscSerialService;
    }

    @NotNull
    @Override
    public List<String> getPortNames(boolean filtered) {
        if (filtered && SerialNativeInterface.getOsType() == SerialNativeInterface.OS_MAC_OS_X) {
            ArrayList<String> ports = new ArrayList<>();

            for (String port : com.intellij.plugins.serialmonitor.service.JsscSerialService.getPortNames()) {
                if (!port.matches("/dev/(:?tty)\\..*")) {
                    ports.add(port);
                }
            }
            return ports;
        } else {
            return com.intellij.plugins.serialmonitor.service.JsscSerialService.getPortNames();
        }

    }

    @Override
    public boolean isPortValid(String portName, boolean filtered) {
        List<String> availablePortNames = getPortNames(filtered);
        return availablePortNames.contains(portName);
    }

    @Override
    public boolean isConnected(String portName) {
        return myJsscSerialService.isConnected(portName);
    }

    private static com.intellij.plugins.serialmonitor.SerialPortProfile from(SerialPortProfile settings) {
        com.intellij.plugins.serialmonitor.SerialPortProfile portProfile = new com.intellij.plugins.serialmonitor.SerialPortProfile();
        portProfile.setBaudRate(settings.baudRate);
        portProfile.setBits(settings.bits);
        portProfile.setEncoding(settings.encoding);
        switch (settings.newLine) {
            case CR:
                portProfile.setNewLine(NewLine.CR);
                break;
            case LF:
                portProfile.setNewLine(NewLine.LF);
                break;
            case CRLF:
                portProfile.setNewLine(NewLine.CRLF);
                break;
        }
        switch (settings.parity) {
            case ODD:
                portProfile.setParity(Parity.ODD);
                break;
            case EVEN:
                portProfile.setParity(Parity.EVEN);
                break;
            case NONE:
                portProfile.setParity(Parity.NONE);
                break;
        }
        switch (settings.stopBits) {
            case BITS_1:
                portProfile.setStopBits(StopBits.BITS_1);
                break;
            case BITS_2:
                portProfile.setStopBits(StopBits.BITS_2);
                break;
            case BITS_1_5:
                portProfile.setStopBits(StopBits.BITS_1_5);
                break;
        }
        portProfile.setPortName(settings.portName);

        return portProfile;
    }

    private static PortStatus from(com.intellij.plugins.serialmonitor.service.SerialConnectionListener.PortStatus status) {
        PortStatus portStatus;
        switch (status) {
            case DISCONNECTED:
                portStatus = PortStatus.DISCONNECTED;
                break;
            case CONNECTING:
                portStatus = PortStatus.CONNECTING;
                break;
            case CONNECTED:
                portStatus = PortStatus.CONNECTED;
                break;

            default:
            case FAILURE:
                portStatus = PortStatus.FAILURE;
                break;
        }
        return portStatus;
    }

    @Override
    public void connect(SerialPortProfile settings, Consumer<byte[]> dataListener, SerialConnectionListener connectListener) throws SerialMonitorException {
        try {
            myJsscSerialService.connect(from(settings), dataListener::accept, status -> connectListener.updateStatus(from(status)));
        } catch (com.intellij.plugins.serialmonitor.SerialMonitorException e) {
            throw new SerialMonitorException(e.getMessage(), e);
        }
    }

    @Override
    public synchronized void close(String portName) throws SerialMonitorException {
        try {
            myJsscSerialService.close(portName);
        } catch (com.intellij.plugins.serialmonitor.SerialMonitorException e) {
            throw new SerialMonitorException(e.getMessage(), e);
        }
    }

    @Override
    public void write(@NotNull String portName, byte[] bytes) throws SerialMonitorException {
        try {
            myJsscSerialService.write(portName, bytes);
        } catch (com.intellij.plugins.serialmonitor.SerialMonitorException e) {
            throw new SerialMonitorException(e.getMessage(), e);
        }
    }

    @Override
    public void dispose() {
        myJsscSerialService.dispose();
    }
}
