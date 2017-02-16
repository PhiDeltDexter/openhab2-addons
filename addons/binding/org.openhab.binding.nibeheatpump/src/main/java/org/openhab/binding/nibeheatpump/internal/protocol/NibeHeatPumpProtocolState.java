/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nibeheatpump.internal.protocol;

public interface NibeHeatPumpProtocolState {
    /**
     * @return true to keep processing, false to read more data.
     */
    boolean process(NibeHeatPumpProtocolContext context);
}
