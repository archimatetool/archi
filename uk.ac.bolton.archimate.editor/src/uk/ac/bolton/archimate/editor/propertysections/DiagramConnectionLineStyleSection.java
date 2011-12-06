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
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.diagram.commands.ConnectionLineTypeCommand;
import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.LineConnectionEditPart;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.ILockable;


/**
 * Connection Line Style Property Section
 * 
 * @author Phillip Beauvoir
 */
public class DiagramConnectionLineStyleSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "uk.ac.bolton.archimate.help.elementPropertySection";

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter implements IFilter {
        @Override
        public boolean select(Object object) {
            return object instanceof LineConnectionEditPart;
        }
    }
    
    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Model event (Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__TYPE ||
                    feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshControls();
            }
        }
    };
    
    private IDiagramModelConnection fConnection;
    
    private StyleSelector fLineStyleSelector;
    private StyleSelector fSourceArrowSelector;
    private StyleSelector fTargetArrowSelector;
    
    @Override
    protected void createControls(Composite parent) {
        createCLabel(parent, "Line Style:", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
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
    protected void setElement(Object element) {
        if(element instanceof IAdaptable) {
            fConnection = (IDiagramModelConnection)((IAdaptable)element).getAdapter(IDiagramModelConnection.class);
        }
        else {
            throw new RuntimeException("Should have been an Edit Part");
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        fLineStyleSelector.update();
        fSourceArrowSelector.update();
        fTargetArrowSelector.update();
        
        boolean enabled = fConnection instanceof ILockable ? !((ILockable)fConnection).isLocked() : true;
        fLineStyleSelector.setEnabled(enabled);
        fSourceArrowSelector.setEnabled(enabled);
        fTargetArrowSelector.setEnabled(enabled);
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fConnection;
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
                    if(isAlive()) {
                        fValue = value;
                        getCommandStack().execute(new ConnectionLineTypeCommand(fConnection, getLineTypeValue()));
                    }
                }
                
                @Override
                public ImageDescriptor getImageDescriptor() {
                    return imageDesc;
                }
            };
            
            action.setChecked((fConnection.getType() & value) != 0);
            
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
            super(parent, "Set the line style");
        }

        @Override
        protected void addActions(MenuManager menuManager) {
            IAction action = createAction("Solid", IDiagramModelConnection.LINE_SOLID, IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.LINE_SOLID));
            menuManager.add(action);
            action.setChecked((fConnection.getType() & IDiagramModelConnection.LINE_DASHED) == 0 &
                              (fConnection.getType() & IDiagramModelConnection.LINE_DOTTED) == 0);
            
            action = createAction("Dashed", IDiagramModelConnection.LINE_DASHED, IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.LINE_DASHED));
            menuManager.add(action);

            action = createAction("Dotted", IDiagramModelConnection.LINE_DOTTED, IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.LINE_DOTTED));
            menuManager.add(action);
        }
        
        @Override
        protected void update() {
            int connectionType = fConnection.getType();
            
            if((connectionType & IDiagramModelConnection.LINE_DASHED) != 0) {
                fValue = IDiagramModelConnection.LINE_DASHED;
                fButton.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.LINE_DASHED));
            }
            else if((connectionType & IDiagramModelConnection.LINE_DOTTED) != 0) {
                fValue = IDiagramModelConnection.LINE_DOTTED;
                fButton.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.LINE_DOTTED));
            }
            else {
                fValue = IDiagramModelConnection.LINE_SOLID;
                fButton.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.LINE_SOLID));
            }
        }
    }

    protected class SourceArrowSelector extends StyleSelector {
        public SourceArrowSelector(Composite parent) {
            super(parent, "Set the line's tail arrow type");
        }

        @Override
        protected void addActions(MenuManager menuManager) {
            IAction action = createAction("None", IDiagramModelConnection.ARROW_NONE, IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.LINE_SOLID));
            action.setChecked((fConnection.getType() & IDiagramModelConnection.ARROW_FILL_SOURCE) == 0 &
                              (fConnection.getType() & IDiagramModelConnection.ARROW_HOLLOW_SOURCE) == 0 &
                              (fConnection.getType() & IDiagramModelConnection.ARROW_LINE_SOURCE) == 0);
            menuManager.add(action);
            
            action = createAction("Fill", IDiagramModelConnection.ARROW_FILL_SOURCE, IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ARROW_SOURCE_FILL));
            menuManager.add(action);

            action = createAction("Hollow", IDiagramModelConnection.ARROW_HOLLOW_SOURCE, IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ARROW_SOURCE_HOLLOW));
            menuManager.add(action);

            action = createAction("Line", IDiagramModelConnection.ARROW_LINE_SOURCE, IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ARROW_SOURCE_LINE));
            menuManager.add(action);
        }
        
        @Override
        protected void update() {
            int connectionType = fConnection.getType();
            
            if((connectionType & IDiagramModelConnection.ARROW_FILL_SOURCE) != 0) {
                fValue = IDiagramModelConnection.ARROW_FILL_SOURCE;
                fButton.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ARROW_SOURCE_FILL));
            }
            else if((connectionType & IDiagramModelConnection.ARROW_HOLLOW_SOURCE) != 0) {
                fValue = IDiagramModelConnection.ARROW_HOLLOW_SOURCE;
                fButton.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ARROW_SOURCE_HOLLOW));
            }
            else if((connectionType & IDiagramModelConnection.ARROW_LINE_SOURCE) != 0) {
                fValue = IDiagramModelConnection.ARROW_LINE_SOURCE;
                fButton.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ARROW_SOURCE_LINE));
            }
            else {
                fValue = IDiagramModelConnection.ARROW_NONE;
                fButton.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.LINE_SOLID));
            }
        }
    }
    
    protected class TargetArrowSelector extends StyleSelector {
        public TargetArrowSelector(Composite parent) {
            super(parent, "Set the line's head arrow type");
        }

        @Override
        protected void addActions(MenuManager menuManager) {
            IAction action = createAction("None", IDiagramModelConnection.ARROW_NONE, IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.LINE_SOLID));
            action.setChecked((fConnection.getType() & IDiagramModelConnection.ARROW_FILL_TARGET) == 0 &
                              (fConnection.getType() & IDiagramModelConnection.ARROW_HOLLOW_TARGET) == 0 &
                              (fConnection.getType() & IDiagramModelConnection.ARROW_LINE_TARGET) == 0);
            menuManager.add(action);
            
            action = createAction("Fill", IDiagramModelConnection.ARROW_FILL_TARGET, IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ARROW_TARGET_FILL));
            menuManager.add(action);

            action = createAction("Hollow", IDiagramModelConnection.ARROW_HOLLOW_TARGET, IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ARROW_TARGET_HOLLOW));
            menuManager.add(action);

            action = createAction("Line", IDiagramModelConnection.ARROW_LINE_TARGET, IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ARROW_TARGET_LINE));
            menuManager.add(action);
        }
        
        @Override
        protected void update() {
            int connectionType = fConnection.getType();
            
            if((connectionType & IDiagramModelConnection.ARROW_FILL_TARGET) != 0) {
                fValue = IDiagramModelConnection.ARROW_FILL_TARGET;
                fButton.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ARROW_TARGET_FILL));
            }
            else if((connectionType & IDiagramModelConnection.ARROW_HOLLOW_TARGET) != 0) {
                fValue = IDiagramModelConnection.ARROW_HOLLOW_TARGET;
                fButton.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ARROW_TARGET_HOLLOW));
            }
            else if((connectionType & IDiagramModelConnection.ARROW_LINE_TARGET) != 0) {
                fValue = IDiagramModelConnection.ARROW_LINE_TARGET;
                fButton.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ARROW_TARGET_LINE));
            }
            else {
                fValue = IDiagramModelConnection.ARROW_NONE;
                fButton.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.LINE_SOLID));
            }
        }
    }

}
