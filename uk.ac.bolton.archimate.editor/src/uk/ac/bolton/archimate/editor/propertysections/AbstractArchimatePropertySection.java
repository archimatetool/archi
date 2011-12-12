/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.editor.ui.UIUtils;
import uk.ac.bolton.archimate.editor.ui.components.StyledTextControl;
import uk.ac.bolton.archimate.model.IAdapter;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * Abstract Property Section
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimatePropertySection extends AbstractPropertySection {
    
    /**
     * The TabbedPropertySheetPage
     */
    protected TabbedPropertySheetPage fPage;
    
    private static int V_SPACING = 10;
    
    /**
     * Set this to true when executing command to stop unnecessary refreshing of controls
     */
    protected boolean fIsExecutingCommand;
    
    
    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
        super.createControls(parent, tabbedPropertySheetPage);
        fPage = tabbedPropertySheetPage;
        setLayout(parent);
        createControls(parent);
    }
    
    /**
     * Set the layout for the main parent composite
     * @param parent
     */
    protected void setLayout(Composite parent) {
        GridLayout layout = new GridLayout(2, false);
        layout.marginTop = V_SPACING;
        layout.marginHeight = 0;
        layout.marginBottom = shouldUseExtraSpace() ? 5 : 0; 
        layout.verticalSpacing = V_SPACING;
        parent.setLayout(layout);
        
        parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, shouldUseExtraSpace()));
    }
    
    /**
     * Create the controls 
     * @param parent
     */
    protected abstract void createControls(Composite parent);
    
    /**
     * @return The ECore Adapter to listen to model changes
     */
    protected abstract Adapter getECoreAdapter();
    
    /**
     * @return The EObject that is the model for this Property Section
     */
    protected abstract EObject getEObject();
    
    /**
     * @return The Command Stack in context
     */
    protected CommandStack getCommandStack() {
        if(getEObject() instanceof IAdapter) {
            return (CommandStack)((IAdapter)getEObject()).getAdapter(CommandStack.class);
        }
        return null;
    }
    
    @Override
    public void setInput(IWorkbenchPart part, ISelection selection) {
        // stop double-firing
        if(selection != getSelection()) { 
            // Remove previous EObject from listener adapter
            if(getEObject() != null && getECoreAdapter() != null) {
                getEObject().eAdapters().remove(getECoreAdapter());
            }
            
            // Set the EObject element
            Object element = ((IStructuredSelection)selection).getFirstElement();
            setElement(element);
            
            // Add ECore listener adapter
            if(getEObject() != null && getECoreAdapter() != null && !getEObject().eAdapters().contains(getECoreAdapter())) {
                getEObject().eAdapters().add(getECoreAdapter());
            }
        }
        
        super.setInput(part, selection);
    }
    
    /**
     * Set the Property sheet to the current element
     * @param element
     */
    protected abstract void setElement(Object element);
    
    /**
     * If the Property sheet was Active (or Pinned) and the Element deleted then the Element's
     * info could still be showing.
     * @return True if alive
     */
    protected boolean isAlive() {
        return (getEObject() != null) && 
        	   (getEObject() instanceof IArchimateModel) || (getEObject().eContainer() != null);
    }

    @Override
    public void dispose() {
        if(getEObject() != null && getECoreAdapter() != null) {
            getEObject().eAdapters().remove(getECoreAdapter());
        }
    }
    
    /**
     * Create a Name control
     */
    protected PropertySectionTextControl createNameControl(Composite parent, String hint) {
        // Label
        createCLabel(parent, "Name:", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);

        // Text
        Text textControl = createSingleTextControl(parent, SWT.NONE);
        
        PropertySectionTextControl textName = new PropertySectionTextControl(textControl, IArchimatePackage.Literals.NAMEABLE__NAME) {
            @Override
            protected void textChanged(String oldText, String newText) {
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new EObjectFeatureCommand("Rename" + " " + oldText, getEObject(),
                            IArchimatePackage.Literals.NAMEABLE__NAME, newText));
                    fIsExecutingCommand = false;
                }
            }
        };
        textName.setHint(hint);

        return textName;
    }

    /**
     * Create a Documentation control
     */
    protected PropertySectionTextControl createDocumentationControl(Composite parent, String hint) {
        // Label
        createCLabel(parent, "Documentation:", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.TOP);
        
        // Text
        StyledTextControl styledTextControl = createStyledTextControl(parent, SWT.NONE);
        
        PropertySectionTextControl textDoc = new PropertySectionTextControl(styledTextControl.getControl(), IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION) {
            @Override
            protected void textChanged(String oldText, String newText) {
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new EObjectFeatureCommand("Change Documentation", getEObject(),
                            IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION, newText));
                    fIsExecutingCommand = false;
                }
            }
        };
        textDoc.setHint(hint);
        
        return textDoc;
    }

    /**
     * @param parent
     * @return A Single line text Control
     */
    protected Text createSingleTextControl(Composite parent, int style) {
        Text textControl = getWidgetFactory().createText(parent, null, style | SWT.SINGLE);
        // Single text control so strip CRLFs
        UIUtils.conformSingleTextControl(textControl);
        
        GridData gd = new GridData(SWT.FILL, SWT.NULL, true, false);
        // This stops excess size if the control contains a lot of text
        gd.widthHint = 100;
        textControl.setLayoutData(gd);
        return textControl;
    }
    
    /**
     * @param parent
     * @return A StyledTextControl
     */
    protected StyledTextControl createStyledTextControl(Composite parent, int style) {
        StyledTextControl styledTextControl = new StyledTextControl(parent, style | SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
        //Text textControl = getWidgetFactory().createText(parent, null, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        // This stops excess size if the control contains a lot of text
        gd.widthHint = 100;
        gd.heightHint = 100;
        styledTextControl.getControl().setLayoutData(gd);
        return styledTextControl;
    }
    
    /**
     * @param parent
     * @return A Composite
     */
    protected Composite createComposite(Composite parent, int numColumns) {
        Composite c = new Composite(parent, SWT.NULL);
        getWidgetFactory().adapt(c);
        
        GridLayout layout = new GridLayout(numColumns, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = V_SPACING;
        c.setLayout(layout);
        c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        return c;
    }
    
    /**
     * @param parent
     * @param text
     * @param width
     * @param v_position
     * @return
     */
    protected CLabel createCLabel(Composite parent, String text, int width, int v_position) {
        CLabel label = getWidgetFactory().createCLabel(parent, text);
        GridData gd = new GridData(SWT.NONE, v_position, false, false);
        gd.widthHint = width;
        label.setLayoutData(gd);
        return label;
    }
    
    /**
     * Create Composite control to hold a Table control with UpdatingTableColumnLayout
     * @param parent
     * @param style
     * @return Composite control
     */
    protected Composite createTableComposite(Composite parent, int style) {
        Composite tableComp = new Composite(parent, style);
        
        // This ensures a minumum and equal size and no horizontal size creep
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 100;
        gd.heightHint = 50;
        tableComp.setLayoutData(gd);

        tableComp.setLayout(new UpdatingTableColumnLayout(tableComp));
        
        return tableComp;
    }
    
    /**
     * TableColumnLayout with public method so we can re-layout when the host table adds/removes vertical scroll bar
     * It's a kludge to stop a bogus horizontal scroll bar being shown.
     */
    static class UpdatingTableColumnLayout extends TableColumnLayout {
        private Composite fParent;

        public UpdatingTableColumnLayout(Composite parent) {
            fParent = parent;
        }

        public void doRelayout() {
            layout(fParent, true);
        }
    }
}
