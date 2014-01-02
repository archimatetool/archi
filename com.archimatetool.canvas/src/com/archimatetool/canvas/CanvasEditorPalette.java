/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import java.util.Hashtable;
import java.util.Map.Entry;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.SWTGraphics;
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
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * PaletteRoot for Canvas Editor
 * 
 * @author Phillip Beauvoir
 */
public class CanvasEditorPalette extends AbstractPaletteRoot {
    
    private FormatPainterToolEntry formatPainterEntry;
    
    private Hashtable<Color, StickyImageDescriptor> fImageTable = new Hashtable<Color, StickyImageDescriptor>();
    
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
                ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_CANVAS_BLOCK_16),
                ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_CANVAS_BLOCK_16));
        group.add(entry);
        
        entry = new CombinedTemplateCreationEntry(
                Messages.CanvasEditorPalette_3,
                null,
                new CanvasModelFactory(ICanvasPackage.eINSTANCE.getCanvasModelImage()),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_LANDSCAPE_16),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_LANDSCAPE_16));
        group.add(entry);
        
        entry = createConnectionCreationToolEntry(
                ICanvasPackage.eINSTANCE.getCanvasModelConnection(),
                IDiagramModelConnection.LINE_SOLID,
                Messages.CanvasEditorPalette_4,
                null,
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_CONNECTION_PLAIN_16));
        group.add(entry);
        
        entry = createConnectionCreationToolEntry(
                ICanvasPackage.eINSTANCE.getCanvasModelConnection(),
                IDiagramModelConnection.ARROW_FILL_TARGET,
                Messages.CanvasEditorPalette_5,
                null,
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_CONNECTION_ARROW_16));
        group.add(entry);
        
        entry = createConnectionCreationToolEntry(
                ICanvasPackage.eINSTANCE.getCanvasModelConnection(),
                IDiagramModelConnection.ARROW_FILL_TARGET | IDiagramModelConnection.LINE_DASHED,
                Messages.CanvasEditorPalette_6,
                null,
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_CONNECTION_DASHED_ARROW_16));
        group.add(entry);
        
        entry = createConnectionCreationToolEntry(
                ICanvasPackage.eINSTANCE.getCanvasModelConnection(),
                IDiagramModelConnection.ARROW_FILL_TARGET | IDiagramModelConnection.LINE_DOTTED,
                Messages.CanvasEditorPalette_7,
                null,
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_CONNECTION_DOTTED_ARROW_16));
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
        return new CombinedTemplateCreationEntry(
                Messages.CanvasEditorPalette_9,
                null,
                new CanvasModelFactory(ICanvasPackage.eINSTANCE.getCanvasModelSticky(), color),
                getStickyImageDescriptor(color),
                getStickyImageDescriptor(color));
    }
    
    private ImageDescriptor getStickyImageDescriptor(Color color) {
        StickyImageDescriptor id = fImageTable.get(color);
        if(id == null) {
            id = new StickyImageDescriptor(color);
            fImageTable.put(color, id);
        }
        return id;
    }
    
    public void dispose() {
        for(Entry<Color, StickyImageDescriptor> entry : fImageTable.entrySet()) {
            entry.getValue().image.dispose();
        }
        
        formatPainterEntry.dispose();
    }
    
    class StickyImageDescriptor extends ImageDescriptor {
        Image image;
        
        StickyImageDescriptor(Color color) {
            image = new Image(Display.getCurrent(), 16, 16);
            GC gc = new GC(image);
            SWTGraphics graphics = new SWTGraphics(gc);
            graphics.setBackgroundColor(color);
            graphics.fillRectangle(0, 0, 15, 15);
            graphics.drawRectangle(0, 0, 15, 15);
            gc.dispose();
            graphics.dispose();
        }
        
        @Override
        public ImageData getImageData() {
            return image.getImageData();
        }
        
        public void dispose() {
            if(image != null && !image.isDisposed()) {
                image.dispose();
            }
        }
    }
}
