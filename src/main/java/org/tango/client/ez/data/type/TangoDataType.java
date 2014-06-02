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

package org.tango.client.ez.data.type;

import org.tango.client.ez.data.TangoDataWrapper;

/**
 * Base class for all TangoDataTypes
 *
 * @param <T> extracted value type
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 27.04.12
 */
public abstract class TangoDataType<T> {
    private final int tangoDevDataType;
    private final Class<T> targetClazz;
    protected final ValueExtracter<T> extracter;
    protected final ValueInserter<T> inserter;

    private final String strAlias;

    protected TangoDataType(int tango_dev_data_type, String strAlias, Class<T> clazz, ValueExtracter<T> extracter, ValueInserter<T> inserter) {
        this.tangoDevDataType = tango_dev_data_type;
        this.strAlias = strAlias;
        this.targetClazz = clazz;
        this.extracter = extracter;
        this.inserter = inserter;
    }

    public Class<T> getDataType() {
        return targetClazz;
    }

    public abstract T extract(TangoDataWrapper data) throws ValueExtractionException;

    public abstract void insert(TangoDataWrapper data, T value) throws ValueInsertionException;

    public int getAlias() {
        return tangoDevDataType;
    }

    public final String toString() {
        return strAlias;
    }
}
