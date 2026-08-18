/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProfiles;

/**
 * Set the Primary Profile (Specialization) Command
 * There can be only one Profile that is a Specialization
 * 
 * @author Phillip Beauvoir
 */
public class SetProfileCommand extends Command {
    private IProfiles owner;
    private List<IProfile> oldProfiles;
    private IProfile newProfile;

    public SetProfileCommand(IProfiles owner, IProfile profile) {
        this.owner = owner;
        oldProfiles = new ArrayList<>(owner.getProfiles());
        newProfile = profile;
        setLabel(Messages.SetProfileCommand_0);
    }

    @Override
    public void execute() {
        // Clear all as we are currently making the assumption everywhere that a IProfile is a Specialization
        // And that there is only one of them. Otherwise it would be
        // owner.getProfiles().removeIf(IProfile::isSpecialization);
        owner.getProfiles().clear();
        
        // If newProfile is null then that clears it else add it
        if(newProfile != null) {
            owner.getProfiles().add(newProfile);
        }
    }

    @Override
    public void undo() {
        owner.getProfiles().clear();
        owner.getProfiles().addAll(oldProfiles);
    }

    @Override
    public boolean canExecute() {
        // This first - If the new Profile is null and owner has no Profiles then can't execute
        if(newProfile == null) {
            return !owner.getProfiles().isEmpty();
        }
        
        // If not a Specialization
        if(!newProfile.isSpecialization() || newProfile.getConceptType() == null) {
            return false;
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
        oldProfiles = null;
        newProfile = null;
    }
}
