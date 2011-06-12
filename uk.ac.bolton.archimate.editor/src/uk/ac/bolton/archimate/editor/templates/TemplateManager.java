/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.templates;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import uk.ac.bolton.archimate.editor.ArchimateEditorPlugin;
import uk.ac.bolton.archimate.editor.utils.ZipUtils;
import uk.ac.bolton.jdom.JDOMUtils;


/**
 * Template Manager.
 * Users must call dispose() when finished with it if the images in Templates are loaded
 * 
 * @author Phillip Beauvoir
 */
public class TemplateManager implements ITemplateXMLTags {
    
    public static final String ARCHIMATE_TEMPLATE_FILE_EXTENSION = ".architemplate";
    public static final String ARCHIMATE_TEMPLATE_FILE_WILDCARD = "*.architemplate";
    
    // Prefix of name of tmp file created when opening a template
    public static final String ARCHIMATE_TEMPLATE_FILE_TMP_PREFIX = "~architemplate";
    
    public static final String ZIP_ENTRY_MANIFEST = "manifest.xml";
    public static final String ZIP_ENTRY_MODEL = "model.archimate";
    public static final String ZIP_ENTRY_THUMBNAILS = "Thumbnails/";
    
    public static final int THUMBNAIL_WIDTH = 512;
    public static final int THUMBNAIL_HEIGHT = 512;
    
    // The "All" User Template group
    public ITemplateGroup AllUserTemplatesGroup = new TemplateGroup("All") {
        @Override
        public List<ITemplate> getTemplates() {
            return getUserTemplates();
        }
    };
    
    private File fUserTemplatesFile = new File(ArchimateEditorPlugin.INSTANCE.getUserDataFolder(), "templates.xml");
    
    private ITemplateGroup fInbuiltTemplateGroup;
    private List<ITemplate> fUserTemplates;
    private List<ITemplateGroup> fUserTemplateGroups;
    
    public TemplateManager() {
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
    
    private ITemplateGroup loadInbuiltTemplates() {
        ITemplateGroup group = new TemplateGroup("Installed Templates");
        File folder = ArchimateEditorPlugin.INSTANCE.getTemplatesFolder();
        if(folder.exists()) {
            for(File file : folder.listFiles()) {
                if(file.getName().toLowerCase().endsWith(ARCHIMATE_TEMPLATE_FILE_EXTENSION)) {
                    ITemplate template = new ModelTemplate();
                    template.setFile(file);
                    group.addTemplate(template);
                }
            }
        }
        return group;
    }

    private void loadUserTemplates() {
        fUserTemplates = new ArrayList<ITemplate>();
        fUserTemplateGroups = new ArrayList<ITemplateGroup>();
        
        if(!fUserTemplatesFile.exists()) {
            return;
        }
        
        Document doc = null;
        try {
            doc = JDOMUtils.readXMLFile(fUserTemplatesFile);
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
            if(XML_TEMPLATE_ATTRIBUTE_TYPE_MODEL.equals(type)) {
                String id = templateElement.getAttributeValue(XML_TEMPLATE_ATTRIBUTE_ID);
                String path = templateElement.getAttributeValue(XML_TEMPLATE_ATTRIBUTE_FILE);
                if(id != null && path != null) {
                    File file = new File(path);
                    if(file.exists()) {
                        ITemplate template = new ModelTemplate(id);
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
        
        JDOMUtils.write2XMLFile(doc, fUserTemplatesFile);
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
    
    private void disposeInbuiltTemplates() {
        if(fInbuiltTemplateGroup != null) {
            for(ITemplate template : fInbuiltTemplateGroup.getTemplates()) {
                template.dispose();
            }
        }
    }
    
    private void disposeUserTemplates() {
        if(fUserTemplates != null) {
            for(ITemplate template : fUserTemplates) {
                template.dispose();
            }
        }
    }
    
    /**
     * @param file
     * @return true if file is a valid template file
     * @throws IOException 
     */
    public static boolean isValidTemplateFile(File file) throws IOException {
        if(file == null || !file.exists()) {
            return false;
        }
        
        String s = ZipUtils.extractZipEntry(file, ZIP_ENTRY_MANIFEST);
        if(s == null) {
            return false;
        }
        
        return true;
    }
}
