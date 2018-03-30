/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.model.IDiagramModel;



/**
 * Property Section for a Diagram Model
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelSection extends AbstractNameDocumentationSection {
    
    private static final String HELP_ID = "com.archimatetool.help.diagramModelSection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModel;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModel.class;
        }
    }

    @Override
    protected void createControls(Composite parent) {
        super.createControls(parent);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
