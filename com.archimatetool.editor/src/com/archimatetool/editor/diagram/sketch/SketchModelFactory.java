/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IEditorPart;

import com.archimatetool.editor.diagram.ICreationFactory;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.ISketchModelActor;
import com.archimatetool.model.ISketchModelSticky;



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
    
    public Object getNewObject() {
        Object object = IArchimateFactory.eINSTANCE.create(fTemplate);
        
        // Actor
        if(object instanceof ISketchModelActor) {
            ((ISketchModelActor)object).setName(Messages.SketchModelFactory_0);
        }
        
        // Sticky
        else if(object instanceof ISketchModelSticky) {
            ISketchModelSticky sticky = (ISketchModelSticky)object;
            sticky.setName(Messages.SketchModelFactory_1);
            
            if(fParam instanceof Color) {
                String color = ColorFactory.convertColorToString((Color)fParam);
                sticky.setFillColor(color);
                
                Color lineColor = ColorFactory.getDefaultLineColor(sticky);
                if(lineColor != null) {
                    sticky.setLineColor(ColorFactory.convertColorToString(lineColor));
                }
            }
        }
        
        // Group
        else if(object instanceof IDiagramModelGroup) {
            IDiagramModelGroup group = (IDiagramModelGroup)object;
            group.setName(Messages.SketchModelFactory_2);
            ColorFactory.setDefaultColors(group);
        }
        
        // Connection
        else if(object instanceof IDiagramModelConnection) {
            IDiagramModelConnection connection = (IDiagramModelConnection)object;
            
            if(fParam instanceof Integer) {
                connection.setType((Integer)fParam);
            }
            
            ColorFactory.setDefaultColors(connection);
        }
        
        return object;
    }

    public Object getObjectType() {
        return fTemplate;
    }
}
