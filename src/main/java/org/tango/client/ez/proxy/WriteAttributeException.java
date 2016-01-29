package org.tango.client.ez.proxy;

import fr.esrf.Tango.DevFailed;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 29.01.2016
 */
public class WriteAttributeException extends TangoProxyException {
    public final String attrName;

    public WriteAttributeException(String device, String attrName, DevFailed devFailed) {
        super(device, devFailed);
        this.attrName = attrName;
    }

    public WriteAttributeException(String device, String attrName, Throwable cause) {
        super(device, cause);
        this.attrName = attrName;
    }

    public WriteAttributeException(String device, String attrName, String msg) {
        super(device, msg);
        this.attrName = attrName;
    }
}
