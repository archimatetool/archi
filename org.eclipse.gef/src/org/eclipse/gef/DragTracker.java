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
package org.eclipse.gef;

/**
 * A specialization of Tool that is used by the
 * {@link org.eclipse.gef.tools.SelectionTool} during a Mouse Drag. The
 * <code>SelectionTool</code> obtains a <code>DragTracker</code> on mouse down,
 * and forwards all input to that tracker until after the mouse is released. The
 * SelectionTool also obtains DragTrackers in keyboard accessible ways.
 */
public interface DragTracker extends Tool {

    /**
     * The <code>SelectionTool</code> supports keyboard accessible drags. In
     * such scenarios it is up to the SelectionTool to interpret <i>commit</i>
     * and <i>abort</i> keystrokes. Since the DragTracker cannot do this, this
     * method is used to indicate that the User has committed the drag using the
     * keyboard. Abort is not handled specially, and the DragTracker should
     * peform the usual cleanup in its {@link Tool#deactivate()} method.
     */
    void commitDrag();

}
