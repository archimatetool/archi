/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.RetargetAction;

import com.archimatetool.editor.diagram.AbstractDiagramEditor;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.impl.ArchimateElement;
import com.archimatetool.model.impl.BusinessActor;
import com.archimatetool.model.impl.BusinessRole;
import com.archimatetool.model.impl.DiagramModelArchimateObject;
import com.archimatetool.model.impl.Value;

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

	public static final String ID = "ChangeElementTypeAction"; //$NON-NLS-1$
	public static final String TEXT = Messages.ChangeElementTypeAction_0;

	/** The list of {@link SelectionAction} created to manage the type changes */
	public static List<ChangeElementTypeAction> actionList = null;

	/**
	 * Creation of all menu actions to change types
	 * 
	 * @param abstractDiagramEditor
	 * @return
	 */
	public static List<ChangeElementTypeAction> createActions(IWorkbenchPart part) {
		if (actionList == null) {
			actionList = new ArrayList<>();
			// Let's find of EClass that has 'ArchimateElement' as a super type
			for (EClass eClassifier : getEClassListFromSuperType(IArchimatePackage.eINSTANCE.getBusinessElement())) {
				actionList.add(
						new ChangeElementTypeAction(part, ID + "_" + eClassifier.getName(), eClassifier.getName()));
			} // for
		}
		return actionList;
	}

	public static List<RetargetAction> getRetargetActions() {
		List<RetargetAction> ret = new ArrayList<>();
		// This method is called before createActions, so we can't use the actionList
		// list.
		for (EClass eClassifier : getEClassListFromSuperType(IArchimatePackage.eINSTANCE.getBusinessElement())) {
			ret.add(new RetargetAction(ID + "_" + eClassifier.getName(), eClassifier.getName()));
		} // for
		return ret;
	}

	/**
	 * Returns the list of EClass, that have the give EClass as a super type
	 * 
	 * @param superType
	 * @return
	 */
	static List<EClass> getEClassListFromSuperType(EClass superType) {
		List<EClass> ret = new ArrayList<>();
		EClass businessElement = IArchimatePackage.eINSTANCE.getBusinessElement();
		// Let's find of EClass that has 'ArchimateElement' as a super type
		for (EClassifier eClassifier : IArchimatePackage.eINSTANCE.getEClassifiers()) {
			// We keep the EClass, that has the given EClass as a super type
			if (eClassifier instanceof EClass && businessElement.isSuperTypeOf((EClass) eClassifier))
				ret.add((EClass) eClassifier);
		} // for
		return ret;
	}

	/**
	 * Fills in the given menu, with one line per Archimate's objet (that is all
	 * objects out of relationships
	 * 
	 * @param actionRegistry
	 * @param refactorMenu   The menu to fill in
	 */
	public static void fillMenu(ActionRegistry actionRegistry, IMenuManager refactorMenu) {
		for (ChangeElementTypeAction a : actionList) {
			refactorMenu.add(actionRegistry.getAction(a.getId()));
		}
	}

	public ChangeElementTypeAction(IWorkbenchPart part, String id, String text) {
		super(part);
		setId(id);
		setText(text);
	}

	/**
	 * Change type is enabled if the selection contains only one editable element.
	 * {@inheritDoc}
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
					result.add(new ChangeElementTypeCommand((IDiagramModelObject) model));
				}
			}
		}

		return result.unwrap();
	}

	private static class ChangeElementTypeCommand extends Command {

		private IDiagramModelObject fDiagramOldObject;

		public ChangeElementTypeCommand(IDiagramModelObject diagramObject) {
			this.fDiagramOldObject = diagramObject;
			setLabel(Messages.ChangeElementTypeAction_0);
		}

		@Override
		public boolean canExecute() {
			// This can only be executed on ArchimateElement
			EClass eClass = fDiagramOldObject.eClass();
			return fDiagramOldObject != null && 
					fDiagramOldObject instanceof DiagramModelArchimateObject 
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
			// TODO do something useful here
		}
	}
}
