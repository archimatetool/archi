/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.RetargetAction;

import com.archimatetool.editor.diagram.AbstractDiagramEditor;
import com.archimatetool.editor.diagram.ArchimateDiagramModelFactory;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.impl.ArchimateElement;
import com.archimatetool.model.impl.ArchimateFactory;
import com.archimatetool.model.impl.BusinessActor;
import com.archimatetool.model.impl.BusinessRole;
import com.archimatetool.model.impl.DiagramModelArchimateObject;
import com.archimatetool.model.impl.Value;
import com.archimatetool.model.util.ArchimateModelUtils;

/**
 * Change the Type of an Element, for instance from a {@link BusinessActor} to a {@link BusinessRole}. All the existing associations are maintained,
 * and of the same type as the existing one. This may lead to association coming to or going from the current object that become invalid, according to
 * the ArchiMate specification.
 * 
 * @author Etienne Gauthier
 */
public class ChangeElementTypeAction extends SelectionAction {

	public static final String ROOT_ID = "ChangeElementTypeAction"; //$NON-NLS-1$
	public static final String TEXT = Messages.ChangeElementTypeAction_0;

	/** The list of type of ArchiMate objects, which contains the entry for the menu, to be displayed in this order */
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

	/** The list of {@link SelectionAction} created to manage the type changes */
	public static List<ChangeElementTypeAction> actionList = null;

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
		if (actionList == null) {
			actionList = new ArrayList<>();
			// Let's go through all archimate objects
			for (EClass type : archimateObjectTypes) {
				for (EClass eclass : archimateObjects.get(type)) {
					actionList.add(new ChangeElementTypeAction(part, ROOT_ID + "_" + eclass.getName(), eclass.getName(), eclass));
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
	 * Fills in the given menu, with one line per Archimate's objet (that is all objects out of relationships
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
	 * Change type is enabled if the selection contains only one editable element. {@inheritDoc}
	 */
	@Override
	protected boolean calculateEnabled() {
		List<?> selected = getSelectedObjects();

		// Quick checks (can change only one item per one item)
		if (selected.isEmpty() || selected.size() > 1) {
			return false;
		}

		for (Object object : selected) {
			if (!(object instanceof EditPart)) {
				return false;
			}
			if (!(((EditPart) object).getModel() instanceof DiagramModelArchimateObject)) {
				// The selected element must be an Archimate object (that is: not a relationship)
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

	static class ChangeElementTypeCommand extends Command {

		/**
		 * The source object, which content (the Archimate Object) must be be migrated to the {@link #targetEClass}.<BR/>
		 * Note: {@link IDiagramModelObject} is an object that can contain any Archimate object (but not a relationship)
		 */
		DiagramModelArchimateObject fDiagramSourceObject;

		/** The {@link EClass} of the {@link #fDiagramSourceObject}. It is stored to allow to undo the change, if asked by the user */
		final EClass sourceEClass;

		/** The {@link EClass} to which we must transform the given {@link IDiagramModelObject} */
		final EClass targetEClass;

		/**
		 * The standard constructor
		 * 
		 * @param selectedDiagramObject The selected object which content (the Archimate Object) must be be migrated to the {@link #targetEClass}
		 * @param targetEClass
		 */
		public ChangeElementTypeCommand(DiagramModelArchimateObject selectedDiagramObject, EClass targetEClass) {
			this.fDiagramSourceObject = selectedDiagramObject;
			setLabel(Messages.ChangeElementTypeAction_0);
			this.sourceEClass = fDiagramSourceObject.getArchimateModel().eClass();
			this.targetEClass = targetEClass;
		}

		@Override
		public boolean canExecute() {
			// This can only be executed on ArchimateElement
			EClass eClass = fDiagramSourceObject.eClass();
			return fDiagramSourceObject != null && fDiagramSourceObject instanceof DiagramModelArchimateObject
//					&& IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(fDiagramOldObject.eClass())
			;
		}

		@Override
		public void execute() {
			changeElementType(fDiagramSourceObject, targetEClass);
		}

		@Override
		public void undo() {
			changeElementType(null, null);
		}

		// TODO Check redo
		// See MoveObjectCommand

		@Override
		public void dispose() {
			fDiagramSourceObject = null;
		}

		/**
		 * This method creates a new {@link IArchimateElement} of the given {@link EClass}, linked it into the Archimate Model in replacement of the
		 * existing one.
		 * 
		 * @param dmo
		 * @param targetEClass
		 * @See {@link ArchimateDiagramModelFactory#createDiagramModelArchimateObject(IArchimateElement)}
		 */
		void changeElementType(DiagramModelArchimateObject dmo, EClass targetEClass) {
			IDiagramModelContainer parent = (IDiagramModelContainer) dmo.eContainer();
			int index = parent.getChildren().indexOf(dmo);

			// Remove the dmo in case it is open in the UI with listeners attached to the underlying concept
			// This will effectively remove the concept listener from the Edit Part
			parent.getChildren().remove(dmo);

			// Creation of the Archimate object of the new type
			ArchimateElement sourceElement = (ArchimateElement) dmo.getArchimateElement();
			ArchimateElement targetElement = (ArchimateElement) createArchimateElement(targetEClass);
			// Figure Type
			dmo.setType(Preferences.STORE.getInt(IPreferenceConstants.DEFAULT_FIGURE_PREFIX + targetElement.eClass().getName()));

			// Cloning of all attributes (name, documentation, properties...) to the new instance
			targetElement.setId(sourceElement.getId());
			targetElement.setName(sourceElement.getName());
			targetElement.setDocumentation(sourceElement.getDocumentation());

			// Let's remove the old element from its container (its folder)
			IFolder sourceFolder = (IFolder) sourceElement.eContainer();
			sourceFolder.getElements().remove(sourceElement);

			// Let's set the target container (the folder) of the target element: the same container if source and target are of the same type. Or the
			// root folder of the target type otherwise

			// TODO: manage change of element type (which implies: change of containing folder)
			sourceFolder.getElements().add(targetElement);

			// Disconnect all source relationship from the old object, and connect them to the new one
			// To avoid a ConcurrentModificationException, we can't directly loop on the sourceRelationships list
			while (sourceElement.getSourceRelationships().size() > 0) {
				sourceElement.getSourceRelationships().get(0).setSource(targetElement);
			}

			// Disconnect all target relationship to the old object, and connect them to the new one
			// To avoid a ConcurrentModificationException, we can't directly loop on the targetRelationships list
			while (sourceElement.getTargetRelationships().size() > 0) {
				sourceElement.getTargetRelationships().get(0).setTarget(targetElement);
			}

			// Set the new Archimate object into the correct folder ... If it's possible (that is: if it's of the same category)

			// Add all the Diagram Objects from the old object into the new one

			// Change the Archimate object in the current diagram object
			dmo.setArchimateElement(targetElement);

			// And re-attach which will also update the UI
			parent.getChildren().add(index, dmo);

			// TODO update other DMO that also contain this element (in the same view, in other views)
		}

		/**
		 * Create an Archimate object instance, from the given {@link EClass}. This {@link EClass} may be the {@link #targetEClass} (when doing the
		 * command), or the old EClass (when undoing the command)
		 * 
		 * @param eclass
		 * @return
		 */
		IArchimateElement createArchimateElement(EClass eclass) {
			String methodName = "create" + eclass.getName();
			Method createMethod;

			// First step: get the create method
			try {
				createMethod = ArchimateFactory.eINSTANCE.getClass().getMethod(methodName);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e.getClass().getSimpleName() + " while getting the '" + methodName + "' method: " + e.getMessage(), e);
			}

			// Second step: invoke the create method to create a new instance of the
			// relevant object
			try {
				return (IArchimateElement) createMethod.invoke(IArchimatePackage.eINSTANCE.getArchimateFactory());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e.getClass().getSimpleName() + " while invoking the '" + methodName + "' method: " + e.getMessage(), e);
			}
		}
	}
}
