/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.propertysections;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.Logger;
import uk.ac.bolton.archimate.editor.model.IArchiveManager;
import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelImage;
import uk.ac.bolton.archimate.model.IDiagramModelImageProvider;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.ILockable;


/**
 * Property Section for an Diagram Model Image
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelImageSection extends AbstractArchimatePropertySection {
    
    protected static final String HELP_ID = "uk.ac.bolton.archimate.help.elementPropertySection";

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter implements IFilter {
        @Override
        public boolean select(Object object) {
            return (object instanceof EditPart) && ((EditPart)object).getModel() instanceof IDiagramModelImage;
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshButtons();
            }
        }
    };
    
    private IDiagramModelImage fDiagramModelImage;
    
    protected Button fImageButton;
    
    @Override
    protected void createControls(Composite parent) {
        createImageButton(parent);
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    /**
     * Create Image button
     * @param parent
     */
    protected void createImageButton(Composite parent) {
        createCLabel(parent, "Image:", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        fImageButton = new Button(parent, SWT.PUSH);
        getWidgetFactory().adapt(fImageButton, true, true); // Need to do it this way for Mac
        GridData gd = new GridData(SWT.NONE, SWT.NONE, false, false);
        gd.widthHint = 150;
        fImageButton.setLayoutData(gd);
        fImageButton.setAlignment(SWT.LEFT);
        fImageButton.setText(" Choose...");
        fImageButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                MenuManager menuManager = new MenuManager();
                
                IAction actionChoose = new Action("Set Image...") {
                    @Override
                    public void run() {
                        chooseImage();
                    }
                };
                
                menuManager.add(actionChoose);
                
                IAction actionClear = new Action("Remove Image") {
                    @Override
                    public void run() {
                        clearImage();
                    }
                };
                
                actionClear.setEnabled(((IDiagramModelImageProvider)getEObject()).getImagePath() != null);
                
                menuManager.add(actionClear);
                
                Menu menu = menuManager.createContextMenu(fImageButton.getShell());
                Point p = fImageButton.getParent().toDisplay(fImageButton.getBounds().x, fImageButton.getBounds().y + fImageButton.getBounds().height);
                menu.setLocation(p);
                menu.setVisible(true);
            }
        });
    }
    
    @Override
    protected void setElement(Object element) {
        if(element instanceof EditPart && ((EditPart)element).getModel() instanceof IDiagramModelImage) {
            fDiagramModelImage = (IDiagramModelImage)((EditPart)element).getModel();
        }

        if(fDiagramModelImage == null) {
            throw new RuntimeException("Object was null");
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        refreshButtons();
    }
    
    protected void refreshButtons() {
        boolean enabled = getEObject() instanceof ILockable ? !((ILockable)getEObject()).isLocked() : true;
        fImageButton.setEnabled(enabled);
    }
    
    protected void clearImage() {
        if(isAlive()) {
            fIsExecutingCommand = true;
            getCommandStack().execute(new EObjectFeatureCommand("Clear Image",
                    getEObject(), IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH,
                    null));
            fIsExecutingCommand = false;
        }
    }
    
    protected void chooseImage() {
        if(!isAlive()) {
            return;
        }
        
        ImageManagerDialog dialog = new ImageManagerDialog(getPart().getSite().getShell(),
                getEObject().getDiagramModel().getArchimateModel(),
                ((IDiagramModelImageProvider)getEObject()).getImagePath());
        
        if(dialog.open() == Window.OK) {
            setImage(dialog.getSelectedObject());
        }
    }
    
    protected void setImage(Object selected) {
        String path = null;
        
        try {
            IArchiveManager archiveManager = (IArchiveManager)getEObject().getAdapter(IArchiveManager.class);
            
            // User selected a file
            if(selected instanceof File) {
                File file = (File)selected;
                if(!file.exists() || !file.canRead()) {
                    return;
                }
                path = archiveManager.addImageFromFile(file);
            }
            // User selected a Gallery image path
            else if(selected instanceof String) {
                path = (String)selected;
            }
            // User selected nothing
            else {
                return;
            }
        }
        catch(IOException ex) {
            Logger.logError("Could not add image file", ex);
            ex.printStackTrace();
            return;
        }
        
        fIsExecutingCommand = true;
        getCommandStack().execute(new EObjectFeatureCommand("Set Image",
                                getEObject(), IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH,
                                path));
        fIsExecutingCommand = false;
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected IDiagramModelObject getEObject() {
        return fDiagramModelImage;
    }
}
