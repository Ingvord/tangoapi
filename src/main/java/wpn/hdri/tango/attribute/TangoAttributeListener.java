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

package wpn.hdri.tango.attribute;

/**
 * Implementation of this interface is passed to {@link TangoAttribute} constructor.
 * When passed {@link TangoAttribute} calls {@link this#onSave(Object)} and {@link this#onLoad()}
 * methods to store and retrieve its value.
 * <p/>
 * If an implementation is thread safe then {@link TangoAttribute} can also be considered as thread safe.
 *
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 11.06.12
 */
public interface TangoAttributeListener<T> {
    /**
     * @return actual value of the attribute
     */
    T onLoad();

    /**
     * @param value new value of the attribute
     */
    void onSave(T value);
}
