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
package org.eclipse.gef.editparts;

import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.EditPart;

/**
 * Responsible for locating <i>layers</i> in a <code>GraphicalViewer</code>.
 * Layers are just transparent {@link org.eclipse.draw2d.Figure figures}.
 * <P>
 * Typically, the <code>RootEditPart</code> will register() itself as the
 * LayerManager for the GraphicalViewer. All other EditParts/EditPolicies
 * looking for a layer use the Viewer's
 * {@link org.eclipse.gef.EditPartViewer#getEditPartRegistry() editPartRegsitry}
 * to find the <code>LayerManager</code>.
 * 
 * @author hudsonr
 * @since 2.0
 */
public interface LayerManager {

    /**
     * This key used to register the LayerManager in the Viewer's
     * EditPartRegistry.
     */
    Object ID = new Object();

    /**
     * Returns a specified layer.
     * 
     * @param key
     *            a key identifying the layer
     * @return the specified layer
     */
    IFigure getLayer(Object key);

    /**
     * A static helper
     * 
     * @since 2.0
     */
    class Helper {
        /**
         * Finds the LayerManager given any EditPart in the Viewer.
         * 
         * @param part
         *            any EditPart in a GraphicalViewer
         * @return the <code>LayerManager</code>
         */
        public static LayerManager find(EditPart part) {
            return (LayerManager) part.getViewer().getEditPartRegistry()
                    .get(ID);
        }
    }

}
