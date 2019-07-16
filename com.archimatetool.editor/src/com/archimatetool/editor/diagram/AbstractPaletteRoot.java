/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

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
        PaletteStack stack = new PaletteStack(Messages.AbstractPaletteRoot_0, Messages.AbstractPaletteRoot_1, null);
        
        MarqueeToolEntry marquee = new MarqueeToolEntry(Messages.AbstractPaletteRoot_2);
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                Integer.valueOf(MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED_AND_RELATED_CONNECTIONS));
        stack.add(marquee);
        
        marquee = new MarqueeToolEntry(Messages.AbstractPaletteRoot_3);
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                Integer.valueOf(MarqueeSelectionTool.BEHAVIOR_NODES_TOUCHED_AND_RELATED_CONNECTIONS));
        stack.add(marquee);
        
        marquee = new MarqueeToolEntry(Messages.AbstractPaletteRoot_4);
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                Integer.valueOf(MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_CONTAINED));
        stack.add(marquee);
        
        marquee = new MarqueeToolEntry(Messages.AbstractPaletteRoot_5);
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                Integer.valueOf(MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED));
        stack.add(marquee);
        
        marquee = new MarqueeToolEntry(Messages.AbstractPaletteRoot_6);
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                Integer.valueOf(MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED));
        stack.add(marquee);
        
        marquee = new MarqueeToolEntry(Messages.AbstractPaletteRoot_7);
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                Integer.valueOf(MarqueeSelectionTool.BEHAVIOR_NODES_TOUCHED));
        stack.add(marquee);
        
        return stack;
    }
}
