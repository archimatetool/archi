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
package org.eclipse.draw2d;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A {@link ScrollPane} with transparent {@link ScrollBar}s.
 * 
 * @author Alexander Nyssen
 * @author Philip Ritzkopf
 * 
 * @since 3.6
 */
public class PuristicScrollPane extends ScrollPane {

    /**
     * A {@link ScrollBar} with no thumb and non-opaque buttons.
     * 
     * @author Alexander Nyssen
     * @author Philip Ritzkopf
     */
    public class PuristicScrollBar extends ScrollBar {

        /**
         * Instantiates a new transparent scroll bar.
         * 
         * @param isHorizontal
         *            whether this scroll bar is used as a horizontal one.
         */
        public PuristicScrollBar(boolean isHorizontal) {
            super();
            setHorizontal(isHorizontal);
            setOpaque(false);
        }

        /**
         * @see org.eclipse.draw2d.ScrollBar#createDefaultDownButton()
         */
        @Override
        protected Clickable createDefaultDownButton() {
            Clickable buttonDown = super.createDefaultDownButton();
            buttonDown.setBorder(null);
            buttonDown.setOpaque(false);
            return buttonDown;
        }

        /**
         * @see org.eclipse.draw2d.ScrollBar#createDefaultThumb()
         */
        @Override
        protected IFigure createDefaultThumb() {
            return null;
        }

        /**
         * @see org.eclipse.draw2d.ScrollBar#createDefaultUpButton()
         */
        @Override
        protected Clickable createDefaultUpButton() {
            Clickable buttonUp = super.createDefaultUpButton();
            buttonUp.setBorder(null);
            buttonUp.setOpaque(false);
            return buttonUp;
        }

        /**
         * @see org.eclipse.draw2d.ScrollBar#createPageDown()
         */
        @Override
        protected Clickable createPageDown() {
            return null;
        }

        /**
         * @see org.eclipse.draw2d.ScrollBar#createPageUp()
         */
        @Override
        protected Clickable createPageUp() {
            return null;
        }

        /**
         * @see PropertyChangeListener#propertyChange(java.beans.
         *      PropertyChangeEvent )
         */
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if (event.getSource() instanceof RangeModel) {
                getButtonDown().setVisible(
                        getValue() != getMaximum() - getExtent());
                getButtonUp().setVisible(getValue() != getMinimum());
            }
            super.propertyChange(event);
        }

    }

    public PuristicScrollPane() {
        // layout to ensure, viewport gets complete client area
        setLayoutManager(new ScrollPaneLayout() {
            @Override
            public void layout(IFigure parent) {
                // scroll panes are layouted normally
                super.layout(parent);
                // viewport gets complete client area
                ScrollPane scrollpane = (ScrollPane) parent;
                Viewport viewport = scrollpane.getViewport();
                viewport.setBounds(parent.getClientArea());
            }
        });
    }

    /**
     * @see org.eclipse.draw2d.ScrollPane#createVerticalScrollBar()
     */
    @Override
    protected void createVerticalScrollBar() {
        PuristicScrollBar verticalScrollBar = new PuristicScrollBar(false);
        setVerticalScrollBar(verticalScrollBar);
    }

    /**
     * @see org.eclipse.draw2d.ScrollPane#createHorizontalScrollBar()
     */
    @Override
    protected void createHorizontalScrollBar() {
        PuristicScrollBar horizontalScrollBar = new PuristicScrollBar(true);
        setHorizontalScrollBar(horizontalScrollBar);
    }

}