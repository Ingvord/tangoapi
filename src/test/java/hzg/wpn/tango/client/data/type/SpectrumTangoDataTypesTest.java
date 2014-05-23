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

package hzg.wpn.tango.client.data.type;

import fr.esrf.TangoApi.DeviceAttribute;
import hzg.wpn.tango.client.data.TangoDataWrapper;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 05.06.12
 */
public class SpectrumTangoDataTypesTest {
    @Test
    public void testStringArr() throws Exception {
        TangoDataType<String[]> instance = SpectrumTangoDataTypes.STRING_ARR;

        String[] values = {"Hello", "World", "!!!"};
        DeviceAttribute attribute = new DeviceAttribute("test", values, values.length, 0);
        TangoDataWrapper data = TangoDataWrapper.create(attribute);
        String[] result = instance.extract(data);

        assertArrayEquals(new String[]{"Hello", "World", "!!!"}, result);
    }

    @Test
    public void testDoubleArr() throws Exception {
        TangoDataType<double[]> instance = SpectrumTangoDataTypes.DOUBLE_ARR;

        double[] values = {1., 2., 3.};
        DeviceAttribute attribute = new DeviceAttribute("test", values, values.length, 0);
        TangoDataWrapper data = TangoDataWrapper.create(attribute);
        double[] result = instance.extract(data);

        assertArrayEquals(new double[]{1., 2., 3.}, result, 0.00001);
    }


    @Test
    public void testShortArr() throws Exception {
        TangoDataType<short[]> instance = SpectrumTangoDataTypes.SHORT_ARR;

        short[] values = {1, 2, 3, 4};
        DeviceAttribute attribute = new DeviceAttribute("test", values, values.length, 0);
        TangoDataWrapper data = TangoDataWrapper.create(attribute);
        short[] result = instance.extract(data);

        assertArrayEquals(new short[]{1, 2, 3, 4}, result);
    }

    @Test
    public void testCharArr() throws Exception {
        TangoDataType<char[]> instance = SpectrumTangoDataTypes.CHAR_ARR;

        char[] values = {'a', 'b', 'c', 'd'};
        DeviceAttribute attribute = new DeviceAttribute("test", new String(values).getBytes("UTF-8"), values.length, 0);
        TangoDataWrapper data = TangoDataWrapper.create(attribute);
        char[] result = instance.extract(data);

        assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, result);
    }
}
