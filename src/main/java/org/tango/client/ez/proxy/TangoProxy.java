package org.tango.client.ez.proxy;

import fr.esrf.TangoApi.DeviceProxy;
import fr.esrf.TangoApi.events.TangoEventsAdapter;
import org.javatuples.Triplet;
import org.tango.client.ez.attribute.Quality;

import java.util.Map;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 29.08.13
 */
public interface TangoProxy {
    /**
     * Convenience method
     *
     * @return a name of this device, i.e. sys/tg_test/1
     */
    String getName();

    boolean hasAttribute(String attrName) throws TangoProxyException;

    TangoAttributeInfoWrapper getAttributeInfo(String attrName) throws TangoProxyException;

    <T> T readAttribute(String attrName) throws TangoProxyException;

    <T> Map.Entry<T, Long> readAttributeValueAndTime(String attrName) throws TangoProxyException;

    <T> Triplet<T, Long, Quality> readAttributeValueTimeQuality(String attrName) throws TangoProxyException;

    <T> void writeAttribute(String attrName, T value) throws TangoProxyException;

    <T, V> V executeCommand(String cmd, T value) throws TangoProxyException;

    void subscribeToEvent(String attrName, TangoEvent event) throws TangoProxyException;

    /**
     * Before calling this method make sure that client is already subscribed to the attribute.
     * Otherwise a NPE will be thrown.
     * <p/>
     * This method caches listener in a weak reference. So it is users responsibility to preserve a reference to it.
     *
     * @param attrName
     * @param event
     * @param listener
     * @param <T>
     * @throws NullPointerException
     */
    <T> void addEventListener(String attrName, TangoEvent event, TangoEventListener<T> listener);

    public void unsubscribeFromEvent(String attrName, TangoEvent event) throws TangoProxyException;

    TangoCommandInfoWrapper getCommandInfo(String cmdName) throws TangoProxyException;

    boolean hasCommand(String name) throws TangoProxyException;

    /**
     * Exports standard DeviceProxy API from TangORB
     *
     * @return this proxy DeviceProxy representation
     * @throws TangoProxyException
     */
    DeviceProxy toDeviceProxy();

    TangoEventsAdapter toTangoEventsAdapter();
}
