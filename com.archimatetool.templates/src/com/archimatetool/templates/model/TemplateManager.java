/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.jdom2.Document;
import org.jdom2.Element;

import com.archimatetool.jdom.JDOMUtils;



/**
 * Template Manager.
 * Users must call dispose() when finished with it if the images in Templates are loaded
 * 
 * @author Phillip Beauvoir
 */
public abstract class TemplateManager implements ITemplateXMLTags {
    
    public static final String ZIP_ENTRY_MANIFEST = "manifest.xml"; //$NON-NLS-1$
    public static final String ZIP_ENTRY_MODEL = "model.archimate"; //$NON-NLS-1$
    public static final String ZIP_ENTRY_THUMBNAILS = "Thumbnails/"; //$NON-NLS-1$
    
    public static final int THUMBNAIL_WIDTH = 512;
    public static final int THUMBNAIL_HEIGHT = 512;
    
    // The "All" User Template group
    public ITemplateGroup AllUserTemplatesGroup = new TemplateGroup(Messages.TemplateManager_0) {
        @Override
        public List<ITemplate> getTemplates() {
            return getUserTemplates();
        }
    };
    
    private ITemplateGroup fInbuiltTemplateGroup;
    private List<ITemplate> fUserTemplates;
    private List<ITemplateGroup> fUserTemplateGroups;
    
    public TemplateManager() {
    }
    
    /**
     * Add an entry referencing a template fle
     * @param templateFile
     * @param group optional group to add it to, can be null in which case it is added to the "All" group
     * @throws IOException 
     */
    public void addTemplateEntry(File templateFile, ITemplateGroup group) throws IOException {
        // Duplicate file
        if(hasTemplateFile(templateFile, group)) {
            return;
        }
        
        ITemplate template = createTemplate(templateFile);
        template.setFile(templateFile);
        addUserTemplate(template);
        // Add to user group
        if(group != null) {
            group.addTemplate(template);
        }
        saveUserTemplatesManifest();
    }

    /**
     * @return the inbuilt Template Group
     */
    public ITemplateGroup getInbuiltTemplateGroup() {
        if(fInbuiltTemplateGroup == null) {
            fInbuiltTemplateGroup = loadInbuiltTemplates();
        }
        return fInbuiltTemplateGroup;
    }

    /**
     * @return user Templates
     */
    public List<ITemplate> getUserTemplates() {
        if(fUserTemplates == null) {
            loadUserTemplates();
        }
        
        return fUserTemplates;
    } 
    
    /**
     * @return all the user Template Groups
     */
    public List<ITemplateGroup> getUserTemplateGroups() {
        if(fUserTemplateGroups == null) {
            loadUserTemplates();
        }
        
        return fUserTemplateGroups;
    }

    /**
     * Reset so we can reload from disk
     */
    public void reset() {
        disposeUserTemplates();
        fUserTemplates = null;
        fUserTemplateGroups = null;
    }
    
    /**
     * Check if a template already exists in a group
     * @param templateFile The template file
     * @param group The group to check. If this is null then all user templates are checked
     * @return true if it exists.
     */
    public boolean hasTemplateFile(File templateFile, ITemplateGroup group) {
        if(templateFile == null) {
            return false;
        }
        
        if(group == null) {
            group = AllUserTemplatesGroup;
        }
        
        for(ITemplate template : group.getTemplates()) {
            if(templateFile.equals(template.getFile())) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Load all user templates as declared in the manifest
     */
    protected void loadUserTemplates() {
        fUserTemplates = new ArrayList<ITemplate>();
        fUserTemplateGroups = new ArrayList<ITemplateGroup>();
        
        if(!getUserTemplatesManifestFile().exists()) {
            return;
        }
        
        Document doc = null;
        try {
            doc = JDOMUtils.readXMLFile(getUserTemplatesManifestFile());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return;
        }
        
        HashMap<String, ITemplate> userTemplateMap = new HashMap<String, ITemplate>();
        
        Element rootElement = doc.getRootElement();

        // Templates
        for(Object child : rootElement.getChildren(XML_TEMPLATE_ELEMENT_TEMPLATE)) {
            Element templateElement = (Element)child;
            String type = templateElement.getAttributeValue(XML_TEMPLATE_ATTRIBUTE_TYPE);
            ITemplate template = createTemplate(type);
            if(template != null) {
                String id = templateElement.getAttributeValue(XML_TEMPLATE_ATTRIBUTE_ID);
                String path = templateElement.getAttributeValue(XML_TEMPLATE_ATTRIBUTE_FILE);
                if(id != null && path != null) {
                    File file = new File(path);
                    if(file.exists()) {
                        template.setID(id);
                        template.setFile(file);
                        fUserTemplates.add(template);
                        userTemplateMap.put(id, template);
                    }
                }
            }
        }

        // Groups
        for(Object child : rootElement.getChildren(XML_TEMPLATE_ELEMENT_GROUP)) {
            Element groupElement = (Element)child;
            ITemplateGroup templateGroup = new TemplateGroup();
            templateGroup.setName(groupElement.getAttributeValue(XML_TEMPLATE_ATTRIBUTE_NAME));
            fUserTemplateGroups.add(templateGroup);

            // Template refs
            for(Object child2 : groupElement.getChildren(XML_TEMPLATE_ELEMENT_TEMPLATE_REF)) {
                Element templateRefElement = (Element)child2;
                String ref = templateRefElement.getAttributeValue(XML_TEMPLATE_ATTRIBUTE_REF);
                if(ref != null) {
                    ITemplate template = userTemplateMap.get(ref);
                    if(template != null) {
                        templateGroup.addTemplate(template);
                    }
                }
            }
        }
    }

    public void saveUserTemplatesManifest() throws IOException {
        if(fUserTemplates == null || fUserTemplateGroups == null) {
            return;
        }
        
        Document doc = new Document();
        Element rootElement = new Element(XML_TEMPLATE_ELEMENT_MANIFEST);
        doc.setRootElement(rootElement);
        
        for(ITemplate template : fUserTemplates) {
            Element templateElement = new Element(XML_TEMPLATE_ELEMENT_TEMPLATE);
            rootElement.addContent(templateElement);
            templateElement.setAttribute(XML_TEMPLATE_ATTRIBUTE_TYPE, template.getType());
            templateElement.setAttribute(XML_TEMPLATE_ATTRIBUTE_ID, template.getID());
            templateElement.setAttribute(XML_TEMPLATE_ATTRIBUTE_FILE, template.getFile().getAbsolutePath());
        }
        
        for(ITemplateGroup group : fUserTemplateGroups) {
            Element groupElement = new Element(XML_TEMPLATE_ELEMENT_GROUP);
            rootElement.addContent(groupElement);
            groupElement.setAttribute(XML_TEMPLATE_ATTRIBUTE_NAME, group.getName());
            for(ITemplate template : group.getTemplates()) {
                Element templateRefElement = new Element(XML_TEMPLATE_ELEMENT_TEMPLATE_REF);
                groupElement.addContent(templateRefElement);
                templateRefElement.setAttribute(XML_TEMPLATE_ATTRIBUTE_REF, template.getID());
            }
        }
        
        JDOMUtils.write2XMLFile(doc, getUserTemplatesManifestFile());
    }
    
    public void addUserTemplate(ITemplate template) {
        if(template != null && !getUserTemplates().contains(template)) {
            getUserTemplates().add(template);
        }
    }
    
    /**
     * Dispose of Resources
     */
    public void dispose() {
        disposeInbuiltTemplates();
        disposeUserTemplates();
    }
    
    protected void disposeInbuiltTemplates() {
        if(fInbuiltTemplateGroup != null) {
            for(ITemplate template : fInbuiltTemplateGroup.getTemplates()) {
                template.dispose();
            }
        }
    }
    
    protected void disposeUserTemplates() {
        if(fUserTemplates != null) {
            for(ITemplate template : fUserTemplates) {
                template.dispose();
            }
        }
    }
    
    
    /**
     * @return Load all plug-ins inbuilt templates
     */
    protected abstract ITemplateGroup loadInbuiltTemplates();
    
    /**
     * @return The file extension to use for the template including the dot
     */
    public abstract String getTemplateFileExtension();
    
    /**
     * Create a new template entry from template file
     * @param file The template file
     * @return The new Template entry or will throw an IOException if cannot create it
     * @throws IOException
     */
    public abstract ITemplate createTemplate(File file) throws IOException;

    /**
     * @return The File location of the user templates file
     */
    public abstract File getUserTemplatesManifestFile();
    
    /**
     * Create a new Template entry of type
     * @param type
     * @return a new Template entry of type or null
     */
    protected abstract ITemplate createTemplate(String type);
    
    /**
     * @return An image that represents this Template Manager
     */
    public abstract Image getMainImage();

    /**
     * Check for valid template file
     * @param file The file to check
     * @return true if file is a valid template file
     * @throws IOException if an error loading occurs or if it is the wrong format
     */
    protected abstract boolean isValidTemplateFile(File file) throws IOException;
}
