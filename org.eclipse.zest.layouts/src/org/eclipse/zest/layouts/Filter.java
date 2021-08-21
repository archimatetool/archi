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
 * A filter is used to filter objects.  Once implemented, interested
 * parties can ask this filter whether or not a specific object
 * is filtered.
 * 
 * For example, in a visualization tool, only unfiltered objects should
 * be displayed.  Before displaying an object, the display can ask
 * this filter if the object is filtered.
 * 
 * @author Casey Best
 */
public interface Filter {

    /**
     * Returns true if the object is filtered, or false if it's not filtered.
     */
    public boolean isObjectFiltered(LayoutItem object);
}
