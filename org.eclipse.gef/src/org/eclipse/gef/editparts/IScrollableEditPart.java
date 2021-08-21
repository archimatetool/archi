/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Research Group Software Construction,
 *     RWTH Aachen University, Germany - initial API and implementation
 */
package org.eclipse.gef.editparts;

import org.eclipse.draw2d.IScrollableFigure;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.ScrollableSelectionFeedbackEditPolicy;

/**
 * Should be implemented by EditParts, to which an
 * {@link ScrollableSelectionFeedbackEditPolicy} is to be registered, as it
 * grants the edit policy type-safe access to the edit part's
 * {@link IScrollableFigure}.
 * 
 * @author Alexander Nyssen
 * @author Philip Ritzkopf
 * 
 * @since 3.6
 */
public interface IScrollableEditPart extends GraphicalEditPart {

    /**
     * Offers type-safe access to the GraphicalEditPart's figure.
     * 
     * @return The figure of this edit part, which has to be an
     *         IScrollableFigure
     */
    IScrollableFigure getScrollableFigure();

}
