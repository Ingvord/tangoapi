/*
 * The main contributor to this project is Institute of Materials Research,
 * Helmholtz-Zentrum Geesthacht,
 * Germany.
 *
 * This project is a contribution of the Helmholtz Association Centres and
 * Technische Universitaet Muenchen to the ESS Design Update Phase.
 *
 * The project's funding reference is FKZ05E11CG1.
 *
 * Copyright (c) 2012. Institute of Materials Research,
 * Helmholtz-Zentrum Geesthacht,
 * Germany.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */

package wpn.hdri.tango.proxy;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.CallBack;
import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.TangoApi.DeviceProxy;
import fr.esrf.TangoApi.events.ITangoChangeListener;
import fr.esrf.TangoApi.events.TangoChange;
import fr.esrf.TangoApi.events.TangoChangeEvent;
import fr.esrf.TangoDs.TangoConst;
import wpn.hdri.tango.data.TangoDataWrapper;
import wpn.hdri.tango.data.format.TangoDataFormat;
import wpn.hdri.tango.data.type.ValueExtractionException;

/**
 * This enum contains items corresponded to {@link TangoConst}.XXX_EVENT.
 * <p/>
 * Each item encapsulates subscription logic.
 * <p/>
 * Item of this enum is passed to {@link DeviceProxyWrapper#subscribeEvent(String, TangoEvent, TangoEventCallback)}
 *
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 07.06.12
 */
public enum TangoEvent {
    CHANGE(TangoConst.CHANGE_EVENT) {
        @Override
        <T> CallBack subscribe(DeviceProxy proxy, String attrName, String[] filters, final TangoEventCallback<T> callback) throws DevFailed {
            TangoChange result = new TangoChange(proxy, attrName, filters);
            result.addTangoChangeListener(new ITangoChangeListener() {
                @Override
                public void change(TangoChangeEvent event) {
                    try {
                        DeviceAttribute deviceAttribute = event.getValue();
                        if (deviceAttribute.hasFailed()) {
                            throw new DevFailed(deviceAttribute.getErrStack());
                        }
                        TangoDataWrapper data = TangoDataWrapper.create(deviceAttribute);
                        TangoDataFormat<T> format = TangoDataFormat.createForAttrDataFormat(deviceAttribute.getDataFormat());
                        EventData<T> result = new EventData<T>(format.extract(data), deviceAttribute.getTimeValMillisSec());
                        callback.onEvent(result);
                    } catch (DevFailed devFailed) {
                        callback.onError(devFailed);
                    } catch (ValueExtractionException e) {
                        callback.onError(e);
                    } catch (Throwable throwable) {
                        callback.onError(throwable);
                    }

                }
            }, true);
            return result;
        }
    };
    //TODO other events

    private final int alias;

    private TangoEvent(int alias) {
        this.alias = alias;
    }

    public int getAlias() {
        return alias;
    }

    abstract <T> CallBack subscribe(DeviceProxy proxy, String attrName, String[] filters, final TangoEventCallback<T> callback) throws DevFailed;
}
