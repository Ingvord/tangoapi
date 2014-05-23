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

package wpn.hdri.tango.data;

import fr.esrf.Tango.DevState;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 05.06.12
 */
public enum EnumDevState {
    ON(DevState._ON),
    OFF(DevState._OFF),
    CLOSE(DevState._CLOSE),
    OPEN(DevState._OPEN),
    INSERT(DevState._INSERT),
    EXTRACT(DevState._EXTRACT),
    MOVING(DevState._MOVING),
    STANDBY(DevState._STANDBY),
    FAULT(DevState._FAULT),
    INIT(DevState._INIT),
    RUNNING(DevState._RUNNING),
    ALARM(DevState._ALARM),
    DISABLE(DevState._DISABLE),
    UNKNOWN(DevState._UNKNOWN);

    private final int alias;

    private EnumDevState(int alias) {
        this.alias = alias;
    }

    private static final Map<Integer, EnumDevState> aliases = new HashMap<Integer, EnumDevState>();

    static {
        for (EnumDevState devState : EnumDevState.values()) {
            aliases.put(devState.alias, devState);
        }
    }

    public static EnumDevState forAlias(int alias) {
        if (!aliases.containsKey(alias)) {
            return UNKNOWN;
        }
        return aliases.get(alias);
    }

    public DevState toDevState() {
        return DevState.from_int(alias);
    }
}
