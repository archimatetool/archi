/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.diagram.ArchimateDiagramModelFactory;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.viewpoints.IViewpoint;



/**
 * Generate View Command
 * 
 * @author Phillip Beauvoir
 */
public class GenerateViewCommand extends Command {
    
    private List<IArchimateElement> fSelectedElements;
    private List<IArchimateElement> fAddedElements;
    
    private IFolder fParentFolder;
    private IArchimateDiagramModel fDiagramModel;
    private IViewpoint fViewpoint;
    private boolean fAddAllConnections;
    private String fViewName;
    
    public GenerateViewCommand(List<IArchimateElement> selectedElements) {
        setLabel(Messages.GenerateViewCommand_0);
        fSelectedElements = selectedElements;
    }
    
    @Override
    public void execute() {
        fDiagramModel = createDiagramModel();
        
        // Open Editor
        EditorManager.openDiagramEditor(fDiagramModel);
    }
    
    @Override
    public void undo() {
        // Close the Editor FIRST!
        EditorManager.closeDiagramEditor(fDiagramModel);

        fParentFolder.getElements().remove(fDiagramModel);
    }
    
    @Override
    public void redo() {
        fParentFolder.getElements().add(fDiagramModel);
        
        // Open Editor
        EditorManager.openDiagramEditor(fDiagramModel);
    }
    
    public boolean openDialog(Shell parentShell) {
        GenerateViewDialog dialog = new GenerateViewDialog(parentShell, fSelectedElements);
        
        if(dialog.open() == Window.OK) {
            fViewpoint = dialog.getSelectedViewpoint();
            fAddAllConnections = dialog.isAddAllConnections();
            fViewName = dialog.getViewName();
            return true;
        }
        
        return false;
    }
    
    private IArchimateDiagramModel createDiagramModel() {
        // New Diagram
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        dm.setName(fViewName);
        dm.setViewpoint(fViewpoint.getID());
        
        fParentFolder = fSelectedElements.get(0).getArchimateModel().getDefaultFolderForObject(dm);
        fParentFolder.getElements().add(dm);
        
        int x = 20;
        int y = 20;
        
        getElementsToAdd();
        
        for(IArchimateElement element : fSelectedElements) {
            createDiagramNode(element, dm, x, y);
            y += 100;
        }
        
        x = 170;
        y = 20;

        for(IArchimateElement element : fAddedElements) {
            if(!fSelectedElements.contains(element)) {
                createDiagramNode(element, dm, x, y);
                y += 80;
                
                if(y > 700) {
                    y = 20;
                    x += 150;
                }
            }
        }
        
        // Add connections between elements first
        for(IDiagramModelObject dmoSource : dm.getChildren()) {
            IArchimateElement elementSource = ((IDiagramModelArchimateObject)dmoSource).getArchimateElement();
            
            for(IArchimateRelationship relation : List.copyOf(elementSource.getSourceRelationships())) { // work on a copy of the list
                for(IDiagramModelObject dmoTarget : dm.getChildren()) {
                    IArchimateElement elementTarget = ((IDiagramModelArchimateObject)dmoTarget).getArchimateElement();
                    
                    // Don't add connections that are not connected to the main elements if option is set
                    if(!fAddAllConnections && !(fSelectedElements.contains(elementSource)) && !(fSelectedElements.contains(elementTarget))) {
                        continue;
                    }
                    
                    if(relation.getTarget() == elementTarget) {
                        // Create connection
                        IDiagramModelArchimateConnection newConnection = ArchimateDiagramModelFactory.createDiagramModelArchimateConnection(relation);
                        newConnection.connect(dmoSource, dmoTarget);
                    }
                }
            }
        }
        
        // Add connections to connections
        for(Iterator<EObject> iter1 = dm.eAllContents(); iter1.hasNext();) {
            EObject eObject1 = iter1.next();
            if(eObject1 instanceof IDiagramModelArchimateConnection) {
                IDiagramModelArchimateConnection connection = (IDiagramModelArchimateConnection)eObject1;
                
                for(IDiagramModelObject dmo : dm.getChildren()) {
                    IArchimateElement element = ((IDiagramModelArchimateObject) dmo).getArchimateElement();
                    
                    for(IArchimateRelationship relation : List.copyOf(connection.getArchimateRelationship().getSourceRelationships())) { // work on a copy of the list
                        if(relation.getTarget() == element) {
                            IDiagramModelArchimateConnection newConnection = ArchimateDiagramModelFactory.createDiagramModelArchimateConnection(relation);
                            newConnection.connect(connection, dmo);
                        }
                    }
                    
                    for(IArchimateRelationship relation : List.copyOf(connection.getArchimateRelationship().getTargetRelationships())) { // work on a copy of the list
                        if(relation.getSource() == element) {
                            IDiagramModelArchimateConnection newConnection = ArchimateDiagramModelFactory.createDiagramModelArchimateConnection(relation);
                            newConnection.connect(dmo, connection);
                        }
                    }
                }
            }
        }
       
        return dm;
    }
    
    private void createDiagramNode(IArchimateElement element, IArchimateDiagramModel dm, int x, int y) {
        IDiagramModelArchimateObject dmo = ArchimateDiagramModelFactory.createDiagramModelArchimateObject(element);
        dm.getChildren().add(dmo);
        
        // Location
        dmo.getBounds().setLocation(x, y);
    }
    
    private void getElementsToAdd() {
        fAddedElements = new ArrayList<IArchimateElement>();
        
        for(IArchimateElement element : fSelectedElements) {
            addElement(element);
            
            // Add connecting target elements
            for(IArchimateRelationship relation : element.getSourceRelationships()) {
                addElement(relation.getTarget());
            }
            
            // Add connecting source elements
            for(IArchimateRelationship relation : element.getTargetRelationships()) {
                addElement(relation.getSource());
            }
        }
    }
    
    private void addElement(IArchimateConcept concept) {
        if(concept instanceof IArchimateElement) {
            if(fViewpoint.isAllowedConcept(concept.eClass()) && !fAddedElements.contains(concept)) {
                fAddedElements.add((IArchimateElement)concept);
            }
        }
        else if(concept instanceof IArchimateRelationship) {
            IArchimateConcept source = ((IArchimateRelationship)concept).getSource();
            if(source instanceof IArchimateElement) {
                addElement(source);
            }
            IArchimateConcept target = ((IArchimateRelationship)concept).getTarget();
            if(target instanceof IArchimateElement) {
                addElement(target);
            }
        }
    }
}
