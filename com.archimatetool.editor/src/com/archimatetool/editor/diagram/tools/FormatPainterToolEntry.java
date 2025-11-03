/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.ToolEntry;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * Format Painter ToolEntry
 * 
 * @author Phillip Beauvoir
 */
public class FormatPainterToolEntry extends ToolEntry implements PropertyChangeListener {

    private FormatPainterTool tool;
    
    public FormatPainterToolEntry() {
        super("", "", null, null); //$NON-NLS-1$ //$NON-NLS-2$
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
        if(tool != null) { // tool might not have been created yet
            // Bug on Linux X11 where the colored cursor doesn't update unless the cursor is set again
            if(PlatformUtils.isLinuxX11()) {
                tool.setDefaultCursor(null);
            }
            tool.setDefaultCursor(FormatPainterInfo.INSTANCE.getCursor());
        }
        
        setLabels();
        setIcons();
    }
    
    private void setLabels() {
        if(FormatPainterInfo.INSTANCE.hasSourceComponent()) {
            setLabel(Messages.FormatPainterToolEntry_0);
            String description = FormatPainterInfo.INSTANCE.getSourceComponent() instanceof IDiagramModelConnection ?
                                    Messages.FormatPainterToolEntry_1 : Messages.FormatPainterToolEntry_2;
            description += "\n" + Messages.FormatPainterToolEntry_3; //$NON-NLS-1$
            setDescription(description);
        }
        else {
            setLabel(Messages.FormatPainterToolEntry_4);
            setDescription(Messages.FormatPainterToolEntry_5);
        }
    }
    
    private void setIcons() {
        if(FormatPainterInfo.INSTANCE.hasSourceComponent()) {
            setLargeIcon(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_FORMAT_PAINTER));
            setSmallIcon(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_FORMAT_PAINTER));
        }
        else {
            setLargeIcon(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_FORMAT_PAINTER_GREY));
            setSmallIcon(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_FORMAT_PAINTER_GREY));
        }
    }
    
    public void dispose() {
        FormatPainterInfo.INSTANCE.removePropertyChangeListener(this);
        tool = null;
    }
}
