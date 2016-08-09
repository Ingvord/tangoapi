package org.tango.client.ez.proxy;

import fr.esrf.Tango.AttrQuality;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 10.11.2015
 */
public class ValueTimeQuality<T> {
    public final long time;
    public final T value;
    public final AttrQuality quality;

    public ValueTimeQuality(T value, long time, AttrQuality quality) {
        this.time = time;
        this.value = value;
        this.quality = quality;
    }
}
