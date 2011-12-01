/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.ui.components;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;


/**
 * This adapter class provides default implementations for the
 * methods described by the <code>IPartListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>IPartListener</code> can
 * extend this class and override only the methods which they are
 * interested in.
 * </p>
 *
 * @see IPartListener
 * 
 * @author Phillip Beauvoir
 */
public abstract class PartListenerAdapter implements IPartListener {

    @Override
    public void partActivated(IWorkbenchPart part) {
    }

    @Override
    public void partBroughtToTop(IWorkbenchPart part) {
    }

    @Override
    public void partClosed(IWorkbenchPart part) {
    }

    @Override
    public void partDeactivated(IWorkbenchPart part) {
    }

    @Override
    public void partOpened(IWorkbenchPart part) {
    }

}
