package org.tango.client.ez.proxy;

import fr.esrf.TangoApi.ConnectionFailed;
import fr.esrf.TangoDs.Except;
import org.junit.Test;

public class TangoProxyExceptionTest {
    @Test
    public void test_createFromConnectionFailed() {
        try {
            Except.throw_connection_failed("reason", "desc", "origin");
        } catch (ConnectionFailed connectionFailed) {
            TangoProxyException result = new TangoProxyException(null, connectionFailed);

            System.out.println(result);
        }
    }

}