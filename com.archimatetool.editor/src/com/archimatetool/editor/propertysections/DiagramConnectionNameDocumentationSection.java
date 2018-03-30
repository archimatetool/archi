/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * Property Section for the name & documentation of a non-Archimate diagram connection
 * 
 * @author Phillip Beauvoir
 */
public class DiagramConnectionNameDocumentationSection extends AbstractNameDocumentationSection {

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return (object instanceof IDiagramModelConnection) && !(object instanceof IDiagramModelArchimateConnection);
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelConnection.class;
        }
    }

    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
