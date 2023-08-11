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
public class WarningsCategory extends AbstractIssueCategory {
    
    public WarningsCategory(List<WarningType> warningList) {
        super(Messages.WarningsCategory_0, warningList);
    }
    
    @Override
    public Image getImage() {
        return IHammerImages.ImageFactory.getImage(IHammerImages.ICON_WARNING);
    }
    
    @Override
    public String getHintContent() {
        return Messages.WarningsCategory_1;
    }
}
