package org.tango.client.ez.data.type;

import fr.esrf.Tango.AttributeDataType;
import fr.esrf.TangoApi.DeviceAttribute;
import org.junit.Test;
import org.tango.client.ez.data.TangoDataWrapper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ScalarTangoDataTypesTest {
    @Test
    public void test_ushort() throws Exception {
        DeviceAttribute attr = new DeviceAttribute("test_ushort");

        TangoDataWrapper data = TangoDataWrapper.create(attr);

        ScalarTangoDataTypes.U_SHORT.insert(data, 123);

        //TODO might fail if TangORB version is different from 9
        assertEquals(AttributeDataType.ATT_USHORT, attr.getAttributeValueObject_5().value.discriminator());
        assertArrayEquals(new short[]{123}, attr.getAttributeValueObject_5().value.ushort_att_value());
    }
}