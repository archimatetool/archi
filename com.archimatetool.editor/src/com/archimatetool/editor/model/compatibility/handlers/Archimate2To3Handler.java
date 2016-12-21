/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.compatibility.handlers;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.archimatetool.editor.model.compatibility.CompatibilityHandlerException;
import com.archimatetool.editor.model.compatibility.ICompatibilityHandler;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFolder;



/**
 *  Convert Archi versions < 4.0.0
 * 
 * @author Phillip Beauvoir
 */
public class Archimate2To3Handler implements ICompatibilityHandler {
    
    @Override
    public void fixCompatibility(Resource resource) throws CompatibilityHandlerException {
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        
        if(!isArchimate2Model(model)) {
            return;
        }
        
        model.setDefaults();
        
        convertFolders(model);
        
        moveElements(model, model.getFolder(FolderType.BUSINESS));
    }
    
    public boolean isArchimate2Model(IArchimateModel model) {
        String version = model.getVersion();
        return version != null && StringUtils.compareVersionNumbers(version, "4.0.0") < 0; //$NON-NLS-1$
    }
    
    private void convertFolders(IArchimateModel model) {
        for(IFolder folder : new ArrayList<IFolder>(model.getFolders())) {
            if("Connectors".equals(folder.getName())) { //$NON-NLS-1$
                convertConnectorsFolder(model, folder);
            }
            else if("Technology".equals(folder.getName())) { //$NON-NLS-1$
                convertTechnologyFolder(folder);
            }
            else if("Derived Relations".equals(folder.getName())) { //$NON-NLS-1$
                convertDerivedFolder(model, folder);
            }
        }
    }
    
    private void convertConnectorsFolder(IArchimateModel model, IFolder connectorsFolder) {
        IFolder otherFolder = model.getFolder(FolderType.OTHER);
        if(otherFolder != null) {
            for(EObject eObject : new ArrayList<EObject>(connectorsFolder.eContents())) {
                otherFolder.getElements().add(eObject);
            }
        }
        
        model.getFolders().remove(connectorsFolder);
    }
    
    private void convertTechnologyFolder(IFolder technologyFolder) {
        technologyFolder.setName("Technology & Physical"); //$NON-NLS-1$
    }
    
    private void convertDerivedFolder(IArchimateModel model, IFolder derivedFolder) {
        IFolder relationsFolder = model.getFolder(FolderType.RELATIONS);
        if(relationsFolder != null) {
            for(EObject eObject : new ArrayList<EObject>(derivedFolder.eContents())) {
                relationsFolder.getElements().add(eObject);
            }
        }
        
        model.getFolders().remove(derivedFolder);
    }
    
    private void moveElements(IArchimateModel model, IFolder folder) {
        if(folder != null) {
            for(EObject eObject : new ArrayList<EObject>(folder.eContents())) {
                // Location
                if(eObject.eClass() == IArchimatePackage.eINSTANCE.getLocation()) {
                    IFolder otherFolder = model.getFolder(FolderType.OTHER);
                    if(otherFolder != null) {
                        otherFolder.getElements().add(eObject);
                    }
                }
                
                // Value / Meaning
                if(eObject.eClass() == IArchimatePackage.eINSTANCE.getMeaning() || eObject.eClass() == IArchimatePackage.eINSTANCE.getValue()) {
                    IFolder motivationFolder = model.getFolder(FolderType.MOTIVATION);
                    if(motivationFolder != null) {
                        motivationFolder.getElements().add(eObject);
                    }
                }

                // Sub-Folder
                if(eObject instanceof IFolder) {
                    moveElements(model, (IFolder)eObject);
                }
            }
        }
    }
}
