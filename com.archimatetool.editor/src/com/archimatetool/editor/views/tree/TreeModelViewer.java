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
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.FontFactory;
import com.archimatetool.editor.ui.UIUtils;
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
    private TreeViewpointFilterProvider fViewpointFilterProvider;
    
    /**
     * Application Preferences Listener
     */
    private IPropertyChangeListener prefsListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            switch(event.getProperty()) {
                case IPreferenceConstants.HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE:
                    refresh();
                    break;

                case IPreferenceConstants.MODEL_TREE_FONT:
                    ((ModelTreeViewerLabelProvider)getLabelProvider()).resetFonts();
                    refresh();
                    break;
            }
        }
    };

    public TreeModelViewer(Composite parent, int style) {
        super(parent, style | SWT.MULTI);
        
        // Font
        UIUtils.setFontFromPreferences(getTree(), IPreferenceConstants.MODEL_TREE_FONT, true);

        // Mac Silicon Item height
        UIUtils.fixMacSiliconItemHeight(getTree());
        
        setContentProvider(new ModelTreeViewerContentProvider());
        setLabelProvider(new ModelTreeViewerLabelProvider());
        
        setUseHashlookup(true);
        
        // Sort
        setComparator(new ViewerComparator() {
            @Override
            public int compare(Viewer viewer, Object e1, Object e2) {
                int cat1 = category(e1);
                int cat2 = category(e2);

                if(cat1 != cat2) {
                    return cat1 - cat2;
                }
                
                // Only user folders are sorted
                if((e1 instanceof IFolder && e2 instanceof IFolder) && (((IFolder)e1).getType() != FolderType.USER 
                        || ((IFolder)e2).getType() != FolderType.USER)) {
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
                
                //return getComparator().compare(label1, label2);
                return label1.compareToIgnoreCase(label2);
            }
            
            @Override
            public int category(Object element) {
                if(element instanceof IFolder) {
                    return 0;
                }
                if(element instanceof EObject) {
                    return 1;
                }
                return 0;
            }
        });
        
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
        
        setCellEditors(new CellEditor[]{ cellEditor });
        
        setCellModifier(new ICellModifier() {
            @Override
            public void modify(Object element, String property, Object value) {
                if(element instanceof TreeItem) {
                    Object data = ((TreeItem)element).getData();
                    if(data instanceof INameable) {
                        RenameCommandHandler.doRenameCommand((INameable)data, value.toString());
                    }
                }
            }
            
            @Override
            public Object getValue(Object element, String property) {
                if(element instanceof INameable) {
                    return ((INameable)element).getName();
                }
                return null;
            }
            
            @Override
            public boolean canModify(Object element, String property) {
                return RenameCommandHandler.canRename(element);
            }
        });
        
        // Filter
        fViewpointFilterProvider = new TreeViewpointFilterProvider(this);
        
        // Listen to Preferences
        ArchiPlugin.PREFERENCES.addPropertyChangeListener(prefsListener);
        
        getTree().addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                ArchiPlugin.PREFERENCES.removePropertyChangeListener(prefsListener);
                fViewpointFilterProvider = null;
            }
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
     * Refresh the tree in the background
     * @param element The root element or null for the whole tree
     */
    void refreshInBackground(final Object element) {
        getControl().getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                if(!getControl().isDisposed()) { // check inside run loop
                    try {
                        getControl().setRedraw(false);
                        // If element is not visible in case of drill-down then refresh the whole tree
                        refresh(element != null ? (findItem(element) != null ? element : null) : null);
                    }
                    finally {
                        getControl().setRedraw(true);
                    }
                }
            }
        });
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
     * @return The Search Filter or null if not filtering
     */
    protected SearchFilter getSearchFilter() {
        for(ViewerFilter filter : getFilters()) {
            if(filter instanceof SearchFilter) {
                return (SearchFilter)filter;
            }
        }
        
        return null;
    }
    
    // Need package access to this method
    @Override
    protected Object[] getSortedChildren(Object parentElementOrTreePath) {
        return super.getSortedChildren(parentElementOrTreePath);
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
    
    // ========================= Model Providers =====================================
    
    /**
     *  Content Provider
     */
    private class ModelTreeViewerContentProvider implements ITreeContentProvider {
        
        @Override
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        @Override
        public void dispose() {
        }
        
        @Override
        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }

        @Override
        public Object[] getChildren(Object parentElement) {
            if(parentElement instanceof IEditorModelManager) {
            	return ((IEditorModelManager)parentElement).getModels().toArray();
            }
            
            if(parentElement instanceof IArchimateModel) {
            	return ((IArchimateModel)parentElement).getFolders().toArray();
            }

            if(parentElement instanceof IFolder) {
                List<Object> list = new ArrayList<Object>();
                
                // Folders
                list.addAll(((IFolder)parentElement).getFolders());
                // Elements
                list.addAll(((IFolder)parentElement).getElements());
                
                return list.toArray();
            }
            
            return new Object[0];
        }

        @Override
        public Object getParent(Object element) {
            if(element instanceof EObject) {
                return ((EObject)element).eContainer();
            }
            return null;
        }

        @Override
        public boolean hasChildren(Object element) {
        	return getFilteredChildren(element).length > 0;
        }
    }
    
    /**
     * Label Provider
     */
    private class ModelTreeViewerLabelProvider extends CellLabelProvider {
        private Font fontItalic;
        private Font fontBold;
        
        @Override
        public void update(ViewerCell cell) {
            cell.setText(getText(cell.getElement()));
            cell.setImage(getImage(cell.getElement()));
            cell.setForeground(getForeground(cell.getElement()));
            cell.setFont(getFont(cell.getElement()));
        }
        
        private void resetFonts() {
            fontItalic = null;
            fontBold = null;
        }

        private String getText(Object element) {
            // If a Concept or a View's parent or ancestor parent folder has a text expression, evaluate it
            String text = getAncestorFolderRenderText((IArchimateModelObject)element);
            if(text != null) {
                return text;
            }
            
            String name = ArchiLabelProvider.INSTANCE.getLabelNormalised(element);
            
            // If a dirty model show asterisk
            if(element instanceof IArchimateModel) {
                IArchimateModel model = (IArchimateModel)element;
                if(IEditorModelManager.INSTANCE.isModelDirty(model)) {
                    name = "*" + name; //$NON-NLS-1$
                }
            }
            
            if(element instanceof IArchimateRelationship) {
                IArchimateRelationship relationship = (IArchimateRelationship)element;
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
            // Show bold if using Search
            SearchFilter filter = getSearchFilter();
            if(filter != null && filter.isFiltering() && filter.matchesFilter(element)) {
                return getBoldFont();
            }
            
            // Italicise unused elements
            if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE) && element instanceof IArchimateConcept) {
                if(!DiagramModelUtils.isArchimateConceptReferencedInDiagrams((IArchimateConcept)element)) {
                    return getItalicFont();
                }
            }
            
            return null;
        }
        
        private Font getBoldFont() {
            if(fontBold == null) {
                fontBold = FontFactory.getBold(getTree().getFont());
            }
            return fontBold;
        }
        
        private Font getItalicFont() {
            if(fontItalic == null) {
                // Because of timing issues we have to get the italic font from user prefs not from the tree
                String fontDetails = ArchiPlugin.PREFERENCES.getString(IPreferenceConstants.MODEL_TREE_FONT);
                if(StringUtils.isSet(fontDetails)) {
                    Font font = FontFactory.get(fontDetails);
                    fontItalic = FontFactory.getItalic(font);
                }
                else {
                    fontItalic = FontFactory.getItalic(getTree().getFont());
                }
            }
            return fontItalic;
        }

        private Color getForeground(Object element) {
            return fViewpointFilterProvider.getTextColor(element);
        }
    }
    
}
