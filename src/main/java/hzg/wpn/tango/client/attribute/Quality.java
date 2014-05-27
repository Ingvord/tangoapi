package hzg.wpn.tango.client.attribute;

import fr.esrf.Tango.AttrQuality;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 17.04.13
 */
public enum Quality {
    VALID(0),
    INVALID(1),
    ALARM(2),
    CHANGING(3),
    WARNING(4);

    private final int id;

    private Quality(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Quality fromAttrQuality(AttrQuality quality) {
        switch (quality.value()) {
            case 0:
                return VALID;
            case 1:
                return INVALID;
            case 2:
                return ALARM;
            case 3:
                return CHANGING;
            case 4:
                return WARNING;
        }
        throw new AssertionError("Should not happen!");
    }
}
