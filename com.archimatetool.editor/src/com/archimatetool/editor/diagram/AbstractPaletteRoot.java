/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.tools.MarqueeSelectionTool;

import com.archimatetool.editor.diagram.figures.MarqueeSelectionFigure;
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
        stack.add(createMarqueeToolEntry(Messages.AbstractPaletteRoot_2, MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED_AND_RELATED_CONNECTIONS));
        stack.add(createMarqueeToolEntry(Messages.AbstractPaletteRoot_3, MarqueeSelectionTool.BEHAVIOR_NODES_TOUCHED_AND_RELATED_CONNECTIONS));
        stack.add(createMarqueeToolEntry(Messages.AbstractPaletteRoot_4, MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_CONTAINED));
        stack.add(createMarqueeToolEntry(Messages.AbstractPaletteRoot_5, MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED));
        stack.add(createMarqueeToolEntry(Messages.AbstractPaletteRoot_6, MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED));
        stack.add(createMarqueeToolEntry(Messages.AbstractPaletteRoot_7, MarqueeSelectionTool.BEHAVIOR_NODES_TOUCHED));
        return stack;
    }
    
    /**
     * @return a MarqueeToolEntry using MarqueeSelectionTool with CustomMarqueeRectangleFigure
     */
    protected MarqueeToolEntry createMarqueeToolEntry(String label, int behaviour) {
        MarqueeToolEntry entry = new MarqueeToolEntry(label) {
            @Override
            public Tool createTool() {
                Tool tool = new MarqueeSelectionTool() {
                    @Override
                    protected IFigure createMarqueeRectangleFigure() {
                        return new MarqueeSelectionFigure();
                    }
                };
                
                tool.setProperties(getToolProperties());
                return tool;
            }
        };
        
        entry.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, behaviour);
        return entry;
    }
    
    public void dispose() {
        if(formatPainterEntry != null) {
            formatPainterEntry.dispose();
            formatPainterEntry = null;
        }
    }
}
