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
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.ui.components.StyledTextControl;
import com.archimatetool.editor.ui.components.UpdatingTableColumnLayout;
import com.archimatetool.editor.utils.PlatformUtils;



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
    
    @SuppressWarnings("restriction")
    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
        super.createControls(parent, tabbedPropertySheetPage);
        fPage = tabbedPropertySheetPage;
        setLayout(parent);
        createControls(parent);
        
        // TODO: Workaround for Eclipse bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=545239
        // Tabs don't always show skinned color until the focus is gained.
        // This fix forces a reskin
        // At the moment we only have this CSS property set for Mac
        // This and the CSS addition can be removed when using Eclipse 4.10+
        if(PlatformUtils.isMac()) {
            ((org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyComposite)fPage.getControl()).getList().reskin(SWT.ALL);
        }
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
        
        // Add Focus Listener for Mac Kludge
        addFocusListener(textControl);

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
        
        // On Mac Group is filled with grey and label is white
        if(parent instanceof Group && PlatformUtils.isMac()) {
            label.setBackground(null);
        }
        
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
    
    // The trick here is to set the content of the text control again when it gains the focus
    
    protected void addFocusListener(Control control) {
        // This fix applies only on Mac OS systems
        if(!Platform.getOS().equals(Platform.OS_MACOSX)) {
            return;
        }

        if(!control.isDisposed()) {
            FocusAdapter listener = new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    AbstractArchiPropertySection.this.focusGained(control);
                }
            };
            
            control.addFocusListener(listener);
            
            control.addDisposeListener(new DisposeListener() {
                @Override
                public void widgetDisposed(DisposeEvent e) {
                    control.removeFocusListener(listener);
                }
            });
        }
    }
    
    // Add Mac kludge.
    // Sub-classes that have text controls should implement this
    // and update the text controls that might be affected by the Mac bug
    protected void focusGained(Control control) {
    }

}
