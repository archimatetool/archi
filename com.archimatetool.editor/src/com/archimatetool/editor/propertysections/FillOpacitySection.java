/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.DiagramModelObjectAlphaCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Property Section for Fill Opacity
 * 
 * @author Phillip Beauvoir
 */
public class FillOpacitySection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelObject dmo && shouldExposeFeature(dmo, IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__ALPHA.getName());
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelObject.class;
        }
    }
    
    private OpacityComposite fOpacityComposite;
    
    @Override
    protected void createControls(Composite parent) {
        fOpacityComposite = new OpacityComposite(this, parent, Messages.FillOpacitySection_0) {
            @Override
            Command getCommand(IDiagramModelObject dmo, int newValue) {
                return new DiagramModelObjectAlphaCommand(dmo, newValue);
            }

            @Override
            int getValue() {
                IDiagramModelObject lastSelected = (IDiagramModelObject)getFirstSelectedObject();
                return lastSelected.getAlpha();
            }
            
            @Override
            boolean isValidObject(EObject eObject) {
                return getFilter().isRequiredType(eObject);
            }
        };
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        Object feature = msg.getFeature();

        if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__ALPHA) {
            if(!isExecutingCommand()) {
                update();
            }
        }
        else if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
            update();
        }
    }

    @Override
    protected void update() {
        fOpacityComposite.updateControl();
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        if(fOpacityComposite != null) {
            fOpacityComposite.dispose();
            fOpacityComposite = null;
        }
    }

}
