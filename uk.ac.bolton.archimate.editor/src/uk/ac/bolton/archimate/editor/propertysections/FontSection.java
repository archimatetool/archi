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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.diagram.commands.FontCompoundCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.FontStyleCommand;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.ui.FontFactory;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IFontAttribute;
import uk.ac.bolton.archimate.model.ILockable;


/**
 * Property Section for a Diagram Model Object Appearance->Font Section
 * 
 * @author Phillip Beauvoir
 */
public class FontSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "uk.ac.bolton.archimate.help.elementPropertySection";
    
    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Color event (From Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT ||
                    feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshControls();
            }
        }
    };
    
    private IFontAttribute fFontObject;
    
    private CLabel fFontLabel;
    private Button fFontSelectionButton;
    private Button fDefaultFontButton;
    
    @Override
    protected void createControls(final Composite parent) {
        createCLabel(parent, "Font:", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        Composite client = createComposite(parent, 3);

        // Button
        fFontSelectionButton = new Button(client, SWT.PUSH);
        fFontSelectionButton.setText("Edit...");
        getWidgetFactory().adapt(fFontSelectionButton, true, true); // Need to do it this way for Mac
        GridData gd = new GridData(SWT.NONE, SWT.NONE, false, false);
        gd.widthHint = ITabbedLayoutConstants.BUTTON_WIDTH;
        fFontSelectionButton.setLayoutData(gd);

        // Default
        fDefaultFontButton = new Button(client, SWT.PUSH);
        fDefaultFontButton.setLayoutData(gd);
        fDefaultFontButton.setText("Default");
        getWidgetFactory().adapt(fDefaultFontButton, true, true); // Need to do it this way for Mac
        
        // Font Name
        fFontLabel = getWidgetFactory().createCLabel(client, "");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fFontLabel.setLayoutData(gd);
        
        fFontSelectionButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(isAlive()) {
                    FontData fontData = FontFactory.getDefaultUserViewFontData();
                    
                    String fontValue = fFontObject.getFont();
                    if(fontValue != null) {
                        try {
                            fontData = new FontData(fontValue);
                        }
                        catch(Exception ex) {
                            //ex.printStackTrace();
                        }
                    }
                    
                    FontDialog dialog = new FontDialog(parent.getShell());
                    dialog.setText("Select Font");
                    dialog.setFontList(new FontData[] { fontData } );
                    
                    RGB rgb = ColorFactory.convertStringToRGB(fFontObject.getFontColor());
                    if(rgb != null) {
                        dialog.setRGB(rgb);
                    }
                    else {
                        // Default color
                        dialog.setRGB(new RGB(0, 0, 0));
                    }
                    
                    FontData selectedFontData = dialog.open();
                    
                    if(selectedFontData != null) {
                        FontCompoundCommand cmd = new FontCompoundCommand(fFontObject, selectedFontData.toString(), dialog.getRGB());
                        getCommandStack().execute(cmd.unwrap());
                    }
                }
            }
        });
        
        fDefaultFontButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(isAlive()) {
                    getCommandStack().execute(new FontStyleCommand(fFontObject, null));
                }
            }
        });
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void setElement(Object element) {
        if(element instanceof EditPart && ((EditPart)element).getModel() instanceof IFontAttribute) {
            fFontObject = (IFontAttribute)((EditPart)element).getModel();
            if(fFontObject == null) {
                throw new RuntimeException("Font Object was null");
            }
        }
        else {
            throw new RuntimeException("Should have been an IFontAttribute");
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        FontData defaultFontData = FontFactory.getDefaultUserViewFontData();
        
        String fontValue = fFontObject.getFont();
        if(fontValue != null) {
            try {
                defaultFontData = new FontData(fontValue);
            }
            catch(Exception ex) {
                //ex.printStackTrace();
            }
        }
        
        fFontLabel.setText(defaultFontData.getName() + " " +
                defaultFontData.getHeight() + " " +
                ((defaultFontData.getStyle() & SWT.BOLD) == SWT.BOLD ? "Bold" : "") + " " +
                ((defaultFontData.getStyle() & SWT.ITALIC) == SWT.ITALIC ? "Italic" : ""));
        
        boolean enabled = fFontObject instanceof ILockable ? !((ILockable)fFontObject).isLocked() : true;
        fFontSelectionButton.setEnabled(enabled);
        fDefaultFontButton.setEnabled(fFontObject.getFont() != null && enabled);
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
