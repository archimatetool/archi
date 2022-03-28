/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.tools.MarqueeSelectionTool;

import com.archimatetool.editor.diagram.tools.FormatPainterToolEntry;
import com.archimatetool.editor.diagram.tools.PanningSelectionExtendedTool;


/**
 * PaletteRoot for All Diagrams
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractPaletteRoot extends PaletteRoot {
    
    /**
     * The Format Painter Tool Entry created by sub-classes
     */
    private FormatPainterToolEntry formatPainterEntry;
    
    /**
     * Create a Group of Tools
     */
    protected PaletteContainer createToolsGroup() {
        PaletteContainer group = new PaletteToolbar(Messages.AbstractPaletteRoot_8);
        
        // The selection tool
        ToolEntry tool = new PanningSelectionToolEntry();
        tool.setToolClass(PanningSelectionExtendedTool.class);
        group.add(tool);

        // Use selection tool as default entry
        setDefaultEntry(tool);
        
        // Marquee Selection Stack
        group.add(createMarqueeSelectionStack());
        
        // Format Painter
        formatPainterEntry = new FormatPainterToolEntry();
        group.add(formatPainterEntry);

        return group;
    }
    
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
    
    public void dispose() {
        if(formatPainterEntry != null) {
            formatPainterEntry.dispose();
            formatPainterEntry = null;
        }
    }
}
