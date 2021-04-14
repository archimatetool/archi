/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.propertysections.ImageManagerDialog;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.ImageFactory;
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
    
    private static String HELP_ID = "com.archimatetool.help.profiles"; //$NON-NLS-1$
    
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
        shell.setText("Specializations Manager");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        setTitle("Manage Specializations");
        setMessage(NLS.bind("Create and Edit Specializations in ''{0}''.", fArchimateModel.getName()));

        Composite composite = (Composite)super.createDialogArea(parent);

        Composite client = new Composite(composite, SWT.NULL);
        client.setLayout(new GridLayout(2, false));
        GridDataFactory.create(GridData.FILL_BOTH).applyTo(client);
        
        // New Action
        fActionNew = new Action("New") {
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
        fActionDelete = new Action("Delete") {
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
        fActionChooseImage = new Action("Choose Image...") {
            @Override
            public void run() {
                chooseImage();
            }
        };
        fActionChooseImage.setEnabled(false);
        
        // Clear Image Action
        fActionClearImage = new Action("Remove Image") {
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
        columnName.getColumn().setText("Name");
        tableLayout.setColumnData(columnName.getColumn(), new ColumnWeightData(25, true));
        columnName.setEditingSupport(new NameEditingSupport(fTableViewer));

        // Restricted to Concept Type
        TableViewerColumn columnConceptType = new TableViewerColumn(fTableViewer, SWT.NONE, 2);
        columnConceptType.getColumn().setText("Restricted To");
        tableLayout.setColumnData(columnConceptType.getColumn(), new ColumnWeightData(25, true));
        columnConceptType.setEditingSupport(new ConceptTypeEditingSupport(fTableViewer));
        
        // Usage
        TableViewerColumn columnUsage = new TableViewerColumn(fTableViewer, SWT.NONE, 3);
        columnUsage.getColumn().setText("Usage");
        tableLayout.setColumnData(columnUsage.getColumn(), new ColumnWeightData(45, true));

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

        fTableViewer.setInput(""); // anything will do //$NON-NLS-1$
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
        fButtonNew.setText("New");
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(fButtonNew);
        fButtonNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                createNewProfile();
            }
        });

        fButtonDelete = new Button(client, SWT.PUSH);
        fButtonDelete.setText("Delete");
        fButtonDelete.setEnabled(false);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(fButtonDelete);
        fButtonDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                deleteSelectedProfiles();
            }
        });
        
        fImageButton = new Button(client, SWT.PUSH);
        fImageButton.setText("Image...");
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
        profile.setConceptType(IArchimatePackage.eINSTANCE.getBusinessActor().getName());
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
        String name = "Specialization" + " ";
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
                                    "Delete",
                                    "Are you sure you want to delete these entries?")) {
            
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

        CompoundCommand compoundCmd = new CompoundCommand("Change Specializations");

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
                compoundCmd.add(new NewProfileCommand(fArchimateModel, profile));
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
                    return getUsage(profile);

                default:
                    return null;
            }
        }
        
        private String getUsage(IProfile profile) {
            List<IProfiles> usage = fProfilesUsage.get(profile.getId());
            
            if(usage == null) {
                return ""; //$NON-NLS-1$
            }
            
            List<String> names = new ArrayList<>();
            
            // Get names for these objects
            for(IProfiles profilesObject : usage) {
                names.add(ArchiLabelProvider.INSTANCE.getLabel(profilesObject));
            }
            
            // Sort the names
            Collections.sort(names, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });
            
            // Create one comma separated string
            return String.join(", ", names); //$NON-NLS-1$
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
            
            // Don't allow a duplicate Profile name for the same concept type
            if(isValidProfileNameAndType(name, profile.getConceptType())) {
                profile.setName((String)value);
                fTableViewer.refresh(); // refresh the whole table so items are sorted
            }
        }
    }
    
    /**
     * Concept Type Editor
     */
    private class ConceptTypeEditingSupport extends EditingSupport {
        ComboBoxCellEditor cellEditor;
        
        Map<Integer, String> indexToClassMap; // Index position -> Class name
        Map<String, Integer> classToIndexMap; // Class name -> Index position

        ConceptTypeEditingSupport(ColumnViewer viewer) {
            super(viewer);
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
            if(cellEditor == null) {
                Map<String, String> nameToClassMap = new TreeMap<>(); // Sorted friendly name -> Class name. TreeMap is sorted by key
                indexToClassMap = new HashMap<>();
                classToIndexMap = new HashMap<>();
                
                // Iterate through all Archimate Concept classes and add to map of friendly name -> Class name
                // Friendly names have to be unique for this to work!
                
                // Elements
                for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
                    nameToClassMap.put(ArchiLabelProvider.INSTANCE.getDefaultName(eClass), eClass.getName());
                }
                
                // Relations
                for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
                    nameToClassMap.put(ArchiLabelProvider.INSTANCE.getDefaultName(eClass), eClass.getName());
                }
                
                // Connectors
                for(EClass eClass : ArchimateModelUtils.getConnectorClasses()) {
                    nameToClassMap.put(ArchiLabelProvider.INSTANCE.getDefaultName(eClass), eClass.getName());
                }
                
                // Map from Index to Class Name and from Class Name to Index
                int i = 0;
                for(String className : nameToClassMap.values()) {
                    indexToClassMap.put(i, className);
                    classToIndexMap.put(className, i++);
                }
                
                cellEditor = new ComboBoxCellEditor((Composite)getViewer().getControl(),
                                                    nameToClassMap.keySet().toArray(String[]::new),
                                                    SWT.READ_ONLY);
                
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
            // Return index of class name
            IProfile profile = (IProfile)element;
            return classToIndexMap.get(profile.getConceptType());
        }

        @Override
        protected void setValue(Object element, Object value) {
            // Check for -1 value. On Mac this happens if the Mod key is down when selecting the same item from the combo box
            Integer index = (Integer)value;
            if(index == -1) {
                return;
            }

            // Get class name from index
            IProfile profile = (IProfile)element;
            String conceptType = indexToClassMap.get(index);
            
            // Don't allow a duplicate Profile name for the same concept type
            if(isValidProfileNameAndType(profile.getName(), conceptType)) {
                profile.setConceptType(conceptType);
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
     * New Profile Command
     */
    private static class NewProfileCommand extends Command {
        IArchimateModel model;
        IProfile profile;

        NewProfileCommand(IArchimateModel model, IProfile profile) {
            this.model = model;
            this.profile = profile;
        }

        @Override
        public void execute() {
            model.getProfiles().add(profile);
        }

        @Override
        public void undo() {
            model.getProfiles().remove(profile);
        }

        @Override
        public void dispose() {
            model = null;
            profile = null;
        }
    }

    /**
     * Delete Profile Command
     */
    private static class DeleteProfileCommand extends Command {
        IArchimateModel model;
        IProfile profile;
        int index;
        List<IProfiles> usages;

        DeleteProfileCommand(IProfile profile, List<IProfiles> usages) {
            this.profile = profile;
            this.usages = usages;
            model = profile.getArchimateModel();
        }

        @Override
        public void execute() {
            // Ensure index position is stored just before execute as this is part of a composite delete action the index position will have changed
            index = model.getProfiles().indexOf(profile);
            
            // Delete the profile from the model and all references to it
            EcoreUtil.delete(profile);
        }

        @Override
        public void undo() {
            if(index != -1) {
                // Restore the profile to the model
                model.getProfiles().add(index, profile);
                
                // Restore all references to the profile
                if(usages != null) {
                    for(IProfiles profilesObject : usages) {
                        profilesObject.getProfiles().add(profile);
                    }
                }
            }
        }

        @Override
        public void dispose() {
            model = null;
            profile = null;
            usages = null;
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
