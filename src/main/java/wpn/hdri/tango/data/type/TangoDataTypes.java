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
        classes.put(Integer.class, ScalarTangoDataTypes.INT);
        classes.put(Long.class, ScalarTangoDataTypes.LONG);
        classes.put(Float.class, ScalarTangoDataTypes.FLOAT);
        classes.put(Double.class, ScalarTangoDataTypes.DOUBLE);
        //TODO others
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

    public static <T> TangoDataType<T> imageTypeForDevDataType(int devDataType) {
        return (TangoDataType<T>) imageTypes[devDataType];
    }
}
