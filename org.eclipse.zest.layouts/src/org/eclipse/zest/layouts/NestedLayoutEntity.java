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
 * Extends LayoutEntity to provide methods for dealing with nested entities.
 * 
 * @author Chris Callendar
 */
@SuppressWarnings("rawtypes")
public interface NestedLayoutEntity extends LayoutEntity {

    /** Returns the parent entity. */
    NestedLayoutEntity getParent();

    /** Returns the list of children. */
    List getChildren();

    /** Returns true if this entity has children. */
    boolean hasChildren();

}
