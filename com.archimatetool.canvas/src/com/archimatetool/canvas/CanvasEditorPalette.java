/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.diagram.AbstractPaletteRoot;
import com.archimatetool.editor.diagram.tools.FormatPainterToolEntry;
import com.archimatetool.editor.diagram.tools.PanningSelectionExtendedTool;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * PaletteRoot for Canvas Editor
 * 
 * @author Phillip Beauvoir
 */
public class CanvasEditorPalette extends AbstractPaletteRoot {
    
    private FormatPainterToolEntry formatPainterEntry;
    
    public CanvasEditorPalette() {
        createControlsGroup();
        add(new PaletteSeparator("")); //$NON-NLS-1$
        
        createElementsGroup();
        add(new PaletteSeparator("")); //$NON-NLS-1$

        createStickiesGroup();
        add(new PaletteSeparator("")); //$NON-NLS-1$
    }

    /**
     * Create a Group of Controls
     */
    private PaletteContainer createControlsGroup() {
        PaletteContainer group = new PaletteToolbar(Messages.CanvasEditorPalette_0);
        add(group);
        
        // The selection tool
        ToolEntry tool = new PanningSelectionToolEntry();
        tool.setToolClass(PanningSelectionExtendedTool.class);
        group.add(tool);

        // Use selection tool as default entry
        setDefaultEntry(tool);

        PaletteStack stack = createMarqueeSelectionStack();
        group.add(stack);
        
        // Format Painter
        formatPainterEntry = new FormatPainterToolEntry();
        group.add(formatPainterEntry);

        return group;
    }
    
    private PaletteContainer createElementsGroup() {
        PaletteContainer group = new PaletteGroup(Messages.CanvasEditorPalette_1);
        add(group);
        
        PaletteEntry entry = new CombinedTemplateCreationEntry(
                Messages.CanvasEditorPalette_2,
                null,
                new CanvasModelFactory(ICanvasPackage.eINSTANCE.getCanvasModelBlock()),
                ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_CANVAS_BLOCK),
                ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_CANVAS_BLOCK));
        group.add(entry);
        
        entry = new CombinedTemplateCreationEntry(
                Messages.CanvasEditorPalette_3,
                null,
                new CanvasModelFactory(ICanvasPackage.eINSTANCE.getCanvasModelImage()),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_LANDSCAPE),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_LANDSCAPE));
        group.add(entry);
        
        entry = createConnectionCreationToolEntry(
                ICanvasPackage.eINSTANCE.getCanvasModelConnection(),
                IDiagramModelConnection.LINE_SOLID,
                Messages.CanvasEditorPalette_4,
                null,
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_CONNECTION_PLAIN));
        group.add(entry);
        
        entry = createConnectionCreationToolEntry(
                ICanvasPackage.eINSTANCE.getCanvasModelConnection(),
                IDiagramModelConnection.ARROW_FILL_TARGET,
                Messages.CanvasEditorPalette_5,
                null,
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_CONNECTION_ARROW));
        group.add(entry);
        
        entry = createConnectionCreationToolEntry(
                ICanvasPackage.eINSTANCE.getCanvasModelConnection(),
                IDiagramModelConnection.ARROW_FILL_TARGET | IDiagramModelConnection.LINE_DASHED,
                Messages.CanvasEditorPalette_6,
                null,
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_CONNECTION_DASHED_ARROW));
        group.add(entry);
        
        entry = createConnectionCreationToolEntry(
                ICanvasPackage.eINSTANCE.getCanvasModelConnection(),
                IDiagramModelConnection.ARROW_FILL_TARGET | IDiagramModelConnection.LINE_DOTTED,
                Messages.CanvasEditorPalette_7,
                null,
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_CONNECTION_DOTTED_ARROW));
        group.add(entry);

        
        return group;
    }

    private PaletteContainer createStickiesGroup() {
        PaletteContainer group = new PaletteToolbar(Messages.CanvasEditorPalette_8);
        add(group);
        
        // Sticky Notes
        group.add(createStickyEntry(ColorConstants.white));
        group.add(createStickyEntry(ColorFactory.get(255, 255, 149)));
        group.add(createStickyEntry(ColorFactory.get(213, 255, 149)));
        group.add(createStickyEntry(ColorFactory.get(198, 249, 198)));
        group.add(createStickyEntry(ColorFactory.get(198, 249, 247)));
        group.add(createStickyEntry(ColorFactory.get(198, 216, 250)));
        group.add(createStickyEntry(ColorFactory.get(196, 196, 248)));
        group.add(createStickyEntry(ColorFactory.get(238, 200, 251)));
        group.add(createStickyEntry(ColorFactory.get(247, 196, 196)));
        group.add(createStickyEntry(ColorFactory.get(248, 196, 145)));
        group.add(createStickyEntry(ColorFactory.get(255, 160, 147)));
        
        return group;
    }
    
    private ConnectionCreationToolEntry createConnectionCreationToolEntry(EClass eClass, int type, String name, String description,
                                                                          ImageDescriptor icon) {
        ConnectionCreationToolEntry entry = new ConnectionCreationToolEntry(
                name,
                description,
                new CanvasModelFactory(eClass, type),
                icon,
                icon);
        
        // Ensure Tool gets deselected
        entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        return entry;
    }

    private PaletteEntry createStickyEntry(Color color) {
        ImageDescriptor id = new ImageDescriptor() {
            @Override
            public ImageData getImageData(int zoom) {
                Image image = new Image(Display.getCurrent(), 16, 16);
                
                GC gc = new GC(image);
                gc.setBackground(color);
                gc.fillRectangle(0, 0, 15, 15);
                gc.drawRectangle(0, 0, 15, 15);
                gc.dispose();
                
                ImageData id = image.getImageData(zoom);
                image.dispose();
                
                return id;
           }
        };
        
        return new CombinedTemplateCreationEntry(
                Messages.CanvasEditorPalette_9,
                null,
                new CanvasModelFactory(ICanvasPackage.eINSTANCE.getCanvasModelSticky(), color),
                id,
                id);
    }
    
    void dispose() {
        formatPainterEntry.dispose();
    }
}
