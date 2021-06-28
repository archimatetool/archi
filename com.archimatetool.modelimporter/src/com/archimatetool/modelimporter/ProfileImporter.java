/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import java.io.IOException;

import com.archimatetool.editor.model.commands.AddListMemberCommand;
import com.archimatetool.editor.model.commands.RemoveListMemberCommand;
import com.archimatetool.model.IProfile;
import com.archimatetool.modelimporter.StatusMessage.StatusMessageLevel;


/**
 * Archi Profile Importer
 * 
 * @author Phillip Beauvoir
 */
class ProfileImporter extends AbstractImporter {
    
    ProfileImporter(ModelImporter importer) {
        super(importer);
    }
    
    IProfile importProfile(IProfile importedProfile) throws ImportException, IOException {
        // Do we have this profile given its ID?
        IProfile targetProfile = findObjectInTargetModel(importedProfile);
        
        // We don't have it, so lookup by name or create a new Profile
        if(targetProfile == null) {
            // Do we have this profile given its name?
            targetProfile = findProfileInTargetModel(importedProfile);
            
            // We really don't have it, so create a new profile
        	if(targetProfile == null) {
        		addNewProfile(importedProfile);
        	}
        }
        // Else update (note: we update only when matched by ID, not by name)
        // TODO: maybe also update profile when matched by ID as user is not able to distinguish both cases
        else if(shouldUpdate()) {
            updateProfile(importedProfile, targetProfile);
        }
        else {
            logMessage(StatusMessageLevel.INFO, Messages.ProfileImporter_0, importedProfile);
        }
        
        return targetProfile;
    }
    
    private void addNewProfile(IProfile importedProfile) throws IOException {
        // Clone the imported Profile
        IProfile newProfile = cloneObject(importedProfile);

        // Add the clone
        addCommand(new AddListMemberCommand<IProfile>(getTargetModel().getProfiles(), newProfile));
        
        // Import any image bytes
        importImageBytes(importedProfile, newProfile);
        
        // Log
        logMessage(StatusMessageLevel.INFO, Messages.ProfileImporter_1, newProfile);
    }

    /**
     * Update the target profile by cloning the imported Profile, removing the target Profile and adding the clone
     */
    private void updateProfile(IProfile importedProfile, IProfile targetProfile) throws IOException {
        // Clone the imported Profile
        IProfile newProfile = cloneObject(importedProfile);
        
        // Remove the Target Profile
        addCommand(new RemoveListMemberCommand<IProfile>(getTargetModel().getProfiles(), targetProfile));
        
        // Add the clone
        addCommand(new AddListMemberCommand<IProfile>(getTargetModel().getProfiles(), newProfile));
        
        // Import any image bytes
        importImageBytes(importedProfile, newProfile);
        
        // Log
        logMessage(StatusMessageLevel.INFO, Messages.ProfileImporter_2, newProfile);
    }
}
