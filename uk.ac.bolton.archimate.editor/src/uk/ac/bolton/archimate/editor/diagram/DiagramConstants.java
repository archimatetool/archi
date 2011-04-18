/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram;


public interface DiagramConstants {

    // TODO Convert these and IDiagramModelConnection.type to integers and deprecate CONNECTION_ARROW_OLD and CONNECTION_DASHED_ARROW_OLD
    String CONNECTION_LINE = null; // default
    String CONNECTION_ARROW = "1";
    String CONNECTION_DASHED_ARROW = "2";
    String CONNECTION_DASHED = "3";
    String CONNECTION_DOTTED = "4";

    // Deprecated
    String CONNECTION_ARROW_OLD = "sketch_arrow";
    String CONNECTION_DASHED_ARROW_OLD = "sketch_dashed_arrow";
}
