/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.FeatureCommand;
import com.archimatetool.editor.ui.components.StyledTextControl;
import com.archimatetool.editor.ui.textrender.TextRenderer;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IFeatures;



/**
 * Property Section for a Label Renderer
 * 
 * @author Phillip Beauvoir
 */
public class LabelRendererSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.labelPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return TextRenderer.getDefault().isSupportedObject(object);
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelComponent.class;
        }
    }

    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
        
        if(isFeatureNotification(msg, TextRenderer.FEATURE_NAME)) {
            update();
        }
    }
    
    protected Label fLabel;
    protected PropertySectionTextControl fTextRender;

    @Override
    protected void createControls(Composite parent) {
        fLabel = createLabel(parent, Messages.LabelRendererSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        StyledTextControl styledTextControl = createStyledTextControl(parent, SWT.NONE);
        styledTextControl.setMessage(Messages.LabelRendererSection_1);
        
        fTextRender = new PropertySectionTextControl(styledTextControl.getControl(), TextRenderer.FEATURE_NAME) {
            @Override
            protected void textChanged(String oldText, String newText) {
                CompoundCommand result = new CompoundCommand();
                
                for(EObject eObject : getEObjects()) {
                    if(isAlive(eObject)) {
                        Command cmd = new FeatureCommand(Messages.LabelRendererSection_2, (IFeatures)eObject, TextRenderer.FEATURE_NAME, newText, ""); //$NON-NLS-1$
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        };
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    @Override
    protected void update() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        fTextRender.refresh(getFirstSelectedObject());
        fTextRender.setEditable(!isLocked(getFirstSelectedObject()));
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
}
