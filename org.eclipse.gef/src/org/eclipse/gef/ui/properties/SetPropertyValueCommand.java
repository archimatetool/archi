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
package org.eclipse.gef.ui.properties;

import com.ibm.icu.text.MessageFormat;

import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySource2;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.internal.GEFMessages;

/**
 * A command used to set or reset the value of a property.
 * 
 * @author pshah
 * @author anyssen
 * 
 * @since 3.7
 * 
 */
public class SetPropertyValueCommand extends Command {

    /**
     * Value constant to indicate that the property is to be reset to its
     * default value during execute/redo and undo.
     */
    protected static final Object DEFAULT_VALUE = new Object();

    /** the value to set for the property */
    private Object newValue;
    /** the old value of the property prior to executing this command */
    private Object oldValue;
    /** the id of the property whose value has to be set */
    private Object propertyId;
    /** the property source whose property has to be set */
    private IPropertySource propertySource;

    /**
     * Constructs a new {@link SetPropertyValueCommand}.
     * 
     * @param propertyLabel
     *            A label to identify the property whose value is set by this
     *            command.
     * @param propertySource
     *            The property source which provides the property, whose value
     *            is to be set.
     * @param propertyId
     *            The id of the property whose value is to be set.
     * @param newValue
     *            The new value to set for the property or
     *            {@link #DEFAULT_VALUE} to indicate that the property should be
     *            reset.
     * @since 3.7
     */
    public SetPropertyValueCommand(String propertyLabel,
            IPropertySource propertySource, Object propertyId, Object newValue) {
        super(MessageFormat.format(GEFMessages.SetPropertyValueCommand_Label,
                new Object[] { propertyLabel }).trim());
        this.propertySource = propertySource;
        this.propertyId = propertyId;
        this.newValue = newValue;
    }

    /**
     * @see org.eclipse.gef.commands.Command#canExecute()
     */
    @Override
    public boolean canExecute() {
        if (propertySource == null || propertyId == null) {
            return false;
        }
        if (newValue == DEFAULT_VALUE) {
            // we may only reset a property to its default value if it supports
            // the notion of a default value and it does not already have this
            // value
            boolean canExecute = propertySource.isPropertySet(propertyId);
            if (propertySource instanceof IPropertySource2) {
                canExecute &= (((IPropertySource2) propertySource)
                        .isPropertyResettable(propertyId));
            }
            return canExecute;
        }
        return true;
    }

    /**
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {
        /*
         * Fix for bug #54250 IPropertySource.isPropertySet(String) returns
         * false both when there is no default value, and when there is a
         * default value and the property is set to that value. To correctly
         * determine if a reset should be done during undo, we compare the
         * return value of isPropertySet(String) before and after
         * setPropertyValue(...) is invoked. If they are different (it must have
         * been false before and true after -- it cannot be the other way
         * around), then that means we need to reset.
         */
        boolean wasPropertySet = propertySource.isPropertySet(propertyId);
        oldValue = unwrapValue(propertySource.getPropertyValue(propertyId));

        // set value of property to new value or reset the value, if specified
        if (newValue == DEFAULT_VALUE) {
            propertySource.resetPropertyValue(propertyId);
        } else {
            propertySource.setPropertyValue(propertyId, unwrapValue(newValue));
        }

        // check if property was set to its default value before (so it will
        // have to be resetted during undo); note that if the new value is
        // DEFAULT_VALUE the old value may not have been the default value as
        // well, as the command would not be executable in this case.
        if (propertySource instanceof IPropertySource2) {
            if (!wasPropertySet
                    && ((IPropertySource2) propertySource)
                            .isPropertyResettable(propertyId)) {
                oldValue = DEFAULT_VALUE;
            }
        } else {
            if (!wasPropertySet && propertySource.isPropertySet(propertyId)) {
                oldValue = DEFAULT_VALUE;
            }
        }
    }

    /**
     * Returns the new value to be set for the property when executing or
     * redoing.
     * 
     * @return the new value or {@link #DEFAULT_VALUE} to indicate that the
     *         default value should be set as the new value.
     * @since 3.7
     */
    protected Object getNewValue() {
        return newValue;
    }

    /**
     * After the command has been executed or redone, returns the old value of
     * the property or {@link #DEFAULT_VALUE} if the property did not have a
     * value before.
     * 
     * @return the old value of the property or {@link #DEFAULT_VALUE}.
     * @since 3.7
     */
    protected Object getOldValue() {
        return oldValue;
    }

    /**
     * Returns the id by which to identify the property whose value is to be
     * set.
     * 
     * @return the id of the property whose value is to be set.
     * @since 3.7
     */
    protected Object getPropertyId() {
        return propertyId;
    }

    /**
     * Returns the {@link IPropertySource} which provides the property, whose
     * value is to be set.
     * 
     * @return the {@link IPropertySource} which provides the property.
     * @since 3.7
     */
    protected IPropertySource getPropertySource() {
        return propertySource;
    }

    /**
     * @see org.eclipse.gef.commands.Command#redo()
     */
    @Override
    public void redo() {
        execute();
    }

    /**
     * @see org.eclipse.gef.commands.Command#undo()
     */
    @Override
    public void undo() {
        if (oldValue == DEFAULT_VALUE) {
            propertySource.resetPropertyValue(propertyId);
        } else {
            propertySource.setPropertyValue(propertyId, oldValue);
        }
    }

    private Object unwrapValue(Object value) {
        if (value instanceof IPropertySource)
            return ((IPropertySource) value).getEditableValue();
        return value;
    }

}
