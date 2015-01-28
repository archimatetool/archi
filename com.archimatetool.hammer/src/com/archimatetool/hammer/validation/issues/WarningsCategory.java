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
public class WarningsCategory extends AbstractIssueCategory {
    
    public WarningsCategory(List<WarningType> warningList) {
        super("Warnings", warningList);
    }
    
    @Override
    public Image getImage() {
        return IHammerImages.ImageFactory.getImage(IHammerImages.ICON_WARNING_16);
    }
}
