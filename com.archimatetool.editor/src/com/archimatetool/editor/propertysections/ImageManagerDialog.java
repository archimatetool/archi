/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
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
    
    private static final int DEFAULT_GALLERY_ITEM_SIZE = 120;
    private static final int MIN_GALLERY_ITEM_SIZE = 64;
    private static final int MAX_GALLERY_ITEM_SIZE = 256;
    
    // Model State holding model and image paths
    private class ModelImagePathInfo {
        private List<String> imagePaths;
        private IArchimateModel model;

        ModelImagePathInfo(IArchimateModel model)  {
            this.model = model;
            IArchiveManager archiveManager = (IArchiveManager)model.getAdapter(IArchiveManager.class);
            imagePaths = new ArrayList<>(archiveManager.getImagePaths());
            
            // Sort by size - smallest first
            imagePaths.sort((imagePath1, imagePath2) -> {
                byte[] bytes1 = archiveManager.getBytesFromEntry(imagePath1);
                byte[] bytes2 = archiveManager.getBytesFromEntry(imagePath2);
                int size1 = bytes1 != null ? bytes1.length : 0;
                int size2 = bytes2 != null ? bytes2.length : 0;
                return size1 - size2;
            });
        }
        
        IArchimateModel getModel() {
            return model;
        }
        
        List<String> getImagePaths() {
            return imagePaths;
        }
    }
    
    private Gallery fGallery;

    // Don't set these to null on dispose!
    private IArchimateModel fSelectedModel;
    private String fSelectedImagePath;
    private File fSelectedFile;
    
    // Map of Models to Model Image Path Info
    private Map<IArchimateModel, ModelImagePathInfo> fModelImagePathInfos = new HashMap<>();
    
    // The model image path info of the currently selected model
    private ModelImagePathInfo fSelectedModelPathInfo;
    
    private Map<String, Image> fImageCache = new HashMap<>();

    /**
     * @param parentShell
     * @param selectedModel The selected model in the model viewer
     * @param selectedImagePath The selected image path in the Gallery. Can be null
     */
    public ImageManagerDialog(Shell parentShell, IArchimateModel selectedModel, String selectedImagePath) {
        super(parentShell, "ImageManagerDialog"); //$NON-NLS-1$
        setTitleImage(IArchiImages.ImageFactory.getImage(IArchiImages.ECLIPSE_IMAGE_NEW_WIZARD));
        setShellStyle(getShellStyle() | SWT.RESIZE);
        
        fSelectedModel = selectedModel;
        fSelectedImagePath = selectedImagePath;
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
        
        Composite tableColumnComp = new Composite(tableComp, SWT.NULL);
        tableColumnComp.setLayout(new TableColumnLayout(true));
        tableColumnComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        ModelsViewer modelsViewer = new ModelsViewer(tableColumnComp);
        modelsViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        modelsViewer.setInput(this);
        
        // Models Tree Selection
        modelsViewer.addSelectionChangedListener(event -> {
            Object selected = event.getStructuredSelection().getFirstElement();
            if(selected instanceof IArchimateModel model
                                  && (fSelectedModelPathInfo == null || fSelectedModelPathInfo.getModel() != model)) {
                disposeItems();
                updateGallery(model);
            }
            else if(selected instanceof String) {
                disposeItems();
                fSelectedModelPathInfo = null;
            }
        });
        
        // Mouse Up action...
        // Select the open from file table element
        modelsViewer.getControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                TableItem item = modelsViewer.getTable().getItem(new Point(e.x, e.y));
                
                // Open file...
                if(item != null && item.getData() instanceof String) {
                    handleOpenAction();
                }
            }
        });
        
        // Press Return on table and selection is "Open from file"
        modelsViewer.getControl().addTraverseListener(event -> {
            if(event.detail == SWT.TRAVERSE_RETURN && modelsViewer.getStructuredSelection().getFirstElement() instanceof String) {
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
        
        fGallery = new Gallery(galleryComposite, SWT.V_SCROLL | SWT.VIRTUAL | SWT.BORDER);
        fGallery.setVirtualGroups(true);
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
        
        // Virtual gallery listens to SWT.SetData events
        fGallery.addListener(SWT.SetData, this::setData);
        
        // Double-clicks
        fGallery.addListener(SWT.MouseDoubleClick, event -> {
            GalleryItem item = fGallery.getItem(new Point(event.x, event.y));
            if(item != null) {
                okPressed();
            }
        });
        
        // Dispose of the images here not in the main dispose() method because if the help system is showing then 
        // the TrayDialog is resized and this control is asked to relayout.
        // Also because Gallery does not dispose properly if virtual.
        fGallery.addDisposeListener(event -> {
            disposeImages();
            disposeItems(); // This is important!
            fModelImagePathInfos = null;
            fSelectedModelPathInfo = null;
            fGallery = null;
        });

        // Slider
        Scale scale = new Scale(galleryComposite, SWT.HORIZONTAL);
        gd = new GridData(SWT.END, SWT.NONE, false, false);
        gd.widthHint = 120;
        scale.setLayoutData(gd);
        scale.setMinimum(MIN_GALLERY_ITEM_SIZE);
        scale.setMaximum(MAX_GALLERY_ITEM_SIZE);
        scale.setIncrement(8);
        scale.setPageIncrement(32);
        scale.setSelection(DEFAULT_GALLERY_ITEM_SIZE);
        scale.addSelectionListener(widgetSelectedAdapter(event -> {
            int inc = scale.getSelection();
            groupRenderer.setItemSize(inc, inc);
        }));

        sash.setWeights(new int[] { 30, 70 });
        
        // If we have this model in the table, select it
        if(fSelectedModel != null && ((ModelsViewerContentProvider)modelsViewer.getContentProvider()).getModels().contains(fSelectedModel)) {
            modelsViewer.setSelection(new StructuredSelection(fSelectedModel));
        }
        // Else select the first model in the table, if there is one
        else {
            Object element = modelsViewer.getElementAt(0);
            if(element != null) {
                modelsViewer.setSelection(new StructuredSelection(element), true);
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
     * Update the gallery from the selected model
     */
    private void updateGallery(IArchimateModel model) {
        // Set current Model Path Info
        fSelectedModelPathInfo = model != null ? fModelImagePathInfos.computeIfAbsent(model, m -> new ModelImagePathInfo(model)) : null;

        // Create one group (this will call SetData);
        fGallery.setItemCount(1);
    }

    /**
     * This is called from the SWT.SetData event
     */
    private void setData(Event event) {
        if(fSelectedModelPathInfo == null) {
            return;
        }
        
        GalleryItem item = (GalleryItem)event.item;
        
        // It's a group, so set child item count
        if(item.getParentItem() == null) {
            item.setItemCount(fSelectedModelPathInfo.getImagePaths().size());
        }
        else {
            // Index of GalleryItem
            int index = item.getParentItem().indexOf(item);
            
            // Get the image path from the list of paths for the current model by index
            String imagepath = fSelectedModelPathInfo.getImagePaths().get(index);
            
            // Make the initial selection of the corresponding GalleryItem if initially set and we haven't already selected an item
            if(fGallery.getSelection().length == 0 && Objects.equals(fSelectedImagePath, imagepath)) {
                fGallery.setSelection(new GalleryItem[] { item });
            }
            
            Display.getDefault().asyncExec(() -> {
                if(!item.getParent().isDisposed() && !item.isDisposed()) {
                    // Create image and cache it
                    // Prefix model id to the image path in case the image path is the same for a different image in another model
                    Image thumbnail = fImageCache.computeIfAbsent(fSelectedModelPathInfo.getModel().getId() + imagepath, p -> {
                        try {
                            IArchiveManager archiveManager = (IArchiveManager)fSelectedModelPathInfo.getModel().getAdapter(IArchiveManager.class);
                            return archiveManager.createImage(imagepath);
                        }
                        catch(Exception ex) {
                            ex.printStackTrace();
                            return null;
                        }
                    });
                    
                    if(thumbnail != null) {
                        item.setImage(thumbnail);
                        item.setData("imagepath", imagepath); //$NON-NLS-1$
                        item.setData("model", fSelectedModelPathInfo.getModel()); //$NON-NLS-1$
                    }
                }
            });
        }
    }
    
    @Override
    protected void okPressed() {
        // Set these to null
        fSelectedModel = null;
        fSelectedImagePath = null;
        
        // If a file has not been selected take what's selected in the Gallery
        if(fSelectedFile == null && fGallery.getSelection().length > 0) {
            GalleryItem item = fGallery.getSelection()[0];
            if(item != null) {
                fSelectedImagePath = (String)item.getData("imagepath"); //$NON-NLS-1$
                fSelectedModel = (IArchimateModel)item.getData("model"); //$NON-NLS-1$
            }
        }
        
        super.okPressed();
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
            fSelectedFile = new File(path);
            okPressed();
        }
        else {
            getShell().setVisible(true);
            getShell().setFocus(); // Need to set focus again
        }
    }
    
    /**
     * @return The selected file if any,
     *         If this is not null use it in preference to the selected image path
     */
    public File getSelectedFile() {
        return fSelectedFile;
    }
    
    /**
     * @return The selected image path if any
     */
    public String getSelectedImagePath() {
        return fSelectedImagePath;
    }
    
    /**
     * @return The selected model if any
     */
    public IArchimateModel getSelectedModel() {
        return fSelectedModel;
    }

    /**
     * Dispose all items
     */
    private void disposeItems() {
        if(fGallery != null && !fGallery.isDisposed()) {
            for(GalleryItem item : fGallery.getItems()) {
                item.dispose();
            }
        } 
    }
    
    private void disposeImages() {
        for(Image image : fImageCache.values()) {
            if(image != null) {
                image.dispose();
            }
        }
        
        fImageCache.clear();
        fImageCache = null;
    }
    
    // ====================================================================================================
    // Table Viewer for models
    // ====================================================================================================
    
    private class ModelsViewer extends TableViewer {
        public ModelsViewer(Composite parent) {
            super(parent, SWT.FULL_SELECTION);
            
            getTable().setHeaderVisible(false);
            
            // Use layout from parent container
            TableColumnLayout layout = (TableColumnLayout)getControl().getParent().getLayout();
            TableViewerColumn column = new TableViewerColumn(this, SWT.NONE);
            layout.setColumnData(column.getColumn(), new ColumnWeightData(100, false));
            
            setContentProvider(new ModelsViewerContentProvider());
            setLabelProvider(new ModelsViewerLabelProvider());
            
            setComparator(new ViewerComparator() {
                @Override
                public int category(Object element) {
                    return element instanceof String ? 1 : 0; // String is open from file
                }
            });
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
