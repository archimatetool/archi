package com.archimatetool.model.relationships;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.archimatetool.model.relationships.IRelationship;
import com.archimatetool.model.relationships.Messages;
import com.archimatetool.model.relationships.Relationship;

public class RelationshipManager {
	

	/**
     * The default relationship representing "none".
     * All relationships are allowed.
     */
    public static IRelationship NONE_RELATIONSHIPS = new Relationship("", Messages.RelationshipManager_0); //$NON-NLS-1$
    
    /**
     * The Bundle ID
     */
    private static final String BUNDLE_ID = "com.archimatetool.model"; //$NON-NLS-1$

    /**
     * The Relationships XML file
     */
    static final String RELATIONSHIPS_FILE = "model/relationshipTypes.xml"; //$NON-NLS-1$
        
    /**
     * Single Instance of ViewpointManager
     */
    public static RelationshipManager INSTANCE = new RelationshipManager();
    
    /**
     * All Viewpoints
     */
    private Map<String, IRelationship> RELATIONSHIPS = new HashMap<String, IRelationship>();
    
    private RelationshipManager() {
        try {
            loadDefaultRelationshipsFile();
        }
        catch(IOException | JDOMException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * @return A list of all Relationships
     */
    public List<IRelationship> getAllRelationships() {
        List<IRelationship> list = new ArrayList<IRelationship>(RELATIONSHIPS.values());

        // Sort the Relationships by name
        Collections.sort(list, new Comparator<IRelationship>() {
            @Override
            public int compare(IRelationship rel1, IRelationship rel2) {
                return rel1.getName().compareTo(rel2.getName());
            }
        });
        
        // Add the default "none" Relationships at the top of the list
        list.add(0, NONE_RELATIONSHIPS);
        
        return list;
    }
    
    /**
     * @param id
     * @return A Relationship by its id
     */
    public IRelationship getRelationship(String id) {
        if(id == null || "".equals(id)) { //$NON-NLS-1$
            return NONE_RELATIONSHIPS;
        }
        
        IRelationship rel = RELATIONSHIPS.get(id);
        
        return rel == null ? NONE_RELATIONSHIPS : rel;
    }
        
    
    /**
     * Load relationships from XML file
     */
    void loadDefaultRelationshipsFile() throws IOException, JDOMException {
        URL url = Platform.getBundle(BUNDLE_ID).getEntry(RELATIONSHIPS_FILE);
        
        Document doc = new SAXBuilder().build(url);
        Element rootElement = doc.getRootElement();
        
        for(Element xmlRelationship : rootElement.getChildren("relationship")) { //$NON-NLS-1$
            
            String id = xmlRelationship.getAttributeValue("id"); //$NON-NLS-1$
            if(id == null || "".equals(id)) { //$NON-NLS-1$
                System.err.println("Blank id for relationship"); //$NON-NLS-1$
                continue;
            }
            
            Element xmlName = xmlRelationship.getChild("name"); //$NON-NLS-1$
            if(xmlName == null) {
                System.err.println("No name element for relationship"); //$NON-NLS-1$
                continue;
            }
            
            String name = xmlName.getText();
            if(name == null || "".equals(name)) { //$NON-NLS-1$
                System.err.println("Blank name for relationship"); //$NON-NLS-1$
                continue;
            }
            
            Relationship rel = new Relationship(id, name);

            RELATIONSHIPS.put(id, rel);
        }
    }    
}
