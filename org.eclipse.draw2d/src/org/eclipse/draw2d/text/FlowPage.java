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

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * The root of a Flow hierarchy. A flow page can be treated as a normal figure,
 * but contains FlowFigures.
 * <P>
 * A FlowPage will not have a defined width unless it is inside a figure whose
 * layout provides width hints when calling
 * {@link org.eclipse.draw2d.IFigure#getPreferredSize(int, int)}.
 * 
 * <P>
 * WARNING: This class is not intended to be subclassed by clients.
 */
@SuppressWarnings("rawtypes")
public class FlowPage extends BlockFlow {

    private Dimension pageSize = new Dimension();
    private int recommendedWidth;
    private int pageSizeCacheKeys[] = new int[3];
    private Dimension pageSizeCacheValues[] = new Dimension[3];

    /**
     * @see org.eclipse.draw2d.Figure#addNotify()
     */
    @Override
    public void addNotify() {
        super.addNotify();
        setValid(false);
    }

    /**
     * @see org.eclipse.draw2d.text.BlockFlow#createDefaultFlowLayout()
     */
    @Override
    protected FlowFigureLayout createDefaultFlowLayout() {
        return new PageFlowLayout(this);
    }

    /**
     * @see org.eclipse.draw2d.Figure#getMinimumSize(int, int)
     */
    @Override
    public Dimension getMinimumSize(int w, int h) {
        return getPreferredSize(w, h);
    }

    /**
     * @see org.eclipse.draw2d.Figure#invalidate()
     */
    @Override
    public void invalidate() {
        pageSizeCacheValues = new Dimension[3];
        super.invalidate();
    }

    /**
     * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
     */
    @Override
    public Dimension getPreferredSize(int width, int h) {
        for (int i = 0; i < 3; i++) {
            if (pageSizeCacheKeys[i] == width && pageSizeCacheValues[i] != null)
                return pageSizeCacheValues[i];
        }

        pageSizeCacheKeys[2] = pageSizeCacheKeys[1];
        pageSizeCacheKeys[1] = pageSizeCacheKeys[0];
        pageSizeCacheKeys[0] = width;

        pageSizeCacheValues[2] = pageSizeCacheValues[1];
        pageSizeCacheValues[1] = pageSizeCacheValues[0];

        // Flowpage must temporarily layout to determine its preferred size
        int oldWidth = getPageWidth();
        setPageWidth(width);
        validate();
        pageSizeCacheValues[0] = pageSize.getCopy();

        if (width != oldWidth) {
            setPageWidth(oldWidth);
            getUpdateManager().addInvalidFigure(this);
        }
        return pageSizeCacheValues[0];
    }

    int getPageWidth() {
        return recommendedWidth;
    }

    /**
     * @see BlockFlow#postValidate()
     */
    @Override
    public void postValidate() {
        Rectangle r = getBlockBox().toRectangle();
        pageSize.width = r.width;
        pageSize.height = r.height;
        List v = getChildren();
        for (int i = 0; i < v.size(); i++)
            ((FlowFigure) v.get(i)).postValidate();
    }

    /**
     * Overridden to set valid.
     * 
     * @see org.eclipse.draw2d.IFigure#removeNotify()
     */
    @Override
    public void removeNotify() {
        super.removeNotify();
        setValid(true);
    }

    /**
     * @see FlowFigure#setBounds(Rectangle)
     */
    @Override
    public void setBounds(Rectangle r) {
        if (getBounds().equals(r))
            return;
        boolean invalidate = getBounds().width != r.width
                || getBounds().height != r.height;
        super.setBounds(r);
        int newWidth = r.width;
        if (invalidate || getPageWidth() != newWidth) {
            setPageWidth(newWidth);
            getUpdateManager().addInvalidFigure(this);
        }
    }

    private void setPageWidth(int width) {
        if (recommendedWidth == width)
            return;
        recommendedWidth = width;
        super.invalidate();
    }

    /**
     * @see org.eclipse.draw2d.Figure#validate()
     */
    @Override
    public void validate() {
        if (isValid())
            return;
        super.validate();
        postValidate();
    }

}
