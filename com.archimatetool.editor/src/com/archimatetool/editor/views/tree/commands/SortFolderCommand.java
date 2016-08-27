/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IFolder;



/**
 * Sort Folder Command.
 * 
 * @author Phillip Beauvoir
 */
public class SortFolderCommand extends Command implements Comparator<EObject>  {
    
    private IFolder fFolder;
    private List<EObject> fList;

    public SortFolderCommand(IFolder folder) {
        setLabel(Messages.SortFolderCommand_0);
        fFolder = folder;
        
        // Keep a copy of the orginal order
        fList = new ArrayList<EObject>();
        for(EObject o : fFolder.getElements()) {
            fList.add(o);
        }
    }
    
    @Override
    public void execute() {
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                IEditorModelManager.PROPERTY_ECORE_EVENTS_START, false, true);
        
        ECollections.sort(fFolder.getElements(), this);
        
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                IEditorModelManager.PROPERTY_ECORE_EVENTS_END, false, true);
    }
    
    @Override
    public void undo() {
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                IEditorModelManager.PROPERTY_ECORE_EVENTS_START, false, true);

        fFolder.getElements().clear();
        fFolder.getElements().addAll(fList);
        
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                IEditorModelManager.PROPERTY_ECORE_EVENTS_END, false, true);
    }
    
    @Override
    public void dispose() {
        fFolder = null;
        fList = null;
    }

    @Override
    public int compare(EObject o1, EObject o2) {
        String name1 = null, name2 = null;
        
        if(o1 instanceof IDiagramModelComponent && o2 instanceof IDiagramModelComponent) {
            name1 = ((IDiagramModelComponent)o1).getName();
            name2 = ((IDiagramModelComponent)o2).getName();
        }
        else if(o1 instanceof IArchimateConcept && o2 instanceof IArchimateConcept) {
            name1 = ((IArchimateConcept)o1).getName();
            name2 = ((IArchimateConcept)o2).getName();
        }
        
        if(name1 == null) {
            name1 = ""; //$NON-NLS-1$
        }
        if(name2 == null) {
            name2 = ""; //$NON-NLS-1$
        }
        
        return name1.compareTo(name2);
    }
}
