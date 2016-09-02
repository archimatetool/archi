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
public class OKType extends AbstractIssueType {

    public OKType() {
        super(Messages.OKType_0, Messages.OKType_1, Messages.OKType_2, null);
    }
    
    @Override
    public Image getImage() {
        return IHammerImages.ImageFactory.getImage(IHammerImages.ICON_OK);
    }
}
