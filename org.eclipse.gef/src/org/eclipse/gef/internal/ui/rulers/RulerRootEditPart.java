/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.rulers;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RangeModel;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.SimpleRootEditPart;
import org.eclipse.gef.editparts.ViewportAutoexposeHelper;

/**
 * RootEditPart for a ruler.
 * 
 * @author Pratik Shah
 * @since 3.0
 */
public class RulerRootEditPart extends SimpleRootEditPart {

    private static final Insets VERTICAL_THRESHOLD = new Insets(18, 0, 18, 0);
    private static final Insets HORIZONTAL_THRESHOLD = new Insets(0, 18, 0, 18);

    private boolean horizontal;

    /**
     * Constructor
     * 
     * @param isHorzontal
     *            whether or not the corresponding model ruler is horizontal
     */
    public RulerRootEditPart(boolean isHorzontal) {
        super();
        horizontal = isHorzontal;
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#addChildVisual(org.eclipse.gef.EditPart,
     *      int)
     */
    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
        getViewport().setContents(child);
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    @Override
    protected IFigure createFigure() {
        return new RulerViewport();
    }

    /**
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == AutoexposeHelper.class) {
            if (((RulerEditPart) getContents()).isHorizontal())
                return new ViewportAutoexposeHelper(this, HORIZONTAL_THRESHOLD);
            return new ViewportAutoexposeHelper(this, VERTICAL_THRESHOLD);
        }
        return super.getAdapter(adapter);
    }

    /**
     * Convenience method to get to the viewport
     * 
     * @return the figure cast as a viewport
     */
    protected Viewport getViewport() {
        return (Viewport) getFigure();
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#removeChildVisual(org.eclipse.gef.EditPart)
     */
    @Override
    protected void removeChildVisual(EditPart childEditPart) {
        getViewport().setContents(null);
    }

    /**
     * A RulerViewport shares a RangeModel with that of the primary
     * GraphicalViewer. The shared RangeModel is set on this viewport externally
     * by a client (in this case, RulerComposite).
     * 
     * @author Pratik Shah
     * @since 3.0
     */
    public class RulerViewport extends Viewport {
        /**
         * Constructor
         */
        public RulerViewport() {
            super(true);
            setLayoutManager(null);
            // The range model that's not shared is initialized such that it
            // can't scroll
            // anymore (otherwise, CTRL + SHIFT + ARROW scrolls it).
            RangeModel bogusRangeModel;
            if (horizontal)
                bogusRangeModel = getVerticalRangeModel();
            else
                bogusRangeModel = getHorizontalRangeModel();
            bogusRangeModel.setMinimum(0);
            bogusRangeModel.setMaximum(100);
            bogusRangeModel.setValue(0);
            bogusRangeModel.setExtent(100);
        }

        /**
         * This is the method that does the actual layout. We don't want a
         * layout to occur when layout() is invoked, rather when something
         * changes in the shared RangeModel.
         * 
         * @param force
         *            if <code>true</code>, the contents will be resized and
         *            revalidated; otherwise, just a repaint will occur.
         */
        protected void doLayout(boolean force) {
            repaint();
            /*
             * @TODO:Pratik It seems you don't really need this force argument.
             * Those that invoke doLayout(false) can invoke repaint()
             * themselves. doLayout() should just layout.
             */
            if (force) {
                RangeModel rModel;
                if (horizontal)
                    rModel = getHorizontalRangeModel();
                else
                    rModel = getVerticalRangeModel();
                Rectangle contentBounds = Rectangle.SINGLETON;
                if (horizontal) {
                    contentBounds.y = 0;
                    contentBounds.x = rModel.getMinimum();
                    contentBounds.height = this.getContents()
                            .getPreferredSize().height;
                    contentBounds.width = rModel.getMaximum()
                            - rModel.getMinimum();
                } else {
                    contentBounds.y = rModel.getMinimum();
                    contentBounds.x = 0;
                    contentBounds.height = rModel.getMaximum()
                            - rModel.getMinimum();
                    contentBounds.width = this.getContents().getPreferredSize().width;
                }
                if (!this.getContents().getBounds().equals(contentBounds)) {
                    this.getContents().setBounds(contentBounds);
                    this.getContents().revalidate();
                }
            }
        }

        /**
         * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
         */
        @Override
        public Dimension getPreferredSize(int wHint, int hHint) {
            if (this.getContents() == null)
                return new Dimension();
            Dimension pSize = this.getContents().getPreferredSize(wHint, hHint);
            if (horizontal) {
                RangeModel rModel = getHorizontalRangeModel();
                pSize.width = rModel.getMaximum() - rModel.getMinimum();
            } else {
                RangeModel rModel = getVerticalRangeModel();
                pSize.height = rModel.getMaximum() - rModel.getMinimum();
            }
            return pSize
                    .expand(getInsets().getWidth(), getInsets().getHeight());
        }

        /**
         * Since the RangeModel is shared with that of the editor's, a
         * RulerViewport should not adjust it.
         * 
         * @see org.eclipse.draw2d.Viewport#readjustScrollBars()
         */
        @Override
        protected void readjustScrollBars() {
        }

        /**
         * @see org.eclipse.draw2d.Figure#paintBorder(org.eclipse.draw2d.Graphics)
         */
        @Override
        protected void paintBorder(Graphics graphics) {
            super.paintBorder(graphics);
            if (this.getContents() != null
                    && ((RulerFigure) this.getContents()).getDrawFocus()) {
                Rectangle focusBounds = getBounds().getCopy();
                if (((RulerFigure) this.getContents()).isHorizontal()) {
                    focusBounds.resize(-2, -4);
                    focusBounds.x++;
                } else {
                    focusBounds.resize(-4, -2);
                    focusBounds.y++;
                }
                graphics.setForegroundColor(ColorConstants.black);
                graphics.setBackgroundColor(ColorConstants.white);
                graphics.drawFocus(focusBounds);
            }
        }

        /**
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if (this.getContents() != null
                    && event.getSource() instanceof RangeModel) {
                String property = event.getPropertyName();
                doLayout(RangeModel.PROPERTY_MAXIMUM.equals(property)
                        || RangeModel.PROPERTY_MINIMUM.equals(property)
                        || RangeModel.PROPERTY_VALUE.equals(property));
            }
        }

        /**
         * @see org.eclipse.draw2d.Viewport#setContents(org.eclipse.draw2d.IFigure)
         */
        @Override
        public void setContents(IFigure figure) {
            super.setContents(figure);
            // Need to layout when contents change
            if (this.getContents() != null)
                doLayout(true);
        }

        /**
         * RulerViewport uses local coordinates.
         * 
         * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
         */
        @Override
        protected boolean useLocalCoordinates() {
            return true;
        }
    }

}
