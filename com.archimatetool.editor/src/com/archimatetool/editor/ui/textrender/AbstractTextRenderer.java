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
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.IFolder;

/**
 * Abstract Text Renderer
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public abstract class AbstractTextRenderer implements ITextRenderer {
    
    /**
     * Get the actual object that this represents
     * If it's an IDiagramModelArchimateComponent return the referenced IArchimateConcept
     * If it's an IDiagramModelReference return the referenced IDiagramModel
     * @param object The object
     * @return object itself or the underlying object
     */
    protected IArchimateModelObject getActualObject(IArchimateModelObject object) {
        if(object instanceof IDiagramModelArchimateComponent dmo) {
            return dmo.getArchimateConcept();
        }
        if(object instanceof IDiagramModelReference dmr) {
            return dmr.getReferencedModel();
        }
        return object;
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
        if(viewPrefix.equals(prefix) && object instanceof IDiagramModelComponent dmc) {
            return dmc.getDiagramModel();
        }
        
        // Model Folder
        if(modelFolderPrefix.equals(prefix) && actualObject.eContainer() instanceof IFolder folder) { // Has a folder parent
            return folder;
        }
        
        // View Folder
        if(viewFolderPrefix.equals(prefix) && object instanceof IDiagramModelComponent dmc) {
            IDiagramModel dm = dmc.getDiagramModel();
            return dm != null ? (IArchimateModelObject)dm.eContainer() : null; // folder parent of IDiagramModel
        }
        
        // Parent
        if(parentPrefix.equals(prefix) && object != null) {
            IArchimateModelObject parent = (IArchimateModelObject)object.eContainer();
            return getActualObject(parent);
        }
        
        // Source of Connection
        if(sourcePrefix.equals(prefix) && object instanceof IDiagramModelConnection dmc) {
            return getActualObject(dmc.getSource());
        }
        
        // Target of Connection
        if(targetPrefix.equals(prefix) && object instanceof IDiagramModelConnection dmc) {
            return getActualObject(dmc.getTarget());
        }
        
        // Linked Source object from a connection/relation
        if(prefix.endsWith(":source") && object instanceof IConnectable connectable) {
            prefix = prefix.replace(":source", "");

            // Has at least one source connection that matches...
            for(IDiagramModelConnection connection : connectable.getTargetConnections()) {
                IArchimateModelObject actualConnection = getActualObject(connection); // Could be "connection" or a relationship type
                if(actualConnection.eClass().getName().toLowerCase().contains(prefix)) {
                    return getActualObject(connection.getSource());
                }
            }
            
            // No connection, so has at least one source model relation that matches...
            if(actualObject instanceof IArchimateConcept concept) {
                for(IArchimateRelationship relationship : concept.getTargetRelationships()) {
                    if(relationship.eClass().getName().toLowerCase().contains(prefix)) {
                        return relationship.getSource();
                    }
                }
            }
        }
        // Linked Target object from a connection/relation
        else if(prefix.endsWith(":target") && object instanceof IConnectable connectable) {
            prefix = prefix.replace(":target", "");
            
            // Has at least one target connection that matches...
            for(IDiagramModelConnection connection : connectable.getSourceConnections()) {
                IArchimateModelObject actualConnection = getActualObject(connection); // Could be "connection" or a relationship type
                if(actualConnection.eClass().getName().toLowerCase().contains(prefix)) {
                    return getActualObject(connection.getTarget());
                }
            }
            
            // No connection, so has at least one target model relation that matches...
            if(actualObject instanceof IArchimateConcept concept) {
                for(IArchimateRelationship relationship : concept.getSourceRelationships()) {
                    if(relationship.eClass().getName().toLowerCase().contains(prefix)) {
                        return relationship.getTarget();
                    }
                }
            }
        }

        return null;
    }

}