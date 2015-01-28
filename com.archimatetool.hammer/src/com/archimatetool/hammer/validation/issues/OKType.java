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
@SuppressWarnings("nls")
public class OKType extends AbstractIssueType {

    public OKType() {
        super("OK", "Everything is OK", "No problems found.", null);
    }
    
    @Override
    public Image getImage() {
        return IHammerImages.ImageFactory.getImage(IHammerImages.ICON_OK_16);
    }
}
