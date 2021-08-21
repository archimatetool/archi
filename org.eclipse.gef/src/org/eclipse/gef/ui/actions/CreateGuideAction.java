/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.actions;

import java.util.Arrays;

import org.eclipse.jface.action.Action;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.ui.rulers.GuideEditPart;
import org.eclipse.gef.internal.ui.rulers.RulerEditPart;
import org.eclipse.gef.rulers.RulerProvider;

/**
 * An Action that creates a guide on a ruler and reveals it.
 * 
 * @author Pratik Shah
 * @since 3.0
 */
public class CreateGuideAction extends Action {

    private EditPartViewer viewer;

    /**
     * Constructor
     * 
     * @param ruler
     *            the viewer for the ruler on which the guide is to be created
     */
    public CreateGuideAction(EditPartViewer ruler) {
        super(GEFMessages.Create_Guide_Label);
        viewer = ruler;
        setToolTipText(GEFMessages.Create_Guide_Tooltip);
    }

    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    @Override
    public void run() {
        RulerProvider provider = ((RulerEditPart) viewer.getRootEditPart()
                .getChildren().get(0)).getRulerProvider();

        // Determine where the guide should be created
        int[] positions = provider.getGuidePositions();
        Arrays.sort(positions);
        int index = 0;
        int newPosition = GuideEditPart.MIN_DISTANCE_BW_GUIDES + 1;
        int desiredDifference = (GuideEditPart.MIN_DISTANCE_BW_GUIDES * 2) + 1;
        boolean found = positions.length > 0
                && positions[0] > desiredDifference;
        while (index < positions.length - 1 && !found) {
            if (positions[index + 1] - positions[index] > desiredDifference) {
                newPosition += positions[index];
                found = true;
            }
            index++;
        }
        if (!found && positions.length > 0)
            newPosition += positions[positions.length - 1];

        // Create the guide and reveal it
        viewer.getEditDomain().getCommandStack()
                .execute(provider.getCreateGuideCommand(newPosition));
        viewer.reveal((EditPart) viewer.getEditPartRegistry().get(
                provider.getGuideAt(newPosition)));
    }

}
