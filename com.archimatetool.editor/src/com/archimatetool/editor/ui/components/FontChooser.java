/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.components;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.archimatetool.editor.preferences.FontsPreferencePage;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.FontFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.model.IFontAttribute;




/**
 * Font Chooser
 * 
 * @author Phillip Beauvoir
 */
public class FontChooser extends EventManager {

    public static final String PROP_FONTCHANGE = "fontValue"; //$NON-NLS-1$
    
    public static final String PROP_FONTDEFAULT = "fontDefault"; //$NON-NLS-1$

    private Composite fComposite;
    private Button fTextButton;
    private Button fMenuButton;
    
    protected boolean fDoShowDefaultMenuItem = true;
    protected boolean fDoShowPreferencesMenuItem = true;
    
    private List<IAction> fExtraActionsList = new ArrayList<IAction>();

    private boolean fIsDefaultFont;

    private IFontAttribute fFontObject;
    
    private FontData fFontData;
    private RGB fFontRGB;
    
    public FontChooser(Composite parent, FormToolkit toolkit) {
        this(parent);
        toolkit.adapt(fTextButton, true, true);
        toolkit.adapt(fMenuButton, true, true);
    }

    public FontChooser(Composite parent) {
        fComposite = new Composite(parent, SWT.NULL);
        fComposite.setBackgroundMode(SWT.INHERIT_FORCE);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = 0;
        fComposite.setLayout(layout);

        fTextButton = new Button(fComposite, SWT.FLAT);
        // Ensure button has initial height, especially on MacOS Big Sur
        fTextButton.setText("Select"); //$NON-NLS-1$
        
        fTextButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                chooseFont();
            }
        });
        
        GridDataFactory.create(SWT.NONE).hint(90, SWT.DEFAULT).applyTo(fTextButton);
        
        fComposite.getAccessible().addAccessibleListener(new AccessibleAdapter() {
            @Override
            public void getName(AccessibleEvent e) {
                e.result = "Font Chooser"; //$NON-NLS-1$
            }
        });
        
        fMenuButton = new Button(fComposite, SWT.FLAT);
        fMenuButton.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        fMenuButton.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.MENU_ARROW));

        fMenuButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                showMenu();
            }
        });
    }
    
    public void setFontObject(IFontAttribute fontObject) {
        fFontObject = fontObject;
        updateFont();
    }
    
    public void setIsDefaultFont(boolean set) {
        fIsDefaultFont = set;
    }
    
    public boolean isDefaultFont() {
        return fIsDefaultFont;
    }
    
    public void setDoShowPreferencesMenuItem(boolean set) {
        fDoShowPreferencesMenuItem = set;
    }
    
    public void setDoShowDefaultMenuItem(boolean set) {
        fDoShowDefaultMenuItem = set;
    }
    
    public RGB getFontRGB() {
        return fFontRGB;
    }
    
    public FontData getFontData() {
        return fFontData;
    }
    
    public Control getControl() {
        return fComposite;
    }
    
    public void showMenu() {
        MenuManager menuManager = new MenuManager();
        addMenuActions(menuManager);
        
        Menu menu = menuManager.createContextMenu(fMenuButton.getShell());
        Point p = fMenuButton.getParent().toDisplay(fMenuButton.getBounds().x, fMenuButton.getBounds().y);
        menu.setLocation(p);
        menu.setVisible(true);
    }
    
    public void addMenuAction(IAction action) {
        if(action != null && !fExtraActionsList.contains(action)) {
            fExtraActionsList.add(action);
        }
    }
    
    protected void addMenuActions(MenuManager menuManager) {
        if(fDoShowDefaultMenuItem) {
            IAction defaultFontAction = new Action(Messages.FontChooser_1) {
                @Override
                public void run() {
                    boolean oldValue = fIsDefaultFont;
                    fIsDefaultFont = !fIsDefaultFont;
                    fireActionListenerEvent(PROP_FONTDEFAULT, oldValue, fIsDefaultFont);
                }
            };
            
            menuManager.add(defaultFontAction);
            defaultFontAction.setEnabled(!isDefaultFont());
        }
        
        for(IAction action : fExtraActionsList) {
            menuManager.add(action);
        }
        
        if(fDoShowPreferencesMenuItem) {
            menuManager.add(new Separator());

            IAction preferencesAction = new Action(Messages.FontChooser_2) {
                @Override
                public void run() {
                    PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(getControl().getShell(),
                            FontsPreferencePage.ID, null, null);
                    if(dialog != null) {
                        dialog.open();
                    }
                }
            };
            menuManager.add(preferencesAction);
        }
    }
    
    /**
     * Set whether or not the control is enabled.
     * 
     * @param state
     *            the enabled state.
     */
    public void setEnabled(boolean state) {
        getControl().setEnabled(state);
        fTextButton.setEnabled(state);
        fMenuButton.setEnabled(state);
    }

    /**
     * Activate the editor for this selector. This causes the font selection
     * dialog to appear and wait for user input.
     */
    public void chooseFont() {
        FontDialog dialog = new FontDialog(getControl().getShell());
        dialog.setEffectsVisible(false); // Don't allow underline/strikeout on Windows. See https://github.com/archimatetool/archi/issues/851
        dialog.setText(Messages.FontChooser_3);
        dialog.setFontList(new FontData[] { fFontData } );        
        dialog.setRGB(fFontRGB);
        
        FontData selectedFontData = dialog.open();
        if(selectedFontData != null) {
            FontData oldValue = fFontData;
            fFontData = selectedFontData;
            fFontRGB = dialog.getRGB();
            fireActionListenerEvent(PROP_FONTCHANGE, oldValue, fFontData);
            updateButtonText();
        }
    }
    
    protected void updateFont() {
        fFontData = FontFactory.getDefaultUserViewFontData();
        
        String fontValue = fFontObject.getFont();
        if(fontValue != null) {
            try {
                fFontData = new FontData(fontValue);
            }
            catch(Exception ex) {
                //ex.printStackTrace();
            }
        }
        
        fFontRGB = ColorFactory.convertStringToRGB(fFontObject.getFontColor());
        if(fFontRGB == null) {
            fFontRGB = new RGB(0, 0, 0);
        }
        
        updateButtonText();
    }
    
    protected void updateButtonText() {
        String text = fFontData.getName() + " " + //$NON-NLS-1$
                fFontData.getHeight() + " " + //$NON-NLS-1$
                ((fFontData.getStyle() & SWT.BOLD) == SWT.BOLD ? Messages.FontChooser_4 : "") + " " + //$NON-NLS-1$ //$NON-NLS-2$
                ((fFontData.getStyle() & SWT.ITALIC) == SWT.ITALIC ? Messages.FontChooser_7 : "");  //$NON-NLS-1$
        
        // Async this as the button width is calculated later in the UI thread
        fTextButton.getDisplay().asyncExec(() -> {
            if(!fTextButton.isDisposed()) { // control can be disposed when more than one object is deleted in a view
                fTextButton.setText(UIUtils.shortenText(text, fTextButton, 6));
            }
        });
        
        fTextButton.setToolTipText(text);
    }
    
    /**
     * Adds a property change listener to this <code>ColorSelector</code>.
     * Events are fired when the color in the control changes via the user
     * clicking an selecting a new one in the color dialog. No event is fired in
     * the case where <code>setColorValue(RGB)</code> is invoked.
     * 
     * @param listener
     *            a property change listener
     */
    public void addListener(IPropertyChangeListener listener) {
        addListenerObject(listener);
    }

    /**
     * Removes the given listener from this <code>ColorSelector</code>. Has
     * no effect if the listener is not registered.
     * 
     * @param listener
     *            a property change listener
     */
    public void removeListener(IPropertyChangeListener listener) {
        removeListenerObject(listener);
    }

    /**
     * Fire the given event to listeners
     * @param propertyName
     * @param oldValue
     * @param newValue
     */
    private void fireActionListenerEvent(String propertyName, Object oldValue, Object newValue) {
        final Object[] finalListeners = getListeners();
        if(finalListeners.length > 0) {
            PropertyChangeEvent pEvent = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
            for(int i = 0; i < finalListeners.length; ++i) {
                IPropertyChangeListener listener = (IPropertyChangeListener) finalListeners[i];
                listener.propertyChange(pEvent);
            }
        }
    }
}
