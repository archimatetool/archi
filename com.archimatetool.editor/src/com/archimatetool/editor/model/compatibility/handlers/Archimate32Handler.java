/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.compatibility.handlers;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.archimatetool.editor.model.compatibility.CompatibilityHandlerException;
import com.archimatetool.editor.model.compatibility.ICompatibilityHandler;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;



/**
 *  ArchiMate 3.2 Model Version 5.0.0
 * 
 * @author Phillip Beauvoir
 */
public class Archimate32Handler implements ICompatibilityHandler {
    
    @Override
    public void fixCompatibility(Resource resource) throws CompatibilityHandlerException {
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        
        // Check some default figures swapped type 1 to 0
        if(isVersion(model)) {
            setDefaultFigures(model);
        }
    }
    
    boolean isVersion(IArchimateModel model) {
        String version = model.getVersion();
        return version != null && StringUtils.compareVersionNumbers(version, "5.0.0") < 0; //$NON-NLS-1$
    }

    void setDefaultFigures(IArchimateModel model) {
        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelArchimateObject) {
                IDiagramModelArchimateObject dmo = (IDiagramModelArchimateObject)eObject;
                IArchimateElement element = dmo.getArchimateElement();
                if(element.eClass() == IArchimatePackage.eINSTANCE.getGrouping()
                        || element.eClass() == IArchimatePackage.eINSTANCE.getApplicationComponent()
                        || element.eClass() == IArchimatePackage.eINSTANCE.getArtifact()
                        || element.eClass() == IArchimatePackage.eINSTANCE.getDevice()
                        || element.eClass() == IArchimatePackage.eINSTANCE.getNode()) {
                    
                    dmo.setType(dmo.getType() ^ 1);
                }
            }
        }
    }
    
}
