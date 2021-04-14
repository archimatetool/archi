/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProfiles;


/**
 * Check Model for integrity
 * 
 * @author Phillip Beauvoir
 */
public class ModelChecker {

    private IArchimateModel fModel;
    
    private List<String> fErrorMessages;
    
    public ModelChecker(IArchimateModel model) {
        fModel = model;
    }
    
    /**
     * @return True if OK, false if not OK
     */
    public boolean checkAll() {
        fErrorMessages = new ArrayList<String>();
        
        // Instance count map
        Map<IArchimateConcept, Integer> dmcMap = new HashMap<IArchimateConcept, Integer>();
        
        // fErrorMessages.addAll(checkFolderStructure()); // not that important
        
        // Iterate through all objects in the model...
        for(Iterator<EObject> iter = fModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            
            // Identifier
            if(eObject instanceof IIdentifier) {
                fErrorMessages.addAll(checkHasIdentifier((IIdentifier)eObject));
            }
            
            // Relation
            if(eObject instanceof IArchimateRelationship) {
                fErrorMessages.addAll(checkRelationship((IArchimateRelationship)eObject));
            }
            
            // Diagram Model Object
            if(eObject instanceof IDiagramModelArchimateObject) {
                fErrorMessages.addAll(checkDiagramModelArchimateObject((IDiagramModelArchimateObject)eObject));
                incrementInstanceCount((IDiagramModelArchimateComponent)eObject, dmcMap);
            }
            
            // Diagram Model Connection
            if(eObject instanceof IDiagramModelArchimateConnection) {
                fErrorMessages.addAll(checkDiagramModelArchimateConnection((IDiagramModelArchimateConnection)eObject));
                incrementInstanceCount((IDiagramModelArchimateConnection)eObject, dmcMap);
            }
            
            // Folder
            if(eObject instanceof IFolder) {
                fErrorMessages.addAll(checkFolder((IFolder)eObject));
            }
            
            // Profiles
            if(eObject instanceof IProfiles) {
                fErrorMessages.addAll(checkProfiles((IProfiles)eObject));
            }
        }
        
        // Now check Diagram Model Object reference count
        fErrorMessages.addAll(checkDiagramComponentInstanceCount(dmcMap));
        
        return fErrorMessages.isEmpty();
    }
    
    public List<String> getErrorMessages() {
        return fErrorMessages;
    }
    
    public void showErrorDialog(Shell shell) {
        if(fErrorMessages == null || fErrorMessages.isEmpty()) {
            return;
        }
        
        // Log all messages
        for(String s : fErrorMessages) {
            logMessage(s);
        }
        
        // Show some messages to user
        String message = Messages.ModelChecker_0 + " " + fModel.getName() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        int maxNumberOfMessages = Math.min(10, fErrorMessages.size());
        
        for(int i = 0; i < maxNumberOfMessages; i++) {
            message += fErrorMessages.get(i) + "\n"; //$NON-NLS-1$
        }
        
        if(maxNumberOfMessages < fErrorMessages.size()) {
            message += Messages.ModelChecker_26;
        }
        
        MessageDialog.openError(shell, Messages.ModelChecker_1, message);
    }
    
    List<String> checkFolderStructure() {
        List<String> messages = new ArrayList<String>();
        
        if(fModel.getFolder(FolderType.STRATEGY) == null) {
            messages.add(Messages.ModelChecker_23);
        }
        if(fModel.getFolder(FolderType.BUSINESS) == null) {
            messages.add(Messages.ModelChecker_2);
        }
        if(fModel.getFolder(FolderType.APPLICATION) == null) {
            messages.add(Messages.ModelChecker_3);
        }
        if(fModel.getFolder(FolderType.TECHNOLOGY) == null) {
            messages.add(Messages.ModelChecker_4);
        }
        if(fModel.getFolder(FolderType.OTHER) == null) {
            messages.add(Messages.ModelChecker_5);
        }
        if(fModel.getFolder(FolderType.IMPLEMENTATION_MIGRATION) == null) {
            messages.add(Messages.ModelChecker_6);
        }
        if(fModel.getFolder(FolderType.MOTIVATION) == null) {
            messages.add(Messages.ModelChecker_7);
        }
        if(fModel.getFolder(FolderType.RELATIONS) == null) {
            messages.add(Messages.ModelChecker_8);
        }
        if(fModel.getFolder(FolderType.DIAGRAMS) == null) {
            messages.add(Messages.ModelChecker_9);
        }
        
        return messages;
    }
    
    List<String> checkHasIdentifier(IIdentifier eObject) {
        List<String> messages = new ArrayList<String>();
        
        if(!StringUtils.isSet(eObject.getId())) {
            String message = Messages.ModelChecker_10 + " " + ArchiLabelProvider.INSTANCE.getLabel(eObject); //$NON-NLS-1$
            messages.add(message);
        }
        
        return messages;
    }
    
    List<String> checkRelationship(IArchimateRelationship relation) {
        List<String> messages = new ArrayList<String>();
        
        String name = " (" + relation.getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        
        // Source missing
        if(relation.getSource() == null) {
            String message = Messages.ModelChecker_19 + name;
            messages.add(message);
        }
        // Source orphaned from model
        else if(relation.getSource().getArchimateModel() == null) {
            String message = Messages.ModelChecker_20 + name;
            messages.add(message);
        }
        
        // Target missing
        if(relation.getTarget() == null) {
            String message = Messages.ModelChecker_21 + name;
            messages.add(message);
        }
        // Target orphaned from model
        else if(relation.getTarget().getArchimateModel() == null) {
            String message = Messages.ModelChecker_22 + name;
            messages.add(message);
        }
        
        return messages;
    }
    
    List<String> checkDiagramModelArchimateObject(IDiagramModelArchimateObject dmo) {
        List<String> messages = new ArrayList<String>();
        
        String name = dmo.getDiagramModel() == null ? Messages.ModelChecker_11 : " '" + dmo.getDiagramModel().getName() + "' (" + dmo.getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        IArchimateElement element = dmo.getArchimateElement();
        
        // No referenced element
        if(element == null) {
            messages.add(Messages.ModelChecker_12 + name);
        }
        // Orphaned element
        else if(element.getArchimateModel() == null) {
            messages.add(Messages.ModelChecker_13 + name);
        }
        
        return messages;
    }
    
    List<String> checkDiagramModelArchimateConnection(IDiagramModelArchimateConnection connection) {
        List<String> messages = new ArrayList<String>();
        
        String name = connection.getDiagramModel() == null ? Messages.ModelChecker_11 : " '" + connection.getDiagramModel().getName() + "' (" + connection.getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        IArchimateRelationship relation = connection.getArchimateRelationship();
        
        // No referenced relation
        if(relation == null) {
            messages.add(Messages.ModelChecker_15 + name);
        }
        else {
            // Orphaned relation
            if(relation.getArchimateModel() == null) {
                messages.add(Messages.ModelChecker_16 + name);
            }
            // Orphaned relation source
            if(relation.getSource() != null && relation.getSource().getArchimateModel() == null) {
                messages.add(Messages.ModelChecker_17 + name);
            }
            // Orphaned relation target
            if(relation.getTarget() != null && relation.getTarget().getArchimateModel() == null) {
                messages.add(Messages.ModelChecker_18 + name);
            }
            // Relationship ends != connection ends
            if(((IDiagramModelArchimateComponent)connection.getSource()).getArchimateConcept() != relation.getSource()) {
                messages.add(Messages.ModelChecker_14 + name);
            }
            if(((IDiagramModelArchimateComponent)connection.getTarget()).getArchimateConcept() != relation.getTarget()) {
                messages.add(Messages.ModelChecker_27 + name);
            }
        }
        
        return messages;
    }
    
    List<String> checkFolder(IFolder folder) {
        List<String> messages = new ArrayList<String>();
        
        // Only allowed these types in folder's elements list
        for(EObject eObject : folder.getElements()) {
            if(!(eObject instanceof IArchimateConcept || eObject instanceof IDiagramModel)) {
                String name = " (Folder: " + folder.getId() + " Object: " + ((IIdentifier)eObject).getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                messages.add(Messages.ModelChecker_25 + name);
            }
        }
        
        return messages;
    }
    
    List<String> checkProfiles(IProfiles profilesObject) {
        List<String> messages = new ArrayList<String>();
        
        for(IProfile profile : profilesObject.getProfiles()) {
            String name = " " + profile.getId(); //$NON-NLS-1$
            
            // Profile must exist in model
            if(profile.getArchimateModel() == null) {
                messages.add("Profile is orphaned:" + name);
            }
            
            // Profile must have matching concept type
            EClass eClass = profile.getConceptClass(); 
            if(eClass == null || eClass != profilesObject.eClass()) {
                messages.add("Profile has wrong concept type:" + name);
            }
        }
        
        return messages;
    }
    
    /**
     * For each IDiagramModelArchimateComponent encountered increment the instance count
     */
    private void incrementInstanceCount(IDiagramModelArchimateComponent dmc, Map<IArchimateConcept, Integer> map) {
        IArchimateConcept concept = dmc.getArchimateConcept();
        if(concept != null) { // don't want an NPE while checking
            Integer count = map.get(concept);
            map.put(concept, count == null ? 1 : ++count);
        }
    }
    
    /**
     * Check the actual IDiagramModelArchimateComponent instance count against the concept's reported instance count
     */
    private List<String> checkDiagramComponentInstanceCount(Map<IArchimateConcept, Integer> map) {
        List<String> messages = new ArrayList<String>();
        
        // Now check the total count against the reported count of the concept
        for(Entry<IArchimateConcept, Integer> entry : map.entrySet()) {
            IArchimateConcept concept = entry.getKey();
            int count = entry.getValue();
            if(concept.getReferencingDiagramComponents().size() != count) {
                String name = " (" + concept.getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                messages.add(Messages.ModelChecker_24 + name);
            }
        }
        
        return messages;
    }

    void logMessage(String message) {
        String s = "Model Error: ";  //$NON-NLS-1$
        s += fModel.getName();
        
        if(fModel.getFile() != null) {
            s += " [" + fModel.getFile().getAbsolutePath() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        s += " " + message; //$NON-NLS-1$
        
        Logger.logError(s);
    }
}
