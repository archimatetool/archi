/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IBorderObject;


/**
 * Border Color Command
 *
 * @author Phillip Beauvoir
 */
public class BorderColorCommand extends EObjectFeatureCommand {
    
    public BorderColorCommand(IBorderObject borderObject, String rgb) {
        super("Change border colour", borderObject, IArchimatePackage.Literals.BORDER_OBJECT__BORDER_COLOR, rgb);
    }
}