/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.propertysections;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.diagram.DiagramConstants;
import uk.ac.bolton.archimate.editor.diagram.commands.ConnectionLineTypeCommand;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;


/**
 * Connection Line Style Property Section
 * 
 * @author Phillip Beauvoir
 */
public class DiagramConnectionLineStyleSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "uk.ac.bolton.archimate.help.elementPropertySection";

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Model event (Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__TYPE) {
                refresh();
            }
        }
    };
    
    private IDiagramModelConnection fConnection;
    
    private Combo fComboLineType;
    
    private boolean fIsUpdating;

    public static final String[] comboLineWidthItems = {
            "Plain",
            "Dashed",
            "Dotted"
    };
    
    @Override
    protected void createControls(Composite parent) {
        createLineStyleComboControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
        
    private void createLineStyleComboControl(Composite parent) {
        createCLabel(parent, "Line Style:", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        fComboLineType = new Combo(parent, SWT.READ_ONLY);
        fComboLineType.setItems(comboLineWidthItems);
        fComboLineType.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fComboLineType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(isAlive()) {
                    if(!fIsUpdating) {
                        getCommandStack().execute(new ConnectionLineTypeCommand(fConnection, 
                                    getStringValueOfCombo(fComboLineType.getSelectionIndex())));
                    }
                }
            }
        });
    }
    
    /**
     * Convert combo box int value to String value
     */
    private String getStringValueOfCombo(int type) {
        switch(type) {
            case 1:
                return DiagramConstants.CONNECTION_DASHED;

            case 2:
                return DiagramConstants.CONNECTION_DOTTED;

            default:
                return null; // null = 0 = plain
        }
    }
    
    /**
     * Convert String value to combo box int value
     */
    private int getComboValueOfConnection(String type) {
        if(DiagramConstants.CONNECTION_DASHED.equals(type)) {
            return 1;
        }
        if(DiagramConstants.CONNECTION_DOTTED.equals(type)) {
            return 2;
        }
        
        return 0; // default plain
    }
    
    @Override
    protected void setElement(Object element) {
        if(element instanceof IAdaptable) {
            fConnection = (IDiagramModelConnection)((IAdaptable)element).getAdapter(IDiagramModelConnection.class);
        }
        else {
            throw new RuntimeException("Should have been an Edit Part");
        }
    }
    
    @Override
    public void refresh() {
        if(fConnection == null) {
            return;
        }
        
        // Populate fields...
        fIsUpdating = true;
        fComboLineType.select(getComboValueOfConnection(fConnection.getType()));
        fIsUpdating = false;
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fConnection;
    }
}
