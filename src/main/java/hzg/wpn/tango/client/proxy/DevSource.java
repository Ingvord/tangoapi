package hzg.wpn.tango.client.proxy;

/**
 * Wraps {@link fr.esrf.Tango.DevSource} into enum
 *
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 10.10.13
 */
public enum DevSource {
    DEV(fr.esrf.Tango.DevSource.DEV),
    CACHE(fr.esrf.Tango.DevSource.CACHE),
    DEV_CACHE(fr.esrf.Tango.DevSource.CACHE_DEV);

    private final fr.esrf.Tango.DevSource value;

    DevSource(fr.esrf.Tango.DevSource devSource) {
        this.value = devSource;
    }

    fr.esrf.Tango.DevSource asDevSource() {
        return value;
    }

    public static DevSource fromInt(int id) {
        switch (id) {
            case 0:
                return DEV;
            case 1:
                return CACHE;
            case 2:
                return DEV_CACHE;
            default:
                throw new IllegalArgumentException("Unknown DevSource id:" + id + ". Valid values are: 0, 1, 2");
        }
    }
}
