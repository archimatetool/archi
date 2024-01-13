/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import com.archimatetool.editor.model.commands.AddListMemberCommand;
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
    
    IProfile importProfile(IProfile importedProfile) throws ImportException {
        // Do we have this profile given its Name and Class type?
        IProfile targetProfile = findObjectInTargetModel(importedProfile);
        
        // We don't have it, so create a new profile
        if(targetProfile == null) {
            addNewProfile(importedProfile);
        }
        // Else update
        else if(shouldUpdate()) {
            updateProfile(importedProfile, targetProfile);
        }
        else {
            logMessage(StatusMessageLevel.INFO, Messages.ProfileImporter_0, importedProfile);
        }
        
        return targetProfile;
    }
    
    private void addNewProfile(IProfile importedProfile) {
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
     * Update the target profile
     */
    private void updateProfile(IProfile importedProfile, IProfile targetProfile) {
        updateObject(importedProfile, targetProfile);
        
        // Import any image bytes
        importImageBytes(importedProfile, targetProfile);
        
        // Log
        logMessage(StatusMessageLevel.INFO, Messages.ProfileImporter_2, importedProfile);
    }
}
