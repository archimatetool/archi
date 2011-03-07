/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.sketch.policies;

import uk.ac.bolton.archimate.editor.diagram.policies.ContainerComponentEditPolicy;
import uk.ac.bolton.archimate.model.IDiagramModel;


/**
 * A policy to handle a Skectch View's Delete and DND commands
 * 
 * @author Phillip Beauvoir
 */
public class SketchContainerComponentEditPolicy extends ContainerComponentEditPolicy {

    @Override
    protected boolean canDropElement(Object element) {
        // Can only DND References to Diagram Models
        return (element instanceof IDiagramModel);
    }
}
