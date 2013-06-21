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

package wpn.hdri.tango.data.type;

import fr.esrf.TangoDs.TangoConst;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 05.06.12
 */
public final class TangoDataTypes {
    private static final TangoDataType<?>[] types = new TangoDataType<?>[30];

    static {
        for (TangoDataType<?> type : ScalarTangoDataTypes.values()) {
            TangoDataTypes.types[type.getAlias()] = type;
        }
        for (TangoDataType<?> type : SpectrumTangoDataTypes.values()) {
            TangoDataTypes.types[type.getAlias()] = type;
        }
        types[TangoConst.Tango_DEV_ENCODED] = ImageTangoDataTypes.ENCODED_IMAGE;
    }

    private static final Map<Class<?>, TangoDataType<?>> classes = new IdentityHashMap<Class<?>, TangoDataType<?>>();

    static {
        classes.put(String.class, ScalarTangoDataTypes.STRING);
        classes.put(Boolean.class, ScalarTangoDataTypes.BOOLEAN);
        classes.put(Short.class, ScalarTangoDataTypes.SHORT);
        classes.put(Character.class, ScalarTangoDataTypes.U_CHAR);
        classes.put(Integer.class, ScalarTangoDataTypes.INT);
        classes.put(Long.class, ScalarTangoDataTypes.LONG);
        classes.put(Float.class, ScalarTangoDataTypes.FLOAT);
        classes.put(Double.class, ScalarTangoDataTypes.DOUBLE);
        classes.put(boolean.class, ScalarTangoDataTypes.BOOLEAN);
        classes.put(short.class, ScalarTangoDataTypes.SHORT);
        classes.put(char.class, ScalarTangoDataTypes.U_CHAR);
        classes.put(int.class, ScalarTangoDataTypes.INT);
        classes.put(long.class, ScalarTangoDataTypes.LONG);
        classes.put(float.class, ScalarTangoDataTypes.FLOAT);
        classes.put(double.class, ScalarTangoDataTypes.DOUBLE);
        classes.put(String[].class, SpectrumTangoDataTypes.STRING_ARR);
        classes.put(short[].class, SpectrumTangoDataTypes.SHORT_ARR);
        classes.put(char[].class, SpectrumTangoDataTypes.CHAR_ARR);
        classes.put(int[].class, SpectrumTangoDataTypes.INT_ARR);
        classes.put(long[].class, SpectrumTangoDataTypes.LONG_ARR);
        classes.put(float[].class, SpectrumTangoDataTypes.FLOAT_ARR);
        classes.put(double[].class, SpectrumTangoDataTypes.DOUBLE_ARR);
        //TODO others
    }

    private static final Map<String, TangoDataType<?>> strings = new HashMap<String, TangoDataType<?>>();

    static {
        strings.put("DevString", ScalarTangoDataTypes.STRING);
        strings.put("DevDouble", ScalarTangoDataTypes.DOUBLE);
        strings.put("DevFloat", ScalarTangoDataTypes.FLOAT);
        strings.put("DevLong64", ScalarTangoDataTypes.LONG);
        strings.put("DevLong", ScalarTangoDataTypes.INT);
        strings.put("DevShort", ScalarTangoDataTypes.SHORT);
        strings.put("DevUShort", ScalarTangoDataTypes.U_SHORT);
        strings.put("DevULong", ScalarTangoDataTypes.U_INT);
        strings.put("DevUChar", ScalarTangoDataTypes.U_CHAR);
        strings.put("DevVarStringArr", SpectrumTangoDataTypes.STRING_ARR);
        strings.put("DevVarDoubleArr", SpectrumTangoDataTypes.DOUBLE_ARR);
        strings.put("DevVarFloatArr", SpectrumTangoDataTypes.FLOAT_ARR);
        strings.put("DevVarLong64Arr", SpectrumTangoDataTypes.LONG_ARR);
        strings.put("DevVarLongArr", SpectrumTangoDataTypes.INT_ARR);
        strings.put("DevVarShortArr", SpectrumTangoDataTypes.SHORT_ARR);
        strings.put("DevVarUShortArr", SpectrumTangoDataTypes.USHORT_ARR);
        strings.put("DevVarULongArr", SpectrumTangoDataTypes.ULONG_ARR);
        //TODO etc
    }

    private static final TangoDataType<?>[] imageTypes = new TangoDataType<?>[30];

    static {
        for (TangoDataType<?> type : ImageTangoDataTypes.values()) {
            imageTypes[type.getAlias()] = type;
        }
    }

    private TangoDataTypes() {
    }

    public static <T> TangoDataType<T> forTangoDevDataType(int tangoDevDataType) {
        return (TangoDataType<T>) types[tangoDevDataType];
    }

    public static <T> TangoDataType<T> forClass(Class<T> clazz) {
        return (TangoDataType<T>) classes.get(clazz);
    }

    public static <T> TangoDataType<T> forString(String devType) {
        return (TangoDataType<T>) strings.get(devType);
    }

    public static <T> TangoDataType<T> imageTypeForDevDataType(int devDataType) {
        return (TangoDataType<T>) imageTypes[devDataType];
    }
}
