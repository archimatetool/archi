/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.issues;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.model.IHelpHintProvider;


public interface IIssue extends IHelpHintProvider, IAdaptable {
    
    void setName(String description);
    String getName();

    void setDescription(String description);
    String getDescription();
    
    void setExplanation(String explanation);
    String getExplanation();
    
    Object getObject();
    void setObject(Object obj);
    
    Image getImage();
}
