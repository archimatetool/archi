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
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.ConnectionLineTypeCommand;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * Connection Line Style Property Section
 * 
 * @author Phillip Beauvoir
 */
public class DiagramConnectionLineStyleSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return (object instanceof IDiagramModelConnection) && !(object instanceof IDiagramModelArchimateConnection);
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelConnection.class;
        }
    }
    
    private StyleSelector fLineStyleSelector;
    private StyleSelector fSourceArrowSelector;
    private StyleSelector fTargetArrowSelector;
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.DiagramConnectionLineStyleSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        Composite client = createComposite(parent, 3);
        
        fSourceArrowSelector = new SourceArrowSelector(client);
        fLineStyleSelector = new LineStyleSelector(client);
        fTargetArrowSelector = new TargetArrowSelector(client);

        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
        
    protected int getLineTypeValue() {
        int value = 0;
        
        value |= fLineStyleSelector.getValue();
        value |= fSourceArrowSelector.getValue();
        value |= fTargetArrowSelector.getValue();
        
        return value;
    }

    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__TYPE ||
                    feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        fLineStyleSelector.update();
        fSourceArrowSelector.update();
        fTargetArrowSelector.update();
        
        boolean enabled = !isLocked(getFirstSelectedObject());
        fLineStyleSelector.setEnabled(enabled);
        fSourceArrowSelector.setEnabled(enabled);
        fTargetArrowSelector.setEnabled(enabled);
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
    
    protected abstract class StyleSelector {
        protected Button fButton;
        protected int fValue = 0;
        
        public StyleSelector(Composite parent, String buttonText) {
            Composite buttonClient = createComposite(parent, 1);
            buttonClient.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));
            
            fButton = getWidgetFactory().createButton(buttonClient, null, SWT.PUSH);
            fButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            fButton.setToolTipText(buttonText);
            
            fButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    MenuManager menuManager = new MenuManager();
                    addActions(menuManager);
                    
                    Menu menu = menuManager.createContextMenu(fButton.getShell());
                    Point p = fButton.getParent().toDisplay(fButton.getBounds().x, fButton.getBounds().y + fButton.getBounds().height);
                    menu.setLocation(p);
                    menu.setVisible(true);
                }
            });
        }
        
        public void setEnabled(boolean enabled) {
            fButton.setEnabled(enabled);
        }

        protected IAction createAction(String text, final int value, final ImageDescriptor imageDesc) {
            IAction action = new Action(text, IAction.AS_RADIO_BUTTON) {
                @Override
                public void run() {
                    fValue = value;
                    
                    CompoundCommand result = new CompoundCommand();

                    for(EObject connection : getEObjects()) {
                        if(isAlive(connection)) {
                            Command cmd = new ConnectionLineTypeCommand((IDiagramModelConnection)connection, getLineTypeValue());
                            if(cmd.canExecute()) {
                                result.add(cmd);
                            }
                        }
                    }

                    executeCommand(result.unwrap());
                }
                
                @Override
                public ImageDescriptor getImageDescriptor() {
                    return imageDesc;
                }
            };
            
            action.setChecked((((IDiagramModelConnection)getFirstSelectedObject()).getType() & value) != 0);
            
            return action;
        }
        
        public int getValue() {
            return fValue;
        }
        
        protected abstract void addActions(MenuManager menuManager);
        
        protected abstract void update();
    }
    
    protected class LineStyleSelector extends StyleSelector {
        public LineStyleSelector(Composite parent) {
            super(parent, Messages.DiagramConnectionLineStyleSection_1);
        }

        @Override
        protected void addActions(MenuManager menuManager) {
            IAction action = createAction(Messages.DiagramConnectionLineStyleSection_2, IDiagramModelConnection.LINE_SOLID, IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.LINE_SOLID));
            menuManager.add(action);
            
            IDiagramModelConnection lastSelectedConnection = (IDiagramModelConnection)getFirstSelectedObject();
            
            action.setChecked((lastSelectedConnection.getType() & IDiagramModelConnection.LINE_DASHED) == 0 &
                              (lastSelectedConnection.getType() & IDiagramModelConnection.LINE_DOTTED) == 0);
            
            action = createAction(Messages.DiagramConnectionLineStyleSection_3, IDiagramModelConnection.LINE_DASHED, IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.LINE_DASHED));
            menuManager.add(action);

            action = createAction(Messages.DiagramConnectionLineStyleSection_4, IDiagramModelConnection.LINE_DOTTED, IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.LINE_DOTTED));
            menuManager.add(action);
        }
        
        @Override
        protected void update() {
            int connectionType = ((IDiagramModelConnection)getFirstSelectedObject()).getType();
            
            if((connectionType & IDiagramModelConnection.LINE_DASHED) != 0) {
                fValue = IDiagramModelConnection.LINE_DASHED;
                fButton.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.LINE_DASHED));
            }
            else if((connectionType & IDiagramModelConnection.LINE_DOTTED) != 0) {
                fValue = IDiagramModelConnection.LINE_DOTTED;
                fButton.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.LINE_DOTTED));
            }
            else {
                fValue = IDiagramModelConnection.LINE_SOLID;
                fButton.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.LINE_SOLID));
            }
        }
    }

    protected class SourceArrowSelector extends StyleSelector {
        public SourceArrowSelector(Composite parent) {
            super(parent, Messages.DiagramConnectionLineStyleSection_5);
        }

        @Override
        protected void addActions(MenuManager menuManager) {
            IDiagramModelConnection lastSelectedConnection = (IDiagramModelConnection)getFirstSelectedObject();
            
            IAction action = createAction(Messages.DiagramConnectionLineStyleSection_6, IDiagramModelConnection.ARROW_NONE, IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.LINE_SOLID));
            action.setChecked((lastSelectedConnection.getType() & IDiagramModelConnection.ARROW_FILL_SOURCE) == 0 &
                              (lastSelectedConnection.getType() & IDiagramModelConnection.ARROW_HOLLOW_SOURCE) == 0 &
                              (lastSelectedConnection.getType() & IDiagramModelConnection.ARROW_LINE_SOURCE) == 0);
            menuManager.add(action);
            
            action = createAction(Messages.DiagramConnectionLineStyleSection_7, IDiagramModelConnection.ARROW_FILL_SOURCE, IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ARROW_SOURCE_FILL));
            menuManager.add(action);

            action = createAction(Messages.DiagramConnectionLineStyleSection_8, IDiagramModelConnection.ARROW_HOLLOW_SOURCE, IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ARROW_SOURCE_HOLLOW));
            menuManager.add(action);

            action = createAction(Messages.DiagramConnectionLineStyleSection_9, IDiagramModelConnection.ARROW_LINE_SOURCE, IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ARROW_SOURCE_LINE));
            menuManager.add(action);
        }
        
        @Override
        protected void update() {
            int connectionType = ((IDiagramModelConnection)getFirstSelectedObject()).getType();
            
            if((connectionType & IDiagramModelConnection.ARROW_FILL_SOURCE) != 0) {
                fValue = IDiagramModelConnection.ARROW_FILL_SOURCE;
                fButton.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ARROW_SOURCE_FILL));
            }
            else if((connectionType & IDiagramModelConnection.ARROW_HOLLOW_SOURCE) != 0) {
                fValue = IDiagramModelConnection.ARROW_HOLLOW_SOURCE;
                fButton.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ARROW_SOURCE_HOLLOW));
            }
            else if((connectionType & IDiagramModelConnection.ARROW_LINE_SOURCE) != 0) {
                fValue = IDiagramModelConnection.ARROW_LINE_SOURCE;
                fButton.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ARROW_SOURCE_LINE));
            }
            else {
                fValue = IDiagramModelConnection.ARROW_NONE;
                fButton.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.LINE_SOLID));
            }
        }
    }
    
    protected class TargetArrowSelector extends StyleSelector {
        public TargetArrowSelector(Composite parent) {
            super(parent, Messages.DiagramConnectionLineStyleSection_10);
        }

        @Override
        protected void addActions(MenuManager menuManager) {
            IDiagramModelConnection lastSelectedConnection = (IDiagramModelConnection)getFirstSelectedObject();
            
            IAction action = createAction(Messages.DiagramConnectionLineStyleSection_11, IDiagramModelConnection.ARROW_NONE, IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.LINE_SOLID));
            action.setChecked((lastSelectedConnection.getType() & IDiagramModelConnection.ARROW_FILL_TARGET) == 0 &
                              (lastSelectedConnection.getType() & IDiagramModelConnection.ARROW_HOLLOW_TARGET) == 0 &
                              (lastSelectedConnection.getType() & IDiagramModelConnection.ARROW_LINE_TARGET) == 0);
            menuManager.add(action);
            
            action = createAction(Messages.DiagramConnectionLineStyleSection_12, IDiagramModelConnection.ARROW_FILL_TARGET, IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ARROW_TARGET_FILL));
            menuManager.add(action);

            action = createAction(Messages.DiagramConnectionLineStyleSection_13, IDiagramModelConnection.ARROW_HOLLOW_TARGET, IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ARROW_TARGET_HOLLOW));
            menuManager.add(action);

            action = createAction(Messages.DiagramConnectionLineStyleSection_14, IDiagramModelConnection.ARROW_LINE_TARGET, IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ARROW_TARGET_LINE));
            menuManager.add(action);
        }
        
        @Override
        protected void update() {
            int connectionType = ((IDiagramModelConnection)getFirstSelectedObject()).getType();
            
            if((connectionType & IDiagramModelConnection.ARROW_FILL_TARGET) != 0) {
                fValue = IDiagramModelConnection.ARROW_FILL_TARGET;
                fButton.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ARROW_TARGET_FILL));
            }
            else if((connectionType & IDiagramModelConnection.ARROW_HOLLOW_TARGET) != 0) {
                fValue = IDiagramModelConnection.ARROW_HOLLOW_TARGET;
                fButton.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ARROW_TARGET_HOLLOW));
            }
            else if((connectionType & IDiagramModelConnection.ARROW_LINE_TARGET) != 0) {
                fValue = IDiagramModelConnection.ARROW_LINE_TARGET;
                fButton.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ARROW_TARGET_LINE));
            }
            else {
                fValue = IDiagramModelConnection.ARROW_NONE;
                fButton.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.LINE_SOLID));
            }
        }
    }

}
