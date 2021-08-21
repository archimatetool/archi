/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.core.widgets.internal;

import org.eclipse.swt.widgets.Control;

/**
 * 
 * A Listener to indicate when a view has become visible.
 * @author Ian Bull
 *
 */
public interface RevealListener {

    public void revealed(Control c);

}
