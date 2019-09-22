/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.DiagramModelObjectOutlineAlphaCommand;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Outline Opacity Action
 * 
 * @author Phillip Beauvoir
 */
public class OutlineOpacityAction extends OpacityAction {
    
    public static final String ID = "OutlineOpacityAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.OutlineOpacityAction_0 + "..."; //$NON-NLS-1$
    
    public OutlineOpacityAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
    }

    @Override
    public void run() {
        List<?> selection = getSelectedObjects();
        
        IDiagramModelObject dmo = (IDiagramModelObject)getFirstValidSelectedModelObject(selection);
        if(dmo == null) {
            return;
        }
        
        // Set default alpha on first selected
        int alpha = dmo.getLineAlpha();

        OpacityDialog dialog = new OpacityDialog(getWorkbenchPart().getSite().getShell(), alpha);
        if(dialog.open() == Window.OK) {
            execute(createCommand(selection, dialog.getAlpha()));
        }
    }
    
    @Override
    protected String getFeatureName() {
        return IDiagramModelObject.FEATURE_LINE_ALPHA;
    }
    
    @Override
    protected Command getCommand(IDiagramModelObject dmo, int newValue) {
        return new DiagramModelObjectOutlineAlphaCommand(dmo, newValue);
    }
    
}
