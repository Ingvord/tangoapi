package org.tango.client.ez.data;

import com.google.common.base.Preconditions;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.lang.reflect.Array;

/**
 *
 * T one of the primitive array(int,float)
 *
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 27.02.2015
 */
public class TangoImage<T> {
    public final T data;
    public final int width;
    public final int height;

    public TangoImage(T data, int width, int height) {
        this.data = data;
        this.width = width;
        this.height = height;
    }

    public T[] to2DArray(){
        return (T[]) extract(data, width, height);//resulting array is of type T see first code line of extract
    }

    public RenderedImage toRenderedImage(){
        return null;//TODO
    }

    public static <T> TangoImage<T> convertFrom2DArray(Object src){
        Preconditions.checkArgument(src.getClass().isArray(), "Array type is expected here!");
        int dimX = Array.getLength(Array.get(src, 0));
        int dimY = Array.getLength(src);
        return new TangoImage<T>((T) insert(src, dimX, dimY),dimX,dimY);
    }

    /**
     * Starts several threads to speedup extraction process if dimY/CPUS > THRESHOLD
     * AND System.getProperty(TANGO_IMAGE_EXTRACTER_USE_MULTITHREADING) is set to true.
     *
     * @param value 1-dimensional array
     * @param dimX  x
     * @param dimY  y
     * @return 2-dimensional array:x,y
     */
    static Object extract(final Object value, final int dimX, final int dimY) {
        final Object result = Array.newInstance(value.getClass().getComponentType(), dimY, dimX);

        for (int i = 0, k = 0; i < dimY; i++, k += dimX)
            System.arraycopy(value, k, Array.get(result, i), 0, dimX);

        return result;
    }

    /**
     * @param value 2-dimensional array:x,y
     * @param dimX  x
     * @param dimY  y
     * @param <V>   1-dimensional array
     * @return 1-dimensional array
     */
    static <V> V insert(Object value, int dimX, int dimY) {
        Object result = Array.newInstance(value.getClass().getComponentType().getComponentType(), dimX * dimY);
        for (int i = 0, k = 0; i < dimY; i++, k += dimX)
            System.arraycopy(Array.get(value, i), 0, result, k, dimX);
        return (V) result;
    }
}
