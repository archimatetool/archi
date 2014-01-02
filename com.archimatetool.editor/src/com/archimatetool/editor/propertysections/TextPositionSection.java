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
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.editparts.ITextPositionedEditPart;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.propertysections.AbstractArchimatePropertySection;
import com.archimatetool.editor.propertysections.ITabbedLayoutConstants;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFontAttribute;
import com.archimatetool.model.ILockable;



/**
 * Property Section for a Text Position
 * 
 * @author Phillip Beauvoir
 */
public class TextPositionSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter implements IFilter {
        @Override
        public boolean select(Object object) {
            return (object instanceof ITextPositionedEditPart) && ((EditPart)object).getModel() instanceof IFontAttribute;
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Model event
            if(feature == IArchimatePackage.Literals.FONT_ATTRIBUTE__TEXT_POSITION) {
                refreshControls();
            }
            else if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshButtons();
            }
        }
    };
    
    private IFontAttribute fFontAttribute;
    
    private Combo fComboPositions;
    
    private static final String[] fComboPositionItems = {
        Messages.TextPositionSection_0,
        Messages.TextPositionSection_1,
        Messages.TextPositionSection_2,
        Messages.TextPositionSection_3,
        Messages.TextPositionSection_4,
        Messages.TextPositionSection_5,
        Messages.TextPositionSection_6,
        Messages.TextPositionSection_7,
        Messages.TextPositionSection_8,
    };
    
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.TextPositionSection_9, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        // Position
        fComboPositions = new Combo(parent, SWT.READ_ONLY);
        fComboPositions.setItems(fComboPositionItems);
        fComboPositions.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fComboPositions.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new EObjectFeatureCommand(Messages.TextPositionSection_10,
                                                fFontAttribute,
                                                IArchimatePackage.Literals.FONT_ATTRIBUTE__TEXT_POSITION,
                                                fComboPositions.getSelectionIndex()));
                    fIsExecutingCommand = false;
                }
            }
        });
        
        GridData gd = new GridData(SWT.NONE, SWT.NONE, false, false);
        fComboPositions.setLayoutData(gd);
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void setElement(Object element) {
        if(element instanceof ITextPositionedEditPart && ((ITextPositionedEditPart)element).getModel() instanceof IFontAttribute) {
            fFontAttribute = (IFontAttribute)((ITextPositionedEditPart)element).getModel();
        }

        if(fFontAttribute == null) {
            throw new RuntimeException("Object was null"); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        refreshButtons();
    }
    
    protected void refreshButtons() {
        boolean enabled = fFontAttribute instanceof ILockable ? !((ILockable)fFontAttribute).isLocked() : true;
        
        int position = fFontAttribute.getTextPosition();
        if(position < IFontAttribute.TEXT_POSITION_TOP_LEFT || position > IFontAttribute.TEXT_POSITION_BOTTOM_RIGHT) {
            position = IFontAttribute.TEXT_POSITION_TOP_RIGHT;
        }
        
        if(!fIsExecutingCommand) {
            fComboPositions.select(position);
            fComboPositions.setEnabled(enabled);
        }
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fFontAttribute;
    }
}
