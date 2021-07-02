/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import java.io.IOException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelImageProvider;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.modelimporter.StatusMessage.StatusMessageLevel;

/**
 * Abstract Importer
 * 
 * @author Phillip Beauvoir
 */
abstract class AbstractImporter {
    
    private ModelImporter importer;
    
    protected AbstractImporter(ModelImporter importer) {
        this.importer = importer;
    }
    
    protected boolean shouldUpdate() {
        return importer.shouldUpdate();
    }
    
    protected boolean shouldUpdateAll() {
        return importer.shouldUpdateAll();
    }
    
    protected <T extends IIdentifier> T findObjectInTargetModel(T eObject) throws ImportException {
        return importer.findObjectInTargetModel(eObject);
    }
    
    protected <T extends IArchimateModelObject> T cloneObject(T eObject) {
        return importer.cloneObject(eObject);
    }
    
    protected void updateObject(EObject source, EObject target) {
        importer.updateObject(source, target);
    }
    
    protected IArchimateModel getImportedModel() {
        return importer.getImportedModel();
    }

    protected IArchimateModel getTargetModel() {
        return importer.getTargetModel();
    }
    
    protected void addCommand(Command cmd) {
        importer.addCommand(cmd);
    }
    
    protected void logMessage(StatusMessageLevel level, String message, Object... objs) {
        importer.logMessage(level, message, objs);
    }
    
    /**
     * Add target object to parent folder
     * @param importedObject The imported object
     * @param targetObject The target object
     * @throws ImportException
     */
    protected void addToParentFolder(IArchimateModelObject importedObject, IArchimateModelObject targetObject) throws ImportException {
        // Imported object's parent folder
        IFolder importedParentFolder = (IFolder)importedObject.eContainer();

        // Imported object's parent folder is a User folder
        if(importedParentFolder.getType() == FolderType.USER) {
            // Do we have this matching parent folder?
            IFolder targetParentFolder = importer.findObjectInTargetModel(importedParentFolder);
            // Yes, add the object to it
            if(targetParentFolder != null) {
                addCommand(new AddObjectCommand(targetParentFolder, targetObject));
            }
            // No
            else {
                throw new ImportException("Target parent folder was null"); //$NON-NLS-1$
            }
        }
        // Parent is a top level folder
        else {
            IFolder targetParentFolder = getTargetModel().getDefaultFolderForObject(targetObject);
            addCommand(new AddObjectCommand(targetParentFolder, targetObject));
        }
    }
    
    /**
     * Import the image bytes from the imported model's IDiagramModelImageProvider to the target model's IDiagramModelImageProvider
     */
    protected void importImageBytes(IDiagramModelImageProvider importedObject, IDiagramModelImageProvider targetObject) throws IOException {
        String importedImagePath = importedObject.getImagePath();
        if(importedImagePath != null) {
            IArchiveManager importedArchiveManager = (IArchiveManager)getImportedModel().getAdapter(IArchiveManager.class);
            IArchiveManager targetArchiveManager = (IArchiveManager)getTargetModel().getAdapter(IArchiveManager.class);
            importedImagePath = targetArchiveManager.copyImageBytes(importedArchiveManager, importedImagePath);
            addCommand(new EObjectFeatureCommand(null, targetObject, IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH, importedImagePath));
        }
    }
    
    // ====================================================================================================
    // Commands
    // ====================================================================================================

    private static class AddObjectCommand extends Command {
        private IFolder parent;
        private EObject object;
        IFolder oldParent;
        int oldPosition;

        private AddObjectCommand(IFolder parent, IArchimateModelObject object) {
            this.parent = parent;
            this.object = object;
            oldParent = (IFolder)object.eContainer();
        }
        
        @Override
        public boolean canExecute() {
            return !parent.getElements().contains(object);
        }
        
        @Override
        public void undo() {
            if(oldParent != null) {
                oldParent.getElements().add(oldPosition, object);
            }
            else {
                // If it's an editor, close it first!
                if(object instanceof IDiagramModel) {
                    EditorManager.closeDiagramEditor((IDiagramModel)object);
                }

                parent.getElements().remove(object);
            }
        }

        @Override
        public void execute() {
            if(oldParent != null) {
                oldPosition = oldParent.getElements().indexOf(object);
            }
            
            parent.getElements().add(object);
        }
        
        @Override
        public void dispose() {
            parent = null;
            object = null;
            oldParent = null;
        }
    }
}
