/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.ui.IEditorPart;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Diagram Model Factory for creating objects from the Palette in the Archimate Diagram Editor
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramModelFactory implements ICreationFactory {
    
    /**
     * Factory method for creating a new IDiagramModelArchimateObject for an IArchimateElement
     * @param element
     * @return a new IDiagramModelArchimateObject
     */
    public static IDiagramModelArchimateObject createDiagramModelArchimateObject(IArchimateElement element) {
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateElement(element);
        // Figure Type
        dmo.setType(Preferences.STORE.getInt(IPreferenceConstants.DEFAULT_FIGURE_PREFIX + element.eClass().getName()));
        
        // Add new bounds with a default size
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(dmo);
        Dimension defaultSize = provider.getDefaultSize();
        dmo.setBounds(0, 0, defaultSize.width, defaultSize.height);

        // Set user default colors as set in prefs
        ColorFactory.setDefaultColors(dmo);
 
        return dmo;
    }

    /**
     * Factory method for creating a new IDiagramModelArchimateConnection for an IRelationship
     * @param element
     * @return a new IDiagramModelArchimateConnection
     */
    public static IDiagramModelArchimateConnection createDiagramModelArchimateConnection(IArchimateRelationship relation) {
        IDiagramModelArchimateConnection connection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        connection.setArchimateRelationship(relation);
        
        // Set user default colors as set in prefs
        ColorFactory.setDefaultColors(connection);
        
        return connection;
    }
    
    private EClass fTemplate;
    
    /**
     * Constructor for creating a new Ecore type model
     * @param eClass
     */
    public ArchimateDiagramModelFactory(EClass template) {
        fTemplate = template;
    }
    
    public boolean isUsedFor(IEditorPart editor) {
        return editor instanceof IArchimateDiagramEditor;
    }
    
    public Object getNewObject() {
        if(fTemplate == null) {
            return null;
        }
        
        Object object = IArchimateFactory.eINSTANCE.create(fTemplate);
        
        // Connection created from Relationship Template
        if(object instanceof IArchimateRelationship) {
            return createDiagramModelArchimateConnection((IArchimateRelationship)object);
        }
        
        // Archimate Diagram Object created from Archimate Element Template
        else if(object instanceof IArchimateElement) {
            IArchimateElement element = (IArchimateElement)object;
            element.setName(ArchiLabelProvider.INSTANCE.getDefaultName(fTemplate));
            return createDiagramModelArchimateObject(element);
        }
        
        // Group
        else if(object instanceof IDiagramModelGroup) {
            IDiagramModelGroup group = (IDiagramModelGroup)object;
            group.setName(ArchiLabelProvider.INSTANCE.getDefaultName(fTemplate));
            ColorFactory.setDefaultColors(group);
        }
        
        // Note
        else if(object instanceof IDiagramModelNote) {
            ColorFactory.setDefaultColors((IDiagramModelObject)object);
        }
        
        // Connection
        else if(object instanceof IDiagramModelConnection) {
            ColorFactory.setDefaultColors((IDiagramModelConnection)object);
        }
                
        return object;
    }

    public Object getObjectType() {
        return fTemplate;
    }
}
