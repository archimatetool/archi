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
import org.eclipse.draw2d.Viewport;

import org.eclipse.gef.GraphicalEditPart;

/**
 * @author hudsonr
 */
abstract class ViewportHelper {

    protected GraphicalEditPart owner;

    protected ViewportHelper(GraphicalEditPart owner) {
        this.owner = owner;
    }

    protected Viewport findViewport(GraphicalEditPart part) {
        IFigure figure = null;
        Viewport port = null;
        do {
            if (figure == null)
                figure = part.getContentPane();
            else
                figure = figure.getParent();
            if (figure instanceof Viewport) {
                port = (Viewport) figure;
                break;
            }
        } while (figure != part.getFigure() && figure != null);
        return port;
    }

}
