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

import java.util.Iterator;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A Clickable responds to mouse clicks in some way (determined by a
 * ClickBehavior) and fires action events. No visual appearance of feedback is
 * offered. Depends on a model holder and an event handler which understands the
 * model and updates the model accordingly. {@link ButtonModel} is used by
 * default. Any figure can be set as contents to a Clickable.
 * Clickable->EventHandler->Model->ModelObserver->Listeners of actions.
 */
@SuppressWarnings("rawtypes")
public class Clickable extends Figure {

    private static final int ROLLOVER_ENABLED_FLAG = Figure.MAX_FLAG << 1,
            STYLE_BUTTON_FLAG = Figure.MAX_FLAG << 2,
            STYLE_TOGGLE_FLAG = Figure.MAX_FLAG << 3;

    /**
     * The highest reserved flag used by this class
     */
    protected static int MAX_FLAG = STYLE_TOGGLE_FLAG;

    /**
     * Style constant that defines a push button. The button will be pressed
     * when the mouse is pressed on top of it. The button will be released when
     * the mouse is released or is move off of the button.
     * 
     */
    public static final int STYLE_BUTTON = STYLE_BUTTON_FLAG;

    /**
     * Style constant that defines a toggle button. The button will toggle
     * between 2 states when the mouse is clicked on the button.
     */
    public static final int STYLE_TOGGLE = STYLE_TOGGLE_FLAG;

    /**
     * An action is performed every time the mouse is released.
     */
    public static final int DEFAULT_FIRING = 0;

    /**
     * Firing starts as soon as the mouse is pressed on this Clickable, and
     * keeps firing at prefixed intervals until the mouse is released.
     */
    public static final int REPEAT_FIRING = 1;

    /**
     * Observes the model for action and state changes.
     * 
     * @see ActionListener
     * @see ChangeListener
     */
    static interface ModelObserver extends ActionListener, ChangeListener {
    }

    private ClickableEventHandler eventHandler;

    private ButtonModel model;

    private ModelObserver modelObserver;

    {
        init();
        setRequestFocusEnabled(true);
        setFocusTraversable(true);
    }

    /**
     * Constructs a Clickable with no contents.
     */
    public Clickable() {
    }

    /**
     * Constructs a Clickable whose contents are provided as input. The content
     * figure occupies the entire region of the Clickable.
     * 
     * @param contents
     *            The content figure
     */
    public Clickable(IFigure contents) {
        this(contents, 0);
    }

    /**
     * Constructs a Clickable whose contents are provided as input. The content
     * figure occupies the entire region of the Clickable. Sets the style to the
     * given <i>style</i> (either {@link #STYLE_BUTTON} or {@link #STYLE_TOGGLE}
     * ).
     * 
     * @param contents
     *            The content figure
     * @param style
     *            The button style
     */
    public Clickable(IFigure contents, int style) {
        setContents(contents);
        setStyle(style);
    }

    /**
     * Adds the given listener to the list of action listeners of this Figure.
     * Listener is called whenever an action is performed.
     * 
     * @param listener
     *            The ActionListener to be added
     * @since 2.0
     */
    public void addActionListener(ActionListener listener) {
        addListener(ActionListener.class, listener);
    }

    /**
     * Adds the given listener to the list of state change listeners of this
     * figure. A ChangeListener is informed if there is any state change in the
     * model requiring action by the listener.
     * 
     * @param listener
     *            The ChangeListener to be added
     * @since 2.0
     */
    public void addChangeListener(ChangeListener listener) {
        addListener(ChangeListener.class, listener);
    }

    /**
     * Returns a newly created ButtonModel as the default model to be used by
     * this Clickable based on the button style.
     * 
     * @return The model to be used by default
     * @since 2.0
     */
    protected ButtonModel createDefaultModel() {
        if (isStyle(STYLE_TOGGLE))
            return new ToggleModel();
        else
            return new ButtonModel();
    }

    /**
     * Returns a newly created event handler for this Clickable and its model.
     * 
     * @return The event handler
     * @since 2.0
     */
    protected ClickableEventHandler createEventHandler() {
        return new ClickableEventHandler();
    }

    /**
     * Returns a newly created model observer which listens to the model, and
     * fires any action or state changes. A ModelObserver holds both an action
     * listener and a state change listener.
     * 
     * @return The newly created model observer
     * @since 2.0
     */
    protected ModelObserver createModelObserver() {
        return new ModelObserver() {
            @Override
            public void actionPerformed(ActionEvent action) {
                fireActionPerformed();
            }

            @Override
            public void handleStateChanged(ChangeEvent change) {
                fireStateChanged(change);
            }
        };
    }

    /**
     * Fires an action performed event.
     * 
     * @since 2.0
     */
    public void doClick() {
        fireActionPerformed();
    }

    /**
     * Called when there has been an action performed by this Clickable, which
     * is determined by the model. Notifies all ActionListener type listeners of
     * an action performed.
     * 
     * @since 2.0
     */
    protected void fireActionPerformed() {
        ActionEvent action = new ActionEvent(this);
        Iterator listeners = getListeners(ActionListener.class);
        while (listeners.hasNext())
            ((ActionListener) listeners.next()) // Leave newline for debug
                                                // stepping
                    .actionPerformed(action);
    }

    /**
     * Called when there has been a change of state in the model of this
     * clickable. Notifies all ChangeListener type listeners of the state
     * change.
     * 
     * @param modelChange
     *            The ChangeEvent
     * @since 2.0
     */
    protected void fireStateChanged(ChangeEvent modelChange) {
        ChangeEvent change = new ChangeEvent(this,
                modelChange.getPropertyName());
        Iterator listeners = getListeners(ChangeListener.class);
        while (listeners.hasNext())
            ((ChangeListener) listeners.next()) // Leave newline for debug
                                                // stepping
                    .handleStateChanged(change);
    }

    /**
     * Returns the behavior model used by this Clickable.
     * 
     * @return The model used by this Clickable
     * @since 2.0
     */
    public ButtonModel getModel() {
        return model;
    }

    /**
     * Adds the given ClickableEventHandler to this clickable. A
     * ClickableEventHandler should be a MouseListener, MouseMotionListener,
     * ChangeListener, KeyListener, and FocusListener.
     * 
     * @param handler
     *            The new event handler
     * @since 2.0
     */
    protected void hookEventHandler(ClickableEventHandler handler) {
        if (handler == null)
            return;
        addMouseListener(handler);
        addMouseMotionListener(handler);
        addChangeListener(handler);
        addKeyListener(handler);
        addFocusListener(handler);
    }

    /**
     * Initializes this Clickable by setting a default model and adding a
     * clickable event handler for that model.
     * 
     * @since 2.0
     */
    protected void init() {
        setModel(createDefaultModel());
        setEventHandler(createEventHandler());
    }

    /**
     * Returns <code>true</code> if rollover feedback is enabled.
     * 
     * @return <code>true</code> rollover feedback is enabled
     * @since 2.0
     */
    public boolean isRolloverEnabled() {
        return (flags & ROLLOVER_ENABLED_FLAG) != 0;
    }

    /**
     * Returns <code>true</code> if this Clickable is in a selected state. The
     * model is the one which holds all this state based information.
     * 
     * @return <code>true</code> if this Clickable is in a selected state
     * @since 2.0
     */
    public boolean isSelected() {
        return getModel().isSelected();
    }

    /**
     * Returns <code>true</code> if this Clickable's style is the same as the
     * passed style.
     * 
     * @param style
     *            The style to be checked
     * @return <code>true</code> if this Clickable's style is the same as the
     *         passed style
     * @since 2.0
     */
    public boolean isStyle(int style) {
        return ((style & flags) == style);
    }

    /**
     * If this Clickable has focus, this method paints a focus rectangle.
     * 
     * @param graphics
     *            Graphics handle for painting
     */
    @Override
    protected void paintBorder(Graphics graphics) {
        super.paintBorder(graphics);
        if (hasFocus()) {
            graphics.setForegroundColor(ColorConstants.black);
            graphics.setBackgroundColor(ColorConstants.white);

            Rectangle area = getClientArea();
            if (isStyle(STYLE_BUTTON))
                graphics.drawFocus(area.x, area.y, area.width, area.height);
            else
                graphics.drawFocus(area.x, area.y, area.width - 1,
                        area.height - 1);
        }
    }

    /**
     * Paints the area of this figure excluded by the borders. Induces a (1,1)
     * pixel shift in the painting if the mouse is armed, giving it the pressed
     * appearance.
     * 
     * @param graphics
     *            Graphics handle for painting
     * @since 2.0
     */
    @Override
    protected void paintClientArea(Graphics graphics) {
        if (isStyle(STYLE_BUTTON)
                && (getModel().isArmed() || getModel().isSelected())) {
            graphics.translate(1, 1);
            graphics.pushState();
            super.paintClientArea(graphics);
            graphics.popState();
            graphics.translate(-1, -1);
        } else
            super.paintClientArea(graphics);
    }

    /**
     * Removes the given listener from the list of ActionListener's of this
     * Clickable.
     * 
     * @param listener
     *            Listener to be removed from this figure
     * @since 2.0
     */
    public void removeActionListener(ActionListener listener) {
        removeListener(ActionListener.class, listener);
    }

    /**
     * Removes the given listener from the list of ChangeListener's of this
     * clickable.
     * 
     * @param listener
     *            Listener to be removed from this figure
     * @since 2.0
     */
    public void removeChangeListener(ChangeListener listener) {
        removeListener(ChangeListener.class, listener);
    }

    /**
     * Sets the Figure which is the contents of this Clickable. This Figure
     * occupies the entire clickable region.
     * 
     * @param contents
     *            Contents of the clickable
     * @since 2.0
     */
    protected void setContents(IFigure contents) {
        setLayoutManager(new StackLayout());
        if (getChildren().size() > 0)
            remove((IFigure) getChildren().get(0));
        add(contents);
    }

    /**
     * @see org.eclipse.draw2d.IFigure#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean value) {
        if (isEnabled() == value)
            return;
        super.setEnabled(value);
        getModel().setEnabled(value);
        setChildrenEnabled(value);
    }

    /**
     * Sets the event handler which interacts with the model to determine the
     * behavior of this Clickable.
     * 
     * @param h
     *            Event handler for this clickable
     * @since 2.0
     */
    public void setEventHandler(ClickableEventHandler h) {
        if (eventHandler != null)
            unhookEventHandler(eventHandler);
        eventHandler = h;
        if (eventHandler != null)
            hookEventHandler(eventHandler);
    }

    /**
     * Determines how this clickable is to fire notifications to its listeners.
     * In the default firing method ({@link #DEFAULT_FIRING}), an action is
     * performed every time the mouse is released. In the repeat firing method (
     * {@link #REPEAT_FIRING}), firing starts as soon as it is pressed on this
     * clickable, and keeps firing at prefixed intervals until the mouse is
     * released.
     * 
     * @param type
     *            Type of firing
     * @since 2.0
     */
    public void setFiringMethod(int type) {
        getModel().setFiringBehavior(type);
    }

    /**
     * Sets the model to be used by this clickable for its state and behavior
     * determination. This clickable removes any observers from the previous
     * model before adding new ones to the new model.
     * 
     * @param model
     *            The new model of this Clickable
     * @since 2.0
     */
    public void setModel(ButtonModel model) {
        if (this.model != null) {
            this.model.removeChangeListener(modelObserver);
            this.model.removeActionListener(modelObserver);
            modelObserver = null;
        }
        this.model = model;
        if (model != null) {
            modelObserver = createModelObserver();
            model.addActionListener(modelObserver);
            model.addChangeListener(modelObserver);
        }
    }

    /**
     * Enables or disables rollover feedback of this figure. Generally used in
     * conjunction with the model to determine if feedback is to be shown.
     * 
     * @param value
     *            The rollover state to be set
     * @since 2.0
     */
    public void setRolloverEnabled(boolean value) {
        if (isRolloverEnabled() == value)
            return;
        setFlag(ROLLOVER_ENABLED_FLAG, value);
        repaint();
    }

    /**
     * Sets the selected state of this Clickable. Since the model is responsible
     * for all state based information, it is informed of the state change.
     * Extending classes can choose selection information, if they do not
     * represent any selection.
     * 
     * @param value
     *            New selected state of this clickable.
     * @see #isSelected()
     * @since 2.0
     */
    public void setSelected(boolean value) {
        getModel().setSelected(value);
    }

    /**
     * Sets this Clickable's style to the passed value, either
     * {@link #STYLE_BUTTON} or {@link #STYLE_TOGGLE}.
     * 
     * @param style
     *            The button style
     * @since 2.0
     */
    public void setStyle(int style) {
        if ((style & STYLE_BUTTON) != 0) {
            setFlag(STYLE_BUTTON_FLAG, true);
            if (!(getBorder() instanceof ButtonBorder))
                setBorder(new ButtonBorder());
            setOpaque(true);
        } else {
            setFlag(STYLE_BUTTON_FLAG, false);
            setOpaque(false);
        }

        if ((style & STYLE_TOGGLE) != 0) {
            setFlag(STYLE_TOGGLE_FLAG, true);
            setModel(createDefaultModel());
        }
    }

    /**
     * Removes the given ClickableEventHandler containing listeners from this
     * Clickable.
     * 
     * @param handler
     *            The event handler to be removed
     * @since 2.0
     */
    protected void unhookEventHandler(ClickableEventHandler handler) {
        if (handler == null)
            return;
        removeMouseListener(handler);
        removeMouseMotionListener(handler);
        removeChangeListener(handler);
    }

}
