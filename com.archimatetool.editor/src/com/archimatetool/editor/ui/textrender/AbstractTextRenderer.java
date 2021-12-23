/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IFolder;

/**
 * Name renderer
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public abstract class AbstractTextRenderer implements ITextRenderer {
    
    /**
     * Get the actual object that this represents
     * If it's an IDiagramModelArchimateComponent return the referenced IArchimateConcept
     * @param object The object
     * @return object itself or the IArchimateConcept
     */
    protected IArchimateModelObject getActualObject(IArchimateModelObject object) {
        return object instanceof IDiagramModelArchimateComponent ? ((IDiagramModelArchimateComponent)object).getArchimateConcept() : object;
    }
    
    /**
     * Get the object referred to by prefix
     * @param object The object
     * @param prefix The prefix
     * @return the referenced object if prefix is set.
     *         Returns the actual object if prefix is null.
     *         Return null if the prefix is not appropriate for the obect.
     */
    protected IArchimateModelObject getObjectFromPrefix(IArchimateModelObject object, String prefix) {
        IArchimateModelObject actualObject = getActualObject(object);

        // No prefix so return actual object
        if(prefix == null) {
            return actualObject;
        }
        
        // Model
        if(modelPrefix.equals(prefix)) {
            return object.getArchimateModel();
        }

        // View - object is an IDiagramModelComponent so return IDiagramModel
        if(viewPrefix.equals(prefix) && object instanceof IDiagramModelComponent) {
            return ((IDiagramModelComponent)object).getDiagramModel();
        }
        
        // Model Folder
        if(modelFolderPrefix.equals(prefix) && actualObject.eContainer() instanceof IFolder) { // Has a folder parent
            return (IFolder)actualObject.eContainer();
        }
        
        // View Folder
        if(viewFolderPrefix.equals(prefix) && object instanceof IDiagramModelComponent) {
            IDiagramModel dm = ((IDiagramModelComponent)object).getDiagramModel();
            return dm != null ? (IArchimateModelObject)dm.eContainer() : null; // folder parent of IDiagramModel
        }
        
        // Parent
        if(parentPrefix.equals(prefix) && object != null) {
            IArchimateModelObject parent = (IArchimateModelObject)object.eContainer();
            return getActualObject(parent);
        }
        
        // Source of Connection
        if(sourcePrefix.equals(prefix) && object instanceof IDiagramModelConnection) {
            return getActualObject(((IDiagramModelConnection)object).getSource());
        }
        
        // Target of Connection
        if(targetPrefix.equals(prefix) && object instanceof IDiagramModelConnection) {
            return getActualObject(((IDiagramModelConnection)object).getTarget());
        }
        
        // Linked Source object from a connection
        if(prefix.endsWith(":source") && object instanceof IConnectable) {
            prefix = prefix.replace(":source", "");

            // Has at least one source connection that matches...
            for(IDiagramModelConnection connection : ((IConnectable)object).getTargetConnections()) {
                IArchimateModelObject actualConnection = getActualObject(connection);
                if(actualConnection.eClass().getName().toLowerCase().contains(prefix)) {
                    return getActualObject(connection.getSource());
                }
            }
        }
        // Linked Target object from a connection
        else if(prefix.endsWith(":target") && object instanceof IConnectable) {
            prefix = prefix.replace(":target", "");

            // Has at least one target connection that matches...
            for(IDiagramModelConnection connection : ((IConnectable)object).getSourceConnections()) {
                IArchimateModelObject actualConnection = getActualObject(connection);
                if(actualConnection.eClass().getName().toLowerCase().contains(prefix)) {
                    return getActualObject(connection.getTarget());
                }
            }
        }
        // Linked model source object from a connection
        else if(prefix.endsWith(":msource") && actualObject instanceof IArchimateConcept) {
            prefix = prefix.replace(":msource", "");
            
            // Has at least one source relation that matches...
            for(IArchimateRelationship r : ((IArchimateConcept)actualObject).getTargetRelationships()) {
                if(r.eClass().getName().toLowerCase().contains(prefix)) {
                    return r.getSource();
                }
            }
        }
        // Linked model target object from a connection
        else if(prefix.endsWith(":mtarget") && actualObject instanceof IArchimateConcept) {
            prefix = prefix.replace(":mtarget", "");
            
                // Has at least one source relation that matches...
            for(IArchimateRelationship r : ((IArchimateConcept)actualObject).getSourceRelationships()) {
                if(r.eClass().getName().toLowerCase().contains(prefix)) {
                    return r.getTarget();
                }
            }
        }

        return null;
    }

}