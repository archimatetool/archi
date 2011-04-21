/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.diagram.commands.ConnectionLineWidthCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.ConnectionTextPositionCommand;
import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;


/**
 * Connection Property Section
 * 
 * @author Phillip Beauvoir
 */
public class DiagramConnectionSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "uk.ac.bolton.archimate.help.elementPropertySection";

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Model event (Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__TEXT) {
                refreshTextField();
            }
            else if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__TEXT_POSITION) {
                refreshTextPositionCombo();
            }
            else if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__LINE_WIDTH) {
                refreshLineWidthCombo();
            }
        }
    };
    
    private IDiagramModelConnection fConnection;
    
    private PropertySectionTextControl fTextConnection;
    private Combo fComboTextPosition;
    private Combo fComboLineWidth;
    
    private boolean fIsUpdating;

    public static final String[] comboTextPositionItems = {
            "Source",
            "Middle",
            "Target"
    };
    
    public static final String[] comboLineWidthItems = {
            "Normal",
            "Medium",
            "Heavy"
    };
    
    @Override
    protected void createControls(Composite parent) {
        createTextRelationshipControl(parent);
        createTextPositionComboControl(parent);
        createLineWidthComboControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createTextRelationshipControl(Composite parent) {
        // Label
        createCLabel(parent, "Line Text:", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        // Text
        Text textControl = createSingleTextControl(parent, SWT.NONE);
        
        fTextConnection = new PropertySectionTextControl(textControl, IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__TEXT) {
            @Override
            protected void textChanged(String oldText, String newText) {
                if(isAlive()) {
                    getCommandStack().execute(new EObjectFeatureCommand("Connection text", fConnection,
                                            IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__TEXT, newText));
                }
            }
        };
        fTextConnection.setHint("Add text to appear on connection");
    }

    private void createTextPositionComboControl(Composite parent) {
        createCLabel(parent, "Text Position:", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        fComboTextPosition = new Combo(parent, SWT.READ_ONLY);
        fComboTextPosition.setItems(comboTextPositionItems);
        fComboTextPosition.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fComboTextPosition.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(isAlive()) {
                    if(!fIsUpdating) {
                        getCommandStack().execute(new ConnectionTextPositionCommand(fConnection, fComboTextPosition.getSelectionIndex()));
                    }
                }
            }
        });
    }
    
    private void createLineWidthComboControl(Composite parent) {
        createCLabel(parent, "Line Width:", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        fComboLineWidth = new Combo(parent, SWT.READ_ONLY);
        fComboLineWidth.setItems(comboLineWidthItems);
        fComboLineWidth.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fComboLineWidth.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(isAlive()) {
                    if(!fIsUpdating) {
                        getCommandStack().execute(new ConnectionLineWidthCommand(fConnection, fComboLineWidth.getSelectionIndex() + 1));
                    }
                }
            }
        });
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
        // Populate fields...
        refreshTextPositionCombo();
        refreshLineWidthCombo();
        refreshTextField();
    }
    
    protected void refreshTextPositionCombo() {
        if(fConnection != null) {
            fIsUpdating = true;
            int pos = fConnection.getTextPosition();
            fComboTextPosition.select(pos);
            fIsUpdating = false;
        }
    }
    
    protected void refreshLineWidthCombo() {
        if(fConnection != null) {
            fIsUpdating = true;
            int lineWidth = fConnection.getLineWidth();
            fComboLineWidth.select(lineWidth - 1);
            fIsUpdating = false;
        }
    }
    
    protected void refreshTextField() {
        fTextConnection.refresh(fConnection);
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
