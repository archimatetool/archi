/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.compatibility.handlers;

import java.util.Iterator;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.archimatetool.editor.model.compatibility.CompatibilityHandlerException;
import com.archimatetool.editor.model.compatibility.ICompatibilityHandler;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IJunction;



/**
 *  In Archi (and ModelVersion number) >= 3.0.0 we no longer save default widths and heights as -1, -1
 * 
 * @author Phillip Beauvoir
 */
public class FixDefaultSizesHandler implements ICompatibilityHandler {
    
    @Override
    public void fixCompatibility(Resource resource) throws CompatibilityHandlerException {
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        
        // Check all widths and heights
        if(isVersion(model)) {
            fixMissingWidthAndHeight(model);
        }
    }
    
    boolean isVersion(IArchimateModel model) {
        String version = model.getVersion();
        return version != null && StringUtils.compareVersionNumbers(version, "3.0.0") < 0; //$NON-NLS-1$
    }

    /**
     * Fix missing width and height values
     */
    void fixMissingWidthAndHeight(IArchimateModel model) {
        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            
            // An Image width/height of -1, -1 signified the actual width/height of the image.
            // However, here, Images with -1, -1 would be converted to a default box size of 200, 150. So ignore it.
            if(eObject instanceof IDiagramModelImage) {
                continue;
            }
            
            if(eObject instanceof IDiagramModelObject) {
                IDiagramModelObject dmo = (IDiagramModelObject)eObject;
                Dimension d = getNewSize(dmo);
                IBounds bounds = dmo.getBounds();
                bounds.setWidth(d.width);
                bounds.setHeight(d.height);
            }
        }
    }
    
    /**
     * Get a new size for a diagram object if width or height are not set
     * Child figures will affect the size.
     */
    Dimension getNewSize(IDiagramModelObject dmo) {
        IBounds bounds = dmo.getBounds().getCopy();
        
        if(bounds.getWidth() != -1 && bounds.getHeight() != -1) {
            return new Dimension(bounds.getWidth(), bounds.getHeight());
        }
        
        // Calculate default size based on children
        if(dmo instanceof IDiagramModelContainer && ((IDiagramModelContainer)dmo).getChildren().size() > 0) {
            IDiagramModelContainer container = (IDiagramModelContainer)dmo;
            // Start with zero and build up from that...
            Dimension childrenSize = new Dimension();

            for(IDiagramModelObject child : container.getChildren()) {
                IBounds childbounds = child.getBounds().getCopy();
                Dimension size = getNewSize(child);
                childrenSize.width = Math.max(childbounds.getX() + size.width() + 10, childrenSize.width);
                childrenSize.height = Math.max(childbounds.getY() + size.height() + 10, childrenSize.height);
            }
            
            Dimension defaultSize = getDefaultSize(dmo);
            Dimension newSize = childrenSize.union(defaultSize);
            
            return newSize;
        }
        
        // No children...
        return getDefaultSize(dmo);
    }

    Dimension getDefaultSize(IDiagramModelObject dmo) {
        IBounds bounds = dmo.getBounds();
        if(bounds.getWidth() != -1 && bounds.getHeight() != -1) {
            return new Dimension(bounds.getWidth(), bounds.getHeight());
        }

        // Legacy size of ArchiMate figure
        if(dmo instanceof IDiagramModelArchimateObject) {
            if(!(((IDiagramModelArchimateObject)dmo).getArchimateElement() instanceof IJunction)) {
                return IGraphicalObjectUIProvider.defaultSize();
            }
        }
        
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(dmo);
        return provider != null ? provider.getDefaultSize() : IGraphicalObjectUIProvider.defaultSize();
    }
}
