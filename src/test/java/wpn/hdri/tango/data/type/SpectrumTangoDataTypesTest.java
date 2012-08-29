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

import fr.esrf.TangoApi.DeviceAttribute;
import org.junit.Test;
import wpn.hdri.tango.data.TangoDataWrapper;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 05.06.12
 */
public class SpectrumTangoDataTypesTest {
    @Test
    public void testStringsArr() throws Exception {
        TangoDataType<String[]> instance = SpectrumTangoDataTypes.STRING_ARR;

        String[] values = {"Hello", "World", "!!!"};
        DeviceAttribute attribute = new DeviceAttribute("test", values, values.length, 0);
        TangoDataWrapper data = TangoDataWrapper.create(attribute);
        String[] result = instance.extract(data);

        assertArrayEquals(new String[]{"Hello", "World", "!!!"}, result);
    }
}
