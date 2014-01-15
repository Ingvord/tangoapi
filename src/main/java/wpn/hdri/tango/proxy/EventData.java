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

package wpn.hdri.tango.proxy;

/**
 * Container for event data.
 * <p/>
 * This class contains a value passed through event and a time in milliseconds.
 * <p/>
 * An instance of this class is passed to {@link TangoEventCallback#onEvent(EventData)}.
 *
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 08.06.12
 */
public final class EventData<T> {
    private final T value;
    private final long time;

    public EventData(T value, long time) {
        this.value = value;
        this.time = time;
    }

    public T getValue() {
        return value;
    }

    /**
     * @return time in millis
     */
    public long getTime() {
        return time;
    }
}
