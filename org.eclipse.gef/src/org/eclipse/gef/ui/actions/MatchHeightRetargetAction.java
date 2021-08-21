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

import org.eclipse.ui.actions.LabelRetargetAction;

import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.InternalImages;

/**
 * A LabelRetargetAction for MatchHeightAction.
 */
public class MatchHeightRetargetAction extends LabelRetargetAction {

    /**
     * Constructs a <code>MatchHeightRetargetAction</code>.
     */
    public MatchHeightRetargetAction() {
        super(GEFActionConstants.MATCH_HEIGHT,
                GEFMessages.MatchHeightAction_Label);
        setImageDescriptor(InternalImages.DESC_MATCH_HEIGHT);
        setDisabledImageDescriptor(InternalImages.DESC_MATCH_HEIGHT_DIS);
        setToolTipText(GEFMessages.MatchHeightAction_Tooltip);
    }

}
