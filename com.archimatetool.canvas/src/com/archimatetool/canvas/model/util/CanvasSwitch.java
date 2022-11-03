/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.model.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

import com.archimatetool.canvas.model.*;
import com.archimatetool.editor.ui.IHelpHintProvider;
import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IBorderObject;
import com.archimatetool.model.ICloneable;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.model.IDiagramModelImageProvider;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDocumentable;
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.IFontAttribute;
import com.archimatetool.model.IIconic;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.ILineObject;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextContent;
import com.archimatetool.model.ITextPosition;


/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see com.archimatetool.canvas.model.ICanvasPackage
 * @generated
 */
public class CanvasSwitch<T> extends Switch<T> {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static ICanvasPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CanvasSwitch() {
        if (modelPackage == null) {
            modelPackage = ICanvasPackage.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param ePackage the package in question.
     * @return whether this is a switch for the given package.
     * @generated
     */
    @Override
    protected boolean isSwitchFor(EPackage ePackage) {
        return ePackage == modelPackage;
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case ICanvasPackage.HINT_PROVIDER: {
                IHintProvider hintProvider = (IHintProvider)theEObject;
                T result = caseHintProvider(hintProvider);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ICanvasPackage.HELP_HINT_PROVIDER: {
                IHelpHintProvider helpHintProvider = (IHelpHintProvider)theEObject;
                T result = caseHelpHintProvider(helpHintProvider);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ICanvasPackage.NOTES_CONTENT: {
                INotesContent notesContent = (INotesContent)theEObject;
                T result = caseNotesContent(notesContent);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ICanvasPackage.CANVAS_MODEL: {
                ICanvasModel canvasModel = (ICanvasModel)theEObject;
                T result = caseCanvasModel(canvasModel);
                if (result == null) result = caseDiagramModel(canvasModel);
                if (result == null) result = caseHintProvider(canvasModel);
                if (result == null) result = caseHelpHintProvider(canvasModel);
                if (result == null) result = caseDiagramModelContainer(canvasModel);
                if (result == null) result = caseDocumentable(canvasModel);
                if (result == null) result = caseProperties(canvasModel);
                if (result == null) result = caseFeatures(canvasModel);
                if (result == null) result = caseDiagramModelComponent(canvasModel);
                if (result == null) result = caseArchimateModelObject(canvasModel);
                if (result == null) result = caseAdapter(canvasModel);
                if (result == null) result = caseNameable(canvasModel);
                if (result == null) result = caseIdentifier(canvasModel);
                if (result == null) result = caseCloneable(canvasModel);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ICanvasPackage.CANVAS_MODEL_STICKY: {
                ICanvasModelSticky canvasModelSticky = (ICanvasModelSticky)theEObject;
                T result = caseCanvasModelSticky(canvasModelSticky);
                if (result == null) result = caseIconic(canvasModelSticky);
                if (result == null) result = caseTextContent(canvasModelSticky);
                if (result == null) result = caseNotesContent(canvasModelSticky);
                if (result == null) result = caseProperties(canvasModelSticky);
                if (result == null) result = caseLockable(canvasModelSticky);
                if (result == null) result = caseBorderObject(canvasModelSticky);
                if (result == null) result = caseTextPosition(canvasModelSticky);
                if (result == null) result = caseDiagramModelObject(canvasModelSticky);
                if (result == null) result = caseDiagramModelImageProvider(canvasModelSticky);
                if (result == null) result = caseConnectable(canvasModelSticky);
                if (result == null) result = caseFontAttribute(canvasModelSticky);
                if (result == null) result = caseLineObject(canvasModelSticky);
                if (result == null) result = caseTextAlignment(canvasModelSticky);
                if (result == null) result = caseDiagramModelComponent(canvasModelSticky);
                if (result == null) result = caseCloneable(canvasModelSticky);
                if (result == null) result = caseArchimateModelObject(canvasModelSticky);
                if (result == null) result = caseIdentifier(canvasModelSticky);
                if (result == null) result = caseAdapter(canvasModelSticky);
                if (result == null) result = caseNameable(canvasModelSticky);
                if (result == null) result = caseFeatures(canvasModelSticky);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ICanvasPackage.CANVAS_MODEL_BLOCK: {
                ICanvasModelBlock canvasModelBlock = (ICanvasModelBlock)theEObject;
                T result = caseCanvasModelBlock(canvasModelBlock);
                if (result == null) result = caseIconic(canvasModelBlock);
                if (result == null) result = caseDiagramModelContainer(canvasModelBlock);
                if (result == null) result = caseProperties(canvasModelBlock);
                if (result == null) result = caseLockable(canvasModelBlock);
                if (result == null) result = caseBorderObject(canvasModelBlock);
                if (result == null) result = caseHelpHintProvider(canvasModelBlock);
                if (result == null) result = caseHintProvider(canvasModelBlock);
                if (result == null) result = caseTextContent(canvasModelBlock);
                if (result == null) result = caseTextPosition(canvasModelBlock);
                if (result == null) result = caseDiagramModelObject(canvasModelBlock);
                if (result == null) result = caseDiagramModelImageProvider(canvasModelBlock);
                if (result == null) result = caseConnectable(canvasModelBlock);
                if (result == null) result = caseFontAttribute(canvasModelBlock);
                if (result == null) result = caseLineObject(canvasModelBlock);
                if (result == null) result = caseTextAlignment(canvasModelBlock);
                if (result == null) result = caseDiagramModelComponent(canvasModelBlock);
                if (result == null) result = caseCloneable(canvasModelBlock);
                if (result == null) result = caseArchimateModelObject(canvasModelBlock);
                if (result == null) result = caseIdentifier(canvasModelBlock);
                if (result == null) result = caseAdapter(canvasModelBlock);
                if (result == null) result = caseNameable(canvasModelBlock);
                if (result == null) result = caseFeatures(canvasModelBlock);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ICanvasPackage.CANVAS_MODEL_IMAGE: {
                ICanvasModelImage canvasModelImage = (ICanvasModelImage)theEObject;
                T result = caseCanvasModelImage(canvasModelImage);
                if (result == null) result = caseDiagramModelImage(canvasModelImage);
                if (result == null) result = caseLockable(canvasModelImage);
                if (result == null) result = caseDiagramModelObject(canvasModelImage);
                if (result == null) result = caseBorderObject(canvasModelImage);
                if (result == null) result = caseDiagramModelImageProvider(canvasModelImage);
                if (result == null) result = caseProperties(canvasModelImage);
                if (result == null) result = caseDocumentable(canvasModelImage);
                if (result == null) result = caseConnectable(canvasModelImage);
                if (result == null) result = caseFontAttribute(canvasModelImage);
                if (result == null) result = caseLineObject(canvasModelImage);
                if (result == null) result = caseTextAlignment(canvasModelImage);
                if (result == null) result = caseDiagramModelComponent(canvasModelImage);
                if (result == null) result = caseCloneable(canvasModelImage);
                if (result == null) result = caseArchimateModelObject(canvasModelImage);
                if (result == null) result = caseIdentifier(canvasModelImage);
                if (result == null) result = caseAdapter(canvasModelImage);
                if (result == null) result = caseNameable(canvasModelImage);
                if (result == null) result = caseFeatures(canvasModelImage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ICanvasPackage.CANVAS_MODEL_CONNECTION: {
                ICanvasModelConnection canvasModelConnection = (ICanvasModelConnection)theEObject;
                T result = caseCanvasModelConnection(canvasModelConnection);
                if (result == null) result = caseDiagramModelConnection(canvasModelConnection);
                if (result == null) result = caseLockable(canvasModelConnection);
                if (result == null) result = caseConnectable(canvasModelConnection);
                if (result == null) result = caseFontAttribute(canvasModelConnection);
                if (result == null) result = caseProperties(canvasModelConnection);
                if (result == null) result = caseDocumentable(canvasModelConnection);
                if (result == null) result = caseLineObject(canvasModelConnection);
                if (result == null) result = caseDiagramModelComponent(canvasModelConnection);
                if (result == null) result = caseCloneable(canvasModelConnection);
                if (result == null) result = caseArchimateModelObject(canvasModelConnection);
                if (result == null) result = caseIdentifier(canvasModelConnection);
                if (result == null) result = caseAdapter(canvasModelConnection);
                if (result == null) result = caseNameable(canvasModelConnection);
                if (result == null) result = caseFeatures(canvasModelConnection);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCanvasModel(ICanvasModel object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model Sticky</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model Sticky</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCanvasModelSticky(ICanvasModelSticky object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model Block</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model Block</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCanvasModelBlock(ICanvasModelBlock object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model Image</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model Image</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCanvasModelImage(ICanvasModelImage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model Connection</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCanvasModelConnection(ICanvasModelConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Image Provider</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Image Provider</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelImageProvider(IDiagramModelImageProvider object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArchimateModelObject(IArchimateModelObject object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Hint Provider</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Hint Provider</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseHintProvider(IHintProvider object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Help Hint Provider</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Help Hint Provider</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseHelpHintProvider(IHelpHintProvider object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Notes Content</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Notes Content</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNotesContent(INotesContent object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Adapter</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Adapter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAdapter(IAdapter object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Identifier</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Identifier</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIdentifier(IIdentifier object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cloneable</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cloneable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCloneable(ICloneable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Nameable</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Nameable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNameable(INameable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Features</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Features</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatures(IFeatures object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Component</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Component</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelComponent(IDiagramModelComponent object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Connectable</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Connectable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConnectable(IConnectable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Container</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Container</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelContainer(IDiagramModelContainer object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Documentable</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Documentable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDocumentable(IDocumentable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Properties</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Properties</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProperties(IProperties object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModel(IDiagramModel object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Font Attribute</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Font Attribute</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFontAttribute(IFontAttribute object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Line Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Line Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLineObject(ILineObject object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Text Alignment</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Text Alignment</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTextAlignment(ITextAlignment object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelObject(IDiagramModelObject object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Text Content</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Text Content</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTextContent(ITextContent object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Lockable</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Lockable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLockable(ILockable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Border Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Border Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBorderObject(IBorderObject object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Text Position</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Text Position</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTextPosition(ITextPosition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Image</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Image</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelImage(IDiagramModelImage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Connection</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelConnection(IDiagramModelConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Iconic</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Iconic</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIconic(IIconic object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    @Override
    public T defaultCase(EObject object) {
        return null;
    }

} //CanvasSwitch
