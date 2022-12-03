/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.compatibility.handlers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.archimatetool.editor.model.compatibility.CompatibilityHandlerException;
import com.archimatetool.editor.model.compatibility.ICompatibilityHandler;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
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
        Set<EClass> set = new HashSet<>();
        set.add(IArchimatePackage.eINSTANCE.getGrouping());
        set.add(IArchimatePackage.eINSTANCE.getBusinessObject());
        set.add(IArchimatePackage.eINSTANCE.getContract());
        set.add(IArchimatePackage.eINSTANCE.getRepresentation());
        set.add(IArchimatePackage.eINSTANCE.getProduct());
        set.add(IArchimatePackage.eINSTANCE.getDataObject());
        set.add(IArchimatePackage.eINSTANCE.getMeaning());
        set.add(IArchimatePackage.eINSTANCE.getValue());
        set.add(IArchimatePackage.eINSTANCE.getDeliverable());
        set.add(IArchimatePackage.eINSTANCE.getApplicationComponent());
        set.add(IArchimatePackage.eINSTANCE.getArtifact());
        set.add(IArchimatePackage.eINSTANCE.getDevice());
        set.add(IArchimatePackage.eINSTANCE.getNode());
        
        for(Iterator<EObject> iter = model.getFolder(FolderType.DIAGRAMS).eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelArchimateObject) {
                IDiagramModelArchimateObject dmo = (IDiagramModelArchimateObject)eObject;
                IArchimateElement element = dmo.getArchimateElement();
                if(set.contains(element.eClass())) {
                    dmo.setType(dmo.getType() ^ 1);
                }
            }
        }
    }
}
