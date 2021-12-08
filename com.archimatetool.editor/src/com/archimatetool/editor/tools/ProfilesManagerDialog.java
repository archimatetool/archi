/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.commands.AddListMemberCommand;
import com.archimatetool.editor.model.commands.RemoveListMemberCommand;
import com.archimatetool.editor.propertysections.ImageManagerDialog;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.ui.components.ExtendedTitleAreaDialog;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProfiles;
import com.archimatetool.model.util.ArchimateModelUtils;

/**
 * Profiles Manager Dialog
 * 
 * @author Phillip Beauvoir
 */
public class ProfilesManagerDialog extends ExtendedTitleAreaDialog {
    
    private static String HELP_ID = "com.archimatetool.help.ProfilesManagerDialog"; //$NON-NLS-1$
    
    private static final int IMAGE_SIZE = 100;
    
    private IArchimateModel fArchimateModel;
    
    private Map<String, IProfile> fProfilesModel; // The model's Profiles
    private Map<String, IProfile> fProfilesTemp;  // A temporary copy of the model's Profiles
    
    private Map<String, List<IProfiles>> fProfilesUsage;  // Usages of a Profile
    
    private TableViewer fTableViewer;
    
    private Button fButtonNew, fButtonDelete;
    private IAction fActionNew, fActionDelete, fActionChooseImage, fActionClearImage;
    
    private Button fImageButton;
    private Canvas fImagePreview;
    private Image fPreviewImage;
    
    // Default class for new Profile
    private EClass fDefaultClass = IArchimatePackage.eINSTANCE.getBusinessActor();

    public ProfilesManagerDialog(Shell parentShell, IArchimateModel model) {
        super(parentShell, "ProfilesManagerDialog"); //$NON-NLS-1$
        
        setTitleImage(IArchiImages.ImageFactory.getImage(IArchiImages.ECLIPSE_IMAGE_NEW_WIZARD));
        setShellStyle(getShellStyle() | SWT.RESIZE);

        fArchimateModel = model;
        
        // Create indexed copies of the model's Profiles
        fProfilesModel = new LinkedHashMap<>();
        fProfilesTemp = new LinkedHashMap<>();

        for(IProfile profile : model.getProfiles()) {
            // Index of original profiles in the model
            fProfilesModel.put(profile.getId(), profile);
            
            // Copies of the original profiles
            fProfilesTemp.put(profile.getId(), EcoreUtil.copy(profile));
        }
        
        // Find Profile usages now as it can be time consuming to do it more than once
        fProfilesUsage = new HashMap<>();
        for(Entry<IProfile, List<IProfiles>> entry : ArchimateModelUtils.findProfilesUsage(model).entrySet()) {
            fProfilesUsage.put(entry.getKey().getId(), entry.getValue());
        }
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.ProfilesManagerDialog_0);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        setTitle(Messages.ProfilesManagerDialog_1);
        setMessage(NLS.bind(Messages.ProfilesManagerDialog_2, fArchimateModel.getName()));

        Composite composite = (Composite)super.createDialogArea(parent);

        Composite client = new Composite(composite, SWT.NULL);
        client.setLayout(new GridLayout(2, false));
        GridDataFactory.create(GridData.FILL_BOTH).applyTo(client);
        
        // New Action
        fActionNew = new Action(Messages.ProfilesManagerDialog_3) {
            @Override
            public void run() {
                createNewProfile();
            }

            @Override
            public String getToolTipText() {
                return getText();
            }
        };

        // Delete Action
        fActionDelete = new Action(Messages.ProfilesManagerDialog_4) {
            @Override
            public void run() {
                deleteSelectedProfiles();
            }

            @Override
            public String getToolTipText() {
                return getText();
            }

            @Override
            public ImageDescriptor getImageDescriptor() {
                return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE);
            };
        };
        fActionDelete.setEnabled(false);
        
        // Choose Image Action
        fActionChooseImage = new Action(Messages.ProfilesManagerDialog_5) {
            @Override
            public void run() {
                chooseImage();
            }
        };
        fActionChooseImage.setEnabled(false);
        
        // Clear Image Action
        fActionClearImage = new Action(Messages.ProfilesManagerDialog_6) {
            @Override
            public void run() {
                clearImages();
            }
        };
        fActionClearImage.setEnabled(false);
        
        // Create panels
        createTableControl(client);
        createButtonPanel(client);
        
        // Create a right-click context menu
        MenuManager menuManager = new MenuManager("#ProfileManagerContextMenu"); //$NON-NLS-1$
        menuManager.add(fActionNew);
        menuManager.add(fActionDelete);
        menuManager.add(new Separator());
        menuManager.add(fActionChooseImage);
        menuManager.add(fActionClearImage);
        Menu menu = menuManager.createContextMenu(fTableViewer.getControl());
        fTableViewer.getControl().setMenu(menu);

        return composite;
    }

    /**
     * Create the Table
     */
    private void createTableControl(Composite parent) {
        Composite tableComp = new Composite(parent, SWT.BORDER);
        TableColumnLayout tableLayout = new TableColumnLayout();
        tableComp.setLayout(tableLayout);
        GridDataFactory.create(GridData.FILL_BOTH).applyTo(tableComp);

        fTableViewer = new TableViewer(tableComp, SWT.MULTI | SWT.FULL_SELECTION);
        GridDataFactory.create(GridData.FILL_BOTH).applyTo(fTableViewer.getControl());
        
        // Mac Silicon Item height
        UIUtils.fixMacSiliconItemHeight(fTableViewer.getTable());

        // Edit cell on single-click and add Tab key traversal
        TableViewerEditor.create(fTableViewer, new ColumnViewerEditorActivationStrategy(fTableViewer),
                ColumnViewerEditor.TABBING_HORIZONTAL | 
                ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | 
                ColumnViewerEditor.TABBING_VERTICAL |
                ColumnViewerEditor.KEEP_EDITOR_ON_DOUBLE_CLICK |
                ColumnViewerEditor.KEYBOARD_ACTIVATION);

        fTableViewer.getTable().setHeaderVisible(true);
        fTableViewer.getTable().setLinesVisible(true);

        fTableViewer.setComparator(new ViewerComparator() {
            @Override
            public int compare(Viewer viewer, Object e1, Object e2) {
                return ((IProfile)e1).getName().compareToIgnoreCase(((IProfile)e2).getName());
            }
        });

        // Columns
        
        // Icon
        TableViewerColumn columnIcon = new TableViewerColumn(fTableViewer, SWT.NONE, 0);
        tableLayout.setColumnData(columnIcon.getColumn(), new ColumnWeightData(5, true));

        // Name
        TableViewerColumn columnName = new TableViewerColumn(fTableViewer, SWT.NONE, 1);
        columnName.getColumn().setText(Messages.ProfilesManagerDialog_7);
        tableLayout.setColumnData(columnName.getColumn(), new ColumnWeightData(50, true));
        columnName.setEditingSupport(new NameEditingSupport(fTableViewer));

        // Restricted to Concept Type
        TableViewerColumn columnConceptType = new TableViewerColumn(fTableViewer, SWT.NONE, 2);
        columnConceptType.getColumn().setText(Messages.ProfilesManagerDialog_8);
        tableLayout.setColumnData(columnConceptType.getColumn(), new ColumnWeightData(35, true));
        columnConceptType.setEditingSupport(new ConceptTypeEditingSupport(fTableViewer));
        
        // Usage
        TableViewerColumn columnUsage = new TableViewerColumn(fTableViewer, SWT.NONE, 3);
        columnUsage.getColumn().setText(Messages.ProfilesManagerDialog_9);
        tableLayout.setColumnData(columnUsage.getColumn(), new ColumnWeightData(10, true));

        // Content Provider
        fTableViewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            @Override
            public void dispose() {
            }

            @Override
            public Object[] getElements(Object inputElement) {
                return fProfilesTemp.values().toArray();
            }
        });

        // Label Provider
        fTableViewer.setLabelProvider(new LabelCellProvider());

        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = event.getStructuredSelection();
                boolean enabled = !selection.isEmpty();
                
                // Disable/enable some actions
                fActionDelete.setEnabled(enabled);
                fButtonDelete.setEnabled(enabled);
                
                fActionChooseImage.setEnabled(enabled);
                fActionClearImage.setEnabled(false);
                
                // Image buttons/actions depend on some factors...
                for(Object o : selection) {
                    IProfile profile = (IProfile)o;
                    
                    // If any selected Profile can't have an image then this is disabled
                    if(!canHaveImage(profile)) {
                        fActionChooseImage.setEnabled(false);
                    }
                    
                    // Enable clear image only if there is one image to clear in the selection
                    if(profile.getImagePath() != null) {
                        fActionClearImage.setEnabled(true);
                    }
                }
                
                fImageButton.setEnabled(fActionChooseImage.isEnabled() || fActionClearImage.isEnabled());
                
                // Update Image Preview
                updateImagePreview();
            }
        });
        
        /*
         * Table Double-click
         */
        fTableViewer.getTable().addListener(SWT.MouseDoubleClick, (e) -> {
            // Get Table item
            Point pt = new Point(e.x, e.y);
            TableItem item = fTableViewer.getTable().getItem(pt);
            
            // Double-click into empty table creates new Profile
            if(item == null) {
                createNewProfile();
            }
        });

        fTableViewer.setInput(""); // anything will do //$NON-NLS-1$
    }
    
    /**
     * Set the default class for a new Profile
     * @param eClass
     */
    public void setDefaultClass(EClass eClass) {
        fDefaultClass = eClass;
    }
    
    /**
     * Create the button panel
     */
    private void createButtonPanel(Composite parent) {
        Composite client = new Composite(parent, SWT.NULL);

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        client.setLayout(layout);

        GridDataFactory.create(GridData.VERTICAL_ALIGN_BEGINNING).applyTo(client);

        fButtonNew = new Button(client, SWT.PUSH);
        fButtonNew.setText(Messages.ProfilesManagerDialog_10);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(fButtonNew);
        fButtonNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                createNewProfile();
            }
        });

        fButtonDelete = new Button(client, SWT.PUSH);
        fButtonDelete.setText(Messages.ProfilesManagerDialog_11);
        fButtonDelete.setEnabled(false);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(fButtonDelete);
        fButtonDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                deleteSelectedProfiles();
            }
        });
        
        fImageButton = new Button(client, SWT.PUSH);
        fImageButton.setText(Messages.ProfilesManagerDialog_12);
        fImageButton.setEnabled(false);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(fImageButton);
        
        // Create sub-menu for Image button
        MenuManager menuManager = new MenuManager();
        menuManager.add(fActionChooseImage);
        menuManager.add(fActionClearImage);
        Menu menu = menuManager.createContextMenu(fImageButton.getShell());

        // Image Button is clicked...
        fImageButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Display sub menu below button
                Rectangle buttonBounds = fImageButton.getBounds();
                menu.setLocation(fImageButton.getParent().toDisplay(buttonBounds.x - 0, buttonBounds.y + buttonBounds.height));
                menu.setVisible(true);
            }
        });
        
        fImagePreview = new Canvas(client, SWT.BORDER);
        GridDataFactory.create(SWT.NONE).hint(IMAGE_SIZE, IMAGE_SIZE).applyTo(fImagePreview);
        
        fImagePreview.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                if(fPreviewImage != null) {
                    e.gc.setAntialias(SWT.ON);
                    e.gc.setInterpolation(SWT.HIGH);
                    
                    Rectangle imageBounds = fPreviewImage.getBounds();
                    Rectangle newSize = ImageFactory.getScaledImageSize(fPreviewImage, IMAGE_SIZE);
                    
                    // Centre the image
                    int x = (IMAGE_SIZE - newSize.width) / 2;
                    int y = (IMAGE_SIZE - newSize.height) / 2;
                    
                    e.gc.drawImage(fPreviewImage, 0, 0, imageBounds.width, imageBounds.height,
                            x, y, newSize.width, newSize.height);
                }
            }
        });
        
        fImagePreview.addListener(SWT.MouseDoubleClick, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if(fActionChooseImage.isEnabled()) {
                    chooseImage();
                }
            }
        });
        
        fImagePreview.addDisposeListener((e) -> {
            disposePreviewImage();
        });
    }
    
    /**
     * Create a new Profile and add it to the temp profiles list
     */
    private void createNewProfile() {
        IProfile profile = IArchimateFactory.eINSTANCE.createProfile();
        profile.setConceptType(fDefaultClass.getName());
        profile.setName(generateNewProfileName(profile.getConceptType()));
        fProfilesTemp.put(profile.getId(), profile);
        
        //fTableViewer.applyEditorValue(); // complete any current editing
        fTableViewer.refresh();
        fTableViewer.editElement(profile, 1);
    }
    
    /**
     * Generate a new unique Specialization name
     */
    private String generateNewProfileName(String conceptType) {
        String name = Messages.ProfilesManagerDialog_13 + " "; //$NON-NLS-1$
        String s;
        int newNameIndex = 1;
        do {
            s = name + newNameIndex++;
        }
        while(!isValidProfileNameAndType(s, conceptType));
        return s;
    }
    
    /**
     * Delete selected Profiles from the temp list
     */
    private void deleteSelectedProfiles() {
        if(MessageDialog.openQuestion(getShell(),
                                    Messages.ProfilesManagerDialog_14,
                                    Messages.ProfilesManagerDialog_15)) {
            
            for(Object o : ((IStructuredSelection)fTableViewer.getSelection()).toList()) {
                fProfilesTemp.remove(((IProfile)o).getId());
            }
            
            fTableViewer.refresh();
        }
    }
    
    /**
     * Update the image preview
     */
    private void updateImagePreview() {
        disposePreviewImage();
        
        IProfile profile = (IProfile)fTableViewer.getStructuredSelection().getFirstElement();
        
        if(profile != null && profile.getImagePath() != null) {
            IArchiveManager archiveManager = (IArchiveManager)fArchimateModel.getAdapter(IArchiveManager.class);
            
            try {
                fPreviewImage = archiveManager.createImage(profile.getImagePath());
            }
            catch(Exception ex) {
                ex.printStackTrace();
                Logger.logError("Could not create image!", ex); //$NON-NLS-1$
            }
        }
        
        fImagePreview.redraw();
    }
    
    /**
     * Choose an image from the dialog and apply to selected profiles
     */
    private void chooseImage() {
        IStructuredSelection selection = fTableViewer.getStructuredSelection();
        
        if(!selection.isEmpty()) {
            IProfile firstSelected = (IProfile)selection.getFirstElement();
            
            ImageManagerDialog dialog = new ImageManagerDialog(getParentShell());
            dialog.setSelected(fArchimateModel, firstSelected.getImagePath());

            if(dialog.open() == Window.OK) {
                try {
                    IArchiveManager archiveManager = (IArchiveManager)fArchimateModel.getAdapter(IArchiveManager.class);
                    String path = null;

                    // Image from file
                    if(dialog.getUserSelectedFile() != null && dialog.getUserSelectedFile().exists()) {
                        path = archiveManager.addImageFromFile(dialog.getUserSelectedFile());
                    }
                    // Existing image which could be in this model or a different model
                    else if(dialog.getUserSelectedImagePath() != null) {
                        if(dialog.getUserSelectedModel() != fArchimateModel) { // Different model
                            IArchiveManager selectedArchiveManager = (IArchiveManager)dialog.getUserSelectedModel().getAdapter(IArchiveManager.class);
                            path = archiveManager.copyImageBytes(selectedArchiveManager, dialog.getUserSelectedImagePath());
                        }
                        else { // Same model
                            path = dialog.getUserSelectedImagePath();
                        }
                    }
                    
                    if(path != null) {
                        // Apply the image path to Profiles that can have an image
                        for(Object o : selection.toList()) {
                            IProfile profile = (IProfile)o;
                            if(canHaveImage(profile)) {
                                profile.setImagePath(path);
                            }
                        }
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                    Logger.logError("Could not create image!", ex); //$NON-NLS-1$
                }

                // Select to update image preview
                fTableViewer.setSelection(selection);
            }
        }
    }
    
    /**
     * Clear all images and set selected Profiles' image paths to null
     */
    private void clearImages() {
        IStructuredSelection selection = fTableViewer.getStructuredSelection();
        
        for(Object o : selection.toList()) {
            ((IProfile)o).setImagePath(null);
        }
        
        // Reselect to update image preview
        fTableViewer.setSelection(selection);
    }
    
    private void disposePreviewImage() {
        if(fPreviewImage != null) {
            fPreviewImage.dispose();
            fPreviewImage = null;
        }
    }
    
    /**
     * @return true if name is not blank and not already used for concept type
     */
    private boolean isValidProfileNameAndType(String name, String conceptType) {
        return !name.isBlank() && !ArchimateModelUtils.hasProfileByNameAndType(fProfilesTemp.values(), name, conceptType);
    }
    
    /**
     * Only ArchiMate elements (not Junctions) can have images
     */
    private boolean canHaveImage(IProfile profile) {
        EClass eClass = profile.getConceptClass();
        return !(IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(eClass) ||
                IArchimatePackage.eINSTANCE.getJunction().isSuperTypeOf(eClass));
    }
    
    @Override
    protected Point getDefaultDialogSize() {
        return new Point(800, 450);
    }

    /**
     * OK was pressed, so see what was added, changed or deleted and execute the Commands
     */
    @Override
    protected void okPressed() {
        super.okPressed();

        CompoundCommand compoundCmd = new CompoundCommand(Messages.ProfilesManagerDialog_16);

        // Iterate thru our temp list of Profiles
        for(IProfile profile : fProfilesTemp.values()) {
            // Is this a copy of the original?
            if(fProfilesModel.containsKey(profile.getId())) {
                IProfile profileOriginal = fProfilesModel.get(profile.getId());

                // The Profile has been edited
                if(!EcoreUtil.equals(profileOriginal, profile)) {
                    List<IProfiles> usages = fProfilesUsage.get(profileOriginal.getId());
                    compoundCmd.add(new ChangeProfileCommand(profileOriginal, profile, usages));
                }
            }
            // A new Profile was added
            else {
                compoundCmd.add(new AddListMemberCommand<IProfile>(fArchimateModel.getProfiles(), profile));
            }
        }
        
        // Iterate thru model's Profiles and compare with our temp list to see if any Profiles were deleted
        for(IProfile profile : fArchimateModel.getProfiles()) {
            if(!fProfilesTemp.containsKey(profile.getId())) {
                List<IProfiles> usages = fProfilesUsage.get(profile.getId());
                compoundCmd.add(new DeleteProfileCommand(profile, usages));
            }
        }

        // Execute the Commands
        CommandStack stack = (CommandStack)fArchimateModel.getAdapter(CommandStack.class);
        stack.execute(compoundCmd);
    }
    

    
    
    // -----------------------------------------------------------------------------------------------------------------
    //
    // Table classes
    //
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Label Provider
     */
    private class LabelCellProvider extends LabelProvider implements ITableLabelProvider {
        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            IProfile profile = (IProfile)element;
            
            if(columnIndex == 0) {
                return ArchiLabelProvider.INSTANCE.getImage(profile.getConceptClass());
            }
            
            return null;
        }

        @Override
        public String getColumnText(Object element, int columnIndex) {
            IProfile profile = (IProfile)element;

            switch(columnIndex) {
                // Profile Name
                case 1:
                    return profile.getName();

                // Restricted to class
                case 2:
                    return ArchiLabelProvider.INSTANCE.getDefaultName(profile.getConceptClass());
                    
                // Usage
                case 3:
                    return getInstanceCount(profile);

                default:
                    return null;
            }
        }
        
        private String getInstanceCount(IProfile profile) {
            List<IProfiles> usage = fProfilesUsage.get(profile.getId());
            return usage == null ? "" : String.valueOf(usage.size()); //$NON-NLS-1$
        }
    }

    /**
     * Name Editor
     */
    private class NameEditingSupport extends EditingSupport {
        TextCellEditor cellEditor;

        NameEditingSupport(ColumnViewer viewer) {
            super(viewer);
            cellEditor = new TextCellEditor((Composite)viewer.getControl());
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
            return cellEditor;
        }

        @Override
        protected boolean canEdit(Object element) {
            return true;
        }

        @Override
        protected Object getValue(Object element) {
            return ((IProfile)element).getName();
        }

        @Override
        protected void setValue(Object element, Object value) {
            IProfile profile = (IProfile)element;
            String name = (String)value;
            
            // Allow the name to change case, but don't allow a duplicate Profile name for the same concept type
            if(profile.getName().equalsIgnoreCase(name) || isValidProfileNameAndType(name, profile.getConceptType())) {
                profile.setName((String)value);
                fTableViewer.refresh(); // refresh the whole table so items are sorted
            }
        }
    }
    
    /**
     * Concept Type Editor
     */
    private class ConceptTypeEditingSupport extends EditingSupport {
        ComboBoxViewerCellEditor cellEditor;
        
        ConceptTypeEditingSupport(ColumnViewer viewer) {
            super(viewer);
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
            if(cellEditor == null) {
                List<EClass> classList = new ArrayList<>();
                
                // Elements
                for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
                    classList.add(eClass);
                }
                
                // Relations
                for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
                    classList.add(eClass);
                }
                
                // Connectors
                for(EClass eClass : ArchimateModelUtils.getConnectorClasses()) {
                    classList.add(eClass);
                }
                
                cellEditor = new ComboBoxViewerCellEditor((Composite)getViewer().getControl(), SWT.READ_ONLY);
                
                cellEditor.setContentProvider(new IStructuredContentProvider() {
                    @Override
                    public Object[] getElements(Object inputElement) {
                        return classList.toArray();
                    }
                });
                
                cellEditor.setLabelProvider(new LabelProvider() {
                    @Override
                    public String getText(Object element) {
                        return ArchiLabelProvider.INSTANCE.getDefaultName((EClass)element);
                    }
                });
                
                cellEditor.getViewer().setComparator(new ViewerComparator() {
                    @Override
                    public int compare(Viewer viewer, Object e1, Object e2) {
                        return ArchiLabelProvider.INSTANCE.getDefaultName((EClass)e1).compareToIgnoreCase(ArchiLabelProvider.INSTANCE.getDefaultName((EClass)e2));
                    }
                });
                
                cellEditor.setInput(classList);
                
                // Ensure the combo drop down appears on single mouse click and tab traversal
                cellEditor.setActivationStyle(ComboBoxCellEditor.DROP_DOWN_ON_MOUSE_ACTIVATION | ComboBoxCellEditor.DROP_DOWN_ON_TRAVERSE_ACTIVATION);
            }
            
            return cellEditor;
        }

        @Override
        protected boolean canEdit(Object element) {
            // Can't edit this if the Profile is in use
            IProfile profile = (IProfile)element;
            return !fProfilesUsage.containsKey(profile.getId());
        }

        @Override
        protected Object getValue(Object element) {
            // Return Class
            IProfile profile = (IProfile)element;
            return profile.getConceptClass();
        }

        @Override
        protected void setValue(Object element, Object value) {
            if(value == null) {
                return;
            }
            
            IProfile profile = (IProfile)element;
            EClass eClass = (EClass)value;
            
            // Don't allow a duplicate Profile name for the same concept type
            if(isValidProfileNameAndType(profile.getName(), eClass.getName())) {
                profile.setConceptType(eClass.getName());
                getViewer().update(profile, null);
                
                // If concept type is a relation or connector remove image
                if(!canHaveImage(profile)) {
                    profile.setImagePath(null);
                    getViewer().setSelection(new StructuredSelection(profile));
                }
            }
        }
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    //
    // Commands
    //
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Delete Profile Command
     */
    private static class DeleteProfileCommand extends CompoundCommand {
        DeleteProfileCommand(IProfile profile, List<IProfiles> usages) {
            // Delete Profile from Model
            add(new RemoveListMemberCommand<IProfile>(profile.getArchimateModel().getProfiles(), profile));
            
            // Delete Usages
            if(usages != null) {
                for(IProfiles owner : usages) {
                    add(new RemoveListMemberCommand<IProfile>(owner.getProfiles(), profile));
                }
            }
        }
    }
    
    /**
     * Change Profile Command
     */
    private static class ChangeProfileCommand extends Command {
        IProfile profileOriginal;
        IProfile profileChanged;
        List<IProfiles> usages;

        ChangeProfileCommand(IProfile profileOriginal, IProfile profileChanged, List<IProfiles> usages) {
            this.profileOriginal = profileOriginal;
            this.profileChanged = profileChanged;
            this.usages = usages;
        }

        @Override
        public void execute() {
            replace(profileOriginal, profileChanged);
        }

        @Override
        public void undo() {
            replace(profileChanged, profileOriginal);
        }
        
        /**
         * Replace the profile and all references to it
         */
        private void replace(IProfile oldProfile, IProfile newProfile) {
            // Replace the profile in the model
            EcoreUtil.replace(oldProfile, newProfile);
            
            // Replace all references to this profile
            if(usages != null) {
                for(IProfiles profilesObject : usages) {
                    EcoreUtil.replace(profilesObject, IArchimatePackage.Literals.PROFILES__PROFILES, oldProfile, newProfile);
                }
            }
        }

        @Override
        public void dispose() {
            profileOriginal = null;
            profileChanged = null;
            usages = null;
        }
    }

}
