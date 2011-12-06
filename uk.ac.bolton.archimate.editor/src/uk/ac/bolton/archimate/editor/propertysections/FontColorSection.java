/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.diagram.commands.FontColorCommand;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IFontAttribute;
import uk.ac.bolton.archimate.model.ILockable;


/**
 * Property Section for a Font Color Section
 * 
 * @author Phillip Beauvoir
 */
public class FontColorSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "uk.ac.bolton.archimate.help.elementPropertySection";
    
    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Color event (From Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT_COLOR ||
                    feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshControls();
            }
        }
    };
    
    /**
     * Color listener
     */
    private IPropertyChangeListener colorListener = new IPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent event) {
            if(isAlive()) {
                RGB rgb = fColorSelector.getColorValue();
                String newColor = ColorFactory.convertRGBToString(rgb);
                if(!newColor.equals(fFontObject.getFontColor())) {
                    getCommandStack().execute(new FontColorCommand(fFontObject, newColor));
                }
            }
        }
    };
    
    private IFontAttribute fFontObject;

    private ColorSelector fColorSelector;
    private Button fDefaultColorButton;
    
    @Override
    protected void createControls(Composite parent) {
        createColorControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createColorControl(Composite parent) {
        createCLabel(parent, "Font colour:", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        Composite client = createComposite(parent, 2);
        
        fColorSelector = new ColorSelector(client);
        GridData gd = new GridData(SWT.NONE, SWT.NONE, false, false);
        gd.widthHint = ITabbedLayoutConstants.BUTTON_WIDTH;
        fColorSelector.getButton().setLayoutData(gd);
        getWidgetFactory().adapt(fColorSelector.getButton(), true, true);
        fColorSelector.addListener(colorListener);

        fDefaultColorButton = new Button(client, SWT.PUSH);
        fDefaultColorButton.setLayoutData(gd);
        getWidgetFactory().adapt(fDefaultColorButton, true, true); // Need to do it this way for Mac
        fDefaultColorButton.setText("Default");
        fDefaultColorButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(isAlive()) {
                    getCommandStack().execute(new FontColorCommand(fFontObject, null));
                }
            }
        });
    }
    
    @Override
    protected void setElement(Object element) {
        if(element instanceof EditPart) {
            fFontObject = (IFontAttribute)((EditPart)element).getModel();
            if(fFontObject == null) {
                throw new RuntimeException("Diagram Model Object was null");
            }
        }
        else {
            throw new RuntimeException("Should have been an EditPart");
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        String colorValue = fFontObject.getFontColor();
        RGB rgb = ColorFactory.convertStringToRGB(colorValue);
        if(rgb != null) {
            fColorSelector.setColorValue(rgb);
        }
        else {
            // Default color
            fColorSelector.setColorValue(new RGB(0, 0, 0));
        }
        
        boolean enabled = fFontObject instanceof ILockable ? !((ILockable)fFontObject).isLocked() : true;
        fColorSelector.setEnabled(enabled);
        fDefaultColorButton.setEnabled(colorValue != null && enabled);
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        if(fColorSelector != null) {
            fColorSelector.removeListener(colorListener);
        }
    }

    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fFontObject;
    }
}
