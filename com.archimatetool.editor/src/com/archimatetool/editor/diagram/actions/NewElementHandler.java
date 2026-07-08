/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.BoldStylerProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageGcDrawer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SearchPattern;
import org.eclipse.ui.dialogs.StyledStringHighlighter;
import org.eclipse.ui.handlers.HandlerUtil;

import com.archimatetool.editor.diagram.ArchimateDiagramModelFactory;
import com.archimatetool.editor.diagram.IArchimateDiagramEditor;
import com.archimatetool.editor.diagram.commands.CreateDiagramArchimateObjectCommand;
import com.archimatetool.editor.diagram.commands.CreateDiagramObjectCommand;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * Add a new element to a diagram with a popup dialog.
 * 
 * The dialog and text filter part of this code is taken from:
 * org.eclipse.ui.internal.FilteredTableBaseHandler
 * org.eclipse.ui.internal.WorkbookEditorsHandler
 */
public class NewElementHandler extends AbstractHandler {
    
    public static final String ID = "com.archimatetool.editor.action.newElement"; //$NON-NLS-1$
    
    private static final int MAX_ITEMS = 15;
    
    private Shell dialog;
    private Text text;
    private TableViewer tableViewer;
    private Table table;
    
    private IArchimateDiagramEditor diagramEditor;
    private SearchPattern searchPattern;
    private Point cursorLocation;
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // Guard against creating a new dialog if there is already one created, and active part has to be ArchiMate Editor
        if(dialog != null || !(HandlerUtil.getActivePart(event) instanceof IArchimateDiagramEditor editor)) {
            return null;
        }
        
        Control viewerControl = editor.getGraphicalViewer().getControl();
        
        // If x,y is set in the trigger Event it was triggered from the editor
        if(event.getTrigger() instanceof Event e && e.x != 0 && e.y != 0) {
            cursorLocation = viewerControl.toDisplay(e.x, e.y);
        }
        // Else the trigger was the shortcut key so get display cursor location if in viewer control
        else {
            cursorLocation = getCursorLocation(viewerControl);
            if(cursorLocation == null) { // Not in viewer control
                return null;
            }
        }
        
        dialog = new Shell(viewerControl.getShell(), SWT.MODELESS); // Get parent shell from GraphicalViewer in case we are in full screen mode on Windows
        dialog.setBackground(getBackground());
        dialog.setMinimumSize(new Point(120, 50));
        dialog.setLayout(new FillLayout());
        dialog.addFocusListener(focusAdapter);
        
        dialog.addDisposeListener(e -> {
            diagramEditor = null;
            dialog = null;
            text = null;
            tableViewer = null;
            table = null;
            searchPattern = null;
        });
        
        Composite composite = new Composite(dialog, SWT.NONE);
        composite.setBackground(getBackground());
        GridLayout layout = new GridLayout(1, false);
        composite.setLayout(layout);
        
        text = new Text(composite, SWT.NONE);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        text.setBackground(getBackground());
        text.setFocus();
        text.addFocusListener(focusAdapter);
        text.setMessage(Messages.NewElementHandler_0);
        text.setText(""); //$NON-NLS-1$
        
        // for issues with dark theme, don't use SWT.SEPARATOR as style
        Label labelSeparator = new Label(composite, SWT.HORIZONTAL);
        Image separatorBgImage = createSeparatorBgImage();
        labelSeparator.setBackgroundImage(separatorBgImage);
        labelSeparator.addDisposeListener(e -> separatorBgImage.dispose());
        GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_label.heightHint = 1;
        labelSeparator.setLayoutData(gd_label);

        tableViewer = new TableViewer(composite, SWT.SINGLE | SWT.FULL_SELECTION);
        table = tableViewer.getTable();
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        table.setBackground(getBackground());
        table.addFocusListener(focusAdapter);
        
        TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        tableViewerColumn.setLabelProvider(getLabelProvider());
        
        TableColumn tc = tableViewerColumn.getColumn();
        tc.setResizable(false);
        
        tableViewer.setContentProvider(ArrayContentProvider.getInstance());
        tableViewer.addFilter(getFilter());
        
        tableViewer.addDoubleClickListener(e -> {
            Object object = tableViewer.getStructuredSelection().getFirstElement();
            if(object != null) {
                createNewElement(editor, object);
            }
        });

        addModifyListener(text);
        addKeyListener(text);
        addKeyListener(table);
        
        List<Object> templates = new ArrayList<>();

        Collections.addAll(templates, ArchimateModelUtils.getOtherClasses());
        Collections.addAll(templates, ArchimateModelUtils.getStrategyClasses());
        Collections.addAll(templates, ArchimateModelUtils.getBusinessClasses());
        Collections.addAll(templates, ArchimateModelUtils.getApplicationClasses());
        Collections.addAll(templates, ArchimateModelUtils.getTechnologyClasses());
        Collections.addAll(templates, ArchimateModelUtils.getPhysicalClasses());
        Collections.addAll(templates, ArchimateModelUtils.getMotivationClasses());
        Collections.addAll(templates, ArchimateModelUtils.getImplementationMigrationClasses());
        Collections.addAll(templates, ArchimateModelUtils.getConnectorClasses());

        templates.add(IArchimatePackage.eINSTANCE.getDiagramModelNote());
        templates.add(IArchimatePackage.eINSTANCE.getDiagramModelGroup());
        templates.add(IDiagramModelNote.LEGEND_MODEL_NAME); // Legend is a string
        
        tableViewer.setInput(templates);
        
        table.setSelection(0);

        tc.pack();
        table.pack();
        dialog.pack();
        
        Rectangle tableBounds = table.getBounds();
        tableBounds.height = Math.min(tableBounds.height, table.getItemHeight() * MAX_ITEMS);
        table.setBounds(tableBounds);
        
        Rectangle dialogBounds = dialog.getBounds();
        dialogBounds.height = text.getBounds().height + labelSeparator.getBounds().height + tableBounds.height
                + layout.marginHeight * 2 + layout.verticalSpacing * 2;
        dialog.setBounds(dialogBounds);
        tc.setWidth(table.getClientArea().width);
        
        diagramEditor = editor;
        
        setDialogLocation(dialog);
        dialog.open();
        
        return null;
    }
    
    private void createNewElement(IArchimateDiagramEditor editor, Object template) {
        // Close the dialog first
        dialog.close();
        
        if(editor == null || template == null) {
            return;
        }
        
        GraphicalViewer viewer = editor.getGraphicalViewer();
        
        // Translate x,y to viewer control
        Point ptToControl = viewer.getControl().toControl(cursorLocation);
        org.eclipse.draw2d.geometry.Point pt = new org.eclipse.draw2d.geometry.Point(ptToControl.x, ptToControl.y);
        
        // Get the edit part onto which to create the element
        GraphicalEditPart editPart = (GraphicalEditPart)editor.getGraphicalViewer().findObjectAt(pt);
        
        // If the edit part is selected and is a container type use that else use root
        if(editPart == null || editPart.getSelected() == EditPart.SELECTED_NONE || !(editPart.getModel() instanceof IDiagramModelContainer)) {
            editPart = (GraphicalEditPart)viewer.getRootEditPart().getContents(); // ArchimateDiagramPart
        }

        // Translate x,y to relative and then parent
        editPart.getFigure().translateToRelative(pt);
        editPart.getFigure().translateFromParent(pt);
        
        // Figure bounds
        org.eclipse.draw2d.geometry.Rectangle rect = new org.eclipse.draw2d.geometry.Rectangle(pt.x, pt.y, -1, -1);
        
        // CreateRequest
        CreateRequest request = new CreateRequest();
        
        // EClass or Legend
        boolean isLegend = template == IDiagramModelNote.LEGEND_MODEL_NAME;
        EClass eClass = isLegend ? IArchimatePackage.eINSTANCE.getDiagramModelNote() : (EClass)template;
        
        // Set CreationFactory
        request.setFactory(new ArchimateDiagramModelFactory(eClass, isLegend ? IDiagramModelNote.LEGEND_MODEL_NAME : null));
        
        // Create Command and execute it
        Command cmd = IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(eClass) ? 
                new CreateDiagramArchimateObjectCommand(editPart, request, rect) : new CreateDiagramObjectCommand(editPart, request, rect);
        CommandStack stack = editor.getAdapter(CommandStack.class);
        stack.execute(cmd);
    }
    
    /**
     * Get the cursor location from the display or null if the cursor is not inside the viewerControl
     */
    private Point getCursorLocation(Control viewerControl) {
        Point cursorLocation = viewerControl.getDisplay().getCursorLocation();
        Point pt = viewerControl.toControl(cursorLocation);
        return viewerControl.getBounds().contains(pt) ? new Point(cursorLocation.x, cursorLocation.y) : null;
    }
    
    private void setDialogLocation(Shell dialog) {
        Rectangle monitorBounds = dialog.getMonitor().getBounds();
        Rectangle dialogBounds = dialog.getBounds();
        
        int posX = cursorLocation.x;
        int posY = cursorLocation.y;

        // Too low
        if(posY + dialogBounds.height > monitorBounds.y + monitorBounds.height) {
            posY = monitorBounds.y + monitorBounds.height - dialogBounds.height;
        }

        // Too far right
        if(posX + dialogBounds.width > monitorBounds.x + monitorBounds.width) {
            posX = monitorBounds.x + monitorBounds.width - dialogBounds.width;
        }
        
        dialog.setLocation(posX, posY);
    }
    
    private Color getBackground() {
        return dialog.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
    }
    
    private final FocusListener focusAdapter = FocusListener.focusLostAdapter(e -> {
        PlatformUI.getWorkbench().getDisplay().asyncExec(() -> {
            // once event completes, if dialog still open...
            if(dialog == null || dialog.isDisposed()) {
                return;
            }
            
            // check if the focus is still in dialog elements
            Control fc = dialog.getDisplay().getFocusControl();
            if(fc != text && fc != table && fc != dialog) {
                dialog.close();
            }
        });
    });

    /**
     * Build a 1x1 px gray image to be used as separator. This color, halfway
     * between white and black, looks good both in Light and in Dark Theme
     */
    private Image createSeparatorBgImage() {
        ImageGcDrawer imageGcDrawer = (gc, width, height) -> {
            gc.setBackground(new Color(127, 127, 127));
            gc.fillRectangle(0, 0, width, height);
        };
        
        return new Image(Display.getDefault(), imageGcDrawer, 1, 1);
    }
    
    private CellLabelProvider getLabelProvider() {
        return new StyledCellLabelProvider() {
            private BoldStylerProvider boldStylerProvider;
            
            @Override
            public void update(ViewerCell cell) {
                Object element = cell.getElement();
                String text = getLabel(element);
                cell.setText(text);
                cell.setImage(getImage(element));
                
                SearchPattern matcher = getMatcher();
                if(matcher == null) {
                    cell.setStyleRanges(null);
                }
                else {
                    String pattern = matcher.getPattern();
                    StyledStringHighlighter ssh = new StyledStringHighlighter();
                    StyledString ss = ssh.highlight(text, pattern, getBoldStylerProvider().getBoldStyler());
                    cell.setStyleRanges(ss.getStyleRanges());
                }
            }
            
            private BoldStylerProvider getBoldStylerProvider() {
                if(boldStylerProvider == null) {
                    boldStylerProvider = new BoldStylerProvider(table.getFont());
                }
                return boldStylerProvider;
            }

            @Override
            public void dispose() {
                super.dispose();
                
                if(boldStylerProvider != null) {
                    boldStylerProvider.dispose();
                    boldStylerProvider = null;
                }
            }
        };
    }
    
    private String getLabel(Object object) {
        return object instanceof EClass eClass ? ArchiLabelProvider.INSTANCE.getDefaultName(eClass) : "Legend"; //$NON-NLS-1$
    }
    
    private Image getImage(Object object) {
        return object instanceof EClass eClass ? ArchiLabelProvider.INSTANCE.getImage(eClass) :
                                                 IArchiImages.ImageFactory.getImage(IArchiImages.ICON_LEGEND);
    }
    
    private ViewerFilter getFilter() {
        return new ViewerFilter() {
            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                SearchPattern matcher = getMatcher();
                return matcher == null ? true : matcher.matches(getLabel(element));
            }
        };
    }
    
    private SearchPattern getMatcher() {
        return searchPattern;
    }
    
    private void setMatcherString(String pattern) {
        if(pattern.isEmpty()) {
            searchPattern = null;
        }
        else {
            searchPattern = new SearchPattern(SearchPattern.RULE_SUBSTRING_MATCH | SearchPattern.DEFAULT_MATCH_RULES);
            searchPattern.setPattern(pattern);
        }
    }
    
    private void addModifyListener(Text text) {
        text.addModifyListener(e -> {
            setMatcherString(text.getText());
            tableViewer.refresh();
            table.setSelection(0);
        });
    }
    
    private void addKeyListener(Control control) {
        control.addKeyListener(KeyListener.keyPressedAdapter(e -> {
            switch(e.keyCode) {
                case SWT.ARROW_DOWN -> {
                    moveForward(e);
                }
                case SWT.ARROW_UP -> {
                    moveBackward(e);
                }
                case SWT.CR, SWT.KEYPAD_CR -> {
                    if(text.isFocusControl() && table.getItems().length == 1) {
                        createNewElement(diagramEditor, table.getItems()[0].getData());
                    }
                }
            }
        }));
    }
    
    private void moveForward(KeyEvent e) {
        if(text.isFocusControl()) {
            table.setFocus();
            table.setSelection(0);
        }
        else if(table.getSelectionIndex() == table.getItemCount() - 1) {
            table.setSelection(0);
            text.setFocus();
            e.doit = false; // have to do this otherwise table selection is 1 not 0
        }
    }

    private void moveBackward(KeyEvent e) {
        if(text.isFocusControl()) {
            table.setFocus();
            table.setSelection(table.getItemCount() - 1);
        }
        else if(table.getSelectionIndex() == 0) {
            text.setFocus();
        }
    }
}
