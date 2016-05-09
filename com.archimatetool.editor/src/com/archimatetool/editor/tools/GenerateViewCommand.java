/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.tools;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.diagram.ArchimateDiagramModelFactory;
import com.archimatetool.editor.model.viewpoints.IViewpoint;
import com.archimatetool.editor.ui.factory.ElementUIFactory;
import com.archimatetool.editor.ui.factory.IElementUIProvider;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;



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
        dm.setViewpoint(fViewpoint.getIndex());
        
        fParentFolder = fSelectedElements.get(0).getArchimateModel().getDefaultFolderForElement(dm);
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
        
        // Add connections
        for(IDiagramModelObject dmoSource : dm.getChildren()) {
            IArchimateElement elementSource = ((IDiagramModelArchimateObject)dmoSource).getArchimateElement();
            for(IRelationship relation : ArchimateModelUtils.getSourceRelationships(elementSource)) {
                for(IDiagramModelObject dmoTarget : dm.getChildren()) {
                    IArchimateElement elementTarget = ((IDiagramModelArchimateObject)dmoTarget).getArchimateElement();
                    // Don't add connections that are not connected to the main elements if option is set
                    if(!fAddAllConnections && !(fSelectedElements.contains(elementSource)) && !(fSelectedElements.contains(elementTarget))) {
                        continue;
                    }
                    if(relation.getTarget() == elementTarget) {
                        IDiagramModelArchimateConnection connection = ArchimateDiagramModelFactory.createDiagramModelArchimateConnection(relation);
                        connection.connect(dmoSource, dmoTarget);
                    }
                }
            }
        }
        
        return dm;
    }
    
    private void createDiagramNode(IArchimateElement element, IArchimateDiagramModel dm, int x, int y) {
        IDiagramModelArchimateObject dmo = ArchimateDiagramModelFactory.createDiagramModelArchimateObject(element);
        dm.getChildren().add(dmo);
        
        // Size
        Dimension defaultSize = getDefaultSizeOfElement(element);
        dmo.setBounds(x, y, defaultSize.width, defaultSize.height);
    }
    
    private void getElementsToAdd() {
        fAddedElements = new ArrayList<IArchimateElement>();
        
        for(IArchimateElement element : fSelectedElements) {
            if(!fAddedElements.contains(element)) {
                fAddedElements.add(element);
            }
            
            // Add connecting target elements
            for(IRelationship relation : ArchimateModelUtils.getSourceRelationships(element)) {
                IArchimateElement target = relation.getTarget();
                if(fViewpoint.isAllowedType(target.eClass()) && !fAddedElements.contains(target)) {
                    fAddedElements.add(target);
                }
            }
            
            // Add connecting source elements
            for(IRelationship relation : ArchimateModelUtils.getTargetRelationships(element)) {
                IArchimateElement source = relation.getSource();
                if(fViewpoint.isAllowedType(source.eClass()) && !fAddedElements.contains(source)) {
                    fAddedElements.add(source);
                }
            }
        }
    }
    
    private Dimension getDefaultSizeOfElement(IArchimateElement element) {
        IElementUIProvider provider = ElementUIFactory.INSTANCE.getProvider(element);
        if(provider != null) {
            return provider.getDefaultSize();
        }
        return new Dimension(120, 55);
    }
}
