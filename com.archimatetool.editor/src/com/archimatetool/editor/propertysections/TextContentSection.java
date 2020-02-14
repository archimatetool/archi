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
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.ui.components.StyledTextControl;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ITextContent;



/**
 * Property Section for a Text Content
 * 
 * @author Phillip Beauvoir
 */
public class TextContentSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof ITextContent;
        }

        @Override
        public Class<?> getAdaptableType() {
            return ITextContent.class;
        }
    }

    
    private PropertySectionTextControl fTextContentControl;
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.TextContentSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        StyledTextControl styledTextControl = createStyledTextControl(parent, SWT.NONE);
        styledTextControl.setMessage(Messages.TextContentSection_2);
        
        fTextContentControl = new PropertySectionTextControl(styledTextControl.getControl(), IArchimatePackage.Literals.TEXT_CONTENT__CONTENT) {
            @Override
            protected void textChanged(String oldText, String newText) {
                CompoundCommand result = new CompoundCommand();
                
                for(EObject textContent : getEObjects()) {
                    if(isAlive(textContent)) {
                        Command cmd = new EObjectFeatureCommand(Messages.TextContentSection_1, textContent,
                                IArchimatePackage.Literals.TEXT_CONTENT__CONTENT, newText);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        };
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(fTextContentControl.getTextControl(), HELP_ID);
    }

    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.TEXT_CONTENT__CONTENT ||
                    feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        fTextContentControl.refresh(getFirstSelectedObject());
        
        fTextContentControl.setEditable(!isLocked(getFirstSelectedObject()));
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
