/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.core.runtime.Platform;
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

import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.ui.components.StyledTextControl;
import com.archimatetool.editor.ui.components.UpdatingTableColumnLayout;



/**
 * Abstract Archi Property Section
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchiPropertySection extends AbstractPropertySection {
    
    /**
     * The TabbedPropertySheetPage
     */
    protected TabbedPropertySheetPage fPage;
    
    protected static int V_SPACING = 10;
    
    @Override
    public void setInput(IWorkbenchPart part, ISelection selection) {
        handleSelection((IStructuredSelection)selection);
        super.setInput(part, selection);
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
     * Handle the current selection
     * @param selection
     */
    protected abstract void handleSelection(IStructuredSelection selection);
    
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
    
    // ========================== Mac workaround ==========================
    // Used for Mac bug - see https://bugs.eclipse.org/bugs/show_bug.cgi?id=383750 
    
    protected Text fHiddenText;
    
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
