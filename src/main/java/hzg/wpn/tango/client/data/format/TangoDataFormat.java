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

package hzg.wpn.tango.client.data.format;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.DevFailed;
import hzg.wpn.tango.client.data.TangoDataWrapper;
import hzg.wpn.tango.client.data.type.TangoDataType;
import hzg.wpn.tango.client.data.type.ValueExtractionException;
import hzg.wpn.tango.client.data.type.ValueInsertionException;

/**
 * This class represents corresponding {@link AttrDataFormat} from Tango Java API.
 *
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 04.06.12
 */
public abstract class TangoDataFormat<T> {
    /**
     * int code of {@link AttrDataFormat}
     */
    protected final int alias;
    private final String strAlias;

    protected TangoDataFormat(int alias, String strAlias) {
        this.alias = alias;
        this.strAlias = strAlias;
    }

    /**
     * @return associated with this instance AttrDataFormat
     */
    public final AttrDataFormat toAttrDataFormat() {
        return AttrDataFormat.from_int(alias);
    }

    public static <T> TangoDataFormat<T> createScalarDataFormat() {
        return createForAttrDataFormat(AttrDataFormat.SCALAR);
    }

    public static <T> TangoDataFormat<T> createSpectrumDataFormat() {
        return createForAttrDataFormat(AttrDataFormat.SPECTRUM);
    }

    public static <T> TangoDataFormat<T> createImageDataFormat() {
        return createForAttrDataFormat(AttrDataFormat.IMAGE);
    }

    /**
     * Creates a new TangoDataFormat.
     *
     * @param alias int alias
     * @param <T>   type of underlying value
     * @return TangoDataFormat
     * @throws NullPointerException if no format was found for alias
     */
    public static <T> TangoDataFormat<T> createForAlias(int alias) {
        switch (alias) {
            case AttrDataFormat._SCALAR:
                return new ScalarTangoDataFormat<T>(alias, "Scalar");
            case AttrDataFormat._SPECTRUM:
                return new SpectrumTangoDataFormat<T>(alias, "Spectrum");
            case AttrDataFormat._IMAGE:
                return new ImageTangoDataFormat<T>(alias, "Image");
            case AttrDataFormat._FMT_UNKNOWN:
            default:
                return new UnknownTangoDataFormat<T>(alias, "Unknown format");
        }
    }

    /**
     * Creates a new TangoDataFormat.
     *
     * @param attrDataFormat AttrDataFormat instance
     * @param <T>            type of underlying value
     * @return TangoDataFormat
     */
    public static <T> TangoDataFormat<T> createForAttrDataFormat(AttrDataFormat attrDataFormat) {
        return createForAlias(attrDataFormat.value());
    }

    /**
     * Returns a match for int code provided. The match is an instance of TangoDataType.
     * So for Spectrum data format int code for double matches to TangoDataTypes.DOUBLE_ARR.
     *
     * @param devDataType int code of TangoConst.Tango_DEV_XXX
     * @return appropriate TangoDataType
     */
    //TODO replace int with enum
    public abstract TangoDataType<T> getDataType(int devDataType);

    /**
     * @param data value container
     * @return extracted value
     * @throws ValueExtractionException
     */
    public T extract(TangoDataWrapper data) throws ValueExtractionException {
        try {
            int devDataType = data.getType();
            TangoDataType<T> type = getDataType(devDataType);
            return type.extract(data);
        } catch (DevFailed devFailed) {
            throw new ValueExtractionException(devFailed);
        }
    }

    /**
     * @param data        value container
     * @param value
     * @param devDataType we need this because usually data is being passed does not contain type information
     * @throws hzg.wpn.tango.client.data.type.ValueInsertionException
     *
     */
    public void insert(TangoDataWrapper data, T value, int devDataType) throws ValueInsertionException {
        TangoDataType<T> type = getDataType(devDataType);
        type.insert(data, value);
    }

    /**
     * @param data  value container
     * @param value
     * @param type  we need this because usually data is being passed does not contain type information
     * @throws ValueInsertionException
     */
    public void insert(TangoDataWrapper data, T value, TangoDataType<T> type) throws ValueInsertionException {
        type.insert(data, value);
    }

    @Override
    public String toString() {
        return strAlias;
    }
}
