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
package org.eclipse.gef.ui.stackview;

import org.eclipse.swt.graphics.Image;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;

/**
 * Internal class used for a debug view.
 * 
 * @deprecated this class will be deleted
 */
public class TreeLabelProvider implements
        org.eclipse.jface.viewers.ILabelProvider {

    /** The CommandStack **/
    protected CommandStack stack = null;
    /** Icons associated with a TreeLabelProvider **/
    protected static Image yesIcon = ImageDescriptor.createFromFile(
            TreeLabelProvider.class, "icons/YESGRN.gif").createImage(), //$NON-NLS-1$
            noIcon = ImageDescriptor.createFromFile(TreeLabelProvider.class,
                    "icons/NORED.gif").createImage(); //$NON-NLS-1$
    /** Label style constants **/
    public static byte NORMAL_LABEL_STYLE = 2, DEBUG_LABEL_STYLE = 3;

    /** Label style, NORMAL_LABEL_STYLE by default **/
    protected byte labelStyle = NORMAL_LABEL_STYLE;

    /**
     * Creates a new TreeLabelProvider with the given CommandStack
     * 
     * @param stack
     *            the CommandStack
     */
    public TreeLabelProvider(CommandStack stack) {
        this.stack = stack;
    }

    /**
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(ILabelProviderListener)
     */
    @Override
    public void addListener(ILabelProviderListener l) {
    }

    /**
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
     */
    @Override
    public void dispose() {
    }

    /**
     * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
     */
    @Override
    public Image getImage(Object o) {
        if (o instanceof Command) {
            Command command = (Command) o;
            // if(((DefaultCommandStack)stack).canUndoCommand(command))
            // return yesIcon;
            // if(((DefaultCommandStack)stack).canRedoCommand(command))
            // return noIcon;
            if (command.canUndo())
                return yesIcon;
            else
                return noIcon;
        }
        return null;
    }

    /**
     * Returns the label style of this TreeLabelProvider.
     * 
     * @return the label style
     */
    public byte getLabelStyle() {
        return labelStyle;
    }

    /**
     * @see ILabelProvider#getText(java.lang.Object)
     */
    @Override
    public String getText(Object o) {
        if (o instanceof CommandStack)
            return "Command Stack";//$NON-NLS-1$
        if (o instanceof Command) {
            if (getLabelStyle() == NORMAL_LABEL_STYLE)
                if (((Command) o).getLabel() == null)
                    return "";//$NON-NLS-1$
                else
                    return ((Command) o).getLabel();
            if (getLabelStyle() == DEBUG_LABEL_STYLE)
                if (((Command) o).getDebugLabel() == null)
                    return "";//$NON-NLS-1$
                else
                    return ((Command) o).getDebugLabel();
        }
        if (o instanceof Command)
            return ((Command) o).getLabel();
        return "???";//$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
     *      java.lang.String)
     */
    @Override
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    /**
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
     */
    @Override
    public void removeListener(ILabelProviderListener l) {
    }

    /**
     * Sets the labelStyle to the passed value
     * 
     * @param labelStyle
     *            the label style
     */
    public void setLabelStyle(byte labelStyle) {
        this.labelStyle = labelStyle;
    }

}
