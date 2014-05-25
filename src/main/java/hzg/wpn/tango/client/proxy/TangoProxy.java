package hzg.wpn.tango.client.proxy;

import hzg.wpn.tango.client.attribute.Quality;
import org.javatuples.Triplet;

import java.util.Map;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 29.08.13
 */
public interface TangoProxy {
    String getName();

    boolean isAttributeExists(String attrName);

    TangoAttributeInfoWrapper getAttributeInfo(String attrName);

    <T> T readAttribute(String attrName) throws TangoProxyException;

    <T> Map.Entry<T, Long> readAttributeValueAndTime(String attrName) throws TangoProxyException;

    <T> Triplet<T, Long, Quality> readAttributeValueTimeQuality(String attrName) throws TangoProxyException;

    <T> void writeAttribute(String attrName, T value) throws TangoProxyException;

    <T, V> V executeCommand(String cmd, T value) throws TangoProxyException;

    int subscribeToEvent(String attrName, TangoEvent event) throws TangoProxyException;

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

    void unsubscribeEvent(int eventId) throws TangoProxyException;

    TangoCommandInfoWrapper getCommandInfo(String cmdName);

    boolean hasCommand(String name);

    /**
     * Exports set_source method from DeviceProxy class
     *
     * @param devSource DevSource
     * @throws TangoProxyException
     * @see <a href="http://www.esrf.eu/computing/cs/tango/tango_doc/kernel_doc/ds_prog/node7.html#SECTION00713300000000000000">Tango API C++ documentation</a>
     */
    void setSource(DevSource devSource) throws TangoProxyException;
}
