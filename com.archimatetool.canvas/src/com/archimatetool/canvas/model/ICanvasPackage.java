/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.model;

import com.archimatetool.model.IArchimatePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.archimatetool.canvas.model.ICanvasFactory
 * @model kind="package"
 * @generated
 */
public interface ICanvasPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "model"; //$NON-NLS-1$

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.archimatetool.com/archimate/canvas"; //$NON-NLS-1$

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "canvas"; //$NON-NLS-1$

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ICanvasPackage eINSTANCE = com.archimatetool.canvas.model.impl.CanvasPackage.init();

    /**
     * The meta object id for the '{@link com.archimatetool.canvas.model.IHintProvider <em>Hint Provider</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.canvas.model.IHintProvider
     * @see com.archimatetool.canvas.model.impl.CanvasPackage#getHintProvider()
     * @generated
     */
    int HINT_PROVIDER = 0;

    /**
     * The feature id for the '<em><b>Hint Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HINT_PROVIDER__HINT_TITLE = 0;

    /**
     * The feature id for the '<em><b>Hint Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HINT_PROVIDER__HINT_CONTENT = 1;

    /**
     * The number of structural features of the '<em>Hint Provider</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HINT_PROVIDER_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link com.archimatetool.editor.ui.IHelpHintProvider <em>Help Hint Provider</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.editor.ui.IHelpHintProvider
     * @see com.archimatetool.canvas.model.impl.CanvasPackage#getHelpHintProvider()
     * @generated
     */
    int HELP_HINT_PROVIDER = 1;

    /**
     * The number of structural features of the '<em>Help Hint Provider</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HELP_HINT_PROVIDER_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link com.archimatetool.canvas.model.INotesContent <em>Notes Content</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.canvas.model.INotesContent
     * @see com.archimatetool.canvas.model.impl.CanvasPackage#getNotesContent()
     * @generated
     */
    int NOTES_CONTENT = 2;

    /**
     * The feature id for the '<em><b>Notes</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NOTES_CONTENT__NOTES = 0;

    /**
     * The number of structural features of the '<em>Notes Content</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NOTES_CONTENT_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link com.archimatetool.canvas.model.impl.CanvasModel <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.canvas.model.impl.CanvasModel
     * @see com.archimatetool.canvas.model.impl.CanvasPackage#getCanvasModel()
     * @generated
     */
    int CANVAS_MODEL = 3;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL__NAME = IArchimatePackage.DIAGRAM_MODEL__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL__ID = IArchimatePackage.DIAGRAM_MODEL__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL__FEATURES = IArchimatePackage.DIAGRAM_MODEL__FEATURES;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL__CHILDREN = IArchimatePackage.DIAGRAM_MODEL__CHILDREN;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL__DOCUMENTATION = IArchimatePackage.DIAGRAM_MODEL__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL__PROPERTIES = IArchimatePackage.DIAGRAM_MODEL__PROPERTIES;

    /**
     * The feature id for the '<em><b>Connection Router Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL__CONNECTION_ROUTER_TYPE = IArchimatePackage.DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE;

    /**
     * The feature id for the '<em><b>Hint Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL__HINT_TITLE = IArchimatePackage.DIAGRAM_MODEL_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Hint Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL__HINT_CONTENT = IArchimatePackage.DIAGRAM_MODEL_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_FEATURE_COUNT = IArchimatePackage.DIAGRAM_MODEL_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link com.archimatetool.canvas.model.impl.CanvasModelSticky <em>Model Sticky</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.canvas.model.impl.CanvasModelSticky
     * @see com.archimatetool.canvas.model.impl.CanvasPackage#getCanvasModelSticky()
     * @generated
     */
    int CANVAS_MODEL_STICKY = 4;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__ID = IArchimatePackage.ICONIC__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__NAME = IArchimatePackage.ICONIC__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__FEATURES = IArchimatePackage.ICONIC__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__SOURCE_CONNECTIONS = IArchimatePackage.ICONIC__SOURCE_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__TARGET_CONNECTIONS = IArchimatePackage.ICONIC__TARGET_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__FONT = IArchimatePackage.ICONIC__FONT;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__FONT_COLOR = IArchimatePackage.ICONIC__FONT_COLOR;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__LINE_WIDTH = IArchimatePackage.ICONIC__LINE_WIDTH;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__LINE_COLOR = IArchimatePackage.ICONIC__LINE_COLOR;

    /**
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__TEXT_ALIGNMENT = IArchimatePackage.ICONIC__TEXT_ALIGNMENT;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__BOUNDS = IArchimatePackage.ICONIC__BOUNDS;

    /**
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__FILL_COLOR = IArchimatePackage.ICONIC__FILL_COLOR;

    /**
     * The feature id for the '<em><b>Alpha</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__ALPHA = IArchimatePackage.ICONIC__ALPHA;

    /**
     * The feature id for the '<em><b>Image Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__IMAGE_PATH = IArchimatePackage.ICONIC__IMAGE_PATH;

    /**
     * The feature id for the '<em><b>Image Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__IMAGE_POSITION = IArchimatePackage.ICONIC__IMAGE_POSITION;

    /**
     * The feature id for the '<em><b>Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__CONTENT = IArchimatePackage.ICONIC_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Notes</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__NOTES = IArchimatePackage.ICONIC_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__PROPERTIES = IArchimatePackage.ICONIC_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Locked</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__LOCKED = IArchimatePackage.ICONIC_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Border Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__BORDER_COLOR = IArchimatePackage.ICONIC_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Text Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY__TEXT_POSITION = IArchimatePackage.ICONIC_FEATURE_COUNT + 5;

    /**
     * The number of structural features of the '<em>Model Sticky</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_STICKY_FEATURE_COUNT = IArchimatePackage.ICONIC_FEATURE_COUNT + 6;

    /**
     * The meta object id for the '{@link com.archimatetool.canvas.model.impl.CanvasModelBlock <em>Model Block</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.canvas.model.impl.CanvasModelBlock
     * @see com.archimatetool.canvas.model.impl.CanvasPackage#getCanvasModelBlock()
     * @generated
     */
    int CANVAS_MODEL_BLOCK = 5;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__ID = IArchimatePackage.ICONIC__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__NAME = IArchimatePackage.ICONIC__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__FEATURES = IArchimatePackage.ICONIC__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__SOURCE_CONNECTIONS = IArchimatePackage.ICONIC__SOURCE_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__TARGET_CONNECTIONS = IArchimatePackage.ICONIC__TARGET_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__FONT = IArchimatePackage.ICONIC__FONT;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__FONT_COLOR = IArchimatePackage.ICONIC__FONT_COLOR;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__LINE_WIDTH = IArchimatePackage.ICONIC__LINE_WIDTH;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__LINE_COLOR = IArchimatePackage.ICONIC__LINE_COLOR;

    /**
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__TEXT_ALIGNMENT = IArchimatePackage.ICONIC__TEXT_ALIGNMENT;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__BOUNDS = IArchimatePackage.ICONIC__BOUNDS;

    /**
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__FILL_COLOR = IArchimatePackage.ICONIC__FILL_COLOR;

    /**
     * The feature id for the '<em><b>Alpha</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__ALPHA = IArchimatePackage.ICONIC__ALPHA;

    /**
     * The feature id for the '<em><b>Image Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__IMAGE_PATH = IArchimatePackage.ICONIC__IMAGE_PATH;

    /**
     * The feature id for the '<em><b>Image Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__IMAGE_POSITION = IArchimatePackage.ICONIC__IMAGE_POSITION;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__CHILDREN = IArchimatePackage.ICONIC_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__PROPERTIES = IArchimatePackage.ICONIC_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Locked</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__LOCKED = IArchimatePackage.ICONIC_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Border Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__BORDER_COLOR = IArchimatePackage.ICONIC_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Hint Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__HINT_TITLE = IArchimatePackage.ICONIC_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Hint Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__HINT_CONTENT = IArchimatePackage.ICONIC_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__CONTENT = IArchimatePackage.ICONIC_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Text Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK__TEXT_POSITION = IArchimatePackage.ICONIC_FEATURE_COUNT + 7;

    /**
     * The number of structural features of the '<em>Model Block</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_BLOCK_FEATURE_COUNT = IArchimatePackage.ICONIC_FEATURE_COUNT + 8;

    /**
     * The meta object id for the '{@link com.archimatetool.canvas.model.impl.CanvasModelImage <em>Model Image</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.canvas.model.impl.CanvasModelImage
     * @see com.archimatetool.canvas.model.impl.CanvasPackage#getCanvasModelImage()
     * @generated
     */
    int CANVAS_MODEL_IMAGE = 6;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__ID = IArchimatePackage.DIAGRAM_MODEL_IMAGE__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__NAME = IArchimatePackage.DIAGRAM_MODEL_IMAGE__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__FEATURES = IArchimatePackage.DIAGRAM_MODEL_IMAGE__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__SOURCE_CONNECTIONS = IArchimatePackage.DIAGRAM_MODEL_IMAGE__SOURCE_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__TARGET_CONNECTIONS = IArchimatePackage.DIAGRAM_MODEL_IMAGE__TARGET_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__FONT = IArchimatePackage.DIAGRAM_MODEL_IMAGE__FONT;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__FONT_COLOR = IArchimatePackage.DIAGRAM_MODEL_IMAGE__FONT_COLOR;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__LINE_WIDTH = IArchimatePackage.DIAGRAM_MODEL_IMAGE__LINE_WIDTH;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__LINE_COLOR = IArchimatePackage.DIAGRAM_MODEL_IMAGE__LINE_COLOR;

    /**
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__TEXT_ALIGNMENT = IArchimatePackage.DIAGRAM_MODEL_IMAGE__TEXT_ALIGNMENT;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__BOUNDS = IArchimatePackage.DIAGRAM_MODEL_IMAGE__BOUNDS;

    /**
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__FILL_COLOR = IArchimatePackage.DIAGRAM_MODEL_IMAGE__FILL_COLOR;

    /**
     * The feature id for the '<em><b>Alpha</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__ALPHA = IArchimatePackage.DIAGRAM_MODEL_IMAGE__ALPHA;

    /**
     * The feature id for the '<em><b>Border Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__BORDER_COLOR = IArchimatePackage.DIAGRAM_MODEL_IMAGE__BORDER_COLOR;

    /**
     * The feature id for the '<em><b>Image Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__IMAGE_PATH = IArchimatePackage.DIAGRAM_MODEL_IMAGE__IMAGE_PATH;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__PROPERTIES = IArchimatePackage.DIAGRAM_MODEL_IMAGE__PROPERTIES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__DOCUMENTATION = IArchimatePackage.DIAGRAM_MODEL_IMAGE__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Locked</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE__LOCKED = IArchimatePackage.DIAGRAM_MODEL_IMAGE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Model Image</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_IMAGE_FEATURE_COUNT = IArchimatePackage.DIAGRAM_MODEL_IMAGE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link com.archimatetool.canvas.model.impl.CanvasModelConnection <em>Model Connection</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.canvas.model.impl.CanvasModelConnection
     * @see com.archimatetool.canvas.model.impl.CanvasPackage#getCanvasModelConnection()
     * @generated
     */
    int CANVAS_MODEL_CONNECTION = 7;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__ID = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__NAME = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__FEATURES = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__SOURCE_CONNECTIONS = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__SOURCE_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__TARGET_CONNECTIONS = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TARGET_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__FONT = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FONT;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__FONT_COLOR = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FONT_COLOR;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__PROPERTIES = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__PROPERTIES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__DOCUMENTATION = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__LINE_WIDTH = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__LINE_WIDTH;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__LINE_COLOR = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__LINE_COLOR;

    /**
     * The feature id for the '<em><b>Text</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__TEXT = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TEXT;

    /**
     * The feature id for the '<em><b>Text Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__TEXT_POSITION = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TEXT_POSITION;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__SOURCE = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__TARGET = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TARGET;

    /**
     * The feature id for the '<em><b>Bendpoints</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__BENDPOINTS = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__BENDPOINTS;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__TYPE = IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TYPE;

    /**
     * The feature id for the '<em><b>Locked</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION__LOCKED = IArchimatePackage.DIAGRAM_MODEL_CONNECTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Model Connection</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CANVAS_MODEL_CONNECTION_FEATURE_COUNT = IArchimatePackage.DIAGRAM_MODEL_CONNECTION_FEATURE_COUNT + 1;


    /**
     * Returns the meta object for class '{@link com.archimatetool.canvas.model.IHintProvider <em>Hint Provider</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Hint Provider</em>'.
     * @see com.archimatetool.canvas.model.IHintProvider
     * @generated
     */
    EClass getHintProvider();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.canvas.model.IHintProvider#getHintTitle <em>Hint Title</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Hint Title</em>'.
     * @see com.archimatetool.canvas.model.IHintProvider#getHintTitle()
     * @see #getHintProvider()
     * @generated
     */
    EAttribute getHintProvider_HintTitle();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.canvas.model.IHintProvider#getHintContent <em>Hint Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Hint Content</em>'.
     * @see com.archimatetool.canvas.model.IHintProvider#getHintContent()
     * @see #getHintProvider()
     * @generated
     */
    EAttribute getHintProvider_HintContent();

    /**
     * Returns the meta object for class '{@link com.archimatetool.editor.ui.IHelpHintProvider <em>Help Hint Provider</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Help Hint Provider</em>'.
     * @see com.archimatetool.editor.ui.IHelpHintProvider
     * @model instanceClass="com.archimatetool.editor.ui.IHelpHintProvider"
     * @generated
     */
    EClass getHelpHintProvider();

    /**
     * Returns the meta object for class '{@link com.archimatetool.canvas.model.INotesContent <em>Notes Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Notes Content</em>'.
     * @see com.archimatetool.canvas.model.INotesContent
     * @generated
     */
    EClass getNotesContent();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.canvas.model.INotesContent#getNotes <em>Notes</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Notes</em>'.
     * @see com.archimatetool.canvas.model.INotesContent#getNotes()
     * @see #getNotesContent()
     * @generated
     */
    EAttribute getNotesContent_Notes();

    /**
     * Returns the meta object for class '{@link com.archimatetool.canvas.model.ICanvasModel <em>Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model</em>'.
     * @see com.archimatetool.canvas.model.ICanvasModel
     * @generated
     */
    EClass getCanvasModel();

    /**
     * Returns the meta object for class '{@link com.archimatetool.canvas.model.ICanvasModelSticky <em>Model Sticky</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model Sticky</em>'.
     * @see com.archimatetool.canvas.model.ICanvasModelSticky
     * @generated
     */
    EClass getCanvasModelSticky();

    /**
     * Returns the meta object for class '{@link com.archimatetool.canvas.model.ICanvasModelBlock <em>Model Block</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model Block</em>'.
     * @see com.archimatetool.canvas.model.ICanvasModelBlock
     * @generated
     */
    EClass getCanvasModelBlock();

    /**
     * Returns the meta object for class '{@link com.archimatetool.canvas.model.ICanvasModelImage <em>Model Image</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model Image</em>'.
     * @see com.archimatetool.canvas.model.ICanvasModelImage
     * @generated
     */
    EClass getCanvasModelImage();

    /**
     * Returns the meta object for class '{@link com.archimatetool.canvas.model.ICanvasModelConnection <em>Model Connection</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model Connection</em>'.
     * @see com.archimatetool.canvas.model.ICanvasModelConnection
     * @generated
     */
    EClass getCanvasModelConnection();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    ICanvasFactory getCanvasFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link com.archimatetool.canvas.model.IHintProvider <em>Hint Provider</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.canvas.model.IHintProvider
         * @see com.archimatetool.canvas.model.impl.CanvasPackage#getHintProvider()
         * @generated
         */
        EClass HINT_PROVIDER = eINSTANCE.getHintProvider();

        /**
         * The meta object literal for the '<em><b>Hint Title</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute HINT_PROVIDER__HINT_TITLE = eINSTANCE.getHintProvider_HintTitle();

        /**
         * The meta object literal for the '<em><b>Hint Content</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute HINT_PROVIDER__HINT_CONTENT = eINSTANCE.getHintProvider_HintContent();

        /**
         * The meta object literal for the '{@link com.archimatetool.editor.ui.IHelpHintProvider <em>Help Hint Provider</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.editor.ui.IHelpHintProvider
         * @see com.archimatetool.canvas.model.impl.CanvasPackage#getHelpHintProvider()
         * @generated
         */
        EClass HELP_HINT_PROVIDER = eINSTANCE.getHelpHintProvider();

        /**
         * The meta object literal for the '{@link com.archimatetool.canvas.model.INotesContent <em>Notes Content</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.canvas.model.INotesContent
         * @see com.archimatetool.canvas.model.impl.CanvasPackage#getNotesContent()
         * @generated
         */
        EClass NOTES_CONTENT = eINSTANCE.getNotesContent();

        /**
         * The meta object literal for the '<em><b>Notes</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute NOTES_CONTENT__NOTES = eINSTANCE.getNotesContent_Notes();

        /**
         * The meta object literal for the '{@link com.archimatetool.canvas.model.impl.CanvasModel <em>Model</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.canvas.model.impl.CanvasModel
         * @see com.archimatetool.canvas.model.impl.CanvasPackage#getCanvasModel()
         * @generated
         */
        EClass CANVAS_MODEL = eINSTANCE.getCanvasModel();

        /**
         * The meta object literal for the '{@link com.archimatetool.canvas.model.impl.CanvasModelSticky <em>Model Sticky</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.canvas.model.impl.CanvasModelSticky
         * @see com.archimatetool.canvas.model.impl.CanvasPackage#getCanvasModelSticky()
         * @generated
         */
        EClass CANVAS_MODEL_STICKY = eINSTANCE.getCanvasModelSticky();

        /**
         * The meta object literal for the '{@link com.archimatetool.canvas.model.impl.CanvasModelBlock <em>Model Block</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.canvas.model.impl.CanvasModelBlock
         * @see com.archimatetool.canvas.model.impl.CanvasPackage#getCanvasModelBlock()
         * @generated
         */
        EClass CANVAS_MODEL_BLOCK = eINSTANCE.getCanvasModelBlock();

        /**
         * The meta object literal for the '{@link com.archimatetool.canvas.model.impl.CanvasModelImage <em>Model Image</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.canvas.model.impl.CanvasModelImage
         * @see com.archimatetool.canvas.model.impl.CanvasPackage#getCanvasModelImage()
         * @generated
         */
        EClass CANVAS_MODEL_IMAGE = eINSTANCE.getCanvasModelImage();

        /**
         * The meta object literal for the '{@link com.archimatetool.canvas.model.impl.CanvasModelConnection <em>Model Connection</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.canvas.model.impl.CanvasModelConnection
         * @see com.archimatetool.canvas.model.impl.CanvasPackage#getCanvasModelConnection()
         * @generated
         */
        EClass CANVAS_MODEL_CONNECTION = eINSTANCE.getCanvasModelConnection();

    }

} //ICanvasPackage
