/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import java.net.MalformedURLException;
import java.text.Collator;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionListener;
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

    // Selected properties elements
    private List<IProperties> fPropertiesElements = new ArrayList<>();

    private TableViewer fTableViewer;
    
    private IAction fActionNewProperty, fActionNewMultipleProperties, fActionRemoveProperties, fActionShowKeyEditor;

    private boolean ignoreMessages;
    
    // Indicates multiple property values (unlikely to be a user-given value)
    private static final String multipleValuesIndicator = UUID.randomUUID().toString();
    
    // Maximum amount of items to display when getting all unique keys and values for combo boxes
    private static final int MAX_ITEMS_COMBO = 20000;
    
    // Display all items
    private static final int MAX_ITEMS_ALL = -1;
    
    @Override
    protected void createControls(Composite parent) {
        createTableControl(parent);
        createActionsAndToolbar(parent);
        
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

            if(feature == IArchimatePackage.Literals.PROPERTY__KEY || feature == IArchimatePackage.Literals.PROPERTY__VALUE) {
                if(isMultiSelection()) {
                    fTableViewer.refresh();
                }
                else {
                    fTableViewer.update(msg.getNotifier(), null);
                }
            }
        }
    }
    
    @Override
    protected void addAdapter() {
        if(getEObjects() != null && getECoreAdapter() != null) {
            for(IArchimateModelObject eObject : getEObjects()) {
                if(!eObject.eAdapters().contains(getECoreAdapter())) {
                    eObject.eAdapters().add(getECoreAdapter());
                }
            }
        }
    }
    
    @Override
    protected void removeAdapter() {
        if(getEObjects() != null && getECoreAdapter() != null) {
            for(IArchimateModelObject eObject : getEObjects()) {
                eObject.eAdapters().remove(getECoreAdapter());
            }
        }
    }

    @Override
    protected void update() {
        // Get selected properties elements
        fPropertiesElements = new ArrayList<>();
        
        for(IArchimateModelObject obj : getEObjects()) {
            if(obj instanceof IProperties p) {
                fPropertiesElements.add(p);
            }
        }
        
        fTableViewer.setInput(fPropertiesElements);
        
        // avoid bogus horizontal scrollbar cheese
        fTableViewer.getTable().getParent().layout();
        
        // Locked
        updateLocked();
    }
    
    private void updateLocked() {
        boolean locked = isLocked(getFirstSelectedObject());
        fTableViewer.getTable().setEnabled(!locked);
        fActionNewProperty.setEnabled(!locked);
        fActionRemoveProperties.setEnabled(!locked && !fTableViewer.getSelection().isEmpty());
        fActionNewMultipleProperties.setEnabled(!locked);
    }

    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }

    /**
     * Create table
     */
    private void createTableControl(Composite parent) {
        // Table Composite
        Composite tableComp = createTableComposite(parent, SWT.NULL);
        TableColumnLayout tableLayout = (TableColumnLayout)tableComp.getLayout();
        
        // Table Viewer
        fTableViewer = new TableViewer(tableComp, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);

        // Font
        UIUtils.setFontFromPreferences(fTableViewer.getTable(), IPreferenceConstants.PROPERTIES_TABLE_FONT, true);
        
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
        fTableViewer.setContentProvider(new TableContentProvider());

        // Label Provider
        fTableViewer.setLabelProvider(new LabelCellProvider());
        
        // Enable tooltips
        ColumnViewerToolTipSupport.enableFor(fTableViewer);

        /*
         * Selection Listener
         */
        fTableViewer.addSelectionChangedListener(e -> {
            fActionRemoveProperties.setEnabled(!e.getSelection().isEmpty());
        });

        /*
         * Table Double-click
         */
        fTableViewer.getTable().addMouseListener(MouseListener.mouseDoubleClickAdapter(e -> {
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
        }));
        
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
    }
    
    /**
     * Create actions, local toolbar and context menu
     */
    private void createActionsAndToolbar(Composite parent) {
        // New Property Action
        fActionNewProperty = new NewPropertyAction();

        // New Multiple Properties Action
        fActionNewMultipleProperties = new NewMultiplePropertiesAction();

        // Remove Properties Action
        fActionRemoveProperties = new RemovePropertiesAction();

        // Manage Keys Action
        fActionShowKeyEditor = new ShowKeyEditorAction();

        // Toolbar
        ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.VERTICAL);
        getWidgetFactory().adapt(toolBar);
        GridDataFactory.fillDefaults().align(SWT.END, SWT.TOP).applyTo(toolBar);

        ToolBarManager toolBarmanager = new ToolBarManager(toolBar);
        toolBarmanager.add(fActionNewProperty);
        toolBarmanager.add(fActionNewMultipleProperties);
        toolBarmanager.add(fActionRemoveProperties);
        toolBarmanager.add(fActionShowKeyEditor);
        toolBarmanager.update(true);

        // Hook into the context menu
        MenuManager menuMgr = new MenuManager("#PropertiesPopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(manager -> {
            manager.add(fActionNewProperty);
            manager.add(fActionNewMultipleProperties);
            manager.add(new Separator());
            manager.add(fActionRemoveProperties);
            manager.add(new Separator());
            manager.add(fActionShowKeyEditor);
        });

        Menu menu = menuMgr.createContextMenu(fTableViewer.getControl());
        fTableViewer.getControl().setMenu(menu);
    }

    /**
     * Sort Keys
     */
    private void sortKeys() {
        if(!isMultiSelection() && isAlive(getFirstSelectedElement())) {
            executeCommand(new SortPropertiesCommand(getFirstSelectedElement().getProperties()));
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
    
    // -----------------------------------------------------------------------------------------------------------------
    //
    // Properties handling
    //
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @return true if more than one element is selected
     */
    private boolean isMultiSelection() {
        return fPropertiesElements.size() > 1;
    }
    
    /**
     * @return The first selected element with properties, or null
     */
    private IProperties getFirstSelectedElement() {
        return fPropertiesElements.size() == 0 ? null : fPropertiesElements.get(0);
    }
    
    /**
     * @return a list of common properties for selected elements that share the same key
     */
    private List<IProperty> getCommonProperties() {
        List<IProperty> properties = new ArrayList<>();
        Map<String, Entry<Set<String>, Set<IProperties>>> map = new LinkedHashMap<>(); // Map of key -> values -> elements
        
        // Iterate thru all selected elements and their properties
        for(IProperties propertiesElement : fPropertiesElements) {
            for(IProperty property : propertiesElement.getProperties()) {
                // Get the first matching property in case the element has more than one with the same key
                property = getFirstMatchingProperty(propertiesElement.getProperties(), property);
                
                // Do we have this property key in the map?
                Entry<Set<String>, Set<IProperties>> entry = map.get(property.getKey());
                
                // No, create and add a new entry
                if(entry == null) {
                    entry = new SimpleEntry<>(new HashSet<>(), new HashSet<>());
                    map.put(property.getKey(), entry);
                }
                
                entry.getKey().add(property.getValue()); // Add the property value
                entry.getValue().add(propertiesElement); // Add the properties element
            }
        }
        
        for(Entry<String, Entry<Set<String>, Set<IProperties>>> entry : map.entrySet()) {
            // If the size of elements equals the size of selected elements, then they all have the property key in common
            if(entry.getValue().getValue().size() == fPropertiesElements.size()) {
                // If there is only one value, use that, else use the multipleValuesIndicator
                IProperty property = IArchimateFactory.eINSTANCE.createProperty(entry.getKey(),
                                                                                entry.getValue().getKey().size() == 1 ? entry.getValue().getKey().iterator().next() : multipleValuesIndicator);
                properties.add(property);
            }
        }
        
        return properties;
    }
    
    /**
     * @return the first matching property in properties that matches the given property
     */
    private IProperty getFirstMatchingProperty(List<IProperty> properties, IProperty property) {
        for(IProperty p : properties) {
            if(Objects.equals(p.getKey(), property.getKey())) { // match on key
                return p;
            }
        }
        return null;
    }
    
    /**
     * Return the Archimate model bound to the properties element
     */
    private IArchimateModel getArchimateModel() {
        if(getFirstSelectedElement() instanceof IArchimateModelObject) {
            return ((IArchimateModelObject)getFirstSelectedElement()).getArchimateModel();
        }
        return null;
    }
    
    /**
     * @return All unique Property Keys for an entire model (sorted)
     */
    private String[] getAllUniquePropertyKeysForModel(int maxSize) {
        IArchimateModel model = getArchimateModel();
        Set<String> set = new LinkedHashSet<String>(); // LinkedHashSet is faster when sorting
        int count = 0;
        
        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();
            if(element instanceof IProperty p) {
                if(maxSize != MAX_ITEMS_ALL && ++count > maxSize) { // Don't get more than this
                    break;
                }
                String key = p.getKey();
                if(StringUtils.isSetAfterTrim(key)) {
                    set.add(key);
                }
            }
        }

        String[] items = set.toArray(new String[set.size()]);
        Arrays.sort(items, (s1, s2) -> s1.compareToIgnoreCase(s2)); // Don't use Collator.getInstance() as it's too slow

        return items;
    }
    
    /**
     * @return All unique Property Values for an entire model (sorted)
     */
    private String[] getAllUniquePropertyValuesForKeyForModel(String key, int maxSize) {
        IArchimateModel model = getArchimateModel();
        Set<String> set = new LinkedHashSet<String>(); // LinkedHashSet is faster when sorting
        int count = 0;

        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();
            if(element instanceof IProperty p) {
                if(maxSize != MAX_ITEMS_ALL && ++count > maxSize) { // Don't get more than this
                    break;
                }
                if(p.getKey().equals(key)) {
                    String value = p.getValue();
                    if(StringUtils.isSetAfterTrim(value)) {
                        set.add(value);
                    }
                }
            }
        }

        String[] items = set.toArray(new String[set.size()]);
        Arrays.sort(items, (s1, s2) -> s1.compareToIgnoreCase(s2)); // Don't use Collator.getInstance() as it's too slow

        return items;
    }


    // -----------------------------------------------------------------------------------------------------------------
    //
    // Table functions
    //
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Content Provider
     */
    private class TableContentProvider implements IStructuredContentProvider {
        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

        @Override
        public void dispose() {
        }

        @Override
        public Object[] getElements(Object inputElement) {
            // More than one element selected
            if(isMultiSelection()) {
                return getCommonProperties().toArray();
            }

            // One element selected
            return isAlive(getFirstSelectedElement()) ? getFirstSelectedElement().getProperties().toArray() : new Object[0];
        }
    }

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
                    return StringUtils.isSetAfterTrim(key) ? key : Messages.UserPropertiesSection_9;

                case 2:
                    String value = ((IProperty)element).getValue();
                    return multipleValuesIndicator.equals(value) ? Messages.UserPropertiesSection_22 : value;

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
            String[] items = isAlive(getFirstSelectedElement()) ? getAllUniquePropertyKeysForModel(MAX_ITEMS_COMBO) : new String[0];
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
            CompoundCommand compoundCmd = new CompoundCommand();

            for(IProperties propertiesElement : fPropertiesElements) {
                IProperty property = (IProperty)element;
                
                if(isAlive(propertiesElement)) {
                    if(isMultiSelection()) {
                        property = getFirstMatchingProperty(propertiesElement.getProperties(), property);
                    }
                    if(property != null) {
                        Command cmd = new EObjectFeatureCommand(Messages.UserPropertiesSection_10, property, IArchimatePackage.Literals.PROPERTY__KEY, value);
                        if(cmd.canExecute()) {
                            compoundCmd.add(cmd);
                        }
                    }
                }
            }
            
            // If multi-selection update the local Property without notifications so we don't refresh the table with fresh contents
            // and avoid problems with tab traversal
            if(isMultiSelection()) {
                try {
                    ignoreMessages = true;
                    executeCommand(compoundCmd.unwrap());
                    ((IProperty)element).setKey((String)value);
                    fTableViewer.update(element, null);
                }
                finally {
                    ignoreMessages = false;
                }
            }
            else {
                executeCommand(compoundCmd.unwrap());
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
            String[] items = isAlive(getFirstSelectedElement()) ? getAllUniquePropertyValuesForKeyForModel(((IProperty)element).getKey(), MAX_ITEMS_COMBO) : new String[0];
            cellEditor.setItems(items);
            return cellEditor;
        }

        @Override
        protected boolean canEdit(Object element) {
            return true;
        }

        @Override
        protected Object getValue(Object element) {
            String value = ((IProperty)element).getValue();
            return multipleValuesIndicator.equals(value) ? "" : value; //$NON-NLS-1$
        }

        @Override
        protected void setValue(Object element, Object value) {
            CompoundCommand compoundCmd = new CompoundCommand();

            for(IProperties propertiesElement : fPropertiesElements) {
                IProperty property = (IProperty)element;
                
                if(isAlive(propertiesElement)) {
                    if(isMultiSelection()) {
                        property = getFirstMatchingProperty(propertiesElement.getProperties(), property);
                    }
                    if(property != null) {
                        Command cmd = new EObjectFeatureCommand(Messages.UserPropertiesSection_11, property, IArchimatePackage.Literals.PROPERTY__VALUE, value);
                        if(cmd.canExecute()) {
                            compoundCmd.add(cmd);
                        }
                    }
                }
            }
            
            // If multi-selection update the local Property without notifications so we don't refresh the table with fresh contents
            // and avoid problems with tab traversal
            if(isMultiSelection()) {
                try {
                    ignoreMessages = true;
                    executeCommand(compoundCmd.unwrap());
                    ((IProperty)element).setValue((String)value);
                    fTableViewer.update(element, null);
                }
                finally {
                    ignoreMessages = false;
                }
            }
            else {
                executeCommand(compoundCmd.unwrap());
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
    // Table Drag & Drop
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
                if(isMultiSelection()) {
                    event.doit = false;
                }
                else if(isAlive(getFirstSelectedElement())) {
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
            int movedIndex = getFirstSelectedElement().getProperties().indexOf(property);
            if(movedIndex == index || (movedIndex + 1) == index) {
                return;
            }
        }

        movePropertiesToIndex((List<IProperty>)list, index);
    }

    private void movePropertiesToIndex(List<IProperty> propertiesToMove, int index) {
        EList<IProperty> properties = getFirstSelectedElement().getProperties();

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
            index = getFirstSelectedElement().getProperties().indexOf(property);
        }
        else {
            index = getFirstSelectedElement().getProperties().size();
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
    // Actions
    //
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * New Property Action
     */
    private class NewPropertyAction extends Action {
        private NewPropertyAction() {
            super(Messages.UserPropertiesSection_2);
            setToolTipText(Messages.UserPropertiesSection_2);
            setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_PLUS));
        }

        @Override
        public void run() {
            fTableViewer.applyEditorValue(); // complete any current editing
            IProperty newProperty = null;
            
            if(isMultiSelection()) {
                CompoundCommand cmd = new CompoundCommand();
                
                for(IProperties propertiesElement : fPropertiesElements) {
                    if(isAlive(propertiesElement)) {
                        IProperty property = IArchimateFactory.eINSTANCE.createProperty();
                        cmd.add(new NewPropertyCommand(propertiesElement.getProperties(), property, -1));
                    }
                }
                
                executeCommand(cmd.unwrap());
                
                if(fTableViewer.getTable().getItemCount() > 0) {
                    newProperty = (IProperty)fTableViewer.getElementAt(fTableViewer.getTable().getItemCount() - 1);
                }
            }
            else if(isAlive(getFirstSelectedElement())) {
                newProperty = IArchimateFactory.eINSTANCE.createProperty();
                int index = -1;
                IProperty selected = (IProperty)((IStructuredSelection)fTableViewer.getSelection()).getFirstElement();
                if(selected != null) {
                    index = getFirstSelectedElement().getProperties().indexOf(selected) + 1;
                }
                
                executeCommand(new NewPropertyCommand(getFirstSelectedElement().getProperties(), newProperty, index));
            }
            
            if(newProperty != null) {
                fTableViewer.editElement(newProperty, 1);
            }
        }
    }
    
    /**
     * New Multiple Properties Action
     */
    private class NewMultiplePropertiesAction extends Action {
        private NewMultiplePropertiesAction() {
            super(Messages.UserPropertiesSection_3);
            setToolTipText(Messages.UserPropertiesSection_3);
            setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_MUTIPLE));
        }

        @Override
        public void run() {
            if(isAlive(getFirstSelectedElement())) {
                MultipleAddDialog dialog = new MultipleAddDialog(fPage.getSite().getShell(), getAllUniquePropertyKeysForModel(MAX_ITEMS_ALL));
                if(dialog.open() != Window.CANCEL) {
                    List<String> newKeys = dialog.getSelectedKeys();
                    if(newKeys == null || newKeys.isEmpty()) {
                        return;
                    }
                    
                    CompoundCommand cmd = isMultiSelection() ? new CompoundCommand(Messages.UserPropertiesSection_20) : 
                                                               new EObjectNonNotifyingCompoundCommand(getFirstSelectedElement(), Messages.UserPropertiesSection_20);
                    
                    // Add properties that are not already present
                    boolean addUnique = dialog.getReturnCode() == IDialogConstants.CLIENT_ID;
                    
                    for(IProperties propertiesElement : fPropertiesElements) {
                        if(isAlive(propertiesElement)) {
                            for(String key : newKeys) {
                                if(!(addUnique && hasPropertyKey(propertiesElement, key))) {
                                    IProperty property = IArchimateFactory.eINSTANCE.createProperty(key, ""); //$NON-NLS-1$
                                    cmd.add(new NewPropertyCommand(propertiesElement.getProperties(), property, -1));
                                }
                            }
                        }
                    }
                    
                    executeCommand(cmd.unwrap());
                }
            }
        }
        
        /**
         * @return true if propertiesElement already has a property by key
         */
        private boolean hasPropertyKey(IProperties propertiesElement, String key) {
            for(IProperty property : propertiesElement.getProperties()) {
                if(key.equals(property.getKey())) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Remove properties Action
     */
    private class RemovePropertiesAction extends Action {
        private RemovePropertiesAction() {
            super(Messages.UserPropertiesSection_4);
            setToolTipText(Messages.UserPropertiesSection_4);
            setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_SMALL_X));
            setEnabled(false);
        }

        @Override
        public void run() {
            CompoundCommand cmd = isMultiSelection() ? new CompoundCommand() : new EObjectNonNotifyingCompoundCommand(getFirstSelectedElement());
            
            for(Object o : ((IStructuredSelection)fTableViewer.getSelection()).toList()) {
                IProperty property = (IProperty)o;
                
                for(IProperties propertiesElement : fPropertiesElements) {
                    if(isAlive(propertiesElement)) {
                        if(isMultiSelection()) {
                            property = getFirstMatchingProperty(propertiesElement.getProperties(), property);
                        }
                        if(property != null) {
                            cmd.add(new RemovePropertyCommand(propertiesElement.getProperties(), property));
                        }
                    }
                }
            }
            
            executeCommand(cmd);
        }
    }

    /**
     * Manage global keys Action
     */
    private class ShowKeyEditorAction extends Action {
        private ShowKeyEditorAction() {
            super(Messages.UserPropertiesSection_7);
            setToolTipText(Messages.UserPropertiesSection_7);
            setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_COG));
        }

        @Override
        public void run() {
            if(isAlive(getFirstSelectedElement())) {
                UserPropertiesManagerDialog dialog = new UserPropertiesManagerDialog(fPage.getSite().getShell(),
                        getArchimateModel());
                dialog.open();
            }
        }
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
            setLabel(Messages.UserPropertiesSection_5);
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
    private static class SortPropertiesCommand extends Command {
        private EList<IProperty> properties;
        private List<IProperty> original;
        private Collator collator = Collator.getInstance();

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

            List<IProperty> temp = new ArrayList<IProperty>(properties);
            Collections.sort(temp, (p1, p2) -> collator.compare(p1.getKey(), p2.getKey()));

            for(int i = 0; i < temp.size(); i++) {
                if(temp.get(i) != properties.get(i)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public void execute() {
            ECollections.sort(properties, (p1, p2) -> collator.compare(p1.getKey(), p2.getKey()));
        }

        @Override
        public void undo() {
            properties.clear();
            properties.addAll(original);
        }

        @Override
        public void dispose() {
            properties = null;
            original = null;
            collator = null;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    //
    // Multiple Add Dialog
    //
    // -----------------------------------------------------------------------------------------------------------------

    private static class MultipleAddDialog extends ExtendedTitleAreaDialog {
        private CheckboxTableViewer tableViewer;
        private Button buttonSelectAll, buttonDeselectAll;

        private String[] keys;
        private List<String> selectedKeys;

        public MultipleAddDialog(Shell parentShell, String[] keys) {
            super(parentShell, "ArchimatePropertiesMultipleAddDialog"); //$NON-NLS-1$
            setTitleImage(IArchiImages.ImageFactory.getImage(IArchiImages.ECLIPSE_IMAGE_IMPORT_PREF_WIZARD));
            setShellStyle(getShellStyle() | SWT.RESIZE);
            this.keys = keys;
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

            tableViewer = CheckboxTableViewer.newCheckList(tableComp, SWT.MULTI | SWT.FULL_SELECTION);
            tableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
            
            tableViewer.getTable().setLinesVisible(true);

            // Column
            TableViewerColumn columnKey = new TableViewerColumn(tableViewer, SWT.NONE, 0);
            tableLayout.setColumnData(columnKey.getColumn(), new ColumnWeightData(100, true));
            
            // Content Provider
            tableViewer.setContentProvider(new IStructuredContentProvider() {
                @Override
                public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
                }

                @Override
                public Object[] getElements(Object inputElement) {
                    return keys;
                }

                @Override
                public void dispose() {
                }
            });
            
            // Label Provider
            tableViewer.setLabelProvider(new LabelProvider());
            tableViewer.setInput(keys);
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
            buttonSelectAll.addSelectionListener(SelectionListener.widgetSelectedAdapter(e ->  {
                tableViewer.setCheckedElements(keys);
            }));

            buttonDeselectAll = new Button(client, SWT.PUSH);
            buttonDeselectAll.setText(Messages.UserPropertiesSection_19);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            buttonDeselectAll.setLayoutData(gd);
            buttonDeselectAll.addSelectionListener(SelectionListener.widgetSelectedAdapter(e ->  {
                tableViewer.setCheckedElements(new Object[] {});
            }));
        }
        
        @Override
        protected void createButtonsForButtonBar(Composite parent) {
            createButton(parent, IDialogConstants.OK_ID, Messages.UserPropertiesSection_6, true);
            createButton(parent, IDialogConstants.CLIENT_ID, Messages.UserPropertiesSection_23, false);
            createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        }
        
        @Override
        protected void buttonPressed(int buttonId) {
            selectedKeys = new ArrayList<>();
            for(Object o : tableViewer.getCheckedElements()) {
                selectedKeys.add((String)o);
            }

            setReturnCode(buttonId);
            close();
        }
        
        List<String> getSelectedKeys() {
            return selectedKeys;
        }

        @Override
        protected Point getDefaultDialogSize() {
            return new Point(400, 250);
        }
    }

}
