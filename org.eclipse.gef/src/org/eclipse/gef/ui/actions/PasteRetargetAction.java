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

import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;

import org.eclipse.gef.internal.GEFMessages;

/**
 * @author Eric Bordeau
 * @deprecated Use org.eclipse.ui.actions.ActionFactory instead
 */
public class PasteRetargetAction extends RetargetAction {

    /**
     * Constructs a new PasteRetargetAction with the default ID, label and
     * image.
     */
    public PasteRetargetAction() {
        super(ActionFactory.PASTE.getId(), GEFMessages.PasteAction_Label);
        ISharedImages sharedImages = PlatformUI.getWorkbench()
                .getSharedImages();
        setImageDescriptor(sharedImages
                .getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
        setDisabledImageDescriptor(sharedImages
                .getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
    }

}
