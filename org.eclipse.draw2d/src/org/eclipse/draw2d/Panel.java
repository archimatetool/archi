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
package org.eclipse.draw2d;

/**
 * A General purpose Container. This figure is opaque by default, and will fill
 * its entire bounds with either the background color that is set on the figure,
 * or the IGraphics current background color if none has been set. Opaque
 * figures help to optimize painting.
 * <p>
 * Note that the paintFigure() method in the superclass Figure actually fills
 * the bounds of this figure.
 */
public class Panel extends Figure {

    /**
     * Returns <code>true</code> as this is an opaque figure.
     * 
     * @return the opaque state of this figure
     * @since 2.0
     */
    @Override
    public boolean isOpaque() {
        return true;
    }

}
