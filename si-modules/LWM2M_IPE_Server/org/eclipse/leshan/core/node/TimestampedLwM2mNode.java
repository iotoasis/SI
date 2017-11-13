/*******************************************************************************
 * Copyright (c) 2016 Sierra Wireless and others.
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
package org.eclipse.leshan.core.node;

import org.eclipse.leshan.util.Validate;

public class TimestampedLwM2mNode {

    private final Long timestamp;

    private final LwM2mNode node;

    public TimestampedLwM2mNode(Long timestamp, LwM2mNode node) {
        Validate.notNull(node);
        this.timestamp = timestamp;
        this.node = node;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public LwM2mNode getNode() {
        return node;
    }

    public boolean isTimespamped() {
        return timestamp != null && timestamp >= 0;
    }
}
