/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.ui.components.StyledTextControl;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.ITextContent;



/**
 * Property Section for a Text Content
 * 
 * @author Phillip Beauvoir
 */
public class TextContentSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        boolean isRequiredType(Object object) {
            return object instanceof ITextContent;
        }

        @Override
        Class<?> getAdaptableType() {
            return ITextContent.class;
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Model Name event (Undo/Redo and here!)
            if(feature == IArchimatePackage.Literals.TEXT_CONTENT__CONTENT ||
                    feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshControls();
            }
        }
    };
    
    private ITextContent fTextContent;
    
    private PropertySectionTextControl fTextContentControl;
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.TextContentSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        StyledTextControl styledTextControl = createStyledTextControl(parent, SWT.NONE);
        
        fTextContentControl = new PropertySectionTextControl(styledTextControl.getControl(), IArchimatePackage.Literals.TEXT_CONTENT__CONTENT) {
            @Override
            protected void textChanged(String oldText, String newText) {
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new EObjectFeatureCommand(Messages.TextContentSection_1, fTextContent,
                                                IArchimatePackage.Literals.TEXT_CONTENT__CONTENT, newText));
                    fIsExecutingCommand = false;
                }
            }
        };
        fTextContentControl.setHint(Messages.TextContentSection_2);
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(fTextContentControl.getTextControl(), HELP_ID);
    }

    @Override
    protected void setElement(Object element) {
        fTextContent = (ITextContent)new Filter().adaptObject(element);
        if(fTextContent == null) {
            System.err.println(getClass() + " failed to get element for " + element); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        if(fIsExecutingCommand) {
            return; 
        }
        fTextContentControl.refresh(fTextContent);
        
        boolean enabled = fTextContent instanceof ILockable ? !((ILockable)fTextContent).isLocked() : true;
        fTextContentControl.setEditable(enabled);
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fTextContent;
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
}
