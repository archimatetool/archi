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
 * Outline Opacity Command
 *
 * @author Phillip Beauvoir
 */
public class DiagramModelObjectOutlineAlphaCommand extends EObjectFeatureCommand {
    
    public DiagramModelObjectOutlineAlphaCommand(IDiagramModelObject object, int alpha) {
        super(Messages.DiagramModelObjectOutlineAlphaCommand_0, object, IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__LINE_ALPHA, alpha);
    }
}