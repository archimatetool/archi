/*******************************************************************************
 * Copyright (c) 2010-11 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.templates.impl.model;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.ArchimateEditorPlugin;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.templates.ArchimateEditorTemplatesPlugin;
import uk.ac.bolton.archimate.templates.model.ITemplate;
import uk.ac.bolton.archimate.templates.model.ITemplateGroup;
import uk.ac.bolton.archimate.templates.model.TemplateGroup;
import uk.ac.bolton.archimate.templates.model.TemplateManager;


/**
 * Archimate Template Manager.
 * Users must call dispose() when finished with it if the images in Templates are loaded
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateTemplateManager extends TemplateManager {
    
    public static final String ARCHIMATE_TEMPLATE_FILE_EXTENSION = ".architemplate";
    
    private File fUserTemplatesFile = new File(ArchimateEditorPlugin.INSTANCE.getUserDataFolder(), "templates.xml");
    
    public ArchimateTemplateManager() {
    }
    
    @Override
    protected ITemplateGroup loadInbuiltTemplates() {
        ITemplateGroup group = new TemplateGroup("Installed Templates");
        File folder = ArchimateEditorTemplatesPlugin.INSTANCE.getTemplatesFolder();
        if(folder.exists()) {
            for(File file : folder.listFiles()) {
                if(file.getName().toLowerCase().endsWith(ARCHIMATE_TEMPLATE_FILE_EXTENSION)) {
                    ITemplate template = new ArchimateModelTemplate();
                    template.setFile(file);
                    group.addTemplate(template);
                }
            }
        }
        return group;
    }

    @Override
    public File getUserTemplatesManifestFile() {
        return fUserTemplatesFile;
    }

    @Override
    public String getTemplateFileExtension() {
        return ARCHIMATE_TEMPLATE_FILE_EXTENSION;
    }

    @Override
    public ITemplate createTemplate(File file) throws IOException {
        if(isValidTemplateFile(file)) {
            return new ArchimateModelTemplate(null);
        }
        return null;
    }

    @Override
    protected ITemplate createTemplate(String type) {
        if(ArchimateModelTemplate.XML_TEMPLATE_ATTRIBUTE_TYPE_MODEL.equals(type)) {
            return new ArchimateModelTemplate();
        }
        return null;
    }
    
    @Override
    public Image getMainImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_MODELS_16);
    }
}
