/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.templates.model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.eclipse.swt.graphics.Image;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.archimatetool.canvas.CanvasEditorPlugin;
import com.archimatetool.canvas.ICanvasImages;
import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.utils.ZipUtils;
import com.archimatetool.jdom.JDOMUtils;
import com.archimatetool.templates.model.ITemplate;
import com.archimatetool.templates.model.ITemplateGroup;
import com.archimatetool.templates.model.ITemplateXMLTags;
import com.archimatetool.templates.model.TemplateGroup;
import com.archimatetool.templates.model.TemplateManager;




/**
 * Template Manager.
 * Users must call dispose() when finished with it if the images in Templates are loaded
 * @author Phillip Beauvoir
 */
public class CanvasTemplateManager extends TemplateManager {
    
    public static final String CANVAS_TEMPLATE_FILE_EXTENSION = ".archicanvas"; //$NON-NLS-1$
    private File fUserTemplatesFile = new File(ArchiPlugin.INSTANCE.getUserDataFolder(), "canvasses.xml"); //$NON-NLS-1$

    @Override
    protected ITemplateGroup loadInbuiltTemplates() {
        ITemplateGroup group = new TemplateGroup(Messages.CanvasTemplateManager_0);
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
            throw new IOException("Wrong template format."); //$NON-NLS-1$
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
        return ICanvasImages.ImageFactory.getImage(ICanvasImages.ICON_CANVAS_MODEL);
    }
    
    @Override
    protected boolean isValidTemplateFile(File file) throws IOException {
        if(file == null || !file.exists()) {
            return false;
        }
        
        // Ensure the template is of the right kind
        String xmlString = ZipUtils.extractZipEntry(file, ZIP_ENTRY_MANIFEST, Charset.forName("UTF-8")); //$NON-NLS-1$
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
