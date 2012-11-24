/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IEditorPart;

import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.ui.ArchimateLabelProvider;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelGroup;
import uk.ac.bolton.archimate.model.IRelationship;


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
        dmo.setType(Preferences.getDefaultFigureType(dmo));
        
        // Set user fill color
        if(Preferences.STORE.getBoolean(IPreferenceConstants.SAVE_USER_DEFAULT_FILL_COLOR)) {
            Color fillColor = ColorFactory.getDefaultFillColor(dmo);
            if(fillColor != null) {
                dmo.setFillColor(ColorFactory.convertRGBToString(fillColor.getRGB()));
            }
        }
        
        return dmo;
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
        if(object instanceof IRelationship) {
            IDiagramModelArchimateConnection connection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
            connection.setRelationship((IRelationship)object);
            return connection;
        }
        
        // Archimate Diagram Object created from Archimate Element Template
        else if(object instanceof IArchimateElement) {
            IArchimateElement element = (IArchimateElement)object;
            element.setName(ArchimateLabelProvider.INSTANCE.getDefaultName(fTemplate));
            return createDiagramModelArchimateObject(element);
        }
        
        // Group
        else if(object instanceof IDiagramModelGroup) {
            ((IDiagramModelGroup)object).setName(Messages.ArchimateDiagramModelFactory_0);
        }
        
        return object;
    }

    public Object getObjectType() {
        return fTemplate;
    }
}
