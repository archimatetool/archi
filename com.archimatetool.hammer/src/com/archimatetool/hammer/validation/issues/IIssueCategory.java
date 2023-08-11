/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.issues;

import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.archimatetool.model.IHelpHintProvider;


/**
 * IIssueCategory
 * 
 * @author Phillip Beauvoir
 */
public interface IIssueCategory extends IHelpHintProvider {

    String getName();
    void setName(String name);
    
    List<? extends IIssue> getIssues();
    void setIssues(List<? extends IIssue> issues);

    Image getImage();
}
