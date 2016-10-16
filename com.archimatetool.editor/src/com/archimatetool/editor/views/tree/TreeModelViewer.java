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
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
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

import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.FontFactory;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.editor.views.tree.search.SearchFilter;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IFolder;




/**
 * Tree Viewer for Model Tree View
 * 
 * @author Phillip Beauvoir
 */
public class TreeModelViewer extends TreeViewer {
    
    private TreeCellEditor fCellEditor;
    
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
        fCellEditor = new TreeCellEditor(getTree());
        
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
        TreeItem item = findTreeItem(element);
        if(item != null) {
            fCellEditor.editItem(item);
        }
    }
    
    @Override
    public void refresh(Object element) {
        if(isEditing()) {
            fCellEditor.cancelEditing();
        }
        super.refresh(element);
    }
    
    @Override
    public void refresh(Object element, boolean updateLabels) {
        if(isEditing()) {
            fCellEditor.cancelEditing();
        }
        super.refresh(element, updateLabels);
    }
    
    boolean isEditing() {
        return fCellEditor != null && fCellEditor.isEditing();
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
    
    // ========================= Model Provoders =====================================
    
    /**
     *  Content Provider
     */
    class ModelTreeViewerContentProvider implements ITreeContentProvider {
        
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
    class ModelTreeViewerLabelProvider extends LabelProvider implements IFontProvider, IColorProvider {
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
        public String getText(Object element) {
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
        
        @Override
        public Image getImage(Object element) {
            return ArchiLabelProvider.INSTANCE.getImage(element);
        }
        
        @Override
        public Font getFont(Object element) {
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

        @Override
        public Color getForeground(Object element) {
            return fViewpointFilterProvider.getTextColor(element);
        }

        @Override
        public Color getBackground(Object element) {
            return null;
        }
    }
}
