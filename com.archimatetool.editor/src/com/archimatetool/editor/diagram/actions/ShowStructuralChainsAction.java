/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.IArchimateDiagramEditor;
import com.archimatetool.editor.ui.IArchimateImages;



/**
 * Show Derived Relations Chains Action
 * 
 * @author Phillip Beauvoir
 */
public class ShowStructuralChainsAction extends WorkbenchPartAction {
    
    public static final String ID = "ShowStructuralChainsAction"; //$NON-NLS-1$
    public static final String DEFAULT_TEXT = Messages.ShowStructuralChainsAction_0;

    public ShowStructuralChainsAction(IWorkbenchPart part) {
        super(part);
        setId(ID);
        setText(DEFAULT_TEXT);
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_DERIVED_16));
    }
    
    @Override
    protected boolean calculateEnabled() {
        return true;
    }
    
    private boolean isShowingChains() {
        GraphicalViewer viewer = (GraphicalViewer)getWorkbenchPart().getAdapter(GraphicalViewer.class);
        Boolean val = (Boolean)viewer.getProperty(IArchimateDiagramEditor.PROPERTY_SHOW_STRUCTURAL_CHAIN);
        if (val != null)
            return val.booleanValue();
        return false;
    }

    @Override
    public void run() {
        GraphicalViewer viewer = (GraphicalViewer)getWorkbenchPart().getAdapter(GraphicalViewer.class);
        boolean val = !isShowingChains();
        viewer.setProperty(IArchimateDiagramEditor.PROPERTY_SHOW_STRUCTURAL_CHAIN, new Boolean(val));
        setText(isShowingChains() ? Messages.ShowStructuralChainsAction_1 : DEFAULT_TEXT);
    }
}
