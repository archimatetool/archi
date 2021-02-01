/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.tools.AbstractTool;

import com.archimatetool.editor.diagram.commands.BorderColorCommand;
import com.archimatetool.editor.diagram.commands.ConnectionTextPositionCommand;
import com.archimatetool.editor.diagram.commands.DiagramModelObjectAlphaCommand;
import com.archimatetool.editor.diagram.commands.DiagramModelObjectOutlineAlphaCommand;
import com.archimatetool.editor.diagram.commands.FillColorCommand;
import com.archimatetool.editor.diagram.commands.FontColorCommand;
import com.archimatetool.editor.diagram.commands.FontStyleCommand;
import com.archimatetool.editor.diagram.commands.LineColorCommand;
import com.archimatetool.editor.diagram.commands.LineWidthCommand;
import com.archimatetool.editor.diagram.commands.TextAlignmentCommand;
import com.archimatetool.editor.diagram.commands.TextPositionCommand;
import com.archimatetool.editor.diagram.tools.FormatPainterInfo.PaintFormat;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.model.commands.FeatureCommand;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IBorderObject;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFontAttribute;
import com.archimatetool.model.IIconic;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.ILineObject;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;



/**
 * Format Painter Tool
 * 
 * @author Phillip Beauvoir
 */
public class FormatPainterTool extends AbstractTool {
    
    public FormatPainterTool() {
        setDefaultCursor(FormatPainterInfo.INSTANCE.getCursor());
    }
    
    @Override
    protected boolean handleButtonUp(int button) {
        if(button == 1) {
            Point pt = getLocation();
            EditPart editpart = getCurrentViewer().findObjectAt(pt);
            if(editpart != null && editpart.getModel() != null) {
                Object object = editpart.getModel();
                if(isPaintableObject(object)) {
                    PaintFormat pf = FormatPainterInfo.INSTANCE.getPaintFormat();
                    if(pf == null) {
                        FormatPainterInfo.INSTANCE.updatePaintFormat((IDiagramModelComponent)object);
                    }
                    else if(!isObjectLocked(object)) {
                        Command cmd = createCommand(pf, (IDiagramModelComponent)object);
                        if(cmd.canExecute()) {
                            executeCommand(cmd);
                        }
                    }
                }

                return true;
            }
        }
        
        return false;
    }
    
    @Override
    protected boolean handleDoubleClick(int button) {
        /*
         * Double-click on canvas clears it
         */
        if(button == 1) {
            Point pt = getLocation();
            EditPart editpart = getCurrentViewer().findObjectAt(pt);
            if(editpart == getCurrentViewer().getRootEditPart()) {
                FormatPainterInfo.INSTANCE.reset();
                return true;
            }
            else return handleButtonUp(1);
        }
        return false;
    }
    
    protected CompoundCommand createCommand(PaintFormat pf, IDiagramModelComponent targetComponent) {
        CompoundCommand result = new CompoundCommand(Messages.FormatPainterTool_0);
        
        // IFontAttribute
        if(pf.getSourceComponent() instanceof IFontAttribute && targetComponent instanceof IFontAttribute) {
            IFontAttribute source = (IFontAttribute)pf.getSourceComponent();
            IFontAttribute target = (IFontAttribute)targetComponent;
            
            Command cmd = new FontStyleCommand(target, source.getFont());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            
            cmd = new FontColorCommand(target, source.getFontColor());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        // ILineObject
        if(pf.getSourceComponent() instanceof ILineObject && targetComponent instanceof ILineObject) {
            ILineObject source = (ILineObject)pf.getSourceComponent();
            ILineObject target = (ILineObject)targetComponent;
            
            Command cmd = new LineColorCommand(target, source.getLineColor());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            cmd = new LineWidthCommand(target, source.getLineWidth());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }

        // IBorderObject
        if(pf.getSourceComponent() instanceof IBorderObject && targetComponent instanceof IBorderObject) {
            IBorderObject source = (IBorderObject)pf.getSourceComponent();
            IBorderObject target = (IBorderObject)targetComponent;
            
            Command cmd = new BorderColorCommand(target, source.getBorderColor());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        // ITextPosition
        if(pf.getSourceComponent() instanceof ITextPosition && targetComponent instanceof ITextPosition) {
            ITextPosition source = (ITextPosition)pf.getSourceComponent();
            ITextPosition target = (ITextPosition)targetComponent;
            
            Command cmd = new TextPositionCommand(target, source.getTextPosition());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        // ITextAlignment
        if(pf.getSourceComponent() instanceof ITextAlignment && targetComponent instanceof ITextAlignment) {
            ITextAlignment source = (ITextAlignment)pf.getSourceComponent();
            ITextAlignment target = (ITextAlignment)targetComponent;
            
            Command cmd = new TextAlignmentCommand(target, source.getTextAlignment());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        // IDiagramModelObject
        if(pf.getSourceComponent() instanceof IDiagramModelObject && targetComponent instanceof IDiagramModelObject) {
            IDiagramModelObject source = (IDiagramModelObject)pf.getSourceComponent();
            IDiagramModelObject target = (IDiagramModelObject)targetComponent;
            
            // Source fill colour is null which is "default"
            String fillColorString = source.getFillColor();
            if(fillColorString == null) {
                fillColorString = ColorFactory.convertColorToString(ColorFactory.getDefaultFillColor(source));
            }
            
            Command cmd = new FillColorCommand(target, fillColorString);
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            
            // Alpha fill opacity
            cmd = new DiagramModelObjectAlphaCommand(target, source.getAlpha());
            if(cmd.canExecute()) {
                result.add(cmd);
            }

            // Alpha line opacity
            cmd = new DiagramModelObjectOutlineAlphaCommand(target, source.getLineAlpha());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            
            // Gradient
            cmd = new FeatureCommand("", target, IDiagramModelObject.FEATURE_GRADIENT, source.getGradient(), IDiagramModelObject.FEATURE_GRADIENT_DEFAULT); //$NON-NLS-1$
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            
            // Icon Visibility, but paste only if the target object has an icon
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(target);
            if(provider instanceof IGraphicalObjectUIProvider && ((IGraphicalObjectUIProvider)provider).hasIcon()) {
                cmd = new FeatureCommand("", target, IDiagramModelObject.FEATURE_ICON_VISIBLE, source.isIconVisible(), IDiagramModelObject.FEATURE_ICON_VISIBLE_DEFAULT); //$NON-NLS-1$
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
        }
        
        // IDiagramModelConnection
        if(pf.getSourceComponent() instanceof IDiagramModelConnection && targetComponent instanceof IDiagramModelConnection) {
            IDiagramModelConnection source = (IDiagramModelConnection)pf.getSourceComponent();
            IDiagramModelConnection target = (IDiagramModelConnection)targetComponent;
            
            // Connection text position
            Command cmd = new ConnectionTextPositionCommand(target, source.getTextPosition());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        // IIconic
        if(pf.getSourceComponent() instanceof IIconic && targetComponent instanceof IIconic) {
            IIconic source = (IIconic)pf.getSourceComponent();
            IIconic target = (IIconic)targetComponent;
            
            // Image Path
            Command cmd = new EObjectFeatureCommand("", target, IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH, source.getImagePath()); //$NON-NLS-1$
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            
            // Image position
            cmd = new EObjectFeatureCommand("", target, IArchimatePackage.Literals.ICONIC__IMAGE_POSITION, source.getImagePosition()); //$NON-NLS-1$
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        return result;
    }
    
    protected boolean isObjectLocked(Object object) {
        return object instanceof ILockable && ((ILockable)object).isLocked();
    }

    protected boolean isPaintableObject(Object object) {
        // Junctions are a no-no
        if(object instanceof IDiagramModelArchimateObject) {
            IArchimateElement element = ((IDiagramModelArchimateObject)object).getArchimateElement();
            return !(element instanceof IJunction);
        }
        
        // No to Image
        if(object instanceof IDiagramModelImage) {
            return false;
        }
        
        return object instanceof IDiagramModelObject || object instanceof IDiagramModelConnection;
    }
    
    @Override
    protected String getCommandName() {
        return "FormatPaint"; //$NON-NLS-1$
    }

}