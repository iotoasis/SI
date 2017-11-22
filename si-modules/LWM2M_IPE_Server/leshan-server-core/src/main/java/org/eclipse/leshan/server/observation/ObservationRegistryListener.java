/*******************************************************************************
 * Copyright (c) 2013-2015 Sierra Wireless and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.leshan.server.observation;

import java.util.List;

import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.core.node.TimestampedLwM2mNode;
import org.eclipse.leshan.core.observation.Observation;

public interface ObservationRegistryListener {
    void newObservation(Observation observation);

    void cancelled(Observation observation);

    /**
     * Called on new notification.
     * 
     * @param observation the observation for which new data are received
     * @param mostRecentValue the most recent value
     * @param timestampedValues the list of time-stamped value (could be null)
     */
    void newValue(Observation observation, LwM2mNode mostRecentValue, List<TimestampedLwM2mNode> timestampedValues);
}
