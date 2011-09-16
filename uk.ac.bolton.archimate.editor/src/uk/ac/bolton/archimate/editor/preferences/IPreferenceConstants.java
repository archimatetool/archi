/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.preferences;




/**
 * Constant definitions for plug-in preferences
 * 
 * @author Phillip Beauvoir
 */
public interface IPreferenceConstants {
    String USER_DATA_FOLDER = "userDataFolder"; //$NON-NLS-1$
    
    String MRU_MAX = "mruMax"; //$NON-NLS-1$
    
    String GRID_SIZE = "gridSize"; //$NON-NLS-1$
    String GRID_VISIBLE = "gridVisible"; //$NON-NLS-1$
    String GRID_SNAP = "gridSnap"; //$NON-NLS-1$
    String GRID_SHOW_GUIDELINES = "gridShowGuidelines"; //$NON-NLS-1$
    
    String VIEW_TOOLTIPS = "viewTooltips";
    
    String OPEN_DIAGRAMS_ON_LOAD = "openDiagramsOnLoad";
    
    String ANIMATE = "animate";
    String ANIMATION_SPEED = "animationSpeed";
    String ANTI_ALIAS = "antiAlias";
    
    String DEFAULT_VIEW_FONT = "defaultViewFont";
    
    String LINK_VIEW = "linkView";
    
    String BUSINESS_INTERFACE_FIGURE = "businessInterfaceFigure";
    String APPLICATION_COMPONENT_FIGURE = "applicationComponentFigure";
    String APPLICATION_INTERFACE_FIGURE = "applicationInterfaceFigure";
    String TECHNOLOGY_NODE_FIGURE = "technologyNodeFigure";
    String TECHNOLOGY_DEVICE_FIGURE = "technologyDeviceFigure";
    String TECHNOLOGY_INTERFACE_FIGURE = "technologyInterfaceFigure";
    
    String SKETCH_SHOW_BACKGROUND = "sketchShowBackground";
    
    String PALETTE_STATE = "palette_state";

    String ANIMATE_MAGIC_CONNECTOR = "animateMagicConnector";
    String MAGIC_CONNECTOR_POLARITY = "polarityMagicConnector";
    
    String ALLOW_CIRCULAR_CONNECTIONS = "circularConnections";
    
    String USE_NESTED_CONNECTIONS = "useNestedConnections";
    String CREATE_RELATION_WHEN_ADDING_NEW_ELEMENT_TO_CONTAINER = "createRelationWhenAddingNewElementToContainer";
    String CREATE_RELATION_WHEN_ADDING_MODEL_TREE_ELEMENT_TO_CONTAINER = "createRelationWhenAddingModelTreeElementToContainer";
    String CREATE_RELATION_WHEN_MOVING_ELEMENT_TO_CONTAINER = "createRelationWhenMovingElementToContainer";
    
    String NEW_RELATIONS_TYPES = "newRelationsTypes";
    String HIDDEN_RELATIONS_TYPES = "hiddenRelationsTypes";
    
    String VIEWPOINTS_FILTER_MODEL_TREE = "viewpointsFilterModelTree";
    String VIEWPOINTS_HIDE_DIAGRAM_ELEMENTS = "viewpointsHideDiagramElements";
    String VIEWPOINTS_HIDE_PALETTE_ELEMENTS = "viewpointsHidePaletteElements";
    String VIEWPOINTS_HIDE_MAGIC_CONNECTOR_ELEMENTS = "viewpointsHideMagicConnectorElements";
}
