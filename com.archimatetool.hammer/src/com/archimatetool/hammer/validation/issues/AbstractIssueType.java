/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.issues;

import org.eclipse.swt.graphics.Image;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IDiagramModelArchimateComponent;





/**
 * Issue Type
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractIssueType implements IIssue {
    
    private String fName = ""; //$NON-NLS-1$
    private String fDescription = ""; //$NON-NLS-1$
    private String fExplanation = ""; //$NON-NLS-1$
    private Object fObject;
    
    protected AbstractIssueType() {
    }
    
    /**
     * @param name The name of the Issue
     * @param description The description of the Issue
     * @param explanation The explnation of the Issue
     * @param obj The object in question
     */
    protected AbstractIssueType(String name, String description, String explanation, Object obj) {
        setName(name);
        setDescription(description);
        setExplanation(explanation);
        setObject(obj);
    }

    @Override
    public void setName(String name) {
        fName = name;
    }

    @Override
    public String getName() {
        return fName;
    }
    
    @Override
    public void setDescription(String description) {
        fDescription = description;
    }

    @Override
    public String getDescription() {
        return fDescription;
    }
    
    @Override
    public void setExplanation(String explanation) {
        fExplanation = explanation;
    }
    
    @Override
    public String getExplanation() {
        return fExplanation;
    }
    
    @Override
    public void setObject(Object obj) {
        fObject = obj;
    }
    
    @Override
    public Object getObject() {
        return fObject;
    }
    
    @Override
    public Image getImage() {
        return null;
    }
    
    @Override
    public String getHintTitle() {
        return getName();
    }
    
    @Override
    public String getHintContent() {
        return getExplanation();
    }
    
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object getAdapter(Class adapter) {
        // These are to update the Properties View...
        
        if(adapter == null) {
            return null;
        }
        
        Object object = getObject();
        if(object == null) {
            return null;
        }
        
        // Return the object
        if(adapter.isInstance(object)) {
            return object;
        }
        
        // Archimate concept inside of diagram component
        if(object instanceof IDiagramModelArchimateComponent) {
            IArchimateConcept concept = ((IDiagramModelArchimateComponent)object).getArchimateConcept();
            if(concept != null && adapter.isAssignableFrom(concept.getClass())) {
                return concept;
            }
        }
        
        return null;
    }
}
