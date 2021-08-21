/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts;

/**
 * @author Ian Bull
 */
public interface LayoutStyles {

    /** Default layout style constant. */
    public final static int NONE = 0x00;

    /** 
     * Layout constant indicating that the layout algorithm 
     * should NOT resize any of the nodes.
     */
    public final static int NO_LAYOUT_NODE_RESIZING = 0x01;

    /**
     * Some layouts may prefer to expand their bounds beyond those of the requested bounds. This
     * flag asks the layout not to do so.
     */
    public static final int ENFORCE_BOUNDS = 0X02;

}
