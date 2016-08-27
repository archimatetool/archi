/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.ui.ArchimateLabelProvider;
import com.archimatetool.model.IArchimateComponent;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IRelationship;

/**
 * Command that will create a new ArchiMate Connection from a given CreateConnectionRequest
 * A relationship will also be created, or, if one similar already exists then the user will
 * be asked if they want to re-use that one.
 * 
 * @author Phillip Beauvoir
 */
public class CreateDiagramArchimateConnectionWithDialogCommand extends CreateDiagramConnectionCommand {
    
    // Flag to mark whether a new relationship was created or whether we re-used an existing one
    private boolean fUseExistingRelation;

    public CreateDiagramArchimateConnectionWithDialogCommand(CreateConnectionRequest request) {
        super(request);
    }

    @Override
    public void execute() {
        fUseExistingRelation = checkToReuseExistingRelationship();
        
        super.execute();
        
        // Now add the relationship to the model
        if(!fUseExistingRelation) {
            ((IDiagramModelArchimateConnection)fConnection).addArchimateComponentToModel(null);
        }
    }

    @Override
    public void redo() {
        super.redo();
        
        // Now add the relationship to the model if we haven't re-used existing one
        if(!fUseExistingRelation) {
            ((IDiagramModelArchimateConnection)fConnection).addArchimateComponentToModel(null);
        }
    }
    
    @Override
    public void undo() {
        super.undo();
        
        // Now remove the relationship from its folder if we haven't re-used existing one
        if(!fUseExistingRelation) {
            ((IDiagramModelArchimateConnection)fConnection).removeArchimateComponentFromModel();
        }
    }
    
    boolean checkToReuseExistingRelationship() {
        EClass classType = (EClass)fRequest.getNewObjectType();
        
        // TODO: A3 check connection-connection rules?
        if(fSource instanceof IDiagramModelArchimateComponent && fTarget instanceof IDiagramModelArchimateComponent) {
            IDiagramModelArchimateComponent source = (IDiagramModelArchimateComponent)fSource;
            IDiagramModelArchimateComponent target = (IDiagramModelArchimateComponent)fTarget;

            // If there is already a relation of this type in the model...
            IRelationship relation = getExistingRelationshipOfType(classType, source.getArchimateComponent(), target.getArchimateComponent());
            if(relation != null) {
                // ...then ask the user if they want to re-use it
                boolean answer = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                        Messages.CreateArchimateConnectionWithDialogCommand_0,
                        NLS.bind(Messages.CreateArchimateConnectionWithDialogCommand_1,
                                ArchimateLabelProvider.INSTANCE.getLabel(source), ArchimateLabelProvider.INSTANCE.getLabel(target)));
                
                // Yes...
                if(answer) {
                     // ...set connection's relationship to the existing relation
                    fConnection = createNewConnection();
                    ((IDiagramModelArchimateConnection)fConnection).setRelationship(relation);
                    return true;
                }
            }
        }
        
        return false;
    }

    /**
     * Swap Source and Target Elements - used by Magic Connector
     */
    public void swapSourceAndTargetElements() {
        IConnectable tmp = fSource;
        fSource = fTarget;
        fTarget = tmp;
    }
    
    /**
     * See if there is an existing relationship of the proposed type between source and target elements.
     * If there is, we can offer to re-use it instead of creating a new one.
     * @return an existing relationship or null
     */
    IRelationship getExistingRelationshipOfType(EClass classType, IArchimateComponent source, IArchimateComponent target) {
        for(IRelationship relation : source.getSourceRelationships()) {
            if(relation.eClass().equals(classType) && relation.getTarget() == target) {
                return relation;
            }
        }
        return null;
    }
}
