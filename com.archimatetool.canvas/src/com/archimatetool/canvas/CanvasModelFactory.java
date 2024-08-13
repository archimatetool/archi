/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorPart;

import com.archimatetool.canvas.model.ICanvasModelBlock;
import com.archimatetool.canvas.model.ICanvasModelConnection;
import com.archimatetool.canvas.model.ICanvasModelImage;
import com.archimatetool.canvas.model.ICanvasModelSticky;
import com.archimatetool.editor.diagram.ICreationFactory;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;



/**
 * Model Factory for creating objects from the Palette in the Canvas Editor
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
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
    
    @Override
    public boolean isUsedFor(IEditorPart editor) {
        return editor instanceof ICanvasEditor;
    }
    
    @Override
    public Object getNewObject() {
        // Create the instance from the registered factory in case of extensions
        EObject object = fTemplate.getEPackage().getEFactoryInstance().create(fTemplate);
        
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(object);

        // Sticky
        if(object instanceof ICanvasModelSticky sticky) {
            if(fParam instanceof RGB rgb) {
                // set fill color if not default
                if(!rgb.equals(provider.getDefaultColor().getRGB())) {
                    String color = ColorFactory.convertRGBToString(rgb);
                    sticky.setFillColor(color);
                }
            }
            sticky.setBorderColor("#C0C0C0");
        }
        
        // Block
        else if(object instanceof ICanvasModelBlock block) {
            block.setBorderColor("#000000");
        }
        
        // Image
        else if(object instanceof ICanvasModelImage image) {
            image.setBorderColor("#000000");
        }
        
        // Canvas Connection
        else if(object instanceof ICanvasModelConnection connection) {
            if(fParam instanceof Integer val) {
                connection.setType(val);
            }
        }
        
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
