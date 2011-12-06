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

import uk.ac.bolton.archimate.editor.diagram.commands.ConnectionLineColorCommand;
import uk.ac.bolton.archimate.editor.diagram.editparts.connections.IDiagramConnectionEditPart;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.ILockable;


/**
 * Property Section for a Line Color
 * 
 * @author Phillip Beauvoir
 */
public class LineColorSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "uk.ac.bolton.archimate.help.elementPropertySection";
    
    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Color event (From Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__LINE_COLOR ||
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
                if(!newColor.equals(fDiagramModelConnection.getLineColor())) {
                    getCommandStack().execute(new ConnectionLineColorCommand(fDiagramModelConnection, newColor));
                }
            }
        }
    };
    
    private IDiagramModelConnection fDiagramModelConnection;

    private ColorSelector fColorSelector;
    private Button fDefaultColorButton;
    
    @Override
    protected void createControls(Composite parent) {
        createColorControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createColorControl(Composite parent) {
        createCLabel(parent, "Line colour:", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
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
                    getCommandStack().execute(new ConnectionLineColorCommand(fDiagramModelConnection, null));
                }
            }
        });
    }
    
    @Override
    protected void setElement(Object element) {
        if(element instanceof IDiagramConnectionEditPart) {
            fDiagramModelConnection = (IDiagramModelConnection)((IDiagramConnectionEditPart)element).getModel();
            if(fDiagramModelConnection == null) {
                throw new RuntimeException("Diagram Connection Object was null");
            }
        }
        else {
            throw new RuntimeException("Should have been an IColoredEditPart");
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        if(fDiagramModelConnection == null) {
            return;
        }
        
        String colorValue = fDiagramModelConnection.getLineColor();
        RGB rgb = ColorFactory.convertStringToRGB(colorValue);
        if(rgb != null) {
            fColorSelector.setColorValue(rgb);
        }
        else {
            // Default color
            fColorSelector.setColorValue(new RGB(0, 0, 0));
        }
        
        boolean enabled = fDiagramModelConnection instanceof ILockable ? !((ILockable)fDiagramModelConnection).isLocked() : true;
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
        return fDiagramModelConnection;
    }
}
