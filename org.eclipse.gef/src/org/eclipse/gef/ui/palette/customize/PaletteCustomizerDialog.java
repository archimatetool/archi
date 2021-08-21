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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.PageBook;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.widgets.MultiLineLabel;

import org.eclipse.gef.internal.Internal;
import org.eclipse.gef.internal.ui.palette.ToolbarDropdownContributionItem;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteCustomizer;
import org.eclipse.gef.ui.palette.PaletteMessages;

/**
 * This class implements a default dialog that allows customization of the
 * different entries/items on a GEF palette, i.e. the model behind the palette.
 * <p>
 * The construction of the dialog is broken down into different methods in order
 * to allow clients to further customize the appearance of the dialog, if so
 * desired.
 * </p>
 * <p>
 * This dialog can be re-used, i.e., it can be re-opened once closed. There is
 * no need to create a new <code>PaletteCustomizerDialog</code> everytime a
 * palette needs to be customized.
 * </p>
 * 
 * @author Pratik Shah
 * @see org.eclipse.gef.palette.PaletteEntry
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer
 */
@SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
public class PaletteCustomizerDialog extends Dialog implements
        EntryPageContainer {

    /**
     * The unique ID for the Apply Button. It can be used to retrieve that
     * widget from the internal map (using {@link #getWidget(int)} or
     * {@link #getButton(int)}), or to identify that widget in
     * {@link #buttonPressed(int)}.
     */
    protected static final int APPLY_ID = IDialogConstants.CLIENT_ID + 1;

    /**
     * Sub-classes that need to create their own unique IDs should do so by
     * adding to this ID.
     */
    protected static final int CLIENT_ID = 16;

    private HashMap widgets = new HashMap();
    private HashMap entriesToPages = new HashMap();
    private List actions;

    private String errorMessage;
    private Tree tree;
    private Composite titlePage, errorPage;
    private PageBook propertiesPanelContainer;
    // This PageBook is used to switch the title of the properties panel to
    // either an error
    // message or the currently active entry's label
    private PageBook titleSwitcher;
    private PaletteCustomizer customizer;
    private EntryPage activePage, noSelectionPage;
    private CLabel title;
    private MultiLineLabel errorTitle;
    private Image titleImage;
    private TreeViewer treeviewer;
    private ILabelProvider treeViewerLabelProvider;
    private PaletteEntry activeEntry;
    private PaletteEntry initialSelection;
    private PaletteRoot root;
    private PropertyChangeListener titleUpdater = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (title == null) {
                return;
            }

            title.setText(((PaletteEntry) evt.getSource()).getLabel());
        }
    };
    private ISelectionChangedListener pageFlippingPreventer = new ISelectionChangedListener() {
        @Override
        public void selectionChanged(SelectionChangedEvent event) {
            treeviewer.removePostSelectionChangedListener(this);
            treeviewer.setSelection(new StructuredSelection(activeEntry));
        }
    };
    private boolean isSetup = true;

    /**
     * Constructs a new customizer dialog.
     * 
     * @param shell
     *            the parent Shell
     * @param customizer
     *            the customizer
     * @param root
     *            the palette root
     */
    public PaletteCustomizerDialog(Shell shell, PaletteCustomizer customizer,
            PaletteRoot root) {
        super(shell);
        this.customizer = customizer;
        this.root = root;
        setShellStyle(getShellStyle() | SWT.RESIZE);
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
        if (APPLY_ID == buttonId) {
            handleApplyPressed();
        } else {
            super.buttonPressed(buttonId);
        }
    }

    /**
     * This method should be invoked by EntryPages when an error that they had
     * earlier reported (using {@link #showProblem(String)}) is fixed. This will
     * hide the error message, enable the OK and Apply buttons and re-allow
     * changing selection in the outline tree.
     * 
     * @see org.eclipse.gef.ui.palette.customize.EntryPageContainer#clearProblem()
     * @see #showProblem(String)
     */
    @Override
    public void clearProblem() {
        if (errorMessage != null) {
            titleSwitcher.showPage(titlePage);
            getButton(IDialogConstants.OK_ID).setEnabled(true);
            getButton(APPLY_ID).setEnabled(true);
            errorMessage = null;
        }
    }

    /**
     * <p>
     * NOTE: This dialog can be re-opened.
     * </p>
     * 
     * @see org.eclipse.jface.window.Window#close()
     */
    @Override
    public boolean close() {
        // Remove listeners
        if (activeEntry != null) {
            activeEntry.removePropertyChangeListener(titleUpdater);
        }

        // Save or dump changes
        // This needs to be done here and not in the handle methods because the
        // user can
        // also close the dialog with the 'X' in the top right of the window
        // (which
        // corresponds to a cancel).
        if (getReturnCode() == OK) {
            save();
        } else {
            revertToSaved();
        }

        // Close the dialog
        boolean returnVal = super.close();

        // Reset variables
        entriesToPages.clear();
        widgets.clear();
        actions = null;
        activePage = null;
        tree = null;
        propertiesPanelContainer = null;
        titleSwitcher = null;
        titlePage = null;
        errorPage = null;
        title = null;
        errorTitle = null;
        treeviewer = null;
        noSelectionPage = null;
        initialSelection = null;
        activeEntry = null;
        errorMessage = null;
        isSetup = true;

        return returnVal;
    }

    /**
     * @see org.eclipse.jface.window.Window#configureShell(Shell)
     */
    @Override
    protected void configureShell(Shell newShell) {
        newShell.setText(PaletteMessages.CUSTOMIZE_DIALOG_TITLE);
        super.configureShell(newShell);
    }

    /**
     * This method should not be used to create buttons for the button bar. Use
     * {@link Dialog#createButton(Composite, int, String, boolean)} for that.
     * This method can be used to create any other button in the dialog. The
     * parent <code>Composite</code> must have a GridLayout. These buttons will
     * be available through {@link #getButton(int)} and {@link #getWidget(int)}.
     * Ensure that the various buttons created by this method are given unique
     * IDs. Pass in a <code>null</code> image descriptor if you don't want the
     * button to have an icon. This method will take care of disposing the
     * images that it creates. {@link #buttonPressed(int)} will be called when
     * any of the buttons created by this method are clicked (selected).
     * 
     * @param parent
     *            The composite in which the button is to be created
     * @param id
     *            The button's unique ID
     * @param label
     *            The button's text
     * @param stylebits
     *            The style bits for creating the button (eg.,
     *            <code>SWT.PUSH</code> or <code>SWT.CHECK</code>)
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

        button.setData(new Integer(id));
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                buttonPressed(((Integer) event.widget.getData()).intValue());
            }
        });
        widgets.put(new Integer(id), button);

        if (descriptor != null) {
            button.setImage(new Image(parent.getDisplay(), descriptor
                    .getImageData()));
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
     * Creates the OK, Cancel and Apply buttons
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        createButton(parent, APPLY_ID, PaletteMessages.APPLY_LABEL, false);
    }

    /**
     * The dialog area contains the following:
     * <UL>
     * <LI>Outline ({@link #createOutline(Composite)})</LI>
     * <LI>Properties Panel ({@link #createPropertiesPanel(Composite)})</LI>
     * </UL>
     * 
     * <p>
     * It is recommended that this method not be overridden. Override one of the
     * methods that this method calls in order to customize the appearance of
     * the dialog.
     * </p>
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout gridLayout = (GridLayout) composite.getLayout();
        gridLayout.numColumns = 2;
        gridLayout.horizontalSpacing = 10;

        // Create the tree
        Control child = createOutline(composite);
        GridData data = new GridData(GridData.VERTICAL_ALIGN_FILL);
        data.verticalSpan = 2;
        child.setLayoutData(data);

        // Create the panel where the properties of the selected palette entry
        // will
        // be shown
        child = createPropertiesPanel(composite);
        child.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Create the separator b/w the dialog area and the button bar
        Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        label.setLayoutData(data);

        // Select an element in the outline and set focus on the outline.
        if (initialSelection == null) {
            // We have to manually select the first item in the tree, because
            // otherwise the
            // will scroll to show the last item, and then will select the first
            // visible item.
            List children = getPaletteRoot().getChildren();
            if (!children.isEmpty()) {
                initialSelection = (PaletteEntry) children.get(0);
            }
        }
        if (initialSelection != null) {
            treeviewer.setSelection(new StructuredSelection(initialSelection));
        } else {
            setActiveEntry(null);
        }
        isSetup = false;
        tree.setFocus();

        return composite;
    }

    /**
     * Creates the outline part of the dialog.
     * 
     * <p>
     * The outline creates the following:
     * <UL>
     * <LI>ToolBar ({@link #createOutlineToolBar(Composite)})</LI>
     * <LI>TreeViewer ({@link #createOutlineTreeViewer(Composite)})</LI>
     * <LI>Context menu ({@link #createOutlineContextMenu()})</LI>
     * </UL>
     * </p>
     * 
     * @param container
     *            The Composite within which the outline has to be created
     * @return The newly created Control that has the outline
     */
    protected Control createOutline(Composite container) {
        // Create the Composite that will contain the outline
        Composite composite = new Composite(container, SWT.NONE);
        composite.setFont(container.getFont());
        GridLayout layout = new GridLayout();
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        composite.setLayout(layout);

        // Create the ToolBar
        createOutlineToolBar(composite);

        // Create the actual outline (TreeViewer)
        treeviewer = createOutlineTreeViewer(composite);
        tree = treeviewer.getTree();

        // Create the context menu for the Tree
        tree.setMenu(createOutlineContextMenu());

        return composite;
    }

    /**
     * Creates the actions that manipulate the palette model. These actions will
     * populate the toolbar and the outline's context menu.
     * 
     * <p>
     * IMPORTANT: All the elements in the returned List MUST be
     * <code>PaletteCustomizationAction</code>s.
     * </p>
     * 
     * @return A List of {@link PaletteCustomizationAction
     *         PaletteCustomizationActions}
     */
    protected List createOutlineActions() {
        List actions = new ArrayList();
        actions.add(new NewAction());
        actions.add(new DeleteAction());
        actions.add(new MoveDownAction());
        actions.add(new MoveUpAction());
        return actions;
    }

    /**
     * Uses a <code>MenuManager</code> to create the context menu for the
     * outline. The <code>IActions</code> used to create the context menu are
     * those created in {@link #createOutlineActions()}.
     * 
     * @return The newly created Menu
     */
    protected Menu createOutlineContextMenu() {
        // MenuManager for the tree's context menu
        final MenuManager outlineMenu = new MenuManager();

        List actions = getOutlineActions();
        // Add all the actions to the context menu
        for (Iterator iter = actions.iterator(); iter.hasNext();) {
            IAction action = (IAction) iter.next();
            if (action instanceof IMenuCreator)
                outlineMenu.add(new ActionContributionItem(action) {
                    @Override
                    public boolean isDynamic() {
                        return true;
                    }
                });
            else
                outlineMenu.add(action);
            // Add separators after new and delete
            if (action instanceof NewAction || action instanceof DeleteAction) {
                outlineMenu.add(new Separator());
            }
        }

        outlineMenu.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(IMenuManager manager) {
                outlineMenu.update(true);
            }
        });

        outlineMenu.createContextMenu(tree);
        return outlineMenu.getMenu();
    }

    /**
     * Uses a ToolBarManager to create the ToolBar in the outline part of the
     * dialog. The Actions used in the ToolBarManager are those that are created
     * in {@link #createOutlineActions()}.
     * 
     * @param parent
     *            The Composite to which the ToolBar is to be added
     * @return The newly created ToolBar
     */
    protected Control createOutlineToolBar(Composite parent) {
        // A customized composite for the toolbar
        final Composite composite = new Composite(parent, SWT.NONE) {
            @Override
            public Rectangle getClientArea() {
                Rectangle area = super.getClientArea();
                area.x += 2;
                area.y += 2;
                area.height -= 2;
                area.width -= 4;
                return area;
            }

            @Override
            public Point computeSize(int wHint, int hHint, boolean changed) {
                Point size = super.computeSize(wHint, hHint, changed);
                size.x += 4;
                size.y += 2;
                return size;
            }
        };
        composite.setFont(parent.getFont());
        composite.setLayout(new FillLayout());

        // A paint listener that draws an etched border around the toolbar
        composite.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                Rectangle area = composite.getBounds();
                GC gc = e.gc;
                gc.setLineStyle(SWT.LINE_SOLID);
                gc.setForeground(ColorConstants.buttonDarker);
                gc.drawLine(area.x, area.y, area.x + area.width - 2, area.y);
                gc.drawLine(area.x, area.y, area.x, area.y + area.height - 1);
                gc.drawLine(area.x + area.width - 2, area.y, area.x
                        + area.width - 2, area.y + area.height - 1);
                gc.setForeground(ColorConstants.buttonLightest);
                gc.drawLine(area.x + 1, area.y + 1, area.x + area.width - 3,
                        area.y + 1);
                gc.drawLine(area.x + area.width - 1, area.y + 1, area.x
                        + area.width - 1, area.y + area.height - 1);
                gc.drawLine(area.x + 1, area.y + 1, area.x + 1, area.y
                        + area.height - 1);
            }
        });

        // Create the ToolBarManager and add all the actions to it
        ToolBarManager tbMgr = new ToolBarManager(SWT.FLAT | SWT.HORIZONTAL);
        List actions = getOutlineActions();
        for (int i = 0; i < actions.size(); i++) {
            tbMgr.add(new ToolbarDropdownContributionItem(((IAction) actions
                    .get(i))));
        }
        tbMgr.createControl(composite);
        tbMgr.getControl().setFont(composite.getFont());

        // By default, the ToolBarManager does not set text on ToolItems. Since,
        // we want to display the text, we will have to do it manually.
        ToolItem[] items = tbMgr.getControl().getItems();
        for (int i = 0; i < items.length; i++) {
            ToolItem item = items[i];
            item.setText(((IAction) actions.get(i)).getText());
        }

        return tbMgr.getControl();
    }

    /**
     * Creates the TreeViewer that is the outline of the model.
     * 
     * @param composite
     *            The Composite to which the ToolBar is to be added
     * @return The newly created TreeViewer
     */
    protected TreeViewer createOutlineTreeViewer(Composite composite) {
        Tree treeForViewer = new Tree(composite, SWT.BORDER);
        treeForViewer.setFont(composite.getFont());
        GridData data = new GridData(GridData.FILL_VERTICAL
                | GridData.HORIZONTAL_ALIGN_FILL);
        data.widthHint = 185;
        // Make the tree this tall even when there is nothing in it. This will
        // keep the
        // dialog from shrinking to an unusually small size.
        data.heightHint = 200;
        treeForViewer.setLayoutData(data);
        TreeViewer viewer = new TreeViewer(treeForViewer) {
            @Override
            protected void preservingSelection(Runnable updateCode) {
                if ((getTree().getStyle() & SWT.SINGLE) != 0)
                    updateCode.run();
                else
                    super.preservingSelection(updateCode);
            }
        };
        viewer.setContentProvider(new PaletteTreeProvider(viewer));
        treeViewerLabelProvider = new PaletteLabelProvider(viewer);
        viewer.setLabelProvider(treeViewerLabelProvider);
        viewer.setInput(getPaletteRoot());
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                handleOutlineSelectionChanged();
            }
        });

        return viewer;
    }

    /**
     * Creates the part of the dialog where the properties of the element
     * selected in the outline will be displayed.
     * 
     * <p>
     * The properties panel contains the following:
     * <UL>
     * <LI>Title ({@link #createPropertiesPanelTitle(Composite)})</LI>
     * </UL>
     * The rest of the panel is constructed in this method.
     * </p>
     * 
     * @param container
     *            The Composite to which this part is to be added
     * @return The properties panel
     */
    protected Control createPropertiesPanel(Composite container) {
        Composite composite = new Composite(container, SWT.NONE);
        composite.setFont(container.getFont());
        GridLayout layout = new GridLayout(1, false);
        layout.horizontalSpacing = 0;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = 0;
        composite.setLayout(layout);

        titleSwitcher = createPropertiesPanelTitle(composite);

        propertiesPanelContainer = new PageBook(composite, SWT.NONE);
        propertiesPanelContainer.setFont(composite.getFont());
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.FILL_VERTICAL);
        data.horizontalSpan = 2;
        propertiesPanelContainer.setLayoutData(data);
        propertiesPanelContainer.addListener(SWT.Resize, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (activePage != null) {
                    propertiesPanelContainer.layout();
                }
            }
        });

        return composite;
    }

    /**
     * Creates the title for the properties panel. It is a PageBook that can
     * switch between showing the regular title (the selected entry's label and
     * icon) and an error message if an error has occured.
     * 
     * @param parent
     *            The parent composite
     * @return The newly created PageBook title
     */
    protected PageBook createPropertiesPanelTitle(Composite parent) {
        GridLayout layout;
        PageBook book = new PageBook(parent, SWT.NONE);
        book.setFont(parent.getFont());
        book.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
                | GridData.VERTICAL_ALIGN_FILL));

        titlePage = new Composite(book, SWT.NONE);
        titlePage.setFont(book.getFont());
        layout = new GridLayout(2, false);
        layout.horizontalSpacing = 0;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = 0;
        titlePage.setLayout(layout);
        title = createSectionTitle(titlePage,
                PaletteMessages.NO_SELECTION_TITLE);

        errorPage = new Composite(book, SWT.NONE);
        errorPage.setFont(book.getFont());
        layout = new GridLayout(1, false);
        layout.horizontalSpacing = 0;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = 0;
        errorPage.setLayout(layout);
        Composite intermediary = new Composite(errorPage, SWT.NONE) {
            @Override
            public Point computeSize(int wHint, int hHint, boolean changed) {
                Rectangle bounds = title.getBounds();
                return new Point(bounds.width, bounds.height);
            }
        };
        intermediary.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
                | GridData.VERTICAL_ALIGN_FILL));
        StackLayout stackLayout = new StackLayout();
        intermediary.setLayout(stackLayout);
        errorTitle = new MultiLineLabel(intermediary);
        stackLayout.topControl = errorTitle;
        errorTitle.setImage(JFaceResources.getImage(DLG_IMG_MESSAGE_ERROR));
        errorTitle.setFont(errorPage.getFont());
        Label separator = new Label(errorPage, SWT.SEPARATOR | SWT.HORIZONTAL);
        separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        book.showPage(titlePage);
        return book;
    }

    /**
     * A convenient method to create CLabel titles (like the ones used in the
     * Preferences dialog in the Eclipse workbench) throughout the dialog.
     * 
     * @param composite
     *            The composite in which the title is to be created (it must
     *            have a GridLayout with two columns).
     * @param text
     *            The title to be displayed
     * @return The newly created CLabel for convenience
     */
    protected CLabel createSectionTitle(Composite composite, String text) {
        CLabel cTitle = new CLabel(composite, SWT.LEFT);
        Color background = JFaceColors.getBannerBackground(composite
                .getDisplay());
        Color foreground = JFaceColors.getBannerForeground(composite
                .getDisplay());
        JFaceColors.setColors(cTitle, foreground, background);
        cTitle.setFont(JFaceResources.getBannerFont());
        cTitle.setText(text);
        cTitle.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
                | GridData.VERTICAL_ALIGN_FILL));

        if (titleImage == null) {
            titleImage = new Image(composite.getDisplay(), ImageDescriptor
                    .createFromFile(Internal.class,
                            "icons/customizer_dialog_title.gif").getImageData()); //$NON-NLS-1$
            composite.addDisposeListener(new DisposeListener() {
                @Override
                public void widgetDisposed(DisposeEvent e) {
                    titleImage.dispose();
                    titleImage = null;
                }
            });
        }

        Label imageLabel = new Label(composite, SWT.LEFT);
        imageLabel.setBackground(background);
        imageLabel.setImage(titleImage);
        imageLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.VERTICAL_ALIGN_FILL));

        Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.horizontalSpan = 2;
        separator.setLayoutData(data);

        return cTitle;
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
     * @return The customizer that is responsible for handling the various tasks
     *         and updating the model.
     */
    protected PaletteCustomizer getCustomizer() {
        return customizer;
    }

    /**
     * Returns the <code>EntryPage</code> for the given
     * <code>PaletteEntry</code>. The <code>EntryPage</code> is retrieved from
     * the customizer. If the given entry is <code>null</code>,
     * <code>null</code> will be returned. If the customizer returns
     * <code>null</code> for the valid entry, a default page will be created and
     * returned.
     * 
     * @param entry
     *            The PaletteEntry whose properties need to be displayed
     * @return The EntryPage with the properties of the given PaletteEntry
     */
    protected EntryPage getEntryPage(PaletteEntry entry) {
        if (entry == null) {
            return null;
        }

        if (entriesToPages.containsKey(entry)) {
            return (EntryPage) entriesToPages.get(entry);
        }

        EntryPage page = getCustomizer().getPropertiesPage(entry);
        if (page == null) {
            page = new DefaultEntryPage();
        }
        page.createControl(propertiesPanelContainer, entry);
        page.setPageContainer(this);
        entriesToPages.put(entry, page);

        return page;
    }

    /**
     * Provides access to the actions that are used to manipulate the model. The
     * actions will be created, if they haven't been yet.
     * 
     * @return the list of <code>PaletteCustomizationAction</code>s
     * @see #createOutlineActions()
     */
    protected final List getOutlineActions() {
        if (actions == null) {
            actions = createOutlineActions();
        }
        return actions;
    }

    /**
     * Provides sub-classes with access to the PaletteRoot
     * 
     * @return the palette root
     */
    protected PaletteRoot getPaletteRoot() {
        return root;
    }

    /**
     * @return The PaletteEntry that is currently selected in the Outline Tree;
     *         <code>null</code> if none is selected
     */
    protected PaletteEntry getSelectedPaletteEntry() {
        TreeItem item = getSelectedTreeItem();
        if (item != null) {
            return (PaletteEntry) item.getData();
        }
        return null;

    }

    /**
     * @return The TreeItem that is currently selected in the Outline Tree;
     *         <code>null</code> if none is selected
     */
    protected TreeItem getSelectedTreeItem() {
        TreeItem[] items = tree.getSelection();
        if (items.length > 0) {
            return items[0];
        }
        return null;
    }

    /**
     * The <code>Widget</code>s that were created with a unique ID and added to
     * this class' internal map can be retrieved through this method.
     * 
     * @param id
     *            The unique ID of the Widget that you wish to retrieve
     * @return The Widget, if one with the given id exists; <code>null</code>
     *         otherwise
     */
    protected Widget getWidget(int id) {
        Widget widget = (Widget) widgets.get(new Integer(id));
        if (widget == null) {
            widget = super.getButton(id);
        }

        return widget;
    }

    /**
     * This method is invoked when the Apply button is pressed
     * <p>
     * IMPORTANT: It is recommended that you not override this method. Closing
     * the dialog with the 'X' at the top right of the window, or by hitting
     * 'Esc' or any other way, corresponds to a "Cancel." That will, however,
     * not result in this method being invoked. To handle such cases, saving or
     * rejecting the changes is handled in {@link #close()}. Override
     * {@link #save()} and {@link #revertToSaved()} to add to what needs to be
     * done when saving or cancelling.
     * </p>
     */
    protected final void handleApplyPressed() {
        save();
    }

    /**
     * This method is called when the "Delete" action is run (either through the
     * context menu or the toolbar). It deletes the selected palette entry.
     */
    protected void handleDelete() {
        getCustomizer().performDelete(getSelectedPaletteEntry());
        handleOutlineSelectionChanged();
    }

    /**
     * This method is called when the "Move Down" action is run (either through
     * the context menu or the toolbar). It moves the selected palette entry
     * down.
     */
    protected void handleMoveDown() {
        PaletteEntry entry = getSelectedPaletteEntry();
        getCustomizer().performMoveDown(entry);
        treeviewer.setSelection(new StructuredSelection(entry), true);
        updateActions();
    }

    /**
     * This method is called when the "Move Up" action is run (either through
     * the context menu or the toolbar). It moves the selected entry up.
     */
    protected void handleMoveUp() {
        PaletteEntry entry = getSelectedPaletteEntry();
        getCustomizer().performMoveUp(entry);
        treeviewer.setSelection(new StructuredSelection(entry), true);
        updateActions();
    }

    /**
     * This is the method that is called everytime the selection in the outline
     * (treeviewer) changes.
     */
    protected void handleOutlineSelectionChanged() {
        PaletteEntry entry = getSelectedPaletteEntry();

        if (activeEntry == entry) {
            return;
        }

        if (errorMessage != null) {
            MessageDialog dialog = new MessageDialog(getShell(),
                    PaletteMessages.ERROR,
                    null,
                    PaletteMessages.ABORT_PAGE_FLIPPING_MESSAGE
                            + "\n" + errorMessage, //$NON-NLS-1$
                    MessageDialog.ERROR,
                    new String[] { IDialogConstants.OK_LABEL }, 0);
            dialog.open();
            treeviewer.addPostSelectionChangedListener(pageFlippingPreventer);
        } else {
            setActiveEntry(entry);
        }
        updateActions();
    }

    /**
     * This method is invoked when the changes made since the last save need to
     * be cancelled.
     */
    protected void revertToSaved() {
        getCustomizer().revertToSaved();
    }

    /**
     * This method is invoked when the changes made since the last save need to
     * be saved.
     */
    protected void save() {
        if (activePage != null) {
            activePage.apply();
        }
        getCustomizer().save();
    }

    /**
     * This methods sets the active entry. Based on the selection, this method
     * will appropriately enable or disable the ToolBar items, will change the
     * CLabel heading of the propreties panel, and will show the properties of
     * the selected item in the properties panel.
     * 
     * @param entry
     *            The new active entry, i.e., the new selected entry (it can be
     *            <code>null</code>)
     */
    protected void setActiveEntry(PaletteEntry entry) {
        if (activeEntry != null) {
            activeEntry.removePropertyChangeListener(titleUpdater);
        }

        activeEntry = entry;

        if (entry != null) {
            title.setText(entry.getLabel());
            Image img = treeViewerLabelProvider.getImage(entry);
            if (img == null) {
                img = getSelectedTreeItem().getImage();
            }
            title.setImage(img);
            entry.addPropertyChangeListener(titleUpdater);
            EntryPage panel = getEntryPage(entry);
            setActiveEntryPage(panel);
        } else {
            title.setImage(null);
            title.setText(PaletteMessages.NO_SELECTION_TITLE);
            // Lazy creation
            if (noSelectionPage == null) {
                noSelectionPage = new EntryPage() {
                    private Text text;

                    @Override
                    public void apply() {
                    }

                    @Override
                    public void createControl(Composite parent,
                            PaletteEntry entry) {
                        text = new Text(parent, SWT.READ_ONLY);
                        text.setFont(parent.getFont());
                        text.setText(PaletteMessages.NO_SELECTION_MADE);
                    }

                    @Override
                    public Control getControl() {
                        return text;
                    }

                    @Override
                    public void setPageContainer(
                            EntryPageContainer pageContainer) {
                    }
                };
                noSelectionPage.createControl(propertiesPanelContainer, null);
            }
            setActiveEntryPage(noSelectionPage);
        }
    }

    /**
     * Sets the given EntryPage as the top page in the PageBook that shows the
     * properties of the item selected in the Outline. If the given EntryPage is
     * null, nothing will be shown.
     * 
     * @param page
     *            The EntryPage to be shown
     */
    protected void setActiveEntryPage(EntryPage page) {
        // Have the currently displayed page save its changes
        if (activePage != null) {
            activePage.apply();
        }

        if (page == null) {
            // No page available to display, so hide the panel container
            propertiesPanelContainer.setVisible(false);
        } else {
            // Show the page and grow the shell, if necessary, so that the page
            // is
            // completely visible
            Point oldSize = getShell().getSize();
            propertiesPanelContainer.showPage(page.getControl());

            /*
             * Fix for bug #34748 There's no need to resize the Shell if
             * initializeBounds() hasn't been called yet. It will automatically
             * resize the Shell so that everything fits in the Dialog. After
             * that, we can resize the Shell whenever there's an entry page that
             * cannot fit in the dialog.
             */
            if (!isSetup) {
                Point newSize = getShell().computeSize(SWT.DEFAULT,
                        SWT.DEFAULT, true);
                int x = newSize.x - oldSize.x;
                x = (x < 0) ? 0 : x;
                int y = newSize.y - oldSize.y;
                y = (y < 0) ? 0 : y;
                if (x > 0 || y > 0) {
                    getShell().setSize(oldSize.x + x, oldSize.y + y);
                }
            }

            // Show the property panel container if it was hidden
            if (!propertiesPanelContainer.isVisible()) {
                propertiesPanelContainer.setVisible(true);
            }
        }

        activePage = page;
    }

    /**
     * Sets the given PaletteEntry as the one to be selected when the dialog
     * opens. It is discarded when the dialog is closed.
     * 
     * @param entry
     *            The PaletteEntry that should be selected when the dialog is
     *            opened
     */
    public void setDefaultSelection(PaletteEntry entry) {
        initialSelection = entry;
    }

    /**
     * This method should be invoked by EntryPages when there is an error. It
     * will show the given error in the title of the properties panel. OK and
     * Apply buttons will be disabled. Selecting some other entry in the outline
     * tree will not be allowed until the error is fixed.
     * 
     * @see org.eclipse.gef.ui.palette.customize.EntryPageContainer#showProblem(String)
     */
    @Override
    public void showProblem(String error) {
        Assert.isNotNull(error);
        errorTitle.setText(error);
        titleSwitcher.showPage(errorPage);
        getButton(IDialogConstants.OK_ID).setEnabled(false);
        getButton(APPLY_ID).setEnabled(false);
        errorMessage = error;
    }

    /**
     * Updates the actions created in {@link #createOutlineActions()}, enabling
     * or disabling them as necessary.
     */
    protected void updateActions() {
        List actions = getOutlineActions();
        for (Iterator iter = actions.iterator(); iter.hasNext();) {
            PaletteCustomizationAction action = (PaletteCustomizationAction) iter
                    .next();
            action.update();
        }
    }

    /*
     * Delete Action
     */
    private class DeleteAction extends PaletteCustomizationAction {
        public DeleteAction() {
            setEnabled(false);
            setText(PaletteMessages.DELETE_LABEL);
            ISharedImages sharedImages = PlatformUI.getWorkbench()
                    .getSharedImages();
            setImageDescriptor(sharedImages
                    .getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
            setDisabledImageDescriptor(sharedImages
                    .getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
        }

        @Override
        public void run() {
            handleDelete();
        }

        @Override
        public void update() {
            boolean enabled = false;
            PaletteEntry entry = getSelectedPaletteEntry();
            if (entry != null) {
                enabled = getCustomizer().canDelete(entry);
            }
            setEnabled(enabled);
        }
    }

    /*
     * Move Down Action
     */
    private class MoveDownAction extends PaletteCustomizationAction {
        public MoveDownAction() {
            setEnabled(false);
            setText(PaletteMessages.MOVE_DOWN_LABEL);
            setImageDescriptor(ImageDescriptor.createFromFile(Internal.class,
                    "icons/next_nav.gif"));//$NON-NLS-1$
            setDisabledImageDescriptor(ImageDescriptor.createFromFile(
                    Internal.class, "icons/move_down_disabled.gif"));//$NON-NLS-1$
        }

        @Override
        public void run() {
            handleMoveDown();
        }

        @Override
        public void update() {
            boolean enabled = false;
            PaletteEntry entry = getSelectedPaletteEntry();
            if (entry != null) {
                enabled = getCustomizer().canMoveDown(entry);
            }
            setEnabled(enabled);
        }
    }

    /*
     * Move Up Action
     */
    private class MoveUpAction extends PaletteCustomizationAction {
        public MoveUpAction() {
            setEnabled(false);
            setText(PaletteMessages.MOVE_UP_LABEL);
            setImageDescriptor(ImageDescriptor.createFromFile(Internal.class,
                    "icons/prev_nav.gif"));//$NON-NLS-1$
            setDisabledImageDescriptor(ImageDescriptor.createFromFile(
                    Internal.class, "icons/move_up_disabled.gif")); //$NON-NLS-1$
        }

        @Override
        public void run() {
            handleMoveUp();
        }

        @Override
        public void update() {
            boolean enabled = false;
            PaletteEntry entry = getSelectedPaletteEntry();
            if (entry != null) {
                enabled = getCustomizer().canMoveUp(entry);
            }
            setEnabled(enabled);
        }
    }

    /*
     * New Action
     */
    private class NewAction extends PaletteCustomizationAction implements
            IMenuCreator {
        private List factories;
        private MenuManager menuMgr;

        public NewAction() {
            factories = wrap(getCustomizer().getNewEntryFactories());
            if (factories.isEmpty()) {
                setEnabled(false);
            } else {
                setMenuCreator(this);
            }

            setText(PaletteMessages.NEW_LABEL);
            setImageDescriptor(ImageDescriptor.createFromFile(Internal.class,
                    "icons/add.gif")); //$NON-NLS-1$
            setDisabledImageDescriptor(ImageDescriptor.createFromFile(
                    Internal.class, "icons/add-disabled.gif")); //$NON-NLS-1$
        }

        private void addActionToMenu(Menu parent, IAction action) {
            ActionContributionItem item = new ActionContributionItem(action);
            item.fill(parent, -1);
        }

        @Override
        public void dispose() {
            if (menuMgr != null) {
                menuMgr.dispose();
                menuMgr = null;
            }
        }

        @Override
        public Menu getMenu(Control parent) {
            // Create the menu manager and add all the NewActions to it
            if (menuMgr == null) {
                // Lazily create the manager
                menuMgr = new MenuManager();
                menuMgr.createContextMenu(parent);
            }

            updateMenuManager(menuMgr);
            return menuMgr.getMenu();
        }

        @Override
        public Menu getMenu(Menu parent) {
            Menu menu = new Menu(parent);
            for (Iterator iter = factories.iterator(); iter.hasNext();) {
                FactoryWrapperAction action = (FactoryWrapperAction) iter
                        .next();
                if (action.isEnabled()) {
                    addActionToMenu(menu, action);
                }
            }

            return menu;
        }

        @Override
        public void run() {
        }

        @Override
        public void update() {
            boolean enabled = false;
            PaletteEntry entry = getSelectedPaletteEntry();
            if (entry == null) {
                entry = getPaletteRoot();
            }
            // Enable or disable the FactoryWrapperActions
            for (Iterator iter = factories.iterator(); iter.hasNext();) {
                FactoryWrapperAction action = (FactoryWrapperAction) iter
                        .next();
                action.setEnabled(action.canCreate(entry));
                enabled = enabled || action.isEnabled();
            }

            // Enable this action IFF at least one of the new actions is enabled
            setEnabled(enabled);
        }

        protected void updateMenuManager(MenuManager manager) {
            manager.removeAll();
            for (Iterator iter = factories.iterator(); iter.hasNext();) {
                FactoryWrapperAction action = (FactoryWrapperAction) iter
                        .next();
                if (action.isEnabled()) {
                    manager.add(action);
                }
            }
        }

        private List wrap(List list) {
            List newList = new ArrayList();
            if (list != null) {
                for (Iterator iter = list.iterator(); iter.hasNext();) {
                    PaletteEntryFactory element = (PaletteEntryFactory) iter
                            .next();
                    newList.add(new FactoryWrapperAction(element));
                }
            }

            return newList;
        }
    }

    /*
     * FactoryWrapperAction class
     */
    private class FactoryWrapperAction extends Action {
        private PaletteEntryFactory factory;

        public FactoryWrapperAction(PaletteEntryFactory factory) {
            this.factory = factory;
            setText(factory.getLabel());
            setImageDescriptor(factory.getImageDescriptor());
            setHoverImageDescriptor(factory.getImageDescriptor());
        }

        public boolean canCreate(PaletteEntry entry) {
            return factory.canCreate(entry);
        }

        @Override
        public void run() {
            PaletteEntry selected = getSelectedPaletteEntry();
            if (selected == null)
                selected = getPaletteRoot();
            PaletteEntry newEntry = factory
                    .createNewEntry(getShell(), selected);
            treeviewer.setSelection(new StructuredSelection(newEntry), true);
            updateActions();
        }
    }

}
