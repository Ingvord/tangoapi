/*
 * The main contributor to this project is Institute of Materials Research,
 * Helmholtz-Zentrum Geesthacht,
 * Germany.
 *
 * This project is a contribution of the Helmholtz Association Centres and
 * Technische Universitaet Muenchen to the ESS Design Update Phase.
 *
 * The project's funding reference is FKZ05E11CG1.
 *
 * Copyright (c) 2012. Institute of Materials Research,
 * Helmholtz-Zentrum Geesthacht,
 * Germany.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */

package hzg.wpn.tango.client.proxy;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.*;
import fr.esrf.TangoApi.events.TangoArchive;
import fr.esrf.TangoApi.events.TangoChange;
import fr.esrf.TangoApi.events.TangoPeriodic;
import fr.esrf.TangoApi.events.TangoUser;
import hzg.wpn.tango.client.attribute.Quality;
import hzg.wpn.tango.client.data.TangoDataWrapper;
import hzg.wpn.tango.client.data.TangoDeviceAttributeWrapper;
import hzg.wpn.tango.client.data.format.TangoDataFormat;
import hzg.wpn.tango.client.data.type.*;
import org.javatuples.Triplet;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class is a main entry point of the proxy framework.
 * <p/>
 * This class encapsulates {@link DeviceProxy} and a number of routines which should be performed by every client
 * of the Tango Java API. These routines are: type conversion, data extraction, exception handling etc.
 *
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 07.06.12
 */
public final class DeviceProxyWrapper implements TangoProxy {
    private final DeviceProxy proxy;

    /**
     * @param name path to tango server
     * @throws TangoProxyException
     */
    protected DeviceProxyWrapper(String name) throws TangoProxyException {
        try {
            this.proxy = new DeviceProxy(name);
        } catch (DevFailed devFailed) {
            throw new TangoProxyException(devFailed);
        }
    }

    protected DeviceProxyWrapper(DeviceProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public String getName() {
        return this.proxy.name();
    }

    /**
     * Checks if attribute specified by name is exists.
     *
     * @param attrName name
     * @return true if attribute is ok, false - otherwise
     */
    @Override
    public boolean isAttributeExists(String attrName) {
        try {
            AttributeInfo attributeInfo = this.proxy.get_attribute_info(attrName);
            return true;
        } catch (DevFailed devFailed) {
            return false;
        }
    }

    /**
     * Returns {@link TangoAttributeInfoWrapper} for the attribute specified by name or null.
     *
     * @param attrName name
     * @return TangoAttributeInfoWrapper
     * @throws TangoProxyException
     */
    @Override
    public TangoAttributeInfoWrapper getAttributeInfo(String attrName) {
        try {
            AttributeInfo attributeInfo = this.proxy.get_attribute_info(attrName);
            return new TangoAttributeInfoWrapper(attributeInfo);
        } catch (DevFailed | UnknownTangoDataType ex) {
            return null;
        }
    }

    /**
     * Reads attribute specified by name and returns value of appropriate type (if defined in TangoDataFormat and TangoDataTypes)
     *
     * @param attrName name
     * @param <T>      type of value
     * @return value
     * @throws TangoProxyException
     */
    @Override
    public <T> T readAttribute(String attrName) throws TangoProxyException {
        try {
            DeviceAttribute deviceAttribute = this.proxy.read_attribute(attrName);
            return readAttributeValue(attrName, deviceAttribute);
        } catch (DevFailed | ValueExtractionException e) {
            throw new TangoProxyException(e);
        }
    }

    /**
     * Same as {@link DeviceProxyWrapper#readAttribute(String)} but returns a pair of value and time in milliseconds.
     *
     * @param attrName name
     * @param <T>      type of value
     * @return pair of value and time
     * @throws TangoProxyException
     */
    @Override
    public <T> Map.Entry<T, Long> readAttributeValueAndTime(String attrName) throws TangoProxyException {
        try {
            DeviceAttribute deviceAttribute = this.proxy.read_attribute(attrName);
            T result = readAttributeValue(attrName, deviceAttribute);

            long time = deviceAttribute.getTimeValMillisSec();
            return new AbstractMap.SimpleImmutableEntry<T, Long>(result, time);
        } catch (DevFailed | ValueExtractionException e) {
            throw new TangoProxyException(e);
        }
    }

    private <T> T readAttributeValue(String attrName, DeviceAttribute deviceAttribute) throws DevFailed, ValueExtractionException {
        if (deviceAttribute.hasFailed()) {
            throw new DevFailed(deviceAttribute.getErrStack());
        }
        TangoDataWrapper dataWrapper = TangoDataWrapper.create(deviceAttribute);
        AttributeInfo attributeInfo = this.proxy.get_attribute_info(attrName);
        TangoDataFormat<T> dataFormat = TangoDataFormat.createForAttrDataFormat(attributeInfo.data_format);
        return dataFormat.extract(dataWrapper);
    }

    /**
     * @param attrName
     * @param <T>
     * @return a triplet(val,time,quality)
     * @throws TangoProxyException
     */
    @Override
    public <T> Triplet<T, Long, Quality> readAttributeValueTimeQuality(String attrName) throws TangoProxyException {
        try {
            DeviceAttribute deviceAttribute = this.proxy.read_attribute(attrName);
            T result = readAttributeValue(attrName, deviceAttribute);

            long time = deviceAttribute.getTimeValMillisSec();
            Quality quality = Quality.fromAttrQuality(deviceAttribute.getQuality());

            return new Triplet<T, Long, Quality>(result, time, quality);
        } catch (DevFailed | ValueExtractionException e) {
            throw new TangoProxyException(e);
        }
    }

    /**
     * Writes a new value of type T to an attribute specified by name.
     *
     * @param attrName name
     * @param value    new value
     * @param <T>      type of value
     * @throws TangoProxyException
     */
    @Override
    public <T> void writeAttribute(String attrName, T value) throws TangoProxyException {
        DeviceAttribute deviceAttribute = new DeviceAttribute(attrName);
        TangoDataWrapper dataWrapper = TangoDataWrapper.create(deviceAttribute);

        try {
            AttributeInfo attributeInfo = this.proxy.get_attribute_info(attrName);
            int devDataType = attributeInfo.data_type;
            TangoDataFormat<T> dataFormat = TangoDataFormat.createForAttrDataFormat(attributeInfo.data_format);
            dataFormat.insert(dataWrapper, value, devDataType);
            this.proxy.write_attribute(deviceAttribute);
        } catch (DevFailed | ValueInsertionException e) {
            throw new TangoProxyException(e);
        }
    }

    /**
     * Executes command on tango server. Command is specified by name.
     * Encapsulates conversion {@link DeviceData}<->actual type (T,V).
     *
     * @param cmd   name
     * @param value input
     * @param <T>   type of input
     * @param <V>   type of output
     * @return result
     * @throws TangoProxyException
     */
    @Override
    public <T, V> V executeCommand(String cmd, T value) throws TangoProxyException {
        try {
            DeviceData argin = new DeviceData();
            TangoDataWrapper arginWrapper = TangoDeviceAttributeWrapper.create(argin);
            CommandInfo cmdInfo = this.proxy.command_query(cmd);
            TangoDataType<T> typeIn = TangoDataTypes.forTangoDevDataType(cmdInfo.in_type);
            typeIn.insert(arginWrapper, value);

            DeviceData argout = this.proxy.command_inout(cmd, argin);
            TangoDataWrapper argoutWrapper = TangoDataWrapper.create(argout);

            TangoDataType<V> typeOut = TangoDataTypes.forTangoDevDataType(cmdInfo.out_type);
            return typeOut.extract(argoutWrapper);
        } catch (Exception e) {
            throw new TangoProxyException(e);
        }
    }

    private final ConcurrentMap<String, CallBack> callBacks = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, TangoEventDispatcher<?>> dispatchers = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Integer> eventIds = new ConcurrentHashMap<>();

    private final Lock subscriptionGuard = new ReentrantLock();

    @Override
    public void subscribeToEvent(String attrName, TangoEvent event) throws TangoProxyException {
        //TODO filters
        String[] filters = new String[0];
        String eventKey = getEventKey(attrName, event);
        try {
            CallBack callBack = callBacks.get(eventKey);
            if (callBack == null) {
                TangoEventDispatcher<?> dispatcher = new TangoEventDispatcher<>();
                TangoEventDispatcher<?> oldDispatcher = dispatchers.putIfAbsent(eventKey, dispatcher);
                if (oldDispatcher != null) dispatcher = oldDispatcher;//this may create unused dispatcher instance

                callBack = createCallBack(attrName, event, dispatcher, filters);
                CallBack oldCallBack = callBacks.putIfAbsent(eventKey, callBack);
                if (oldCallBack != null) callBack = oldCallBack;//this may create unused callback instance

                subscriptionGuard.lock();
                try {
                    if (eventIds.containsKey(eventKey)) return;
                    int id = this.proxy.subscribe_event(attrName, event.getAlias(), callBack, filters);
                    eventIds.put(eventKey, id);
                } finally {
                    subscriptionGuard.unlock();
                }
            }


        } catch (Exception e) {
            throw new TangoProxyException(e);
        }
    }

    private String getEventKey(String attrName, TangoEvent event) {
        return this.proxy.name() + "/" + attrName + "." + event.name().toLowerCase();
    }

    private CallBack createCallBack(String attrName, TangoEvent event, TangoEventDispatcher<?> dispatcher, String... filters) throws DevFailed {
        switch (event) {
            case CHANGE:
                TangoChange change = new TangoChange(proxy, attrName, filters);
                change.addTangoChangeListener(dispatcher, true);
                return change;
            case PERIODIC:
                TangoPeriodic periodic = new TangoPeriodic(proxy, attrName, filters);
                periodic.addTangoPeriodicListener(dispatcher, true);
                return periodic;
            case ARCHIVE:
                TangoArchive archive = new TangoArchive(proxy, attrName, filters);
                archive.addTangoArchiveListener(dispatcher, true);
                return archive;
            case USER:
                TangoUser user = new TangoUser(proxy, attrName, filters);
                user.addTangoUserListener(dispatcher, true);
                return user;
            default:
                throw new IllegalArgumentException("Unknown TangoEvent:" + event);
        }
    }

    public <T> void addEventListener(String attrName, TangoEvent event, TangoEventListener<T> listener) {
        ((TangoEventDispatcher<T>) dispatchers.get(getEventKey(attrName, event))).addListener(listener);
    }

    @Override
    public void unsubscribeFromEvent(String attrName, TangoEvent event) throws TangoProxyException {
        String eventKey = getEventKey(attrName, event);
        subscriptionGuard.lock();
        try {
            if (!eventIds.containsKey(eventKey)) return;
            int eventId = eventIds.get(eventKey);
            eventIds.remove(eventKey);
            this.proxy.unsubscribe_event(eventId);
        } catch (Throwable throwable) {
            throw new TangoProxyException(throwable);
        } finally {
            subscriptionGuard.unlock();
        }
    }

    /**
     * Returns {@link TangoCommandInfoWrapper} instance or null.
     *
     * @param cmdName
     * @return
     * @throws TangoProxyException
     */
    @Override
    public TangoCommandInfoWrapper getCommandInfo(String cmdName) {
        try {
            return new TangoCommandInfoWrapper(proxy.command_query(cmdName));
        } catch (DevFailed | UnknownTangoDataType devFailed) {
            return null;
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("proxy", proxy.name())
                .toString();
    }

    //TODO replace with set of Strings (command names)
    private final Map<String, Boolean> hasCommandCache = Maps.newHashMap();

    /**
     * Uses unsynchronized {@link java.util.HashMap} for caching values. This is thread safe because cached value is not changing over time
     * and if two or more threads add a similar value - who cares. Performance might suffer in this case because threads perform network call.
     * But this should be an issue and if it is implementation will be changed (introduce Future)
     *
     * @param name
     * @return
     */
    @Override
    public boolean hasCommand(String name) {
        Boolean hasCommand = hasCommandCache.get(name);
        if (hasCommand == null) hasCommandCache.put(name, hasCommand = getCommandInfo(name) != null);
        return hasCommand;
    }

    @Override
    public DeviceProxy toDeviceProxy() {
        return proxy;
    }
}
