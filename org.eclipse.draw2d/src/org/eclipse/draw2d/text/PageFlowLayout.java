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
package org.eclipse.draw2d.text;

/**
 * A block layout which requires no FlowContext to perform its layout. This
 * class is used by {@link FlowPage}.
 * <p>
 * WARNING: This class is not intended to be subclassed by clients.
 */
public class PageFlowLayout extends BlockFlowLayout {

    /**
     * Creates a new PageFlowLayout with the given FlowPage
     * 
     * @param page
     *            the FlowPage
     */
    public PageFlowLayout(FlowPage page) {
        super(page);
    }

    /**
     * @see org.eclipse.draw2d.text.BlockFlowLayout#getContextWidth()
     */
    @Override
    int getContextWidth() {
        return ((FlowPage) getFlowFigure()).getPageWidth();
    }

}
