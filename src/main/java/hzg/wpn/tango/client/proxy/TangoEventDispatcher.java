package hzg.wpn.tango.client.proxy;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.TangoApi.events.ITangoChangeListener;
import fr.esrf.TangoApi.events.ITangoPeriodicListener;
import fr.esrf.TangoApi.events.TangoChangeEvent;
import fr.esrf.TangoApi.events.TangoPeriodicEvent;
import hzg.wpn.tango.client.data.TangoDataWrapper;
import hzg.wpn.tango.client.data.format.TangoDataFormat;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Vector;

/**
 * This class is a common implementation for all ITangoXXXListener.
 * <p/>
 * Class contains its own listeners cache. Each listener is wrapped with WeakReference and therefore user must keep references to its callbacks.
 *
 * @author ingvord
 * @since 5/26/14@1:20 AM
 */
public class TangoEventDispatcher<T> implements ITangoChangeListener, ITangoPeriodicListener { //TODO implement others
    private final Vector<WeakReference<TangoEventListener<T>>> listeners = new Vector<>();

    public void addListener(TangoEventListener<T> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void change(TangoChangeEvent e) {
        try {
            dispatch(e.getValue());
        } catch (DevFailed devFailed) {
            handleError(devFailed);
        }
    }

    private void dispatch(DeviceAttribute deviceAttribute) {
        try {
            if (deviceAttribute.hasFailed()) {
                throw new DevFailed(deviceAttribute.getErrStack());
            }
            TangoDataWrapper data = TangoDataWrapper.create(deviceAttribute);
            TangoDataFormat<T> format = TangoDataFormat.createForAttrDataFormat(deviceAttribute.getDataFormat());
            EventData<T> result = new EventData<>(format.extract(data), deviceAttribute.getTimeValMillisSec());
            for (Iterator<WeakReference<TangoEventListener<T>>> iterator = listeners.iterator(); iterator.hasNext(); ) {
                WeakReference<TangoEventListener<T>> weakRef = iterator.next();
                TangoEventListener<T> listener = weakRef.get();
                if (listener != null) {
                    listener.onEvent(result);
                } else {
                    iterator.remove();
                }
            }
        } catch (Throwable throwable) {
            handleError(throwable);
        }
    }

    private void handleError(Throwable error) {
        for (Iterator<WeakReference<TangoEventListener<T>>> iterator = listeners.iterator(); iterator.hasNext(); ) {
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
            handleError(devFailed);
        }
    }
}
