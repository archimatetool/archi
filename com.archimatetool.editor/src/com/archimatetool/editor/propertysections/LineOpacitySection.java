/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.DiagramModelObjectOutlineAlphaCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Property Section for Line Opacity
 * 
 * @author Phillip Beauvoir
 */
public class LineOpacitySection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelObject dmo && shouldExposeFeature(dmo, IDiagramModelObject.FEATURE_LINE_ALPHA);
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelObject.class;
        }
    }
    
    private OpacityComposite fOpacityComposite;
    
    @Override
    protected void createControls(Composite parent) {
        fOpacityComposite = new OpacityComposite(this, parent, Messages.LineOpacitySection_0) {
            @Override
            Command getCommand(IDiagramModelObject dmo, int newValue) {
                return new DiagramModelObjectOutlineAlphaCommand(dmo, newValue);
            }

            @Override
            int getValue() {
                IDiagramModelObject lastSelected = (IDiagramModelObject)getFirstSelectedObject();
                return lastSelected.getLineAlpha();
            }
        };
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        Object feature = msg.getFeature();

        if(isFeatureNotification(msg, IDiagramModelObject.FEATURE_LINE_ALPHA)) {
            if(!fIsExecutingCommand) {
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
