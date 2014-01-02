/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
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

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.model.IDiagramModelImageProvider;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILockable;



/**
 * Property Section for an Diagram Model Image
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelImageSection extends AbstractArchimatePropertySection {
    
    protected static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

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
        createLabel(parent, Messages.DiagramModelImageSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fImageButton = new Button(parent, SWT.PUSH);
        fImageButton.setText(" " + Messages.DiagramModelImageSection_1); //$NON-NLS-1$
        getWidgetFactory().adapt(fImageButton, true, true); // Need to do it this way for Mac
        GridData gd = new GridData(SWT.NONE, SWT.NONE, true, false);
        gd.minimumWidth = ITabbedLayoutConstants.COMBO_WIDTH;
        fImageButton.setLayoutData(gd);
        fImageButton.setAlignment(SWT.LEFT);
        fImageButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                MenuManager menuManager = new MenuManager();
                
                IAction actionChoose = new Action(Messages.DiagramModelImageSection_2) {
                    @Override
                    public void run() {
                        chooseImage();
                    }
                };
                
                menuManager.add(actionChoose);
                
                IAction actionClear = new Action(Messages.DiagramModelImageSection_3) {
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
            throw new RuntimeException("Object was null"); //$NON-NLS-1$
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
            getCommandStack().execute(new EObjectFeatureCommand(Messages.DiagramModelImageSection_4,
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
            // User selected a file
            if(selected instanceof File) {
                File file = (File)selected;
                if(!file.exists() || !file.canRead()) {
                    return;
                }
                
                IArchiveManager archiveManager = (IArchiveManager)getEObject().getAdapter(IArchiveManager.class);
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
            ex.printStackTrace();
            MessageDialog.openError(getPart().getSite().getShell(),
                    Messages.DiagramModelImageSection_5,
                    Messages.DiagramModelImageSection_6);
            return;
        }
        
        fIsExecutingCommand = true;
        getCommandStack().execute(new EObjectFeatureCommand(Messages.DiagramModelImageSection_7,
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
