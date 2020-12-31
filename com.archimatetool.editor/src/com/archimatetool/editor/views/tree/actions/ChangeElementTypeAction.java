package com.archimatetool.editor.views.tree.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.LabelRetargetAction;

import com.archimatetool.editor.diagram.commands.ChangeElementTypeCommand;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.views.tree.TreeModelViewer;
import com.archimatetool.model.IArchimateElement;

/**
 * 
 * @author etienne-sf
 */
public class ChangeElementTypeAction extends ViewerAction {

	public static final String ROOT_ID = "changetype "; //$NON-NLS-1$

	/** The target type that must be given to the selected elements */
	EClass targetEClass;

	/** All the created actions, one for each Archimate type */
	static List<IViewerAction> allActions = null;

	/** All the created actions, grouped by Archimate type */
	static Map<EClass, List<IViewerAction>> actionsPerType = null;

	/** All the actions, associated with its {@link ActionFactory} */
	static Map<ActionFactory, IViewerAction> actionPerActionFactory = null;

	public static List<IViewerAction> makeActions(TreeModelViewer selectionProvider) {
		if (allActions == null || actionsPerType == null || actionPerActionFactory == null) {
			allActions = new ArrayList<>();
			actionsPerType = new HashMap<>();
			actionPerActionFactory = new HashMap<>();

			// Let's go through all archimate objects
			for (EClass type : com.archimatetool.editor.diagram.actions.ChangeElementTypeAction.getArchimateObjectTypes()) {
				List<IViewerAction> actions = new ArrayList<>();
				for (EClass eclass : com.archimatetool.editor.diagram.actions.ChangeElementTypeAction.getArchimateObjects().get(type)) {
					IViewerAction action = new ChangeElementTypeAction(selectionProvider, eclass);
					action.setImageDescriptor(ArchiLabelProvider.INSTANCE.getImageDescriptor(eclass));
					allActions.add(action);
					actions.add(action);

					ActionFactory actionFactory = new ActionFactory(ChangeElementTypeAction.ROOT_ID + eclass.getName(),
							"com.archimatetool.editor.action.changetype." + eclass.getName()) { //$NON-NLS-1$
						@Override
						public IWorkbenchAction create(IWorkbenchWindow window) {
							if (window == null) {
								throw new IllegalArgumentException();
							}
							LabelRetargetAction action = new LabelRetargetAction(getId(), ArchiLabelProvider.INSTANCE.getDefaultName(eclass));
							window.getPartService().addPartListener(action);
							return action;
						}
					};
					actionPerActionFactory.put(actionFactory, action);
				}
				actionsPerType.put(type, actions);
			}
		}

		return allActions;
	}

	public static Map<ActionFactory, IViewerAction> getActionFactories() {
		return actionPerActionFactory;
	}

	/**
	 * Fills in the given menu, with one line per Archimate's objet (that is all objects out of relationships
	 * 
	 * @param actionRegistry
	 * @param refactorMenu   The contextual menu to fill in
	 */
	public static void fillMenu(IMenuManager refactorMenu) {
		// Let's go through all archimate objects, in the correct order
		for (EClass type : com.archimatetool.editor.diagram.actions.ChangeElementTypeAction.getArchimateObjectTypes()) {
			IMenuManager subMenu = new MenuManager(com.archimatetool.editor.diagram.actions.ChangeElementTypeAction.getDisplayableTypeLabel(type),
					"treemenu_refactor_" + type.getName()); //$NON-NLS-1$
			refactorMenu.add(subMenu);
			for (IViewerAction viewerAction : actionsPerType.get(type)) {
				subMenu.add(viewerAction);
			}
		}
	}

	public ChangeElementTypeAction(TreeModelViewer selectionProvider, EClass targetEClass) {
		super(selectionProvider);
		this.targetEClass = targetEClass;
		setText(ArchiLabelProvider.INSTANCE.getDefaultName(targetEClass));
		setEnabled(false);
	}

	@Override
	public void run() {
		IStructuredSelection selection = getSelection();
		if (selection == null || selection.isEmpty()) {
			return;
		}

		// Let's loop over all the selected objects, and execute the change type command on the right stack
		Map<CommandStack, CompoundCommand> commandMap = new Hashtable<>();
		for (Object object : getSelection()) {
			// Non ArchimateElement are ignored
			if (object instanceof IArchimateElement) {
				IArchimateElement element = (IArchimateElement) object;

				// Get the Command Stack registered to the object
				CommandStack stack = (CommandStack) element.getAdapter(CommandStack.class);
				if (stack == null) {
					System.err.println("CommandStack was null in " + getClass()); //$NON-NLS-1$
				}

				// Get the compoundCommand we've stored for this stack, or create and store a new one
				CompoundCommand compoundCommand = commandMap.get(stack);
				if (compoundCommand == null) {
					compoundCommand = new CompoundCommand();
					commandMap.put(stack, compoundCommand);
				}

				// Add the changeElementType command to the compoundCommand
				compoundCommand.add(new ChangeElementTypeCommand(element, targetEClass));
			}
		}

		// Once all compound commands have been stored, we execute them. This allows to undo and redo them as a a block, stack per stack.
		for (Entry<CommandStack, CompoundCommand> entry : commandMap.entrySet()) {
			entry.getKey().execute(entry.getValue());
		}
	}

	@Override
	public void update() {
		// The command is enabled, if at least one selected object is an ArchimateElement
		boolean enabled = false;
		for (Object o : getSelection()) {
			if (o instanceof IArchimateElement) {
				enabled = true;
				break;
			}
		}
		setEnabled(enabled);
	}

}
