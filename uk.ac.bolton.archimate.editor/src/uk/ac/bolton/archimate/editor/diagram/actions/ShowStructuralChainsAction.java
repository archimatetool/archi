/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.actions;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;

import uk.ac.bolton.archimate.editor.diagram.IDiagramEditor;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;


/**
 * Show Derived Relations Chains Action
 * 
 * @author Phillip Beauvoir
 */
public class ShowStructuralChainsAction extends WorkbenchPartAction {
    
    public static final String ID = "ShowStructuralChainsAction";
    public static final String TEXT = "Show Structural Chains";

    public ShowStructuralChainsAction(IWorkbenchPart part) {
        super(part, AS_CHECK_BOX);
        setId(ID);
        setText(TEXT);
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_DERIVED_16));
        setChecked(isChecked());
    }

    @Override
    protected boolean calculateEnabled() {
        return true;
    }
    
    @Override
    public boolean isChecked() {
        GraphicalViewer viewer = (GraphicalViewer)getWorkbenchPart().getAdapter(GraphicalViewer.class);
        Boolean val = (Boolean)viewer.getProperty(IDiagramEditor.PROPERTY_SHOW_STRUCTURAL_CHAIN);
        if (val != null)
            return val.booleanValue();
        return false;
    }

    @Override
    public void run() {
        GraphicalViewer viewer = (GraphicalViewer)getWorkbenchPart().getAdapter(GraphicalViewer.class);
        boolean val = !isChecked();
        viewer.setProperty(IDiagramEditor.PROPERTY_SHOW_STRUCTURAL_CHAIN, new Boolean(val));
    }
    
    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_DERIVED_16);
    }
}
