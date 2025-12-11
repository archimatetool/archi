/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.widgets.gallery.CustomDefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.components.ExtendedTitleAreaDialog;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.INameable;



/**
 * Image Manager Dialog
 * 
 * @author Phillip Beauvoir
 */
public class ImageManagerDialog extends ExtendedTitleAreaDialog {
    
    private static String HELP_ID = "com.archimatetool.help.ImageManagerDialog"; //$NON-NLS-1$
    
    public static final int DEFAULT_GALLERY_ITEM_SIZE = 96;
    public static final int MIN_GALLERY_ITEM_SIZE = 64;
    public static final int MAX_GALLERY_ITEM_SIZE = 256;
    
    protected Gallery fGallery;
    protected GalleryItem fGalleryRoot;
    protected Scale fScale;
    protected ModelsViewer fModelsViewer;

    private IArchimateModel fUserSelectedModel;
    private String fUserSelectedImagePath;
    private File fUserSelectedFile;
    
    private Map<String, Image> fImageCache = new HashMap<>();

    public ImageManagerDialog(Shell parentShell) {
        super(parentShell, "ImageManagerDialog"); //$NON-NLS-1$
        setTitleImage(IArchiImages.ImageFactory.getImage(IArchiImages.ECLIPSE_IMAGE_NEW_WIZARD));
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }
    
    /**
     * Set the initial selected model and image path, if any
     */
    public void setSelected(IArchimateModel model, String imagePath) {
        fUserSelectedModel = model;
        fUserSelectedImagePath = imagePath;
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.ImageManagerDialog_1);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        setTitle(Messages.ImageManagerDialog_1);
        setMessage(Messages.ImageManagerDialog_2);
        Composite composite = (Composite)super.createDialogArea(parent);

        Composite client = new Composite(composite, SWT.NULL);
        GridLayout layout = new GridLayout(2, false);
        client.setLayout(layout);
        client.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        SashForm sash = new SashForm(client, SWT.HORIZONTAL);
        sash.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 580;
        gd.heightHint = 300;
        sash.setLayoutData(gd);
        
        // Table
        Composite tableComp = new Composite(sash, SWT.BORDER);
        layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        tableComp.setLayout(layout);
        
        CLabel label = new CLabel(tableComp, SWT.NULL);
        label.setText(Messages.ImageManagerDialog_3);
        
        Composite tableComp2 = new Composite(tableComp, SWT.NULL);
        tableComp2.setLayout(new TableColumnLayout(true));
        tableComp2.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        fModelsViewer = new ModelsViewer(tableComp2);
        fModelsViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        fModelsViewer.setInput(""); //$NON-NLS-1$
        fModelsViewer.addSelectionChangedListener(event -> {
            if(event.getStructuredSelection().getFirstElement() instanceof IArchimateModel model) {
                fScale.setEnabled(true);
                clearGallery();
                updateGallery(model);
            }
        });
        
        // Mouse Up action...
        // Select the open from file table element
        fModelsViewer.getControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                // Open file...
                if(fModelsViewer.getStructuredSelection().getFirstElement() instanceof String) {
                    handleOpenAction();
                }
            }
        });
        
        // Press Return on table and selection is "Open from file"
        fModelsViewer.getControl().addTraverseListener(event -> {
            if(event.detail == SWT.TRAVERSE_RETURN && fModelsViewer.getStructuredSelection().getFirstElement() instanceof String) {
                event.doit = false;
                event.detail = SWT.TRAVERSE_NONE;
                handleOpenAction();
            }
        });
        
        Composite galleryComposite = new Composite(sash, SWT.NULL);
        layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        galleryComposite.setLayout(layout);
        
        fGallery = new Gallery(galleryComposite, SWT.V_SCROLL | SWT.BORDER);
        fGallery.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // If the Gallery has the focus pressing Return or Esc does not close the dialog
        fGallery.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.keyCode == SWT.CR) {
                    okPressed();
                }
                if(e.keyCode == SWT.ESC) {
                    cancelPressed();
                }
            }
        });
        
        // Renderers
        NoGroupRenderer groupRenderer = new NoGroupRenderer();
        groupRenderer.setItemSize(DEFAULT_GALLERY_ITEM_SIZE, DEFAULT_GALLERY_ITEM_SIZE);
        groupRenderer.setAutoMargin(true);
        groupRenderer.setMinMargin(10);
        fGallery.setGroupRenderer(groupRenderer);
        
        CustomDefaultGalleryItemRenderer itemRenderer = new CustomDefaultGalleryItemRenderer();
        itemRenderer.setShowRoundedSelectionCorners(true);
        fGallery.setItemRenderer(itemRenderer);
        
        // Root Group
        fGalleryRoot = new GalleryItem(fGallery, SWT.NONE);
        
        // Slider
        fScale = new Scale(galleryComposite, SWT.HORIZONTAL);
        gd = new GridData(SWT.END, SWT.NONE, false, false);
        gd.widthHint = 120;
        fScale.setLayoutData(gd);
        fScale.setMinimum(MIN_GALLERY_ITEM_SIZE);
        fScale.setMaximum(MAX_GALLERY_ITEM_SIZE);
        fScale.setIncrement(8);
        fScale.setPageIncrement(32);
        fScale.setSelection(DEFAULT_GALLERY_ITEM_SIZE);
        fScale.setEnabled(false);
        fScale.addSelectionListener(widgetSelectedAdapter(event -> {
            int inc = fScale.getSelection();
            itemRenderer.setDropShadows(inc >= 96);
            groupRenderer.setItemSize(inc, inc);
        }));

        // Gallery selections
        fGallery.addSelectionListener(widgetSelectedAdapter(event -> {
            if(event.item instanceof GalleryItem) {
                fUserSelectedImagePath = (String)event.item.getData("imagepath"); //$NON-NLS-1$
                fUserSelectedModel = (IArchimateModel)event.item.getData("model"); //$NON-NLS-1$
            }
            else {
                fUserSelectedImagePath = null;
                fUserSelectedModel = null;
            }
        }));
        
        // Double-clicks
        fGallery.addListener(SWT.MouseDoubleClick, event -> {
            GalleryItem item = fGallery.getItem(new Point(event.x, event.y));
            if(item != null) {
                okPressed();
            }
        });
        
        // Dispose of the images here not in the main dispose() method because if the help system is showing then 
        // the TrayDialog is resized and this control is asked to relayout.
        fGallery.addDisposeListener(event -> {
            disposeImages();
        });

        sash.setWeights(new int[] { 30, 70 });
        
        // If we have this model in the table, select it
        if(fUserSelectedModel != null && ((ModelsViewerContentProvider)fModelsViewer.getContentProvider()).getModels().contains(fUserSelectedModel)) {
            fModelsViewer.setSelection(new StructuredSelection(fUserSelectedModel));

            // Make selection of image path if it's set
            if(fUserSelectedImagePath != null) {
                for(GalleryItem item : fGalleryRoot.getItems()) {
                    String imagePath = (String)item.getData("imagepath"); //$NON-NLS-1$
                    if(imagePath != null && fUserSelectedImagePath.equals(imagePath)) {
                        fGallery.setSelection(new GalleryItem[] { item });
                        break;
                    }
                }
            }
        }
        // Else select the first model in the table, if there is one
        else {
            Object element = fModelsViewer.getElementAt(0);
            if(element != null) {
                fModelsViewer.setSelection(new StructuredSelection(element), true);
            }
        }
        
        // Bug on Mac. If the dialog is not very high the CLabel is not visible so this forces a layout
        if(PlatformUtils.isMac()) {
            parent.getDisplay().asyncExec(() -> {
                tableComp.layout();
            });
        }
        
        return composite;
    }
    
    /**
     * Clear old root group
     */
    private void clearGallery() {
        if(fGalleryRoot != null && !fGalleryRoot.isDisposed()) {
            for(GalleryItem item : fGalleryRoot.getItems()) {
                item.dispose();
            }
        }
    }

    private void updateGallery(final IArchimateModel model) {
        BusyIndicator.showWhile(null, () -> {
            IArchiveManager archiveManager = (IArchiveManager)model.getAdapter(IArchiveManager.class);

            for(String path : archiveManager.getImagePaths()) {
                // Create image and cache it
                Image thumbnail = fImageCache.computeIfAbsent(path, p -> {
                    try {
                        return archiveManager.createImage(path);
                    }
                    catch(Exception ex) {
                        ex.printStackTrace();
                        return null;
                    }
                });

                if(thumbnail != null) {
                    GalleryItem item = new GalleryItem(fGalleryRoot, SWT.NONE);
                    item.setImage(thumbnail);
                    item.setData("imagepath", path); //$NON-NLS-1$
                    item.setData("model", model); //$NON-NLS-1$
                }
            }
            
            fGallery.redraw(); // at some scale settings this is needed
        });
    }

    /**
     * User wants to open Image from file
     */
    private void handleOpenAction() {
        getShell().setVisible(false);

        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { "*.png;*.jpg;*.jpeg;*.gif;*.tif;*.tiff;*.bmp;*.ico", "*.*" } ); //$NON-NLS-1$ //$NON-NLS-2$
        String path = dialog.open();
        if(path != null) {
            fUserSelectedFile = new File(path);
            fUserSelectedImagePath = null;
            fUserSelectedModel = null;
            okPressed();
        }
        else {
            getShell().setVisible(true);
            getShell().setFocus(); // Nees to set focus again
        }
    }
    
    /**
     * @return The user selected file
     */
    public File getUserSelectedFile() {
        return fUserSelectedFile;
    }
    
    /**
     * @return The user selected image path
     */
    public String getUserSelectedImagePath() {
        return fUserSelectedImagePath;
    }
    
    /**
     * @return The user selected model
     */
    public IArchimateModel getUserSelectedModel() {
        return fUserSelectedModel;
    }

    private void disposeImages() {
        for(Image image : fImageCache.values()) {
            if(image != null) {
                image.dispose();
            }
        }
    }
    
    // ====================================================================================================
    // Table Viewer for models
    // ====================================================================================================
    
    private class ModelsViewer extends TableViewer {
        public ModelsViewer(Composite parent) {
            super(parent, SWT.FULL_SELECTION);
            
            setColumns();
            setContentProvider(new ModelsViewerContentProvider());
            setLabelProvider(new ModelsViewerLabelProvider());
            setComparator(new ViewerComparator() {
                @Override
                public int category(Object element) {
                    return element instanceof String ? 1 : 0; // String is open from file
                }
            });
        }
        
        private void setColumns() {
            Table table = getTable();
            table.setHeaderVisible(false);
            
            // Use layout from parent container
            TableColumnLayout layout = (TableColumnLayout)getControl().getParent().getLayout();
            TableViewerColumn column = new TableViewerColumn(this, SWT.NONE);
            layout.setColumnData(column.getColumn(), new ColumnWeightData(100, false));
        }
    }
    
    private class ModelsViewerContentProvider implements IStructuredContentProvider {
        private Set<IArchimateModel> models;
        
        @Override
        public Object[] getElements(Object inputElement) {
            Set<Object> list = new HashSet<>(getModels());
            list.add(Messages.ImageManagerDialog_0);
            return list.toArray();
        }
        
        public Set<IArchimateModel> getModels() {
            if(models == null) {
                models = new HashSet<>();
                
                // Add all models that have images
                for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {
                    IArchiveManager archiveManager = (IArchiveManager)model.getAdapter(IArchiveManager.class);
                    if(archiveManager != null && archiveManager.hasImages()) {
                        models.add(model);
                    }
                }
            }
            return models;
        }
    }

    private class ModelsViewerLabelProvider extends LabelProvider {
        @Override
        public String getText(Object element) {
            return element instanceof INameable nameable ? nameable.getName() : super.getText(element);
        }
        
        @Override
        public Image getImage(Object element) {
            return ArchiLabelProvider.INSTANCE.getImage(element);
        }
    }
}
