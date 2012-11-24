/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.tools;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.Tool;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.palette.ToolEntry;

import uk.ac.bolton.archimate.editor.diagram.tools.FormatPainterInfo.PaintFormat;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;


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
            protected CompoundCommand createCommand(PaintFormat pf, Object targetObject) {
                CompoundCommand result = super.createCommand(pf, targetObject);
                
                // Add any additional commands from Sub-classes
                Command extraCommand = FormatPainterToolEntry.this.getCommand(pf.sourceComponent, targetObject);
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
     * @param targetObject 
     * @param sourceObject 
     */
    public Command getCommand(Object sourceObject, Object targetObject) {
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
            String description = (FormatPainterInfo.INSTANCE.getPaintFormat().sourceComponent instanceof IDiagramModelConnection) ?
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
