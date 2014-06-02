package org.tango.client.ez.proxy;

import fr.esrf.TangoApi.CommandInfo;
import org.tango.client.ez.data.type.TangoDataType;
import org.tango.client.ez.data.type.TangoDataTypes;
import org.tango.client.ez.data.type.UnknownTangoDataType;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 12.10.12
 */
public class TangoCommandInfoWrapper {
    private final CommandInfo info;
    private final TangoDataType<?> typeIn;
    private final TangoDataType<?> typeOut;

    public TangoCommandInfoWrapper(CommandInfo info) throws UnknownTangoDataType {
        this.info = info;
        this.typeIn = TangoDataTypes.forTangoDevDataType(info.in_type);
        this.typeOut = TangoDataTypes.forTangoDevDataType(info.out_type);
    }

    public Class<?> getArginType() {
        return typeIn.getDataType();
    }

    public Class<?> getArgoutType() {
        return typeOut.getDataType();
    }

    public CommandInfo toCommandInfo() {
        return info;
    }
}
