/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link AstroBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Gerhard Riegler - Initial contribution
 */
public class AstroBindingConstants {

    public static final String BINDING_ID = "astro";

    public static final String SUN = "sun";
    public static final String MOON = "moon";
    public static final String LOCAL = "local";

    // things
    public static final ThingTypeUID THING_TYPE_SUN = new ThingTypeUID(BINDING_ID, SUN);
    public static final ThingTypeUID THING_TYPE_MOON = new ThingTypeUID(BINDING_ID, MOON);
}
