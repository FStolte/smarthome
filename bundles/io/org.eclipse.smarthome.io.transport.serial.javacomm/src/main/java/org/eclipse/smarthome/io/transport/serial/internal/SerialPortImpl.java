/**
 * Copyright (c) 2014,2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.smarthome.io.transport.serial.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import javax.comm.SerialPortEvent;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.io.transport.serial.SerialPort;
import org.eclipse.smarthome.io.transport.serial.SerialPortEventListener;
import org.eclipse.smarthome.io.transport.serial.UnsupportedCommOperationException;

/**
 * Specific serial port implementation.
 *
 * @author Markus Rathgeb - Initial contribution
 * @author Kai Kreuzer - added further methods
 */
@NonNullByDefault
public class SerialPortImpl implements SerialPort {

    private final javax.comm.SerialPort sp;

    /**
     * Constructor.
     *
     * @param sp the underlying serial port implementation
     */
    public SerialPortImpl(final javax.comm.SerialPort sp) {
        this.sp = sp;
    }

    @Override
    public void close() {
        sp.close();
    }

    @Override
    public void setSerialPortParams(int baudrate, int dataBits, int stopBits, int parity)
            throws UnsupportedCommOperationException {
        try {
            sp.setSerialPortParams(baudrate, dataBits, stopBits, parity);
        } catch (javax.comm.UnsupportedCommOperationException ex) {
            throw new UnsupportedCommOperationException(ex);
        }
    }

    @Override
    public @Nullable InputStream getInputStream() throws IOException {
        return sp.getInputStream();
    }

    @Override
    public @Nullable OutputStream getOutputStream() throws IOException {
        return sp.getOutputStream();
    }

    @Override
    public void addEventListener(SerialPortEventListener listener) throws TooManyListenersException {
        sp.addEventListener(new javax.comm.SerialPortEventListener() {
            @Override
            public void serialEvent(final @Nullable SerialPortEvent event) {
                if (event == null) {
                    return;
                }
                listener.serialEvent(new SerialPortEventImpl(event));
            }
        });
    }

    @Override
    public void removeEventListener() {
        sp.removeEventListener();
    }

    @Override
    public void notifyOnDataAvailable(boolean enable) {
        sp.notifyOnDataAvailable(enable);
    }

    @Override
    public void enableReceiveTimeout(int timeout) throws UnsupportedCommOperationException {
        if (timeout < 0) {
            throw new IllegalArgumentException(String.format("timeout must be non negative (is: %d)", timeout));
        }
        try {
            sp.enableReceiveTimeout(timeout);
        } catch (javax.comm.UnsupportedCommOperationException ex) {
            throw new UnsupportedCommOperationException(ex);
        }
    }

    @Override
    public void disableReceiveTimeout() {
        sp.disableReceiveTimeout();
    }

    @Override
    public String getName() {
        return sp.getName();
    }

    @Override
    public void setFlowControlMode(int flowcontrolRtsctsOut) throws UnsupportedCommOperationException {
        try {
            sp.setFlowControlMode(flowcontrolRtsctsOut);
        } catch (javax.comm.UnsupportedCommOperationException e) {
            throw new UnsupportedCommOperationException(e);
        }
    }

    @Override
    public void enableReceiveThreshold(int i) throws UnsupportedCommOperationException {
        try {
            sp.enableReceiveThreshold(i);
        } catch (javax.comm.UnsupportedCommOperationException e) {
            throw new UnsupportedCommOperationException(e);
        }
    }

}
