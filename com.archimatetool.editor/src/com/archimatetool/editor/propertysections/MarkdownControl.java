/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import java.util.Objects;
import java.util.function.Function;

import org.eclipse.draw2d.GridData;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.ThemeUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.markdown.MarkdownUtils;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDocumentable;

/**
 * Control for editing and displaying Markdown
 * 
 * @author Phillip Beauvoir
 */
public class MarkdownControl {
    
    private static final int EDIT = 0;
    private static final int VIEW = 1;
    
    private int mode;
    
    private Composite stack;
    private StackLayout stackLayout;
    
    private PropertySectionTextControl textControl;
    private AbstractECorePropertySection section;
    private Browser browser;
    private Label browserErrorLabel;
    private IAction modeAction;
    private IPropertyChangeListener prefsListener;
    
    // If this is true back or forward was triggered
    private boolean isNavigating;
    
    /**
     * @param parent Parent composite
     * @param section Parent Section
     */
    public MarkdownControl(Composite parent, AbstractECorePropertySection section) {
        this.section = section;
        
        parent.addDisposeListener(e -> {
            dispose();
        });
        
        IPreferenceStore prefs = ArchiPlugin.getInstance().getPreferenceStore();
        
        Composite client = section.getWidgetFactory().createComposite(parent, SWT.NONE);
        GridDataFactory.create(GridData.FILL_BOTH).applyTo(client);
        GridLayoutFactory.swtDefaults().numColumns(2).margins(0, 0).applyTo(client);
        
        stack = section.getWidgetFactory().createComposite(client, SWT.BORDER);
        stackLayout = new StackLayout();
        stack.setLayout(stackLayout);
        GridDataFactory.create(GridData.FILL_BOTH).hint(100, 100).applyTo(stack); // stops excess size if the control contains a lot of text
        
        modeAction = new Action(null, SWT.NONE) {
            @Override
            public void run() {
                // Just set preference, control will be updated in prefsListener
                prefs.setValue(IPreferenceConstants.MARKDOWN_MODE, mode ^ 1);
            };
        };
        
        // modeAction's ImageDescriptor has to be set *before* creating the toolbar on Windows
        // else the toolbar item is too small
        if(prefs.getInt(IPreferenceConstants.MARKDOWN_MODE) == VIEW) {
            setBrowserControl();
        }
        else {
            setTextControl(); // Set ImageDescriptor even if no current text control
        }
        
        ToolBarManager toolbarManager = new ToolBarManager(SWT.FLAT | SWT.VERTICAL | SWT.NO_FOCUS);
        toolbarManager.add(modeAction);
        
        ToolBar toolBar = toolbarManager.createControl(client);
        section.getWidgetFactory().adapt(toolBar);
        GridDataFactory.fillDefaults().align(SWT.END, SWT.TOP).applyTo(toolBar);
        
        // All instances listen to prefs so each can update their state when this or another instance changes state
        prefsListener = event -> {
            if(IPreferenceConstants.MARKDOWN_MODE.equals(event.getProperty())) {
                switch((int)event.getNewValue()) {
                    case EDIT -> {
                        setTextControl();
                    }
                    case VIEW -> {
                        setBrowserControl();
                    }
                }
            }
        };
        
        prefs.addPropertyChangeListener(prefsListener);
    }
    
    /**
     * Set the PropertySectionTextControl with parent control
     * @param controlFactory the factory to create the control. Composite is the parent control.
     */
    public void setPropertySectionTextControl(Function<Composite, PropertySectionTextControl> controlFactory) {
        Objects.requireNonNull(controlFactory);
        textControl = Objects.requireNonNull(controlFactory.apply(stack));
        if(mode == EDIT) {
            setTextControl();
        }
    }
    
    public void update() {
        if(textControl != null) {
            textControl.refresh(section.getFirstSelectedObject());
            textControl.setEditable(!section.isLocked(section.getFirstSelectedObject()));
        }

        if(mode == VIEW) {
            updatePreview();
        }
    }
    
    private void setTextControl() {
        mode = EDIT;
        modeAction.setToolTipText(Messages.MarkdownControl_0);
        modeAction.setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_PREVIEW));
        if(textControl != null) {
            setCurrentControl(textControl.getTextControl());
        }
    }
    
    private void setBrowserControl() {
        mode = VIEW;
        modeAction.setToolTipText(Messages.MarkdownControl_1);
        modeAction.setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_EDIT));
        setCurrentControl(getBrowserControl()); // This first so the text control loses focus and commits any changes
        updatePreview();
    }
    
    private void setCurrentControl(Control control) {
        if(control != null && stackLayout.topControl != control) {
            stackLayout.topControl = control;
            stack.layout();
            control.setFocus();
        }
    }
    
    private void updatePreview() {
        String text = ""; //$NON-NLS-1$
        
        if(section.getFirstSelectedObject() instanceof IDocumentable documentable) {
            text = documentable.getDocumentation();
        }
        else if(section.getFirstSelectedObject() instanceof IArchimateModel model) {
            text = model.getPurpose();
        }
        
        if(getBrowserControl() instanceof Browser browser) {
            String html = MarkdownUtils.convertMarkdownToFullHtml(StringUtils.safeString(text), ThemeUtils.isDarkTheme());
            browser.setText(html);
        }
    }
    
    private Control getBrowserControl() {
        if(browser == null) {
            try {
                browser = new Browser(stack, SWT.NONE);
            }
            catch(SWTError error) {
                if(browserErrorLabel == null) {
                    browserErrorLabel = section.getWidgetFactory().createLabel(stack, Messages.MarkdownControl_2 + " " + error.getMessage()); //$NON-NLS-1$
                    browserErrorLabel.setForeground(new Color(255, 45, 45));
                }
                return browserErrorLabel;
            }
            
            browser.addLocationListener(LocationListener.changedAdapter(event -> {
                if(isNavigating && isBrowserHome(browser.getUrl())) {
                    isNavigating = false;
                    updatePreview();
                }
            }));
            
            MenuManager menuManager = new MenuManager();
            menuManager.setRemoveAllWhenShown(true);
            
            menuManager.addMenuListener(manager -> {
                manager.add(new Action(isBrowserHome(browser.getUrl()) ? Messages.MarkdownControl_3 : Messages.MarkdownControl_4) {
                    @Override
                    public void run() {
                        updatePreview();
                    }
                });
                
                manager.add(new Separator());
                
                if(browser.isBackEnabled()) {
                    manager.add(new Action(Messages.MarkdownControl_5) {
                        @Override
                        public void run() {
                            isNavigating = true;
                            browser.back();
                        }
                    });
                }
                
                if(browser.isForwardEnabled()) {
                    manager.add(new Action(Messages.MarkdownControl_6) {
                        @Override
                        public void run() {
                            isNavigating = true;
                            browser.forward();
                        }
                    });
                }
            });
            
            browser.setMenu(menuManager.createContextMenu(browser));
        }
        
        return browser;
    }
    
    private boolean isBrowserHome(String location) {
        return Objects.equals("about:blank", location); //$NON-NLS-1$
    }
    
    private void dispose() {
        ArchiPlugin.getInstance().getPreferenceStore().removePropertyChangeListener(prefsListener);
        prefsListener = null;
        stack = null;
        stackLayout = null;
        textControl = null;
        browser = null;
        section = null;
    }
}
