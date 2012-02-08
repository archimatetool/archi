/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelObject;


/**
 * Fill Color Command
 *
 * @author Phillip Beauvoir
 */
public class FillColorCommand extends EObjectFeatureCommand {
    
    public FillColorCommand(IDiagramModelObject object, String rgb) {
        super(Messages.FillColorCommand_0, object, IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__FILL_COLOR, rgb);
    }
}