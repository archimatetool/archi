/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
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
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.diagram.commands.BorderColorCommand;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IBorderObject;
import uk.ac.bolton.archimate.model.ILockable;


/**
 * Property Section for a Border Color
 * 
 * @author Phillip Beauvoir
 */
public class BorderColorSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "uk.ac.bolton.archimate.help.elementPropertySection";
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter implements IFilter {
        @Override
        public boolean select(Object object) {
            return (object instanceof EditPart) && ((EditPart)object).getModel() instanceof IBorderObject;
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Color event (From Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.BORDER_OBJECT__BORDER_COLOR ||
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
                if(!newColor.equals(fBorderObject.getBorderColor())) {
                    getCommandStack().execute(new BorderColorCommand(fBorderObject, newColor));
                }
            }
        }
    };
    
    private IBorderObject fBorderObject;

    private ColorSelector fColorSelector;
    private Button fNoBorderButton;
    
    @Override
    protected void createControls(Composite parent) {
        createColorControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createColorControl(Composite parent) {
        createCLabel(parent, "Border colour:", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        Composite client = createComposite(parent, 2);

        fColorSelector = new ColorSelector(client);
        GridData gd = new GridData(SWT.NONE, SWT.NONE, false, false);
        gd.widthHint = ITabbedLayoutConstants.BUTTON_WIDTH;
        fColorSelector.getButton().setLayoutData(gd);
        getWidgetFactory().adapt(fColorSelector.getButton(), true, true);
        fColorSelector.addListener(colorListener);

        fNoBorderButton = new Button(client, SWT.PUSH);
        fNoBorderButton.setLayoutData(gd);
        getWidgetFactory().adapt(fNoBorderButton, true, true); // Need to do it this way for Mac
        fNoBorderButton.setText("None");
        fNoBorderButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(isAlive()) {
                    getCommandStack().execute(new BorderColorCommand(fBorderObject, null));
                }
            }
        });
    }
    
    @Override
    protected void setElement(Object element) {
        if(element instanceof EditPart && ((EditPart)element).getModel() instanceof IBorderObject) {
            fBorderObject = (IBorderObject)((EditPart)element).getModel();
        }

        if(fBorderObject == null) {
            throw new RuntimeException("Object was null");
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        String colorValue = fBorderObject.getBorderColor();
        if(colorValue != null) {
            RGB rgb = ColorFactory.convertStringToRGB(colorValue);
            fColorSelector.setColorValue(rgb);
        }
        else {
            // No color
            fColorSelector.setColorValue(new RGB(255, 255, 255));
        }
        
        boolean enabled = fBorderObject instanceof ILockable ? !((ILockable)fBorderObject).isLocked() : true;
        fColorSelector.setEnabled(enabled);
        fNoBorderButton.setEnabled(colorValue != null && enabled);
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
        return fBorderObject;
    }
}
