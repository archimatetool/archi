/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.ui.components.StyledTextControl;
import com.archimatetool.editor.ui.components.UpdatingTableColumnLayout;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;



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
    
    /*
     * Used for Mac bug - see https://bugs.eclipse.org/bugs/show_bug.cgi?id=383750 
     */
    protected Text fHiddenText;
    
    /**
     * Object Filter class to show or reject this section depending on input value
     */
    public static abstract class ObjectFilter implements IFilter {
        @Override
        public boolean select(Object object) {
            return adaptObject(object) != null;
        }
        
        /**
         * Get the required object for this Property Section from the given object
         * @param object
         * @return The required object or null
         */
        public Object adaptObject(Object object) {
            if(isRequiredType(object)) {
                return object;
            }
            
            if(object instanceof IAdaptable) {
                object = ((IAdaptable)object).getAdapter(getAdaptableType());
                return isRequiredType(object) ? object : null;
            }
            
            return null;
        }
        
        /**
         * @return True if the feature should be exposed on the object
         */
        public boolean shouldExposeFeature(EObject eObject, EAttribute feature) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(eObject);
            
            if(provider != null) {
                return provider.shouldExposeFeature(feature);
            }
            
            return true;
        }
        
        protected abstract boolean isRequiredType(Object object);
        protected abstract Class<?> getAdaptableType();
    }
    
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
        layout.marginLeft = 3;
        layout.marginBottom = 2; 
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
        Label label = createLabel(parent, Messages.AbstractArchimatePropertySection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        // CSS
        label.setData("org.eclipse.e4.ui.css.CssClassName", "PropertiesNameLabel"); //$NON-NLS-1$ //$NON-NLS-2$

        // Text
        Text textControl = createSingleTextControl(parent, SWT.NONE);
        
        // CSS
        textControl.setData("org.eclipse.e4.ui.css.CssClassName", "PropertiesNameText"); //$NON-NLS-1$ //$NON-NLS-2$

        PropertySectionTextControl textName = new PropertySectionTextControl(textControl, IArchimatePackage.Literals.NAMEABLE__NAME) {
            @Override
            protected void textChanged(String oldText, String newText) {
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new EObjectFeatureCommand(Messages.AbstractArchimatePropertySection_1 + " " + oldText, getEObject(), //$NON-NLS-1$
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
        Label label = createLabel(parent, Messages.AbstractArchimatePropertySection_2, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        // CSS
        label.setData("org.eclipse.e4.ui.css.CssClassName", "PropertiesDocumentationLabel"); //$NON-NLS-1$ //$NON-NLS-2$
        
        // Text
        StyledTextControl styledTextControl = createStyledTextControl(parent, SWT.NONE);
        
        // CSS
        styledTextControl.getControl().setData("org.eclipse.e4.ui.css.CssClassName", "PropertiesDocumentationText"); //$NON-NLS-1$ //$NON-NLS-2$
        
        PropertySectionTextControl textDoc = new PropertySectionTextControl(styledTextControl.getControl(), IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION) {
            @Override
            protected void textChanged(String oldText, String newText) {
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new EObjectFeatureCommand(Messages.AbstractArchimatePropertySection_3, getEObject(),
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
        
        // Filter out any illegal xml characters
        UIUtils.applyInvalidCharacterFilter(textControl);

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
        c.setLayoutData(new GridData());
        
        return c;
    }
    
    /**
     * Create Label control. Style is set to SWT.WRAP
     * @param parent Parent composite
     * @param text Text to display
     * @param width Width of label in pixels
     * @param v_position Vertical position. Should be SWT.CENTER or SWT.NONE
     * @return
     */
    protected Label createLabel(Composite parent, String text, int width, int verticalPosition) {
        Label label = getWidgetFactory().createLabel(parent, text, SWT.WRAP);
        GridData gd = new GridData(SWT.NONE, verticalPosition, false, false);
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
     * Add hidden text field to section on a Mac because of a bug.
     * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=383750
     * @param parent
     */
    protected void addHiddenTextFieldToForm(Composite parent) {
        // This fix applies only on Mac OS systems
        if(!Platform.getOS().equals(Platform.OS_MACOSX)) {
            return;
        }

        // The grid data used to reduce space of the fake Text field
        final GridData hiddenData = new GridData(0, 0);
        // It takes 2 columns spaces in the table
        hiddenData.horizontalSpan = 2;

        // The fake Text field
        fHiddenText = new Text(parent, SWT.READ_ONLY);
        fHiddenText.setLayoutData(hiddenData);

        // Here is the trick. To hide the fake Text field, we change top margin
        // value to move the content up, and bottom margin to prevent from
        // cropping the end of the table content. This is very bad, but it works.
        ((GridLayout)parent.getLayout()).marginHeight = -5; // default was 5
        ((GridLayout)parent.getLayout()).marginBottom = 10; // default was 0
    }
    
    @Override
    public void refresh() {
        // Workaround for bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=383750
        if(fHiddenText != null && !fHiddenText.isDisposed()) {
            fHiddenText.setFocus();
        }
    }

}
