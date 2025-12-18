/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.wizard;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.io.File;
import java.io.IOException;
import java.text.Collator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.components.ExtendedWizardDialog;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.templates.dialog.TemplateManagerDialog;
import com.archimatetool.templates.impl.model.ArchimateTemplateManager;
import com.archimatetool.templates.model.ITemplate;
import com.archimatetool.templates.model.ITemplateGroup;
import com.archimatetool.templates.model.TemplateManager;



/**
 * New Model From Template Wizard Page
 * 
 * @author Phillip Beauvoir
 */
public abstract class NewModelFromTemplateWizardPage extends WizardPage {
    
    protected Gallery fGallery;
    protected GalleryItem fGalleryRoot;
    protected StyledText fDescriptionText;
    
    protected TemplateManager fTemplateManager;
    
    protected TableViewer fInbuiltTableViewer;
    protected TableViewer fUserTableViewer;
    
    protected ITemplate fSelectedTemplate;

    /*
     * Track and de-select Viewers so that only one has focus at a time
     */
    protected TableViewer fLastViewerFocus;
    
    protected int DEFAULT_GALLERY_ITEM_SIZE = 120;
    
    protected static final String OPEN = Messages.NewModelFromTemplateWizardPage_0;
    protected static final String MANAGE = Messages.NewModelFromTemplateWizardPage_1;
    
    public NewModelFromTemplateWizardPage(String pageName, TemplateManager templateManager) {
        super(pageName);
        fTemplateManager = templateManager;
        init();
        setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ECLIPSE_IMAGE_NEW_WIZARD));
    }

    protected abstract void init();
    
    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, getHelpID());

        GridData gd;
        
        SashForm sash1 = new SashForm(container, SWT.HORIZONTAL);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.widthHint = 800;
        gd.heightHint = 500;
        sash1.setLayoutData(gd);
        
        Composite tableComposite = new Composite(sash1, SWT.BORDER);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = 0;
        tableComposite.setLayout(layout);
        
        // Inbuilt Templates
        gd = new GridData(SWT.FILL, SWT.FILL, true, false);
        fInbuiltTableViewer = createGroupsTableViewer(tableComposite, Messages.NewModelFromTemplateWizardPage_2, gd);
        fInbuiltTableViewer.setInput(new Object[] { fTemplateManager.getInbuiltTemplateGroup(), OPEN, MANAGE });
        
        // Mouse UP actions...
        fInbuiltTableViewer.getControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                TableItem item = fInbuiltTableViewer.getTable().getItem(new Point(e.x, e.y));
                if(item != null) {
                    // Open...
                    if(item.getData() == OPEN) {
                        handleOpenAction();
                    }
                    // Manage...
                    else if(item.getData() == MANAGE) {
                        handleManageTemplatesAction();
                    }
                }
            }
        });
        
        // Press Return on table and selection is open or manager
        fInbuiltTableViewer.getControl().addTraverseListener(event -> {
            if(event.detail == SWT.TRAVERSE_RETURN) {
                Object selected = fInbuiltTableViewer.getStructuredSelection().getFirstElement();
                // Open...
                if(selected == OPEN) {
                    event.doit = false;
                    event.detail = SWT.TRAVERSE_NONE;
                    handleOpenAction();
                }
                // Manage...
                else if(selected == MANAGE) {
                    event.doit = false;
                    event.detail = SWT.TRAVERSE_NONE;
                    handleManageTemplatesAction();
                }
            }
        });
        
        // My Templates
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        fUserTableViewer = createGroupsTableViewer(tableComposite, Messages.NewModelFromTemplateWizardPage_3, gd);
        fUserTableViewer.setComparator(new ViewerComparator(Collator.getInstance()) {
            @Override
            public int category(Object element) {
                if(element == fTemplateManager.AllUserTemplatesGroup) {
                    return 0;
                }
                return 1;
            }
        });
        fUserTableViewer.setInput(fTemplateManager);
        
        SashForm sash2 = new SashForm(sash1, SWT.VERTICAL);
        
        Composite galleryComposite = new Composite(sash2, SWT.NULL);
        layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        //layout.verticalSpacing = 0;
        galleryComposite.setLayout(layout);
        
        fGallery = new Gallery(galleryComposite, SWT.V_SCROLL | SWT.BORDER);
        fGallery.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // If the Gallery has the focus pressing Return or Esc does not close the dialog
        fGallery.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.keyCode == SWT.CR) {
                    finishPressed();
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
        
        DefaultGalleryItemRenderer itemRenderer = new DefaultGalleryItemRenderer();
        itemRenderer.setShowRoundedSelectionCorners(true);
        fGallery.setItemRenderer(itemRenderer);
        
        // Root Group
        fGalleryRoot = new GalleryItem(fGallery, SWT.NONE);
        
        // Slider
        Scale scale = new Scale(galleryComposite, SWT.HORIZONTAL);
        gd = new GridData(SWT.END, SWT.NONE, false, false);
        gd.widthHint = 120;
        scale.setLayoutData(gd);
        scale.setMaximum(480);
        scale.setMinimum(64);
        scale.setIncrement(8);
        scale.setPageIncrement(64);
        scale.setSelection(DEFAULT_GALLERY_ITEM_SIZE);
        scale.addSelectionListener(widgetSelectedAdapter(event -> {
            int inc = scale.getSelection();
            groupRenderer.setItemSize(inc, inc);
        }));
        
        // Description
        fDescriptionText = new StyledText(sash2, SWT.V_SCROLL | SWT.READ_ONLY | SWT.WRAP | SWT.BORDER);
        
        fGallery.addSelectionListener(widgetSelectedAdapter(event -> {
            if(event.item instanceof GalleryItem) {
                ITemplate template = (ITemplate)event.item.getData();
                updateWizard(template);
            }
            else {
                updateWizard(null);
            }
        }));
        
        // Double-clicks
        fGallery.addListener(SWT.MouseDoubleClick, event -> {
            GalleryItem item = fGallery.getItem(new Point(event.x, event.y));
            if(item != null) {
                ((ExtendedWizardDialog)getContainer()).finishPressed();
            }
        });
        
        // Mouse move shows thumbnails
        registerMouseMoveHandler();
        
        // Select first group on table
        selectFirstTableItem();
        
        sash1.setWeights(new int[] { 30, 70 });
        sash2.setWeights(new int[] { 70, 30 });
        
        setPageComplete(true); // Yes it's OK
    }
    
    public ITemplate getSelectedTemplate() {
        return fSelectedTemplate;
    }
    
    /**
     * Create a table
     */
    protected TemplateGroupsTableViewer createGroupsTableViewer(Composite parent, String labelText, GridData gd) {
        CLabel label = new CLabel(parent, SWT.NULL);
        label.setText(labelText);
        label.setImage(fTemplateManager.getMainImage());
        
        Composite tableComp = new Composite(parent, SWT.NULL);
        tableComp.setLayout(new TableColumnLayout());
        tableComp.setLayoutData(gd);
        TemplateGroupsTableViewer tableViewer = new TemplateGroupsTableViewer(tableComp, SWT.NULL);
        tableViewer.addSelectionChangedListener(event -> {
            handleTableItemSelected(event.getStructuredSelection().getFirstElement());
            deFocusTable(tableViewer);
        });
        
        return tableViewer;
    }
    
    /**
     * Set the focus on one table at a time
     */
    private void deFocusTable(TableViewer viewer) {
        if(fLastViewerFocus != null && !fLastViewerFocus.getControl().isDisposed() && fLastViewerFocus != viewer) {
            fLastViewerFocus.getTable().deselectAll();
        }
        fLastViewerFocus = viewer;
    }
    
    protected void handleTableItemSelected(Object item) {
        // Template Group
        if(item instanceof ITemplateGroup group) {
            clearGallery();
            updateGallery(group);
        }
    }
    
    /**
     * User wants to open Template from file
     */
    protected void handleOpenAction() {
        getContainer().getShell().setVisible(false);
        
        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setText(Messages.NewModelFromTemplateWizardPage_4);
        dialog.setFilterExtensions(new String[] { "*" + fTemplateManager.getTemplateFileExtension(), "*.*" } ); //$NON-NLS-1$ //$NON-NLS-2$
        String path = dialog.open();
        if(path == null) {
            getContainer().getShell().setVisible(true);
            selectFirstTableItem();
            return;
        }
        
        File file = new File(path);
        
        // Create template and Finish
        BusyIndicator.showWhile(null, () -> { 
            try {
                fSelectedTemplate = fTemplateManager.createTemplate(file);
                finishPressed();
            }
            catch(IOException ex) {
                MessageDialog.openError(getShell(), Messages.NewModelFromTemplateWizardPage_5, ex.getMessage());
                selectFirstTableItem();
                getContainer().getShell().setVisible(true);
            }
        });
    }
    
    /**
     * User wants to manage Templates
     */
    protected void handleManageTemplatesAction() {
        getContainer().getShell().setVisible(false);
        
        TemplateManagerDialog dialog = createTemplateManagerDialog();
        if(dialog.open() == Window.OK) {
            fTemplateManager.reset();
            fUserTableViewer.refresh();
        }
        
        getContainer().getShell().setVisible(true);
        selectFirstTableItem();
    }
    
    protected TemplateManagerDialog createTemplateManagerDialog() {
        // Use a new instance of a Template Manager as a clone in case user cancels
        return new TemplateManagerDialog(getShell(), new ArchimateTemplateManager());
    }
    
    /**
     * Select first group on user table if there is one, else first group on inbuilt table
     */
    private void selectFirstTableItem() {
        TableViewer tableViewer;
        
        if(!fTemplateManager.getUserTemplates().isEmpty()) {
            tableViewer = fUserTableViewer;
        }
        else {
            tableViewer = fInbuiltTableViewer;
        }
        
        Object o = tableViewer.getElementAt(0);
        if(o != null) {
            tableViewer.setSelection(new StructuredSelection(o));
            tableViewer.getControl().setFocus();
        }
    }
    
    /*
     * Show the thumbnail images as the mouse moves
     */
    private void registerMouseMoveHandler() {
        fGallery.addMouseMoveListener(new MouseMoveListener() {
            final int mouse_movement_factor = 6;
            int index = 1;
            GalleryItem selectedItem;
            int last_x;
            
            @Override
            public void mouseMove(MouseEvent event) {
                GalleryItem item = fGallery.getItem(new Point(event.x, event.y));
                
                // Not enough thumbnail images in the item
                if(item != null) {
                    ITemplate template = (ITemplate)item.getData();
                    if(template.getThumbnailCount() <= 1) {
                        return;
                    }
                }
                
                // This can be disposed when we clear the gallery
                if(selectedItem != null && selectedItem.isDisposed()) {
                    selectedItem = null;
                }
                
                if(item != selectedItem) {
                    if(selectedItem != null) {
                        ITemplate template = (ITemplate)selectedItem.getData();
                        selectedItem.setImage(template.getKeyThumbnail());
                    }
                    
                    selectedItem = item;
                    index = 1;
                    last_x = event.x;
                }
                
                if(item != null) {
                    ITemplate template = (ITemplate)item.getData();
                    
                    if(event.x < last_x - mouse_movement_factor) {
                        if(--index < 1) {
                            index = template.getThumbnailCount();
                        }
                    }
                    else if(event.x > last_x + mouse_movement_factor) {
                        if(++index > template.getThumbnailCount()) {
                            index = 1;
                        }
                    }
                    else {
                        return;
                    }
                    
                    last_x = event.x;

                    item.setImage(template.getThumbnail(index));
                }
            }
        });
    }
    
    /**
     * Clear old root group
     */
    protected void clearGallery() {
        if(fGalleryRoot != null && !fGalleryRoot.isDisposed()) {
            for(GalleryItem item : fGalleryRoot.getItems()) {
                item.dispose();
            }
        }
    }
    
    /**
     * Update the Gallery
     * @param group
     */
    protected void updateGallery(ITemplateGroup group) {
        for(ITemplate template : group.getSortedTemplates()) {
            GalleryItem item = new GalleryItem(fGalleryRoot, SWT.NONE);
            item.setText(StringUtils.safeString(template.getName()));
            item.setImage(template.getKeyThumbnail());
            item.setData(template);
        }
        
        if(fGalleryRoot.getItem(0) != null) {
            fGallery.setSelection(new GalleryItem[] { fGalleryRoot.getItem(0) });
            updateWizard((ITemplate)fGalleryRoot.getItem(0).getData());
        }
        else {
            updateWizard(null);
        }
    }
    
    /**
     * Update the wizard to show selected description text
     * @param template
     */
    protected void updateWizard(ITemplate template) {
        fSelectedTemplate = template;

        if(template == null) {
            fDescriptionText.setText(""); //$NON-NLS-1$
            setPageComplete(false);
        }
        else {
            // Update description
            String text = StringUtils.safeString(template.getDescription());
            String desc = StringUtils.safeString(template.getName()) + ":"; //$NON-NLS-1$
            fDescriptionText.setText(desc + "   " + text); //$NON-NLS-1$
            StyleRange style = new StyleRange();
            style.start = 0;
            style.length = desc.length();
            style.fontStyle = SWT.BOLD;
            fDescriptionText.setStyleRange(style);
            setPageComplete(true);
        }
    }
    
    /**
     * Close the wizard with a finish pressed action
     */
    protected void finishPressed() {
        ((ExtendedWizardDialog)getContainer()).finishPressed();
    }
    
    /**
     * Close the wizard with a cancel pressed action
     */
    protected void cancelPressed() {
        ((ExtendedWizardDialog)getContainer()).close();
    }
    
    protected abstract String getHelpID();

}
