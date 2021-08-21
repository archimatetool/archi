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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Transposer;

/**
 * Provides for the scrollbars used by the {@link ScrollPane}. A ScrollBar is
 * made up of five essential Figures: An 'Up' arrow button, a 'Down' arrow
 * button, a draggable 'Thumb', a 'Pageup' button, and a 'Pagedown' button.
 */
public class ScrollBar extends Figure implements Orientable,
        PropertyChangeListener {

    private static final int ORIENTATION_FLAG = Figure.MAX_FLAG << 1;
    /** @see Figure#MAX_FLAG */
    protected static final int MAX_FLAG = ORIENTATION_FLAG;

    private static final Color COLOR_TRACK = FigureUtilities.mixColors(
            ColorConstants.white, ColorConstants.button);

    private RangeModel rangeModel = null;
    private IFigure thumb;
    private Clickable pageUp, pageDown;
    private Clickable buttonUp, buttonDown;
    /**
     * Listens to mouse events on the scrollbar to take care of scrolling.
     */
    protected ThumbDragger thumbDragger = new ThumbDragger();

    private boolean isHorizontal = false;

    private int pageIncrement = 50;
    private int stepIncrement = 10;

    /**
     * Transposes from vertical to horizontal if needed.
     */
    protected final Transposer transposer = new Transposer();

    {
        setRangeModel(new DefaultRangeModel());
    }

    /**
     * Constructs a ScrollBar. ScrollBar orientation is vertical by default.
     * Call {@link #setHorizontal(boolean)} with <code>true</code> to set
     * horizontal orientation.
     * 
     * @since 2.0
     */
    public ScrollBar() {
        initialize();
    }

    /**
     * Creates the default 'Up' ArrowButton for the ScrollBar.
     * 
     * @return the up button
     * @since 2.0
     */
    protected Clickable createDefaultUpButton() {
        Button buttonUp = new ArrowButton();
        buttonUp.setBorder(new ButtonBorder(
                ButtonBorder.SCHEMES.BUTTON_SCROLLBAR));
        return buttonUp;
    }

    /**
     * Creates the default 'Down' ArrowButton for the ScrollBar.
     * 
     * @return the down button
     * @since 2.0
     */
    protected Clickable createDefaultDownButton() {
        Button buttonDown = new ArrowButton();
        buttonDown.setBorder(new ButtonBorder(
                ButtonBorder.SCHEMES.BUTTON_SCROLLBAR));
        return buttonDown;
    }

    /**
     * Creates the pagedown Figure for the Scrollbar.
     * 
     * @return the page down figure
     * @since 2.0
     */
    protected Clickable createPageDown() {
        return createPageUp();
    }

    /**
     * Creates the pageup Figure for the Scrollbar.
     * 
     * @return the page up figure
     * @since 2.0
     */
    protected Clickable createPageUp() {
        final Clickable clickable = new Clickable();
        clickable.setOpaque(true);
        clickable.setBackgroundColor(COLOR_TRACK);
        clickable.setRequestFocusEnabled(false);
        clickable.setFocusTraversable(false);
        clickable.addChangeListener(new ChangeListener() {
            @Override
            public void handleStateChanged(ChangeEvent evt) {
                if (clickable.getModel().isArmed())
                    clickable.setBackgroundColor(ColorConstants.black);
                else
                    clickable.setBackgroundColor(COLOR_TRACK);
            }
        });
        return clickable;
    }

    /**
     * Creates the Scrollbar's "thumb", the draggable Figure that indicates the
     * Scrollbar's position.
     * 
     * @return the thumb figure
     * @since 2.0
     */
    protected IFigure createDefaultThumb() {
        Panel thumb = new Panel();
        thumb.setMinimumSize(new Dimension(6, 6));
        thumb.setBackgroundColor(ColorConstants.button);

        thumb.setBorder(new SchemeBorder(SchemeBorder.SCHEMES.BUTTON_CONTRAST));
        return thumb;
    }

    /**
     * Returns the figure used as the up button.
     * 
     * @return the up button
     */
    protected IFigure getButtonUp() {
        // TODO: The set method takes a Clickable while the get method returns
        // an IFigure.
        // Change the get method to return Clickable (since that's what it's
        // typed as).
        return buttonUp;
    }

    /**
     * Returns the figure used as the down button.
     * 
     * @return the down button
     */
    protected IFigure getButtonDown() {
        // TODO: The set method takes a Clickable while the get method returns
        // an IFigure.
        // Change the get method to return Clickable (since that's what it's
        // typed as).
        return buttonDown;
    }

    /**
     * Returns the extent.
     * 
     * @return the extent
     * @see RangeModel#getExtent()
     */
    public int getExtent() {
        return getRangeModel().getExtent();
    }

    /**
     * Returns the minumum value.
     * 
     * @return the minimum
     * @see RangeModel#getMinimum()
     */
    public int getMinimum() {
        return getRangeModel().getMinimum();
    }

    /**
     * Returns the maximum value.
     * 
     * @return the maximum
     * @see RangeModel#getMaximum()
     */
    public int getMaximum() {
        return getRangeModel().getMaximum();
    }

    /**
     * Returns the figure used for page down.
     * 
     * @return the page down figure
     */
    protected IFigure getPageDown() {
        // TODO: The set method takes a Clickable while the get method returns
        // an IFigure.
        // Change the get method to return Clickable (since that's what it's
        // typed as).
        return pageDown;
    }

    /**
     * Returns the the amound the scrollbar will move when the page up or page
     * down areas are pressed.
     * 
     * @return the page increment
     */
    public int getPageIncrement() {
        return pageIncrement;
    }

    /**
     * Returns the figure used for page up.
     * 
     * @return the page up figure
     */
    protected IFigure getPageUp() {
        // TODO: The set method takes a Clickable while the get method returns
        // an IFigure.
        // Change the get method to return Clickable (since that's what it's
        // typed as).
        return pageUp;
    }

    /**
     * Returns the range model for this scrollbar.
     * 
     * @return the range model
     */
    public RangeModel getRangeModel() {
        return rangeModel;
    }

    /**
     * Returns the amount the scrollbar will move when the up or down arrow
     * buttons are pressed.
     * 
     * @return the step increment
     */
    public int getStepIncrement() {
        return stepIncrement;
    }

    /**
     * Returns the figure used as the scrollbar's thumb.
     * 
     * @return the thumb figure
     */
    protected IFigure getThumb() {
        return thumb;
    }

    /**
     * Returns the current scroll position of the scrollbar.
     * 
     * @return the current value
     * @see RangeModel#getValue()
     */
    public int getValue() {
        return getRangeModel().getValue();
    }

    /**
     * Returns the size of the range of allowable values.
     * 
     * @return the value range
     */
    protected int getValueRange() {
        return getMaximum() - getExtent() - getMinimum();
    }

    /**
     * Initilization of the ScrollBar. Sets the Scrollbar to have a
     * ScrollBarLayout with vertical orientation. Creates the Figures that make
     * up the components of the ScrollBar.
     * 
     * @since 2.0
     */
    protected void initialize() {
        setLayoutManager(new ScrollBarLayout(transposer));
        setUpClickable(createDefaultUpButton());
        setDownClickable(createDefaultDownButton());
        setPageUp(createPageUp());
        setPageDown(createPageDown());
        setThumb(createDefaultThumb());
    }

    /**
     * Returns <code>true</code> if this scrollbar is orientated horizontally,
     * <code>false</code> otherwise.
     * 
     * @return whether this scrollbar is horizontal
     */
    public boolean isHorizontal() {
        return isHorizontal;
    }

    private void pageDown() {
        setValue(getValue() + getPageIncrement());
    }

    private void pageUp() {
        setValue(getValue() - getPageIncrement());
    }

    /**
     * @see PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getSource() instanceof RangeModel) {
            setEnabled(getRangeModel().isEnabled());
            if (RangeModel.PROPERTY_VALUE.equals(event.getPropertyName())) {
                firePropertyChange("value", event.getOldValue(), //$NON-NLS-1$
                        event.getNewValue());
                revalidate();
            }
            if (RangeModel.PROPERTY_MINIMUM.equals(event.getPropertyName())) {
                firePropertyChange("value", event.getOldValue(), //$NON-NLS-1$
                        event.getNewValue());
                revalidate();
            }
            if (RangeModel.PROPERTY_MAXIMUM.equals(event.getPropertyName())) {
                firePropertyChange("value", event.getOldValue(), //$NON-NLS-1$
                        event.getNewValue());
                revalidate();
            }
            if (RangeModel.PROPERTY_EXTENT.equals(event.getPropertyName())) {
                firePropertyChange("value", event.getOldValue(), //$NON-NLS-1$
                        event.getNewValue());
                revalidate();
            }
        }
    }

    /**
     * @see IFigure#revalidate()
     */
    @Override
    public void revalidate() {
        // Override default revalidate to prevent going up the parent chain.
        // Reason for this
        // is that preferred size never changes unless orientation changes.
        invalidate();
        getUpdateManager().addInvalidFigure(this);
    }

    /**
     * Does nothing because this doesn't make sense for a scrollbar.
     * 
     * @see Orientable#setDirection(int)
     */
    @Override
    public void setDirection(int direction) {
        // Doesn't make sense for Scrollbar.
    }

    /**
     * Sets the Clickable that represents the down arrow of the Scrollbar to
     * <i>down</i>.
     * 
     * @param down
     *            the down button
     * @since 2.0
     */
    public void setDownClickable(Clickable down) {
        if (buttonDown != null) {
            remove(buttonDown);
        }
        buttonDown = down;
        if (buttonDown != null) {
            if (buttonDown instanceof Orientable)
                ((Orientable) buttonDown)
                        .setDirection(isHorizontal() ? PositionConstants.EAST
                                : PositionConstants.SOUTH);
            buttonDown.setFiringMethod(Clickable.REPEAT_FIRING);
            buttonDown.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stepDown();
                }
            });
            add(buttonDown, ScrollBarLayout.DOWN_ARROW);
        }
    }

    /**
     * Sets the Clickable that represents the up arrow of the Scrollbar to
     * <i>up</i>.
     * 
     * @param up
     *            the up button
     * @since 2.0
     */
    public void setUpClickable(Clickable up) {
        if (buttonUp != null) {
            remove(buttonUp);
        }
        buttonUp = up;
        if (up != null) {
            if (up instanceof Orientable)
                ((Orientable) up).setDirection(isHorizontal() ? PositionConstants.WEST
                        : PositionConstants.NORTH);
            buttonUp.setFiringMethod(Clickable.REPEAT_FIRING);
            buttonUp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stepUp();
                }
            });
            add(buttonUp, ScrollBarLayout.UP_ARROW);
        }
    }

    /**
     * @see IFigure#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean value) {
        if (isEnabled() == value)
            return;
        super.setEnabled(value);
        setChildrenEnabled(value);
        if (getThumb() != null) {
            getThumb().setVisible(value);
            revalidate();
        }
    }

    /**
     * Sets the extent of the Scrollbar to <i>ext</i>
     * 
     * @param ext
     *            the extent
     * @since 2.0
     */
    public void setExtent(int ext) {
        if (getExtent() == ext)
            return;
        getRangeModel().setExtent(ext);
    }

    /**
     * Sets the orientation of the ScrollBar. If <code>true</code>, the
     * Scrollbar will have a horizontal orientation. If <code>false</code>, the
     * scrollBar will have a vertical orientation.
     * 
     * @param value
     *            <code>true</code> if the scrollbar should be horizontal
     * @since 2.0
     */
    public final void setHorizontal(boolean value) {
        setOrientation(value ? HORIZONTAL : VERTICAL);
    }

    /**
     * Sets the maximum position to <i>max</i>.
     * 
     * @param max
     *            the maximum position
     * @since 2.0
     */
    public void setMaximum(int max) {
        if (getMaximum() == max)
            return;
        getRangeModel().setMaximum(max);
    }

    /**
     * Sets the minimum position to <i>min</i>.
     * 
     * @param min
     *            the minumum position
     * @since 2.0
     */
    public void setMinimum(int min) {
        if (getMinimum() == min)
            return;
        getRangeModel().setMinimum(min);
    }

    /**
     * @see Orientable#setOrientation(int)
     */
    @Override
    public void setOrientation(int value) {
        if ((value == HORIZONTAL) == isHorizontal())
            return;
        isHorizontal = value == HORIZONTAL;
        transposer.setEnabled(isHorizontal);

        setChildrenOrientation(value);
        super.revalidate();
    }

    /**
     * Sets the ScrollBar to scroll <i>increment</i> pixels when its pageup or
     * pagedown buttons are pressed. (Note that the pageup and pagedown buttons
     * are <b>NOT</b> the arrow buttons, they are the figures between the arrow
     * buttons and the ScrollBar's thumb figure).
     * 
     * @param increment
     *            the new page increment
     * @since 2.0
     */
    public void setPageIncrement(int increment) {
        pageIncrement = increment;
    }

    /**
     * Sets the pagedown button to the passed Clickable. The pagedown button is
     * the figure between the down arrow button and the ScrollBar's thumb
     * figure.
     * 
     * @param down
     *            the page down figure
     * @since 2.0
     */
    public void setPageDown(Clickable down) {
        if (pageDown != null)
            remove(pageDown);
        pageDown = down;
        if (pageDown != null) {
            pageDown.setFiringMethod(Clickable.REPEAT_FIRING);
            pageDown.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    pageDown();
                }
            });
            add(down, ScrollBarLayout.PAGE_DOWN);
        }
    }

    /**
     * Sets the pageup button to the passed Clickable. The pageup button is the
     * rectangular figure between the down arrow button and the ScrollBar's
     * thumb figure.
     * 
     * @param up
     *            the page up figure
     * @since 2.0
     */
    public void setPageUp(Clickable up) {
        if (pageUp != null)
            remove(pageUp);
        pageUp = up;
        if (pageUp != null) {
            pageUp.setFiringMethod(Clickable.REPEAT_FIRING);
            pageUp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    pageUp();
                }
            });
            add(pageUp, ScrollBarLayout.PAGE_UP);
        }
    }

    /**
     * Sets the ScrollBar's RangeModel to the passed value.
     * 
     * @param rangeModel
     *            the new range model
     * @since 2.0
     */
    public void setRangeModel(RangeModel rangeModel) {
        if (this.rangeModel != null)
            this.rangeModel.removePropertyChangeListener(this);
        this.rangeModel = rangeModel;
        rangeModel.addPropertyChangeListener(this);
    }

    /**
     * Sets the ScrollBar's step increment to the passed value. The step
     * increment indicates how many pixels the ScrollBar will scroll when its up
     * or down arrow button is pressed.
     * 
     * @param increment
     *            the new step increment
     * @since 2.0
     */
    public void setStepIncrement(int increment) {
        stepIncrement = increment;
    }

    /**
     * Sets the ScrollBar's thumb to the passed Figure. The thumb is the
     * draggable component of the ScrollBar that indicates the ScrollBar's
     * position.
     * 
     * @param figure
     *            the thumb figure
     * @since 2.0
     */
    public void setThumb(IFigure figure) {
        if (thumb != null) {
            thumb.removeMouseListener(thumbDragger);
            thumb.removeMouseMotionListener(thumbDragger);
            remove(thumb);
        }
        thumb = figure;
        if (thumb != null) {
            thumb.addMouseListener(thumbDragger);
            thumb.addMouseMotionListener(thumbDragger);
            add(thumb, ScrollBarLayout.THUMB);
        }
    }

    /**
     * Sets the value of the Scrollbar to <i>v</i>
     * 
     * @param v
     *            the new value
     * @since 2.0
     */
    public void setValue(int v) {
        getRangeModel().setValue(v);
    }

    /**
     * Causes the ScrollBar to scroll down (or right) by the value of its step
     * increment.
     * 
     * @since 2.0
     */
    protected void stepDown() {
        setValue(getValue() + getStepIncrement());
    }

    /**
     * Causes the ScrollBar to scroll up (or left) by the value of its step
     * increment.
     * 
     * @since 2.0
     */
    protected void stepUp() {
        setValue(getValue() - getStepIncrement());
    }

    /**
     * @since 3.6
     */
    protected class ThumbDragger extends MouseMotionListener.Stub implements
            MouseListener {
        protected Point start;
        protected int dragRange;
        protected int revertValue;
        protected boolean armed;

        public ThumbDragger() {
        }

        @Override
        public void mousePressed(MouseEvent me) {
            armed = true;
            start = me.getLocation();
            Rectangle area = new Rectangle(transposer.t(getClientArea()));
            Dimension thumbSize = transposer.t(getThumb().getSize());
            if (getButtonUp() != null)
                area.height -= transposer.t(getButtonUp().getSize()).height;
            if (getButtonDown() != null)
                area.height -= transposer.t(getButtonDown().getSize()).height;
            Dimension sizeDifference = new Dimension(area.width, area.height
                    - thumbSize.height);
            dragRange = sizeDifference.height;
            revertValue = getValue();
            me.consume();
        }

        @Override
        public void mouseDragged(MouseEvent me) {
            if (!armed)
                return;
            Dimension difference = transposer.t(me.getLocation().getDifference(
                    start));
            int change = getValueRange() * difference.height / dragRange;
            setValue(revertValue + change);
            me.consume();
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            if (!armed)
                return;
            armed = false;
            me.consume();
        }

        @Override
        public void mouseDoubleClicked(MouseEvent me) {
        }
    }

}
