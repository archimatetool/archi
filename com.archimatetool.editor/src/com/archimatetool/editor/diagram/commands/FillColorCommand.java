/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;



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