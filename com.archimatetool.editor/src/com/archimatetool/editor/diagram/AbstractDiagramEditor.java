/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GraphicsSource;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.internal.InternalGEFPlugin;
import org.eclipse.gef.palette.PaletteListener;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.MatchHeightAction;
import org.eclipse.gef.ui.actions.MatchWidthAction;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.actions.BorderColorAction;
import com.archimatetool.editor.diagram.actions.BringForwardAction;
import com.archimatetool.editor.diagram.actions.BringToFrontAction;
import com.archimatetool.editor.diagram.actions.ConnectionLineWidthAction;
import com.archimatetool.editor.diagram.actions.ConnectionRouterAction;
import com.archimatetool.editor.diagram.actions.CopyAction;
import com.archimatetool.editor.diagram.actions.CutAction;
import com.archimatetool.editor.diagram.actions.DefaultEditPartSizeAction;
import com.archimatetool.editor.diagram.actions.ExportAsImageAction;
import com.archimatetool.editor.diagram.actions.ExportAsImageToClipboardAction;
import com.archimatetool.editor.diagram.actions.FillColorAction;
import com.archimatetool.editor.diagram.actions.FontAction;
import com.archimatetool.editor.diagram.actions.FontColorAction;
import com.archimatetool.editor.diagram.actions.FullScreenAction;
import com.archimatetool.editor.diagram.actions.LineColorAction;
import com.archimatetool.editor.diagram.actions.LockObjectAction;
import com.archimatetool.editor.diagram.actions.OpacityAction;
import com.archimatetool.editor.diagram.actions.OutlineOpacityAction;
import com.archimatetool.editor.diagram.actions.PasteAction;
import com.archimatetool.editor.diagram.actions.PasteSpecialAction;
import com.archimatetool.editor.diagram.actions.PrintDiagramAction;
import com.archimatetool.editor.diagram.actions.PropertiesAction;
import com.archimatetool.editor.diagram.actions.ResetAspectRatioAction;
import com.archimatetool.editor.diagram.actions.SelectAllAction;
import com.archimatetool.editor.diagram.actions.SelectElementInTreeAction;
import com.archimatetool.editor.diagram.actions.SendBackwardAction;
import com.archimatetool.editor.diagram.actions.SendToBackAction;
import com.archimatetool.editor.diagram.actions.TextAlignmentAction;
import com.archimatetool.editor.diagram.actions.TextPositionAction;
import com.archimatetool.editor.diagram.actions.ToggleGridEnabledAction;
import com.archimatetool.editor.diagram.actions.ToggleGridVisibleAction;
import com.archimatetool.editor.diagram.actions.ToggleSnapToAlignmentGuidesAction;
import com.archimatetool.editor.diagram.actions.ZoomNormalAction;
import com.archimatetool.editor.diagram.dnd.PaletteTemplateTransferDropTargetListener;
import com.archimatetool.editor.diagram.editparts.ExtendedScalableFreeformRootEditPart;
import com.archimatetool.editor.diagram.figures.ITextFigure;
import com.archimatetool.editor.diagram.tools.FormatPainterInfo;
import com.archimatetool.editor.diagram.tools.FormatPainterToolEntry;
import com.archimatetool.editor.diagram.tools.MouseWheelHorizontalScrollHandler;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.services.ComponentSelectionManager;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.ui.textrender.TextRenderer;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.util.LightweightAdapter;



/**
 * Abstract GEF Diagram Editor that checks for valid Editor Input.
 * If the Editor Input is of type NullDiagramEditorInput it shows a warning message.
 * This can happen when Eclipse tries to restore an Editor Part and the Diagram Model cannot be restored
 * because the model's file may have been deleted, renamed or moved on the file system.
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("restriction")
public abstract class AbstractDiagramEditor extends GraphicalEditorWithFlyoutPalette
implements IDiagramModelEditor, IContextProvider, ITabbedPropertySheetPageContributor {

    /*
     * Error handlers
     */
    private Composite fErrorComposite;
    private NullDiagramEditorInput fNullInput;
    
    /**
     * Graphics Model
     */
    protected IDiagramModel fDiagramModel;
    
    /**
     * Actions that need to be updated after CommandStack changed
     */
    protected List<UpdateAction> fUpdateCommandStackActions = new ArrayList<UpdateAction>();
    
    /**
     * Listen to User Preferences Changes
     */
    protected IPropertyChangeListener appPreferencesListener = this::applicationPreferencesChanged;
    
    /**
     * Listen to ecore changes for model/view name change
     */
    private LightweightAdapter eCoreAdapter = new LightweightAdapter(this::notifyChanged);
    
    /**
     * Application Preference changed
     * @param event
     */
    protected void applicationPreferencesChanged(PropertyChangeEvent event) {
        if(IPreferenceConstants.GRID_SIZE == event.getProperty()) {
            applyUserGridPreferences();
        }
        else if(IPreferenceConstants.GRID_VISIBLE == event.getProperty()) {
            applyUserGridPreferences();
        }
        else if(IPreferenceConstants.GRID_SNAP == event.getProperty()) {
            applyUserGridPreferences();
        }
        else if(IPreferenceConstants.GRID_SHOW_GUIDELINES == event.getProperty()) {
            applyUserGridPreferences();
        }
    }
    
    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        if(input instanceof NullDiagramEditorInput) {
            fNullInput = (NullDiagramEditorInput)input;
            super.setSite(site);
            super.setInput(input); // Make sure to call super.setInput(input)
            setPartName(input.getName());
        }
        else {
            super.init(site, input);
        }
    }
    
    @Override
    public void setInput(IEditorInput input) {
        super.setInput(input);
        
        // This first - set model
        fDiagramModel = ((DiagramEditorInput)input).getDiagramModel();
        
        // Listen to notifications from diagram model and main model for name changes to update part
        eCoreAdapter.add(fDiagramModel, fDiagramModel.getArchimateModel());
        
        // Edit Domain before init
        // Use CommandStack from Model
        DefaultEditDomain domain = new DefaultEditDomain(this) {
            private CommandStack stack;
            
            @Override
            public CommandStack getCommandStack() {
                if(stack == null) {
                    stack = (CommandStack)fDiagramModel.getAdapter(CommandStack.class);
                }
                return stack;
            }
        };
        
        setEditDomain(domain);
        
        // Part Name
        setPartName(input.getName());

        // Listen to App Prefs changes
        Preferences.STORE.addPropertyChangeListener(appPreferencesListener);
    }

    @Override
    public void createPartControl(Composite parent) {
        // Show error message
        if(fNullInput != null) {
            createErrorComposite(parent);
        }
        else {
            super.createPartControl(parent);
            doCreatePartControl(parent);
        }
    }
    
    /**
     * Create the Error composite messate
     * @param parent
     */
    protected void createErrorComposite(Composite parent) {
        fErrorComposite = new Composite(parent, SWT.NULL);
        fErrorComposite.setLayout(new GridLayout());
        fErrorComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        String errorMessage = Messages.AbstractDiagramEditor_0 + " "; //$NON-NLS-1$
        
        String fileName = fNullInput.getFileName();
        if(fileName != null) {
            if(!new File(fileName).exists()) {
                errorMessage += NLS.bind(Messages.AbstractDiagramEditor_1, fileName);
            }
            else {
                errorMessage += NLS.bind(Messages.AbstractDiagramEditor_3, fileName);
            }
        }

        CLabel imageLabel = new CLabel(fErrorComposite, SWT.NULL);
        imageLabel.setImage(Display.getDefault().getSystemImage(SWT.ICON_INFORMATION));
        imageLabel.setText(errorMessage);
    }
    
    @Override
    public IDiagramModel getModel() {
        return fDiagramModel;
    }
    
    /**
     * Do the createPartControl(Composite parent) method
     */
    protected abstract void doCreatePartControl(Composite parent);
    
    /**
     * Register a context menu
     */
    protected abstract void registerContextMenu(GraphicalViewer viewer);
    
    /**
     * Create the Root Edit Part
     */
    protected void createRootEditPart(GraphicalViewer viewer) {
        viewer.setRootEditPart(new ExtendedScalableFreeformRootEditPart());
    }
    
    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();

        GraphicalViewer viewer = getGraphicalViewer();
        
        // Key handler
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        
        // Context menu
        registerContextMenu(viewer);
        
        // Set the Root Edit Part *before* Actions as the Edit Part will create and register a new ZoomManager
        createRootEditPart(viewer);
        
        // Create Actions after Viewer created and after Root Edit Part set
        createActions(viewer);
        
        // Create a drop target listener for this palette viewer
        // this will enable model element creation by dragging a CombinatedTemplateCreationEntries 
        // from the palette into the editor
        viewer.addDropTargetListener(new PaletteTemplateTransferDropTargetListener(this));
        
        // Set some Properties
        setProperties();
        
        // Listen to selections
        hookSelectionListener();
        
        // If on Wayland double-click open requests are not forwarded so hook in here
        if(PlatformUtils.isLinuxWayland()) {
            viewer.getControl().addListener(SWT.MouseDoubleClick, event -> {
                EditPart editPart = viewer.findObjectAt(new Point(event.x, event.y));
                if(editPart != null ) {
                    SelectionRequest request = new SelectionRequest();
                    request.setType(RequestConstants.REQ_OPEN);
                    request.setLocation(new Point(event.x, event.y));
                    request.setModifiers(event.stateMask);
                    request.setLastButtonPressed(event.button);
                    editPart.performRequest(request);
                }
            });
        }
    }
    
    @Override
    protected void createGraphicalViewer(Composite parent) {
        // NOTE from Phillipus - the bug only seems to affect Windows and only when dragging from
        // a source that isn't the diagram (Model Tree, Table, File, etc).
        // I can't remember where the following hack came from, as I didn't devise it.
        if(!PlatformUtils.isWindows()) {
            super.createGraphicalViewer(parent);
            return;
        }

        // Hack: specialize the GraphicsSource to avoid calling the
        // Canvas#update() method in the GraphicsSource#getGraphics(Rectangle) method.
        // This hack is needed to avoid SWT/GEF bug 137786
        // (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=137786) where drag over
        // feedback (ie the drag feedback provided by the drag source) and drag under
        // feedback (ie the feedback provided by the drop target) interfere with each
        // other, causing flickering and more importantly ugly graphical artifacts.

        // In order to be able to specialize the GraphicsSource, we need to specialize
        // the LightWeightSystem, and to do that we need in turn to specialize the GraphicalViewer.
        final GraphicalViewer viewer = new ScrollingGraphicalViewer() {
            @Override
            protected LightweightSystem createLightweightSystem() {
                return new LightweightSystem() {
                    @Override
                    public void setControl(final Canvas c) {
                        super.setControl(c);
                        this.getUpdateManager().setGraphicsSource(new GraphicsSource() {
                            @Override
                            public Graphics getGraphics(Rectangle r) {
                                c.redraw(r.x, r.y, r.width, r.height, false);
                                // The actual hack is the following code line:
                                // In original GEF code a call is made to the #update() method of the c Canvas.
                                // But calling #update() at this point causes SWT to redraw the drag over
                                // feedback which in turn causes GEF to redraw the drag under feedback etc.
                                // The final result is flickering (because of constant erase and redraw) and graphical artifacts.
                                // Commenting this line however seems to have no side effect (so far).
                                //c.update();
                                return null;
                            }
        
                            @Override
                            public void flushGraphics(Rectangle region) {
                                // Nothing to do.
                            }
                        });
                    }
                };
            }
        };

        viewer.createControl(parent);
        setGraphicalViewer(viewer);
        configureGraphicalViewer();
        hookGraphicalViewer();
        initializeGraphicalViewer();
    }

    private void hookSelectionListener() {
        getGraphicalViewer().addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                // Update status bar on selection
                Object selected = ((IStructuredSelection)event.getSelection()).getFirstElement();
                updateStatusBarWithSelection(selected);
            }
        });
    }
    
    @Override
    public void setFocus() {
        if(fNullInput != null) {
            fErrorComposite.setFocus();
        }
        else {
            super.setFocus();
            updateShellTitleBarWithFileName(); // Shell title
        }
    }
    
    /**
     * Update Status Bar with selected image and text
     * @param selected
     */
    protected void updateStatusBarWithSelection(Object selected) {
        IStatusLineManager status = getEditorSite().getActionBars().getStatusLineManager();
        
        if(selected instanceof EditPart) {
            selected = ((EditPart)selected).getModel();
            Image image = ArchiLabelProvider.INSTANCE.getImage(selected);
            String text = ArchiLabelProvider.INSTANCE.getLabelNormalised(selected);
            status.setMessage(image, text);
        }
        else {
            status.setMessage(null, ""); //$NON-NLS-1$
        }
    }
    
    /**
     * Update Shell title bar with file name of current model
     */
    protected void updateShellTitleBarWithFileName() {
        String appname = Platform.getProduct().getName();
        File file = getModel().getArchimateModel().getFile();
        
        if(file != null) {
            getEditorSite().getShell().setText(appname + " - " + file.getPath()); //$NON-NLS-1$
        }
        else {
            getEditorSite().getShell().setText(appname);
        }
    }
    
    @Override
    public GraphicalViewer getGraphicalViewer() {
        return super.getGraphicalViewer();
    }
    
    @Override
    protected DefaultEditDomain getEditDomain() {
        if(fNullInput != null) {
            return new DefaultEditDomain(this);
        }
        else {
            return super.getEditDomain();
        }
    }
    
    /**
     * Set Graphical Properties
     */
    protected void setProperties() {
        // Grid Preferences
        applyUserGridPreferences();
        
        // Ctrl + Scroll wheel Zooms
        getGraphicalViewer().setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);
        
        // Shift + Scroll wheel horizontal scroll
        getGraphicalViewer().setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD2), MouseWheelHorizontalScrollHandler.SINGLETON);
    }

    /**
     * Apply grid Prefs
     */
    protected void applyUserGridPreferences() {
        // Grid Spacing
        int gridSize = Preferences.getGridSize();
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(gridSize, gridSize));
        
        // Grid Visible
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, Preferences.isGridVisible());
        
        // Grid Enabled
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, Preferences.isGridSnap());

        // Snap to Guidelines
        getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, Preferences.doShowGuideLines());
    }

    /**
     * Create the PaletteViewerProvider.
     * Over-ride this so we can hook into the creation of the PaletteViewer.
     */
    @Override
    protected PaletteViewerProvider createPaletteViewerProvider() {
        // Ensure palette is showing or not
        boolean showPalette = Preferences.doShowPalette();
        getPalettePreferences().setPaletteState(showPalette ? FlyoutPaletteComposite.STATE_PINNED_OPEN : FlyoutPaletteComposite.STATE_COLLAPSED);

        return new PaletteViewerProvider(getEditDomain()) {
            @Override
            protected void hookPaletteViewer(PaletteViewer viewer) {
                super.hookPaletteViewer(viewer);
                AbstractDiagramEditor.this.configurePaletteViewer(viewer);
            }
        };
    }
    
    /**
     * Configure the Palette Viewer
     */
    protected void configurePaletteViewer(final PaletteViewer viewer) {
        PaletteViewerPreferences prefs = viewer.getPaletteViewerPreferences();
        
        // First time use so set to icons layout
        if(!InternalGEFPlugin.getDefault().getPreferenceStore().getBoolean("com.archimatetool.paletteSet")) { //$NON-NLS-1$
            InternalGEFPlugin.getDefault().getPreferenceStore().setValue("com.archimatetool.paletteSet", true); //$NON-NLS-1$
            prefs.setLayoutSetting(PaletteViewerPreferences.LAYOUT_ICONS);
            prefs.setCurrentUseLargeIcons(false);
        }
        
        // Register as drag source to drag onto the canvas
        viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));

        /*
         * Tool Changed
         */
        viewer.addPaletteListener(new PaletteListener() {
            @Override
            public void activeToolChanged(PaletteViewer palette, ToolEntry toolEntry) {
                CreationFactory factory = (CreationFactory)toolEntry.getToolProperty(CreationTool.PROPERTY_CREATION_FACTORY);
                if(factory != null) {
                    ComponentSelectionManager.INSTANCE.fireSelectionEvent(toolEntry, factory.getObjectType());
                }
            }
        });
        
        /*
         * Mouse Hover
         */
        viewer.getControl().addMouseTrackListener(new MouseTrackAdapter() {
            @Override
            public void mouseHover(MouseEvent e) {
                ToolEntry toolEntry = findToolEntryAt(viewer, new Point(e.x, e.y));
                if(toolEntry != null) {
                    CreationFactory factory = (CreationFactory)toolEntry.getToolProperty(CreationTool.PROPERTY_CREATION_FACTORY);
                    if(factory != null) {
                        ComponentSelectionManager.INSTANCE.fireSelectionEvent(toolEntry, factory.getObjectType());
                    }
                }
            }
        });
        
        viewer.getControl().addMouseListener(new MouseAdapter() {
            /*
             * If Shift key is pressed set Tool Entry to unload or not
             */
            @Override
            public void mouseDown(MouseEvent e) {
                ToolEntry toolEntry = findToolEntryAt(viewer, new Point(e.x, e.y));
                if(toolEntry != null) {
                    boolean shiftKey = (e.stateMask & SWT.SHIFT) != 0;
                    toolEntry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, !shiftKey);
                }
            }
            
            /*
             * Double-click on Format Painter
             */
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                ToolEntry toolEntry = findToolEntryAt(viewer, new Point(e.x, e.y));
                if(toolEntry instanceof FormatPainterToolEntry) {
                    FormatPainterInfo.INSTANCE.reset();
                }
            }
        });
    }
    
    /**
     * Find a Tool Entry on the palette at point, or return null
     */
    private ToolEntry findToolEntryAt(PaletteViewer viewer, Point pt) {
        EditPart ep = viewer.findObjectAt(pt);
        if(ep != null && ep.getModel() instanceof ToolEntry) {
            return (ToolEntry)ep.getModel();
        }
        return null;
    }

    @Override
    public void commandStackChanged(EventObject event) {
        super.commandStackChanged(event);
        updateCommandStackActions(); // Need to update these too
        setDirty(getCommandStack().isDirty());
        
        refreshFiguresWithLabelFeature(); // Refresgh Figures with Label Features
    }
    
    /**
     * Update those actions that need updating when the Command Stack changes
     */
    protected void updateCommandStackActions() {
        // If not the active editor, ignore changed.
        if(this.equals(getSite().getPage().getActiveEditor())) {
            for(UpdateAction action : getUpdateCommandStackActions()) {
                action.update();
            }
        }
    }
    
    protected List<UpdateAction> getUpdateCommandStackActions() {
        return fUpdateCommandStackActions;
    }
    
    /**
     * Refresh all figures with label features
     */
    protected void refreshFiguresWithLabelFeature() {
        for(Object editPart : getGraphicalViewer().getEditPartRegistry().values()) {
            if(editPart instanceof GraphicalEditPart) {
                IFigure figure = ((GraphicalEditPart)editPart).getFigure();
                Object model = ((GraphicalEditPart)editPart).getModel();

                // If it is a text figure and has a label render feature update text
                if(model instanceof IDiagramModelComponent
                                        && TextRenderer.getDefault().hasFormatExpression((IDiagramModelComponent)model)
                                        && figure instanceof ITextFigure) {
                    ((ITextFigure)figure).setText();
                }
            }
        }
    }
    
    @Override
    public void doSave(IProgressMonitor monitor) {
        // Save happens in SaveAction class
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    protected void setDirty(boolean dirty) {
        firePropertyChange(IEditorPart.PROP_DIRTY);
    }

    @Override
    public boolean isSaveOnCloseNeeded() {
        return false;
    }
    
    /**
     * Add some extra Actions - *after* the graphical viewer has been created
     */
    @SuppressWarnings("unchecked")
    protected void createActions(GraphicalViewer viewer) {
        ActionRegistry registry = getActionRegistry();
        IAction action;
        
        // Zoom Manager tweaking
        ZoomManager zoomManager = (ZoomManager)getAdapter(ZoomManager.class);
        double[] zoomLevels = { .25, .5, .75, 1, 1.5, 2, 3, 4, 6, 8 };
        zoomManager.setZoomLevels(zoomLevels);
        List<String> zoomContributionLevels = new ArrayList<String>();
        zoomContributionLevels.add(ZoomManager.FIT_ALL);
        zoomContributionLevels.add(ZoomManager.FIT_WIDTH);
        zoomContributionLevels.add(ZoomManager.FIT_HEIGHT);
        zoomManager.setZoomLevelContributions(zoomContributionLevels);
        
        // Zoom Actions
        IAction zoomIn = new ZoomInAction(zoomManager);
        IAction zoomOut = new ZoomOutAction(zoomManager);
        IAction zoomNormal = new ZoomNormalAction(zoomManager);
        registry.registerAction(zoomIn);
        registry.registerAction(zoomOut);
        registry.registerAction(zoomNormal);
        
        // Add these zoom actions to the key binding service
        IHandlerService service = getEditorSite().getService(IHandlerService.class);
        service.activateHandler(zoomIn.getActionDefinitionId(), new ActionHandler(zoomIn));
        service.activateHandler(zoomOut.getActionDefinitionId(), new ActionHandler(zoomOut));
        service.activateHandler(zoomNormal.getActionDefinitionId(), new ActionHandler(zoomNormal));
     
        // Add our own Select All Action so we can select connections as well
        action = new SelectAllAction(this);
        registry.registerAction(action);
        
        // Add our own Print Action
        action = new PrintDiagramAction(this);
        registry.registerAction(action);
        
        // Direct Edit Rename
        action = new DirectEditAction(this);
        action.setId(ActionFactory.RENAME.getId()); // Set this for Global Handler
        action.setText(Messages.AbstractDiagramEditor_4); // Externalise this one
        action.setToolTipText(Messages.AbstractDiagramEditor_13);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);
        
        // Change the Delete Action label
        action = registry.getAction(ActionFactory.DELETE.getId());
        action.setText(Messages.AbstractDiagramEditor_2);
        action.setToolTipText(action.getText());
        getUpdateCommandStackActions().add((UpdateAction)action);
        
        // Paste
        PasteAction pasteAction = new PasteAction(this, viewer);
        registry.registerAction(pasteAction);
        getSelectionActions().add(pasteAction.getId());
        
        // Paste Special
        PasteSpecialAction pasteSpecialAction = new PasteSpecialAction(this, viewer);
        registry.registerAction(pasteSpecialAction);
        getSelectionActions().add(pasteSpecialAction.getId());

        // Cut
        action = new CutAction(this, pasteAction);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);
        
        // Copy
        action = new CopyAction(this, pasteAction);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);
        
        // Use Grid Action
        action = new ToggleGridEnabledAction();
        registry.registerAction(action);
        
        // Show Grid Action
        action = new ToggleGridVisibleAction();
        registry.registerAction(action);
        
        // Snap to Alignment Guides
        action = new ToggleSnapToAlignmentGuidesAction();
        registry.registerAction(action);
        
        // Ruler
        //IAction showRulers = new ToggleRulerVisibilityAction(getGraphicalViewer());
        //registry.registerAction(showRulers);
        
        action = new MatchWidthAction(this);
        action.setText(Messages.AbstractDiagramEditor_5); // Externalise string as it's internal to GEF
        action.setToolTipText(Messages.AbstractDiagramEditor_14);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        
        action = new MatchHeightAction(this);
        registry.registerAction(action);
        action.setText(Messages.AbstractDiagramEditor_6); // Externalise string as it's internal to GEF
        action.setToolTipText(Messages.AbstractDiagramEditor_15);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.LEFT);
        action.setText(Messages.AbstractDiagramEditor_7); // Externalise string as it's internal to GEF
        action.setToolTipText(Messages.AbstractDiagramEditor_16);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.RIGHT);
        action.setText(Messages.AbstractDiagramEditor_8); // Externalise string as it's internal to GEF
        action.setToolTipText(Messages.AbstractDiagramEditor_17);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.TOP);
        action.setText(Messages.AbstractDiagramEditor_9); // Externalise string as it's internal to GEF
        action.setToolTipText(Messages.AbstractDiagramEditor_18);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.BOTTOM);
        action.setText(Messages.AbstractDiagramEditor_10); // Externalise string as it's internal to GEF
        action.setToolTipText(Messages.AbstractDiagramEditor_19);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.CENTER);
        action.setText(Messages.AbstractDiagramEditor_11); // Externalise string as it's internal to GEF
        action.setToolTipText(Messages.AbstractDiagramEditor_20);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.MIDDLE);
        action.setText(Messages.AbstractDiagramEditor_12); // Externalise string as it's internal to GEF
        action.setToolTipText(Messages.AbstractDiagramEditor_21);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        
        // Default Size
        action = new DefaultEditPartSizeAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);
        
        // Reset Aspect Ratio
        action = new ResetAspectRatioAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);
        
        // Properties
        action = new PropertiesAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        
        // Fill Colour
        action = new FillColorAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);
        
        // Connection Line Width
        action = new ConnectionLineWidthAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);
        
        // Connection Line Color
        action = new LineColorAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);

        // Font
        action = new FontAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);

        // Font Colour
        action = new FontColorAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);
        
        // Fill Opacity
        action = new OpacityAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);

        // Outline Opacity
        action = new OutlineOpacityAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);

        // Export As Image
        action = new ExportAsImageAction(this);
        registry.registerAction(action);
        
        // Export As Image to Clipboard
        action = new ExportAsImageToClipboardAction(this);
        registry.registerAction(action);
        
        // Connection Router types
        action = new ConnectionRouterAction.BendPointConnectionRouterAction(this);
        registry.registerAction(action);
// Doesn't work with Connection to Connection
//        action = new ConnectionRouterAction.ShortestPathConnectionRouterAction(this);
        registry.registerAction(action);
        action = new ConnectionRouterAction.ManhattanConnectionRouterAction(this);
        registry.registerAction(action);
        
        // Send Backward
        action = new SendBackwardAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);
        
        // Bring Forward
        action = new BringForwardAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);
        
        // Send to Back
        action = new SendToBackAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);
        
        // Bring To Front
        action = new BringToFrontAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);
        
        // Text Alignment Actions
        for(TextAlignmentAction a : TextAlignmentAction.createActions(this)) {
            registry.registerAction(a);
            getSelectionActions().add(a.getId());
            getUpdateCommandStackActions().add(a);
        }
        
        // Text Position Actions
        for(TextPositionAction a : TextPositionAction.createActions(this)) {
            registry.registerAction(a);
            getSelectionActions().add(a.getId());
            getUpdateCommandStackActions().add(a);
        }
        
        // Lock Object
        action = new LockObjectAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);
        
        // Border Color
        action = new BorderColorAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        getUpdateCommandStackActions().add((UpdateAction)action);

        // Full Screen
        if(!PlatformUtils.isMac()) {
            action = new FullScreenAction(this);
            registry.registerAction(action);
        }
        
        // Select Element in Tree
        action = new SelectElementInTreeAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
    }
    
    /**
     * Disable all actions
     * We need to do this when closing the Editor
     */
    protected void disableActions() {
        Iterator<?> actions = getActionRegistry().getActions();
        while(actions.hasNext()) {
            IAction action = (IAction)actions.next();
            action.setEnabled(false);
        }
    }
    
    @Override
    public void selectObjects(Object[] objects) {
        // Safety check in case this is called via Display#asyncExec()
        if(getGraphicalViewer().getControl() == null) {
            return;
        }
        
        Set<Object> selection = new HashSet<>();
        
        for(Object object : objects) {
            // Diagram Model so replace with diagram reference objects
            if(object instanceof IDiagramModel) {
                for(IDiagramModelComponent dc : DiagramModelUtils.findDiagramModelReferences(getModel(), (IDiagramModel)object)) {
                    selection.add(dc);
                }
            }
            // Else add it
            else {
                selection.add(object);
            }
        }
        
        List<EditPart> editParts = new ArrayList<EditPart>();
        
        for(Object object : selection) {
            EditPart editPart = (EditPart)getGraphicalViewer().getEditPartRegistry().get(object);
            if(editPart != null && editPart.isSelectable() && !editParts.contains(editPart)) {
                editParts.add(editPart);
            }
        }
        
        if(!editParts.isEmpty()) {
            getGraphicalViewer().setSelection(new StructuredSelection(editParts));
            getGraphicalViewer().reveal(editParts.get(0));
        }
        else {
            getGraphicalViewer().setSelection(StructuredSelection.EMPTY);
        }
    }

    @Override
    public String getContributorId() {
        return ArchiPlugin.PLUGIN_ID;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        /*
         * Return the Zoom Manager
         */
        if(adapter == ZoomManager.class && getGraphicalViewer() != null) {
            return getGraphicalViewer().getProperty(ZoomManager.class.toString());
        }

        /*
         * Return the singleton Outline Page
         */
        if(adapter == IContentOutlinePage.class && getGraphicalViewer() != null) {
            return new OverviewOutlinePage(this);
        }
        
        /*
         * Return the Property Sheet Page
         */
        if(adapter == IPropertySheetPage.class) {
            return new TabbedPropertySheetPage(this);
        }

        /*
         * Return the Archimate Model
         * DO NOT REMOVE! SaveAction requires this
         */
        if(adapter == IArchimateModel.class && getModel() != null) {
            return getModel().getArchimateModel();
        }
        
        /*
         * Return the Diagram Model
         */
        if(adapter == IDiagramModel.class) {
            return getModel();
        }

        return super.getAdapter(adapter);
    }
    
    /**
     * The eCore Model changed
     * @param msg
     */
    protected void notifyChanged(Notification msg) {
        // Archimate Model or Diagram Model name changed
        if(msg.getFeature() == IArchimatePackage.Literals.NAMEABLE__NAME) {
            setPartName(getEditorInput().getName());
        }
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        // Remove Preference listener
        Preferences.STORE.removePropertyChangeListener(appPreferencesListener);
        
        // Remove eCore adapter listener objects
        eCoreAdapter.remove(getModel(), getModel() != null ? getModel().getArchimateModel() : null);
        
        // Update shell text
        getSite().getShell().setText(Platform.getProduct().getName());
        
        // Disable Actions
        disableActions();
        
        // Release the reference to the IDiagramModel in the DiagramEditorInput because it is not released by the system
        // And can't be garbage collected
        if(getEditorInput() instanceof DiagramEditorInput) {
            int openEditors = EditorManager.getDiagramEditorReferences(fDiagramModel).length;
            if(openEditors == 0) { // There may be more than one instance open (split editor) sharing the same DiagramEditorInput
                ((DiagramEditorInput)getEditorInput()).dispose();
            }
        }

        // Can now be garbage collected
        fDiagramModel = null;
    }
}
