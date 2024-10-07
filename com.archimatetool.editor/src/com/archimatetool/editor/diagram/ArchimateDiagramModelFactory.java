/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IEditorPart;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;
import com.archimatetool.model.impl.Grouping;



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
        dmo.setType(ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.DEFAULT_FIGURE_PREFIX + element.eClass().getName()));
        
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(dmo);
        
        // Add new bounds with a default user size
        Dimension size = provider.getDefaultSize();
        dmo.setBounds(0, 0, size.width, size.height);
        
        // Text position and alignment
        dmo.setTextPosition(provider.getDefaultTextPosition());
        dmo.setTextAlignment(provider.getDefaultTextAlignment());

        // Set user default colors as set in prefs
        ColorFactory.setDefaultColors(dmo);
        
        // Gradient
        dmo.setGradient(ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.DEFAULT_GRADIENT));
        
        // Line Style for Grouping
        if(element instanceof Grouping) {
            dmo.setLineStyle(IDiagramModelObject.LINE_STYLE_DASHED);
        }
        
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
        
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(connection);
        
        // Text alignment
        connection.setTextAlignment(provider.getDefaultTextAlignment());
        
        // Set user default colors as set in prefs
        ColorFactory.setDefaultColors(connection);
        
        return connection;
    }
    
    private EClass fTemplate;
    private IProfile fProfile;
    
    /**
     * Constructor for creating a new Ecore type model
     * @param eClass
     */
    public ArchimateDiagramModelFactory(EClass template) {
        fTemplate = template;
    }
    
    public ArchimateDiagramModelFactory(EClass template, IProfile profile) {
        fTemplate = template;
        fProfile = profile;
    }

    @Override
    public boolean isUsedFor(IEditorPart editor) {
        return editor instanceof IArchimateDiagramEditor;
    }
    
    @Override
    public Object getNewObject() {
        if(fTemplate == null) {
            return null;
        }
        
        boolean isSpecialization = fProfile != null && fProfile.getArchimateModel() != null;
        
        EObject object = IArchimateFactory.eINSTANCE.create(fTemplate);
        
        // Add Profile to Concept if set
        if(object instanceof IArchimateConcept concept && isSpecialization) {
            concept.getProfiles().add(fProfile);
        }
        
        // Archimate Connection created from Relationship Template
        if(object instanceof IArchimateRelationship relationship) {
            return createDiagramModelArchimateConnection(relationship);
        }
        
        // Archimate Diagram Object created from Archimate Element Template
        else if(object instanceof IArchimateElement element) {
            element.setName(isSpecialization ? fProfile.getName() : ArchiLabelProvider.INSTANCE.getDefaultName(fTemplate));
            return createDiagramModelArchimateObject(element);
        }
        
        // Group
        else if(object instanceof IDiagramModelGroup group) {
            group.setName(ArchiLabelProvider.INSTANCE.getDefaultName(fTemplate));
            ColorFactory.setDefaultColors(group);
            group.setGradient(ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.DEFAULT_GRADIENT));
        }
        
        // Note
        else if(object instanceof IDiagramModelNote note) {
            ColorFactory.setDefaultColors(note);
            note.setGradient(ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.DEFAULT_GRADIENT));
        }
        
        // Connection
        else if(object instanceof IDiagramModelConnection connection) {
            ColorFactory.setDefaultColors(connection);
        }
        
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(object);

        if(object instanceof ITextAlignment textAlignment) {
            textAlignment.setTextAlignment(provider.getDefaultTextAlignment());
        }
                
        if(object instanceof ITextPosition textPosition) {
            textPosition.setTextPosition(provider.getDefaultTextPosition());
        }
        
        // Add new bounds with a default user size
        if(object instanceof IDiagramModelObject dmo) {
            Dimension size = provider.getDefaultSize();
            dmo.setBounds(0, 0, size.width, size.height);
        }

        return object;
    }

    @Override
    public Object getObjectType() {
        return fTemplate;
    }
}
