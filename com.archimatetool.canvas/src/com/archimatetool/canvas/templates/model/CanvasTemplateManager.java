/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.templates.model;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.graphics.Image;

import com.archimatetool.canvas.CanvasEditorPlugin;
import com.archimatetool.canvas.ICanvasImages;
import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.Logger;
import com.archimatetool.templates.model.ITemplate;
import com.archimatetool.templates.model.ITemplateGroup;
import com.archimatetool.templates.model.TemplateGroup;
import com.archimatetool.templates.model.TemplateManager;




/**
 * Template Manager.
 * Users must call dispose() when finished with it if the images in Templates are loaded
 * @author Phillip Beauvoir
 */
public class CanvasTemplateManager extends TemplateManager {
    
    public static final String CANVAS_TEMPLATE_FILE_EXTENSION = ".archicanvas"; //$NON-NLS-1$
    
    @Override
    protected ITemplateGroup loadInbuiltTemplates() {
        ITemplateGroup group = new TemplateGroup(Messages.CanvasTemplateManager_0);
        File folder = CanvasEditorPlugin.getInstance().getTemplatesFolder();
        File[] files = folder.listFiles();
        if(files != null) {
            for(File file : files) {
                if(file.getName().toLowerCase().endsWith(CANVAS_TEMPLATE_FILE_EXTENSION)) {
                    try {
                        group.addTemplate(createTemplate(file));
                    }
                    catch(IOException ex) {
                        Logger.error("Error loading template", ex); //$NON-NLS-1$
                    }
                }
            }
        }
        return group;
    }

    @Override
    public File getUserTemplatesManifestFile() {
        return new File(ArchiPlugin.getInstance().getWorkspaceFolder(), "canvasses.xml"); //$NON-NLS-1$
    }

    @Override
    public String getTemplateFileExtension() {
        return CANVAS_TEMPLATE_FILE_EXTENSION;
    }

    @Override
    public ITemplate createTemplate(File file) throws IOException {
        return new CanvasModelTemplate(file);
    }

    @Override
    public Image getMainImage() {
        return ICanvasImages.ImageFactory.getImage(ICanvasImages.ICON_CANVAS_MODEL);
    }
}
