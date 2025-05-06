/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorPart;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.ICreationFactory;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ISketchModelActor;
import com.archimatetool.model.ISketchModelSticky;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;



/**
 * Sketch Model Factory for creating objects from the Palette in the Sketch Editor
 * 
 * @author Phillip Beauvoir
 */
public class SketchModelFactory implements ICreationFactory {
    
    private EClass fTemplate;
    private Object fParam;
    
    public SketchModelFactory(EClass template) {
        this(template, null);
    }
    
    @Override
    public boolean isUsedFor(IEditorPart editor) {
        return editor instanceof ISketchEditor;
    }
    
    /**
     * Constructor for creating a new Ecore type model with a parameter
     * @param eClass
     * @param param
     */
    public SketchModelFactory(EClass template, Object param) {
        fTemplate = template;
        fParam = param;
    }
    
    @Override
    public Object getNewObject() {
        EObject object = IArchimateFactory.eINSTANCE.create(fTemplate);
        
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(object);

        // Actor
        if(object instanceof ISketchModelActor actor) {
            actor.setName(ArchiLabelProvider.INSTANCE.getDefaultName(fTemplate));
        }
        
        // Sticky
        else if(object instanceof ISketchModelSticky sticky) {
            sticky.setName(ArchiLabelProvider.INSTANCE.getDefaultName(fTemplate));
            
            // Derive element line color
            sticky.setDeriveElementLineColor(ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.DERIVE_ELEMENT_LINE_COLOR));
            
            // Gradient
            sticky.setGradient(ArchiPlugin.getInstance().getPreferenceStore().getInt(IPreferenceConstants.DEFAULT_GRADIENT));

            if(fParam instanceof RGB rgb) {
                // set fill color if not default
                if(!rgb.equals(provider.getDefaultColor().getRGB())) {
                    String color = ColorFactory.convertRGBToString(rgb);
                    sticky.setFillColor(color);
                }
                
                Color lineColor = ColorFactory.getDefaultLineColor(sticky);
                if(lineColor != null) {
                    sticky.setLineColor(ColorFactory.convertColorToString(lineColor));
                }
            }
        }
        
        // Group
        else if(object instanceof IDiagramModelGroup group) {
            group.setName(ArchiLabelProvider.INSTANCE.getDefaultName(fTemplate));
            ColorFactory.setDefaultColors(group);
            // Gradient
            group.setGradient(ArchiPlugin.getInstance().getPreferenceStore().getInt(IPreferenceConstants.DEFAULT_GRADIENT));
        }
        
        // Connection
        else if(object instanceof IDiagramModelConnection connection) {
            if(fParam instanceof Integer i) {
                connection.setType(i);
            }
            
            ColorFactory.setDefaultColors(connection);
        }
        
        if(object instanceof ITextAlignment ta) {
            ta.setTextAlignment(provider.getDefaultTextAlignment());
        }
                
        if(object instanceof ITextPosition tp) {
            tp.setTextPosition(provider.getDefaultTextPosition());
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
