/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.TextPositionCommand;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.ITextPosition;



/**
 * Property Section for a Text Position
 * 
 * @author Phillip Beauvoir
 */
public class TextPositionSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    private static EAttribute FEATURE = IArchimatePackage.Literals.TEXT_POSITION__TEXT_POSITION;
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        protected boolean isRequiredType(Object object) {
            return (object instanceof ITextPosition) && shouldExposeFeature((EObject)object, FEATURE);
        }

        @Override
        protected Class<?> getAdaptableType() {
            return ITextPosition.class;
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Position event (From Undo/Redo and here)
            if(feature == FEATURE || feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshControls();
            }
        }
    };
    
    private ITextPosition fTextPositionObject;
    
    private Button[] fPositionButtons = new Button[3];
    
    
    @Override
    protected void createControls(Composite parent) {
        SelectionAdapter adapter = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                for(int i = 0; i < fPositionButtons.length; i++) {
                    // Select/deselects
                    fPositionButtons[i].setSelection(e.widget == fPositionButtons[i]);
                    
                    // Command
                    if(fPositionButtons[i] == e.widget) {
                        int position = (Integer)fPositionButtons[i].getData();
                        if(fTextPositionObject.getTextPosition() != position) {
                            if(isAlive()) {
                                fIsExecutingCommand = true;
                                getCommandStack().execute(new TextPositionCommand(fTextPositionObject, position));
                                fIsExecutingCommand = false;
                            }
                        }
                    }
                }
            }
        };

        createLabel(parent, Messages.TextPositionSection_3 + ":", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        Composite client = createComposite(parent, 3);
        
        for(int i = 0; i < fPositionButtons.length; i++) {
            fPositionButtons[i] = new Button(client, SWT.TOGGLE | SWT.FLAT);
            getWidgetFactory().adapt(fPositionButtons[i], true, true); // Need to do it this way for Mac
            fPositionButtons[i].addSelectionListener(adapter);
        }
        
        // Top Button
        fPositionButtons[0].setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ICON_ALIGN_TEXT_TOP));
        fPositionButtons[0].setData(ITextPosition.TEXT_POSITION_TOP);

        // Middle Button
        fPositionButtons[1].setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ICON_ALIGN_TEXT_MIDDLE));
        fPositionButtons[1].setData(ITextPosition.TEXT_POSITION_CENTRE);

        // Bottom Button
        fPositionButtons[2].setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ICON_ALIGN_TEXT_BOTTOM));
        fPositionButtons[2].setData(ITextPosition.TEXT_POSITION_BOTTOM);
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void setElement(Object element) {
        fTextPositionObject = (ITextPosition)new Filter().adaptObject(element);
        if(fTextPositionObject == null) {
            System.err.println(getClass() + " failed to get element for " + element); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        for(int i = 0; i < fPositionButtons.length; i++) {
            fPositionButtons[i].setSelection(fPositionButtons[i] == getPositionButton());
            boolean enabled = fTextPositionObject instanceof ILockable ? !((ILockable)fTextPositionObject).isLocked() : true;
            fPositionButtons[i].setEnabled(enabled);
        }
    }
    
    private Button getPositionButton() {
        int position = fTextPositionObject.getTextPosition();
        
        switch(position) {
            case ITextPosition.TEXT_POSITION_TOP:
                return fPositionButtons[0];

            case ITextPosition.TEXT_POSITION_CENTRE:
                return fPositionButtons[1];

            case ITextPosition.TEXT_POSITION_BOTTOM:
                return fPositionButtons[2];

            default:
                return fPositionButtons[1];
        }
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fTextPositionObject;
    }
}
