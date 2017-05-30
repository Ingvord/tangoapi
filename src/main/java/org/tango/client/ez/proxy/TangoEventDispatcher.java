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

package org.tango.client.ez.proxy;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.TangoApi.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tango.client.ez.data.TangoDataWrapper;
import org.tango.client.ez.data.format.TangoDataFormat;
import org.tango.client.ez.util.TangoUtils;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class is a common implementation for all ITangoXXXListener.
 * <p/>
 * Class contains its own listeners cache. Each listener is wrapped with WeakReference and therefore user must keep references to its callbacks.
 * <p/>
 * This class does not implement ITangoAttrConfigListener nor ITangoDataReadyListener. Use standard TangORB API
 *
 * @author ingvord
 * @since 5/26/14@1:20 AM
 */
public class TangoEventDispatcher<T> implements ITangoChangeListener, ITangoPeriodicListener,
        ITangoArchiveListener, ITangoUserListener {
    private final Logger logger = LoggerFactory.getLogger(TangoEventDispatcher.class);
    private final Queue<TangoEventListenerWeakReference<T>> listeners = new ConcurrentLinkedQueue<>();

    public void addListener(TangoEventListener<T> listener) {
        boolean isAdded = listeners.add(new TangoEventListenerWeakReference<>(listener));
        logger.debug("Listener {} has been added: {}", listener, isAdded);
    }

    public void removeListener(TangoEventListener<T> listener) {
        boolean isRemoved = listeners.remove(new TangoEventListenerWeakReference<>(listener));
        logger.debug("Listener {} has been removed: {}", listener, isRemoved);
    }

    @Override
    public void change(TangoChangeEvent e) {
        try {
            dispatch(e.getValue());
        } catch (DevFailed devFailed) {
            handleError(TangoUtils.convertDevFailedToException(devFailed));
        } catch (Exception ex) {
            handleError(ex);
        }
    }

    private void dispatch(DeviceAttribute deviceAttribute) {
        try {
            if (deviceAttribute.hasFailed()) {
                throw new DevFailed(deviceAttribute.getErrStack());
            }
            TangoDataWrapper data = TangoDataWrapper.create(deviceAttribute, null);//TODO extract TangoAttributeInfoWrapper.create
            TangoDataFormat<T> format = TangoDataFormat.createForAttrDataFormat(deviceAttribute.getDataFormat());
            EventData<T> result = new EventData<>(format.extract(data), deviceAttribute.getTimeValMillisSec(), deviceAttribute);
            for (Iterator<TangoEventListenerWeakReference<T>> iterator = listeners.iterator(); iterator.hasNext(); ) {
                WeakReference<TangoEventListener<T>> weakRef = iterator.next();
                TangoEventListener<T> listener = weakRef.get();
                if (listener != null) {
                    listener.onEvent(result);
                } else {
                    iterator.remove();
                }
            }
        } catch (Exception throwable) {
            handleError(throwable);
        }
    }

    private void handleError(Exception error) {
        for (Iterator<TangoEventListenerWeakReference<T>> iterator = listeners.iterator(); iterator.hasNext(); ) {
            WeakReference<TangoEventListener<T>> weakRef = iterator.next();
            TangoEventListener<T> listener = weakRef.get();
            if (listener != null) {
                listener.onError(error);
            } else {
                iterator.remove();
            }
        }
    }

    @Override
    public void periodic(TangoPeriodicEvent e) {
        try {
            dispatch(e.getValue());
        } catch (DevFailed devFailed) {
            handleError(TangoUtils.convertDevFailedToException(devFailed));
        } catch (Exception ex) {
            handleError(ex);
        }
    }

    @Override
    public void archive(TangoArchiveEvent e) {
        try {
            dispatch(e.getValue());
        } catch (DevFailed devFailed) {
            handleError(TangoUtils.convertDevFailedToException(devFailed));
        } catch (Exception ex) {
            handleError(ex);
        }
    }

    @Override
    public void user(TangoUserEvent e) {
        try {
            dispatch(e.getValue());
        } catch (DevFailed devFailed) {
            handleError(TangoUtils.convertDevFailedToException(devFailed));
        } catch (Exception ex) {
            handleError(ex);
        }
    }

    private static class TangoEventListenerWeakReference<T> extends WeakReference<TangoEventListener<T>> {
        private final TangoEventListener<T> referent;

        public TangoEventListenerWeakReference(TangoEventListener<T> referent) {
            super(referent);
            this.referent = referent;
        }

        @Override
        public int hashCode() {
            return referent.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (TangoEventListenerWeakReference.class.isAssignableFrom(obj.getClass())) {
                return referent.equals(((TangoEventListenerWeakReference) obj).referent);
            } else if (TangoEventListener.class.isAssignableFrom(obj.getClass())) {
                return referent.equals(obj);
            }
            return false;
        }
    }
}
