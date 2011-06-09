/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram;

import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.tools.MarqueeSelectionTool;


/**
 * PaletteRoot for All Diagrams
 * 
 * @author Phillip Beauvoir
 */
public class AbstractPaletteRoot extends PaletteRoot {
    
    /**
     * Create a PaletteStack containing the Marquee selection tools
     */
    protected PaletteStack createMarqueeSelectionStack() {
        PaletteStack stack = new PaletteStack("Marquee Selection Tools", "Selection Tools", null);
        
        MarqueeToolEntry marquee = new MarqueeToolEntry("Select contained nodes and related connections");
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                new Integer(MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED_AND_RELATED_CONNECTIONS));
        stack.add(marquee);
        
        marquee = new MarqueeToolEntry("Select touched nodes and related connections");
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                new Integer(MarqueeSelectionTool.BEHAVIOR_NODES_TOUCHED_AND_RELATED_CONNECTIONS));
        stack.add(marquee);
        
        marquee = new MarqueeToolEntry("Select contained connections");
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                new Integer(MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_CONTAINED));
        stack.add(marquee);
        
        marquee = new MarqueeToolEntry("Select touched connections");
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                new Integer(MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED));
        stack.add(marquee);
        
        marquee = new MarqueeToolEntry("Select contained nodes");
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                new Integer(MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED));
        stack.add(marquee);
        
        marquee = new MarqueeToolEntry("Select touched nodes");
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                new Integer(MarqueeSelectionTool.BEHAVIOR_NODES_TOUCHED));
        stack.add(marquee);
        
        return stack;
    }
}
