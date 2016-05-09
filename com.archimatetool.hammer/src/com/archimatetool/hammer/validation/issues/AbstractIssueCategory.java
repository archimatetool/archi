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
    
    public String getName() {
        return fName;
    }
    
    public void setName(String name) {
        fName = name;
    }
    
    public List<? extends IIssue> getIssues() {
        return fIssues;
    }
    
    public void setIssues(List<? extends IIssue> issues) {
        fIssues = issues;
    }
    
    public Image getImage() {
        return null;
    }
}
