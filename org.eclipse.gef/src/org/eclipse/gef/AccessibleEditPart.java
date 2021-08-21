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
package org.eclipse.gef;

import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;

import org.eclipse.draw2d.AccessibleBase;

/**
 * This class provides Accessibility support for
 * {@link org.eclipse.gef.EditPart EditParts}. EditParts are the unit of
 * selection in GEF. When selection changes, Accessibility clients are notified.
 * These clients then query the EditPartViewer for various accessibility
 * information.
 * <P>
 * EditParts must provide AccessibileEditPart adapters in order to work with
 * screen-readers, screen magnifiers, and other accessibility tools. EditParts
 * should override
 * {@link org.eclipse.gef.editparts.AbstractEditPart#getAccessibleEditPart()}.
 * 
 * @author hudsonr
 */
public abstract class AccessibleEditPart extends AccessibleBase {

    /**
     * @see org.eclipse.swt.accessibility.AccessibleControlAdapter#getChildCount(AccessibleControlEvent)
     * @param e
     *            AccessibleControlEvent
     */
    public abstract void getChildCount(AccessibleControlEvent e);

    /**
     * @see org.eclipse.swt.accessibility.AccessibleControlAdapter#getChildren(AccessibleControlEvent)
     * @param e
     *            AccessibleControlEvent
     */
    public abstract void getChildren(AccessibleControlEvent e);

    /**
     * @see org.eclipse.swt.accessibility.AccessibleControlAdapter#getDefaultAction(AccessibleControlEvent)
     * @param e
     *            AccessibleControlEvent
     */
    public void getDefaultAction(AccessibleControlEvent e) {
    }

    /**
     * @see org.eclipse.swt.accessibility.AccessibleAdapter#getDescription(AccessibleEvent)
     * @param e
     *            AccessibleEvent
     */
    public void getDescription(AccessibleEvent e) {
    }

    /**
     * @see org.eclipse.swt.accessibility.AccessibleAdapter#getKeyboardShortcut(AccessibleEvent)
     * @param e
     *            AccessibleEvent
     */
    public void getKeyboardShortcut(AccessibleEvent e) {
    }

    /**
     * @see org.eclipse.swt.accessibility.AccessibleAdapter#getHelp(AccessibleEvent)
     * @param e
     *            AccessibleEvent
     */
    public void getHelp(AccessibleEvent e) {
    }

    /**
     * @see org.eclipse.swt.accessibility.AccessibleAdapter#getName(AccessibleEvent)
     * @param e
     *            AccessibleEvent
     */
    public abstract void getName(AccessibleEvent e);

    /**
     * @see org.eclipse.swt.accessibility.AccessibleControlAdapter#getLocation(AccessibleControlEvent)
     * @param e
     *            AccessibleControlEvent
     */
    public abstract void getLocation(AccessibleControlEvent e);

    /**
     * @see org.eclipse.swt.accessibility.AccessibleControlAdapter#getRole(AccessibleControlEvent)
     * @param e
     *            AccessibleControlEvent
     */
    public void getRole(AccessibleControlEvent e) {
    }

    /**
     * @see org.eclipse.swt.accessibility.AccessibleControlAdapter#getState(AccessibleControlEvent)
     * @param e
     *            AccessibleControlEvent
     */
    public abstract void getState(AccessibleControlEvent e);

    /**
     * @see org.eclipse.swt.accessibility.AccessibleControlAdapter#getValue(AccessibleControlEvent)
     * @param e
     *            AccessibleControlEvent
     */
    public void getValue(AccessibleControlEvent e) {
    }

}
