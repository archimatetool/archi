/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.issues;

import org.eclipse.swt.graphics.Image;

import com.archimatetool.help.hints.IHelpHintProvider;
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

    public void setName(String name) {
        fName = name;
    }

    public String getName() {
        return fName;
    }
    
    public void setDescription(String description) {
        fDescription = description;
    }

    public String getDescription() {
        return fDescription;
    }
    
    public void setExplanation(String explanation) {
        fExplanation = explanation;
    }
    
    public String getExplanation() {
        return fExplanation;
    }
    
    public void setObject(Object obj) {
        fObject = obj;
    }
    
    public Object getObject() {
        return fObject;
    }
    
    public Image getImage() {
        return null;
    }
    
    public String getHelpHintTitle() {
        return getName();
    }
    
    public String getHelpHintContent() {
        return getExplanation();
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object getAdapter(Class adapter) {
        if(adapter == IHelpHintProvider.class) {
            return this;
        }
        
        Object object = getObject();
        if(object == null) {
            return null;
        }
        
        if(adapter.isInstance(object) || adapter.isInstance(this)) {
            return object;
        }
        
        if(adapter.isAssignableFrom(IArchimateConcept.class)) {
            if(object instanceof IDiagramModelArchimateComponent) {
                return ((IDiagramModelArchimateComponent)object).getArchimateConcept();
            }
        }
        
        return null;
    }
}
