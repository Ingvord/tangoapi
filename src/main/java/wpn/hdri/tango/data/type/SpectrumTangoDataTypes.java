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

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoDs.TangoConst;
import wpn.hdri.tango.data.TangoDataWrapper;
import wpn.hdri.tango.util.TangoUtils;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * This class aggregates spectrum data types.
 *
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 04.06.12
 */
public class SpectrumTangoDataTypes {
    private SpectrumTangoDataTypes() {
    }

    public static final TangoDataType<String[]> STRING_ARR = new SpectrumTangoDataType<String[]>(
            TangoConst.Tango_DEVVAR_STRINGARRAY, "DevVarStringArr", String[].class,
            new ValueExtracter<String[]>() {
                @Override
                public String[] extract(TangoDataWrapper data) throws ValueExtractionException {
                    try {
                        return data.extractStringArray();
                    } catch (DevFailed devFailed) {
                        throw new ValueExtractionException(TangoUtils.convertDevFailedToException(devFailed));
                    }
                }
            },
            new ValueInserter<String[]>() {
                @Override
                public void insert(TangoDataWrapper data, String[] value, int dimX, int dimY) {
                    data.insert(value);
                }
            }
    );

    public static final TangoDataType<double[]> DOUBLE_ARR = new SpectrumTangoDataType<double[]>(
            TangoConst.Tango_DEVVAR_DOUBLEARRAY, "DevVarDoubleArr", double[].class,
            new ValueExtracter<double[]>() {
                @Override
                public double[] extract(TangoDataWrapper data) throws ValueExtractionException {
                    try {
                        return data.extractDoubleArray();
                    } catch (DevFailed devFailed) {
                        throw new ValueExtractionException(TangoUtils.convertDevFailedToException(devFailed));
                    }
                }
            },
            new ValueInserter<double[]>() {
                @Override
                public void insert(TangoDataWrapper data, double[] value, int dimX, int dimY) {
                    data.insert(value);
                }
            }
    );

    public static final TangoDataType<float[]> FLOAT_ARR = new SpectrumTangoDataType<float[]>(
            TangoConst.Tango_DEVVAR_FLOATARRAY, "DevVarFloatArr", float[].class,
            new ValueExtracter<float[]>() {
                @Override
                public float[] extract(TangoDataWrapper data) throws ValueExtractionException {
                    try {
                        return data.extractFloatArray();
                    } catch (DevFailed devFailed) {
                        throw new ValueExtractionException(TangoUtils.convertDevFailedToException(devFailed));
                    }
                }
            },
            new ValueInserter<float[]>() {
                @Override
                public void insert(TangoDataWrapper data, float[] value, int dimX, int dimY) {
                    data.insert(value);
                }
            }
    );

    public static final TangoDataType<short[]> SHORT_ARR = new SpectrumTangoDataType<short[]>(
            TangoConst.Tango_DEVVAR_SHORTARRAY, "DevVarShortArr", short[].class,
            new ValueExtracter<short[]>() {
                @Override
                public short[] extract(TangoDataWrapper data) throws ValueExtractionException {
                    try {
                        return data.extractShortArray();
                    } catch (DevFailed devFailed) {
                        throw new ValueExtractionException(TangoUtils.convertDevFailedToException(devFailed));
                    }
                }
            },
            new ValueInserter<short[]>() {
                @Override
                public void insert(TangoDataWrapper data, short[] value, int dimX, int dimY) {
                    data.insert(value);
                }
            }
    );


    static Collection<? extends TangoDataType<?>> values() {
        return Sets.newHashSet(STRING_ARR, DOUBLE_ARR, FLOAT_ARR, SHORT_ARR);
    }

    public static final class SpectrumTangoDataType<T> extends TangoDataType<T> {
        protected SpectrumTangoDataType(int tango_dev_data_type, String strAlias, Class<T> clazz, ValueExtracter<T> tValueExtracter, ValueInserter<T> tValueInserter) {
            super(tango_dev_data_type, strAlias, clazz, tValueExtracter, tValueInserter);
        }

        @Override
        public T extract(TangoDataWrapper data) throws ValueExtractionException {
            try {
                Object src = extracter.extract(data);
                int newLength = data.getDimX() > -1 ? data.getDimX() : Array.getLength(src);
                if (data.getDimY() > 0) {
                    newLength *= data.getDimY();
                }
                Object result = Array.newInstance(src.getClass().getComponentType(), newLength);
                System.arraycopy(src, 0, result, 0, newLength);
                return (T) result;
            } catch (DevFailed devFailed) {
                throw new ValueExtractionException(devFailed);
            }
        }

        @Override
        public void insert(TangoDataWrapper data, T value) {
            Preconditions.checkArgument(value.getClass().isArray());
            inserter.insert(data, value, Array.getLength(value), 0);
        }
    }
}
