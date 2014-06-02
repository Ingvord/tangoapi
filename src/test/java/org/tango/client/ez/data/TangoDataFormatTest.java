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

package org.tango.client.ez.data;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.TangoApi.DeviceAttribute;
import org.junit.Assert;
import org.junit.Test;
import org.tango.client.ez.data.format.TangoDataFormat;

import java.lang.reflect.Array;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 05.06.12
 */
public class TangoDataFormatTest {
    @Test
    public void testExtract_String() throws Exception {
        TangoDataFormat<String> instance = TangoDataFormat.createForAttrDataFormat(AttrDataFormat.SCALAR);

        DeviceAttribute attribute = new DeviceAttribute("test", "some value");
        TangoDataWrapper data = TangoDataWrapper.create(attribute);
        String result = instance.extract(data);

        assertEquals("some value", result);
    }

    @Test
    public void testExtract_DoubleArr() throws Exception {
        TangoDataFormat<double[]> instance = TangoDataFormat.createForAttrDataFormat(AttrDataFormat.SPECTRUM);

        DeviceAttribute attribute = new DeviceAttribute("test", new double[]{0.1D, 0.9D, 0.8D, 0.4D}, 4, 0);
        TangoDataWrapper data = TangoDataWrapper.create(attribute);
        double[] result = instance.extract(data);

        assertArrayEquals(new double[]{0.1D, 0.9D, 0.8D, 0.4D}, result, 0.0);
    }

    @Test
    public void testExtract_Unknown() throws Exception {
        TangoDataFormat<Object> instance = TangoDataFormat.createForAttrDataFormat(AttrDataFormat.FMT_UNKNOWN);

        DeviceAttribute attribute = new DeviceAttribute("test", 1234);
        TangoDataWrapper data = TangoDataWrapper.create(attribute);

        Object result = instance.extract(data);
        assertEquals(1234, Array.getInt(result, 0));
    }

    @Test
    public void testExtract_Unknown_Arr() throws Exception {
        TangoDataFormat<Object> instance = TangoDataFormat.createForAttrDataFormat(AttrDataFormat.FMT_UNKNOWN);

        DeviceAttribute attribute = new DeviceAttribute("test", new int[]{1, 2, 3, 4}, 4, 0);
        TangoDataWrapper data = TangoDataWrapper.create(attribute);

        Object result = instance.extract(data);
        assertEquals(1, Array.getInt(result, 0));
        //TODO Tango does not set dimensions correctly: nbRead == 1.
//        assertEquals(2, Array.getInt(result, 1));
//        assertEquals(3, Array.getInt(result, 2));
//        assertEquals(4, Array.getInt(result, 3));
    }

    @Test
    public void testToString() {
        TangoDataFormat<?> instance = TangoDataFormat.createForAttrDataFormat(AttrDataFormat.SPECTRUM);

        Assert.assertEquals("Spectrum", instance.toString());
    }
}
