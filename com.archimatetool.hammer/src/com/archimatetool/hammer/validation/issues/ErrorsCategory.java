/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.issues;

import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.archimatetool.hammer.IHammerImages;



/**
 * Category Type
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class ErrorsCategory extends AbstractIssueCategory {
    
    public ErrorsCategory(List<ErrorType> issues) {
        super("Errors", issues);
    }
    
    @Override
    public Image getImage() {
        return IHammerImages.ImageFactory.getImage(IHammerImages.ICON_ERROR_16);
    }
}
