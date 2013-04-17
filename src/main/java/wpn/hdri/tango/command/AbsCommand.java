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

package wpn.hdri.tango.command;

import com.google.common.base.Throwables;
import fr.esrf.Tango.DevError;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoApi.DeviceData;
import fr.esrf.TangoDs.Command;
import fr.esrf.TangoDs.DeviceImpl;
import org.apache.log4j.Logger;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_OPERATION;
import wpn.hdri.tango.data.TangoDataWrapper;
import wpn.hdri.tango.data.type.TangoDataType;
import wpn.hdri.tango.data.type.ValueExtractionException;
import wpn.hdri.tango.util.TangoUtils;

/**
 * This class is designed for inheritance.
 * <p/>
 * This class extends {@link Command}, implements {@link Command#execute(fr.esrf.TangoDs.DeviceImpl, org.omg.CORBA.Any)}
 * and provides abstract {@link this#executeInternal(V, I, org.apache.log4j.Logger)} method.
 * <p/>
 * This class simplifies usage of {@link Command} by encapsulating conversion logic (Any <-> actual type). So clients of
 * this class does not care about it any longer.
 * <p/>
 * Here is an usage example:
 * <pre>
 *     {@code
 *     //define custom command logic
 *     //MyCommand.java
 *     public class MyCommand extends AbsCommand<MyTangoServer,String,String> {
 *         public MyCommand(){
 *             super("myCommand",TangoDataTypes.STRING,TangoDataTypes.STRING,"your name","greeting");
 *         }
 *         protected String executeInternal(MyTangoServer instance, String in, Logger log){
 *             return "Hello, " + in;
 *         }
 *     }
 *     //now add new command to tango
 *     //MyTangoServerClass.java
 *     ...
 *     public void command_factory() {
 *         command_list.add(new MyCommand().toCommand());
 *     }
 *     ...
 *     }
 * </pre>
 *
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 14.05.12
 */
public abstract class AbsCommand<V extends DeviceImpl, I, O> extends Command {
    private final TangoDataType<I> typeIn;
    private final TangoDataType<O> typeOut;

    /**
     * @param name     a name of the command
     * @param in       type of the input arg
     * @param out      type of the out arg
     * @param in_desc  input description
     * @param out_desc output description
     */
    public AbsCommand(String name, TangoDataType<I> in, TangoDataType<O> out, String in_desc, String out_desc) {
        super(name, in.getAlias(), out.getAlias(), in_desc, out_desc);
        this.typeIn = in;
        this.typeOut = out;
    }

    /**
     * Casts dev to V, extracts value of appropriate type (I) from Any, passes it into {@link AbsCommand#executeInternal(fr.esrf.TangoDs.DeviceImpl, Object, org.apache.log4j.Logger)}
     * receives output value of type O, converts it to Any and returns.
     *
     * @param dev     DeviceImpl
     * @param data_in input
     * @return output
     * @throws DevFailed
     */
    @Override
    public final Any execute(DeviceImpl dev, Any data_in) throws DevFailed {
        V device = (V) dev;
        Logger log = device.get_logger();
        try {
            I input = convertAnyToInput(data_in);
            //TODO convert input into complex type: e.g. User
            O output = executeInternal(device, input, log);
            Any result = convertOutputToAny(output);
            return result;
        } catch (BAD_OPERATION bad_operation) {
            log.error(bad_operation);
            throw bad_operation;
        } catch (DevFailed devFailed) {
            log.error(get_name() + " command execution failed. DevFailed exception caught:", TangoUtils.convertDevFailedToException(devFailed));
            throw devFailed;
        } catch (Throwable e) {
            log.error(get_name() + " command execution failed. General exception caught:", e);
            throw new DevFailed(new DevError[]{
                    new DevError(e.toString(), ErrSeverity.ERR, Throwables.getStackTraceAsString(e), get_name())
            });
        }
    }

    private Any convertOutputToAny(O result) throws DevFailed {
        try {
            DeviceData data = new DeviceData();
            TangoDataWrapper dataWrapper = TangoDataWrapper.create(data);
            this.typeOut.insert(dataWrapper, result);
            return data.extractAny();
        } catch (Throwable e) {
            throw new DevFailed(new DevError[]{
                    new DevError(e.toString(), ErrSeverity.ERR, Throwables.getStackTraceAsString(e), get_name())
            });
        }
    }

    private I convertAnyToInput(Any any) throws DevFailed, ValueExtractionException {
        DeviceData data = new DeviceData(any);
        TangoDataWrapper dataWrapper = TangoDataWrapper.create(data);
        return this.typeIn.extract(dataWrapper);
    }

    /**
     * Performs command main logic.
     *
     * @param instance tango server
     * @param data     command input
     * @param log      centralized logger
     * @return command output
     * @throws DevFailed
     */
    protected abstract O executeInternal(V instance, I data, Logger log) throws DevFailed;
}
