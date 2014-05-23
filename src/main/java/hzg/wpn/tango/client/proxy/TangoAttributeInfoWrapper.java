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

package hzg.wpn.tango.client.proxy;

import fr.esrf.TangoApi.AttributeInfo;
import hzg.wpn.tango.client.data.format.TangoDataFormat;
import hzg.wpn.tango.client.data.type.TangoDataType;
import hzg.wpn.tango.client.data.type.TangoDataTypes;
import hzg.wpn.tango.client.data.type.UnknownTangoDataType;

/**
 * This class encapsulates {@link AttributeInfo} along with {@link TangoDataFormat}, {@link TangoDataType} and {@link Class}.
 *
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 08.06.12
 */
public final class TangoAttributeInfoWrapper {
    /**
     * Tango attribute info
     */
    protected final AttributeInfo info;

    /**
     * Attribute TangoDataType
     */
    protected final TangoDataType<?> type;

    /**
     * Corresponding to TangoDataType Class
     */
    protected final Class<?> clazz;

    /**
     * Attribute TangoDataFormat
     */
    protected final TangoDataFormat<?> format;

    /**
     * This class is created internally in {@link DeviceProxyWrapper}
     *
     * @param info attribute info
     */
    TangoAttributeInfoWrapper(AttributeInfo info) throws UnknownTangoDataType {
        this.info = info;
        this.format = TangoDataFormat.createForAttrDataFormat(info.data_format);
        this.type = TangoDataTypes.forTangoDevDataType(info.data_type);
        this.clazz = this.format.getDataType(this.type.getAlias()).getDataType();
    }

    public TangoDataFormat<?> getFormat() {
        return format;
    }

    public TangoDataType<?> getType() {
        return type;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public int getMaxDimX() {
        return info.max_dim_x;
    }

    public int getMaxDimY() {
        return info.max_dim_y;
    }
    //TODO others
}
