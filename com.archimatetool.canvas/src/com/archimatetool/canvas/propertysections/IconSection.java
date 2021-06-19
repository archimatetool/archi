/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.propertysections;

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

import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.canvas.model.IIconic;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.propertysections.DiagramModelImageSection;
import com.archimatetool.editor.propertysections.IObjectFilter;
import com.archimatetool.editor.propertysections.ITabbedLayoutConstants;
import com.archimatetool.editor.propertysections.ObjectFilter;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.model.IArchimatePackage;



/**
 * Property Section for an Icon
 * 
 * @author Phillip Beauvoir
 */
public class IconSection extends DiagramModelImageSection {
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IIconic;
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
    };
    
    
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
                if(!isLocked(getFirstSelectedObject())) {
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
                if(event.data instanceof String[]) {
                    if(!isLocked(getFirstSelectedObject())) {
                        File file = new File(((String[])event.data)[0]);
                        setImage(file);
                    }
                }
            }
        });
        
        // Image Button
        createImageButton(parent);
        
        // Position
        createLabel(parent, Messages.IconSection_11, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);

        fComboPosition = new Combo(parent, SWT.READ_ONLY);
        fComboPosition.setItems(fComboPositionItems);
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
                                ICanvasPackage.Literals.ICONIC__IMAGE_POSITION,
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
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH) {
                refreshPreviewImage();
            }
            else if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED
                    || feature == ICanvasPackage.Literals.ICONIC__IMAGE_POSITION) {
                refreshButtons();
            }
        }
    }
    
    @Override
    protected void update() {
        refreshPreviewImage();
        refreshButtons();
    }
    
    private void refreshPreviewImage() {
        disposeImage();
        
        IIconic iconic = (IIconic)getFirstSelectedObject();
        
        if(iconic.getImagePath() != null) {
            IArchiveManager archiveManager = (IArchiveManager)iconic.getAdapter(IArchiveManager.class);
            fImage = archiveManager.createImage(iconic.getImagePath());
        }
        
        fCanvas.redraw();
    }
    
    @Override
    protected void refreshButtons() {
        super.refreshButtons();
        
        IIconic iconic = (IIconic)getFirstSelectedObject();
        
        int position = iconic.getImagePosition();
        if(position < IIconic.ICON_POSITION_TOP_LEFT || position > IIconic.ICON_POSITION_BOTTOM_RIGHT) {
            position = IIconic.ICON_POSITION_TOP_RIGHT;
        }
        
        if(!fIsExecutingCommand) {
            fComboPosition.select(position);
            fComboPosition.setEnabled(!isLocked(iconic));
        }
    }
    
    private void disposeImage() {
        if(fImage != null && !fImage.isDisposed()) {
            fImage.dispose();
            fImage = null;
        }
    }
}
