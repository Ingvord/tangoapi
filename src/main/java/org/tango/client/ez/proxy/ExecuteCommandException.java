package org.tango.client.ez.proxy;

import fr.esrf.Tango.DevFailed;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 29.01.2016
 */
public class ExecuteCommandException extends TangoProxyException {
    public final String cmdName;

    public ExecuteCommandException(String device, String cmdName, DevFailed devFailed) {
        super(device, devFailed);
        this.cmdName = cmdName;
    }

    public ExecuteCommandException(String device, String cmdName, Throwable cause) {
        super(device, cause);
        this.cmdName = cmdName;
    }

    public ExecuteCommandException(String device, String cmdName, String msg) {
        super(device, msg);
        this.cmdName = cmdName;
    }
}
