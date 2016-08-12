package org.tango.client.ez.proxy;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 10.11.2015
 */
public class ValueTime<T> {
    private final long time;
    private final T value;

    public ValueTime(T value, long time) {
        this.time = time;
        this.value = value;
    }

    public long getTime() {
        return time;
    }

    public T getValue() {
        return value;
    }
}
