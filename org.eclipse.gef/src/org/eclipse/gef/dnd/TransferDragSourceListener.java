/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.dnd;

import org.eclipse.swt.dnd.Transfer;

/**
 * A DragSourceListener that can handle one type of SWT {@link Transfer}. The
 * purpose of a TransferDragSourceListener is to:
 * <UL>
 * <LI>Determine enablement for a Drag operation. Enablement is often a function
 * of the current <I>Selection</I> and/or other criteria.
 * <LI>Set data for a single type of Drag and Transfer.
 * </UL> {@link DelegatingDragAdapter} allows these functions to be implemented
 * separately for unrelated types of Drags. DelegatingDragAdapter then combines
 * the function of each TransferDragSourceListener, while allowing them to be
 * implemented as if they were the only DragSourceListener.
 * 
 * @deprecated use org.eclipse.jface.util.TransferDragSourceListener instead
 */
public interface TransferDragSourceListener extends
        org.eclipse.jface.util.TransferDragSourceListener {

}
