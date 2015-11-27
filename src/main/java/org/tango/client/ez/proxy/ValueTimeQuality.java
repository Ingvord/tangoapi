package org.tango.client.ez.proxy;

import org.tango.client.ez.attribute.Quality;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 10.11.2015
 */
public class ValueTimeQuality<T> {
    private final long time;
    private final T value;
    private final Quality quality;

    public ValueTimeQuality(T value, long time, Quality quality) {
        this.time = time;
        this.value = value;
        this.quality = quality;
    }

    public long getTime() {
        return time;
    }

    public T getValue() {
        return value;
    }

    public Quality getQuality() {
        return quality;
    }
}
