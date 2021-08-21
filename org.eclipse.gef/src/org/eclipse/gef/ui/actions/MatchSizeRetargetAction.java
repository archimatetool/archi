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
 * A LabelRetargetAction for MatchSizeAction.
 * 
 * @since 3.7
 */
public class MatchSizeRetargetAction extends LabelRetargetAction {

    /**
     * Constructs a <code>MatchSizeRetargetAction</code>.
     */
    public MatchSizeRetargetAction() {
        super(GEFActionConstants.MATCH_SIZE, GEFMessages.MatchSizeAction_Label);
        setImageDescriptor(InternalImages.DESC_MATCH_SIZE);
        setDisabledImageDescriptor(InternalImages.DESC_MATCH_SIZE_DIS);
        setToolTipText(GEFMessages.MatchSizeAction_Tooltip);
    }

}
