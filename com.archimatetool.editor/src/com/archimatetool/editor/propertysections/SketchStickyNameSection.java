/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import com.archimatetool.model.INameable;
import com.archimatetool.model.ISketchModelSticky;



/**
 * Property Section for a Sketch Sticky
 * 
 * @author Phillip Beauvoir
 */
public class SketchStickyNameSection extends AbstractNameSection {
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof ISketchModelSticky;
        }

        @Override
        public Class<?> getAdaptableType() {
            return INameable.class;
        }
    }

    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
