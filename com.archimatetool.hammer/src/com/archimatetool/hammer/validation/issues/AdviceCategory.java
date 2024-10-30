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
public class AdviceCategory extends AbstractIssueCategory {
    
    public AdviceCategory(List<AdviceType> adviceList) {
        super(Messages.AdviceCategory_0, adviceList);
    }
    
    @Override
    public Image getImage() {
        return IHammerImages.ImageFactory.getImage(IHammerImages.ICON_ADVICE);
    }
    
    @Override
    public String getHintContent() {
        return Messages.AdviceCategory_1;
    }
}
