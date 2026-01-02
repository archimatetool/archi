/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.dialog.ErrorMessageDialog;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
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

    // If this is set in Program arguments then don't model check
    private static boolean NO_MODELCHECK = Arrays.asList(Platform.getApplicationArgs()).contains("-noModelCheck"); //$NON-NLS-1$

    protected IArchimateModel model;
    protected List<String> errorMessages;
    
    public ModelChecker(IArchimateModel model) {
        this.model = model;
    }
    
    /**
     * @return True if OK, false if not OK
     */
    public boolean checkAll() {
        errorMessages = new ArrayList<>();
        
        // Don't model check
        if(NO_MODELCHECK) {
            return true;
        }
        
        // Instance count map
        Map<IArchimateConcept, Integer> dmcMap = new HashMap<>();
        
        // Model ID
        errorMessages.addAll(checkHasIdentifier(model));
        
        // not that important
        // addErrorMessages(checkFolderStructure());
        
        // Iterate through all child objects in the model...
        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            
            // Identifier
            if(eObject instanceof IIdentifier identifier) {
                errorMessages.addAll(checkHasIdentifier(identifier));
            }
            
            // Relation
            if(eObject instanceof IArchimateRelationship relationship) {
                errorMessages.addAll(checkRelationship(relationship));
            }
            
            // Diagram Model ArchiMate Object
            if(eObject instanceof IDiagramModelArchimateObject dmo) {
                errorMessages.addAll(checkDiagramModelArchimateObject(dmo));
                incrementInstanceCount(dmo, dmcMap);
            }
            
            // Diagram Model Connection
            if(eObject instanceof IDiagramModelConnection dmc) {
                errorMessages.addAll(checkDiagramModelConnection(dmc));
            }

            // Diagram Model ArchiMate Connection
            if(eObject instanceof IDiagramModelArchimateConnection dmc) {
                errorMessages.addAll(checkDiagramModelArchimateConnection(dmc));
                incrementInstanceCount(dmc, dmcMap);
            }
            
            // Concept or Diagram is in correct Folder
            if(eObject instanceof IArchimateConcept || eObject instanceof IDiagramModel) {
                errorMessages.addAll(checkObjectInCorrectFolder((IArchimateModelObject)eObject));
            }
            
            // Folders contain correct objects
            if(eObject instanceof IFolder folder) {
                errorMessages.addAll(checkFolderContainsCorrectObjects(folder));
            }
            
            // Profiles
            if(eObject instanceof IProfiles profiles) {
                errorMessages.addAll(checkProfiles(profiles));
            }
            
            // Extension
            errorMessages.addAll(checkObject(eObject));
        }
        
        // Now check Diagram Model Object reference count
        errorMessages.addAll(checkDiagramComponentInstanceCount(dmcMap));
        
        return errorMessages.isEmpty();
    }
    
    public List<String> getErrorMessages() {
        return errorMessages;
    }
    
    public void showErrorDialog(Shell shell) {
        String summary = createMessageSummary();
        if(summary != null) {
            // Log these messages to log file
            logErrorMesssages();
            
            // Show dialog
            ErrorMessageDialog.open(shell, Messages.ModelChecker_1,
                    Messages.ModelChecker_0 + " " + model.getName(), //$NON-NLS-1$
                    summary);
        }
    }
    
    public String createMessageSummary() {
        if(errorMessages == null || errorMessages.isEmpty()) {
            return null;
        }
        
        // Sort messages
        errorMessages.sort(null);
        
        StringBuilder sb = new StringBuilder();
        
        for(String message : errorMessages) {
            sb.append(message);
            sb.append('\n');
        }
        
        return sb.toString();
    }
    
    public void logErrorMesssages() {
        if(errorMessages == null || errorMessages.isEmpty()) {
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("Model Error: "); //$NON-NLS-1$
        sb.append(model.getName());
        
        if(model.getFile() != null) {
            sb.append(" ["); //$NON-NLS-1$
            sb.append(model.getFile().getAbsolutePath());
            sb.append("]"); //$NON-NLS-1$
        }
        
        sb.append(':');
        sb.append('\n');
        
        for(String message : errorMessages) {
            sb.append("   "); //$NON-NLS-1$
            sb.append(message);
            sb.append('\n');
        }
        
        Logger.error(sb.toString());
    }

    protected List<String> checkFolderStructure() {
        List<String> messages = new ArrayList<>();
        
        if(model.getFolder(FolderType.STRATEGY) == null) {
            messages.add(Messages.ModelChecker_23);
        }
        if(model.getFolder(FolderType.BUSINESS) == null) {
            messages.add(Messages.ModelChecker_2);
        }
        if(model.getFolder(FolderType.APPLICATION) == null) {
            messages.add(Messages.ModelChecker_3);
        }
        if(model.getFolder(FolderType.TECHNOLOGY) == null) {
            messages.add(Messages.ModelChecker_4);
        }
        if(model.getFolder(FolderType.OTHER) == null) {
            messages.add(Messages.ModelChecker_5);
        }
        if(model.getFolder(FolderType.IMPLEMENTATION_MIGRATION) == null) {
            messages.add(Messages.ModelChecker_6);
        }
        if(model.getFolder(FolderType.MOTIVATION) == null) {
            messages.add(Messages.ModelChecker_7);
        }
        if(model.getFolder(FolderType.RELATIONS) == null) {
            messages.add(Messages.ModelChecker_8);
        }
        if(model.getFolder(FolderType.DIAGRAMS) == null) {
            messages.add(Messages.ModelChecker_9);
        }
        
        return messages;
    }
    
    protected List<String> checkHasIdentifier(IIdentifier eObject) {
        return StringUtils.isSet(eObject.getId()) ? List.of() :
                         List.of(NLS.bind(Messages.ModelChecker_10, ArchiLabelProvider.INSTANCE.getLabel(eObject)));
    }
    
    protected List<String> checkRelationship(IArchimateRelationship relation) {
        List<String> messages = new ArrayList<>();
        
        String name = "(" + relation.getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        
        // Source missing
        if(relation.getSource() == null) {
            messages.add(NLS.bind(Messages.ModelChecker_19, name));
        }
        // Source orphaned from model
        else if(relation.getSource().getArchimateModel() == null) {
            messages.add(NLS.bind(Messages.ModelChecker_20, name));
        }
        
        // Target missing
        if(relation.getTarget() == null) {
            messages.add(NLS.bind(Messages.ModelChecker_21, name));
        }
        // Target orphaned from model
        else if(relation.getTarget().getArchimateModel() == null) {
            messages.add(NLS.bind(Messages.ModelChecker_22, name));
        }
        
        return messages;
    }
    
    protected List<String> checkDiagramModelArchimateObject(IDiagramModelArchimateObject dmo) {
        String name = dmo.getDiagramModel() == null ? Messages.ModelChecker_11 : "'" + dmo.getDiagramModel().getName() + "' (" + dmo.getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        IArchimateElement element = dmo.getArchimateElement();
        
        // No referenced element
        if(element == null) {
            return List.of(NLS.bind(Messages.ModelChecker_12, name));
        }
        
        // Orphaned element
        if(element.getArchimateModel() == null) {
            return List.of(NLS.bind(Messages.ModelChecker_13, name));
        }
        
        return List.of();
    }
    
    protected List<String> checkDiagramModelConnection(IDiagramModelConnection connection) {
        List<String> messages = new ArrayList<>();
        
        String name = connection.getDiagramModel() == null ? Messages.ModelChecker_11 : "'" + connection.getDiagramModel().getName() + "' (" + connection.getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        // Connection source is null
        if(connection.getSource() == null) {
            messages.add(NLS.bind(Messages.ModelChecker_30, name));
        }
        
        // Connection target is null
        if(connection.getTarget() == null) {
            messages.add(NLS.bind(Messages.ModelChecker_31, name));
        }

        return messages;
    }
    
    protected List<String> checkDiagramModelArchimateConnection(IDiagramModelArchimateConnection connection) {
        List<String> messages = new ArrayList<>();
        
        String name = connection.getDiagramModel() == null ? Messages.ModelChecker_11 : "'" + connection.getDiagramModel().getName() + "' (" + connection.getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        IArchimateRelationship relation = connection.getArchimateRelationship();
        
        // No referenced relation
        if(relation == null) {
            messages.add(NLS.bind(Messages.ModelChecker_15, name));
        }
        else {
            // Orphaned relation
            if(relation.getArchimateModel() == null) {
                messages.add(NLS.bind(Messages.ModelChecker_16, name));
            }
            
            // Orphaned relation source
            if(relation.getSource() != null && relation.getSource().getArchimateModel() == null) {
                messages.add(NLS.bind(Messages.ModelChecker_17, name));
            }
            // Orphaned relation target
            if(relation.getTarget() != null && relation.getTarget().getArchimateModel() == null) {
                messages.add(NLS.bind(Messages.ModelChecker_18, name));
            }
            
            // Connection source end != relation source end
            if(connection.getSource() instanceof IDiagramModelArchimateComponent dmcSource
                                      && dmcSource.getArchimateConcept() != relation.getSource()) {
                messages.add(NLS.bind(Messages.ModelChecker_14, name));
            }
            // Connection target end != relation target end
            if(connection.getTarget() instanceof IDiagramModelArchimateComponent dmcTarget
                                      && dmcTarget.getArchimateConcept() != relation.getTarget()) {
                messages.add(NLS.bind(Messages.ModelChecker_14, name));
            }
        }
        
        return messages;
    }
    
    protected List<String> checkObjectInCorrectFolder(IArchimateModelObject object) {
        if(!(object.eContainer() instanceof IFolder folder)) {
            return List.of();
        }
        
        IFolder topFolder = folder.getArchimateModel().getDefaultFolderForObject(object);
        
        if(folder == topFolder) {
            return List.of();
        }
        
        EObject e = folder;
        while((e = e.eContainer()) != null) {
            if(e == topFolder) {
                return List.of();
            }
        }
        
        return List.of(NLS.bind(Messages.ModelChecker_26, new String[] {object.getName(), object.getId(), folder.getName()}));
    }
    
    protected List<String> checkFolderContainsCorrectObjects(IFolder folder) {
        List<String> messages = new ArrayList<>();
        
        // Only allowed these types in folder's elements list
        for(EObject eObject : folder.getElements()) {
            if(!(eObject instanceof IArchimateConcept || eObject instanceof IDiagramModel)) {
                String name = "(Folder: " + folder.getId() + " Object: " + ((IIdentifier)eObject).getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                messages.add(NLS.bind(Messages.ModelChecker_25, name));
            }
        }
        
        return messages;
    }
    
    protected List<String> checkProfiles(IProfiles profilesObject) {
        List<String> messages = new ArrayList<>();
        
        for(IProfile profile : profilesObject.getProfiles()) {
            // Profile must exist in this model
            if(profile.getArchimateModel() != ((IArchimateModelObject)profilesObject).getArchimateModel()) {
                messages.add(NLS.bind(Messages.ModelChecker_28, profile.getId()));
            }
            
            // Profile must have matching concept type
            EClass eClass = profile.getConceptClass(); 
            if(eClass == null || eClass != profilesObject.eClass()) {
                messages.add(NLS.bind(Messages.ModelChecker_29, profile.getId()));
            }
        }
        
        return messages;
    }
    
    /**
     * Sub-classes can add a check by over-riding this
     * @param eObject The object in the model to check
     * @return an array of error messages which can be empty
     */
    protected List<String> checkObject(EObject eObject) {
        return List.of();
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
        List<String> messages = new ArrayList<>();
        
        // Now check the total count against the reported count of the concept
        for(Entry<IArchimateConcept, Integer> entry : map.entrySet()) {
            IArchimateConcept concept = entry.getKey();
            int count = entry.getValue();
            if(concept.getReferencingDiagramComponents().size() != count) {
                String name = "(" + concept.getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                messages.add(NLS.bind(Messages.ModelChecker_24, name));
            }
        }
        
        return messages;
    }
}
