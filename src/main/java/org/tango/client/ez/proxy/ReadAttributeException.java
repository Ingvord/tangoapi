package org.tango.client.ez.proxy;

import fr.esrf.Tango.DevFailed;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 29.01.2016
 */
public class ReadAttributeException extends TangoProxyException {
    public final String attrName;

    public ReadAttributeException(String device, String attrName, String msg) {
        super(device, msg);
        this.attrName = attrName;
    }

    public ReadAttributeException(String device, String attrName, Throwable cause) {
        super(device, cause);
        this.attrName = attrName;
    }

    public ReadAttributeException(String device, String attrName, DevFailed devFailed) {
        super(device, devFailed);
        this.attrName = attrName;
    }
}
