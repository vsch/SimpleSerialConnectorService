package com.vladsch.plugins.simpleserialportservice;

import com.google.common.base.Suppliers;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import jssc.SerialNativeInterface;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static jssc.SerialPort.MASK_ERR;
import static jssc.SerialPort.MASK_RXCHAR;
import static jssc.SerialPort.PARITY_EVEN;
import static jssc.SerialPort.PARITY_NONE;
import static jssc.SerialPort.PARITY_ODD;
import static jssc.SerialPort.STOPBITS_1;
import static jssc.SerialPort.STOPBITS_1_5;
import static jssc.SerialPort.STOPBITS_2;

/**
 * @author Dmitry_Cherkas
 */
public class JsscSerialServiceImpl implements JsscSerialService {
    // Memorizing available ports for better performance
    private static final Supplier<List<String>>
            portNamesSupplier = Suppliers.memoizeWithExpiration(JsscSerialServiceImpl::doGetPortNames, 3000, TimeUnit.MILLISECONDS);

    private final ConcurrentMap<String, SerialConnection> openPorts = new ConcurrentHashMap<>();

    private static List<String> doGetPortNames() {
        return
                ProgressManager.getInstance().computeInNonCancelableSection(
                        () -> Arrays.asList(SerialPortList.getPortNames())
                );
    }

    static boolean haveSerialMonitor = true;

    static @NotNull JsscSerialService getInstance() {
        // try to get the serial monitor JsscSerialService
        if (haveSerialMonitor) {
            try {
                JsscSerialService instance = JsscSerialServiceDelegate.getInstance();
                if (instance != null) {
                    return instance;
                }
            } catch (NoClassDefFoundError | IllegalAccessError ignored) {
                int tmp = 0;
            }
            haveSerialMonitor = false;
        }

        // return our implementation
        return ApplicationManager.getApplication().getService(JsscSerialService.class);
    }

    @NotNull
    @Override
    public List<String> getPortNames(boolean filtered) {
        if (filtered && SerialNativeInterface.getOsType() == SerialNativeInterface.OS_MAC_OS_X) {
            ArrayList<String> ports = new ArrayList<>();

            for (String port : portNamesSupplier.get()) {
                if (!port.matches("/dev/(:?tty)\\..*")) {
                    ports.add(port);
                }
            }
            return ports;
        } else {
            return portNamesSupplier.get();
        }
    }

    @Override
    public boolean isPortValid(String portName, boolean filtered) {
        List<String> availablePortNames = getPortNames(filtered);
        return availablePortNames.contains(portName);
    }

    @Override
    public boolean isConnected(String name) {
        SerialConnection connection = openPorts.get(name);
        return connection != null && connection.mySerialPort.isOpened();
    }

    @Override
    public synchronized void connect(SerialPortProfile settings, Consumer<byte[]> dataListener, SerialConnectionListener connectListener) throws SerialMonitorException {
        String portName = settings.getPortName();
        int dataBits = settings.getBits();
        int stopBits;
        switch (settings.getStopBits()) {
            case BITS_2:
                stopBits = STOPBITS_2;
                break;
            case BITS_1_5:
                stopBits = STOPBITS_1_5;
                break;
            default:
                stopBits = STOPBITS_1;
        }
        int parity;
        switch (settings.getParity()) {
            case EVEN:
                parity = PARITY_EVEN;
                break;
            case ODD:
                parity = PARITY_ODD;
                break;
            default:
                parity = PARITY_NONE;
        }
        try {
            SerialPort port = new SerialPort(portName);
            port.openPort();
            boolean res = port.setParams(settings.getBaudRate(), dataBits, stopBits, parity, true, true);
            if (!res) {
                throw new SerialMonitorException(Bundle.message("serial.port.parameters.wrong"));
            }
            port.addEventListener(new MySerialPortEventListener(port, dataListener, connectListener), MASK_ERR | MASK_RXCHAR);
            openPorts.put(portName, new SerialConnection(port, connectListener));
        } catch (SerialPortException e) {
            SerialPort port = e.getPort();
            if (port != null &&
                    port.getPortName().startsWith("/dev") &&
                    SerialPortException.TYPE_PERMISSION_DENIED.equals(e.getExceptionType())) {
                throw new SerialMonitorException(Bundle.message("serial.port.permissions.denied", portName));
            } else {
                throw new SerialMonitorException(Bundle.message("serial.port.open.error", portName, e.getExceptionType()));
            }
        }
    }

    @Override
    public synchronized void close(String portName) throws SerialMonitorException {
        SerialConnection serialConnection = openPorts.remove(portName);
        if (serialConnection != null) {
            try {
                if (serialConnection.mySerialPort.isOpened()) {
                    serialConnection.mySerialPort.removeEventListener();
                    serialConnection.mySerialPort.closePort();  // close the port
                }
                serialConnection.myListener.updateStatus(SerialConnectionListener.PortStatus.DISCONNECTED);
            } catch (SerialPortException e) {
                throw new SerialMonitorException(e.getMessage(), e);
            }
        }
    }

    @Override
    public void write(@NotNull String portName, byte[] bytes) throws SerialMonitorException {
        try {
            SerialConnection connection = openPorts.get(portName);
            if (connection != null) {
                connection.mySerialPort.writeBytes(bytes);
            }
        } catch (SerialPortException e) {
            throw new SerialMonitorException(e.getMessage(), e);
        }
    }

    @Override
    public void dispose() {
        openPorts.values().forEach(connection -> {
            try {
                connection.mySerialPort.closePort();
            } catch (SerialPortException e) {
                Logger.getInstance(JsscSerialServiceImpl.class).debug(e);
            }
        });
    }

    private static class MySerialPortEventListener implements SerialPortEventListener {
        private final SerialPort myPort;
        private final Consumer<byte[]> myDataListener;
        private final SerialConnectionListener myConnectionListener;

        private MySerialPortEventListener(
                SerialPort port,
                Consumer<byte[]> dataListener,
                SerialConnectionListener connectionListener
        ) {
            myPort = port;
            myDataListener = dataListener;
            myConnectionListener = connectionListener;
        }

        @Override
        public synchronized void serialEvent(SerialPortEvent serialEvent) {
            try {
                byte[] buf = myPort.readBytes(serialEvent.getEventValue());
                if (buf.length > 0) {
                    myDataListener.accept(buf);
                }
            } catch (SerialPortException e) {
                myConnectionListener.updateStatus(SerialConnectionListener.PortStatus.FAILURE);
            }
        }
    }

    private static class SerialConnection {
        private final SerialPort mySerialPort;
        private final SerialConnectionListener myListener;

        private SerialConnection(SerialPort port, SerialConnectionListener listener) {
            mySerialPort = port;
            myListener = listener;
        }
    }
}
