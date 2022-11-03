/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.issues;

import java.util.List;

import org.eclipse.swt.graphics.Image;



/**
 * Issue Category Type
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractIssueCategory implements IIssueCategory {
    
    private String fName;
    private List<? extends IIssue> fIssues;

    protected AbstractIssueCategory(String name, List<? extends IIssue> issues) {
        setName(name);
        setIssues(issues);
    }
    
    @Override
    public String getName() {
        return fName;
    }
    
    @Override
    public void setName(String name) {
        fName = name;
    }
    
    @Override
    public List<? extends IIssue> getIssues() {
        return fIssues;
    }
    
    @Override
    public void setIssues(List<? extends IIssue> issues) {
        fIssues = issues;
    }
    
    @Override
    public Image getImage() {
        return null;
    }
    
    @Override
    public String getHelpHintTitle() {
        return getName();
    }
    
    @Override
    public String getHelpHintContent() {
        return null;
    }
}
