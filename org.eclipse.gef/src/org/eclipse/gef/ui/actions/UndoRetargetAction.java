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
package org.eclipse.gef.ui.actions;

import com.ibm.icu.text.MessageFormat;

import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.LabelRetargetAction;

import org.eclipse.gef.internal.GEFMessages;

/**
 * @author Eric Bordeau
 */
public class UndoRetargetAction extends LabelRetargetAction {

    /**
     * Constructs a new UndoRetargetAction with the default ID, label and image.
     */
    public UndoRetargetAction() {
        super(ActionFactory.UNDO.getId(), MessageFormat.format(
                GEFMessages.UndoAction_Label, new Object[] { "" }).trim()); //$NON-NLS-1$
        ISharedImages sharedImages = PlatformUI.getWorkbench()
                .getSharedImages();
        setImageDescriptor(sharedImages
                .getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
        setDisabledImageDescriptor(sharedImages
                .getImageDescriptor(ISharedImages.IMG_TOOL_UNDO_DISABLED));
    }

}
