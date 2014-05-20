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
import hzg.wpn.tango.client.attribute.Quality;
import hzg.wpn.tango.client.data.TangoDataWrapper;
import hzg.wpn.tango.client.data.TangoDeviceAttributeWrapper;
import hzg.wpn.tango.client.data.format.TangoDataFormat;
import hzg.wpn.tango.client.data.type.TangoDataType;
import hzg.wpn.tango.client.data.type.TangoDataTypes;
import hzg.wpn.tango.client.data.type.ValueExtractionException;
import hzg.wpn.tango.client.data.type.ValueInsertionException;
import org.javatuples.Triplet;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Map;

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
        } catch (DevFailed devFailed) {
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
        } catch (DevFailed devFailed) {
            throw new TangoProxyException(devFailed);
        } catch (ValueExtractionException e) {
            throw new TangoProxyException(e);
        } catch (Throwable throwable) {
            throw new TangoProxyException(throwable);
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
        } catch (DevFailed devFailed) {
            throw new TangoProxyException(devFailed);
        } catch (ValueExtractionException e) {
            throw new TangoProxyException(e);
        } catch (Throwable throwable) {
            throw new TangoProxyException(throwable);
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
        } catch (DevFailed devFailed) {
            throw new TangoProxyException(devFailed);
        } catch (ValueExtractionException e) {
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
        } catch (DevFailed devFailed) {
            throw new TangoProxyException(devFailed);
        } catch (ValueInsertionException e) {
            throw new TangoProxyException(e);
        } catch (Throwable throwable) {
            throw new TangoProxyException(throwable);
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
        } catch (DevFailed devFailed) {
            throw new TangoProxyException(devFailed);
        } catch (ValueExtractionException e) {
            throw new TangoProxyException(e);
        } catch (Throwable throwable) {
            throw new TangoProxyException(throwable);
        }
    }

    @Override
    public <T> int subscribeEvent(String attrName, TangoEvent event, TangoEventCallback<T> cbk) throws TangoProxyException {
        //TODO filters
        try {
            String[] filters = new String[0];
            CallBack callBack = event.subscribe(this.proxy, attrName, filters, cbk);


            return getEventId(callBack);
        } catch (DevFailed devFailed) {
            throw new TangoProxyException(devFailed);
        } catch (Throwable throwable) {
            throw new TangoProxyException(throwable);
        }
    }

    private int getEventId(CallBack callBack) throws TangoProxyException {
        Field eventIdFld = null;
        try {
            eventIdFld = callBack.getClass().getDeclaredField("event_identifier");
            eventIdFld.setAccessible(true);
            int eventId = Integer.parseInt(String.valueOf(eventIdFld.get(callBack)));
            return eventId;
        } catch (NoSuchFieldException e) {
            throw new TangoProxyException(e);
        } catch (IllegalAccessException e) {
            eventIdFld.setAccessible(false);
            throw new TangoProxyException(e);
        } catch (Exception e) {
            throw new TangoProxyException(e);
        } catch (Throwable throwable) {
            throw new TangoProxyException(throwable);
        }
    }

    @Override
    public void unsubscribeEvent(int eventId) throws TangoProxyException {
        try {
            this.proxy.unsubscribe_event(eventId);
        } catch (DevFailed devFailed) {
            throw new TangoProxyException(devFailed);
        } catch (Throwable throwable) {
            throw new TangoProxyException(throwable);
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
        } catch (DevFailed devFailed) {
            return null;
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("proxy", proxy.name())
                .toString();
    }

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
    public void setSource(DevSource src) throws TangoProxyException {
        try {
            proxy.set_source(src.asDevSource());
        } catch (DevFailed devFailed) {
            throw new TangoProxyException(devFailed);
        }
    }
}
