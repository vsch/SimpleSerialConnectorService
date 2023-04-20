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

package com.vladsch.plugins.simpleserialportservice;

import com.intellij.openapi.Disposable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public interface JsscSerialService extends Disposable {
    static @NotNull JsscSerialService getInstance() {
        return JsscSerialServiceImpl.getInstance();
    }

    @NotNull
    List<String> getPortNames(boolean filtered);

    boolean isPortValid(String portName, boolean filtered);

    boolean isConnected(String name);

    void connect(SerialPortProfile settings, Consumer<byte[]> dataListener, SerialConnectionListener connectListener) throws SerialMonitorException;

    void close(String portName) throws SerialMonitorException;

    void write(@NotNull String portName, byte[] bytes) throws SerialMonitorException;

    @Override
    void dispose();
}
