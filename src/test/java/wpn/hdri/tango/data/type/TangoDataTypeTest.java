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

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.TangoDs.TangoConst;
import org.junit.Test;
import wpn.hdri.tango.data.TangoDataWrapper;
import wpn.hdri.tango.data.format.TangoDataFormat;

import java.util.Arrays;

import static junit.framework.Assert.*;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 04.06.12
 */
public class TangoDataTypeTest {
    @Test
    public void testConvert_Void() throws Exception {
        TangoDataType<Void> type = TangoDataTypes.forTangoDevDataType(TangoConst.Tango_DEV_VOID);

        DeviceAttribute attribute = new DeviceAttribute("test", "some value");
        TangoDataWrapper data = TangoDataWrapper.create(attribute);
        Void result = type.extract(data);

        assertNull(result);
    }

    @Test
    public void testConvert() throws Exception {
        TangoDataType<String> type = TangoDataTypes.forTangoDevDataType(TangoConst.Tango_DEV_STRING);

        DeviceAttribute attribute = new DeviceAttribute("test", "some value");
        TangoDataWrapper data = TangoDataWrapper.create(attribute);
        String result = type.extract(data);

        assertEquals("some value", result);
    }

    @Test
    public void testConvert_Array() throws Exception {
        TangoDataType<String[]> type = TangoDataTypes.forTangoDevDataType(TangoConst.Tango_DEVVAR_STRINGARRAY);

        DeviceAttribute attribute = new DeviceAttribute("test", new String[]{"some value"}, 0, 0);
        TangoDataWrapper data = TangoDataWrapper.create(attribute);
        String[] result = type.extract(data);

        assertEquals("[some value]", Arrays.toString(result));
    }

    @Test
    public void testInsert() throws Exception {

    }

    @Test
    public void testToString() {
        TangoDataType<?> type = TangoDataTypes.forClass(String.class);

        assertEquals("DevString", type.toString());
    }

    @Test
    public void testGetEncoded() throws Exception {
        TangoDataFormat<byte[]> format = TangoDataFormat.createForAttrDataFormat(AttrDataFormat.SCALAR);
        TangoDataType<byte[]> type = format.getDataType(TangoConst.Tango_DEV_ENCODED);

        assertSame(ScalarTangoDataTypes.DEV_ENCODED, type);
    }

    @Test
    public void testGetEncoded_Image() throws Exception {
        TangoDataFormat<byte[]> format = TangoDataFormat.createForAttrDataFormat(AttrDataFormat.IMAGE);
        TangoDataType<byte[]> type = format.getDataType(TangoConst.Tango_DEV_ENCODED);

        assertNull(type);
    }


    @Test(expected = NullPointerException.class)//there is no spectrum data type for DevEncoded
    public void testGetEncoded_Spectrum() throws Exception {
        TangoDataFormat<byte[]> format = TangoDataFormat.createForAttrDataFormat(AttrDataFormat.SPECTRUM);
        TangoDataType<byte[]> type = format.getDataType(TangoConst.Tango_DEV_ENCODED);

        assertNull(type);
    }
}
