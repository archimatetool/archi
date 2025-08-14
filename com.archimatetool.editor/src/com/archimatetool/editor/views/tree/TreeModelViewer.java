/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchWindow;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.FontFactory;
import com.archimatetool.editor.ui.ThemeUtils;
import com.archimatetool.editor.ui.components.TreeTextCellEditor;
import com.archimatetool.editor.ui.textrender.TextRenderer;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.editor.views.tree.commands.RenameCommandHandler;
import com.archimatetool.editor.views.tree.search.SearchFilter;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.INameable;




/**
 * Tree Viewer for Model Tree View
 * 
 * Text Cell Editing code inspired by http://ramkulkarni.com/blog/in-place-editing-in-eclipse-treeviewer/
 * 
 * @author Phillip Beauvoir
 */
public class TreeModelViewer extends TreeViewer {
    
    /**
     * Show elements as grey if not in Viewpoint
     */
    private TreeViewpointFilterProvider viewpointFilterProvider;
    
    /**
     * Label Provider needs this
     */
    private SearchFilter searchFilter;
    
    /**
     * Root expanded visible tree elements, saved when DrillDownAdapter is used.
     */
    private Object[] rootVisibleExpandedElements;
    
    /**
     * Listener for theme font change
     */
    private IPropertyChangeListener propertyChangeListener = event -> {
        if(IPreferenceConstants.MODEL_TREE_FONT.equals(event.getProperty())) {
            ((ModelTreeViewerLabelProvider)getLabelProvider()).resetFonts();
        }
    };
    
    public TreeModelViewer(Composite parent, int style) {
        this(null, parent, style);
    }
    
    public TreeModelViewer(IWorkbenchWindow window, Composite parent, int style) {
        super(parent, style | SWT.MULTI | SWT.VIRTUAL);
        
        // Set CSS ID and apply the style so that we can immediately get the italic and bold fonts from the base font style
        ThemeUtils.registerCssId(getTree(), "ModelTree"); //$NON-NLS-1$
        ThemeUtils.applyStyles(getTree(), false);
        
        // Set font in case CSS theming is disabled
        ThemeUtils.setFontIfCssThemingDisabled(getTree(), IPreferenceConstants.MODEL_TREE_FONT);

        setContentProvider(new ModelTreeViewerContentProvider());
        setLabelProvider(new ModelTreeViewerLabelProvider());
        
        setUseHashlookup(true);
        
        // Cell Editor
        TreeTextCellEditor cellEditor = new TreeTextCellEditor(getTree());
        setColumnProperties(new String[]{ "col1" }); //$NON-NLS-1$
        setCellEditors(new CellEditor[]{ cellEditor });
        
        // Edit cell programmatically, not on mouse click
        TreeViewerEditor.create(this, new ColumnViewerEditorActivationStrategy(this){
            @Override
            protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
                return event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
            }  
            
        }, ColumnViewerEditor.DEFAULT);
        
        setCellModifier(new ICellModifier() {
            @Override
            public void modify(Object element, String property, Object value) {
                if(element instanceof TreeItem treeItem) {
                    if(treeItem.getData() instanceof INameable nameable) {
                        RenameCommandHandler.doRenameCommand(nameable, value.toString());
                    }
                }
            }
            
            @Override
            public Object getValue(Object element, String property) {
                if(element instanceof INameable nameable) {
                    return nameable.getName();
                }
                return null;
            }
            
            @Override
            public boolean canModify(Object element, String property) {
                return RenameCommandHandler.canRename(element);
            }
        });
        
        // Filter
        if(window != null) {
            viewpointFilterProvider = new TreeViewpointFilterProvider(window, this);
        }
        
        // Listen to theme font changes
        if(ThemeUtils.getThemeManager() != null) {
            ThemeUtils.getThemeManager().addPropertyChangeListener(propertyChangeListener);
        }
        
        // Remove listener and clean up
        getTree().addDisposeListener(e -> {
            if(ThemeUtils.getThemeManager() != null) {
                ThemeUtils.getThemeManager().removePropertyChangeListener(propertyChangeListener);
            }
            
            viewpointFilterProvider = null;
            searchFilter = null;
            rootVisibleExpandedElements = null;
        });
    }
    
    /**
     * Edit an element on the tree
     * @param element the element to be edited
     */
    public void editElement(Object element) {
        editElement(element, 0);
    }
    
    @Override
    public void editElement(Object element, int column) {
        /*
         * Important to set focus first!
         * 
         * 1. If the focus is on either the Hints, Help, Welcome, or Cheat Sheet Views (all Views with a Browser control)
         * 2. "New Empty Model" or "New Model With Canvas" is selected from the main "File" menu
         * 3. Or "Create a new ArchiMate Model" button pressed in the "Welcome" View
         * 4. Then a focus lost event will occur
         * 5. And cause a NPE in ColumnViewerEditor#activateCellEditor
         */
        getControl().setFocus();
        super.editElement(element, column);
    }
    
    /**
     * Refresh the tree viewer in the background.
     */
    void refreshInBackground() {
        refreshInBackground(null);
    }

    /**
     * Refresh the tree viewer starting with the given element in the background.
     * @param element The element, or null to refresh the root.
     */
    void refreshInBackground(Object element) {
        getControl().getDisplay().asyncExec(() -> {
            if(!getControl().isDisposed()) { // check inside run loop
                try {
                    getControl().setRedraw(false);
                    // If element is not visible in case of drill-down then refresh the root element
                    refresh(element != null ? (findItem(element) != null ? element : null) : null);
                }
                finally {
                    getControl().setRedraw(true);
                }
            }
        });
    }
    
    /**
     * Update the tree viewer's elements in the background.
     */
    void updateInBackground() {
        updateInBackground(null);
    }

    /**
     * Update the given element and all of its child elements in the background.
     * @param element The element, or null to update the root.
     */
    void updateInBackground(Object element) {
        getControl().getDisplay().asyncExec(() -> {
            if(!getControl().isDisposed()) { // check inside run loop
                update(element);
            }
        });
    }
    
    /**
     * Update all of the tree viewer's elements.
     */
    void update() {
        update(null);
    }
    
    /**
     * Update the given element and all of its child elements.
     * @param element The element, or null to update the root.
     */
    void update(Object element) {
        try {
            getControl().setRedraw(false);
            // If element is null use the root element
            element = element == null ? getRoot() : element;
            updateElement(element);
        }
        finally {
            getControl().setRedraw(true);
        }
    }
    
    /**
     * Update element and all of its child elements from the content provider
     */
    private void updateElement(Object element) {
        if(element != null) {
            update(element, null);
            for(Object child : ((ModelTreeViewerContentProvider)getContentProvider()).getChildren(element)) {
                updateElement(child);
            }
        }
    }
    
    /**
     * Refresh the tree and restore expanded tree nodes
     */
    void refreshTreePreservingExpandedNodes() {
        try {
            Object[] expanded = getExpandedElements();
            getControl().setRedraw(false);
            refresh();
            setExpandedElements(expanded);
        }
        finally {
            getControl().setRedraw(true);
        }
    }
    
    /**
     * Finds the widget which represents the given element.
     * @param element the element
     * @return the TreeItem or null
     */
    public TreeItem findTreeItem(Object element) {
        Widget item = findItem(element);
        return (TreeItem)(item instanceof TreeItem ? item : null);
    }
    
    /**
     * Keep a reference to the SearchFilter
     * TODO: filters not supported
     */
    @Override
    public void addFilter(ViewerFilter filter) {
        if(filter instanceof SearchFilter searchFilter) {
            this.searchFilter = searchFilter;
        }
        super.addFilter(filter);
    }
    
    @Override
    public void removeFilter(ViewerFilter filter) {
        // TODO: filters not supported
        if(filter instanceof SearchFilter) {
            this.searchFilter = null;
        }
        super.removeFilter(filter);
    }
    
    @Override
    protected Object[] getSortedChildren(Object parentElementOrTreePath) {
        return ((ModelTreeViewerContentProvider)getContentProvider()).getSortedChildren(parentElementOrTreePath).toArray();
    }
    
    /**
     * If a Concept or a View's parent or ancestor parent folder has a text expression, evaluate it and return it
     * But let's keep a limit to its length
     */
    private String getAncestorFolderRenderText(IArchimateModelObject object) {
        if(object instanceof IArchimateConcept || object instanceof IDiagramModel) {
            String expression = TextRenderer.getDefault().getFormatExpressionFromAncestorFolder(object);
            if(expression != null) {
                String text = StringUtils.normaliseNewLineCharacters(TextRenderer.getDefault().renderWithExpression(object, expression));
                if(StringUtils.isSet(text)) {
                    return text.length() > 256 ? text.substring(0, 256) : text;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get the root expanded elements in case the DrillDownAdapter is active.
     * Note that some of these elements might have been deleted when the tree was drilled into.
     * Returns either the root expanded elements that were visible before the DrillDownAdapter was drilled into,
     * or the current visible expanded elements if the DrillDownAdapter is "Home".
     */
    Object[] getRootVisibleExpandedElements() {
        return rootVisibleExpandedElements != null ? rootVisibleExpandedElements : getVisibleExpandedElements();
    }
    
    // ========================= Model Providers =====================================
    
    /**
     *  Content Provider
     */
    private class ModelTreeViewerContentProvider implements ILazyTreeContentProvider {
        
        @Override
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
            // The DrillDownAdapter sets the new input when calling DrillDownAdapter#goInto().
            // We save the current expanded elements in the root state so we can persist these when we save the expanded tree state
            if(oldInput == IEditorModelManager.INSTANCE && newInput != null) { // Drilldown has moved out of "Home"
                rootVisibleExpandedElements = getVisibleExpandedElements();
            }
            else if(newInput == IEditorModelManager.INSTANCE) { // Drilldown is "Home"
                rootVisibleExpandedElements = null;
            }
        }
        
        @Override
        public void dispose() {
        }
        
        /**
         * TODO - sorting is very slow on large collections, so it can't be done here.
         *        It would have to be done after the model is loaded.
         *        Adding a new element or renaming would require inserting it at the correct position.
         */
        public List<?> getSortedChildren(Object parentElement) {
            List<?> children = getChildren(parentElement);
            children.sort((Object e1, Object e2) -> {
                int cat1 = category(e1);
                int cat2 = category(e2);

                if(cat1 != cat2) {
                    return cat1 - cat2;
                }
                
                // Only user folders are sorted
                if((e1 instanceof IFolder folder1 && e2 instanceof IFolder folder2) && (folder1.getType() != FolderType.USER 
                        || folder2.getType() != FolderType.USER)) {
                    return 0;
                }
                
                // Get rendered text or name
                String label1 = getAncestorFolderRenderText((IArchimateModelObject)e1);
                if(label1 == null) {
                    label1 = StringUtils.safeString(ArchiLabelProvider.INSTANCE.getLabelNormalised(e1));
                }

                // Get rendered text or name
                String label2 = getAncestorFolderRenderText((IArchimateModelObject)e2);
                if(label2 == null) {
                    label2 = StringUtils.safeString(ArchiLabelProvider.INSTANCE.getLabelNormalised(e2));
                }
                
                return label1.compareTo(label2); 
            });
            
            return children;
        }
        
        private int category(Object element) {
            if(element instanceof IFolder) {
                return 0;
            }
            if(element instanceof EObject) {
                return 1;
            }
            return 0;
        }
        
        public List<?> getChildren(Object parentElement) {
            if(parentElement instanceof IEditorModelManager editorModelManager) {
            	return editorModelManager.getModels();
            }
            
            if(parentElement instanceof IArchimateModel model) {
            	return model.getFolders();
            }

            if(parentElement instanceof IFolder folder) {
                List<Object> list = new ArrayList<>();
                
                // Folders
                list.addAll(folder.getFolders());
                // Elements
                list.addAll(folder.getElements());
                
                return list;
            }
            
            return new ArrayList<>();
        }

        @Override
        public Object getParent(Object element) {
            return element instanceof EObject eObject ? eObject.eContainer() : null;
        }

        @Override
        public void updateElement(Object parent, int index) {
            List<?> children = getSortedChildren(parent);
            if(!children.isEmpty()) {
                Object element = children.get(index);
                replace(parent, index, element);
                setChildCount(element, getChildren(element).size());
            }
        }

        @Override
        public void updateChildCount(Object element, int currentChildCount) {
            setChildCount(element, getChildren(element).size());
        }
    }
    
    /**
     * Label Provider
     */
    private class ModelTreeViewerLabelProvider extends CellLabelProvider {
        private Font fontItalic;
        private Font fontBold;
        private Font fontBoldItalic;
        
        @Override
        public void update(ViewerCell cell) {
            Object element = cell.getElement();
            cell.setText(getText(element));
            cell.setImage(getImage(element));
            cell.setForeground(getForeground(element));
            cell.setFont(getFont(element));
        }
        
        private void resetFonts() {
            fontItalic = null;
            fontBold = null;
            fontBoldItalic = null;
            
            // Need to update the tree asynchronously because a theme font change will set the font later
            updateInBackground();
        }

        private String getText(Object element) {
            // If a Concept or a View's parent or ancestor parent folder has a text expression, evaluate it
            String text = getAncestorFolderRenderText((IArchimateModelObject)element);
            if(text != null) {
                return text;
            }
            
            String name = ArchiLabelProvider.INSTANCE.getLabelNormalised(element);
            
            // If a dirty model show asterisk
            if(element instanceof IArchimateModel model && IEditorModelManager.INSTANCE.isModelDirty(model)) {
                name = "*" + name; //$NON-NLS-1$
            }
            // If a relationship show source and target
            else if(element instanceof IArchimateRelationship relationship) {
                name += " ("; //$NON-NLS-1$
                name += ArchiLabelProvider.INSTANCE.getLabelNormalised(relationship.getSource());
                name += " - "; //$NON-NLS-1$
                name += ArchiLabelProvider.INSTANCE.getLabelNormalised(relationship.getTarget());
                name += ")"; //$NON-NLS-1$
            }
            
            return name;
        }
        
        private Image getImage(Object element) {
            return ArchiLabelProvider.INSTANCE.getImage(element);
        }
        
        private Font getFont(Object element) {
            // Using Search
            boolean isSearching = searchFilter != null && searchFilter.isFiltering() && searchFilter.matchesFilter(element);
            
            // Unused concept
            boolean isUnusedConcept = element instanceof IArchimateConcept concept
                    && ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE)
                    && !DiagramModelUtils.isArchimateConceptReferencedInDiagrams(concept);
            
            if(isSearching) {
                return isUnusedConcept ? getBoldItalicFont() : getBoldFont();
            }
            
            return isUnusedConcept ? getItalicFont() : null;
        }
        
        private Font getBoldFont() {
            if(fontBold == null) {
                fontBold = FontFactory.getBold(getTree().getFont());
            }
            return fontBold;
        }
        
        private Font getBoldItalicFont() {
            if(fontBoldItalic == null) {
                fontBoldItalic = FontFactory.getBold(getItalicFont());
            }
            return fontBoldItalic;
        }
        
        private Font getItalicFont() {
            if(fontItalic == null) {
                fontItalic = FontFactory.getItalic(getTree().getFont());
            }
            return fontItalic;
        }

        private Color getForeground(Object element) {
            return viewpointFilterProvider != null ? viewpointFilterProvider.getTextColor(element) : null;
        }
    }
    
}
