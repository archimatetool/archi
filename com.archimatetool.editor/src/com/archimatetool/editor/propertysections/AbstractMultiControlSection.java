/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.model.IArchimateModelObject;



/**
 * Abstract Property Section that contains two or more controls that update and layout according to the selection
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractMultiControlSection extends AbstractECorePropertySection {
    
    protected Composite parentComposite;
    protected GridLayoutColumnHandler columnHandler;
    
    /**
     * Create controls and set number of columns
     * @param parent
     * @param numColumns
     */
    protected void init(Composite parent, int numColumns) {
        parentComposite = parent;
        ((GridLayout)parent.getLayout()).horizontalSpacing = 30;
        columnHandler = GridLayoutColumnHandler.create(parentComposite, numColumns);
    }
    
    /**
     * @param featureName
     * @return true if a control shouldbe shown according to its feature
     */
    protected boolean shouldShowControl(String featureName) {
        for(IArchimateModelObject object : getEObjects()) {
            if(!getFilter().shouldExposeFeature(object, featureName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Layout controls
     */
    protected void layout() {
        if(columnHandler != null) {
            columnHandler.updateColumns();
        }
        
        parentComposite.requestLayout();
    }
    
    @Override
    public void dispose() {
        super.dispose();
        columnHandler = null;
        parentComposite = null;
    }
}
