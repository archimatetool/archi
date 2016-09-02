/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.issues;

import org.eclipse.swt.graphics.Image;

import com.archimatetool.hammer.IHammerImages;



/**
 * Issue Type
 * 
 * @author Phillip Beauvoir
 */
public class ErrorType extends AbstractIssueType {

    public ErrorType() {
    }
    
    public ErrorType(String name, String description, String explanation, Object obj) {
        super(name, description, explanation, obj);
    }

    @Override
    public Image getImage() {
        return IHammerImages.ImageFactory.getImage(IHammerImages.ICON_ERROR);
    }
}
