/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import java.io.File;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.figures.IconicDelegate;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IIconic;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.util.LightweightEContentAdapter;



/**
 * Property Section for an User Image Icon
 * 
 * @author Phillip Beauvoir
 */
public class IconSection extends ImageChooserSection {
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            // Should be IIconic and exposes image path feature
            return object instanceof IIconic && shouldExposeFeature((EObject)object,
                    IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH.getName());
        }

        @Override
        public Class<?> getAdaptableType() {
            return IIconic.class;
        }
    }

    private Image fImage;
    private Canvas fCanvas;
    private Combo fComboPosition;
    
    private static final int IMAGE_SIZE = 100;
    
    private static final String[] fComboPositionItems = {
        Messages.IconSection_0,
        Messages.IconSection_1,
        Messages.IconSection_2,
        Messages.IconSection_3,
        Messages.IconSection_4,
        Messages.IconSection_5,
        Messages.IconSection_6,
        Messages.IconSection_7,
        Messages.IconSection_8,
        Messages.IconSection_13
    };
    
    /**
     * Adapter to listen to Model's Profile changes
     */
    private LightweightEContentAdapter eAdapter = new LightweightEContentAdapter(this::notifyChanged, IProfile.class);
    
    /**
     * Model that we are listening to changes on
     * Store this model in case the selected object is deleted
     */
    private IArchimateModel fModel;
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.IconSection_9, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        fCanvas = new Canvas(parent, SWT.BORDER);
        getWidgetFactory().adapt(fCanvas);
        GridData gd = new GridData(SWT.NONE, SWT.NONE, false, false);
        gd.widthHint = IMAGE_SIZE;
        gd.heightHint = IMAGE_SIZE;
        fCanvas.setLayoutData(gd);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        fCanvas.setLayout(layout);
        
        fCanvas.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                disposeImage();
            }
        });
        
        fCanvas.addListener(SWT.MouseDoubleClick, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if(fImageButton.isEnabled()) {
                    chooseImage();
                }
            }
        });
        
        fCanvas.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                if(fImage != null) {
                    e.gc.setAntialias(SWT.ON);
                    e.gc.setInterpolation(SWT.HIGH);
                    
                    Rectangle imageBounds = fImage.getBounds();
                    Rectangle newSize = ImageFactory.getScaledImageSize(fImage, IMAGE_SIZE);
                    
                    // Centre the image
                    int x = (IMAGE_SIZE - newSize.width) / 2;
                    int y = (IMAGE_SIZE - newSize.height) / 2;
                    
                    e.gc.drawImage(fImage, 0, 0, imageBounds.width, imageBounds.height,
                            x, y, newSize.width, newSize.height);
                }
            }
        });
        
        String tooltip = Messages.IconSection_10;
        fCanvas.setToolTipText(tooltip);
        
        DropTarget target = new DropTarget(fCanvas, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT);
        target.setTransfer(new Transfer[] { FileTransfer.getInstance() } );
        
        target.addDropListener(new DropTargetAdapter() {
            @Override
            public void drop(DropTargetEvent event) {
                if(event.data instanceof String[] && fImageButton.isEnabled()) {
                    File file = new File(((String[])event.data)[0]);
                    setImage(file);
                }
            }
        });
        
        // Image Button
        createImageButton(parent);
        
        // Position
        createLabel(parent, Messages.IconSection_11, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);

        fComboPosition = new Combo(parent, SWT.READ_ONLY);
        fComboPosition.setItems(fComboPositionItems);
        getWidgetFactory().adapt(fComboPosition, true, true);
        gd = new GridData(SWT.NONE, SWT.NONE, false, false);
        fComboPosition.setLayoutData(gd); 
        fComboPosition.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject iconic : getEObjects()) {
                    if(isAlive(iconic)) {
                        Command cmd = new EObjectFeatureCommand(Messages.IconSection_12,
                                iconic,
                                IArchimatePackage.Literals.ICONIC__IMAGE_POSITION,
                                fComboPosition.getSelectionIndex());
                        
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        });
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }

    @Override
    protected void notifyChanged(Notification msg) {
        Object feature = msg.getFeature();
        
        // Profile might have been added or removed from the Model or the underlying Archimate element which might affect the image
        // Or Image Path or Source changed
        if(feature == IArchimatePackage.Literals.ARCHIMATE_MODEL__PROFILES
                || feature == IArchimatePackage.Literals.PROFILES__PROFILES
                || feature == IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH
                || isFeatureNotification(msg, IDiagramModelArchimateObject.FEATURE_IMAGE_SOURCE)) {
            refreshPreviewImage();
        }
        
        if(msg.getNotifier() == getFirstSelectedObject()) {
            if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED
                    || feature == IArchimatePackage.Literals.ICONIC__IMAGE_POSITION
                    || isFeatureNotification(msg, IDiagramModelArchimateObject.FEATURE_IMAGE_SOURCE)) {
                refreshButton();
            }
        }
    }
    
    @Override
    protected void addAdapter() {
        super.addAdapter();
        
        // Add our adapter to listen to underlying ArchimateElement if there is one
        IArchimateModelObject selected = getFirstSelectedObject();
        if(selected instanceof IDiagramModelArchimateObject) {
            IArchimateElement element = ((IDiagramModelArchimateObject)selected).getArchimateElement();
            if(!element.eAdapters().contains(eAdapter)) {
                element.eAdapters().add(eAdapter);
            }
            // And the model itself
            if(element.getArchimateModel() != null && !element.getArchimateModel().eAdapters().contains(eAdapter)) {
                fModel = element.getArchimateModel();
                fModel.eAdapters().add(eAdapter);
            }
        }
    }
    
    @Override
    protected void removeAdapter() {
        super.removeAdapter();
        
        // Remove our adapter to underlying ArchimateElement if there is one
        IArchimateModelObject selected = getFirstSelectedObject();
        if(selected instanceof IDiagramModelArchimateObject) {
            IArchimateElement element = ((IDiagramModelArchimateObject)selected).getArchimateElement();
            element.eAdapters().remove(eAdapter);
        }

        // Remove our adapter from the model
        if(fModel != null) {
            fModel.eAdapters().remove(eAdapter);
            fModel = null;
        }
    }
    
    @Override
    protected void update() {
        refreshPreviewImage();
        refreshButton();
    }
    
    private void refreshPreviewImage() {
        // Check also if the selected object has been orphaned in case the Properties View is still showing the object if it has the focus
        if(isAlive(getFirstSelectedObject())) {
            disposeImage();
            
            // Use an IconicDelegate to create the image which may come from the object or via a profile image
            IconicDelegate iconicDelegate = new IconicDelegate((IIconic)getFirstSelectedObject());
            iconicDelegate.updateImage();
            fImage = iconicDelegate.getImage();
            
            fCanvas.redraw();
        }
    }
    
    @Override
    protected void refreshButton() {
        super.refreshButton();
        
        IArchimateModelObject selected = getFirstSelectedObject();
        
        // If this is an ArchiMate element and we are not using a custom image then disable this
        if(selected instanceof IDiagramModelArchimateObject) {
            int source = ((IDiagramModelArchimateObject)selected).getImageSource();
            fImageButton.setEnabled(source == IDiagramModelArchimateObject.IMAGE_SOURCE_CUSTOM  && !isLocked(selected));
        }
        
        int position = ((IIconic)selected).getImagePosition();
        if(position < IIconic.ICON_POSITION_TOP_LEFT || position > IIconic.ICON_POSITION_FILL) {
            position = IIconic.ICON_POSITION_TOP_RIGHT;
        }
        
        if(!fIsExecutingCommand) {
            fComboPosition.select(position);
            fComboPosition.setEnabled(!isLocked(selected));
        }
    }
    
    private void disposeImage() {
        if(fImage != null && !fImage.isDisposed()) {
            fImage.dispose();
            fImage = null;
        }
    }
    
    @Override
    public void dispose() {
        super.dispose();  // super first
        disposeImage();
        eAdapter = null;
        fModel = null;
    }
}
