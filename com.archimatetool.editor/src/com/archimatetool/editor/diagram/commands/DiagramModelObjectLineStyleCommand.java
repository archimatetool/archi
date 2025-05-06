/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import com.archimatetool.editor.model.commands.FeatureCommand;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Line Style Command
 *
 * @author Phillip Beauvoir
 */
public class DiagramModelObjectLineStyleCommand extends FeatureCommand {
    
    private int style;
    
    public DiagramModelObjectLineStyleCommand(IDiagramModelObject dmo, int style) {
        super(Messages.DiagramModelObjectLineStyleCommand_0, dmo, IDiagramModelObject.FEATURE_LINE_STYLE, style, getDefaultLineStyle(dmo));
        this.style = style;
    }
    
    @Override
    public boolean canExecute() {
        return super.canExecute() && style >= IDiagramModelObject.LINE_STYLE_SOLID && style <= IDiagramModelObject.LINE_STYLE_NONE;
    }
    
    /**
     * Get the default line style for the IDiagramModelObject
     * This can be either IDiagramModelObject.LINE_STYLE_SOLID or IDiagramModelObject.LINE_STYLE_DASHED
     */
    private static int getDefaultLineStyle(IDiagramModelObject dmo) {
        return (int)ObjectUIFactory.INSTANCE.getProvider(dmo).getDefaultFeatureValue(IDiagramModelObject.FEATURE_LINE_STYLE);
    }
}