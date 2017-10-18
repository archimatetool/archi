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
import org.eclipse.jface.viewers.TextCellEditor;
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
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.FontFactory;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.ui.components.CellEditorGlobalActionHandler;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.editor.views.tree.commands.RenameCommandHandler;
import com.archimatetool.editor.views.tree.search.SearchFilter;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
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
    
    private TextCellEditor fCellEditor;
    
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
            if(IPreferenceConstants.HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE.equals(event.getProperty())) {
                refresh();
            }
        }
    };

    public TreeModelViewer(Composite parent, int style) {
        super(parent, style | SWT.MULTI);
        
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
                
                String name1 = ArchiLabelProvider.INSTANCE.getLabel(e1);
                String name2 = ArchiLabelProvider.INSTANCE.getLabel(e2);
                
                if(name1 == null) {
                    name1 = "";//$NON-NLS-1$
                }
                if(name2 == null) {
                    name2 = "";//$NON-NLS-1$
                }
                
                return getComparator().compare(name1, name2);
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
        fCellEditor = new TreeTextCellEditor(getTree());
        setColumnProperties(new String[]{ "col1" }); //$NON-NLS-1$
        setCellEditors(new CellEditor[]{ fCellEditor });
        
        // Edit cell programmatically, not on mouse click
        TreeViewerEditor.create(this, new ColumnViewerEditorActivationStrategy(this){
            @Override
            protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
                return event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
            }  
            
        }, ColumnViewerEditor.DEFAULT);
        
        setCellEditors(new CellEditor[]{ fCellEditor });
        
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
        Preferences.STORE.addPropertyChangeListener(prefsListener);
        
        getTree().addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                Preferences.STORE.removePropertyChangeListener(prefsListener);
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
    
    /**
     * Refresh the tree in the background
     * @param element The root element or null for the whole tree
     */
    void refreshInBackground(final Object element) {
        getControl().getDisplay().asyncExec(new Runnable() {
            public void run() {
                if(!getControl().isDisposed()) { // check inside run loop
                    try {
                        getControl().setRedraw(false);
                        refresh(element);
                    }
                    finally {
                        getControl().setRedraw(true);
                    }
                }
            }
        });
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
    
    // ========================= Model Providers =====================================
    
    /**
     *  Content Provider
     */
    private class ModelTreeViewerContentProvider implements ITreeContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }

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

        public Object getParent(Object element) {
            if(element instanceof EObject) {
                return ((EObject)element).eContainer();
            }
            return null;
        }

        public boolean hasChildren(Object element) {
        	return getFilteredChildren(element).length > 0;
        }
    }
    
    /**
     * Label Provider
     */
    private class ModelTreeViewerLabelProvider extends CellLabelProvider {
        Font fontNormal = null;
        Font fontItalic = FontFactory.SystemFontItalic;
        Font fontBold = FontFactory.SystemFontBold;
        
        ModelTreeViewerLabelProvider() {
            // Mac font issues
            if(PlatformUtils.isMac()) {
                fontNormal = FontFactory.getMacAlternateFont(getTree().getFont());
                fontItalic = FontFactory.getMacAlternateFont(fontItalic);
                fontBold = FontFactory.getMacAlternateFont(fontBold);
            }
        }
        
        @Override
        public void update(ViewerCell cell) {
            cell.setText(getText(cell.getElement()));
            cell.setImage(getImage(cell.getElement()));
            cell.setForeground(getForeground(cell.getElement()));
            cell.setFont(getFont(cell.getElement()));
        }
        
        String getText(Object element) {
            String name = ArchiLabelProvider.INSTANCE.getLabel(element);
            
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
                name += ArchiLabelProvider.INSTANCE.getLabel(relationship.getSource());
                name += " - "; //$NON-NLS-1$
                name += ArchiLabelProvider.INSTANCE.getLabel(relationship.getTarget());
                name += ")"; //$NON-NLS-1$
            }
            
            return name;
        }
        
        Image getImage(Object element) {
            return ArchiLabelProvider.INSTANCE.getImage(element);
        }
        
        Font getFont(Object element) {
            SearchFilter filter = getSearchFilter();
            if(filter != null && filter.isFiltering() && filter.matchesFilter(element)) {
                return fontBold;
            }
            
            // Italicise unused elements
            if(Preferences.STORE.getBoolean(IPreferenceConstants.HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE) && element instanceof IArchimateConcept) {
                if(!DiagramModelUtils.isArchimateConceptReferencedInDiagrams((IArchimateConcept)element)) {
                    return fontItalic;
                }
            }
            
            return fontNormal;
        }

        Color getForeground(Object element) {
            return fViewpointFilterProvider.getTextColor(element);
        }
    }
    
    // ========================= Text Cell Editor =====================================
    
    private class TreeTextCellEditor extends TextCellEditor {
        int minHeight = 0;
        CellEditorGlobalActionHandler fGlobalActionHandler;

        public TreeTextCellEditor(Tree tree) {
            super(tree, SWT.BORDER);
            Text txt = (Text)getControl();
            
            // Filter out nasties
            UIUtils.applyInvalidCharacterFilter(txt);
            
            // Not sure if we need this
            //UIUtils.conformSingleTextControl(txt);

            FontData[] fontData = txt.getFont().getFontData();
            if(fontData != null && fontData.length > 0) {
                minHeight = fontData[0].getHeight() + 10;
            }
        }

        @Override
        public LayoutData getLayoutData() {
            LayoutData data = super.getLayoutData();
            if(minHeight > 0) {
                data.minimumHeight = minHeight;
            }
            return data;
        }
        
        @Override
        public void activate() {
            // Clear global key binds
            fGlobalActionHandler = new CellEditorGlobalActionHandler();
            fGlobalActionHandler.clearGlobalActions();
        }
        
        @Override
        public void deactivate() {
            super.deactivate();
            
            // Restore global key binds
            if(fGlobalActionHandler != null) {
                fGlobalActionHandler.restoreGlobalActions();
                fGlobalActionHandler = null;
            }
        }
    }
}
