/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.tools;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.ToolEntry;

import uk.ac.bolton.archimate.editor.diagram.tools.FormatPainterInfo.ConnectionPaintFormat;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;


/**
 * Format Painter ToolEntry
 * 
 * @author Phillip Beauvoir
 */
public class FormatPainterToolEntry extends ToolEntry implements PropertyChangeListener {

    private FormatPainterTool tool;
    
    public FormatPainterToolEntry() {
        super("", "", null, null);
        setToolClass(FormatPainterTool.class);
        setLabels();
        setIcons();
        FormatPainterInfo.INSTANCE.addPropertyChangeListener(this);
    }
    
    @Override
    public Tool createTool() {
        tool = (FormatPainterTool)super.createTool();
        return tool;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(tool != null) {
            tool.setDefaultCursor(FormatPainterInfo.INSTANCE.getCursor());
        }
        
        setLabels();
        setIcons();
    }
    
    private void setLabels() {
        if(FormatPainterInfo.INSTANCE.isFat()) {
            setLabel("Format Painter (primed)");
            String type = (FormatPainterInfo.INSTANCE.getPaintFormat() instanceof ConnectionPaintFormat) ?
                    "a connection" : "an element";
            setDescription("Click on " + type + " to paste the format.\nDouble-click this tool or the canvas to clear");
        }
        else {
            setLabel("Format Painter (empty)");
            setDescription("Click on an element or connection to copy its formatting");
        }
    }
    
    private void setIcons() {
        if(FormatPainterInfo.INSTANCE.isFat()) {
            setLargeIcon(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_FORMAT_PAINTER_16));
            setSmallIcon(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_FORMAT_PAINTER_16));
        }
        else {
            setLargeIcon(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_FORMAT_PAINTER_GREY_16));
            setSmallIcon(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_FORMAT_PAINTER_GREY_16));
        }
    }
    
    public void dispose() {
        FormatPainterInfo.INSTANCE.removePropertyChangeListener(this);
    }
}
