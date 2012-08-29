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

package wpn.hdri.tango.attribute;

import com.google.common.base.Objects;
import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoDs.*;
import wpn.hdri.tango.data.TangoDataWrapper;
import wpn.hdri.tango.data.format.TangoDataFormat;
import wpn.hdri.tango.data.type.TangoDataType;
import wpn.hdri.tango.data.type.ValueExtractionException;
import wpn.hdri.tango.data.type.ValueInsertionException;
import wpn.hdri.tango.util.TangoUtils;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Instances of this class are used as tango java server attributes.
 * <p/>
 * This class encapsulates a value of the attribute and logic of reading and writing of the value. See {@link TangoAttribute#read(fr.esrf.TangoDs.Attribute)}
 * and {@link TangoAttribute#write(fr.esrf.TangoDs.WAttribute)}
 * By default the value is stored in {@link AtomicReference}. It is also possible to provide custom read/write logic
 * by implementing a custom {@link TangoAttributeListener} and passing it to the constructor of this class.
 * <p/>
 * To integrate this class into tango java server do the following:
 * <ol>
 * <li>Define a container for the attributes, for instance a enum</li>
 * <li>Add the attributes to server during its initialization</li>
 * <li>Implement server's read and write attribute methods</li>
 * </ol>
 * Below is a complete example:
 * <pre>
 *     {@code
 *     //attributes container
 *     //MyAttribute.java
 *     public enum MyAttribute{
 *         MY_ATTR(new TangoAttribute<String>(
 *             "MyAttribute",TangoDataFormat.createScalarDataFormat(),ScalarTangoDataTypes.STRING,EnumAttrWriteType.READ_WRITE,null));
 *
 *         private final TangoAttribute<?> tangoAttribute;
 *
 *         private MyAttribute(TangoAttribute<?> tangoAttribute){
 *             this.tangoAttribute = tangoAttribute;
 *         }
 *
 *         public TangoAttribute toTangoAttribute(){
 *             return tangoAttribute;
 *         }
 *     }
 *     }
 *
 *     //add attributes to server
 *     //MyTangoServer.java
 *     ...
 *     //name to attribute map
 *     private final Map<String, TangoAttribute<?>> attributes = new HashMap<String, TangoAttribute<?>>();
 *     ...
 *     public void init_device() throws DevFailed {
 *         for(MyAttribute myAttribute : MyAttribute.values()){
 *             attributes.put(myAttribute.toTangoAttribute().getName(),myAttribute.toTangoAttribute());
 *             //now add the attribute to tango
 *             add_attribute(attribute.toTangoAttribute().toAttr());
 *         }
 *     }
 *     ...
 *     //implement read/write attribute methods
 *     public void read_attr(Attribute attr) throws DevFailed {
 *         String attrName = attr.get_name();
 *
 *         TangoAttribute<?> attribute = attributes.get(attr_name);
 *
 *         attribute.read(attr);
 *     }
 *
 *     public void write_attr_hardware(Vector attr_list) throws DevFailed {
 *         for (int i = 0; i < attr_list.size(); i++) {
 *             WAttribute att = dev_attr.get_w_attr_by_ind(((Integer) (attr_list.elementAt(i))).intValue());
 *             String attr_name = att.get_name();
 *
 *             TangoAttribute<?> attribute = attributes.get(attr_name);
 *
 *             attribute.write(att);
 *         }
 *     }
 * </pre>
 * <p/>
 * <p/>
 * Thread safeness is not guaranteed when custom listener is used. In other words
 * thread safeness depends on thread safeness of the listener.
 * <p/>
 * Immutability is not guaranteed when mutable value is being used.
 *
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 11.06.12
 */
@ThreadSafe
public final class TangoAttribute<T> {
    /**
     * Scalar, Spectrum or Image
     */
    protected final TangoDataFormat<T> format;
    /**
     * Corresponds to {@link TangoConst}.Tango_DEV_XXX
     */
    protected final TangoDataType<T> type;
    /**
     * A name of the attribute
     */
    protected final String name;
    /**
     * {@link Attr} is being created during construction of this class
     */
    protected final Attr attr;

    /**
     *
     */
    protected final TangoAttributeListener<T> listener;

    /**
     * Value holder
     */
    protected final AtomicReference<T> refToValue = new AtomicReference<T>();

    /**
     * @param name      a name of the attribute
     * @param format    instance of {@link TangoDataFormat}
     * @param type      instance of {@link TangoDataType}
     * @param writeType instance of {@link EnumAttrWriteType}
     * @param listener  implementation of {@link TangoAttributeListener} or null
     * @throws UnrecoverableTangoException
     * @construtor
     */
    public TangoAttribute(String name, TangoDataFormat<T> format, TangoDataType<T> type, EnumAttrWriteType writeType,
                          @Nullable TangoAttributeListener<T> listener) {
        this.name = name;
        this.format = format;
        this.type = type;
        this.listener = listener;
        try {
            switch (format.toAttrDataFormat().value()) {
                case AttrDataFormat._SCALAR:
                    this.attr = new Attr(name, format.getDataType(type.getAlias()).getAlias(), writeType.toAttrWriteType());
                    break;
                case AttrDataFormat._SPECTRUM:
                    //TODO replace MAX_VALUE
                    this.attr = new SpectrumAttr(name, format.getDataType(type.getAlias()).getAlias(), Integer.MAX_VALUE);
                    break;
                case AttrDataFormat._IMAGE:
                    //TODO replace MAX_VALUE
                    this.attr = new ImageAttr(name, format.getDataType(type.getAlias()).getAlias(), Integer.MAX_VALUE, Integer.MAX_VALUE);
                    break;
                case AttrDataFormat._FMT_UNKNOWN:
                default:
                    throw TangoUtils.createDevFailed(new IllegalArgumentException("Unknown data format code:" + format.toAttrDataFormat().value()));
            }
        } catch (DevFailed devFailed) {
            throw new UnrecoverableTangoException("Can not create Attr instance.", devFailed);
        }
    }

    /**
     * Encapsulates logic of a value conversion, i.e. extracts a value of type T and passes it to {@link TangoAttribute#setCurrentValue(T)}
     *
     * @param attribute Tango framework specific attribute
     * @throws DevFailed
     */
    public final void write(WAttribute attribute) throws DevFailed {
        TangoDataWrapper attrWrapper = TangoDataWrapper.create(attribute);
        try {
            T value = format.extract(attrWrapper);
            setCurrentValue(value);
        } catch (ValueExtractionException e) {
            throw TangoUtils.createDevFailed(e);
        }
    }

    /**
     * Encapsulates logic of a value conversion, i.e. inserts a value ({@link TangoAttribute#getCurrentValue()}) of type T into an Attribute.
     *
     * @param attribute Tango framework specific attribute
     * @throws DevFailed
     */
    public final void read(Attribute attribute) throws DevFailed {
        T value = getCurrentValue();
        TangoDataWrapper attrWrapper = TangoDataWrapper.create(attribute);
        try {
            format.insert(attrWrapper, value, type);
        } catch (ValueInsertionException e) {
            throw TangoUtils.createDevFailed(e);
        }
    }

    /**
     * Generic method for write operation.
     * <p/>
     * If {@link TangoAttribute#listener} is not null a value will be passed to listener's onSave method.
     *
     * @param value new value of this attribute
     */
    public void setCurrentValue(T value) {
        refToValue.set(value);
        if (listener != null) {
            listener.onSave(value);
        }
    }

    /**
     * Generic method for read operation.
     * <p/>
     * If {@link TangoAttribute#listener} is not null a result of listener's onLoad method will be returned.
     *
     * @return actual value of this attribute
     */
    public T getCurrentValue() {
        if (listener != null) {
            T value = listener.onLoad();
            refToValue.set(value);
        }
        return refToValue.get();
    }

    public String getName() {
        return name;
    }

    public Attr toAttr() {
        return attr;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("name", name)
                .toString();
    }
}
