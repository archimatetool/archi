/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IEditorPart;

import com.archimatetool.canvas.model.ICanvasModelBlock;
import com.archimatetool.canvas.model.ICanvasModelConnection;
import com.archimatetool.canvas.model.ICanvasModelImage;
import com.archimatetool.canvas.model.ICanvasModelSticky;
import com.archimatetool.editor.diagram.ICreationFactory;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;



/**
 * Model Factory for creating objects from the Palette in the Canvas Editor
 * 
 * @author Phillip Beauvoir
 */
public class CanvasModelFactory implements ICreationFactory {
    
    private EClass fTemplate;
    private Object fParam;
    
    public CanvasModelFactory(EClass template) {
        this(template, null);
    }
    
    /**
     * Constructor for creating a new Ecore type model with a parameter
     * @param eClass
     * @param param
     */
    public CanvasModelFactory(EClass template, Object param) {
        fTemplate = template;
        fParam = param;
    }
    
    public boolean isUsedFor(IEditorPart editor) {
        return editor instanceof ICanvasEditor;
    }
    
    public Object getNewObject() {
        // Create the instance from the registered factory in case of extensions
        Object object = fTemplate.getEPackage().getEFactoryInstance().create(fTemplate);
        
        // Sticky
        if(object instanceof ICanvasModelSticky) {
            ICanvasModelSticky sticky = (ICanvasModelSticky)object;
            sticky.setTextPosition(ITextPosition.TEXT_POSITION_CENTRE);
            sticky.setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_CENTER);
            if(fParam instanceof Color) {
                String color = ColorFactory.convertColorToString((Color)fParam);
                sticky.setFillColor(color);
            }
            sticky.setBorderColor("#C0C0C0"); //$NON-NLS-1$
        }
        
        // Block
        else if(object instanceof ICanvasModelBlock) {
            ICanvasModelBlock block = (ICanvasModelBlock)object;
            block.setTextPosition(ITextPosition.TEXT_POSITION_TOP);
            block.setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_LEFT);
            block.setBorderColor("#000000"); //$NON-NLS-1$
        }
        
        // Image
        else if(object instanceof ICanvasModelImage) {
            ICanvasModelImage image = (ICanvasModelImage)object;
            image.setBorderColor("#000000"); //$NON-NLS-1$
        }
        
        // Canvas Connection
        else if(object instanceof ICanvasModelConnection) {
            ICanvasModelConnection connection = (ICanvasModelConnection)object;
            if(fParam instanceof Integer) {
                connection.setType((Integer)fParam);
            }
        }
        
        return object;
    }

    public Object getObjectType() {
        return fTemplate;
    }
}
