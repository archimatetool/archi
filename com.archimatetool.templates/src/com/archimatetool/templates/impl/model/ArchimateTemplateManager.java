/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.impl.model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.eclipse.swt.graphics.Image;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.utils.ZipUtils;
import com.archimatetool.jdom.JDOMUtils;
import com.archimatetool.templates.ArchiTemplatesPlugin;
import com.archimatetool.templates.model.ITemplate;
import com.archimatetool.templates.model.ITemplateGroup;
import com.archimatetool.templates.model.ITemplateXMLTags;
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
    
    private File fUserTemplatesFile = new File(ArchiPlugin.INSTANCE.getUserDataFolder(), "templates.xml"); //$NON-NLS-1$
    
    public ArchimateTemplateManager() {
    }
    
    @Override
    protected ITemplateGroup loadInbuiltTemplates() {
        ITemplateGroup group = new TemplateGroup(Messages.ArchimateTemplateManager_2);
        File folder = ArchiTemplatesPlugin.INSTANCE.getTemplatesFolder();
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
        else {
            throw new IOException(Messages.ArchimateTemplateManager_0);
        }
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
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_MODELS);
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

        // If the attribute "type" exists then return true if its value is "model".
        // If the attribute doesn't exist it was from an older version (before 2.1)
        try {
            Document doc = JDOMUtils.readXMLString(xmlString);
            Element root = doc.getRootElement();
            Attribute attType = root.getAttribute(ITemplateXMLTags.XML_TEMPLATE_ATTRIBUTE_TYPE);
            if(attType != null) {
                return ArchimateModelTemplate.XML_TEMPLATE_ATTRIBUTE_TYPE_MODEL.equals(attType.getValue());
            }
        }
        catch(JDOMException ex) {
            return false;
        }
         
        return true;
    }
}
