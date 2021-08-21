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
import java.util.ArrayList;
import java.util.List;

/**
 * A ButtonGroup holds a group of {@link Clickable Clickable's} models and
 * provides unique selection in them. There is capability to add a default
 * selection. Models who want to belong to the group should just add themselves
 * to this group. By doing so they listen to this group for changes.
 * <p>
 * Setting of the default selection results in its being selected any time
 * {@link #setSelected(ButtonModel, boolean)} is called. If no default selection
 * is set, the last entry selected is not allowed to deselect.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ButtonGroup {

    private ButtonModel selectedModel;
    private ButtonModel defaultModel;
    private List members = new ArrayList();
    private List listeners = new ArrayList();

    /**
     * Constructs a ButtonGroup with no default selection.
     * 
     * @since 2.0
     */
    public ButtonGroup() {
    }

    /**
     * Adds the passed ButtonModel to the ButtonGroup.
     * 
     * @param model
     *            ButtonModel to be added to this group
     * @since 2.0
     */
    public void add(ButtonModel model) {
        if (!members.contains(model)) {
            model.setGroup(this);
            members.add(model);
        }
    }

    /**
     * Adds the passed listener. ButtonGroups use PropertyChangeListeners to
     * react to selection changes in the ButtonGroup.
     * 
     * @param listener
     *            Listener to be added to this group
     * @since 2.0
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Fires a PropertyChangeEvent to all PropertyChangeListeners added to this
     * ButtonGroup.
     * 
     * @param oldValue
     *            Old selection value
     * @param newValue
     *            New selection value
     * @since 2.0
     */
    protected void firePropertyChange(Object oldValue, Object newValue) {
        PropertyChangeEvent event = new PropertyChangeEvent(this,
                ButtonModel.SELECTED_PROPERTY, oldValue, newValue);
        for (int i = 0; i < listeners.size(); i++)
            ((PropertyChangeListener) listeners.get(i)).propertyChange(event);
    }

    /**
     * Returns the ButtonModel which is selected by default for this
     * ButtonGroup.
     * 
     * @return The default ButtonModel
     * @since 2.0
     */
    public ButtonModel getDefault() {
        return defaultModel;
    }

    /**
     * Returns a List which contains all of the {@link ButtonModel ButtonModels}
     * added to this ButtonGroup.
     * 
     * @return The List of ButtonModels in this ButtonGroup
     * @since 2.0
     */
    public List getElements() {
        return members;
    }

    /**
     * Returns the ButtonModel for the currently selected button.
     * 
     * @return The ButtonModel for the currently selected button
     * @since 2.0
     */
    public ButtonModel getSelected() {
        return selectedModel;
    }

    /**
     * Determines if the given ButtonModel is selected or not.
     * 
     * @param model
     *            Model being tested for selected status
     * @return Selection state of the given model
     * @since 2.0
     */
    public boolean isSelected(ButtonModel model) {
        return (model == getSelected());
    }

    /**
     * Removes the given ButtonModel from this ButtonGroup.
     * 
     * @param model
     *            ButtonModel being removed
     * @since 2.0
     */
    public void remove(ButtonModel model) {
        if (getSelected() == model)
            setSelected(getDefault(), true);
        if (defaultModel == model)
            defaultModel = null;
        members.remove(model);
    }

    /**
     * Removes the passed PropertyChangeListener from this ButtonGroup.
     * 
     * @param listener
     *            PropertyChangeListener to be removed
     * @since 2.0
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Sets the passed ButtonModel to be the currently selected ButtonModel of
     * this ButtonGroup. Fires a property change.
     * 
     * @param model
     *            ButtonModel to be selected
     * @since 2.0
     */
    protected void selectNewModel(ButtonModel model) {
        ButtonModel oldModel = selectedModel;
        selectedModel = model;
        if (oldModel != null)
            oldModel.setSelected(false);
        firePropertyChange(oldModel, model);
    }

    /**
     * Sets the default selection of this ButtonGroup. Does nothing if it is not
     * present in the group. Sets selection to the passed ButtonModel.
     * 
     * @param model
     *            ButtonModel which is to be the default selection.
     * @since 2.0
     */
    public void setDefault(ButtonModel model) {
        defaultModel = model;
        if (getSelected() == null && defaultModel != null)
            defaultModel.setSelected(true);
    }

    /**
     * Sets the button with the given ButtonModel to be selected.
     * 
     * @param model
     *            The ButtonModel to be selected
     * @since 2.0
     */
    public void setSelected(ButtonModel model) {
        if (model == null)
            selectNewModel(null);
        else
            model.setSelected(true);
    }

    /**
     * Sets model to the passed state.
     * <p>
     * If <i>value</i> is
     * <ul>
     * <li><code>true</code>:
     * <ul>
     * <li>The passed ButtonModel will own selection.
     * </ul>
     * <li><code>false</code>:
     * <ul>
     * <li>If the passed model owns selection, it will lose selection, and
     * selection will be given to the default ButonModel. If no default
     * ButtonModel was set, selection will remain as it was, as one ButtonModel
     * must own selection at all times.
     * <li>If the passed model does not own selection, then selection will
     * remain as it was.
     * </ul>
     * </ul>
     * 
     * @param model
     *            The model to be affected
     * @param value
     *            The selected state
     * @since 2.0
     */
    public void setSelected(ButtonModel model, boolean value) {
        if (value) {
            if (model == getSelected())
                return;
            selectNewModel(model);
        } else {
            if (model != getSelected())
                return;
            if (getDefault() == null)
                return;
            getDefault().setSelected(true);
        }
    }

}
