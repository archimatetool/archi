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

import java.util.List;

/**
 * When a layout completes an iteration, it throws this event
 * to allow the application to update.  For example, at the
 * end of an iteration is can be assumed the layout has placed
 * each entity into a new location.  This event allows the application
 * to update the GUI to represent the new locations
 * 
 * @author Casey Best and Rob Lintern
 */
@SuppressWarnings("rawtypes")
public class LayoutIterationEvent {
    private List relationshipsToLayout, entitiesToLayout;
    private int iterationCompleted;

    /**
     * Return the relationships used in this layout.
     */
    public List getRelationshipsToLayout() {
        return relationshipsToLayout;
    }

    /**
     * Return the entities used in this layout. 
     */
    public List getEntitiesToLayout() {
        return entitiesToLayout;
    }

    /**
     * Return the iteration of the layout algorithm that was
     * just completed. 
     */
    public int getIterationCompleted() {
        return iterationCompleted;
    }
}
