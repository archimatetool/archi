/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


/**
 * Version number of model.<p>
 * Use this to determine when loading a different version model if it will bring the whole
 * show down. Example, folders were introduced in version 1.3 of Archi but won't open in Archi 1.2.<p>
 * 
 * PLEASE NOTE - THIS IS THE VERSION OF THE MODEL, NOT ARCHI.
 * As from Archi version 2.6.0 I will try to keep the model version number the same as Archi's version number when the model version is incremented.
 * Only need to change the model version number if there are changes that affect backwards compatibility.<p>
 * 
 * History:<br>
 * 
 * 1.0.0 - Archi versions 0.7 - 1.2.0 the version number was not saved to the XMI file<br>
 * 1.1.0 - Archi version 1.3.0 introduced sub-folders which are not backwards-compatible
 * 1.1.1 - Archi version 1.4.0 added sub-folders in the Views folder which are not backwards-compatible
 * 1.2.0 - Archi version 1.5.0 added the Sketch View which is not backwards-compatible, and text alignment attribute
 * 1.2.1 - Archi version 1.6.0 added IInterfaceElement and lineColor to IDiagramModelConnection
 * 1.3.0 - Archi version 1.7.0 added accessType to IAccessRelationship, diagram shortcuts in Sketch Diagrams,
 *         type in IDiagramModelArchimateObject, plain connections in Diagrams, Properties, and Documentation to Folders
 * 2.0.0 - Archi version 2.0.0 added multiple occurrences of objects in a View, Documentation to Groups, Properties to Sketch
 *         View objects, Viewpoints, and refactoring of DiagramModel and ArchimateDiagramModel
 * 2.1.0 - The "text" attribute of IDiagramModelConnection is deprecated.
 *		   Lockable support. Borders. DiagramModelImage and IDiagramModelImageProvider
 * 2.2.0 - ArchiMate 2.0 elements.
 * 2.2.1 - Sketch Model Background attribute
 * 2.3.0 - Add ILineObject - implemented in IDiagramModelObject and IDiagramModelConnection
 * 2.6.0 - Add new model namespace URIs
 *       - Archi 2.7.0 added the Metadata element to the model. Keep the same model number as this is not catastrophic
 * 3.0.0 - Default widths and heights are not saved as -1
 * 3.1.0 - Note has border types
 * 3.1.1 - Group figure removes 18 pixel offset
 * 4.0.0 - Everything changed for ArchiMate 3.0
 * 4.0.1 - Add alpha attribute to IDiagramModelObject
 * 4.4.0 - Add Properties to IDiagramModelNote
 *       - Add Properties to IDiagramModelImage
 *       - Add Documentation IDiagramModelImage
 *       - Grouping's Text Alignment is checked for centre and converted to left
 *       - Added IBorderType to Ecore
 *       - Group can have Rectangle Border Type
 *       - Group implements ITextPosition
 * 4.6.0 - Add Features API
 *       - ArchiMate 3.1 ValueStream concept and Association relationship directed attribute
 * 4.9.0 - Add Profiles
 *       - Add images to IDiagramModelArchimateObject
 *       - Refactor IDiagramModelImageProvider, IIconic and more...
 * 
 * @author Phillip Beauvoir
 */
public interface ModelVersion {
    String VERSION = "4.9.0"; //$NON-NLS-1$
}
