/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import uk.ac.bolton.archimate.editor.ui.services.ViewManager;
import uk.ac.bolton.archimate.editor.views.tree.ITreeModelView;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;

/**
 * Action to select the current element in the Tree
 * 
 * @author Phillip Beauvoir
 */
public class SelectElementInTreeAction extends SelectionAction {

    public static final String ID = "SelectElementInTreeAction"; //$NON-NLS-1$

    public SelectElementInTreeAction(IWorkbenchPart part) {
        super(part);
        setText(Messages.SelectElementInTreeAction_0);
        setId(ID);
        setToolTipText(Messages.SelectElementInTreeAction_1);
    }

    @Override
    public void run() {
        List<?> selection = getSelectedObjects();
        
        List<Object> elements = new ArrayList<Object>();
        
        for(Object object : selection) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(model instanceof IDiagramModel) {
                    elements.add(model);
                }
                else if(model instanceof IDiagramModelArchimateObject) {
                    elements.add(((IDiagramModelArchimateObject)model).getArchimateElement());
                }
                else if(model instanceof IDiagramModelArchimateConnection) {
                    elements.add(((IDiagramModelArchimateConnection)model).getRelationship());
                }
            }
        }
        
        ITreeModelView view = (ITreeModelView)ViewManager.showViewPart(ITreeModelView.ID, false);
        if(view != null) {
            view.getViewer().setSelection(new StructuredSelection(elements), true);
        }
    }

    @Override
    protected boolean calculateEnabled() {
        List<?> list = getSelectedObjects();
        
        if(list.isEmpty()) {
            return false;
        }
        
        for(Object object : list) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(model instanceof IDiagramModel || model instanceof IDiagramModelArchimateConnection || model instanceof IDiagramModelArchimateObject) {
                    return true;
                }
            }
        }
        
        return false;
    }

}
