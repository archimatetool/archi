/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.impl.model;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.Logger;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.templates.ArchiTemplatesPlugin;
import com.archimatetool.templates.model.ITemplate;
import com.archimatetool.templates.model.ITemplateGroup;
import com.archimatetool.templates.model.TemplateGroup;
import com.archimatetool.templates.model.TemplateManager;



/**
 * ArchiMate Template Manager.
 * Users must call dispose() when finished with it if the images in Templates are loaded
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateTemplateManager extends TemplateManager {
    
    public static final String ARCHIMATE_TEMPLATE_FILE_EXTENSION = ".architemplate"; //$NON-NLS-1$
    
    public ArchimateTemplateManager() {
    }
    
    @Override
    protected ITemplateGroup loadInbuiltTemplates() {
        ITemplateGroup group = new TemplateGroup(Messages.ArchimateTemplateManager_0);
        File folder = ArchiTemplatesPlugin.getInstance().getTemplatesFolder();
        if(folder.exists()) {
            File[] files = folder.listFiles();
            if(files != null) {
                for(File file : files) {
                    if(file.getName().toLowerCase().endsWith(ARCHIMATE_TEMPLATE_FILE_EXTENSION)) {
                        try {
                            group.addTemplate(createTemplate(file));
                        }
                        catch(IOException ex) {
                            Logger.error("Error loading template", ex); //$NON-NLS-1$
                        }
                    }
                }
            }
        }
        return group;
    }

    @Override
    public File getUserTemplatesManifestFile() {
        return new File(ArchiPlugin.getInstance().getWorkspaceFolder(), "templates.xml"); //$NON-NLS-1$
    }

    @Override
    public String getTemplateFileExtension() {
        return ARCHIMATE_TEMPLATE_FILE_EXTENSION;
    }

    @Override
    public ITemplate createTemplate(File file) throws IOException {
        return new ArchimateModelTemplate(file);
    }

    @Override
    public Image getMainImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_MODELS);
    }
}
