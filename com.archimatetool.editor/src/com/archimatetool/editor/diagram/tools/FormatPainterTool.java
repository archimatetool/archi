/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import java.io.IOException;
import java.util.Objects;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.tools.AbstractTool;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.diagram.commands.BorderColorCommand;
import com.archimatetool.editor.diagram.commands.ConnectionLineTypeCommand;
import com.archimatetool.editor.diagram.commands.ConnectionTextPositionCommand;
import com.archimatetool.editor.diagram.commands.DiagramModelObjectAlphaCommand;
import com.archimatetool.editor.diagram.commands.DiagramModelObjectLineStyleCommand;
import com.archimatetool.editor.diagram.commands.DiagramModelObjectOutlineAlphaCommand;
import com.archimatetool.editor.diagram.commands.FillColorCommand;
import com.archimatetool.editor.diagram.commands.FontColorCommand;
import com.archimatetool.editor.diagram.commands.FontStyleCommand;
import com.archimatetool.editor.diagram.commands.LineColorCommand;
import com.archimatetool.editor.diagram.commands.LineWidthCommand;
import com.archimatetool.editor.diagram.commands.TextAlignmentCommand;
import com.archimatetool.editor.diagram.commands.TextPositionCommand;
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
                    if(FormatPainterInfo.INSTANCE.getSourceComponent() == null) {
                        FormatPainterInfo.INSTANCE.updateWithSourceComponent((IDiagramModelComponent)object);
                    }
                    else if(!isObjectLocked(object)) {
                        Command cmd = createCommand((IDiagramModelComponent)object);
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
        }
        
        return false;
    }
    
    CompoundCommand createCommand(IDiagramModelComponent targetComponent) {
        CompoundCommand result = new CompoundCommand(Messages.FormatPainterTool_0);
        
        IDiagramModelComponent sourceComponent = FormatPainterInfo.INSTANCE.getSourceComponent();
        
        IObjectUIProvider sourceUIProvider = ObjectUIFactory.INSTANCE.getProvider(sourceComponent);
        IObjectUIProvider targetUIProvider = ObjectUIFactory.INSTANCE.getProvider(targetComponent);
        
        if(sourceUIProvider == null || targetUIProvider == null) {
            return result;
        }
        
        // IFontAttribute
        if(sourceComponent instanceof IFontAttribute source && targetComponent instanceof IFontAttribute target) {
            if(targetUIProvider.shouldExposeFeature(IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT.getName())) {
                Command cmd = new FontStyleCommand(target, source.getFont());
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
            
            if(targetUIProvider.shouldExposeFeature(IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT_COLOR.getName())) {
                Command cmd = new FontColorCommand(target, source.getFontColor());
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
        }
        
        // ILineObject
        if(sourceComponent instanceof ILineObject source && targetComponent instanceof ILineObject target) {
            // Line color
            if(targetUIProvider.shouldExposeFeature(IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR.getName())) {
                Command cmd = new LineColorCommand(target, source.getLineColor());
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }

            // Line width
            if(targetUIProvider.shouldExposeFeature(IArchimatePackage.Literals.LINE_OBJECT__LINE_WIDTH.getName())) {
                Command cmd = new LineWidthCommand(target, source.getLineWidth());
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
        }

        // IBorderObject
        if(sourceComponent instanceof IBorderObject source && targetComponent instanceof IBorderObject target
                && targetUIProvider.shouldExposeFeature(IArchimatePackage.Literals.BORDER_OBJECT__BORDER_COLOR.getName())) {
            Command cmd = new BorderColorCommand(target, source.getBorderColor());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        // IBorderType
        if(sourceComponent instanceof IBorderType source && targetComponent instanceof IBorderType target
                && source.eClass() == target.eClass() && targetUIProvider.shouldExposeFeature(IArchimatePackage.Literals.BORDER_TYPE__BORDER_TYPE.getName())) {
            Command cmd = new EObjectFeatureCommand("", target, IArchimatePackage.Literals.BORDER_TYPE__BORDER_TYPE, source.getBorderType()); //$NON-NLS-1$
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        // ITextPosition
        if(sourceComponent instanceof ITextPosition source && targetComponent instanceof ITextPosition target
                        && targetUIProvider.shouldExposeFeature(IArchimatePackage.Literals.TEXT_POSITION__TEXT_POSITION.getName())) {
            Command cmd = new TextPositionCommand(target, source.getTextPosition());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        // ITextAlignment
        if(sourceComponent instanceof ITextAlignment source && targetComponent instanceof ITextAlignment target
                        && targetUIProvider.shouldExposeFeature(IArchimatePackage.Literals.TEXT_ALIGNMENT__TEXT_ALIGNMENT.getName())) {
            Command cmd = new TextAlignmentCommand(target, source.getTextAlignment());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        // IDiagramModelObject
        if(sourceComponent instanceof IDiagramModelObject source && targetComponent instanceof IDiagramModelObject target) {
            Command cmd;
            
            // Fill color
            if(targetUIProvider.shouldExposeFeature(IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__FILL_COLOR.getName())) {
                String sourcefillColor = source.getFillColor();
                if(sourcefillColor == null) { // Source fill colour is null which is "default"
                    sourcefillColor = ColorFactory.convertColorToString(ColorFactory.getDefaultFillColor(source));
                }
                
                String targetfillColor = target.getFillColor();
                if(targetfillColor == null) { // target fill colour is null which is "default"
                    targetfillColor = ColorFactory.convertColorToString(ColorFactory.getDefaultFillColor(target));
                }
                
                // Compare actual fill colours rather than null fill colors
                if(!Objects.equals(sourcefillColor, targetfillColor)) {
                    cmd = new FillColorCommand(target, sourcefillColor);
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }
            
            // Alpha fill opacity
            if(targetUIProvider.shouldExposeFeature(IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__ALPHA.getName())) {
                cmd = new DiagramModelObjectAlphaCommand(target, source.getAlpha());
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }

            // Alpha line opacity
            if(targetUIProvider.shouldExposeFeature(IDiagramModelObject.FEATURE_LINE_ALPHA)) {
                cmd = new DiagramModelObjectOutlineAlphaCommand(target, source.getLineAlpha());
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
            
            // Line Style
            if(targetUIProvider.shouldExposeFeature(IDiagramModelObject.FEATURE_LINE_STYLE)) {
                cmd = new DiagramModelObjectLineStyleCommand(target, (int)sourceUIProvider.getFeatureValue(IDiagramModelObject.FEATURE_LINE_STYLE));
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
            
            // Gradient
            if(targetUIProvider.shouldExposeFeature(IDiagramModelObject.FEATURE_GRADIENT)) {
                cmd = new FeatureCommand("", target, IDiagramModelObject.FEATURE_GRADIENT, source.getGradient(), IDiagramModelObject.FEATURE_GRADIENT_DEFAULT); //$NON-NLS-1$
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
            
            // Derive line color
            if(targetUIProvider.shouldExposeFeature(IDiagramModelObject.FEATURE_DERIVE_ELEMENT_LINE_COLOR)) {
                cmd = new FeatureCommand("", target, IDiagramModelObject.FEATURE_DERIVE_ELEMENT_LINE_COLOR, source.getDeriveElementLineColor(), IDiagramModelObject.FEATURE_DERIVE_ELEMENT_LINE_COLOR_DEFAULT); //$NON-NLS-1$
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
            
            // Icon Visibility and Color, but paste only if the target object has an icon
            if(targetUIProvider instanceof IGraphicalObjectUIProvider targetProvider && targetProvider.hasIcon()) {
                // Icon visible
                if(targetUIProvider.shouldExposeFeature(IDiagramModelObject.FEATURE_ICON_VISIBLE)) {
                    cmd = new FeatureCommand("", target, IDiagramModelObject.FEATURE_ICON_VISIBLE, source.getIconVisibleState(), IDiagramModelObject.FEATURE_ICON_VISIBLE_DEFAULT); //$NON-NLS-1$
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }

                // Icon color
                if(targetUIProvider.shouldExposeFeature(IDiagramModelObject.FEATURE_ICON_COLOR)) {
                    cmd = new FeatureCommand("", target, IDiagramModelObject.FEATURE_ICON_COLOR, source.getIconColor(), IDiagramModelObject.FEATURE_ICON_COLOR_DEFAULT); //$NON-NLS-1$
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }
        }
        
        // Archimate objects
        if(sourceComponent instanceof IDiagramModelArchimateObject source && targetComponent instanceof IDiagramModelArchimateObject target) {
            // Image Source
            if(targetUIProvider.shouldExposeFeature(IDiagramModelArchimateObject.FEATURE_IMAGE_SOURCE)) {
                Command cmd = new FeatureCommand("", target, IDiagramModelArchimateObject.FEATURE_IMAGE_SOURCE, //$NON-NLS-1$
                                                 source.getImageSource(), IDiagramModelArchimateObject.FEATURE_IMAGE_SOURCE_DEFAULT);
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
        }
        
        // IDiagramModelConnection
        if(sourceComponent instanceof IDiagramModelConnection source && targetComponent instanceof IDiagramModelConnection target) {
            // Connection text position
            if(targetUIProvider.shouldExposeFeature(IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__TEXT_POSITION.getName())) {
                Command cmd = new ConnectionTextPositionCommand(target, source.getTextPosition());
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
            
            // If a non-Archimate connection, connection line type
            if(!(target instanceof IDiagramModelArchimateConnection)
                     && targetUIProvider.shouldExposeFeature(IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__TYPE.getName())) {
                Command  cmd = new ConnectionLineTypeCommand(target, source.getType());
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
        }
        
        // IIconic
        if(sourceComponent instanceof IIconic source && targetComponent instanceof IIconic target) {
            // Image path
            if(targetUIProvider.shouldExposeFeature(IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH.getName())) {
                // If we have an image path and the source has image bytes
                String imagePath = source.getImagePath();
                if(imagePath != null && FormatPainterInfo.INSTANCE.getSourceImageBytes() != null) {
                    // The target must have these image bytes present for the command to work
                    IArchiveManager targetArchiveManager = (IArchiveManager)target.getAdapter(IArchiveManager.class);
                    if(targetArchiveManager != null) {
                        try {
                            imagePath = targetArchiveManager.addByteContentEntry(imagePath, FormatPainterInfo.INSTANCE.getSourceImageBytes());
                        }
                        catch(IOException ex) {
                            ex.printStackTrace();
                            Logger.error("Could not copy image bytes when copying and pasting objects.", ex); //$NON-NLS-1$
                        }
                    }
                }
                
                Command cmd = new EObjectFeatureCommand("", target, IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH, imagePath); //$NON-NLS-1$
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
                
                // Image position (only supported if image path is supported)
                if(targetUIProvider.shouldExposeFeature(IArchimatePackage.Literals.ICONIC__IMAGE_POSITION.getName())) {
                    cmd = new EObjectFeatureCommand("", target, IArchimatePackage.Literals.ICONIC__IMAGE_POSITION, source.getImagePosition()); //$NON-NLS-1$
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }
        }
        
        return result;
    }
    
    private boolean isObjectLocked(Object object) {
        return object instanceof ILockable lockable && lockable.isLocked();
    }

    boolean isPaintableObject(Object object) {
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