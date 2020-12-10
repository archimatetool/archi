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
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.RetargetAction;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.impl.ArchimateElement;
import com.archimatetool.model.impl.BusinessActor;
import com.archimatetool.model.impl.BusinessRole;
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

	static List<Class<? extends IArchimateElement>> archimateElementClasses = new ArrayList<>();
	static {
		initArchimateElementClasses();
	}

	public ChangeElementTypeAction(IWorkbenchPart part) {
		super(part);
		setText(TEXT);
		setId(ID);
	}

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
			return fDiagramOldObject != null;
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

	/**
	 * This method initializes the {@link #archimateElementClasses} field, with all
	 * the Concrete class that implements this class, that is with all types of
	 * objects of ArchiMate
	 */
	static void initArchimateElementClasses() {
		for (Class<?> clazz : getComArchimatetoolModelImplClasses()) {
			if (IArchimateElement.class.isAssignableFrom(clazz)) {
				// Ok, this class is an ArchiMate's object (not a relationship or a technical
				// class)
				@SuppressWarnings("unchecked")
				Class<? extends IArchimateElement> c = (Class<? extends IArchimateElement>) clazz;
				archimateElementClasses.add(c);
			}
		} // for
	}

	/**
	 * Returns the classes contained in the com.archimatetool.model.impl package. As
	 * getting these classes from reflection is complex (need external
	 * dependencies), this way is simpler. And this list should be quite stable...
	 * 
	 * @return
	 */
	static List<Class<? extends Object>> getComArchimatetoolModelImplClasses() {
		return Arrays.asList(com.archimatetool.model.impl.AccessRelationship.class,
				com.archimatetool.model.impl.AggregationRelationship.class,
				com.archimatetool.model.impl.ApplicationCollaboration.class,
				com.archimatetool.model.impl.ApplicationComponent.class,
				com.archimatetool.model.impl.ApplicationEvent.class,
				com.archimatetool.model.impl.ApplicationFunction.class,
				com.archimatetool.model.impl.ApplicationInteraction.class,
				com.archimatetool.model.impl.ApplicationInterface.class,
				com.archimatetool.model.impl.ApplicationProcess.class,
				com.archimatetool.model.impl.ApplicationService.class,
				com.archimatetool.model.impl.ArchimateConcept.class,
				com.archimatetool.model.impl.ArchimateDiagramModel.class,
				com.archimatetool.model.impl.ArchimateElement.class,
				com.archimatetool.model.impl.ArchimateFactory.class, com.archimatetool.model.impl.ArchimateModel.class,
				com.archimatetool.model.impl.ArchimatePackage.class,
				com.archimatetool.model.impl.ArchimateRelationship.class, com.archimatetool.model.impl.Artifact.class,
				com.archimatetool.model.impl.Assessment.class,
				com.archimatetool.model.impl.AssignmentRelationship.class,
				com.archimatetool.model.impl.AssociationRelationship.class, com.archimatetool.model.impl.Bounds.class,
				com.archimatetool.model.impl.BusinessActor.class,
				com.archimatetool.model.impl.BusinessCollaboration.class,
				com.archimatetool.model.impl.BusinessEvent.class, com.archimatetool.model.impl.BusinessFunction.class,
				com.archimatetool.model.impl.BusinessInteraction.class,
				com.archimatetool.model.impl.BusinessInterface.class, com.archimatetool.model.impl.BusinessObject.class,
				com.archimatetool.model.impl.BusinessProcess.class, com.archimatetool.model.impl.BusinessRole.class,
				com.archimatetool.model.impl.BusinessService.class, com.archimatetool.model.impl.Capability.class,
				com.archimatetool.model.impl.CommunicationNetwork.class,
				com.archimatetool.model.impl.CompositionRelationship.class,
				com.archimatetool.model.impl.Connectable.class, com.archimatetool.model.impl.Constraint.class,
				com.archimatetool.model.impl.Contract.class, com.archimatetool.model.impl.CourseOfAction.class,
				com.archimatetool.model.impl.DataObject.class, com.archimatetool.model.impl.Deliverable.class,
				com.archimatetool.model.impl.Device.class, com.archimatetool.model.impl.DiagramModel.class,
				com.archimatetool.model.impl.DiagramModelArchimateConnection.class,
				com.archimatetool.model.impl.DiagramModelArchimateObject.class,
				com.archimatetool.model.impl.DiagramModelBendpoint.class,
				com.archimatetool.model.impl.DiagramModelComponent.class,
				com.archimatetool.model.impl.DiagramModelConnection.class,
				com.archimatetool.model.impl.DiagramModelGroup.class,
				com.archimatetool.model.impl.DiagramModelImage.class,
				com.archimatetool.model.impl.DiagramModelNote.class,
				com.archimatetool.model.impl.DiagramModelObject.class,
				com.archimatetool.model.impl.DiagramModelReference.class,
				com.archimatetool.model.impl.DistributionNetwork.class, com.archimatetool.model.impl.Driver.class,
				com.archimatetool.model.impl.Equipment.class, com.archimatetool.model.impl.Facility.class,
				com.archimatetool.model.impl.Feature.class, com.archimatetool.model.impl.FeaturesEList.class,
				com.archimatetool.model.impl.FlowRelationship.class, com.archimatetool.model.impl.Folder.class,
				com.archimatetool.model.impl.Gap.class, com.archimatetool.model.impl.Goal.class,
				com.archimatetool.model.impl.Grouping.class, com.archimatetool.model.impl.ImplementationEvent.class,
				com.archimatetool.model.impl.InfluenceRelationship.class, com.archimatetool.model.impl.Junction.class,
				com.archimatetool.model.impl.Location.class, com.archimatetool.model.impl.Material.class,
				com.archimatetool.model.impl.Meaning.class, com.archimatetool.model.impl.Metadata.class,
				com.archimatetool.model.impl.Node.class, com.archimatetool.model.impl.Outcome.class,
				com.archimatetool.model.impl.Path.class, com.archimatetool.model.impl.Plateau.class,
				com.archimatetool.model.impl.Principle.class, com.archimatetool.model.impl.Product.class,
				com.archimatetool.model.impl.Property.class, com.archimatetool.model.impl.RealizationRelationship.class,
				com.archimatetool.model.impl.Representation.class, com.archimatetool.model.impl.Requirement.class,
				com.archimatetool.model.impl.Resource.class, com.archimatetool.model.impl.ServingRelationship.class,
				com.archimatetool.model.impl.SketchModel.class, com.archimatetool.model.impl.SketchModelActor.class,
				com.archimatetool.model.impl.SketchModelSticky.class,
				com.archimatetool.model.impl.SpecializationRelationship.class,
				com.archimatetool.model.impl.Stakeholder.class, com.archimatetool.model.impl.SystemSoftware.class,
				com.archimatetool.model.impl.TechnologyCollaboration.class,
				com.archimatetool.model.impl.TechnologyEvent.class,
				com.archimatetool.model.impl.TechnologyFunction.class,
				com.archimatetool.model.impl.TechnologyInteraction.class,
				com.archimatetool.model.impl.TechnologyInterface.class,
				com.archimatetool.model.impl.TechnologyObject.class,
				com.archimatetool.model.impl.TechnologyProcess.class,
				com.archimatetool.model.impl.TechnologyService.class,
				com.archimatetool.model.impl.TriggeringRelationship.class, com.archimatetool.model.impl.Value.class,
				com.archimatetool.model.impl.ValueStream.class, com.archimatetool.model.impl.WorkPackage.class);
	}

	public static List<RetargetAction> getRetargetActions() {
		List<RetargetAction> ret = new ArrayList<>();
		EClass businessElement = IArchimatePackage.eINSTANCE.getBusinessElement();
		// Let's find of EClass that has 'ArchimateElement' as a super type
		for (EClassifier eClassifier : IArchimatePackage.eINSTANCE.getEClassifiers()) {
			// We keep the EClass, that has
			if (eClassifier instanceof EClass) {
				if (businessElement.isSuperTypeOf((EClass) eClassifier)) {
					ret.add(new RetargetAction(TEXT + "_" + eClassifier.getName(), eClassifier.getName()));
				}
			}
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
		refactorMenu.add(actionRegistry.getAction(GEFActionConstants.ALIGN_TOP));
	}

}
