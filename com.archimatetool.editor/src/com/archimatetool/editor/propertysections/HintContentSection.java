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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.ui.components.StyledTextControl;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IHintProvider;



/**
 * Property Section for a Hint Content
 * 
 * @author Phillip Beauvoir
 */
public class HintContentSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IHintProvider;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IHintProvider.class;
        }
    }

    private PropertySectionTextControl fTextTitleControl;
    private PropertySectionTextControl fTextContentControl;

    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.HintContentSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        Text text = createSingleTextControl(parent, SWT.NONE);
        text.setMessage(Messages.HintContentSection_2);
        
        fTextTitleControl = new PropertySectionTextControl(text, IArchimatePackage.Literals.HINT_PROVIDER__HINT_TITLE) {
            @Override
            protected void textChanged(String oldText, String newText) {
                CompoundCommand result = new CompoundCommand();

                for(EObject provider : getEObjects()) {
                    if(isAlive(provider)) {
                        Command cmd = new EObjectFeatureCommand(Messages.HintContentSection_1, provider,
                                IArchimatePackage.Literals.HINT_PROVIDER__HINT_TITLE, newText);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        };
        
        createLabel(parent, Messages.HintContentSection_3, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        StyledTextControl styledTextControl = createStyledTextControl(parent, SWT.NONE);
        styledTextControl.setMessage(Messages.HintContentSection_5);
        
        fTextContentControl = new PropertySectionTextControl(styledTextControl.getControl(), IArchimatePackage.Literals.HINT_PROVIDER__HINT_CONTENT) {
            @Override
            protected void textChanged(String oldText, String newText) {
                CompoundCommand result = new CompoundCommand();

                for(EObject provider : getEObjects()) {
                    if(isAlive(provider)) {
                        Command cmd = new EObjectFeatureCommand(Messages.HintContentSection_4, provider,
                                IArchimatePackage.Literals.HINT_PROVIDER__HINT_CONTENT, newText);
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
            
            if(feature == IArchimatePackage.Literals.HINT_PROVIDER__HINT_TITLE || 
                    feature == IArchimatePackage.Literals.HINT_PROVIDER__HINT_CONTENT ||
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
        
        IHintProvider provider = (IHintProvider)getFirstSelectedObject();
        
        fTextTitleControl.refresh(provider);
        fTextContentControl.refresh(provider);
        
        fTextTitleControl.setEditable(!isLocked(provider));
        fTextContentControl.setEditable(!isLocked(provider));
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
