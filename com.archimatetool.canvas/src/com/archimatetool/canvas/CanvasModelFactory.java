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
        
        // Sticky
        if(object instanceof ICanvasModelSticky) {
            ICanvasModelSticky sticky = (ICanvasModelSticky)object;
            if(fParam instanceof RGB) {
                String color = ColorFactory.convertRGBToString((RGB)fParam);
                sticky.setFillColor(color);
            }
            sticky.setBorderColor("#C0C0C0"); //$NON-NLS-1$
        }
        
        // Block
        else if(object instanceof ICanvasModelBlock) {
            ICanvasModelBlock block = (ICanvasModelBlock)object;
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
        
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(object);

        if(object instanceof ITextAlignment) {
            ((IDiagramModelObject)object).setTextAlignment(provider.getDefaultTextAlignment());
        }
                
        if(object instanceof ITextPosition) {
            ((ITextPosition)object).setTextPosition(provider.getDefaultTextPosition());
        }
        
        // Add new bounds with a default user size
        if(object instanceof IDiagramModelObject) {
            Dimension size = provider.getDefaultSize();
            ((IDiagramModelObject)object).setBounds(0, 0, size.width, size.height);
        }
        
        return object;
    }

    @Override
    public Object getObjectType() {
        return fTemplate;
    }
}
