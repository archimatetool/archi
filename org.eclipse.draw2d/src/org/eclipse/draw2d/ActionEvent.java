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
 * An event that occurs as a result of an action being performed.
 */
public class ActionEvent extends java.util.EventObject {

    private String actionName;

    /**
     * Constructs a new ActionEvent with <i>source</i> as the source of the
     * event.
     * 
     * @param source
     *            The source of the event
     */
    public ActionEvent(Object source) {
        super(source);
    }

    /**
     * Constructs a new ActionEvent with <i>source</i> as the source of the
     * event and <i>name</i> as the name of the action that was performed.
     * 
     * @param source
     *            The source of the event
     * @param name
     *            The name of the action
     */
    public ActionEvent(Object source, String name) {
        super(source);
        actionName = name;
    }

    /**
     * Returns the name of the action that was performed.
     * 
     * @return String The name of the action
     */
    public String getActionName() {
        return actionName;
    }

}
