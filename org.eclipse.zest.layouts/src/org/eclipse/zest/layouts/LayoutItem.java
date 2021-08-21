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
 * Super interface for both Layout Entities and Layout Relationships
 *  
 * @author Ian Bull
 *
 */
public interface LayoutItem {

    public void setGraphData(Object o);

    public Object getGraphData();

}
