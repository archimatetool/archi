/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.templates.model;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.graphics.Image;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import uk.ac.bolton.archimate.canvas.CanvasEditorPlugin;
import uk.ac.bolton.archimate.canvas.ICanvasImages;
import uk.ac.bolton.archimate.editor.ArchimateEditorPlugin;
import uk.ac.bolton.archimate.editor.utils.ZipUtils;
import uk.ac.bolton.archimate.templates.model.ITemplate;
import uk.ac.bolton.archimate.templates.model.ITemplateGroup;
import uk.ac.bolton.archimate.templates.model.ITemplateXMLTags;
import uk.ac.bolton.archimate.templates.model.TemplateGroup;
import uk.ac.bolton.archimate.templates.model.TemplateManager;
import uk.ac.bolton.jdom.JDOMUtils;



/**
 * Template Manager.
 * Users must call dispose() when finished with it if the images in Templates are loaded
 * @author Phillip Beauvoir
 */
public class CanvasTemplateManager extends TemplateManager {
    
    public static final String CANVAS_TEMPLATE_FILE_EXTENSION = ".archicanvas";
    private File fUserTemplatesFile = new File(ArchimateEditorPlugin.INSTANCE.getUserDataFolder(), "canvasses.xml");

    @Override
    protected ITemplateGroup loadInbuiltTemplates() {
        ITemplateGroup group = new TemplateGroup("Installed Templates");
        File folder = CanvasEditorPlugin.INSTANCE.getTemplatesFolder();
        if(folder.exists()) {
            for(File file : folder.listFiles()) {
                if(file.getName().toLowerCase().endsWith(CANVAS_TEMPLATE_FILE_EXTENSION)) {
                    ITemplate template = new CanvasModelTemplate();
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
        return CANVAS_TEMPLATE_FILE_EXTENSION;
    }

    @Override
    public ITemplate createTemplate(File file) throws IOException {
        if(isValidTemplateFile(file)) {
            return new CanvasModelTemplate(null);
        }
        else {
            throw new IOException("Wrong template format.");
        }
    }

    @Override
    protected ITemplate createTemplate(String type) {
        if(CanvasModelTemplate.XML_CANVAS_TEMPLATE_ATTRIBUTE_TYPE_MODEL.equals(type)) {
            return new CanvasModelTemplate();
        }
        return null;
    }
    
    @Override
    public Image getMainImage() {
        return ICanvasImages.ImageFactory.getImage(ICanvasImages.ICON_CANVAS_MODEL_16);
    }
    
    @Override
    protected boolean isValidTemplateFile(File file) throws IOException {
        if(file == null || !file.exists()) {
            return false;
        }
        
        // Ensure the template is of the right kind
        String xmlString = ZipUtils.extractZipEntry(file, ZIP_ENTRY_MANIFEST);
        if(xmlString == null) {
            return false;
        }

        // If the attribute "type" exists then return true if its value is "canvas".
        // If the attribute doesn't exist it was from an older version (before 2.1)
        try {
            Document doc = JDOMUtils.readXMLString(xmlString);
            Element root = doc.getRootElement();
            Attribute attType = root.getAttribute(ITemplateXMLTags.XML_TEMPLATE_ATTRIBUTE_TYPE);
            if(attType != null) {
                return CanvasModelTemplate.XML_CANVAS_TEMPLATE_ATTRIBUTE_TYPE_MODEL.equals(attType.getValue());
            }
        }
        catch(JDOMException ex) {
            return false;
        }
         
        return true;
    }
}
