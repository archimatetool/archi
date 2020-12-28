/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.RetargetAction;

import com.archimatetool.editor.diagram.commands.ChangeElementTypeCommand;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.impl.BusinessActor;
import com.archimatetool.model.impl.BusinessRole;
import com.archimatetool.model.impl.DiagramModelArchimateObject;
import com.archimatetool.model.util.ArchimateModelUtils;

/**
 * Change the Type of an Element, for instance from a {@link BusinessActor} to a {@link BusinessRole}. All the existing associations are maintained,
 * and of the same type as the existing one. This may lead to association coming to or going from the current object that become invalid, according to
 * the ArchiMate specification.
 * 
 * @author Etienne Gauthier
 */
public class ChangeElementTypeAction extends SelectionAction {

	public static final String ROOT_ID = "Diagram_ChangeElementTypeAction_"; //$NON-NLS-1$
	public static final String TEXT = Messages.ChangeElementTypeAction_0;

	/**
	 * The list of type of ArchiMate objects, which contains the entry for the menu, to be displayed IN THIS ORDER.
	 */
	public static List<EClass> archimateObjectTypes = Arrays.asList(IArchimatePackage.eINSTANCE.getStrategyElement(),
			IArchimatePackage.eINSTANCE.getBusinessElement(), IArchimatePackage.eINSTANCE.getApplicationElement(),
			IArchimatePackage.eINSTANCE.getTechnologyElement(), IArchimatePackage.eINSTANCE.getPhysicalElement(),
			IArchimatePackage.eINSTANCE.getMotivationElement(), IArchimatePackage.eINSTANCE.getImplementationMigrationElement());

	/**
	 * The list of Archimate Objects, per type. This is used to create the menus and the actions. The order of the items in the list is the order
	 * displayed in the menus
	 */
	public static Map<EClass, EClass[]> archimateObjects = new HashMap<>();
	static {
		archimateObjects.put(IArchimatePackage.eINSTANCE.getStrategyElement(), ArchimateModelUtils.getStrategyClasses());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getBusinessElement(), ArchimateModelUtils.getBusinessClasses());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getApplicationElement(), ArchimateModelUtils.getApplicationClasses());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getTechnologyElement(), ArchimateModelUtils.getTechnologyClasses());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getPhysicalElement(), ArchimateModelUtils.getPhysicalClasses());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getMotivationElement(), ArchimateModelUtils.getMotivationClasses());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getImplementationMigrationElement(),
				ArchimateModelUtils.getImplementationMigrationClasses());
	}

	/**
	 * The class that is the target of this change. That is: when this change is applied to an object, it will be transformed into an instance of this
	 * type
	 */
	private EClass targetEClass = null;

	/**
	 * Creation of all menu actions to change types
	 * 
	 * @param abstractDiagramEditor
	 * @return
	 */
	public static List<ChangeElementTypeAction> createActions(IWorkbenchPart part) {
		List<ChangeElementTypeAction> actionList = new ArrayList<>();
		// Let's go through all archimate objects
		for (EClass type : archimateObjectTypes) {
			for (EClass eclass : archimateObjects.get(type)) {
				actionList.add(new ChangeElementTypeAction(part, ROOT_ID + eclass.getName(), eclass));
			}
		}

		return actionList;
	}

	public static List<EClass> getArchimateObjectTypes() {
		return archimateObjectTypes;
	}

	public static Map<EClass, EClass[]> getArchimateObjects() {
		return archimateObjects;
	}

	public static List<RetargetAction> getRetargetActions() {
		List<RetargetAction> ret = new ArrayList<>();
		// This method is called before createActions, so we can't use the actionList
		// Let's go through all archimate objects
		for (EClass type : archimateObjectTypes) {
			for (EClass eclass : archimateObjects.get(type)) {
				ret.add(new RetargetAction(ROOT_ID + eclass.getName(), eclass.getName()));
			}
		} // for
		return ret;
	}

	/**
	 * Fills in the given menu, with one line per Archimate's objet (that is all objects out of relationships
	 * 
	 * @param actionRegistry
	 * @param refactorMenu   The menu to fill in
	 */
	public static void fillMenu(ActionRegistry actionRegistry, IMenuManager refactorMenu) {
		// Let's go through all archimate objects
		for (EClass type : archimateObjectTypes) {
			IMenuManager subMenu = new MenuManager(getDisplayableTypeLabel(type), "menu_refactor_" + type.getName()); //$NON-NLS-1$
			refactorMenu.add(subMenu);
			for (EClass eclass : archimateObjects.get(type)) {
				subMenu.add(actionRegistry.getAction(ROOT_ID + eclass.getName()));
			}
		}
	}

	/**
	 * There seems to be no way to get a proper label for the types (Strategy, Business...). There lable exist in a lot of places, but for various
	 * usage, and a mapping to them would have to be done. <BR/>
	 * This method take the EClass's name (StrategyElement, BusinessElement...) and return the associated label
	 */
	public static String getDisplayableTypeLabel(EClass type) {
		String label = type.getName();
		if (label.equals("ImplementationMigrationElement")) {
			return "Implementation && Migration";
		} else if (label.endsWith("Element")) {
			return label.substring(0, label.length() - "Element".length());
		} else {
			// Oups, the label has changed.
			return label;
		}
	}

	/**
	 * This constructor can only be created by the {@link #createActions(IWorkbenchPart)} static method
	 * 
	 * @param part
	 * @param id
	 * @param targetEClass
	 */
	private ChangeElementTypeAction(IWorkbenchPart part, String id, EClass targetEClass) {
		super(part);
		setId(id);
		setText(ArchiLabelProvider.INSTANCE.getDefaultName(targetEClass));
		this.targetEClass = targetEClass;
	}

	/**
	 * Change type is enabled if the selection contains only one editable element. {@inheritDoc}
	 */
	@Override
	protected boolean calculateEnabled() {
		List<?> selected = getSelectedObjects();

		// Quick checks (can change only one item per one item)
		if (selected.isEmpty()) {
			return false;
		}

		for (Object object : selected) {
			if (!(object instanceof EditPart)) {
				return false;
			}
			if (!(((EditPart) object).getModel() instanceof DiagramModelArchimateObject)) {
				// The selected element must be an Archimate object (that is: not a
				// relationship)
				return false;
			}
		}

		Command command = createCommand(selected);
		if (command == null) {
			return false;
		}
		return command.canExecute();
	}

	@Override
	public void run() {
		execute(createCommand(getSelectedObjects()));
	}

	private Command createCommand(List<?> selection) {
		CompoundCommand result = new CompoundCommand(Messages.ChangeElementTypeAction_0);

		for (Object object : selection) {
			if (object instanceof EditPart) {
				Object dmo = ((EditPart) object).getModel();

				if (dmo instanceof ILockable && ((ILockable) dmo).isLocked()) {
					continue;
				}

				// IDiagramModelObject: any Archimate object (but not a relationship)
				if (dmo instanceof DiagramModelArchimateObject) {
					result.add(new ChangeElementTypeCommand((DiagramModelArchimateObject) dmo, targetEClass));
				}
			}
		}

		return result.unwrap();
	}

}
