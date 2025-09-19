/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.IHandlerService;

import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelComponent;


/**
 * Select Same Object Type Action
 * 
 * @author Phillip Beauvoir
 */
public class SelectSameObjectTypeAction extends SelectionAction {
    
    public static final String ID = "com.archimatetool.editor.selectSameObjectType"; //$NON-NLS-1$
    
    public SelectSameObjectTypeAction(IWorkbenchPart part) {
        super(part);
        setText(Messages.SelectSameObjectTypeAction_0);
        setId(ID);

        // Register for key binding
        setActionDefinitionId(ID);
        IHandlerService service = part.getSite().getService(IHandlerService.class);
        service.activateHandler(getActionDefinitionId(), new ActionHandler(this));
    }
    
    @Override
    protected boolean calculateEnabled() {
        for(Object object : getSelectedObjects()) {
            if(object instanceof EditPart editPart && getObjectClass(editPart.getModel()) != null) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void run() {
        IDiagramModelEditor editor = (IDiagramModelEditor)getWorkbenchPart();
        List<Object> sameObjects = new ArrayList<>();
        
        Set<EClass> selectedTypes = getSelectedTypes();
        
        // Get all objects in the diagram and collect the ones of the same type
        for(Iterator<EObject> iter = editor.getModel().eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(selectedTypes.contains(getObjectClass(eObject))) {
                sameObjects.add(eObject);
            }
        }
        
        // Select them
        if(sameObjects.size() > 1) {
            editor.selectObjects(sameObjects.toArray());
        }
    }
    
    private Set<EClass> getSelectedTypes() {
        Set<EClass> selected = new HashSet<>();
        
        for(Object object : getSelectedObjects()) {
            if(object instanceof EditPart editPart) {
                EClass eClass = getObjectClass(editPart.getModel());
                if(eClass != null) {
                    selected.add(eClass);
                }
            }
        }
        
        return selected;
    }
    
    private EClass getObjectClass(Object object) {
        if(object instanceof IDiagramModelArchimateComponent dmac) {
            return dmac.getArchimateConcept().eClass();
        }
        else if(object instanceof IDiagramModelComponent dmc && !(object instanceof IDiagramModel)) {
            return dmc.eClass();
        }
        return null;
    }
}
