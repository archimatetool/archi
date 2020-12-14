package com.archimatetool.editor.diagram.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;

import com.archimatetool.editor.diagram.ArchimateDiagramModelFactory;
import com.archimatetool.editor.diagram.actions.Messages;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.impl.ArchimateElement;
import com.archimatetool.model.impl.ArchimateFactory;
import com.archimatetool.model.impl.DiagramModelArchimateObject;

public class ChangeElementTypeCommand extends Command {

	/**
	 * The source object, which content (the Archimate Object) must be be migrated
	 * to the {@link #targetEClass}.<BR/>
	 * Note: {@link IDiagramModelObject} is an object that can contain any Archimate
	 * object (but not a relationship)
	 */
	DiagramModelArchimateObject fDiagramSourceObject;

	/**
	 * The {@link EClass} of the {@link #fDiagramSourceObject}. It is stored to
	 * allow to undo the change, if asked by the user
	 */
	EClass sourceEClass;

	/**
	 * The folder that contains the soruceElement, before any action. This can be
	 * necessary to properly undo the action
	 */
	IFolder sourceFolder;

	/**
	 * The {@link EClass} to which we must transform the given
	 * {@link IDiagramModelObject}
	 */
	EClass targetEClass;

	/**
	 * This class is used in the
	 * {@link ChangeElementTypeCommand#changeElementType(DiagramModelArchimateObject, EClass)}
	 * method, to store the parent's properties for each model
	 */
	static class ArchimateObjectModelParent {
		ArchimateObjectModelParent(IDiagramModelContainer parent, int index) {
			this.parent = parent;
			this.index = index;
		}

		/**
		 * The parent of the {@link DiagramModelArchimateObject} model. We need to store
		 * it, as we'll remove the model from its parent, before restoring it into its
		 * parent
		 */
		IDiagramModelContainer parent;
		/** The index of the model in its parent's children list */
		int index;
	}

	/**
	 * The standard constructor
	 * 
	 * @param selectedDiagramObject The selected object which content (the Archimate
	 *                              Object) must be be migrated to the
	 *                              {@link #targetEClass}
	 * @param targetEClass
	 */
	public ChangeElementTypeCommand(DiagramModelArchimateObject selectedDiagramObject, EClass targetEClass) {
		setLabel(Messages.ChangeElementTypeAction_0);
		this.fDiagramSourceObject = selectedDiagramObject;
		this.sourceEClass = fDiagramSourceObject.getArchimateElement().eClass();
		this.targetEClass = targetEClass;
		sourceFolder = (IFolder) fDiagramSourceObject.getArchimateElement().eContainer();
	}

	@Override
	public boolean canExecute() {
		// This can only be executed on ArchimateElement
		return fDiagramSourceObject != null && fDiagramSourceObject instanceof DiagramModelArchimateObject;
	}

	@Override
	public void execute() {
		changeElementType(fDiagramSourceObject, targetEClass);
	}

	@Override
	public void undo() {
		changeElementType(fDiagramSourceObject, sourceEClass);
	}

	@Override
	public void dispose() {
		fDiagramSourceObject = null;
		sourceEClass = null;
		sourceFolder = null;
		targetEClass = null;
	}

	/**
	 * This method creates a new {@link IArchimateElement} of the given
	 * {@link EClass}, linked it into the Archimate Model in replacement of the
	 * existing one.
	 * 
	 * @param dmo
	 * @param targetEClass
	 * @See {@link ArchimateDiagramModelFactory#createDiagramModelArchimateObject(IArchimateElement)}
	 */
	void changeElementType(DiagramModelArchimateObject dmo, EClass targetEClass) {

		// Creation of the Archimate object of the new type
		ArchimateElement sourceElement = (ArchimateElement) dmo.getArchimateElement();
		ArchimateElement targetElement = (ArchimateElement) createArchimateElement(targetEClass);

		// Let's store the list of IDiagramModelArchimateObject that contains this
		// element, and for each, its parent and the index in its parent's
		// children list of the model that contains the sourceElement
		Map<IDiagramModelArchimateObject, ArchimateObjectModelParent> models = new HashMap<>();
		for (IDiagramModel dm : sourceElement.getArchimateModel().getDiagramModels()) {
			for (Iterator<EObject> iter = dm.eAllContents(); iter.hasNext();) {
				EObject eObject = iter.next();
				if (eObject instanceof IDiagramModelArchimateObject) {
					IDiagramModelArchimateObject model = ((IDiagramModelArchimateObject) eObject);
					if (model.getArchimateElement() == sourceElement) {
						// We've found a IDiagramModelArchimateObject, which element is our
						// sourceElement (whose type we want to change)
						if (!models.keySet().contains(model)) {
							IDiagramModelContainer parent = (IDiagramModelContainer) model.eContainer();
							models.put(model,
									new ArchimateObjectModelParent(parent, parent.getChildren().indexOf(model)));
						}
					}
				}
			}
		}

		// Remove the dmo in case it is open in the UI with listeners attached to the
		// underlying concept
		// This will effectively remove the concept listener from the Edit Part.
		// It needs to be done in a separate loop, to avoid side effects of the children
		// removal.
		for (IDiagramModelArchimateObject model : models.keySet()) {
			models.get(model).parent.getChildren().remove(model);
		}

		// Cloning the attributes (id, name, documentation, properties) to the new
		// instance
		targetElement.setId(sourceElement.getId());
		targetElement.setName(sourceElement.getName());
		targetElement.setDocumentation(sourceElement.getDocumentation());
		targetElement.getProperties().addAll(sourceElement.getProperties());

		// Let's remove the old element from its container (its folder), and add the new
		// one instead
		sourceFolder.getElements().remove(sourceElement);
		// Let's set the target container (the folder) of the target element: the same
		// container if source and target are of the same type. Or the
		// root folder of the target type otherwise
		// TODO: manage change of element type (which implies: change of containing
		// folder)
		sourceFolder.getElements().add(targetElement);

		// Disconnect all source relationship from the old object, and connect them to
		// the new one
		// To avoid a ConcurrentModificationException, we can't directly loop on the
		// sourceRelationships list
		while (sourceElement.getSourceRelationships().size() > 0) {
			sourceElement.getSourceRelationships().get(0).setSource(targetElement);
		}

		// Disconnect all target relationship to the old object, and connect them to the
		// new one
		// To avoid a ConcurrentModificationException, we can't directly loop on the
		// targetRelationships list
		while (sourceElement.getTargetRelationships().size() > 0) {
			sourceElement.getTargetRelationships().get(0).setTarget(targetElement);
		}

		// Change the Archimate object in the containing diagram objects
		for (IDiagramModelArchimateObject model : models.keySet()) {
			model.setArchimateElement(targetElement);
			// Figure Type
			model.setType(Preferences.STORE
					.getInt(IPreferenceConstants.DEFAULT_FIGURE_PREFIX + targetElement.eClass().getName()));
		}

		// And re-attach the DMOs into their parent's children list. This which will
		// also update the UI
		for (IDiagramModelArchimateObject model : models.keySet()) {
			ArchimateObjectModelParent parentInfo = models.get(model);
			// When undoing/redoing with multiple selections, it seems that the command may
			// not be executed in the same order or reverse. So we
			// need to check first if the index is still valid.
			if (parentInfo.index >= parentInfo.parent.getChildren().size())
				parentInfo.parent.getChildren().add(model);
			else
				parentInfo.parent.getChildren().add(parentInfo.index, model);
		}
	}

	/**
	 * Create an Archimate object instance, from the given {@link EClass}. This
	 * {@link EClass} may be the {@link #targetEClass} (when doing the command), or
	 * the old EClass (when undoing the command)
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
			throw new RuntimeException(
					e.getClass().getSimpleName() + " while getting the '" + methodName + "' method: " + e.getMessage(),
					e);
		}

		// Second step: invoke the create method to create a new instance of the
		// relevant object
		try {
			return (IArchimateElement) createMethod.invoke(IArchimatePackage.eINSTANCE.getArchimateFactory());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(
					e.getClass().getSimpleName() + " while invoking the '" + methodName + "' method: " + e.getMessage(),
					e);
		}
	}
}
