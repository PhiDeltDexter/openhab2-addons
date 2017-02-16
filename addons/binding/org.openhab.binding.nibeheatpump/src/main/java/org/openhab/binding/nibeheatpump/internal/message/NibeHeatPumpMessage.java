/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nibeheatpump.internal.message;

import org.openhab.binding.nibeheatpump.internal.NibeHeatPumpException;

public interface NibeHeatPumpMessage {

    /**
     * Procedure for present class information in string format. Used for
     * logging purposes.
     *
     */
    @Override
    String toString();

    /**
     * Procedure for encode raw data.
     *
     * @param data
     *            Raw data.
     */
    void encodeMessage(byte[] data) throws NibeHeatPumpException;

    /**
     * Procedure for decode object to raw data.
     *
     * @return raw data.
     */
    byte[] decodeMessage();

    /**
     * Procedure to covert message to hex string format. Used for
     * logging purposes.
     *
     */
    String toHexString();
}
