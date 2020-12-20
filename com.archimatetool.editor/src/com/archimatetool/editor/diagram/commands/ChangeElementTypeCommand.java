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
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.impl.ArchimateElement;
import com.archimatetool.model.impl.ArchimateFactory;
import com.archimatetool.model.impl.ArchimateModel;
import com.archimatetool.model.impl.DiagramModelArchimateObject;
import com.archimatetool.model.util.ArchimateModelUtils;

public class ChangeElementTypeCommand extends Command {

	/**
	 * This could/should be user configuration parameter. <BR/>
	 * If false, the color of the {@link DiagramModelArchimateObject}s is never changed when the type object is changed. <BR/>
	 * If true and the new type is different than the other (for instance going from a Business element to an Application element), then color of each
	 * {@link DiagramModelArchimateObject} that contains this element is changed back to the default color. When undoing the action, the color of each
	 * {@link DiagramModelArchimateObject} that contains this element is restored.<BR/>
	 * Note: It's forced to false here, but the code works properly also when its value is true.
	 */
	final static boolean CHANGE_COLOR_TO_DEFAULT_IF_ELEMENT_TYPE_CHANGES = false;

	/** The current {@link ArchimateModel} */
	IArchimateModel model;

	/**
	 * The ID of the element, whose type should be changed.<BR/>
	 * Note: it can not be a {@link DiagramModelArchimateObject}, as this element may be on no diagram. And it can not be an {@link ArchimateElement},
	 * as we recreate it.
	 */
	String elementId;

	/**
	 * This map contains the list of {@link IDiagramModelArchimateObject}s that contain this element, and for each, its parent, index (in its parent's
	 * children list), colors (...) of the model that contains the sourceElement.<BR/>
	 * We can't just reuse the
	 * {@link DiagramModelUtils#findDiagramModelComponentsForArchimateConcept(IDiagramModel, com.archimatetool.model.IArchimateConcept)}, as we need
	 * to store its parent and its index in its parent's children list
	 */
	Map<IDiagramModelArchimateObject, ArchimateObjectModelProperties> modelProperties;

	/** The {@link EClass} of the {@link #fDiagramSourceObject}. It is stored to allow to undo the change, if asked by the user */
	EClass sourceEClass;

	/** The folder that contains the soruceElement, before any action. This can be necessary to properly undo the action */
	IFolder sourceFolder;

	/** The {@link EClass} to which we must transform the given {@link IDiagramModelObject} */
	EClass targetEClass;

	/**
	 * This class is used in the {@link ChangeElementTypeCommand#changeElementType(DiagramModelArchimateObject, EClass)} method, to store the
	 * properties for each model, and be able to restore them
	 */
	static class ArchimateObjectModelProperties {
		/**
		 * The parent of the {@link DiagramModelArchimateObject} model. We need to store it, as we'll remove the model from its parent, before
		 * restoring it into its parent
		 */
		IDiagramModelContainer parent;
		/** The index of the model in its parent's children list */
		int index;
		/** The fill Color of the source element, to restore it properly when undoing the action */
		String sourceFillColor;
		/** The font Color of the source element, to restore it properly when undoing the action */
		String sourceFontColor;
		/** The text Color of the source element, to restore it properly when undoing the action */
		String sourceLineColor;
	}

	/**
	 * @param model                 The current {@link ArchimateModel}
	 * @param selectedDiagramObject The selected object which content (the Archimate Object) must be be migrated to the {@link #targetEClass}
	 * @param targetEClass
	 */
	public ChangeElementTypeCommand(DiagramModelArchimateObject selectedDiagramObject, EClass targetEClass) {
		this(selectedDiagramObject.getArchimateElement(), targetEClass);
	}

	/**
	 * The standard constructor
	 * 
	 * @param model         The current {@link ArchimateModel}
	 * @param sourceElement
	 * @param targetEClass
	 */
	public ChangeElementTypeCommand(IArchimateElement sourceElement, EClass targetEClass) {
		setLabel(Messages.ChangeElementTypeAction_0);
		this.model = sourceElement.getArchimateModel();
		this.elementId = sourceElement.getId();
		this.sourceEClass = sourceElement.eClass();
		this.targetEClass = targetEClass;
		sourceFolder = (IFolder) sourceElement.eContainer();

		// Let's store the model properties, to be able to restore them, if the action is to be undone
		modelProperties = new HashMap<>();
		for (IDiagramModel dm : sourceElement.getArchimateModel().getDiagramModels()) {
			for (Iterator<EObject> iter = dm.eAllContents(); iter.hasNext();) {
				EObject eObject = iter.next();
				if (eObject instanceof IDiagramModelArchimateObject) {
					IDiagramModelArchimateObject dmo = ((IDiagramModelArchimateObject) eObject);
					if (dmo.getArchimateElement() == sourceElement) {
						// We've found a IDiagramModelArchimateObject, which element is our sourceElement (whose type we want to change)
						if (!modelProperties.keySet().contains(dmo)) {
							ArchimateObjectModelProperties props = new ArchimateObjectModelProperties();
							modelProperties.put(dmo, props);
							props.parent = (IDiagramModelContainer) dmo.eContainer();
							props.index = props.parent.getChildren().indexOf(dmo);
							props.sourceFillColor = dmo.getFillColor();
							props.sourceFontColor = dmo.getFontColor();
							props.sourceLineColor = dmo.getLineColor();
						}
					}
				}
			}
		}
	}

	@Override
	public boolean canExecute() {
		// This can only be executed on ArchimateElement, from which we know the id
		return elementId != null && ArchimateModelUtils.getObjectByID(model, elementId) != null;
	}

	@Override
	public boolean canUndo() {
		// This can only be executed on ArchimateElement, from which we know the id, and which id exists (when undoing/redoing several commands, if
		// the changeType occurs on an element that has been created by another of these command, then it's not possible to maintain the list between
		// the newly created/recreated element and the one that which type should be changed)
		return elementId != null && ArchimateModelUtils.getObjectByID(model, elementId) != null;
	}

	@Override
	public boolean canRedo() {
		// This can only be executed on ArchimateElement, from which we know the id, and which id exists (when undoing/redoing several commands, if
		// the changeType occurs on an element that has been created by another of these command, then it's not possible to maintain the list between
		// the newly created/recreated element and the one that which type should be changed)
		return elementId != null && ArchimateModelUtils.getObjectByID(model, elementId) != null;
	}

	@Override
	public void execute() {
		changeElementType(targetEClass, false);
	}

	@Override
	public void undo() {
		changeElementType(sourceEClass, true);
	}

	@Override
	public void dispose() {
		model = null;
		elementId = null;
		sourceEClass = null;
		sourceFolder = null;
		targetEClass = null;
	}

	/**
	 * This method creates a new {@link IArchimateElement} of the given {@link EClass}, linked it into the Archimate Model in replacement of the
	 * existing one.
	 * 
	 * @param dmo
	 * @param targetEClass
	 * @See {@link ArchimateDiagramModelFactory#createDiagramModelArchimateObject(IArchimateElement)}
	 */
	void changeElementType(EClass targetEClass, boolean restoreColor) {
		boolean changeColor = false;

		// Creation of the Archimate object of the new type
		ArchimateElement sourceElement = (ArchimateElement) ArchimateModelUtils.getObjectByID(model, elementId);
		ArchimateElement targetElement = (ArchimateElement) createArchimateElement(targetEClass);

		// Remove the dmo in case it is open in the UI with listeners attached to the underlying concept
		// This will effectively remove the concept listener from the Edit Part.
		// It needs to be done in a separate loop, to avoid side effects of the children removal.
		for (IDiagramModelArchimateObject model : modelProperties.keySet()) {
			modelProperties.get(model).parent.getChildren().remove(model);
		}

		// Cloning the attributes (id, name, documentation, properties) to the new instance
		targetElement.setId(sourceElement.getId());
		targetElement.setName(sourceElement.getName());
		targetElement.setDocumentation(sourceElement.getDocumentation());
		targetElement.getProperties().addAll(sourceElement.getProperties());

		IFolder currentFolder = (IFolder) sourceElement.eContainer();
		// So, we have two folders, now:
		// - sourceFolder: the folder that contains the sourceElement, before executing the command
		// - currentFolder: the folder that contains the element of the given dmo
		// These folders are the same, when executing the command.
		// They are different when undoing the command AND sourceElement and targetElement are not of the same type (for instance Business element
		// versus Application element)
		//
		// Let's remove the old element from its container (its folder), and add the new one instead
		currentFolder.getElements().remove(sourceElement);
		// Let's set the target container (the folder) of the target element: the same container if source and target are of the same type. Or the
		// root folder of the target type otherwise
		if (ArchimateModelUtils.isCorrectFolderForObject(currentFolder, targetElement)) {
			// In this case, we're doing or undoing the action, and sourceElement and targetElement are of the same type (for instance Business
			// element)
			currentFolder.getElements().add(targetElement);
			changeColor = false;
		} else if (ArchimateModelUtils.isCorrectFolderForObject(sourceFolder, targetElement)) {
			// In this case, we're undoing the action, and sourceElement and targetElement are not of the same type (for instance Business element
			// versus Application element).
			sourceFolder.getElements().add(targetElement);
			changeColor = true;
		} else {
			// In this case, we're doing the action, and sourceElement and targetElement are not of the same type (for instance Business element
			// versus Application element).

			// We must change its folder
			IFolder newTargetFolder = model.getDefaultFolderForObject(targetElement);
			newTargetFolder.getElements().add(targetElement);

			// And its color (that is: the color of each DiagramModelArchimateObject that contains it)
			// We change the color even if the color is not the default, that is: it was changed (this could be a configuration parameter, to let the
			// user choose)
			changeColor = true;
		}

		// Disconnect all source relationship from the old object, and connect them to the new one
		while (sourceElement.getSourceRelationships().size() > 0) {
			sourceElement.getSourceRelationships().get(0).setSource(targetElement);
		}
		while (sourceElement.getTargetRelationships().size() > 0) {
			sourceElement.getTargetRelationships().get(0).setTarget(targetElement);
		}

		// Change the Archimate object in the containing diagram objects
		for (IDiagramModelArchimateObject model : modelProperties.keySet()) {
			model.setArchimateElement(targetElement);
			// Figure Type
			model.setType(Preferences.STORE.getInt(IPreferenceConstants.DEFAULT_FIGURE_PREFIX + targetElement.eClass().getName()));
		}

		// And re-attach the DMOs into their parent's children list. This which will also update the UI
		// This loop also restore the color (back to the source one when undogin, and back to neutral if necessary)
		for (IDiagramModelArchimateObject model : modelProperties.keySet()) {
			ArchimateObjectModelProperties props = modelProperties.get(model);
			// When undoing/redoing with multiple selections, it seems that the command may not be executed in the same order or reverse. So we need
			// to check first if the index is still valid.
			if (props.index >= props.parent.getChildren().size())
				props.parent.getChildren().add(model);
			else
				props.parent.getChildren().add(props.index, model);

			// Shall we manage the dmo's colors ?
			if (CHANGE_COLOR_TO_DEFAULT_IF_ELEMENT_TYPE_CHANGES) {
				if (restoreColor) {
					// The source color must be restored (which means we're undoing the action)
					model.setFillColor(props.sourceFillColor);
					model.setFontColor(props.sourceFontColor);
					model.setLineColor(props.sourceLineColor);
				} else if (changeColor) {
					// Let's change the color to the default one, of needed
					model.setFillColor(null);
					model.setFontColor(null);
					model.setLineColor(null);
				}
			}
		}
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
