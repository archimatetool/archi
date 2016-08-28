/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.testingtools;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.commands.CommandStack;

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.model.util.ArchimateResourceFactory;


/**
 * An Archimate Test model
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class ArchimateTestModel {
    
    private File file;
    private IArchimateModel model;
    
    public ArchimateTestModel() {
    }
    
    public ArchimateTestModel(File file) {
        setFile(file);
    }

    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Load the basic Archimate model with no added value of adapters, command stacks etc.
     * @return The model
     * @throws IOException
     */
    public IArchimateModel loadModel() throws IOException {
        if(file == null) {
            throw new IllegalArgumentException("File is null");
        }
        
        if(!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + file);
        }

        Resource resource = ArchimateResourceFactory.createNewResource(file);
        resource.load(null);
        model = (IArchimateModel)resource.getContents().get(0);
        
        // Set file
        model.setFile(file);
        
        return model;
    }
    
    /**
     * Load the Archimate model with a Command Stack added
     * @return The model
     * @throws IOException
     */
    public IArchimateModel loadModelWithCommandStack() throws IOException {
        model = loadModel();
        
        // Add a Command Stack
        CommandStack cmdStack = new CommandStack();
        model.setAdapter(CommandStack.class, cmdStack);
        
        return model;
    }
    
    /**
     * Replica of EditorModelManager#createNewModel()
     * Adds Command Stack and ArchiveManager
     */
    public IArchimateModel createNewModel() {
        model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();

        // Add one default diagram model
        addNewArchimateDiagramModel();
        
        // Add a Command Stack
        CommandStack cmdStack = new CommandStack();
        model.setAdapter(CommandStack.class, cmdStack);
        
        // Add an Archive Manager
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(model);
        model.setAdapter(IArchiveManager.class, archiveManager);

        return model;
    }
    
    /**
     * @return A simple ArchimateModel with no CommandStack or any other BS
     */
    public IArchimateModel createSimpleModel() {
        model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        return model;
    }

    public IArchimateModel getModel() {
        return model;
    }
    
    /**
     * Create and add a new blank ArchimateDiagramModel adding it to its default folder
     */
    public IArchimateDiagramModel addNewArchimateDiagramModel() {
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForObject(dm).getElements().add(dm);
        return dm;
    }
    
    /**
     * Create a DiagramModelArchimateObject and add an Archimate element to it.
     * The Archimate element will be added to its default container folder in the model
     */
    public IDiagramModelArchimateObject createDiagramModelArchimateObjectAndAddToModel(IArchimateElement element) {
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateElement(element);
        if(element.eContainer() == null) {
            model.getArchimateModel().getDefaultFolderForObject(element).getElements().add(element);
        }
        return dmo;
    }
    
    /**
     * Create a DiagramModelArchimateConnection and add an Archimate relationship to it.
     * The relationship will be added to its default container folder in the model
     */
    public IDiagramModelArchimateConnection createDiagramModelArchimateConnectionAndAddToModel(IArchimateRelationship relationship) {
        IDiagramModelArchimateConnection conn = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        conn.setArchimateRelationship(relationship);
        if(relationship.eContainer() == null) {
            model.getArchimateModel().getDefaultFolderForObject(relationship).getElements().add(relationship);
        }
        return conn;
    }
    
    /**
     * Create an Archimate Element or Relationship of type eClass and add it to the model
     * @param eClass The type of Archimate Element or Relationship
     * @return The new object which will have an eContainer folder
     */
    public EObject createModelElementAndAddToModel(EClass eClass) {
        EObject element = IArchimateFactory.eINSTANCE.create(eClass);
        model.getDefaultFolderForObject(element).getElements().add(element);
        return element;
    }

    /**
     * Get an object in the model by its ID
     */
    public EObject getObjectByID(String id) {
        return ArchimateModelUtils.getObjectByID(model, id);
    }
    
    /**
     * If a model is "dirty" this will stop the UI asking to save it
     */
    public void flushCommandStack() {
        CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        if(stack != null) {
            stack.flush();
        }
    }

    
    // ======================================= Factory methods =============================================
    
    /**
     * Create a DiagramModelArchimateObject and add an Archimate element to it
     */
    public static IDiagramModelArchimateObject createDiagramModelArchimateObject(IArchimateElement element) {
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setBounds(0, 0, 120, 55);
        dmo.setArchimateElement(element);
        return dmo;
    }
    
    /**
     * Create a DiagramModelArchimateObject, add an Archimate element to it and add it to a parent container
     */
    public static IDiagramModelArchimateObject createDiagramModelArchimateObjectAndAddToParent(IArchimateElement element, IDiagramModelContainer parent) {
        IDiagramModelArchimateObject dmo = createDiagramModelArchimateObject(element);
        parent.getChildren().add(dmo);
        return dmo;
    }
    
    /**
     * Create a DiagramModelArchimateConnection and add an Archimate relationship to it
     */
    public static IDiagramModelArchimateConnection createDiagramModelArchimateConnection(IArchimateRelationship relationship) {
        IDiagramModelArchimateConnection conn = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        conn.setArchimateRelationship(relationship);
        return conn;
    }
}
