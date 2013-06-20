package wpn.hdri.tango.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * This utility class creates interface specific proxy of the remote device.
 *
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 20.06.13
 */
public class TangoProxy {
    private TangoProxy() {
    }

    public static <T> T proxy(final String device, Class<T> clazz) throws TangoProxyException {
        //TODO check device and interface compatibility, i.e. clazz is the class of the device

        InvocationHandler handler = new InvocationHandler() {
            final TangoProxyWrapper tangoProxy = new TangoProxyWrapper(device);

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws TangoProxyException {
                String methodName = method.getName();

                if (tangoProxy.hasCommand(methodName))
                    return tangoProxy.executeCommand(methodName, args[0]);
                else if (methodName.startsWith("get"))
                    return tangoProxy.readAttribute(methodName.substring(3));
                else if (methodName.startsWith("is"))
                    return tangoProxy.readAttribute(methodName.substring(2));
                else if (methodName.startsWith("set"))
                    tangoProxy.writeAttribute(methodName.substring(3), args[0]);
                else
                    throw new TangoProxyException("unknown method " + methodName);

                return null;
            }
        };

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, handler);
    }
}
