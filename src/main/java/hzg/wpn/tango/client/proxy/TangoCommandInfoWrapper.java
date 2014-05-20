package hzg.wpn.tango.client.proxy;

import fr.esrf.TangoApi.CommandInfo;
import hzg.wpn.tango.client.data.type.TangoDataType;
import hzg.wpn.tango.client.data.type.TangoDataTypes;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 12.10.12
 */
public class TangoCommandInfoWrapper {
    private final CommandInfo info;
    private final TangoDataType<?> typeIn;
    private final TangoDataType<?> typeOut;

    public TangoCommandInfoWrapper(CommandInfo info) {
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
}
