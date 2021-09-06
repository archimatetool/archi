/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.commands;

import org.eclipse.gef.commands.Command;

import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProfiles;

/**
 * Set Profile Command
 * 
 * @author Phillip Beauvoir
 */
public class SetProfileCommand extends Command {
    private IProfiles owner;
    private IProfile oldProfile, newProfile;

    public SetProfileCommand(IProfiles owner, IProfile profile) {
        this.owner = owner;
        newProfile = profile;
        setLabel(Messages.SetProfileCommand_0);
    }

    @Override
    public void execute() {
        // Contains no Profiles, so add it
        if(owner.getProfiles().isEmpty()) {
            owner.getProfiles().add(newProfile);
        }
        // Contains at least one Profile, so store old one and set to new one
        else {
            // Store old Profile
            oldProfile = owner.getPrimaryProfile();
            
            // New profile is null so remove Profile
            if(newProfile == null) {
                owner.getProfiles().remove(oldProfile);
            }
            // Set to new Profile
            else {
                owner.getProfiles().set(0, newProfile);
            }
        }
    }

    @Override
    public void undo() {
        // We have an old Profile
        if(oldProfile != null) {
            // If Empty add it
            if(owner.getProfiles().isEmpty()) {
                owner.getProfiles().add(oldProfile);
            }
            // Else set it
            else {
                owner.getProfiles().set(0, oldProfile);
            }
        }
        // Else remove it
        else {
            owner.getProfiles().remove(newProfile);
        }
    }

    @Override
    public boolean canExecute() {
        // This first - If the new Profile is null and owner has no Profiles then can't execute
        if(newProfile == null) {
            return !owner.getProfiles().isEmpty();
        }
        
        // If Profile's concept type doesn't match owner type
        if(!owner.eClass().getName().equals(newProfile.getConceptType())) {
            return false;
        }
        
        // If owner's Primary Profile is already set to this Profile
        if(owner.getPrimaryProfile() == newProfile) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public void dispose() {
        owner = null;
        oldProfile = null;
        newProfile = null;
    }
}
