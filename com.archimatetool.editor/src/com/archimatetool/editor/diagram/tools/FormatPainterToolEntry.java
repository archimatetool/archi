/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.Tool;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.palette.ToolEntry;

import com.archimatetool.editor.diagram.tools.FormatPainterInfo.PaintFormat;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * Format Painter ToolEntry
 * 
 * @author Phillip Beauvoir
 */
public class FormatPainterToolEntry extends ToolEntry implements PropertyChangeListener {

    protected FormatPainterTool tool;
    
    public FormatPainterToolEntry() {
        super("", "", null, null); //$NON-NLS-1$ //$NON-NLS-2$
        setToolClass(FormatPainterTool.class);
        setLabels();
        setIcons();
        FormatPainterInfo.INSTANCE.addPropertyChangeListener(this);
    }
    
    @Override
    public Tool createTool() {
        tool = new FormatPainterTool() {
            @Override
            protected CompoundCommand createCommand(PaintFormat pf, IDiagramModelComponent targetComponent) {
                CompoundCommand result = super.createCommand(pf, targetComponent);
                
                // Add any additional commands from Sub-classes
                Command extraCommand = FormatPainterToolEntry.this.getCommand(pf.getSourceComponent(), targetComponent);
                if(extraCommand != null && extraCommand.canExecute()) {
                    result.add(extraCommand);
                }
                
                return result;
            }
        };
        tool.setProperties(getToolProperties());
        return tool;
    }
    
    /**
     * Sub-classes can add additional commands to the Main Command that will be executed when the format paint is applied
     * @param sourceComponent 
     * @param targetComponent 
     */
    public Command getCommand(IDiagramModelComponent sourceComponent, IDiagramModelComponent targetComponent) {
        return null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(tool != null) {
            tool.setDefaultCursor(FormatPainterInfo.INSTANCE.getCursor());
        }
        
        setLabels();
        setIcons();
    }
    
    protected void setLabels() {
        if(FormatPainterInfo.INSTANCE.isFat()) {
            setLabel(Messages.FormatPainterToolEntry_0);
            
            String description1 = Messages.FormatPainterToolEntry_1;
            String description2 = Messages.FormatPainterToolEntry_2;
            String description = (FormatPainterInfo.INSTANCE.getPaintFormat().getSourceComponent() instanceof IDiagramModelConnection) ?
                    description1 : description2;
            
            description += "\n" + Messages.FormatPainterToolEntry_3; //$NON-NLS-1$
            setDescription(description);
        }
        else {
            setLabel(Messages.FormatPainterToolEntry_4);
            setDescription(Messages.FormatPainterToolEntry_5);
        }
    }
    
    protected void setIcons() {
        if(FormatPainterInfo.INSTANCE.isFat()) {
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
    }
}
