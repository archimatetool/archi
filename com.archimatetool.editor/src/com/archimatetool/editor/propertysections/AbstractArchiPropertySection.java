/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.ui.components.GlobalActionDisablementHandler;
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
    
    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
        super.createControls(parent, tabbedPropertySheetPage);
        fPage = tabbedPropertySheetPage;
        setLayout(parent);
        createControls(parent);
        
        // Workaround for Eclipse bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=545239
        // If the CSS element "TabbedPropertyList" is set in a theme's css file then
        // Tabs don't always show skinned color until the focus is gained.
        // This fix forces a reskin.
        // At the moment we don't have this CSS property set in any CSS file (Eclipse itself does, though)
        // ((org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyComposite)fPage.getControl()).getList().reskin(SWT.ALL);
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
     * Update/Refresh the main Properties label
     */
    protected void updatePropertiesLabel() {
        fPage.labelProviderChanged(null);
    }
    
    /**
     * @param parent
     * @return A Single line text Control
     */
    protected Text createSingleTextControl(Composite parent, int style) {
        Text textControl = getWidgetFactory().createText(parent, null, style | SWT.SINGLE);
        
        // Set font from preferences
        UIUtils.setFontFromPreferences(textControl, IPreferenceConstants.SINGLE_LINE_TEXT_FONT, true);
        
        // Single text control so strip CRLFs
        UIUtils.conformSingleTextControl(textControl);
        
        // Filter out any illegal xml characters
        UIUtils.applyInvalidCharacterFilter(textControl);
        
        // Add listener to disable global actions when it gets the focus
        addGlobalActionDisablementListener(textControl);

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
        
        // Add listener to disable global actions when it gets the focus
        addGlobalActionDisablementListener(styledTextControl.getControl());
        
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
        return createComposite(parent, numColumns, false);
    }
    
    /**
     * @param parent
     * @return A Composite
     */
    protected Composite createComposite(Composite parent, int numColumns, boolean makeColumnsEqualWidth) {
        Composite c = new Composite(parent, SWT.NULL);
        getWidgetFactory().adapt(c);
        
        GridLayout layout = new GridLayout(numColumns, makeColumnsEqualWidth);
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
     * @param parent Parent composite
     * @param style Style flags
     * @return Composite control
     */
    protected Composite createTableComposite(Composite parent, int style) {
        Composite tableComp = new Composite(parent, style);
        
        GridData gd = new GridData(GridData.FILL_BOTH);
        //gd.widthHint = 100; // This causes table columns to be squashed on Mac Silicon
        gd.heightHint = 50; // Stop table height creep
        tableComp.setLayoutData(gd);

        tableComp.setLayout(new UpdatingTableColumnLayout(tableComp));
        
        return tableComp;
    }
    
    // =========================== Global Action Disablement ==================================
    // When a text control gets the focus we don't want global action handlers like Undo/Redo
    // to be enabled otherwise the user thinks they are Undoing local text edits and actually
    // they are undoing the last thing on the global Command stack
    
    private class TextControlGlobalActionDisablementListener implements Listener {
        // We have to disable the action handlers of the the active Editor/View site *and* the Properties View Site
        private GlobalActionDisablementHandler propertiesViewGlobalActionHandler, globalActionHandler;
        
        @Override
        public void handleEvent(Event event) {
            switch(event.type) {
                case SWT.FocusIn:
                    event.widget.addListener(SWT.Modify, this);
                    break;
                
                case SWT.Modify:
                    if(propertiesViewGlobalActionHandler != null) { // We already got one...
                        return;
                    }
                    
                    // The Properties View site action bars
                    IActionBars actionBars = fPage.getSite().getActionBars();
                    propertiesViewGlobalActionHandler = new GlobalActionDisablementHandler(actionBars);
                    propertiesViewGlobalActionHandler.clearGlobalActions();
                    
                    // The active View or Editor site's action bars also have to be updated
                    globalActionHandler = new GlobalActionDisablementHandler();
                    globalActionHandler.update();
                    
                    break;

                case SWT.FocusOut:
                    event.widget.removeListener(SWT.Modify, this);
                    // fall through
                case SWT.DefaultSelection:
                    if(propertiesViewGlobalActionHandler != null && globalActionHandler != null) {
                        propertiesViewGlobalActionHandler.restoreGlobalActions();
                        globalActionHandler.update();
                    }
                    
                    propertiesViewGlobalActionHandler = null;
                    globalActionHandler = null;
                    
                    break;
                
                default:
                    break;
            }
        }
    };
    
    protected void addGlobalActionDisablementListener(Control control) {
        final int[] eventTypes = {SWT.FocusIn, SWT.FocusOut, SWT.DefaultSelection};
        
        TextControlGlobalActionDisablementListener listener = new TextControlGlobalActionDisablementListener();
        
        for(int type : eventTypes) {
            control.addListener(type, listener);
        }
        
        control.addDisposeListener((event)-> {
            for(int type : eventTypes) {
                control.removeListener(type, listener);
            }
        });
    }
    
    // Add Mac kludge.
    // Sub-classes that have text controls should implement this
    // and update the text controls that might be affected by the Mac bug
    @Deprecated
    protected void focusGained(Control control) {
    }

}
