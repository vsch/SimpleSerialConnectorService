/*
 * Copyright (c) 2016-2023 Vladimir Schneider <vladimir.schneider@gmail.com>
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.vladsch.plugins.simpleserialconnectorservice;

import java.nio.charset.StandardCharsets;

public class SerialPortProfile {
    public enum Parity {
        ODD("uart.parity.odd"), EVEN("uart.parity.even"), NONE("uart.parity.none");

        final String displayKey;

        Parity(String displayKey) {
            this.displayKey = displayKey;
        }

        public String toString() {
            return Bundle.message(displayKey);
        }
    }

    public enum StopBits {
        BITS_1("uart.stopbits.1"), BITS_2("uart.stopbits.2"), BITS_1_5("uart.stopbits.1.5");

        final String displayKey;

        StopBits(String displayKey) {
            this.displayKey = displayKey;
        }

        public String toString() {
            return Bundle.message(displayKey);
        }
    }

    public enum NewLine {
        CR("uart.newline.cr", "\r"),
        LF("uart.newline.lf", "\n"),
        CRLF("uart.newline.crlf", "\r\n");

        final String displayKey;
        final String value;

        NewLine(String displayKey, String value) {
            this.displayKey = displayKey;
            this.value = value;
        }

        public String toString() {
            return Bundle.message(displayKey);
        }
    }

    public enum BaudRate {
        BAUDRATE_110(110),
        BAUDRATE_300(300),
        BAUDRATE_600(600),
        BAUDRATE_1200(1200),
        BAUDRATE_2400(2400),
        BAUDRATE_4800(4800),
        BAUDRATE_9600(9600),
        BAUDRATE_14400(14400),
        BAUDRATE_19200(19200),
        BAUDRATE_38400(38400),
        BAUDRATE_57600(57600),
        BAUDRATE_115200(115200),
        BAUDRATE_128000(128000),
        BAUDRATE_230400(230400),
        BAUDRATE_256000(256000),
        BAUDRATE_460800(460800),
        BAUDRATE_512000(512000);

        final public int intValue;

        BaudRate(int value) {
            this.intValue = value;
        }

        public String toString() {
            return Bundle.message(String.valueOf(intValue));
        }
    }

    public String portName = "";
    public int baudRate = 0;
    public int bits = 8;

    public StopBits stopBits = StopBits.BITS_1;
    public Parity parity = Parity.NONE;
    public NewLine newLine = NewLine.CR;
    public String encoding = StandardCharsets.US_ASCII.name();

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public int getBits() {
        return bits;
    }

    public void setBits(int bits) {
        this.bits = bits;
    }

    public StopBits getStopBits() {
        return stopBits;
    }

    public void setStopBits(StopBits stopBits) {
        this.stopBits = stopBits;
    }

    public Parity getParity() {
        return parity;
    }

    public void setParity(Parity parity) {
        this.parity = parity;
    }

    public NewLine getNewLine() {
        return newLine;
    }

    public void setNewLine(NewLine newLine) {
        this.newLine = newLine;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
