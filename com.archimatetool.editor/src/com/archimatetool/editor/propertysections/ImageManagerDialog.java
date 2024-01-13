/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.ui.components.ExtendedTitleAreaDialog;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.INameable;



/**
 * Image Manager Dialog
 * 
 * @author Phillip Beauvoir
 */
public class ImageManagerDialog extends ExtendedTitleAreaDialog {
    
    private static String HELP_ID = "com.archimatetool.help.ImageManagerDialog"; //$NON-NLS-1$
    
    protected static final String OPEN = Messages.ImageManagerDialog_0;
    
    protected int DEFAULT_GALLERY_ITEM_SIZE = 128;
    protected int MIN_GALLERY_ITEM_SIZE = 64;
    protected int MAX_GALLERY_ITEM_SIZE = 256;
    
    protected Gallery fGallery;
    protected GalleryItem fGalleryRoot;
    protected Scale fScale;
    protected ModelsViewer fModelsViewer;

    private IArchimateModel fUserSelectedModel;
    private String fUserSelectedImagePath;
    private File fUserSelectedFile;
    
    private Map<String, Image> fImageCache = new HashMap<String, Image>();

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
        tableComp2.setLayout(new TableColumnLayout());
        tableComp2.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        fModelsViewer = new ModelsViewer(tableComp2);
        fModelsViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        fModelsViewer.setInput(""); //$NON-NLS-1$
        fModelsViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                Object o = ((IStructuredSelection)event.getSelection()).getFirstElement();
                if(o instanceof IArchimateModel) {
                    fScale.setEnabled(true);
                    clearGallery();
                    updateGallery((IArchimateModel)o);
                }
            }
        });
        
        // Mouse Up action...
        fModelsViewer.getControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                Object o = ((IStructuredSelection)fModelsViewer.getSelection()).getFirstElement();
                // Open...
                if(o == OPEN) {
                    handleOpenAction();
                }
            }
        });

        Composite galleryComposite = new Composite(sash, SWT.NULL);
        layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        galleryComposite.setLayout(layout);
        
        fGallery = new Gallery(galleryComposite, SWT.V_SCROLL | SWT.BORDER);
        fGallery.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // Renderers
        final NoGroupRenderer groupRenderer = new NoGroupRenderer();
        groupRenderer.setItemSize(DEFAULT_GALLERY_ITEM_SIZE, DEFAULT_GALLERY_ITEM_SIZE);
        groupRenderer.setAutoMargin(true);
        groupRenderer.setMinMargin(10);
        fGallery.setGroupRenderer(groupRenderer);
        
        final DefaultGalleryItemRenderer itemRenderer = new DefaultGalleryItemRenderer();
        //itemRenderer.setDropShadows(true);
        //itemRenderer.setDropShadowsSize(7);
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
        fScale.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int inc = fScale.getSelection();
                itemRenderer.setDropShadows(inc >= 96);
                groupRenderer.setItemSize(inc, inc);
            }
        });

        // Gallery selections
        fGallery.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(e.item instanceof GalleryItem) {
                    fUserSelectedImagePath = (String)((GalleryItem)e.item).getData("imagepath"); //$NON-NLS-1$
                    fUserSelectedModel = (IArchimateModel)((GalleryItem)e.item).getData("model"); //$NON-NLS-1$
                }
                else {
                    fUserSelectedImagePath = null;
                    fUserSelectedModel = null;
                }
             }
        });
        
        // Double-clicks
        fGallery.addListener(SWT.MouseDoubleClick, new Listener() {
            @Override
            public void handleEvent(Event event) {
                GalleryItem item = fGallery.getItem(new Point(event.x, event.y));
                if(item != null) {
                    okPressed();
                }
            }
        });
        
        // Dispose of the images here not in the main dispose() method because if the help system is showing then 
        // the TrayDialog is resized and this control is asked to relayout.
        fGallery.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                disposeImages();
            }
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
        
        return composite;
    }
    
    /**
     * Clear old root group
     */
    private void clearGallery() {
        if(fGalleryRoot != null && !fGallery.isDisposed() && fGallery.getItemCount() > 0) {
            while(fGalleryRoot.getItemCount() > 0) {
                GalleryItem item = fGalleryRoot.getItem(0);
                fGalleryRoot.remove(item);
            }
        }
    }

    private void updateGallery(final IArchimateModel model) {
        BusyIndicator.showWhile(null, new Runnable() {
            @Override
            public void run() {
                IArchiveManager archiveManager = (IArchiveManager)model.getAdapter(IArchiveManager.class);
                
                for(String path : archiveManager.getImagePaths()) {
                    Image thumbnail = fImageCache.get(path);
                    
                    // Create image and cache it
                    if(thumbnail == null) {
                        thumbnail = archiveManager.createImage(path);
                        if(thumbnail != null) {
                            fImageCache.put(path, thumbnail);
                        }
                    }
                    
                    if(thumbnail != null) {
                        GalleryItem item = new GalleryItem(fGalleryRoot, SWT.NONE);
                        item.setImage(thumbnail);
                        item.setData("imagepath", path); //$NON-NLS-1$
                        item.setData("model", model); //$NON-NLS-1$
                    }
                }
                
                fGallery.redraw(); // at some scale settings this is needed
            }
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
        for(Entry<String, Image> entry : fImageCache.entrySet()) {
            Image image = entry.getValue();
            if(image != null && !image.isDisposed()) {
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
            
            // Mac Silicon Item height
            UIUtils.fixMacSiliconItemHeight(getTable());
            
            setColumns();
            setContentProvider(new ModelsViewerContentProvider());
            setLabelProvider(new ModelsViewerLabelProvider());
            setComparator(new ViewerComparator() {
                @Override
                public int category(Object element) {
                    if(element == OPEN) {
                        return 1;
                    }
                    return 0;
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
        private List<IArchimateModel> models;
        
        @Override
        public void dispose() {
        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

        @Override
        public Object[] getElements(Object inputElement) {
            List<Object> list = new ArrayList<Object>(getModels());
            list.add(OPEN);
            return list.toArray();
        }
        
        public List<IArchimateModel> getModels() {
            if(models == null) {
                models = new ArrayList<>();
                
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
            if(element instanceof INameable) {
                return ((INameable)element).getName();
            }
            return super.getText(element);
        }
        
        @Override
        public Image getImage(Object element) {
            return ArchiLabelProvider.INSTANCE.getImage(element);
        }
    }
}
