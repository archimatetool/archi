/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.ui.IEditorPart;

import uk.ac.bolton.archimate.editor.ui.ArchimateNames;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelGroup;
import uk.ac.bolton.archimate.model.IDiagramModelNote;
import uk.ac.bolton.archimate.model.IRelationship;


/**
 * Diagram Model Factory for creating objects from the Palette in the Diagram Editor
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelFactory implements ICreationFactory {
    
    private EClass fTemplate;
    
    /**
     * Constructor for creating a new Ecore type model
     * @param eClass
     */
    public DiagramModelFactory(EClass template) {
        fTemplate = template;
    }
    
    public boolean isUsedFor(IEditorPart editor) {
        return editor instanceof IDiagramEditor;
    }
    
    public Object getNewObject() {
        if(fTemplate == null) {
            return null;
        }
        
        Object object = IArchimateFactory.eINSTANCE.create(fTemplate);
        
        // Connection created from Relationship Template
        if(object instanceof IRelationship) {
            ((IRelationship)object).setName(ArchimateNames.getDefaultName(fTemplate));
            IDiagramModelArchimateConnection connection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
            connection.setRelationship((IRelationship)object);
            return connection;
        }
        
        // Archimate Diagram Object created from Archimate Element Template
        else if(object instanceof IArchimateElement) {
            ((IArchimateElement)object).setName(ArchimateNames.getDefaultName(fTemplate));
            IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
            dmo.setArchimateElement((IArchimateElement)object);
            return dmo;
        }
        
        // Note
        else if(object instanceof IDiagramModelNote) {
            ((IDiagramModelNote)object).setName("Note");
        }
        
        // Group
        else if(object instanceof IDiagramModelGroup) {
            ((IDiagramModelGroup)object).setName("Group");
        }
        
        // Object
        else if(object instanceof IDiagramModelConnection) {
            ((IDiagramModelConnection)object).setName("Connection");
        }
        
        return object;
    }

    public Object getObjectType() {
        return fTemplate;
    }
}
