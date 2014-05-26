package hzg.wpn.tango.client.proxy;

import hzg.wpn.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 29.08.13
 */
public class TangoProxies {
    private TangoProxies() {
    }

    public static TangoProxy newDeviceProxyWrapper(String url) throws TangoProxyException {
        return new DeviceProxyWrapper(url);
    }

    public static <T extends TangoProxy> T newTangoProxy(final String device, Class<T> clazz) throws TangoProxyException {
        //TODO check device and interface compatibility, i.e. clazz is the class of the device

        InvocationHandler handler = new InvocationHandler() {
            final TangoProxy tangoProxy = new DeviceProxyWrapper(device);

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws TangoProxyException {
                //if the method is from TangoProxy interface invoke it
                if (ReflectionUtils.hasMethod(tangoProxy.getClass(), method.getName(), method.getParameterTypes()))
                    return ReflectionUtils.invoke(method, tangoProxy, args, TangoProxyException.class);

                //otherwise delegate method execution to read/writeAttribute or executeCommand
                String methodName = method.getName();

                if (tangoProxy.hasCommand(methodName))
                    return tangoProxy.executeCommand(methodName, args != null ? args[0] : null);
                else if (methodName.startsWith("get"))
                    return tangoProxy.readAttribute(methodName.substring(3));
                else if (methodName.startsWith("is"))
                    return tangoProxy.readAttribute(methodName.substring(2));
                else if (methodName.startsWith("set"))
                    tangoProxy.writeAttribute(methodName.substring(3), args != null ? args[0] : null);
                else
                    throw new TangoProxyException("unknown method " + methodName);

                return null;
            }
        };

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, handler);
    }
}
