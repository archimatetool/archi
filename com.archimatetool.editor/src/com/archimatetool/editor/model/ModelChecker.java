/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IIdentifier;


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
        
        // fErrorMessages.addAll(checkFolderStructure()); // not that important
        fErrorMessages.addAll(checkHasIdentifiers());
        fErrorMessages.addAll(checkRelationsHaveElements());
        fErrorMessages.addAll(checkDiagramObjectsReferences());
        
        return fErrorMessages.isEmpty();
    }
    
    public List<String> getErrorMessages() {
        return fErrorMessages;
    }
    
    public void showErrorDialog(Shell shell) {
        if(fErrorMessages == null || fErrorMessages.isEmpty()) {
            return;
        }
        
        String message = Messages.ModelChecker_0 + " " + fModel.getName() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        
        for(String s : fErrorMessages) {
            message += s + "\n"; //$NON-NLS-1$
            logMessage(s);
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
    
    List<String> checkHasIdentifiers() {
        List<String> messages = new ArrayList<String>();
        
        for(Iterator<EObject> iter = fModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IIdentifier && !StringUtils.isSet(((IIdentifier)eObject).getId())) {
                String message = Messages.ModelChecker_10 + " " + ArchiLabelProvider.INSTANCE.getLabel(eObject); //$NON-NLS-1$
                messages.add(message);
            }
        }
        
        return messages;
    }
    
    List<String> checkRelationsHaveElements() {
        List<String> messages = new ArrayList<String>();
        
        for(Iterator<EObject> iter = fModel.getFolder(FolderType.RELATIONS).eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IArchimateRelationship) {
                IArchimateRelationship relation = (IArchimateRelationship)eObject;
                String name = " (" + relation.getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                if(relation.getSource() == null) {
                    String message = Messages.ModelChecker_19 + name;
                    messages.add(message);
                }
                else if(relation.getSource().getArchimateModel() == null) {
                    String message = Messages.ModelChecker_20 + name;
                    messages.add(message);
                }
                if(relation.getTarget() == null) {
                    String message = Messages.ModelChecker_21 + name;
                    messages.add(message);
                }
                else if(relation.getTarget().getArchimateModel() == null) {
                    String message = Messages.ModelChecker_22 + name;
                    messages.add(message);
                }
            }
        }
        
        return messages;
    }
    
    List<String> checkDiagramObjectsReferences() {
        List<String> messages = new ArrayList<String>();
        
        for(Iterator<EObject> iter = fModel.getFolder(FolderType.DIAGRAMS).eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelArchimateObject) {
                IDiagramModelArchimateObject dmo = (IDiagramModelArchimateObject)eObject;
                String name = dmo.getDiagramModel() == null ? Messages.ModelChecker_11 : " '" + dmo.getDiagramModel().getName() + "' (" + dmo.getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                
                IArchimateElement element = dmo.getArchimateElement();
                if(element == null) {
                    messages.add(Messages.ModelChecker_12 + name);
                }
                else if(element.getArchimateModel() == null) {
                    messages.add(Messages.ModelChecker_13 + name);
                }
            }
            if(eObject instanceof IDiagramModelArchimateConnection) {
                IDiagramModelArchimateConnection conn = (IDiagramModelArchimateConnection)eObject;
                String name = conn.getDiagramModel() == null ? Messages.ModelChecker_14 : " '" + conn.getDiagramModel().getName() + "' (" + conn.getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                
                IArchimateRelationship relation = conn.getArchimateRelationship();
                if(relation == null) {
                    messages.add(Messages.ModelChecker_15 + name);
                }
                else {
                    if(relation.getArchimateModel() == null) {
                        messages.add(Messages.ModelChecker_16 + name);
                    }
                    if(relation.getSource() != null && relation.getSource().getArchimateModel() == null) {
                        messages.add(Messages.ModelChecker_17 + name);
                    }
                    if(relation.getTarget() != null && relation.getTarget().getArchimateModel() == null) {
                        messages.add(Messages.ModelChecker_18 + name);
                    }
                }
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
