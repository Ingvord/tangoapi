package org.tango.client.ez.data;

import fr.esrf.Tango.*;
import fr.esrf.TangoApi.DeviceDataHistory;
import fr.esrf.TangoApi.IDeviceDataHistoryDAO;
import org.omg.CORBA.Any;
import org.omg.CORBA.TypeCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 01.02.2016
 */
public class TangoDeviceDataHistoryWrapper extends TangoDataWrapper {
    private static final Logger logger = LoggerFactory.getLogger(TangoDeviceDataHistoryWrapper.class);

    private final DeviceDataHistory wrapped;

    public TangoDeviceDataHistoryWrapper(DeviceDataHistory wrapped) {
        super();
        this.wrapped = wrapped;
    }

    @Override
    public int getDimX() throws DevFailed {
        return wrapped.getDimX();
    }

    public void setTimeVal(TimeVal tval) {
        wrapped.setTimeVal(tval);
    }

    public TimeVal getTimeVal() {
        return wrapped.getTimeVal();
    }

    public long getTimeValSec() {
        return wrapped.getTimeValSec();
    }

    public long getTime() {
        return wrapped.getTime();
    }

    public void setAttrQuality(AttrQuality q) throws DevFailed {
        wrapped.setAttrQuality(q);
    }

    public AttrQuality getAttrQuality() throws DevFailed {
        return wrapped.getAttrQuality();
    }

    public void setDimX(int dim) throws DevFailed {
        wrapped.setDimX(dim);
    }

    public void setDimY(int dim) throws DevFailed {
        wrapped.setDimY(dim);
    }

    @Override
    public int getDimY() throws DevFailed {
        return wrapped.getDimY();
    }

    public Any extractAny() throws DevFailed {
        return wrapped.extractAny();
    }

    @Override
    public boolean extractBoolean() throws DevFailed {
        return wrapped.extractBoolean();
    }

    @Override
    public short extractUChar() throws DevFailed {
        return wrapped.extractUChar();
    }

    @Override
    public short extractShort() throws DevFailed {
        return wrapped.extractShort();
    }

    @Override
    public int extractUShort() throws DevFailed {
        return wrapped.extractUShort();
    }

    @Override
    public int extractLong() throws DevFailed {
        return wrapped.extractLong();
    }

    @Override
    public long extractULong() throws DevFailed {
        return wrapped.extractULong();
    }

    @Override
    public long extractLong64() throws DevFailed {
        return wrapped.extractLong64();
    }

    @Override
    public long extractULong64() throws DevFailed {
        return wrapped.extractULong64();
    }

    @Override
    public float extractFloat() throws DevFailed {
        return wrapped.extractFloat();
    }

    @Override
    public double extractDouble() throws DevFailed {
        return wrapped.extractDouble();
    }

    @Override
    public String extractString() throws DevFailed {
        return wrapped.extractString();
    }

    @Override
    public DevState extractDevState() throws DevFailed {
        return wrapped.extractDevState();
    }

    @Override
    public DevEncoded extractDevEncoded() throws DevFailed {
        return wrapped.extractDevEncoded();
    }

    @Override
    public boolean[] extractBooleanArray() throws DevFailed {
        return wrapped.extractBooleanArray();
    }

    @Override
    public byte[] extractCharArray() throws DevFailed {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public byte[] extractByteArray() throws DevFailed {
        return wrapped.extractByteArray();
    }

    @Override
    public short[] extractUCharArray() throws DevFailed {
        return wrapped.extractUCharArray();
    }

    @Override
    public void insert(boolean[] argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(boolean[] argin, int dim_x, int dim_y) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(double[] argin, int dim_x, int dim_y) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(float[] argin, int dim_x, int dim_y) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(int[] argin, int dim_x, int dim_y) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(long[] argin, int dim_x, int dim_y) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(short[] argin, int dim_x, int dim_y) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(String[] argin, int dim_x, int dim_y) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_u64(long[] argin, int dim_x, int dim_y) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_uc(byte[] argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_uc(byte[] argin, int dim_x, int dim_y) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_uc(short[] argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_uc(short[] argin, int dim_x, int dim_y) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_ul(int[] argin, int dim_x, int dim_y) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_ul(long[] argin, int dim_x, int dim_y) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_us(int[] argin, int dim_x, int dim_y) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_us(short[] argin, int dim_x, int dim_y) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public short[] extractShortArray() throws DevFailed {
        return wrapped.extractShortArray();
    }

    @Override
    public int[] extractUShortArray() throws DevFailed {
        short[] shorts = wrapped.extractUShortArray();
        int[] ints = new int[shorts.length];
        for (int i = 0; i < shorts.length; i++) {
            short s = shorts[i];
            ints[i] = s;
        }
        return ints;
    }

    @Override
    public DevVarLongStringArray extractDevVarLongStringArray() throws DevFailed {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public DevVarDoubleStringArray extractDevVarDoubleStringArray() throws DevFailed {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(boolean argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(byte[] argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(double argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public int[] extractLongArray() throws DevFailed {
        return wrapped.extractLongArray();
    }

    @Override
    public long[] extractULongArray() throws DevFailed {
        int[] ints = wrapped.extractULongArray();
        long[] longs = new long[ints.length];
        for (int i = 0; i < ints.length; i++) {
            int v = ints[i];
            longs[i] = v;
        }
        return longs;
    }

    @Override
    public long[] extractLong64Array() throws DevFailed {
        return wrapped.extractLong64Array();
    }

    @Override
    public long[] extractULong64Array() throws DevFailed {
        return wrapped.extractULong64Array();
    }

    @Override
    public float[] extractFloatArray() throws DevFailed {
        return wrapped.extractFloatArray();
    }

    @Override
    public double[] extractDoubleArray() throws DevFailed {
        return wrapped.extractDoubleArray();
    }

    @Override
    public String[] extractStringArray() throws DevFailed {
        return wrapped.extractStringArray();
    }

    @Override
    public DevState[] extractDevStateArray() throws DevFailed {
        return wrapped.extractDevStateArray();
    }

    @Override
    public DevEncoded[] extractDevEncodedArray() throws DevFailed {
        return wrapped.extractDevEncodedArray();
    }

    public DevVarLongStringArray extractLongStringArray() throws DevFailed {
        return wrapped.extractLongStringArray();
    }

    public DevVarDoubleStringArray extractDoubleStringArray() throws DevFailed {
        return wrapped.extractDoubleStringArray();
    }

    public boolean hasFailed() {
        return wrapped.hasFailed();
    }

    public void setErrStack(DevError[] err) {
        wrapped.setErrStack(err);
    }

    public DevError[] getErrStack() {
        return wrapped.getErrStack();
    }

    public TypeCode type() {
        return wrapped.type();
    }

    public String getName() {
        return wrapped.getName();
    }

    @Override
    public int getNbRead() {
        return wrapped.getNbRead();
    }

    public int getNbWritten() {
        return wrapped.getNbWritten();
    }

    public void setWrittenDimX(int nb) {
        wrapped.setWrittenDimX(nb);
    }

    public void setWrittenDimY(int nb) {
        wrapped.setWrittenDimY(nb);
    }

    public int getWrittenDimX() {
        return wrapped.getWrittenDimX();
    }

    public int getWrittenDimY() {
        return wrapped.getWrittenDimY();
    }

    @Override
    public int getType() throws DevFailed {
        return wrapped.getType();
    }

    public IDeviceDataHistoryDAO getDeviceedatahistoryDAO() {
        return wrapped.getDeviceedatahistoryDAO();
    }

    public int getDataLength() throws DevFailed {
        return wrapped.getDataLength();
    }

    public int insert(boolean[] values, int base) throws DevFailed {
        return wrapped.insert(values, base);
    }

    public int insert(short[] values, int base) throws DevFailed {
        return wrapped.insert(values, base);
    }

    public int insert(int[] values, int base) throws DevFailed {
        return wrapped.insert(values, base);
    }

    public int insert(long[] values, int base) throws DevFailed {
        return wrapped.insert(values, base);
    }

    public int insert(float[] values, int base) throws DevFailed {
        return wrapped.insert(values, base);
    }

    public int insert(double[] values, int base) throws DevFailed {
        return wrapped.insert(values, base);
    }

    public int insert(String[] values, int base) throws DevFailed {
        return wrapped.insert(values, base);
    }

    public int insert(DevState[] values, int base) throws DevFailed {
        return wrapped.insert(values, base);
    }

    public int insert(DevEncoded[] values, int base) throws DevFailed {
        return wrapped.insert(values, base);
    }

    public int[] insert(DevVarLongStringArray lsa, int[] bases) throws DevFailed {
        return wrapped.insert(lsa, bases);
    }

    public int[] insert(DevVarDoubleStringArray dsa, int[] bases) throws DevFailed {
        return wrapped.insert(dsa, bases);
    }

    @Override
    public void insert(double[] values) {
        try {
            wrapped.insert(values);
        } catch (DevFailed devFailed) {
            logger.warn("Insert has failed! Values: {}", Arrays.toString(values));
        }
    }

    @Override
    public void insert(float argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(float[] argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(int argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(int[] argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(long argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(long[] argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(short argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(short[] argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(String argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(String[] values) {
        try {
            wrapped.insert(values);
        } catch (DevFailed devFailed) {
            logger.warn("Insert has failed! Values: {}", Arrays.toString(values));
        }
    }

    @Override
    public void insert_u64(long argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_u64(long[] argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_uc(byte argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_uc(short argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_ul(int argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_ul(int[] argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_ul(long argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_ul(long[] argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_us(int argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_us(int[] argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_us(short argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert_us(short[] argin) {
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(DevEncoded encoded){
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(DevVarLongStringArray array){
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(DevVarDoubleStringArray array){
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(DevState state){
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(DevState state[]){
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }

    @Override
    public void insert(DevState state[], int x, int y){
        throw new UnsupportedOperationException("This method is not supported in " + this.getClass());
    }
}
