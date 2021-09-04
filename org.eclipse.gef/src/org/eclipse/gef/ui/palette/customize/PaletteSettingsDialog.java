/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.palette.customize;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.ui.part.PageBook;

import org.eclipse.gef.ui.palette.PaletteMessages;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;

/**
 * @author Pratik Shah
 */
public class PaletteSettingsDialog extends Dialog {

    private PaletteViewerPreferences prefs;
    private Label fontName;
    private PageBook book;
    private Control columnsPanel, detailsPanel, iconsPanel, listPanel;
    private HashMap<Integer, Button> widgets = new HashMap<>();

    /**
     * A HashMap to cache the various settings displayed in this dialog
     */
    protected HashMap<String, Object> settings = new HashMap<>();

    /**
     * HashMap keys used for caching the various settings displayed in this
     * dialog.
     */
    protected static final String CACHE_LAYOUT = "layout setting", //$NON-NLS-1$
            CACHE_COLUMNS_ICON_SIZE = "columns - use large icons", //$NON-NLS-1$
            CACHE_LIST_ICON_SIZE = "list - use large icons", //$NON-NLS-1$
            CACHE_ICONS_ICON_SIZE = "icons only - use large icons", //$NON-NLS-1$
            CACHE_DETAILS_ICON_SIZE = "details - use large icons", //$NON-NLS-1$
            CACHE_FONT = "font", //$NON-NLS-1$
            CACHE_COLLAPSE = "auto-collapse setting"; //$NON-NLS-1$

    /**
     * The unique IDs for the various widgets. These IDs can be used to retrieve
     * these widgets from the internal map (using {@link #getWidget(int)}), or
     * to identify widgets in {@link #buttonPressed(int)}.
     */
    protected static final int LAYOUT_COLUMNS_VIEW_ID = IDialogConstants.CLIENT_ID + 1,
            LAYOUT_LIST_VIEW_ID = IDialogConstants.CLIENT_ID + 2,
            LAYOUT_ICONS_VIEW_ID = IDialogConstants.CLIENT_ID + 3,
            LAYOUT_COLUMNS_ICON_SIZE_ID = IDialogConstants.CLIENT_ID + 4,
            LAYOUT_LIST_ICON_SIZE_ID = IDialogConstants.CLIENT_ID + 5,
            LAYOUT_ICONS_ICON_SIZE_ID = IDialogConstants.CLIENT_ID + 6,
            LAYOUT_DETAILS_ICON_SIZE_ID = IDialogConstants.CLIENT_ID + 7,
            COLLAPSE_NEVER_ID = IDialogConstants.CLIENT_ID + 8,
            COLLAPSE_ALWAYS_ID = IDialogConstants.CLIENT_ID + 9,
            COLLAPSE_NEEDED_ID = IDialogConstants.CLIENT_ID + 10,
            APPLY_ID = IDialogConstants.CLIENT_ID + 11,
            LAYOUT_DETAILS_VIEW_ID = IDialogConstants.CLIENT_ID + 12,
            FONT_CHANGE_ID = IDialogConstants.CLIENT_ID + 13,
            DEFAULT_FONT_ID = IDialogConstants.CLIENT_ID + 14;

    /**
     * Sub - classes that need to create their own unique IDs should do so by
     * adding to this ID.
     */
    protected static final int CLIENT_ID = 16;

    /**
     * Constructor
     * 
     * @param parentShell
     *            The parent shell, or <code>null</code> to create a top - level
     *            shell
     * @param prefs
     *            The PaletteViewerPreferences object that can provide access to
     *            and allow modification of the palette's settings. It cannot be
     *            <code>null</code>.
     */
    public PaletteSettingsDialog(Shell parentShell,
            PaletteViewerPreferences prefs) {
        super(parentShell);
        this.prefs = prefs;
        setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
    }

    /**
     * This method will be invoked whenever any <code>Button</code> created
     * using {@link #createButton(Composite, int, String, int, ImageDescriptor)}
     * or {@link Dialog#createButton(Composite, int, String, boolean)} is
     * selected.
     * 
     * @see Dialog#buttonPressed(int)
     */
    @Override
    protected void buttonPressed(int buttonId) {
        Button b = getButton(buttonId);

        if (FONT_CHANGE_ID == buttonId) {
            handleChangeFontPressed();
        } else if (COLLAPSE_ALWAYS_ID == buttonId) {
            handleAutoCollapseSettingChanged(PaletteViewerPreferences.COLLAPSE_ALWAYS);
        } else if (COLLAPSE_NEVER_ID == buttonId) {
            handleAutoCollapseSettingChanged(PaletteViewerPreferences.COLLAPSE_NEVER);
        } else if (COLLAPSE_NEEDED_ID == buttonId) {
            handleAutoCollapseSettingChanged(PaletteViewerPreferences.COLLAPSE_AS_NEEDED);
        } else if (LAYOUT_COLUMNS_VIEW_ID == buttonId) {
            handleLayoutSettingChanged(PaletteViewerPreferences.LAYOUT_COLUMNS);
        } else if (LAYOUT_ICONS_VIEW_ID == buttonId) {
            handleLayoutSettingChanged(PaletteViewerPreferences.LAYOUT_ICONS);
        } else if (LAYOUT_LIST_VIEW_ID == buttonId) {
            handleLayoutSettingChanged(PaletteViewerPreferences.LAYOUT_LIST);
        } else if (LAYOUT_DETAILS_VIEW_ID == buttonId) {
            handleLayoutSettingChanged(PaletteViewerPreferences.LAYOUT_DETAILS);
        } else if (LAYOUT_DETAILS_ICON_SIZE_ID == buttonId) {
            handleIconSizeChanged(b.getSelection());
        } else if (LAYOUT_COLUMNS_ICON_SIZE_ID == buttonId) {
            handleIconSizeChanged(b.getSelection());
        } else if (LAYOUT_ICONS_ICON_SIZE_ID == buttonId) {
            handleIconSizeChanged(b.getSelection());
        } else if (LAYOUT_LIST_ICON_SIZE_ID == buttonId) {
            handleIconSizeChanged(b.getSelection());
        } else if (DEFAULT_FONT_ID == buttonId) {
            handleDefaultFontRequested();
        } else {
            super.buttonPressed(buttonId);
        }
    }

    /**
     * This method saves the various settings in this dialog, so that they can
     * be restored later on if "Cancel" is pressed.
     * 
     * @see #restoreSettings()
     */
    protected void cacheSettings() {
        settings.put(CACHE_LAYOUT, Integer.valueOf(prefs.getLayoutSetting()));
        settings.put(CACHE_COLLAPSE,
                Integer.valueOf(prefs.getAutoCollapseSetting()));
        settings.put(CACHE_FONT, prefs.getFontData());
        settings.put(
                CACHE_DETAILS_ICON_SIZE,
                Boolean.valueOf(prefs
                        .useLargeIcons(PaletteViewerPreferences.LAYOUT_DETAILS)));
        settings.put(
                CACHE_ICONS_ICON_SIZE,
                Boolean.valueOf(prefs
                        .useLargeIcons(PaletteViewerPreferences.LAYOUT_ICONS)));
        settings.put(
                CACHE_COLUMNS_ICON_SIZE,
                Boolean.valueOf(prefs
                        .useLargeIcons(PaletteViewerPreferences.LAYOUT_COLUMNS)));
        settings.put(
                CACHE_LIST_ICON_SIZE,
                Boolean.valueOf(prefs
                        .useLargeIcons(PaletteViewerPreferences.LAYOUT_LIST)));
    }

    /**
     * @see org.eclipse.jface.window.Window#close()
     */
    @Override
    public boolean close() {
        // Save or dump changes
        // This needs to be done here and not in the handle methods because the
        // user can
        // also close the dialog with the 'X' in the top right of the window
        // (which
        // corresponds to a cancel).
        if (getReturnCode() != OK) {
            handleCancelPressed();
        }

        return super.close();
    }

    /**
     * @see org.eclipse.jface.window.Window#configureShell(Shell)
     */
    @Override
    protected void configureShell(Shell newShell) {
        newShell.setText(PaletteMessages.SETTINGS_DIALOG_TITLE);
        super.configureShell(newShell);
    }

    /**
     * This method should not be used to create buttons for the button bar. Use
     * {@link Dialog#createButton(Composite, int, String, boolean)} for that.
     * This method can be used to create any other button in the dialog. The
     * parent <code>Composite</code> must have a GridLayout. These buttons will
     * be available through {@link #getButton(int)} and {@link #getWidget(int)}.
     * Ensure that the various buttons created by this method are given unique
     * IDs. Pass in a null image descriptor if you don't want the button to have
     * an icon. This method will take care of disposing the images that it
     * creates. {@link #buttonPressed(int)} will be called when any of the
     * buttons created by this method are clicked (selected).
     * 
     * @param parent
     *            The composite in which the button is to be created
     * @param id
     *            The button's unique ID
     * @param label
     *            The button's text
     * @param stylebits
     *            The style bits for creating the button (eg.,
     *            <code>SWT.PUSH</code) or <code>SWT.CHECK</code>)
     * @param descriptor
     *            The ImageDescriptor from which the image/icon for this button
     *            should be created
     * @return The newly created button for convenience
     */
    protected Button createButton(Composite parent, int id, String label,
            int stylebits, ImageDescriptor descriptor) {
        Button button = new Button(parent, stylebits);
        button.setText(label);
        button.setFont(parent.getFont());
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        button.setLayoutData(data);

        button.setData(Integer.valueOf(id));
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                buttonPressed(((Integer) event.widget.getData()).intValue());
            }
        });
        widgets.put(Integer.valueOf(id), button);

        if (descriptor != null) {
            button.setImage(new Image(parent.getDisplay(), descriptor
                    .getImageData(100)));
            button.addDisposeListener(new DisposeListener() {
                @Override
                public void widgetDisposed(DisposeEvent e) {
                    Image img = ((Button) e.getSource()).getImage();
                    if (img != null && !img.isDisposed()) {
                        img.dispose();
                    }
                }
            });
        }

        return button;
    }

    /**
     * Creates and initializes (i.e., loads the current value from the
     * PaletteViewerPreferences) the part of the dialog where the options to
     * close drawers will be displayed.
     * 
     * @param container
     *            The parent composite
     * @return The newly created Control which has the drawer collapse options
     */
    @SuppressWarnings("null")
    protected Control createDrawerCollapseOptions(Composite container) {
        Composite composite = new Composite(container, SWT.NONE);
        composite.setFont(container.getFont());
        GridLayout layout = new GridLayout(1, false);
        composite.setLayout(layout);

        Label label = new Label(composite, SWT.NONE);
        label.setFont(composite.getFont());
        label.setText(PaletteMessages.COLLAPSE_OPTIONS_TITLE);
        GridData data = new GridData();
        label.setLayoutData(data);

        Button b = createButton(composite, COLLAPSE_ALWAYS_ID,
                PaletteMessages.COLLAPSE_ALWAYS_LABEL, SWT.RADIO, null);
        ((GridData) b.getLayoutData()).horizontalIndent = 5;

        b = createButton(composite, COLLAPSE_NEEDED_ID,
                PaletteMessages.COLLAPSE_AS_NEEDED_LABEL, SWT.RADIO, null);
        ((GridData) b.getLayoutData()).horizontalIndent = 5;

        b = createButton(composite, COLLAPSE_NEVER_ID,
                PaletteMessages.COLLAPSE_NEVER_LABEL, SWT.RADIO, null);
        ((GridData) b.getLayoutData()).horizontalIndent = 5;

        // Load auto - collapse settings
        b = null;
        int collapse = prefs.getAutoCollapseSetting();
        switch (collapse) {
        case PaletteViewerPreferences.COLLAPSE_ALWAYS:
            b = getButton(COLLAPSE_ALWAYS_ID);
            break;
        case PaletteViewerPreferences.COLLAPSE_AS_NEEDED:
            b = getButton(COLLAPSE_NEEDED_ID);
            break;
        case PaletteViewerPreferences.COLLAPSE_NEVER:
            b = getButton(COLLAPSE_NEVER_ID);
        }
        b.setSelection(true);

        return composite;
    }

    /**
     * Creates and initializes (i.e. loads the current settings from
     * PaletteViewerPreferences) the options for details layout.
     * 
     * @param parent
     *            the parent composite
     * @return the newly created Control
     */
    protected Control createDetailsOptions(Composite parent) {
        Control contents = createOptionsPage(parent,
                PaletteMessages.SETTINGS_OPTIONS_DETAILS,
                LAYOUT_DETAILS_ICON_SIZE_ID);
        getButton(LAYOUT_DETAILS_ICON_SIZE_ID).setSelection(
                prefs.useLargeIcons(PaletteViewerPreferences.LAYOUT_DETAILS));
        return contents;
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout layout = (GridLayout) composite.getLayout();
        layout.horizontalSpacing = 0;
        layout.numColumns = 2;

        Control child = createFontSettings(composite);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        data.horizontalIndent = 5;
        child.setLayoutData(data);

        Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.horizontalSpan = 2;
        label.setLayoutData(data);

        child = createLayoutSettings(composite);
        data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.FILL_VERTICAL);
        data.horizontalSpan = 2;
        data.horizontalIndent = 5;
        child.setLayoutData(data);

        label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.horizontalSpan = 2;
        label.setLayoutData(data);

        child = createDrawerCollapseOptions(composite);
        data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.horizontalSpan = 2;
        data.horizontalIndent = 5;
        child.setLayoutData(data);

        label = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.horizontalSpan = 2;
        label.setLayoutData(data);

        cacheSettings();

        return composite;
    }

    /**
     * Creates and initializes (i.e. loads the current settings from
     * PaletteViewerPreferences) the options for columns layout.
     * 
     * @param parent
     *            the parent composite
     * @return the newly created Control
     */
    protected Control createColumnsOptions(Composite parent) {
        Composite contents = (Composite) createOptionsPage(parent,
                PaletteMessages.SETTINGS_OPTIONS_COLUMNS,
                LAYOUT_COLUMNS_ICON_SIZE_ID);
        getButton(LAYOUT_COLUMNS_ICON_SIZE_ID).setSelection(
                prefs.useLargeIcons(PaletteViewerPreferences.LAYOUT_COLUMNS));

        // final Button button = createButton(contents, -1,
        // PaletteMessages.SETTINGS_LAYOUT_COLUMNS_OVERRIDE_WIDTH, SWT.CHECK,
        // null);
        // ((GridData)button.getLayoutData()).horizontalSpan = 2;
        //
        // Composite container = new Composite(contents, SWT.NONE);
        // container.setFont(contents.getFont());
        // GridLayout layout = new GridLayout(2, false);
        // container.setLayout(layout);
        // GridData data = new GridData(GridData.FILL_HORIZONTAL |
        // GridData.VERTICAL_ALIGN_FILL);
        // data.horizontalSpan = 2;
        // container.setLayoutData(data);
        //
        // final Label label = new Label(container, SWT.NONE);
        // label.setFont(container.getFont());
        // label.setText(PaletteMessages.SETTINGS_LAYOUT_COLUMNS_WIDTH);
        // data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
        // | GridData.VERTICAL_ALIGN_BEGINNING);
        // label.setLayoutData(data);
        // label.setEnabled(false);
        //
        // final Text box = new Text(container, SWT.SINGLE | SWT.BORDER);
        // box.setFont(container.getFont());
        // // box.setText("30");
        // box.setEnabled(false);
        // data = new GridData(GridData.FILL_HORIZONTAL |
        // GridData.VERTICAL_ALIGN_BEGINNING);
        // data.widthHint = 50;
        // box.setLayoutData(data);
        //
        // button.addSelectionListener(new SelectionListener() {
        // public void widgetSelected(SelectionEvent e) {
        // label.setEnabled(!label.isEnabled());
        // box.setEnabled(!box.isEnabled());
        // }
        // public void widgetDefaultSelected(SelectionEvent e) {
        // }
        // });

        return contents;
    }

    /**
     * Creates and initializes (i.e. loads the current settings from
     * PaletteViewerPreferences) the part of the dialog that displays the font
     * settings.
     * 
     * @param parent
     *            the parent composite
     * @return the newly created Control
     */
    protected Control createFontSettings(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setFont(parent.getFont());
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        fontName = new Label(container, SWT.LEFT | SWT.WRAP);
        fontName.setFont(container.getFont());
        GridData data = new GridData(GridData.FILL_BOTH);
        data.verticalSpan = 2;
        fontName.setLayoutData(data);
        updateFontName();

        createButton(container, FONT_CHANGE_ID,
                PaletteMessages.SETTINGS_FONT_CHANGE, SWT.PUSH, null);

        createButton(container, DEFAULT_FONT_ID,
                PaletteMessages.SETTINGS_DEFAULT_FONT, SWT.PUSH, null);

        return container;
    }

    /**
     * Creates and initializes (i.e. loads the current settings from
     * PaletteViewerPreferences) the options for icons layout.
     * 
     * @param parent
     *            the parent composite
     * @return the newly created Control
     */
    protected Control createIconsOnlyOptions(Composite parent) {
        Control contents = createOptionsPage(parent,
                PaletteMessages.SETTINGS_OPTIONS_ICONS_ONLY,
                LAYOUT_ICONS_ICON_SIZE_ID);
        getButton(LAYOUT_ICONS_ICON_SIZE_ID).setSelection(
                prefs.useLargeIcons(PaletteViewerPreferences.LAYOUT_ICONS));
        return contents;
    }

    /**
     * Creates the part of the dialog that displays the various options for the
     * selected layout.
     * 
     * @param parent
     *            the parent composite
     * @return the newly created Control
     */
    @SuppressWarnings("null")
    protected Control createLayoutOptions(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setFont(parent.getFont());
        GridLayout layout = new GridLayout(1, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        composite.setLayout(layout);

        Label label = new Label(composite, SWT.NONE);
        label.setFont(composite.getFont());
        label.setText(PaletteMessages.SETTINGS_LAYOUT_TITLE);
        GridData data = new GridData();
        label.setLayoutData(data);

        Button b = null;
        int[] modes = prefs.getSupportedLayoutModes();
        for (int i = 0; i < modes.length; i++) {
            switch (modes[i]) {
            case PaletteViewerPreferences.LAYOUT_COLUMNS:
                b = createButton(composite, LAYOUT_COLUMNS_VIEW_ID,
                        PaletteMessages.SETTINGS_COLUMNS_VIEW_LABEL, SWT.RADIO,
                        null);
                ((GridData) b.getLayoutData()).horizontalIndent = 5;
                break;
            case PaletteViewerPreferences.LAYOUT_LIST:
                b = createButton(composite, LAYOUT_LIST_VIEW_ID,
                        PaletteMessages.SETTINGS_LIST_VIEW_LABEL, SWT.RADIO,
                        null);
                ((GridData) b.getLayoutData()).horizontalIndent = 5;
                break;
            case PaletteViewerPreferences.LAYOUT_ICONS:
                b = createButton(composite, LAYOUT_ICONS_VIEW_ID,
                        PaletteMessages.SETTINGS_ICONS_VIEW_LABEL, SWT.RADIO,
                        null);
                ((GridData) b.getLayoutData()).horizontalIndent = 5;
                break;
            case PaletteViewerPreferences.LAYOUT_DETAILS:
                b = createButton(composite, LAYOUT_DETAILS_VIEW_ID,
                        PaletteMessages.SETTINGS_DETAILS_VIEW_LABEL, SWT.RADIO,
                        null);
                ((GridData) b.getLayoutData()).horizontalIndent = 5;
                break;
            }
        }

        // Load layout settings
        int layoutSetting = prefs.getLayoutSetting();
        switch (layoutSetting) {
        case PaletteViewerPreferences.LAYOUT_COLUMNS:
            b = getButton(LAYOUT_COLUMNS_VIEW_ID);
            break;
        case PaletteViewerPreferences.LAYOUT_ICONS:
            b = getButton(LAYOUT_ICONS_VIEW_ID);
            break;
        case PaletteViewerPreferences.LAYOUT_LIST:
            b = getButton(LAYOUT_LIST_VIEW_ID);
            break;
        case PaletteViewerPreferences.LAYOUT_DETAILS:
            b = getButton(LAYOUT_DETAILS_VIEW_ID);
            break;
        }
        b.setSelection(true);
        b.setFocus();

        return composite;
    }

    /**
     * Creates the part of the dialog that displays the lists the available
     * layout modes.
     * 
     * @param parent
     *            the parent composite
     * @return the newly created Control
     */
    protected Control createLayoutSettings(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setFont(parent.getFont());
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);

        Control layoutOptions = createLayoutOptions(composite);
        GridData data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        layoutOptions.setLayoutData(data);

        book = new PageBook(composite, SWT.NONE);
        book.setFont(composite.getFont());
        data = new GridData(GridData.FILL_BOTH);
        book.setLayoutData(data);

        columnsPanel = createColumnsOptions(book);
        listPanel = createListOptions(book);
        iconsPanel = createIconsOnlyOptions(book);
        detailsPanel = createDetailsOptions(book);

        // Show the right page in the book
        handleLayoutSettingChanged(prefs.getLayoutSetting());

        return composite;
    }

    /**
     * Creates and initializes (i.e. loads the current settings from
     * PaletteViewerPreferences) the options for list layout.
     * 
     * @param parent
     *            the parent composite
     * @return the newly created Control
     */
    protected Control createListOptions(Composite parent) {
        Control composite = createOptionsPage(parent,
                PaletteMessages.SETTINGS_OPTIONS_LIST, LAYOUT_LIST_ICON_SIZE_ID);
        getButton(LAYOUT_LIST_ICON_SIZE_ID).setSelection(
                prefs.useLargeIcons(PaletteViewerPreferences.LAYOUT_LIST));
        return composite;
    }

    /**
     * This helper method is a result of code-factoring. It creates a Group
     * displaying the given title and creates a "Use Large Icons" checkbox with
     * the given buttonId in it. This method is used to create the options for
     * the different layout modes.
     * 
     * @param parent
     *            the parent composite
     * @param title
     *            The title for the group to be created.
     * @param buttonId
     *            The ID for the "Use Large Icons" checkbox to be created in the
     *            group.
     * @return the newly created Group
     */
    protected Control createOptionsPage(Composite parent, String title,
            int buttonId) {
        Group contents = new Group(parent, SWT.NONE);
        contents.setFont(parent.getFont());
        GridLayout layout = new GridLayout(1, false);
        contents.setLayout(layout);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.VERTICAL_ALIGN_FILL);
        data.heightHint = 0;
        contents.setLayoutData(data);
        contents.setText(title);

        Button b = createButton(contents, buttonId,
                PaletteMessages.SETTINGS_USE_LARGE_ICONS_LABEL, SWT.CHECK, null);

        // Modified by Phillipus to disable this setting
        b.setEnabled(false);
        
        return contents;
    }

    /**
     * Returns the Button with the given id; or <code>null</code> if none was
     * found.
     * 
     * @see org.eclipse.jface.dialogs.Dialog#getButton(int)
     */
    @Override
    protected Button getButton(int id) {
        Button button = null;
        Widget widget = getWidget(id);
        if (widget instanceof Button) {
            button = (Button) widget;
        }

        return button;
    }

    /**
     * The <code>Widget</code>s that were created with a unique ID and added to
     * this class' internal map can be retrieved through this method.
     * 
     * @param id
     *            The unique ID of the Widget that you wish to retrieve
     * @return The Widget, if one with the given id exists. <code>null</code>
     *         otherwise.
     */
    protected Widget getWidget(int id) {
        Widget widget = widgets.get(Integer.valueOf(id));
        if (widget == null) {
            widget = super.getButton(id);
        }

        return widget;
    }

    /**
     * Called when any one of the "Auto - Collapse" radio buttons is clicked. It
     * changes the setting in the
     * {@link org.eclipse.gef.ui.palette.PaletteViewerPreferences} object.
     * 
     * @param newSetting
     *            The flag for the new setting
     */
    protected void handleAutoCollapseSettingChanged(int newSetting) {
        prefs.setAutoCollapseSetting(newSetting);
    }

    /**
     * This method is invoked when "Cancel" is invoked on the dialog. It simply
     * restores the settings, thus undoing any changes made in this Dialog.
     */
    protected void handleCancelPressed() {
        restoreSettings();
    }

    /**
     * This method is invoked when the user selects the "Change" font button. It
     * opens the FontDialog to allow the user to change the font.
     */
    protected void handleChangeFontPressed() {
        FontDialog dialog = new FontDialog(getShell());
        FontData data = prefs.getFontData();
        dialog.setFontList(new FontData[] { data });
        data = dialog.open();
        if (data != null) {
            prefs.setFontData(data);
        }
        updateFontName();
    }

    /**
     * This method is invoked when the user selects the "Restore Default" font
     * button. It changes the font, in case it was different, to the default
     * one, which is the Workbench Dialog font.
     */
    protected void handleDefaultFontRequested() {
        prefs.setFontData(JFaceResources.getDialogFont().getFontData()[0]);
        updateFontName();
    }

    /**
     * This method is invoked when the "Use Large Icons" checkbox is
     * selected/deselected for the currently active layout mode.
     * 
     * @param selection
     *            indicates whether large icons are to be used or not.
     */
    protected void handleIconSizeChanged(boolean selection) {
        prefs.setCurrentUseLargeIcons(selection);
    }

    /**
     * This method is called when any one of the "Layout" radio buttons is
     * clicked. It changes the setting in the
     * {@link org.eclipse.gef.ui.palette.PaletteViewerPreferences} object.
     * 
     * @param newSetting
     *            The flag for the new setting
     */
    protected void handleLayoutSettingChanged(int newSetting) {
        prefs.setLayoutSetting(newSetting);
        switch (newSetting) {
        case PaletteViewerPreferences.LAYOUT_COLUMNS:
            showLayoutOptionsPage(columnsPanel);
            break;
        case PaletteViewerPreferences.LAYOUT_LIST:
            showLayoutOptionsPage(listPanel);
            break;
        case PaletteViewerPreferences.LAYOUT_ICONS:
            showLayoutOptionsPage(iconsPanel);
            break;
        case PaletteViewerPreferences.LAYOUT_DETAILS:
            showLayoutOptionsPage(detailsPanel);
            break;
        default:
            break;
        }
    }

    /**
     * Restores the cached settings, thus undoing any changes made since the
     * last caching of settings.
     * 
     * @see #cacheSettings()
     */
    protected void restoreSettings() {
        prefs.setFontData((FontData) settings.get(CACHE_FONT));
        prefs.setAutoCollapseSetting(((Integer) settings.get(CACHE_COLLAPSE))
                .intValue());
        prefs.setLayoutSetting(((Integer) settings.get(CACHE_LAYOUT))
                .intValue());
        prefs.setUseLargeIcons(PaletteViewerPreferences.LAYOUT_DETAILS,
                ((Boolean) settings.get(CACHE_DETAILS_ICON_SIZE))
                        .booleanValue());
        prefs.setUseLargeIcons(PaletteViewerPreferences.LAYOUT_ICONS,
                ((Boolean) settings.get(CACHE_ICONS_ICON_SIZE)).booleanValue());
        prefs.setUseLargeIcons(PaletteViewerPreferences.LAYOUT_LIST,
                ((Boolean) settings.get(CACHE_LIST_ICON_SIZE)).booleanValue());
        prefs.setUseLargeIcons(PaletteViewerPreferences.LAYOUT_COLUMNS,
                ((Boolean) settings.get(CACHE_COLUMNS_ICON_SIZE))
                        .booleanValue());
    }

    /**
     * This helper method is mainly a result of code-factoring. It shows the
     * given page (which should be one of the controls showing the layout
     * options) in the PageBook and grows the dialog if necessary.
     * 
     * @param page
     *            One of the controls showing the layout options that already
     *            belongs to the PageBook book.
     */
    protected void showLayoutOptionsPage(Control page) {
        // Show the page and grow the shell (if necessary) so that the page is
        // completely
        // visible
        Point oldSize = getShell().getSize();
        book.showPage(page);
        Point newSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        int x = newSize.x - oldSize.x;
        x = (x < 0) ? 0 : x;
        int y = newSize.y - oldSize.y;
        y = (y < 0) ? 0 : y;
        if (x > 0 || y > 0) {
            getShell().setSize(oldSize.x + x, oldSize.y + y);
        }
    }

    /**
     * Updates the label showing the font's name to show the name of the current
     * font.
     */
    protected void updateFontName() {
        String name;
        if (prefs.getFontData().equals(
                (JFaceResources.getDialogFont().getFontData()[0]))) {
            name = PaletteMessages.SETTINGS_WORKBENCH_FONT_LABEL;
        } else {
            name = StringConverter.asString(prefs.getFontData());
        }
        fontName.setText(PaletteMessages.SETTINGS_FONT_CURRENT + name);
    }

}
