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

import com.google.common.collect.Sets;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.TangoDs.TangoConst;
import wpn.hdri.tango.data.EnumDevState;
import wpn.hdri.tango.data.TangoDataWrapper;
import wpn.hdri.tango.util.TangoUtils;

import java.util.Collection;

/**
 * This class aggregates all scalar data types.
 *
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 04.06.12
 */
public class ScalarTangoDataTypes {
    private ScalarTangoDataTypes() {
    }

    public static final TangoDataType<EnumDevState> DEV_STATE = new ScalarTangoDataType<EnumDevState>(TangoConst.Tango_DEV_STATE, "DevState", EnumDevState.class, new ValueExtracter<EnumDevState>() {
        @Override
        public EnumDevState extract(TangoDataWrapper data) throws ValueExtractionException {
            try {
                DevState state = data.extractDevState();
                return EnumDevState.forAlias(state.value());
            } catch (DevFailed devFailed) {
                throw new ValueExtractionException(TangoUtils.convertDevFailedToException(devFailed));
            }
        }
    }, new ValueInserter<EnumDevState>() {
        @Override
        public void insert(TangoDataWrapper data, EnumDevState value, int dimX, int dimY) {
            data.insert(value.toDevState());
        }
    }
    );

    public static final TangoDataType<Void> VOID = new ScalarTangoDataType<Void>(TangoConst.Tango_DEV_VOID, "DevVoid", Void.class, new ValueExtracter<Void>() {
        @Override
        public Void extract(TangoDataWrapper data) throws ValueExtractionException {
            return null;
        }
    }, new ValueInserter<Void>() {
        @Override
        public void insert(TangoDataWrapper data, Void value, int dimX, int dimY) {
        }
    }
    );

    public static final TangoDataType<Boolean> BOOLEAN = new ScalarTangoDataType<Boolean>(TangoConst.Tango_DEV_BOOLEAN, "DevBoolean", Boolean.class, new ValueExtracter<Boolean>() {
        @Override
        public Boolean extract(TangoDataWrapper data) throws ValueExtractionException {
            try {
                return data.extractBoolean();
            } catch (DevFailed devFailed) {
                throw new ValueExtractionException(TangoUtils.convertDevFailedToException(devFailed));
            }
        }
    }, new ValueInserter<Boolean>() {
        @Override
        public void insert(TangoDataWrapper data, Boolean value, int dimX, int dimY) {
            data.insert(value);
        }
    }
    );
    public static final TangoDataType<Double> DOUBLE = new ScalarTangoDataType<Double>(TangoConst.Tango_DEV_DOUBLE, "DevDouble", Double.class, new ValueExtracter<Double>() {
        @Override
        public Double extract(TangoDataWrapper input) throws ValueExtractionException {
            try {
                return input.extractDouble();
            } catch (DevFailed devFailed) {
                throw new ValueExtractionException(TangoUtils.convertDevFailedToException(devFailed));
            }
        }
    }, new ValueInserter<Double>() {
        @Override
        public void insert(TangoDataWrapper data, Double value, int dimX, int dimY) {
            data.insert(value);
        }
    }
    );
    public static final TangoDataType<Float> FLOAT = new ScalarTangoDataType<Float>(TangoConst.Tango_DEV_FLOAT, "DevFloat", Float.class, new ValueExtracter<Float>() {
        @Override
        public Float extract(TangoDataWrapper data) throws ValueExtractionException {
            try {
                return data.extractFloat();
            } catch (DevFailed devFailed) {
                throw new ValueExtractionException(TangoUtils.convertDevFailedToException(devFailed));
            }
        }
    }, new ValueInserter<Float>() {
        @Override
        public void insert(TangoDataWrapper data, Float value, int dimX, int dimY) {
            data.insert(value);
        }
    }
    );
    public static final TangoDataType<Short> SHORT = new ScalarTangoDataType<Short>(TangoConst.Tango_DEV_SHORT, "DevShort", Short.class, new ValueExtracter<Short>() {
        @Override
        public Short extract(TangoDataWrapper data) throws ValueExtractionException {
            try {
                return data.extractShort();
            } catch (DevFailed devFailed) {
                throw new ValueExtractionException(TangoUtils.convertDevFailedToException(devFailed));
            }
        }
    }, new ValueInserter<Short>() {
        @Override
        public void insert(TangoDataWrapper data, Short value, int dimX, int dimY) {
            data.insert(value);
        }
    }
    );
    public static final TangoDataType<Integer> U_SHORT = new ScalarTangoDataType<Integer>(TangoConst.Tango_DEV_USHORT, "DevUShort", Integer.class, new ValueExtracter<Integer>() {
        @Override
        public Integer extract(TangoDataWrapper data) throws ValueExtractionException {
            try {
                return data.extractUShort();
            } catch (DevFailed devFailed) {
                throw new ValueExtractionException(TangoUtils.convertDevFailedToException(devFailed));
            }
        }
    }, new ValueInserter<Integer>() {
        @Override
        public void insert(TangoDataWrapper data, Integer value, int dimX, int dimY) {
            data.insert(value);
        }
    }
    );
    public static final TangoDataType<Integer> INT = new ScalarTangoDataType<Integer>(TangoConst.Tango_DEV_LONG, "DevLong", Integer.class, new ValueExtracter<Integer>() {
        @Override
        public Integer extract(TangoDataWrapper data) throws ValueExtractionException {
            try {
                return data.extractLong();
            } catch (DevFailed devFailed) {
                throw new ValueExtractionException(TangoUtils.convertDevFailedToException(devFailed));
            }
        }
    }, new ValueInserter<Integer>() {
        @Override
        public void insert(TangoDataWrapper data, Integer value, int dimX, int dimY) {
            data.insert(value);
        }
    }
    );
    public static final TangoDataType<Long> U_INT = new ScalarTangoDataType<Long>(TangoConst.Tango_DEV_ULONG, "DevULong", Long.class, new ValueExtracter<Long>() {
        @Override
        public Long extract(TangoDataWrapper data) throws ValueExtractionException {
            try {
                return data.extractULong();
            } catch (DevFailed devFailed) {
                throw new ValueExtractionException(TangoUtils.convertDevFailedToException(devFailed));
            }
        }
    }, new ValueInserter<Long>() {
        @Override
        public void insert(TangoDataWrapper data, Long value, int dimX, int dimY) {
            data.insert(value);
        }
    }
    );
    public static final TangoDataType<Long> LONG = new ScalarTangoDataType<Long>(TangoConst.Tango_DEV_LONG64, "DevLong64", Long.class, new ValueExtracter<Long>() {
        @Override
        public Long extract(TangoDataWrapper data) throws ValueExtractionException {
            try {
                return data.extractLong64();
            } catch (DevFailed devFailed) {
                throw new ValueExtractionException(TangoUtils.convertDevFailedToException(devFailed));
            }
        }
    }, new ValueInserter<Long>() {
        @Override
        public void insert(TangoDataWrapper data, Long value, int dimX, int dimY) {
            data.insert(value);
        }
    }
    );
    public static final TangoDataType<Long> U_LONG = new ScalarTangoDataType<Long>(TangoConst.Tango_DEV_ULONG64, "DevULong64", Long.class, new ValueExtracter<Long>() {
        @Override
        public Long extract(TangoDataWrapper data) throws ValueExtractionException {
            try {
                return data.extractULong64();
            } catch (DevFailed devFailed) {
                throw new ValueExtractionException(TangoUtils.convertDevFailedToException(devFailed));
            }
        }
    }, new ValueInserter<Long>() {
        @Override
        public void insert(TangoDataWrapper data, Long value, int dimX, int dimY) {
            data.insert(value);
        }
    }
    );
    public static final TangoDataType<String> STRING = new ScalarTangoDataType<String>(TangoConst.Tango_DEV_STRING, "DevString", String.class, new ValueExtracter<String>() {
        @Override
        public String extract(TangoDataWrapper data) throws ValueExtractionException {
            try {
                return data.extractString();
            } catch (DevFailed devFailed) {
                throw new ValueExtractionException(TangoUtils.convertDevFailedToException(devFailed));
            }
        }
    }, new ValueInserter<String>() {
        @Override
        public void insert(TangoDataWrapper data, String value, int dimX, int dimY) {
            data.insert(value);
        }
    }
    );

    public static final TangoDataType<Character> U_CHAR = new ScalarTangoDataType<Character>(TangoConst.Tango_DEV_UCHAR, "DevUChar", Character.class, new ValueExtracter<Character>() {
        @Override
        public Character extract(TangoDataWrapper data) throws ValueExtractionException {
            try {
                return (char) data.extractUChar();
            } catch (DevFailed devFailed) {
                throw new ValueExtractionException(TangoUtils.convertDevFailedToException(devFailed));
            }
        }
    }, new ValueInserter<Character>() {
        @Override
        public void insert(TangoDataWrapper data, Character value, int dimX, int dimY) {
            data.insert((short) value.charValue());
        }
    }
    );


    static Collection<? extends TangoDataType<?>> values() {
        //TODO remove warning
        return Sets.newHashSet(DEV_STATE, VOID, BOOLEAN, STRING, SHORT, U_SHORT, U_CHAR, INT, U_INT, LONG, U_LONG, FLOAT, DOUBLE);
    }

    public final static class ScalarTangoDataType<T> extends TangoDataType<T> {
        protected ScalarTangoDataType(int tango_dev_data_type, String strAlias, Class<T> clazz, ValueExtracter<T> tValueExtracter, ValueInserter<T> tValueInserter) {
            super(tango_dev_data_type, strAlias, clazz, tValueExtracter, tValueInserter);
        }

        @Override
        public T extract(TangoDataWrapper data) throws ValueExtractionException {
            return extracter.extract(data);
        }

        @Override
        public void insert(TangoDataWrapper data, T value) {
            inserter.insert(data, value, 1, 0);
        }
    }
}
