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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
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
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.impl.ArchimateElement;
import com.archimatetool.model.impl.ArchimateFactory;
import com.archimatetool.model.impl.BusinessActor;
import com.archimatetool.model.impl.BusinessRole;
import com.archimatetool.model.impl.DiagramModelArchimateObject;
import com.archimatetool.model.impl.Value;

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
	public static Map<EClass, List<EClass>> archimateObjects = new HashMap<>();
	static {
		List<EClass> eclasses = new ArrayList<>();
		eclasses.add(IArchimatePackage.eINSTANCE.getCapability());
		eclasses.add(IArchimatePackage.eINSTANCE.getCourseOfAction());
		eclasses.add(IArchimatePackage.eINSTANCE.getResource());
		eclasses.add(IArchimatePackage.eINSTANCE.getValueStream());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getStrategyElement(), eclasses);

		eclasses = new ArrayList<>();
		eclasses.add(IArchimatePackage.eINSTANCE.getBusinessActor());
		eclasses.add(IArchimatePackage.eINSTANCE.getBusinessCollaboration());
		eclasses.add(IArchimatePackage.eINSTANCE.getBusinessEvent());
		eclasses.add(IArchimatePackage.eINSTANCE.getBusinessFunction());
		eclasses.add(IArchimatePackage.eINSTANCE.getBusinessInteraction());
		eclasses.add(IArchimatePackage.eINSTANCE.getBusinessInterface());
		eclasses.add(IArchimatePackage.eINSTANCE.getBusinessObject());
		eclasses.add(IArchimatePackage.eINSTANCE.getBusinessProcess());
		eclasses.add(IArchimatePackage.eINSTANCE.getBusinessRole());
		eclasses.add(IArchimatePackage.eINSTANCE.getBusinessService());
		eclasses.add(IArchimatePackage.eINSTANCE.getContract());
		eclasses.add(IArchimatePackage.eINSTANCE.getProduct());
		eclasses.add(IArchimatePackage.eINSTANCE.getRepresentation());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getBusinessElement(), eclasses);

		eclasses = new ArrayList<>();
		eclasses.add(IArchimatePackage.eINSTANCE.getApplicationCollaboration());
		eclasses.add(IArchimatePackage.eINSTANCE.getApplicationComponent());
		eclasses.add(IArchimatePackage.eINSTANCE.getApplicationEvent());
		eclasses.add(IArchimatePackage.eINSTANCE.getApplicationFunction());
		eclasses.add(IArchimatePackage.eINSTANCE.getApplicationInteraction());
		eclasses.add(IArchimatePackage.eINSTANCE.getApplicationInterface());
		eclasses.add(IArchimatePackage.eINSTANCE.getApplicationProcess());
		eclasses.add(IArchimatePackage.eINSTANCE.getApplicationService());
		eclasses.add(IArchimatePackage.eINSTANCE.getDataObject());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getApplicationElement(), eclasses);

		eclasses = new ArrayList<>();
		eclasses.add(IArchimatePackage.eINSTANCE.getTechnologyCollaboration());
		eclasses.add(IArchimatePackage.eINSTANCE.getTechnologyEvent());
		eclasses.add(IArchimatePackage.eINSTANCE.getTechnologyFunction());
		eclasses.add(IArchimatePackage.eINSTANCE.getTechnologyInteraction());
		eclasses.add(IArchimatePackage.eINSTANCE.getTechnologyInterface());
		eclasses.add(IArchimatePackage.eINSTANCE.getTechnologyProcess());
		eclasses.add(IArchimatePackage.eINSTANCE.getTechnologyService());
		eclasses.add(IArchimatePackage.eINSTANCE.getNode());
		eclasses.add(IArchimatePackage.eINSTANCE.getDevice());
		eclasses.add(IArchimatePackage.eINSTANCE.getSystemSoftware());
		eclasses.add(IArchimatePackage.eINSTANCE.getPath());
		eclasses.add(IArchimatePackage.eINSTANCE.getCommunicationNetwork());
		eclasses.add(IArchimatePackage.eINSTANCE.getArtifact());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getTechnologyElement(), eclasses);

		eclasses = new ArrayList<>();
		eclasses.add(IArchimatePackage.eINSTANCE.getEquipment());
		eclasses.add(IArchimatePackage.eINSTANCE.getFacility());
		eclasses.add(IArchimatePackage.eINSTANCE.getDistributionNetwork());
		eclasses.add(IArchimatePackage.eINSTANCE.getMaterial());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getPhysicalElement(), eclasses);

		eclasses = new ArrayList<>();
		eclasses.add(IArchimatePackage.eINSTANCE.getStakeholder());
		eclasses.add(IArchimatePackage.eINSTANCE.getDriver());
		eclasses.add(IArchimatePackage.eINSTANCE.getAssessment());
		eclasses.add(IArchimatePackage.eINSTANCE.getGoal());
		eclasses.add(IArchimatePackage.eINSTANCE.getOutcome());
		eclasses.add(IArchimatePackage.eINSTANCE.getPrinciple());
		eclasses.add(IArchimatePackage.eINSTANCE.getRequirement());
		eclasses.add(IArchimatePackage.eINSTANCE.getConstraint());
		eclasses.add(IArchimatePackage.eINSTANCE.getMeaning());
		eclasses.add(IArchimatePackage.eINSTANCE.getValue());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getMotivationElement(), eclasses);

		eclasses = new ArrayList<>();
		eclasses.add(IArchimatePackage.eINSTANCE.getWorkPackage());
		eclasses.add(IArchimatePackage.eINSTANCE.getDeliverable());
		eclasses.add(IArchimatePackage.eINSTANCE.getImplementationEvent());
		eclasses.add(IArchimatePackage.eINSTANCE.getPlateau());
		eclasses.add(IArchimatePackage.eINSTANCE.getGap());
		archimateObjects.put(IArchimatePackage.eINSTANCE.getImplementationMigrationElement(), eclasses);
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
				if (model instanceof IDiagramModelObject) {
					result.add(new ChangeElementTypeCommand((IDiagramModelObject) model, targetEClass));
				}
			}
		}

		return result.unwrap();
	}

	static class ChangeElementTypeCommand extends Command {

		// IDiagramModelObject: any Archimate object (but not a relationship)
		private IDiagramModelObject fDiagramOldObject;

		/**
		 * The {@link EClass} to which we must transform the given {@link IDiagramModelObject}
		 */
		EClass targetEClass;

		public ChangeElementTypeCommand(IDiagramModelObject diagramObject, EClass targetEClass) {
			this.fDiagramOldObject = diagramObject;
			setLabel(Messages.ChangeElementTypeAction_0);
			this.targetEClass = targetEClass;
		}

		@Override
		public boolean canExecute() {
			// This can only be executed on ArchimateElement
			EClass eClass = fDiagramOldObject.eClass();
			return fDiagramOldObject != null && fDiagramOldObject instanceof DiagramModelArchimateObject
//					&& IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(fDiagramOldObject.eClass())
			;
		}

		@Override
		public void execute() {
			changeElementType();
		}

		@Override
		public void undo() {
			changeElementType();
		}

		@Override
		public void dispose() {
			fDiagramOldObject = null;
		}

		void changeElementType() {
			// Creation of the Archimate object of the new type
			IDiagramModelArchimateObject diagramModelArchimateObject = IArchimatePackage.eINSTANCE.getArchimateFactory()
					.createDiagramModelArchimateObject();
			IArchimateElement archimateElement = createArchimateElement(targetEClass);

			// Cloning of all attributes (name, documentation, properties...) to the new instance

			// Disconnect all source relationship from the old object, and connect them to the new one

			// Disconnect all target relationship to the old object, and connect them to the new one

			// Set the new Archimate object into the correct folder ... If it's possible (that is: if it's of the same category)

			// Add all the Diagram Objects from the old object into the new one

			// Change the Archimate object in the current diagram object

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
