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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.archimatetool.editor.preferences.ColoursPreferencePage;
import com.archimatetool.editor.ui.IArchiImages;




/**
 * Colour Chooser
 * Based on org.eclipse.jface.preference.ColorSelector
 * 
 * @author Phillip Beauvoir
 */
public class ColorChooser extends EventManager {
    /**
     * Property name that signifies the selected color of this
     * <code>ColorSelector</code> has changed.
     */
    public static final String PROP_COLORCHANGE = "colorValue"; //$NON-NLS-1$
    
    /**
     * Property name that signifies the selected color of this
     * <code>ColorSelector</code> has changed to default.
     */
    public static final String PROP_COLORDEFAULT = "colorDefault"; //$NON-NLS-1$

    private Composite fComposite;
    private Button fColorButton;
    private Button fMenuButton;
    
    private RGB fColorValue;
    
    private boolean fIsDefaultColor;
    
    protected boolean fDoShowDefaultMenuItem = true;
    protected boolean fDoShowPreferencesMenuItem = true;
    protected boolean fDoShowColorImage = true;
    
    private List<IAction> fExtraActionsList = new ArrayList<IAction>();
    
    public ColorChooser(Composite parent, FormToolkit toolkit) {
        this(parent);
        toolkit.adapt(fColorButton, true, true);
        toolkit.adapt(fMenuButton, true, true);
    }
    
    public ColorChooser(Composite parent) {
        fComposite = new Composite(parent, SWT.NULL);
        fComposite.setBackgroundMode(SWT.INHERIT_FORCE);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = 0;
        fComposite.setLayout(layout);
        
        fColorButton = new Button(fComposite, SWT.FLAT);
        
        GridDataFactory.create(SWT.NONE).hint(60, SWT.DEFAULT).applyTo(fColorButton);
        
        fColorButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                chooseColor();
            }
        });
        
        fColorButton.addDisposeListener((e) -> {
            if(fColorButton.getImage() != null) {
                fColorButton.getImage().dispose();
            }
        });
        
        fComposite.getAccessible().addAccessibleListener(new AccessibleAdapter() {
            @Override
            public void getName(AccessibleEvent e) {
                e.result = "Colour Chooser"; //$NON-NLS-1$
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
    
    public void setIsDefaultColor(boolean set) {
        fIsDefaultColor = set;
    }
    
    public boolean isDefaultColor() {
        return fIsDefaultColor;
    }
    
    public void setDoShowPreferencesMenuItem(boolean set) {
        fDoShowPreferencesMenuItem = set;
    }
    
    public void setDoShowDefaultMenuItem(boolean set) {
        fDoShowDefaultMenuItem = set;
    }
    
    public void setDoShowColorImage(boolean set) {
        fDoShowColorImage = set;
        updateColorImage();
    }
    
    public Control getControl() {
        return fComposite;
    }
    
    public Button getColorButton() {
        return fColorButton;
    }
    
    public void showMenu() {
        MenuManager menuManager = new MenuManager();
        addMenuActions(menuManager);
        
        Menu menu = menuManager.createContextMenu(fMenuButton.getShell());
        Point p = fColorButton.getParent().toDisplay(fMenuButton.getBounds().x, fMenuButton.getBounds().y);
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
            IAction defaultColorAction = new Action(Messages.ColorChooser_1) {
                @Override
                public void run() {
                    boolean oldValue = fIsDefaultColor;
                    fIsDefaultColor = !fIsDefaultColor;
                    fireActionListenerEvent(PROP_COLORDEFAULT, oldValue, fIsDefaultColor);
                }
            };
            
            menuManager.add(defaultColorAction);
            defaultColorAction.setEnabled(!isDefaultColor());
        }
        
        for(IAction action : fExtraActionsList) {
            menuManager.add(action);
        }
        
        if(fDoShowPreferencesMenuItem) {
            menuManager.add(new Separator());

            IAction preferencesAction = new Action(Messages.ColorChooser_2) {
                @Override
                public void run() {
                    PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(getControl().getShell(),
                            ColoursPreferencePage.ID, null, null);
                    if(dialog != null) {
                        dialog.open();
                    }
                }
            };
            menuManager.add(preferencesAction);
        }
    }
    
    /**
     * Return the currently displayed color.
     * 
     * @return <code>RGB</code>
     */
    public RGB getColorValue() {
        return fColorValue;
    }
    
    /**
     * Set the current color value and update the control.
     * 
     * @param rgb
     *            The new color.
     */
    public void setColorValue(RGB rgb) {
        if(rgb != null && !rgb.equals(fColorValue)) {
            fColorValue = rgb;
            updateColorImage();
        }
    }

    /**
     * Set whether or not the button is enabled.
     * 
     * @param state
     *            the enabled state.
     */
    public void setEnabled(boolean state) {
        getControl().setEnabled(state);
        fColorButton.setEnabled(state);
        fMenuButton.setEnabled(state);
    }

    /**
     * Activate the editor for this selector. This causes the color selection
     * dialog to appear and wait for user input.
     */
    public void chooseColor() {
        CustomColorDialog colorDialog = new CustomColorDialog(fColorButton.getShell());
        colorDialog.setRGB(fColorValue);
        
        RGB newColor = colorDialog.open();
        if(newColor != null && !newColor.equals(fColorValue)) {
            RGB oldValue = fColorValue;
            fColorValue = newColor;
            fireActionListenerEvent(PROP_COLORCHANGE, oldValue, newColor);
            updateColorImage();
        }
    }

    /**
     * Update the image being displayed on the button using the current color
     * setting.
     */
    protected void updateColorImage() {
        Image image = new Image(fColorButton.getDisplay(), (ImageDataProvider) zoom -> {
            final int width = 40;
            final int height = 15;
            
            Image tmp = new Image(fColorButton.getDisplay(), width, height);
            GC gc = new GC(tmp);
            
            gc.setBackground(fDoShowColorImage ? new Color(fColorValue) : new Color(255, 255, 255));
            gc.fillRectangle(0, 0, width - 1, height - 1);
            gc.drawRectangle(0, 0, width - 1, height - 1);
            
            if(!fDoShowColorImage) {
                gc.drawLine(0, 1, width - 1, height - 2);
            }
            
            gc.dispose();
            
            ImageData imageData = tmp.getImageData(zoom);
            tmp.dispose();
            return imageData;
        });
        
        // Get previous button's image *first* so we can dispose it *after* setting image
        Image oldImage = fColorButton.getImage();

        // Replace with autoscaled image (this is needed on Linux)
        fColorButton.setImage(image);

        if(oldImage != null) {
            oldImage.dispose();
        }
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
