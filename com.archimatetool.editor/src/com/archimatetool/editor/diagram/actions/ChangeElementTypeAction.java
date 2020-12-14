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
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.impl.BusinessActor;
import com.archimatetool.model.impl.BusinessRole;
import com.archimatetool.model.impl.DiagramModelArchimateObject;
import com.archimatetool.model.util.ArchimateModelUtils;

/**
 * Change the Type of an Element, for instance from a {@link BusinessActor} to a
 * {@link BusinessRole}. All the existing associations are maintained, and of
 * the same type as the existing one. This may lead to association coming to or
 * going from the current object that become invalid, according to the ArchiMate
 * specification.
 * 
 * @author Etienne Gauthier
 */
public class ChangeElementTypeAction extends SelectionAction {

	public static final String ROOT_ID = "ChangeElementTypeAction"; //$NON-NLS-1$
	public static final String TEXT = Messages.ChangeElementTypeAction_0;

	/**
	 * The list of type of ArchiMate objects, which contains the entry for the menu,
	 * to be displayed in this order
	 */
	public static List<EClass> archimateObjectTypes = Arrays.asList(IArchimatePackage.eINSTANCE.getStrategyElement(),
			IArchimatePackage.eINSTANCE.getBusinessElement(), IArchimatePackage.eINSTANCE.getApplicationElement(),
			IArchimatePackage.eINSTANCE.getTechnologyElement(), IArchimatePackage.eINSTANCE.getPhysicalElement(),
			IArchimatePackage.eINSTANCE.getMotivationElement(),
			IArchimatePackage.eINSTANCE.getImplementationMigrationElement());

	/**
	 * The list of Archimate Objects, per type. This is used to create the menus and
	 * the actions. The order of the items in the list is the order displayed in the
	 * menus
	 */
	public static Map<EClass, EClass[]> archimateObjects = new HashMap<>();
	static {
		archimateObjects.put(IArchimatePackage.eINSTANCE.getStrategyElement(),
				ArchimateModelUtils.getStrategyClasses());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getBusinessElement(),
				ArchimateModelUtils.getBusinessClasses());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getApplicationElement(),
				ArchimateModelUtils.getApplicationClasses());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getTechnologyElement(),
				ArchimateModelUtils.getTechnologyClasses());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getPhysicalElement(),
				ArchimateModelUtils.getPhysicalClasses());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getMotivationElement(),
				ArchimateModelUtils.getMotivationClasses());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getImplementationMigrationElement(),
				ArchimateModelUtils.getImplementationMigrationClasses());
	}

	/** The list of {@link SelectionAction} created to manage the type changes */
	public static List<ChangeElementTypeAction> actionList = null;

	/**
	 * The class that is the target of this change. That is: when this change is
	 * applied to an object, it will be transformed into an instance of this type
	 */
	private EClass targetEClass = null;

	/**
	 * Creation of all menu actions to change types
	 * 
	 * @param abstractDiagramEditor
	 * @return
	 */
	public static List<ChangeElementTypeAction> createActions(IWorkbenchPart part) {
		if (actionList == null) {
			actionList = new ArrayList<>();
			// Let's go through all archimate objects
			for (EClass type : archimateObjectTypes) {
				for (EClass eclass : archimateObjects.get(type)) {
					actionList.add(new ChangeElementTypeAction(part, ROOT_ID + "_" + eclass.getName(), eclass.getName(),
							eclass));
				}
			}
		}
		return actionList;
	}

	public static List<RetargetAction> getRetargetActions() {
		List<RetargetAction> ret = new ArrayList<>();
		// This method is called before createActions, so we can't use the actionList
		// Let's go through all archimate objects
		for (EClass type : archimateObjectTypes) {
			for (EClass eclass : archimateObjects.get(type)) {
				ret.add(new RetargetAction(ROOT_ID + "_" + eclass.getName(), eclass.getName()));
			}
		} // for
		return ret;
	}

//	/**
//	 * Returns the list of EClass, that have the give EClass as a super type
//	 * 
//	 * @param superType
//	 * @return
//	 */
//	static List<EClass> getEClassListFromSuperType(EClass superType) {
//		List<EClass> ret = new ArrayList<>();
//		// Let's find of EClass that has 'ArchimateElement' as a super type
//		for (EClassifier eClassifier : IArchimatePackage.eINSTANCE.getEClassifiers()) {
//			// We keep the EClass, that has the given EClass as a super type
//			if (eClassifier instanceof EClass && superType.isSuperTypeOf((EClass) eClassifier) && !(((EClass) eClassifier).equals(superType)))
//				ret.add((EClass) eClassifier);
//		} // for
//		return ret;
//	}

	/**
	 * Fills in the given menu, with one line per Archimate's objet (that is all
	 * objects out of relationships
	 * 
	 * @param actionRegistry
	 * @param refactorMenu   The menu to fill in
	 */
	public static void fillMenu(ActionRegistry actionRegistry, IMenuManager refactorMenu) {
		// Let's go through all archimate objects
		for (EClass type : archimateObjectTypes) {
			IMenuManager subMenu = new MenuManager(type.getName(), "menu_refactor_" + type.getName()); //$NON-NLS-1$
			refactorMenu.add(subMenu);
			for (EClass eclass : archimateObjects.get(type)) {
				subMenu.add(actionRegistry.getAction(ROOT_ID + "_" + eclass.getName()));
			}
		}
	}

	public ChangeElementTypeAction(IWorkbenchPart part, String id, String text, EClass targetEClass) {
		super(part);
		setId(id);
		setText(text);
		this.targetEClass = targetEClass;
	}

	/**
	 * Change type is enabled if the selection contains only one editable element.
	 * {@inheritDoc}
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
				Object model = ((EditPart) object).getModel();

				if (model instanceof ILockable && ((ILockable) model).isLocked()) {
					continue;
				}

				// IDiagramModelObject: any Archimate object (but not a relationship)
				if (model instanceof DiagramModelArchimateObject) {
					result.add(new ChangeElementTypeCommand((DiagramModelArchimateObject) model, targetEClass));
				}
			}
		}

		return result.unwrap();
	}

}
