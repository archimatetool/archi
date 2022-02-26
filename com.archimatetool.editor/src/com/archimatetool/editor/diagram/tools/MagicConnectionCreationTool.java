/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.tools.ConnectionCreationTool;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ArmEvent;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.ArchimateDiagramModelFactory;
import com.archimatetool.editor.diagram.commands.CreateDiagramArchimateConnectionWithDialogCommand;
import com.archimatetool.editor.diagram.editparts.AbstractBaseEditPart;
import com.archimatetool.editor.diagram.editparts.diagram.GroupEditPart;
import com.archimatetool.editor.diagram.figures.IContainerFigure;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.editor.ui.services.ComponentSelectionManager;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.model.viewpoints.IViewpoint;
import com.archimatetool.model.viewpoints.ViewpointManager;



/**
 * Magic Connection Creation Tool
 * 
 * @author Phillip Beauvoir
 */
public class MagicConnectionCreationTool extends ConnectionCreationTool {
    
    private static Cursor cursor = new Cursor(
            null,
            IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.CURSOR_IMG_MAGIC_CONNECTOR).getImageData(ImageFactory.getLogicalDeviceZoom()),
            0,
            0);

    
    /**
     * Flag to update Factory elements when hovering on relationship menu items
     * This is to ensure that when the user presses escape the menu selection is cancelled
     */
    private boolean fSetRelationshipTypeWhenHoveringOnConnectionMenuItem;

    /**
     * This flag stops some thread conditions (on Mac) that can re-set the current command when the popup menu is showing
     */
    private boolean fCanSetCurrentCommand = true;
    
    public MagicConnectionCreationTool() {
       setDefaultCursor(cursor);
       setDisabledCursor(cursor);
    }
    
    /**
     * When this is called, a CreateConnectionRequest will already have been created.
     * This CreateConnectionRequest will be passed to the ArchimateDiagramConnectionPolicy.
     * ArchimateDiagramConnectionPolicy will, in turn, create a CreateArchimateConnectionCommand.
     * 
     * So, at this point, we already have the source and target edit parts and model objects set in a Command.
     * If we want to change this at all we will have to kludge it.
     */
    @Override
    protected boolean handleCreateConnection() {
        // Clear the connection factory first
        getFactory().clear();
        
        // Do this first, here (we have to!)
        fCanSetCurrentCommand = true;
        Command endCommand = getCommand();
        setCurrentCommand(endCommand);
        
        // Get this now!
        CreateConnectionRequest request = (CreateConnectionRequest)getTargetRequest();
        
        EditPart sourceEditPart = request.getSourceEditPart();
        EditPart targetEditPart = request.getTargetEditPart();
        
        // Allow circular connections
        //if(sourceEditPart == null || sourceEditPart == targetEditPart) { 
        if(sourceEditPart == null) {
            eraseSourceFeedback();
            return false;
        }
        
        IDiagramModelArchimateComponent sourceDiagramModelComponent = (IDiagramModelArchimateComponent)sourceEditPart.getModel();
        
        // If targetEditPart is null then user clicked on the diagram or on a non-Archimate concept Editpart
        if(targetEditPart == null) {
            return createElementAndConnection(sourceDiagramModelComponent, request.getLocation());
        }
        
        // User clicked on Archimate target concept edit part
        if(targetEditPart.getModel() instanceof IDiagramModelArchimateComponent) {
            return createConnection(request, sourceDiagramModelComponent, (IDiagramModelArchimateComponent)targetEditPart.getModel());
        }
        
        eraseSourceFeedback();
        return false;
    }
    
    @Override
    protected void setTargetEditPart(EditPart editpart) {
        /*
         * Set editpart to null if it's not an Archimate target concept so we can handle it as if we clicked on the canvas.
         * This also disables unwanted connection target feedback.
         */
        if(editpart != null && !(editpart.getModel() instanceof IDiagramModelArchimateComponent)) {
            editpart = null;
        }
        
        /*
         * Check whether we are trying to connect an element to a relation where the relation is already connected to that element
         * If we are, set editpart to null to veto it.
         */
        if(editpart != null) {
            EditPart sourceEditPart = ((CreateConnectionRequest)getSourceRequest()).getSourceEditPart();
            if(sourceEditPart != null) {
                if(sourceEditPart.getModel() instanceof IDiagramModelArchimateComponent && editpart.getModel() instanceof IDiagramModelArchimateComponent) {
                    IArchimateConcept sourceConcept = ((IDiagramModelArchimateComponent)sourceEditPart.getModel()).getArchimateConcept();
                    IArchimateConcept targetConcept = ((IDiagramModelArchimateComponent)editpart.getModel()).getArchimateConcept();
                    
                    if(ArchimateModelUtils.hasDirectRelationship(sourceConcept, targetConcept)) {
                        editpart = null;
                    }
                }
            }
        }
        
        super.setTargetEditPart(editpart);
    }
    
    @Override
    protected void setCurrentCommand(Command c) {
        // Guard against Mac threading issue
        if(fCanSetCurrentCommand) {
            super.setCurrentCommand(c);
        }
    }
    
    /**
     * Create just a new connection between source and target components
     */
    private boolean createConnection(CreateConnectionRequest request, IDiagramModelArchimateComponent sourceDiagramModelComponent,
            IDiagramModelArchimateComponent targetDiagramModelComponent) {
        
        // Only set when a menu selection for a connection is actually made
        fSetRelationshipTypeWhenHoveringOnConnectionMenuItem = false;
        
        // Set this threading safety guard
        fCanSetCurrentCommand = false;
        
        Menu menu = new Menu(getCurrentViewer().getControl());
        addConnectionActions(menu, sourceDiagramModelComponent.getArchimateConcept(), targetDiagramModelComponent.getArchimateConcept());
        menu.setVisible(true);
        
        // Modal menu
        Display display = menu.getDisplay();
        while(!menu.isDisposed() && menu.isVisible()) {
            if(!display.readAndDispatch()) {
                display.sleep();
            }
        }
        
        // Workaround for Bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=480318
        // SWT Menu does not fire Selection Event when Windows touch display is enabled
        if(PlatformUtils.isWindows()) {
            while(display.readAndDispatch());
        }

        if(!menu.isDisposed()) {
            menu.dispose();
        }

        // Reset guard
        fCanSetCurrentCommand = true;

        eraseSourceFeedback();

        // No selection
        if(getFactory().getObjectType() == null) {
            getFactory().clear();
            return false;
        }
        
        // If user selected a reverse connection from target to source then swap the source/target in the Command
        // (Yes, I know this is kludgey, but you try and disentangle GEF's Request/Policy/Factory/Command dance...)
        if(getFactory().swapSourceAndTarget()) {
            CreateDiagramArchimateConnectionWithDialogCommand cmd = (CreateDiagramArchimateConnectionWithDialogCommand)getCurrentCommand();
            cmd.swapSourceAndTargetConcepts();
        }
        
        executeCurrentCommand();
        
        // Clear the factory type
        getFactory().clear();
        return true;
    }
    
    /**
     * Create an Element and a connection in one go when user clicks on the canvas or in a non-Archimate Editpart
     */
    private boolean createElementAndConnection(IDiagramModelArchimateComponent sourceDiagramModelComponent, Point location) {
        // Grab this now as it will disappear after menu is shown
        EditPartViewer viewer = getCurrentViewer();
        
        // What did we click on?
        GraphicalEditPart targetEditPart = (GraphicalEditPart)viewer.findObjectAt(getCurrentInput().getMouseLocation());
        
        // Target parent (default is the diagram itself)
        IDiagramModelContainer parent = sourceDiagramModelComponent.getDiagramModel();
        
        // If we clicked on a Group EditPart use that as parent
        if(targetEditPart instanceof GroupEditPart) {
            parent = (IDiagramModelContainer)targetEditPart.getModel();
        }
        // Or did we click on something else? Then use the parent of that
        else if(targetEditPart instanceof AbstractBaseEditPart) {
            targetEditPart = (GraphicalEditPart)targetEditPart.getParent();
            parent = (IDiagramModelContainer)targetEditPart.getModel();
        }
        
        boolean elementsFirst = ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.MAGIC_CONNECTOR_POLARITY);
        boolean modKeyPressed = getCurrentInput().isModKeyDown(SWT.MOD1);
        elementsFirst ^= modKeyPressed;
        
        Menu menu = new Menu(getCurrentViewer().getControl());
        
        // User will hover over element, then connection
        if(elementsFirst) {
            fSetRelationshipTypeWhenHoveringOnConnectionMenuItem = false; 
            addElementActions(menu, sourceDiagramModelComponent);
        }
        // User will hover over connection, then element
        else {
            fSetRelationshipTypeWhenHoveringOnConnectionMenuItem = true;
            addConnectionActions(menu, sourceDiagramModelComponent);
        }
        
        menu.setVisible(true);
        
        // Modal menu
        Display display = menu.getDisplay();
        while(!menu.isDisposed() && menu.isVisible()) {
            if(!display.readAndDispatch()) {
                display.sleep();
            }
        }
        
        // Workaround for Bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=480318
        // SWT Menu does not fire Selection Event when Windows touch display is enabled
        if(PlatformUtils.isWindows()) {
            while(display.readAndDispatch());
        }

        if(!menu.isDisposed()) {
            menu.dispose();
        }
        
        eraseSourceFeedback();
        
        // No selection
        if(getFactory().getElementType() == null || getFactory().getRelationshipType() == null) {
            getFactory().clear();
            return false;
        }
        
        // Create Compound Command first
        CompoundCommand cmd = new CompoundCommand(Messages.MagicConnectionCreationTool_6);
        
        // If the EditPart's Figure is a Container, adjust the location to relative co-ords
        if(targetEditPart.getFigure() instanceof IContainerFigure) {
            ((IContainerFigure)targetEditPart.getFigure()).translateMousePointToRelative(location);
        }
        // Or compensate for scrolled parent figure
        else {
            IFigure contentPane = targetEditPart.getContentPane();
            contentPane.translateToRelative(location);
        }
        
        CreateNewDiagramObjectCommand cmd1 = new CreateNewDiagramObjectCommand(parent,
                getFactory().getElementType(), location, viewer);
        Command cmd2 = new CreateNewConnectionCommand(sourceDiagramModelComponent, cmd1.getNewObject(),
                getFactory().getRelationshipType());
        cmd.add(cmd1);
        cmd.add(cmd2);
        
        executeCommand(cmd);
        
        // Clear the factory
        getFactory().clear();
        
        return true;
    }
    
    /**
     * Add Connection->Element Actions
     */
    private void addConnectionActions(Menu menu, IDiagramModelArchimateComponent sourceDiagramModelComponent) {
        for(EClass relationshipType : ArchimateModelUtils.getRelationsClasses()) {
            if(ArchimateModelUtils.isValidRelationshipStart(sourceDiagramModelComponent.getArchimateConcept(), relationshipType)) {
                MenuItem item = addConnectionAction(menu, relationshipType, false);
                Menu subMenu = new Menu(item);
                item.setMenu(subMenu);
                
                addConnectionActions(subMenu, Messages.MagicConnectionCreationTool_7,
                        sourceDiagramModelComponent, ArchimateModelUtils.getStrategyClasses(), relationshipType);
                addConnectionActions(subMenu, Messages.MagicConnectionCreationTool_0,
                        sourceDiagramModelComponent, ArchimateModelUtils.getBusinessClasses(), relationshipType);
                addConnectionActions(subMenu, Messages.MagicConnectionCreationTool_1,
                        sourceDiagramModelComponent, ArchimateModelUtils.getApplicationClasses(), relationshipType);
                addConnectionActions(subMenu, Messages.MagicConnectionCreationTool_2,
                        sourceDiagramModelComponent, ArchimateModelUtils.getTechnologyClasses(), relationshipType);
                addConnectionActions(subMenu, Messages.MagicConnectionCreationTool_9,
                        sourceDiagramModelComponent, ArchimateModelUtils.getPhysicalClasses(), relationshipType);
                addConnectionActions(subMenu, Messages.MagicConnectionCreationTool_3,
                        sourceDiagramModelComponent, ArchimateModelUtils.getMotivationClasses(), relationshipType);
                addConnectionActions(subMenu, Messages.MagicConnectionCreationTool_4,
                        sourceDiagramModelComponent, ArchimateModelUtils.getImplementationMigrationClasses(), relationshipType);
                addConnectionActions(subMenu, Messages.MagicConnectionCreationTool_8,
                        sourceDiagramModelComponent, getOtherAndConnectorClasses(), relationshipType);
                
                if(subMenu.getItemCount() == 0) {
                    item.dispose(); // Nothing there
                }
            }
        }
    }
    
    private void addConnectionActions(Menu menu, String menuText, IDiagramModelArchimateComponent sourceDiagramModelComponent,
            EClass[] list, EClass relationshipType) {
        
        MenuItem item = new MenuItem(menu, SWT.CASCADE);
        item.setText(menuText);
        Menu subMenu = new Menu(item);
        item.setMenu(subMenu);
        
        for(EClass type : list) {
            // Check if allowed by Viewpoint
            if(!isAllowedTargetTypeInViewpoint(sourceDiagramModelComponent, type)) {
                continue;
            }

            // Check valid relationship between actual concepts to check Junction rules, if any
            IArchimateConcept sourceConcept = sourceDiagramModelComponent.getArchimateConcept();
            IArchimateConcept targetConcept = (IArchimateConcept)IArchimateFactory.eINSTANCE.create(type);
            
            if(ArchimateModelUtils.isValidRelationship(sourceConcept, targetConcept, relationshipType)) {
                addElementAction(subMenu, type);
            }
        }
        
        if(subMenu.getItemCount() == 0) {
            item.dispose(); // Nothing there
        }
    }
    
    /**
     * Add Element to Connection Actions
     */
    private void addElementActions(Menu menu, IDiagramModelArchimateComponent sourceDiagramModelComponent) {
        addElementActions(menu, Messages.MagicConnectionCreationTool_7, sourceDiagramModelComponent, ArchimateModelUtils.getStrategyClasses());
        addElementActions(menu, Messages.MagicConnectionCreationTool_0, sourceDiagramModelComponent, ArchimateModelUtils.getBusinessClasses());
        addElementActions(menu, Messages.MagicConnectionCreationTool_1, sourceDiagramModelComponent, ArchimateModelUtils.getApplicationClasses());
        addElementActions(menu, Messages.MagicConnectionCreationTool_2, sourceDiagramModelComponent, ArchimateModelUtils.getTechnologyClasses());
        addElementActions(menu, Messages.MagicConnectionCreationTool_3, sourceDiagramModelComponent, ArchimateModelUtils.getMotivationClasses());
        addElementActions(menu, Messages.MagicConnectionCreationTool_4, sourceDiagramModelComponent, ArchimateModelUtils.getImplementationMigrationClasses());
        addElementActions(menu, Messages.MagicConnectionCreationTool_8, sourceDiagramModelComponent, getOtherAndConnectorClasses());
    }
    
    private void addElementActions(Menu menu, String menuText, IDiagramModelArchimateComponent sourceDiagramModelComponent, EClass[] list) {
        MenuItem item = new MenuItem(menu, SWT.CASCADE);
        item.setText(menuText);
        Menu subMenu = new Menu(item);
        item.setMenu(subMenu);

        for(EClass type : list) {
            // Check if allowed by Viewpoint
            if(!isAllowedTargetTypeInViewpoint(sourceDiagramModelComponent, type)) {
                continue;
            }
            
            MenuItem subItem = addElementAction(subMenu, type);
            Menu childSubMenu = new Menu(subItem);
            subItem.setMenu(childSubMenu);
            
            // Check valid relationship between actual concepts to check Junction rules, if any
            IArchimateConcept sourceConcept = sourceDiagramModelComponent.getArchimateConcept();
            IArchimateConcept targetConcept = (IArchimateConcept)IArchimateFactory.eINSTANCE.create(type);
            
            for(EClass eClass : ArchimateModelUtils.getValidRelationships(sourceConcept, targetConcept)) {
                addConnectionAction(childSubMenu, eClass, false);
            }
            
            if(childSubMenu.getItemCount() == 0) {
                subItem.dispose(); // Nothing there
            }
        }
        
        if(subMenu.getItemCount() == 0) {
            item.dispose(); // Nothing there
        }
    }

    private MenuItem addElementAction(Menu menu, final EClass type) {
        final MenuItem item = new MenuItem(menu, SWT.CASCADE);
        item.setText(ArchiLabelProvider.INSTANCE.getDefaultName(type));
        item.setImage(ArchiLabelProvider.INSTANCE.getImage(type));
        
        // Add arm listener to notify Hints View and also set element if elements first
        item.addArmListener(new ArmListener() {
            @Override
            public void widgetArmed(ArmEvent e) {
                if(!fSetRelationshipTypeWhenHoveringOnConnectionMenuItem) { // User will hover over element, then connection
                    getFactory().setElementType(type);
                }
                ComponentSelectionManager.INSTANCE.fireSelectionEvent(item, type);
            }
        });
        
        item.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                getFactory().setElementType(type);
            }
        });
        
        return item;
    }

    /**
     * Add Connection Actions going in both directions
     */
    private void addConnectionActions(Menu menu, IArchimateConcept sourceConcept, IArchimateConcept targetConcept) {
        EClass[] forwardConnections = ArchimateModelUtils.getValidRelationships(sourceConcept, targetConcept);
        EClass[] reverseConnections = ArchimateModelUtils.getValidRelationships(targetConcept, sourceConcept);
        
        // Add forward direction connections
        for(EClass type : forwardConnections) {
            addConnectionAction(menu, type, false);
        }
        
        // Add reverse direction connections
        for(EClass type : reverseConnections) {
            addConnectionAction(menu, type, true);
        }
        
        // Add a separator only if we have both sets of items on the menu
        if(forwardConnections.length > 0 && reverseConnections.length > 0) {
            new MenuItem(menu, SWT.SEPARATOR, forwardConnections.length);
        }
    }
    
    /**
     * Add a Connection Action with a relationship type
     */
    private MenuItem addConnectionAction(Menu menu, final EClass relationshipType, final boolean reverseDirection) {
        final MenuItem item = new MenuItem(menu, SWT.CASCADE);
        item.setText(ArchiLabelProvider.INSTANCE.getRelationshipPhrase(relationshipType, reverseDirection));
        item.setImage(ArchiLabelProvider.INSTANCE.getImage(relationshipType));
        
        // Add arm listener to notify Hints View
        item.addArmListener(new ArmListener() {
            @Override
            public void widgetArmed(ArmEvent e) {
                if(fSetRelationshipTypeWhenHoveringOnConnectionMenuItem) { // User will hover over connection, then element
                    getFactory().setRelationshipType(relationshipType);
                }
                ComponentSelectionManager.INSTANCE.fireSelectionEvent(item, relationshipType);
            }
        });
        
        item.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                getFactory().setRelationshipType(relationshipType);
                getFactory().setSwapSourceAndTarget(reverseDirection);
            }
        });
        
        return item;
    }
    
    /**
     * @return "Other" and "Connector" Classes as one arraArraysy
     */
    private EClass[] getOtherAndConnectorClasses() {
        List<EClass> list = new ArrayList<EClass>();
        list.addAll(Arrays.asList(ArchimateModelUtils.getOtherClasses()));
        list.addAll(Arrays.asList(ArchimateModelUtils.getConnectorClasses()));
        return list.toArray(new EClass[] {});
    }
    
    /**
     * @return True if type is an allowed target type for a given Viewpoint
     */
    private boolean isAllowedTargetTypeInViewpoint(IDiagramModelArchimateComponent diagramComponent, EClass type) {
        if(!ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.VIEWPOINTS_HIDE_MAGIC_CONNECTOR_ELEMENTS)) {
            return true;
        }
        
        IArchimateDiagramModel dm = (IArchimateDiagramModel)diagramComponent.getDiagramModel();
        String id = dm.getViewpoint();
        IViewpoint viewpoint = ViewpointManager.INSTANCE.getViewpoint(id);
        return viewpoint == null ? true : viewpoint.isAllowedConcept(type);
    }
    
    @Override
    protected MagicConnectionModelFactory getFactory() {
        return (MagicConnectionModelFactory)super.getFactory();
    }
    
    /**
     * We need to explicitly set this since the source feedback is not always erased
     * (when pressing the Escape key after the popup menu is displayed)
     * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#eraseSourceFeedback(Request)
     */
    @Override
    protected void eraseSourceFeedback() {
        getSourceRequest().setType(RequestConstants.REQ_CONNECTION_END);
        super.eraseSourceFeedback();
    }
    
    
    // ======================================================================================================
    // COMMANDS
    // ======================================================================================================
    
    /**
     * Create New DiagramObject Command
     */
    private static class CreateNewDiagramObjectCommand extends Command {
        private IDiagramModelContainer fParent;
        private IDiagramModelArchimateObject fChild;
        private EClass fTemplate;
        private EditPartViewer fViewer;

        CreateNewDiagramObjectCommand(IDiagramModelContainer parent, EClass type, Point location, EditPartViewer viewer) {
            fParent = parent;
            fTemplate = type;
            fViewer = viewer;

            // Create this now
            fChild = (IDiagramModelArchimateObject)new ArchimateDiagramModelFactory(fTemplate).getNewObject();
            
            // Location
            fChild.getBounds().setLocation(location.x, location.y);
        }
        
        IDiagramModelArchimateObject getNewObject() {
            return fChild;
        }
        
        @Override
        public void execute() {
            redo();
            
            // Select EditPart and edit name
            if(fViewer != null && ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.EDIT_NAME_ON_NEW_OBJECT)) {
                EditPart editPart = (EditPart)fViewer.getEditPartRegistry().get(fChild);
                if(editPart != null) {
                    // Async this otherwise the edit label is not aligned
                    Display.getCurrent().asyncExec(() -> {
                        fViewer.select(editPart);
                        Request directEditRequest = new Request(RequestConstants.REQ_DIRECT_EDIT);
                        editPart.performRequest(directEditRequest);
                    });
                }
            }                
        }

        @Override
        public void undo() {
            fParent.getChildren().remove(fChild);
            fChild.removeArchimateConceptFromModel();
        }

        @Override
        public void redo() {
            fParent.getChildren().add(fChild);
            fChild.addArchimateConceptToModel(null);
        }
        
        @Override
        public void dispose() {
            fParent = null;
            fChild = null;
            fTemplate = null;
            fViewer = null;
        }
    }
    
    /**
     * Create New Connection Command
     */
    private static class CreateNewConnectionCommand extends Command {
        private IDiagramModelArchimateConnection fConnection;
        private IDiagramModelArchimateComponent fSource;
        private IDiagramModelArchimateComponent fTarget;
        private EClass fTemplate;
        
        CreateNewConnectionCommand(IDiagramModelArchimateComponent source, IDiagramModelArchimateComponent target, EClass type) {
            fSource = source;
            fTarget = target;
            fTemplate = type;
        }
        
        @Override
        public void execute() {
            fConnection = (IDiagramModelArchimateConnection)new ArchimateDiagramModelFactory(fTemplate).getNewObject();
            fConnection.connect(fSource, fTarget);
            fConnection.addArchimateConceptToModel(null);
        }
        
        @Override
        public void redo() {
            fConnection.reconnect();
            fConnection.addArchimateConceptToModel(null);
        }
        
        @Override
        public void undo() {
            fConnection.disconnect();
            fConnection.removeArchimateConceptFromModel();
        }

        @Override
        public void dispose() {
            fConnection = null;
            fSource = null;
            fTarget = null;
            fTemplate = null;
        }
    }
}
