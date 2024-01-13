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
import com.archimatetool.editor.diagram.commands.ConnectionLineTypeCommand;
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
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.model.commands.FeatureCommand;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IBorderObject;
import com.archimatetool.model.IBorderType;
import com.archimatetool.model.IDiagramModelArchimateConnection;
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
        
        IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(targetComponent);
        
        // IFontAttribute
        if(pf.getSourceComponent() instanceof IFontAttribute source && targetComponent instanceof IFontAttribute target) {
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
        if(pf.getSourceComponent() instanceof ILineObject source && targetComponent instanceof ILineObject target && provider != null) {
            // Line color
            if(provider.shouldExposeFeature(IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR.getName())) {
                Command cmd = new LineColorCommand(target, source.getLineColor());
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }

            // Line width
            if(provider.shouldExposeFeature(IArchimatePackage.Literals.LINE_OBJECT__LINE_WIDTH.getName())) {
                Command cmd = new LineWidthCommand(target, source.getLineWidth());
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
        }

        // IBorderObject
        if(pf.getSourceComponent() instanceof IBorderObject source && targetComponent instanceof IBorderObject target) {
            Command cmd = new BorderColorCommand(target, source.getBorderColor());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        // IBorderType
        if(pf.getSourceComponent() instanceof IBorderType source && targetComponent instanceof IBorderType target && source.eClass() == target.eClass()) {
            Command cmd = new EObjectFeatureCommand("", target, IArchimatePackage.Literals.BORDER_TYPE__BORDER_TYPE, source.getBorderType()); //$NON-NLS-1$
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        // ITextPosition
        if(pf.getSourceComponent() instanceof ITextPosition source && targetComponent instanceof ITextPosition target) {
            Command cmd = new TextPositionCommand(target, source.getTextPosition());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        // ITextAlignment
        if(pf.getSourceComponent() instanceof ITextAlignment source && targetComponent instanceof ITextAlignment target) {
            Command cmd = new TextAlignmentCommand(target, source.getTextAlignment());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        // IDiagramModelObject
        if(pf.getSourceComponent() instanceof IDiagramModelObject source && targetComponent instanceof IDiagramModelObject target) {
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
            if(provider != null && provider.shouldExposeFeature(IDiagramModelObject.FEATURE_GRADIENT)) {
                cmd = new FeatureCommand("", target, IDiagramModelObject.FEATURE_GRADIENT, source.getGradient(), IDiagramModelObject.FEATURE_GRADIENT_DEFAULT); //$NON-NLS-1$
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
            
            // Derive line color
            if(provider != null && provider.shouldExposeFeature(IDiagramModelObject.FEATURE_DERIVE_ELEMENT_LINE_COLOR)) {
                cmd = new FeatureCommand("", target, IDiagramModelObject.FEATURE_DERIVE_ELEMENT_LINE_COLOR, source.getDeriveElementLineColor(), IDiagramModelObject.FEATURE_DERIVE_ELEMENT_LINE_COLOR_DEFAULT); //$NON-NLS-1$
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
            
            // Icon Visibility and Color, but paste only if the target object has an icon
            if(provider instanceof IGraphicalObjectUIProvider && ((IGraphicalObjectUIProvider)provider).hasIcon()) {
                // Icon visible
                cmd = new FeatureCommand("", target, IDiagramModelObject.FEATURE_ICON_VISIBLE, source.getIconVisibleState(), IDiagramModelObject.FEATURE_ICON_VISIBLE_DEFAULT); //$NON-NLS-1$
                if(cmd.canExecute()) {
                    result.add(cmd);
                }

                // Icon color
                cmd = new FeatureCommand("", target, IDiagramModelObject.FEATURE_ICON_COLOR, source.getIconColor(), IDiagramModelObject.FEATURE_ICON_COLOR_DEFAULT); //$NON-NLS-1$
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
        }
        
        // Archimate objects
        if(pf.getSourceComponent() instanceof IDiagramModelArchimateObject source && targetComponent instanceof IDiagramModelArchimateObject target) {
            // Image Source
            Command cmd = new FeatureCommand("", target, IDiagramModelArchimateObject.FEATURE_IMAGE_SOURCE, //$NON-NLS-1$
                    source.getImageSource(), IDiagramModelArchimateObject.FEATURE_IMAGE_SOURCE_DEFAULT);
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        // IDiagramModelConnection
        if(pf.getSourceComponent() instanceof IDiagramModelConnection source && targetComponent instanceof IDiagramModelConnection target) {
            // Connection text position
            Command cmd = new ConnectionTextPositionCommand(target, source.getTextPosition());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            
            // If a non-Archimate connection, connection line type
            if(!(target instanceof IDiagramModelArchimateConnection)) {
                cmd = new ConnectionLineTypeCommand(target, source.getType());
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
        }
        
        // IIconic
        if(pf.getSourceComponent() instanceof IIconic source && targetComponent instanceof IIconic target) {
            // If we have an image path and the source and target models are different, copy the image bytes
            String imagePath = source.getImagePath();
            if(imagePath != null && source.getArchimateModel() != target.getArchimateModel()) {
                IArchiveManager targetArchiveManager = (IArchiveManager)target.getAdapter(IArchiveManager.class);
                imagePath = targetArchiveManager.copyImageBytes(source.getArchimateModel(), imagePath);
            }
            
            Command cmd = new EObjectFeatureCommand("", target, IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH, imagePath); //$NON-NLS-1$
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
        return object instanceof ILockable lockable && lockable.isLocked();
    }

    protected boolean isPaintableObject(Object object) {
        // Junctions are a no-no
        if(object instanceof IDiagramModelArchimateObject dmo) {
            IArchimateElement element = dmo.getArchimateElement();
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