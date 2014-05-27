package hzg.wpn.tango.client.proxy;

import org.junit.Test;

import java.io.IOException;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 20.06.13
 */
public class TangoProxyImplTest {
    @Test
    public void testProxy() throws Exception {
        //TODO create "remote" device
//        SomeStupidTangoDevice device = TangoProxy.proxy("",SomeStupidTangoDevice.class);
//
//        String result = device.executeCommand(new int[]{1,2,3});
    }

    private static interface SomeStupidTangoDevice {
        int getIntAttr();

        String executeCommand(int[] args) throws IOException;

        //etc
    }
}
