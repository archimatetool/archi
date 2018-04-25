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
 * Alpha Opacity Command
 *
 * @author Phillip Beauvoir
 */
public class DiagramModelObjectAlphaCommand extends EObjectFeatureCommand {
    
    public DiagramModelObjectAlphaCommand(IDiagramModelObject object, int alpha) {
        super(Messages.DiagramModelObjectAlphaCommand_0, object, IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__ALPHA, alpha);
    }
}