// +======================================================================
//   $Source$
//
//   Project:   ezTangORB
//
//   Description:  java source code for the simplified TangORB API.
//
//   $Author: Igor Khokhriakov <igor.khokhriakov@hzg.de> $
//
//   Copyright (C) :      2014
//                        Helmholtz-Zentrum Geesthacht
//                        Max-Planck-Strasse, 1, Geesthacht 21502
//                        GERMANY
//                        http://hzg.de
//
//   This file is part of Tango.
//
//   Tango is free software: you can redistribute it and/or modify
//   it under the terms of the GNU Lesser General Public License as published by
//   the Free Software Foundation, either version 3 of the License, or
//   (at your option) any later version.
//
//   Tango is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU Lesser General Public License for more details.
//
//   You should have received a copy of the GNU Lesser General Public License
//   along with Tango.  If not, see <http://www.gnu.org/licenses/>.
//
//  $Revision: 25721 $
//
// -======================================================================

package org.tango.client.ez.util;

import fr.esrf.Tango.*;

import java.util.Arrays;

/**
 * Provides a number of helpers to deal with {@link DevFailed}.
 * <p/>
 * {@link TangoUtils#convertDevFailedToException(fr.esrf.Tango.DevFailed)} takes a DevFailed and returns {@link Exception}
 * in which message is a full information of cause extracted from {@link DevError}s containing in the devFailed.
 * <p/>
 * {@link TangoUtils#createDevError(String, String)} creates new {@link DevError} with specified reason and description,
 * severity=ERR and current stack trace.
 * <p/>
 * {@link TangoUtils#createDevError(Throwable)} creates new {@link DevError} with specified reason (throwable.getMessage)
 * and description (throwable.getCause().getMessage()), severity=ERR and throwable stack trace.
 * <p/>
 * {@link TangoUtils#createDevFailed(Throwable)} creates new {@link DevFailed} with underlying {@link DevError} created from throwable.
 *
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 02.05.12
 */
public final class TangoUtils {
    //utility class
    private TangoUtils() {
    }

    /**
     * Returned {@link Exception} will contain meaningful information extracted from {@link DevFailed}.
     *
     * @param devFailed cause
     * @return Exception
     */
    public static Exception convertDevFailedToException(DevFailed devFailed) {
        Exception result = new Exception(devFailed.getLocalizedMessage(), null);
        for (int i = devFailed.errors.length - 1; i > -1; --i) {
            DevError error = devFailed.errors[i];
            result = new Exception(
                    String.format("%s: %s (%s)", error.reason, error.desc, error.origin), result);
        }
        return result;
    }

    /**
     * Creates new {@link DevError} with specified reason and description,
     * severity=ERR and current stack trace.
     *
     * @param reason      reason
     * @param description description
     * @return DevError
     */
    public static DevError createDevError(String reason, String description) {
        StackTraceElement[] traceElements = Thread.getAllStackTraces().get(Thread.currentThread());
        return new DevError(reason, ErrSeverity.ERR, description,
                Arrays.toString(
                        Arrays.copyOfRange(
                                traceElements, 3/*skip first elements: dumpThreads; getAllStackTraces; createDevError*/,
                                traceElements.length > 6 ? 6 : traceElements.length)));
    }

    /**
     * Creates new {@link DevError} with specified reason (throwable.getMessage)
     * and description (throwable.getCause().getMessage()), severity=ERR and throwable stack trace.
     *
     * @param throwable cause
     * @return DevError
     */
    public static DevError createDevError(Throwable throwable) {
        return new DevError(throwable.getMessage(), ErrSeverity.ERR, throwable.getCause() != null ? throwable.getCause().getMessage() : "",
                Arrays.toString(throwable.getStackTrace()));
    }

    public static DevFailed createDevFailed(Throwable throwable) {
        return new DevFailed(throwable.getMessage(), new DevError[]{
                createDevError(throwable)
        });
    }

//    /**
//     * Copy-n-paste from {@link fr.esrf.tangoatk.widget.attribute.RawImageViewer#setData(String, byte[])}
//     *
//     * @param encFormat
//     * @return
//     */
//    public static IImageFormat createImageFormat(String encFormat) {
//        IImageFormat result = null;
//        if (encFormat.equalsIgnoreCase("JPEG_GRAY8")) {
//            result = new Jpeg8ImageFormat();
//        } else if (encFormat.equalsIgnoreCase("GRAY8")) {
//            result = new Mono8ImageFormat();
//        } else if (encFormat.equalsIgnoreCase("GRAY16")) {
//            result = new Mono16ImageFormat();
//        } else if (encFormat.equalsIgnoreCase("JPEG_RGB")) {
//            result = new Jpeg24ImageFormat();
//        } else if (encFormat.equalsIgnoreCase("RGB24")) {
//            result = new RGB24ImageFormat();
//        } else {
//            throw new IllegalArgumentException("Format " + encFormat + " is not supported.");
//        }
//
//        return result;
//    }

    public static DispLevel displayLevelFromString(String dispLevel){
        switch (dispLevel.toUpperCase()){
            case "OPERATOR":
                return DispLevel.OPERATOR;
            case "EXPERT":
                return DispLevel.EXPERT;
            case "DL_UNKNOWN":
                return DispLevel.DL_UNKNOWN;
            default:
                throw new IllegalArgumentException("Unknown AttrWriteType:" + dispLevel);
        }
    }

    public static AttrWriteType attrWriteTypeFromString(String attrWriteType){
        switch (attrWriteType.toUpperCase()){
            case "READ":
                return AttrWriteType.READ;
            case "WRITE":
                return AttrWriteType.WRITE;
            case "READ_WRITE":
                return AttrWriteType.READ_WRITE;
            case "READ_WITH_WRITE":
                return AttrWriteType.READ_WITH_WRITE;
            default:
                throw new IllegalArgumentException("Unknown AttrWriteType:" + attrWriteType);
        }
    }

    public static AttrDataFormat attrDataFormatFromString(String attrDataFormat){
        switch (attrDataFormat.toUpperCase()){
            case "IMAGE":
                return AttrDataFormat.IMAGE;
            case "SPECTRUM":
                return AttrDataFormat.SPECTRUM;
            case "SCALAR":
                return AttrDataFormat.SCALAR;
            case "FMT_UNKNOWN":
                return AttrDataFormat.FMT_UNKNOWN;
            default:
                throw new IllegalArgumentException("Unknown AttrWriteType:" + attrDataFormat);
        }
    }
}
