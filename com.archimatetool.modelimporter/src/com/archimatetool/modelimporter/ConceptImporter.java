/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.archimatetool.editor.Logger;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProfiles;
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
        else {
            logMessage(StatusMessageLevel.INFO, (targetConcept instanceof IArchimateElement)
                    ? Messages.ConceptImporter_4 : Messages.ConceptImporter_5, targetConcept);
        }
        
        if(shouldUpdate() || createdNewConcept) {
            // Relationship ends
            if(importedConcept instanceof IArchimateRelationship) {
                setRelationshipEnds((IArchimateRelationship)importedConcept, (IArchimateRelationship)targetConcept);
            }
            
            // Add to parent folder
            addToParentFolder(importedConcept, targetConcept);

            // Because IArchimateConcept.getProfiles() are references to Profiles in the imported model, these have to be updated to the target model's Profiles
            // Important - this has to called *after* addToParentFolder() because undo() will orphan the concept
            addCommand(new UpdateProfilesCommand(importedConcept, targetConcept));
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
    
    /**
     * Because the imported object.getProfiles() list references Profiles in the imported model we can't just copy them into the target object.
     * So we have to find the corresponding Profiles by their IDs in the target model and add those.
     */
    private class UpdateProfilesCommand extends Command {
        private IProfiles importedObject;
        private IProfiles targetObject;
        private List<IProfile> oldProfiles;
        private List<IProfile> newProfiles;

        private UpdateProfilesCommand(IProfiles importedObject, IProfiles targetObject) {
            this.importedObject = importedObject;
            this.targetObject = targetObject;
            oldProfiles = new ArrayList<>(targetObject.getProfiles());
        }

        @Override
        public void execute() {
            targetObject.getProfiles().clear();
            
            for(IProfile importedProfile : importedObject.getProfiles()) {
                try {
                    // Find Profile in target model by its ID
                    // findObjectInTargetModel() can only be called in execute(), not redo()
                    IProfile targetProfile = findObjectInTargetModel(importedProfile);
                    if(targetProfile != null) {
                        targetObject.getProfiles().add(targetProfile);
                    }
                    else {
                        Logger.logError("Could not get referenced Profile!"); //$NON-NLS-1$
                    }
                }
                catch(ImportException ex) {
                    Logger.logError("Error getting referenced Profile!", ex); //$NON-NLS-1$
                    ex.printStackTrace();
                }
            }
            
            newProfiles = new ArrayList<>(targetObject.getProfiles());
        }

        @Override
        public void undo() {
            targetObject.getProfiles().clear();
            targetObject.getProfiles().addAll(oldProfiles);
        }
        
        @Override
        public void redo() {
            targetObject.getProfiles().clear();
            targetObject.getProfiles().addAll(newProfiles);
        }
        
        @Override
        public void dispose() {
            importedObject = null;
            targetObject = null;
            oldProfiles = null;
            newProfiles = null;
        }
    }

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
