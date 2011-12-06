/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.widgets;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Activator
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateWidgetsPlugin extends AbstractUIPlugin {
    
    public static final String PLUGIN_ID = "uk.ac.bolton.archimate.widgets";

    /**
     * The shared instance
     */
    public static ArchimateWidgetsPlugin INSTANCE;
    

    public ArchimateWidgetsPlugin() {
        INSTANCE = this;
    }
}
