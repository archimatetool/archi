/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.model.commands.EObjectNonNotifyingCompoundCommand;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.ui.components.ExtendedTitleAreaDialog;
import com.archimatetool.editor.ui.components.GlobalActionDisablementHandler;
import com.archimatetool.editor.ui.components.StringComboBoxCellEditor;
import com.archimatetool.editor.ui.components.UpdatingTableColumnLayout;
import com.archimatetool.editor.utils.HTMLUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.util.LightweightEContentAdapter;



/**
 * User Properties Section
 * 
 * @author Phillip Beauvoir
 */
public class UserPropertiesSection extends AbstractECorePropertySection {

    private static final String HELP_ID = "com.archimatetool.help.userProperties"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IProperties;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IProperties.class;
        }
    }

    private IProperties fPropertiesElement;

    private TableViewer fTableViewer;
    private IAction fActionNewProperty, fActionNewMultipleProperty, fActionRemoveProperty, fActionShowKeyEditor;

    private boolean ignoreMessages;
    
    @Override
    protected void createControls(Composite parent) {
        createTableControl(parent);
        
        // We are interested in listening to notifications from child IProperty objects
        ((LightweightEContentAdapter)getECoreAdapter()).addClass(IProperty.class);
    }

    @Override
    protected void notifyChanged(Notification msg) {
        super.notifyChanged(msg);
        
        if(msg.getEventType() == EObjectNonNotifyingCompoundCommand.START) {
            ignoreMessages = true;
            return;
        }
        else if(msg.getEventType() == EObjectNonNotifyingCompoundCommand.END) {
            ignoreMessages = false;
            fTableViewer.refresh();
        }

        if(!ignoreMessages) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                updateLocked();
            }

            if(feature == IArchimatePackage.Literals.PROPERTIES__PROPERTIES) {
                fTableViewer.refresh();
            }

            if(feature == IArchimatePackage.Literals.PROPERTY__KEY
                    || feature == IArchimatePackage.Literals.PROPERTY__VALUE) {
                fTableViewer.update(msg.getNotifier(), null);
            }
        }
    }

    @Override
    protected void update() {
        fPropertiesElement = (IProperties)getFirstSelectedObject();
        
        fTableViewer.setInput(fPropertiesElement);
        
        // Update kludge
        ((UpdatingTableColumnLayout)fTableViewer.getTable().getParent().getLayout()).doRelayout();
        
        // Locked
        updateLocked();
    }
    
    private void updateLocked() {
        boolean locked = isLocked(getFirstSelectedObject());
        fTableViewer.getTable().setEnabled(!locked);
        fActionNewProperty.setEnabled(!locked);
        fActionRemoveProperty.setEnabled(!locked && !fTableViewer.getSelection().isEmpty());
        fActionNewMultipleProperty.setEnabled(!locked);
    }

    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
    
    /**
     * Return the Archimate model bound to the properties element
     * @return 
     */
    protected IArchimateModel getArchimateModel() {
        if(fPropertiesElement instanceof IArchimateModelObject) {
            return ((IArchimateModelObject)fPropertiesElement).getArchimateModel();
        }
        return null;
    }

    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }

    private void createTableControl(Composite parent) {
        // Table Composite
        Composite tableComp = createTableComposite(parent, SWT.NULL);
        TableColumnLayout tableLayout = (TableColumnLayout)tableComp.getLayout();
        
        // Table Viewer
        fTableViewer = new TableViewer(tableComp, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);

        // Font
        UIUtils.setFontFromPreferences(fTableViewer.getTable(), IPreferenceConstants.PROPERTIES_TABLE_FONT, true);
        
        // Mac Silicon Item height
        UIUtils.fixMacSiliconItemHeight(fTableViewer.getTable());
        
        // Edit cell on double-click and add Tab key traversal
        TableViewerEditor.create(fTableViewer, new ColumnViewerEditorActivationStrategy(fTableViewer) {
            @Override
            protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
                return super.isEditorActivationEvent(event) ||
                      (event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION);
            }
            
        }, ColumnViewerEditor.TABBING_HORIZONTAL | 
                ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | 
                ColumnViewerEditor.TABBING_VERTICAL |
                ColumnViewerEditor.KEEP_EDITOR_ON_DOUBLE_CLICK |
                ColumnViewerEditor.KEYBOARD_ACTIVATION);
        
        fTableViewer.getTable().setHeaderVisible(true);
        fTableViewer.getTable().setLinesVisible(true);

        addDragSupport();
        addDropSupport();

        // Help ID on table
        PlatformUI.getWorkbench().getHelpSystem().setHelp(fTableViewer.getTable(), HELP_ID);

        // Columns
        TableViewerColumn columnBlank = new TableViewerColumn(fTableViewer, SWT.NONE, 0);
        tableLayout.setColumnData(columnBlank.getColumn(), new ColumnWeightData(3, false));
        columnBlank.getColumn().setWidth(38);

        TableViewerColumn columnKey = new TableViewerColumn(fTableViewer, SWT.NONE, 1);
        columnKey.getColumn().setText(Messages.UserPropertiesSection_0);
        tableLayout.setColumnData(columnKey.getColumn(), new ColumnWeightData(20, true));
        columnKey.setEditingSupport(new KeyEditingSupport(fTableViewer));

        // Click on Key Table Header
        columnKey.getColumn().addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                sortKeys();
            }
        });

        TableViewerColumn columnValue = new TableViewerColumn(fTableViewer, SWT.NONE, 2);
        columnValue.getColumn().setText(Messages.UserPropertiesSection_1);
        tableLayout.setColumnData(columnValue.getColumn(), new ColumnWeightData(77, true));
        columnValue.setEditingSupport(new ValueEditingSupport(fTableViewer));

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
                return ((IProperties)inputElement).getProperties().toArray();
            }
        });

        // Label Provider
        fTableViewer.setLabelProvider(new LabelCellProvider());
        
        // Enable tooltips
        ColumnViewerToolTipSupport.enableFor(fTableViewer);

        // Toolbar
        ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.VERTICAL);
        getWidgetFactory().adapt(toolBar);
        GridDataFactory.fillDefaults().align(SWT.END, SWT.TOP).applyTo(toolBar);

        ToolBarManager toolBarmanager = new ToolBarManager(toolBar);

        // New Property
        fActionNewProperty = new Action(Messages.UserPropertiesSection_2) {
            @Override
            public void run() {
                if(isAlive(fPropertiesElement)) {
                    fTableViewer.applyEditorValue(); // complete any current editing
                    int index = -1;
                    IProperty selected = (IProperty)((IStructuredSelection)fTableViewer.getSelection()).getFirstElement();
                    if(selected != null) {
                        index = fPropertiesElement.getProperties().indexOf(selected) + 1;
                    }
                    IProperty property = IArchimateFactory.eINSTANCE.createProperty();
                    executeCommand(new NewPropertyCommand(fPropertiesElement.getProperties(), property, index));
                    fTableViewer.editElement(property, 1);
                }
            }

            @Override
            public String getToolTipText() {
                return getText();
            }

            @Override
            public ImageDescriptor getImageDescriptor() {
                return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_PLUS);
            }
        };

        // New Multiple Properties
        fActionNewMultipleProperty = new Action(Messages.UserPropertiesSection_3) {
            @Override
            public void run() {
                if(isAlive(fPropertiesElement)) {
                    MultipleAddDialog dialog = new MultipleAddDialog(fPage.getSite().getShell());
                    if(dialog.open() == Window.OK) {
                        executeCommand(dialog.getCommand());
                    }
                }
            }

            @Override
            public String getToolTipText() {
                return getText();
            }

            @Override
            public ImageDescriptor getImageDescriptor() {
                return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_MUTIPLE);
            }
        };

        // Remove Property
        fActionRemoveProperty = new Action(Messages.UserPropertiesSection_4) {
            @Override
            public void run() {
                if(isAlive(fPropertiesElement)) {
                    CompoundCommand compoundCmd = new EObjectNonNotifyingCompoundCommand(fPropertiesElement) {
                        @Override
                        public String getLabel() {
                            return getCommands().size() > 1 ? Messages.UserPropertiesSection_5 : Messages.UserPropertiesSection_6;
                        }
                    };
                    for(Object o : ((IStructuredSelection)fTableViewer.getSelection()).toList()) {
                        Command cmd = new RemovePropertyCommand(fPropertiesElement.getProperties(), (IProperty)o);
                        compoundCmd.add(cmd);
                    }
                    executeCommand(compoundCmd);
                }
            }

            @Override
            public String getToolTipText() {
                return getText();
            }

            @Override
            public ImageDescriptor getImageDescriptor() {
                return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_SMALL_X);
            }
        };
        fActionRemoveProperty.setEnabled(false);

        // Manage
        fActionShowKeyEditor = new Action(Messages.UserPropertiesSection_7) {
            @Override
            public void run() {
                if(isAlive(fPropertiesElement)) {
                    UserPropertiesManagerDialog dialog = new UserPropertiesManagerDialog(fPage.getSite().getShell(),
                            getArchimateModel());
                    dialog.open();
                }
            }

            @Override
            public String getToolTipText() {
                return getText();
            }

            @Override
            public ImageDescriptor getImageDescriptor() {
                return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_COG);
            }
        };

        toolBarmanager.add(fActionNewProperty);
        toolBarmanager.add(fActionNewMultipleProperty);
        toolBarmanager.add(fActionRemoveProperty);
        toolBarmanager.add(fActionShowKeyEditor);
        toolBarmanager.update(true);

        /*
         * Selection Listener
         */
        fTableViewer.addSelectionChangedListener((e) -> {
            fActionRemoveProperty.setEnabled(!e.getSelection().isEmpty());
        });

        /*
         * Table Double-click
         */
        fTableViewer.getTable().addListener(SWT.MouseDoubleClick, (e) -> {
            // Get Table item
            Point pt = new Point(e.x, e.y);
            TableItem item = fTableViewer.getTable().getItem(pt);
            
            // Double-click into empty table creates new Property
            if(item == null) {
                fActionNewProperty.run();                    
            }
            // Double-clicked in column 0 with item
            else if(item.getData() instanceof IProperty) {
                Rectangle rect = item.getBounds(0);
                if(rect.contains(pt)) {
                    handleDoubleClick((IProperty)item.getData());
                }
            }
        });
        
        /*
         * Edit table row on key press
         */
        fTableViewer.getTable().addKeyListener(KeyListener.keyPressedAdapter(e -> {
            if(e.keyCode == SWT.CR) {
                Object selected = fTableViewer.getStructuredSelection().getFirstElement();
                if(selected != null) {
                    fTableViewer.editElement(selected, 1);
                }
            }
        }));

        hookContextMenu();
    }

    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PropertiesPopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);

        menuMgr.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });

        Menu menu = menuMgr.createContextMenu(fTableViewer.getControl());
        fTableViewer.getControl().setMenu(menu);
    }

    private void fillContextMenu(IMenuManager manager) {
        manager.add(fActionNewProperty);
        manager.add(fActionNewMultipleProperty);
        manager.add(new Separator());
        manager.add(fActionRemoveProperty);
        manager.add(new Separator());
        manager.add(fActionShowKeyEditor);
    }

    /**
     * Sort Keys
     */
    private void sortKeys() {
        if(isAlive(fPropertiesElement)) {
            executeCommand(new SortPropertiesCommand(fPropertiesElement.getProperties()));
        }
    }

    /**
     * If it's a URL open in Browser
     */
    private void handleDoubleClick(IProperty selected) {
        Matcher matcher = HTMLUtils.HTML_LINK_PATTERN.matcher(selected.getValue());
        if(matcher.find()) {
            String href = matcher.group();
            try {
                HTMLUtils.openLinkInBrowser(href);
            }
            catch(PartInitException | MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * @return All unique Property Keys for an entire model (sorted)
     */
    private String[] getAllUniquePropertyKeysForModel() {
        IArchimateModel model = getArchimateModel();

        Set<String> set = new HashSet<String>();

        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();
            if(element instanceof IProperty) {
                String key = ((IProperty)element).getKey();
                if(StringUtils.isSetAfterTrim(key)) {
                    set.add(key);
                }
            }
        }

        String[] items = set.toArray(new String[set.size()]);
        Arrays.sort(items, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        return items;
    }
    
    /**
     * @return All unique Property Values for an entire model (sorted)
     */
    private String[] getAllUniquePropertyValuesForKeyForModel(String key) {
        IArchimateModel model = getArchimateModel();

        Set<String> set = new HashSet<String>();

        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();
            if(element instanceof IProperty) {
                IProperty p = (IProperty)element;
                if(p.getKey().equals(key)) {
                    String value = p.getValue();
                    if(StringUtils.isSetAfterTrim(value)) {
                        set.add(value);
                    }
                }
            }
        }

        String[] items = set.toArray(new String[set.size()]);
        Arrays.sort(items, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        return items;
    }


    // -----------------------------------------------------------------------------------------------------------------
    //
    // Drag & Drop
    //
    // -----------------------------------------------------------------------------------------------------------------

    private boolean fDragSourceValid;

    /*
     * Drag Source support
     */
    private void addDragSupport() {
        int operations = DND.DROP_MOVE;
        Transfer[] transferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
        fTableViewer.addDragSupport(operations, transferTypes, new DragSourceListener() {
            @Override
            public void dragFinished(DragSourceEvent event) {
                LocalSelectionTransfer.getTransfer().setSelection(null);
                fDragSourceValid = false;
            }

            @Override
            public void dragSetData(DragSourceEvent event) {
                // For consistency set the data to the selection even though
                // the selection is provided by the LocalSelectionTransfer
                // to the drop target adapter.
                event.data = LocalSelectionTransfer.getTransfer().getSelection();
            }

            @Override
            public void dragStart(DragSourceEvent event) {
                if(isAlive(fPropertiesElement)) {
                    IStructuredSelection selection = (IStructuredSelection)fTableViewer.getSelection();
                    LocalSelectionTransfer.getTransfer().setSelection(selection);
                    event.doit = true;
                    fDragSourceValid = true;
                }
            }
        });
    }

    private void addDropSupport() {
        int operations = DND.DROP_MOVE;
        Transfer[] transferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
        fTableViewer.addDropSupport(operations, transferTypes, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetEvent event) {
            }

            @Override
            public void dragLeave(DropTargetEvent event) {
            }

            @Override
            public void dragOperationChanged(DropTargetEvent event) {
                event.detail = getEventDetail(event);
            }

            @Override
            public void dragOver(DropTargetEvent event) {
                event.detail = getEventDetail(event);

                if(event.detail == DND.DROP_NONE) {
                    event.feedback = DND.FEEDBACK_NONE;
                }
                else {
                    event.feedback = getDragFeedbackType(event);
                }

                event.feedback |= DND.FEEDBACK_SCROLL;
            }

            @Override
            public void drop(DropTargetEvent event) {
                doDropOperation(event);
            }

            @Override
            public void dropAccept(DropTargetEvent event) {
                event.detail = getEventDetail(event); // double-check this
            }

            private int getEventDetail(DropTargetEvent event) {
                return fDragSourceValid ? DND.DROP_MOVE : DND.DROP_NONE;
            }
            
        });
    }

    @SuppressWarnings("unchecked")
    private void doDropOperation(DropTargetEvent event) {
        if(!LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)){
            return;
        }

        ISelection selection = LocalSelectionTransfer.getTransfer().getSelection();
        if(selection == null || selection.isEmpty()) {
            return;
        }

        // Determine the index position of the drop
        int index = getDropTargetPosition(event);

        // Valid position?
        List<?> list = ((IStructuredSelection)selection).toList();
        for(Object o : list) {
            IProperty property = (IProperty)o;
            int movedIndex = fPropertiesElement.getProperties().indexOf(property);
            if(movedIndex == index || (movedIndex + 1) == index) {
                return;
            }
        }

        movePropertiesToIndex((List<IProperty>)list, index);
    }

    private void movePropertiesToIndex(List<IProperty> propertiesToMove, int index) {
        EList<IProperty> properties = fPropertiesElement.getProperties();

        // Sanity check
        if(index < 0) {
            index = 0;
        }
        if(index > properties.size()) {
            index = properties.size();
        }

        CompoundCommand compoundCmd = new CompoundCommand(Messages.UserPropertiesSection_8);

        for(IProperty property : propertiesToMove) {
            int oldIndex = properties.indexOf(property);

            if(index > oldIndex) {
                index--;
            }

            if(index == oldIndex) {
                break;
            }

            compoundCmd.add(new MovePropertyCommand(properties, property, index));

            index++;
        }

        executeCommand(compoundCmd.unwrap());
    }

    private int getDropTargetPosition(DropTargetEvent event) {
        int index = -1;

        Point pt = fTableViewer.getControl().toControl(event.x, event.y);

        if(pt.y < 5) {
            index = 0;
        }
        else if(event.item != null) {
            IProperty property = (IProperty)event.item.getData();
            index = fPropertiesElement.getProperties().indexOf(property);
        }
        else {
            index = fPropertiesElement.getProperties().size();
        }

        // Dropped in after position
        int feedback = getDragFeedbackType(event);
        if(feedback == DND.FEEDBACK_INSERT_AFTER) {
            index += 1;
        }

        return index;
    }

    /**
     * Determine the feedback type for DND
     */
    private int getDragFeedbackType(DropTargetEvent event) {
        if(event.item == null) {
            return DND.FEEDBACK_NONE;
        }

        Rectangle rect = ((TableItem)event.item).getBounds();
        Point pt = fTableViewer.getControl().toControl(event.x, event.y);
        if(pt.y < rect.y + 3) {
            return DND.FEEDBACK_INSERT_BEFORE;
        }
        if(pt.y > rect.y + rect.height - 3) {
            return DND.FEEDBACK_INSERT_AFTER;
        }

        return DND.FEEDBACK_NONE; // <----- This is important otherwise we get unwanted selection cheese on XP
    }


    // -----------------------------------------------------------------------------------------------------------------
    //
    // Table functions
    //
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Label Provider
     */
    private static class LabelCellProvider extends CellLabelProvider {

        @Override
        public void update(ViewerCell cell) {
            cell.setText(getColumnText(cell.getElement(), cell.getColumnIndex()));
            cell.setForeground(getForeground(cell.getElement(), cell.getColumnIndex()));
            cell.setImage(getColumnImage(cell.getElement(), cell.getColumnIndex()));
        }
        
        public Image getColumnImage(Object element, int columnIndex) {
            if(columnIndex == 0) {
                return isLink(element) ? IArchiImages.ImageFactory.getImage(IArchiImages.ICON_BROWSER) : null;
            }
            
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            switch(columnIndex) {
                case 1:
                    String key = ((IProperty)element).getKey();
                    if(!StringUtils.isSetAfterTrim(key)) {
                        key = Messages.UserPropertiesSection_9;
                    }
                    return key;

                case 2:
                    return ((IProperty)element).getValue();

                default:
                    return null;
            }
        }

        public Color getForeground(Object element, int columnIndex) {
            if(columnIndex == 2) {
                return isLink(element) ? ColorConstants.blue : null;
            }
            return null;
        }

        @Override
        public String getToolTipText(Object element) {
            return isLink(element) ? Messages.UserPropertiesSection_21 : null;
        }
        
        private boolean isLink(Object element) {
            Matcher matcher = HTMLUtils.HTML_LINK_PATTERN.matcher(((IProperty)element).getValue());
            return matcher.find();
        }
    }

    /**
     * Key Editor
     */
    private class KeyEditingSupport extends EditingSupport {
        StringComboBoxCellEditor cellEditor;

        public KeyEditingSupport(ColumnViewer viewer) {
            super(viewer);
            cellEditor = new StringComboBoxCellEditor((Composite)viewer.getControl(), new String[0], true);
            
            // Nullify some global Action Handlers so that this cell editor can handle them
            hookCellEditorGlobalActionHandler(cellEditor);
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
            String[] items = new String[0];

            if(isAlive(fPropertiesElement)) {
                items = getAllUniquePropertyKeysForModel();
            }

            cellEditor.setItems(items);
            
            return cellEditor;
        }

        @Override
        protected boolean canEdit(Object element) {
            return true;
        }

        @Override
        protected Object getValue(Object element) {
            return ((IProperty)element).getKey();
        }

        @Override
        protected void setValue(Object element, Object value) {
            if(isAlive(fPropertiesElement)) {
                executeCommand(new EObjectFeatureCommand(Messages.UserPropertiesSection_10, (IProperty)element,
                                            IArchimatePackage.Literals.PROPERTY__KEY, value));
            }
        }
    }

    /**
     * Value Editor
     */
    private class ValueEditingSupport extends EditingSupport {
        StringComboBoxCellEditor cellEditor;

        public ValueEditingSupport(ColumnViewer viewer) {
            super(viewer);
            cellEditor = new StringComboBoxCellEditor((Composite)viewer.getControl(), new String[0], true);
            
            // Nullify some global Action Handlers so that this cell editor can handle them
            hookCellEditorGlobalActionHandler(cellEditor);
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
            String[] items = new String[0];

            if(isAlive(fPropertiesElement)) {
                items = getAllUniquePropertyValuesForKeyForModel(((IProperty)element).getKey());
            }

            cellEditor.setItems(items);
            
            return cellEditor;
        }

        @Override
        protected boolean canEdit(Object element) {
            return true;
        }

        @Override
        protected Object getValue(Object element) {
            return ((IProperty)element).getValue();
        }

        @Override
        protected void setValue(Object element, Object value) {
            if(isAlive(fPropertiesElement)) {
                executeCommand(new EObjectFeatureCommand(Messages.UserPropertiesSection_11, (IProperty)element,
                                        IArchimatePackage.Literals.PROPERTY__VALUE, value));
            }
        }
    }
    
    /**
     * Set some editing global Action Handlers to null when the cell editor is activated
     * And restore them when the cell editor is deactivated.
     */
    private void hookCellEditorGlobalActionHandler(CellEditor cellEditor) {
        Listener listener = new Listener() {
            // We have to disable the action handlers of the the active Editor/View site *and* the Properties View Site
            GlobalActionDisablementHandler propertiesViewGlobalActionHandler, globalActionHandler;
            
            @Override
            public void handleEvent(Event event) {
                switch(event.type) {
                    case SWT.Activate:
                        // The Properties View site action bars
                        IActionBars actionBars = fPage.getSite().getActionBars();
                        propertiesViewGlobalActionHandler = new GlobalActionDisablementHandler(actionBars);
                        propertiesViewGlobalActionHandler.clearGlobalActions();
                        
                        // The active View or Editor site's action bars also have to be updated
                        globalActionHandler = new GlobalActionDisablementHandler();
                        globalActionHandler.update();
                        break;

                    case SWT.Deactivate:
                        if(propertiesViewGlobalActionHandler != null) {
                            propertiesViewGlobalActionHandler.restoreGlobalActions();
                            globalActionHandler.update();
                        }
                        break;

                    default:
                        break;
                }
            }
        };
        
        cellEditor.getControl().addListener(SWT.Activate, listener);
        cellEditor.getControl().addListener(SWT.Deactivate, listener);
    }

    // -----------------------------------------------------------------------------------------------------------------
    //
    // Commands
    //
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * New Property Command
     */
    private static class NewPropertyCommand extends Command {
        private EList<IProperty> properties;
        private IProperty property;
        private int index;

        NewPropertyCommand(EList<IProperty> properties, IProperty property, int index) {
            this.properties = properties;
            this.property = property;
            this.index = index;
            setLabel(Messages.UserPropertiesSection_12);
        }

        @Override
        public void execute() {
            if(index < 0 || index > properties.size()) {
                properties.add(property);
            }
            else {
                properties.add(index, property);
            }
        }

        @Override
        public void undo() {
            properties.remove(property);
        }

        @Override
        public void dispose() {
            properties = null;
            property = null;
        }
    }

    /**
     * Remove Property Command
     */
    private static class RemovePropertyCommand extends Command {
        private EList<IProperty> properties;
        private IProperty property;
        private int index;

        RemovePropertyCommand(EList<IProperty> properties, IProperty property) {
            this.properties = properties;
            this.property = property;
        }

        @Override
        public void execute() {
            // Ensure index is stored just before execute because if this is part of a composite action
            // then the index positions will have changed
            index = properties.indexOf(property); 
            if(index != -1) {
                properties.remove(property);
            }
        }

        @Override
        public void undo() {
            if(index != -1) {
                properties.add(index, property);
            }
        }

        @Override
        public void dispose() {
            properties = null;
            property = null;
        }
    }

    /**
     * Move Property Command
     */
    private static class MovePropertyCommand extends Command {
        private EList<IProperty> properties;
        private IProperty property;
        private int oldIndex;
        private int newIndex;

        MovePropertyCommand(EList<IProperty> properties, IProperty property, int newIndex) {
            this.properties = properties;
            this.property = property;
            this.newIndex = newIndex;
            setLabel(Messages.UserPropertiesSection_13);
        }

        @Override
        public void execute() {
            oldIndex = properties.indexOf(property);
            properties.move(newIndex, property);
        }

        @Override
        public void undo() {
            properties.move(oldIndex, property);
        }

        @Override
        public void dispose() {
            properties = null;
            property = null;
        }
    }

    /**
     * Sort Properties Command
     */
    private static class SortPropertiesCommand extends Command implements Comparator<IProperty>  {
        private EList<IProperty> properties;
        private List<IProperty> original;

        public SortPropertiesCommand(EList<IProperty> properties) {
            setLabel(Messages.UserPropertiesSection_14);
            this.properties = properties;

            // Keep a copy of the original order
            original = new ArrayList<IProperty>(properties);
        }

        @Override
        public boolean canExecute() {
            if(properties.size() < 2) {
                return false;
            }

            EList<IProperty> temp = new BasicEList<IProperty>(properties);
            ECollections.sort(temp, this);

            for(int i = 0; i < temp.size(); i++) {
                if(temp.get(i) != properties.get(i)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public void execute() {
            ECollections.sort(properties, this);
        }

        @Override
        public void undo() {
            properties.clear();
            properties.addAll(original);
        }

        @Override
        public int compare(IProperty p1, IProperty p2) {
            String key1 = StringUtils.safeString(p1.getKey());
            String key2 = StringUtils.safeString(p2.getKey());
            return key1.compareToIgnoreCase(key2);
        }

        @Override
        public void dispose() {
            properties = null;
            original = null;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    //
    // Multiple Add Dialog
    //
    // -----------------------------------------------------------------------------------------------------------------

    private class MultipleAddDialog extends ExtendedTitleAreaDialog {
        private CheckboxTableViewer tableViewer;
        private Button buttonSelectAll, buttonDeselectAll;

        private CompoundCommand compoundCmd;
        private String[] keys;

        public MultipleAddDialog(Shell parentShell) {
            super(parentShell, "ArchimatePropertiesMultipleAddDialog"); //$NON-NLS-1$
            setTitleImage(IArchiImages.ImageFactory.getImage(IArchiImages.ECLIPSE_IMAGE_IMPORT_PREF_WIZARD));
            setShellStyle(getShellStyle() | SWT.RESIZE);

            keys = getAllUniquePropertyKeysForModel();
        }

        @Override
        protected void configureShell(Shell shell) {
            super.configureShell(shell);
            shell.setText(Messages.UserPropertiesSection_15);
        }

        @Override
        protected Control createButtonBar(Composite parent) {
            Control c = super.createButtonBar(parent);
            if(keys.length == 0) {
                getButton(IDialogConstants.OK_ID).setEnabled(false);
                buttonSelectAll.setEnabled(false);
                buttonDeselectAll.setEnabled(false);
            }
            return c;
        }

        @Override
        protected Control createDialogArea(Composite parent) {
            // Help
            PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

            setTitle(Messages.UserPropertiesSection_16);
            setMessage(Messages.UserPropertiesSection_17);

            Composite composite = (Composite)super.createDialogArea(parent);

            Composite client = new Composite(composite, SWT.NULL);
            GridLayout layout = new GridLayout(2, false);
            client.setLayout(layout);
            client.setLayoutData(new GridData(GridData.FILL_BOTH));

            createTableControl(client);
            createButtonPanel(client);

            return composite;
        }

        private void createTableControl(Composite parent) {
            Composite tableComp = new Composite(parent, SWT.BORDER);
            TableColumnLayout tableLayout = new TableColumnLayout();
            tableComp.setLayout(tableLayout);
            tableComp.setLayoutData(new GridData(GridData.FILL_BOTH));

            Table table = new Table(tableComp, SWT.MULTI | SWT.FULL_SELECTION | SWT.CHECK);
            tableViewer = new CheckboxTableViewer(table);
            tableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
            
            // Mac Silicon Item height
            UIUtils.fixMacSiliconItemHeight(table);
            
            tableViewer.getTable().setLinesVisible(true);

            tableViewer.setComparator(new ViewerComparator());

            // Column
            TableViewerColumn columnKey = new TableViewerColumn(tableViewer, SWT.NONE, 0);
            tableLayout.setColumnData(columnKey.getColumn(), new ColumnWeightData(100, true));

            // Content Provider
            tableViewer.setContentProvider(new IStructuredContentProvider() {
                @Override
                public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
                }

                @Override
                public void dispose() {
                }

                @Override
                public Object[] getElements(Object inputElement) {
                    return keys;
                }
            });

            // Label Provider
            tableViewer.setLabelProvider(new LabelProvider());
            tableViewer.setInput(""); // anything will do //$NON-NLS-1$
        }

        private void createButtonPanel(Composite parent) {
            Composite client = new Composite(parent, SWT.NULL);

            GridLayout layout = new GridLayout();
            layout.marginHeight = 0;
            layout.marginWidth = 0;
            client.setLayout(layout);

            GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
            client.setLayoutData(gd);

            buttonSelectAll = new Button(client, SWT.PUSH);
            buttonSelectAll.setText(Messages.UserPropertiesSection_18);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            buttonSelectAll.setLayoutData(gd);
            buttonSelectAll.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    tableViewer.setCheckedElements(keys);
                }
            });

            buttonDeselectAll = new Button(client, SWT.PUSH);
            buttonDeselectAll.setText(Messages.UserPropertiesSection_19);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            buttonDeselectAll.setLayoutData(gd);
            buttonDeselectAll.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    tableViewer.setCheckedElements(new Object[] {});
                }
            });
        }

        @Override
        protected void okPressed() {
            compoundCmd = new EObjectNonNotifyingCompoundCommand(fPropertiesElement, Messages.UserPropertiesSection_20);

            for(Object o : tableViewer.getCheckedElements()) {
                IProperty property = IArchimateFactory.eINSTANCE.createProperty();
                property.setKey((String)o);
                compoundCmd.add(new NewPropertyCommand(fPropertiesElement.getProperties(), property, -1));
            }

            super.okPressed();
        }

        Command getCommand() {
            return compoundCmd;
        }

        @Override
        protected Point getDefaultDialogSize() {
            return new Point(400, 250);
        }
    }

}
