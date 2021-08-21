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
package org.eclipse.gef.editpolicies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.AccessibleHandleProvider;
import org.eclipse.gef.Handle;
import org.eclipse.gef.LayerConstants;

/**
 * A SelectionEditPolicy which manages a List of handles provided by the
 * subclass. Handles are Figures which are added to the HANDLE layer, and
 * generally return a DragTracker for dragging them. Handles are accessible for
 * keyboard use if they return an accessible location.
 * <P>
 * SelectionHandlesEditPolicy implements
 * {@link org.eclipse.core.runtime.IAdaptable} for accessibility support. If any
 * of the managed Handles provide accesible locations, then a
 * {@link org.eclipse.gef.AccessibleHandleProvider} is automatically created.
 * 
 * @since 2.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class SelectionHandlesEditPolicy extends SelectionEditPolicy
        implements IAdaptable {

    /**
     * the List of handles
     */
    protected List handles;

    /**
     * Adds the handles to the handle layer.
     */
    protected void addSelectionHandles() {
        removeSelectionHandles();
        IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);
        handles = createSelectionHandles();
        for (int i = 0; i < handles.size(); i++)
            layer.add((IFigure) handles.get(i));
    }

    /**
     * Subclasses must implement to provide the list of handles.
     * 
     * @return List of handles; cannot be <code>null</code>
     */
    protected abstract List createSelectionHandles();

    /**
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(Class)
     */
    @Override
    public Object getAdapter(Class key) {
        if (key == AccessibleHandleProvider.class)
            return new AccessibleHandleProvider() {
                @Override
                public List getAccessibleHandleLocations() {
                    List result = new ArrayList();
                    if (handles != null) {
                        for (int i = 0; i < handles.size(); i++) {
                            Point p = ((Handle) handles.get(i))
                                    .getAccessibleLocation();
                            if (p != null)
                                result.add(p);
                        }
                    }
                    return result;
                }
            };
        return null;
    }

    /**
     * Implemented to remove the handles.
     * 
     * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#hideSelection()
     */
    @Override
    protected void hideSelection() {
        removeSelectionHandles();
    }

    /**
     * removes the selection handles from the selection layer.
     */
    protected void removeSelectionHandles() {
        if (handles == null)
            return;
        IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);
        for (int i = 0; i < handles.size(); i++)
            layer.remove((IFigure) handles.get(i));
        handles = null;
    }

    /**
     * Implemented to add the selection handles
     * 
     * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#showSelection()
     */
    @Override
    protected void showSelection() {
        addSelectionHandles();
    }

}
