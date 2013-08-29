package wpn.hdri.tango.proxy;

import org.javatuples.Triplet;
import wpn.hdri.tango.attribute.Quality;

import java.util.Map;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 29.08.13
 */
public interface TangoProxyWrapper {
    String getName();

    boolean checkAttribute(String attrName);

    TangoAttributeInfoWrapper getAttributeInfo(String attrName);

    <T> T readAttribute(String attrName) throws TangoProxyException;

    <T> Map.Entry<T, Long> readAttributeValueAndTime(String attrName) throws TangoProxyException;

    <T> Triplet<T, Long, Quality> readAttributeValueTimeQuality(String attrName) throws TangoProxyException;

    <T> void writeAttribute(String attrName, T value) throws TangoProxyException;

    <T, V> V executeCommand(String cmd, T value) throws TangoProxyException;

    <T> int subscribeEvent(String attrName, TangoEvent event, TangoEventCallback<T> cbk) throws TangoProxyException;

    void unsubscribeEvent(int eventId) throws TangoProxyException;

    TangoCommandInfoWrapper getCommandInfo(String cmdName);

    boolean hasCommand(String name);
}
