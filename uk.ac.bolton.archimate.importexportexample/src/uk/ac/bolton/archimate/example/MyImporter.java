/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.example;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.model.IModelImporter;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IFolder;
import uk.ac.bolton.archimate.model.IRelationship;


/**
 * Example Model Importer
 * 
 * @author Phillip Beauvoir
 */
public class MyImporter implements IModelImporter {

    String MY_EXTENSION_WILDCARD = "*.mex"; //$NON-NLS-1$

    @Override
    public void doImport() throws IOException {
        File file = askOpenFile();
        if(file == null) {
            return;
        }
        
        // Load in the file and get its information here.
        // Assuming you load in the data in some way, perhaps with JDOM, or a SAX Parser
        // Then you will have a representation of it in memory
        // Which you need to map to Archi elements...
        
        // If successful create a new Archimate Model and set defaults
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        model.setName(Messages.MyImporter_0);
        
        // Create and add elements matching imported data
        IArchimateElement actor = IArchimateFactory.eINSTANCE.createBusinessActor();
        actor.setName("Actor"); //$NON-NLS-1$
        IFolder folder = model.getDefaultFolderForElement(actor);
        folder.getElements().add(actor);
        
        IArchimateElement role = IArchimateFactory.eINSTANCE.createBusinessRole();
        role.setName("Role"); //$NON-NLS-1$
        folder = model.getDefaultFolderForElement(role);
        folder.getElements().add(role);
        
        // Create a relationship
        IRelationship relationship = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        relationship.setSource(actor);
        relationship.setTarget(role);
        folder = model.getDefaultFolderForElement(relationship);
        folder.getElements().add(relationship);
        
        // Add a diagram view
        IDiagramModel diagramModel = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        diagramModel.setName("A view"); //$NON-NLS-1$
        folder = model.getDefaultFolderForElement(diagramModel);
        folder.getElements().add(diagramModel);

        // Add elements to view
        IDiagramModelArchimateObject source = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        source.setArchimateElement(actor);
        source.setBounds(10, 10, -1, -1);
        diagramModel.getChildren().add(source);
        
        IDiagramModelArchimateObject target = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        target.setArchimateElement(role);
        target.setBounds(210, 10, -1, -1);
        diagramModel.getChildren().add(target);
        
        // Add connection to view
        IDiagramModelArchimateConnection dmc = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        dmc.setRelationship(relationship);
        dmc.connect(source, target);
        
        // And open it
        IEditorModelManager.INSTANCE.openModel(model);
    }

    private File askOpenFile() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { MY_EXTENSION_WILDCARD, "*.*" } ); //$NON-NLS-1$
        String path = dialog.open();
        return path != null ? new File(path) : null;
    }
}
