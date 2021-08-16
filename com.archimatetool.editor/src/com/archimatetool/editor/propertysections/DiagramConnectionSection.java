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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.ConnectionTextPositionCommand;
import com.archimatetool.editor.diagram.commands.LineWidthCommand;
import com.archimatetool.editor.model.commands.FeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.ILineObject;



/**
 * Connection Property Section
 * 
 * @author Phillip Beauvoir
 */
public class DiagramConnectionSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelConnection;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelConnection.class;
        }
    }

    private Combo fComboTextPosition;
    private Combo fComboLineWidth;
    private Button fButtonDisplayName;
    
    public static final String[] comboTextPositionItems = {
            Messages.DiagramConnectionSection_0,
            Messages.DiagramConnectionSection_1,
            Messages.DiagramConnectionSection_2
    };
    
    public static final String[] comboLineWidthItems = {
            Messages.DiagramConnectionSection_3,
            Messages.DiagramConnectionSection_4,
            Messages.DiagramConnectionSection_5
    };
    
    @Override
    protected void createControls(Composite parent) {
        createDisplayNameControl(parent);
        createTextPositionComboControl(parent);
        createLineWidthComboControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createTextPositionComboControl(Composite parent) {
        createLabel(parent, Messages.DiagramConnectionSection_6, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fComboTextPosition = new Combo(parent, SWT.READ_ONLY);
        getWidgetFactory().adapt(fComboTextPosition, true, true);
        fComboTextPosition.setItems(comboTextPositionItems);
        
        GridData gd = new GridData(SWT.NONE, SWT.NONE, true, false);
        gd.minimumWidth = ITabbedLayoutConstants.COMBO_WIDTH;
        fComboTextPosition.setLayoutData(gd);
        
        fComboTextPosition.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject connection : getEObjects()) {
                    if(isAlive(connection)) {
                        Command cmd = new ConnectionTextPositionCommand((IDiagramModelConnection)connection, fComboTextPosition.getSelectionIndex());
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        });
    }
    
    private void createLineWidthComboControl(Composite parent) {
        createLabel(parent, Messages.DiagramConnectionSection_7, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fComboLineWidth = new Combo(parent, SWT.READ_ONLY);
        getWidgetFactory().adapt(fComboLineWidth, true, true);
        fComboLineWidth.setItems(comboLineWidthItems);
        
        GridData gd = new GridData(SWT.NONE, SWT.NONE, true, false);
        gd.minimumWidth = ITabbedLayoutConstants.COMBO_WIDTH;
        fComboLineWidth.setLayoutData(gd);
        
        fComboLineWidth.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject connection : getEObjects()) {
                    if(isAlive(connection)) {
                        Command cmd = new LineWidthCommand((ILineObject)connection, fComboLineWidth.getSelectionIndex() + 1);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        });
    }
    
    private void createDisplayNameControl(Composite parent) {
        createLabel(parent, Messages.DiagramConnectionSection_8 + ":", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER); //$NON-NLS-1$
        
        fButtonDisplayName = getWidgetFactory().createButton(parent, null, SWT.CHECK);
        
        fButtonDisplayName.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject connection : getEObjects()) {
                    if(isAlive(connection)) {
                        Command cmd = new FeatureCommand(Messages.DiagramConnectionSection_8, (IFeatures)connection,
                                IDiagramModelConnection.FEATURE_NAME_VISIBLE, fButtonDisplayName.getSelection(), IDiagramModelConnection.FEATURE_NAME_VISIBLE_DEFAULT);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        });
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__TEXT_POSITION) {
                refreshTextPositionCombo();
            }
            else if(feature == IArchimatePackage.Literals.LINE_OBJECT__LINE_WIDTH) {
                refreshLineWidthCombo();
            }
            else if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
        
        // Notifier is the Feature
        if(isFeatureNotification(msg, IDiagramModelConnection.FEATURE_NAME_VISIBLE)) {
            refreshNameVisibleButton();
        }
    }

    @Override
    protected void update() {
        refreshNameVisibleButton();
        refreshTextPositionCombo();
        refreshLineWidthCombo();
    }
    
    protected void refreshTextPositionCombo() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        IDiagramModelConnection lastSelectedConnection = (IDiagramModelConnection)getFirstSelectedObject();
        
        int pos = lastSelectedConnection.getTextPosition();
        fComboTextPosition.select(pos);
        
        fComboTextPosition.setEnabled(!isLocked(lastSelectedConnection));
    }
    
    protected void refreshLineWidthCombo() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        IDiagramModelConnection lastSelectedConnection = (IDiagramModelConnection)getFirstSelectedObject();
        
        int lineWidth = lastSelectedConnection.getLineWidth();
        fComboLineWidth.select(lineWidth - 1);
        
        fComboLineWidth.setEnabled(!isLocked(lastSelectedConnection));
    }
    
    protected void refreshNameVisibleButton() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        IDiagramModelConnection lastSelectedConnection = (IDiagramModelConnection)getFirstSelectedObject();
        
        fButtonDisplayName.setSelection(lastSelectedConnection.isNameVisible());
        
        fButtonDisplayName.setEnabled(!isLocked(lastSelectedConnection));
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
