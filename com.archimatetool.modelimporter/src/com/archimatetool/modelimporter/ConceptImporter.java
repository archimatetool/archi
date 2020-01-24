/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import org.eclipse.gef.commands.Command;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.modelimporter.StatusMessage.StatusMessageLevel;


/**
 * Archi Concept Importer
 * 
 * @author Phillip Beauvoir
 */
class ConceptImporter extends AbstractImporter {
    
    ConceptImporter(ModelImporter importer) {
        super(importer);
    }
    
    IArchimateConcept importConcept(IArchimateConcept importedConcept) throws ImportException {
        boolean createdNewConcept = false;
        
        // Do we have this concept given its ID?
        IArchimateConcept targetConcept = findObjectInTargetModel(importedConcept);
        
        // We don't have it, so create a new concept
        if(targetConcept == null) {
            targetConcept = cloneObject(importedConcept);
            createdNewConcept = true;
            logMessage(StatusMessageLevel.INFO, (targetConcept instanceof IArchimateElement)
                    ? Messages.ConceptImporter_0 : Messages.ConceptImporter_1, targetConcept);
        }
        else if(shouldUpdate()) {
            updateObject(importedConcept, targetConcept);
            logMessage(StatusMessageLevel.INFO, (targetConcept instanceof IArchimateElement)
                    ? Messages.ConceptImporter_2 : Messages.ConceptImporter_3, targetConcept);
        }
        
        if(shouldUpdate() || createdNewConcept) {
            // Relationship ends
            if(importedConcept instanceof IArchimateRelationship) {
                setRelationshipEnds((IArchimateRelationship)importedConcept, (IArchimateRelationship)targetConcept);
            }
            
            // Add to parent folder
            addToParentFolder(importedConcept, targetConcept);
        }
        
        return targetConcept;
    }
    
    private void setRelationshipEnds(IArchimateRelationship importedRelationship, IArchimateRelationship targetRelationship) throws ImportException {
        IArchimateConcept source = findObjectInTargetModel(importedRelationship.getSource());
        if(source == null) {
            source = importConcept(importedRelationship.getSource());
        }
        
        IArchimateConcept target = findObjectInTargetModel(importedRelationship.getTarget());
        if(target == null) {
            target = importConcept(importedRelationship.getTarget());
        }
        
        addCommand(new SetRelationshipEndsCommand(targetRelationship, source, target));
    }
    
    // ====================================================================================================
    // Commands
    // ====================================================================================================

    private static class SetRelationshipEndsCommand extends Command {
        private IArchimateRelationship relationship;
        private IArchimateConcept sourceConcept;
        private IArchimateConcept targetConcept;
        private IArchimateConcept oldSourceConcept;
        private IArchimateConcept oldTargetConcept;
        
        private SetRelationshipEndsCommand(IArchimateRelationship relationship, IArchimateConcept sourceConcept, IArchimateConcept targetConcept) {
            this.relationship = relationship;
            this.sourceConcept = sourceConcept;
            this.targetConcept = targetConcept;
            oldSourceConcept = relationship.getSource();
            oldTargetConcept = relationship.getTarget();
        }

        @Override
        public void execute() {
            relationship.setSource(sourceConcept);
            relationship.setTarget(targetConcept);
        }
        
        @Override
        public void undo() {
            relationship.setSource(oldSourceConcept);
            relationship.setTarget(oldTargetConcept);
        }
        
        @Override
        public void dispose() {
            relationship = null;
            sourceConcept = null;
            targetConcept = null;
            oldSourceConcept = null;
            oldTargetConcept = null;
        }
    }
}
