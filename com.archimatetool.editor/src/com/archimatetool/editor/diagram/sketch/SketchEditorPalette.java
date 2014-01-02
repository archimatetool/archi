/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch;

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

import com.archimatetool.editor.diagram.AbstractPaletteRoot;
import com.archimatetool.editor.diagram.tools.FormatPainterToolEntry;
import com.archimatetool.editor.diagram.tools.PanningSelectionExtendedTool;
import com.archimatetool.editor.ui.ArchimateLabelProvider;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * PaletteRoot for Sketch
 * 
 * @author Phillip Beauvoir
 */
public class SketchEditorPalette extends AbstractPaletteRoot {
    
    private FormatPainterToolEntry formatPainterEntry;
    
    private Hashtable<Color, StickyImageDescriptor> fImageTable = new Hashtable<Color, StickyImageDescriptor>();
    
    public SketchEditorPalette() {
        createControlsGroup();
        add(new PaletteSeparator("")); //$NON-NLS-1$
        
        createElementsGroup();
        add(new PaletteSeparator("")); //$NON-NLS-1$
        
        createStickiesGroup();
        add(new PaletteSeparator("")); //$NON-NLS-1$
        
        createConnectionsGroup();
        add(new PaletteSeparator("")); //$NON-NLS-1$
    }

    /**
     * Create a Group of Controls
     */
    private PaletteContainer createControlsGroup() {
        PaletteContainer group = new PaletteToolbar(Messages.SketchEditorPalette_0);
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
        PaletteContainer group = new PaletteGroup(Messages.SketchEditorPalette_1);
        add(group);
        
        // Actor
        PaletteEntry groupEntry = createCombinedTemplateCreationEntry(IArchimatePackage.eINSTANCE.getSketchModelActor(),
                Messages.SketchEditorPalette_2,
                Messages.SketchEditorPalette_3);
        group.add(groupEntry);
        
        // Group
        groupEntry = createCombinedTemplateCreationEntry(IArchimatePackage.eINSTANCE.getDiagramModelGroup(),
                Messages.SketchEditorPalette_4,
                Messages.SketchEditorPalette_5);
        group.add(groupEntry);
    
        return group;
    }
    
    private PaletteContainer createStickiesGroup() {
        PaletteContainer group = new PaletteToolbar(Messages.SketchEditorPalette_6);
        add(group);
        
        // Sticky Notes
        group.add(createStickyEntry(ColorFactory.COLOR_BUSINESS));
        group.add(createStickyEntry(ColorFactory.COLOR_APPLICATION));
        group.add(createStickyEntry(ColorFactory.COLOR_TECHNOLOGY));
        group.add(createStickyEntry(ColorConstants.orange));
        group.add(createStickyEntry(ColorConstants.yellow));
        group.add(createStickyEntry(ColorConstants.lightGreen));
        group.add(createStickyEntry(ColorConstants.lightBlue));
        group.add(createStickyEntry(ColorConstants.white));
        
        return group;
    }
    
    private PaletteContainer createConnectionsGroup() {
        PaletteContainer group = new PaletteGroup(Messages.SketchEditorPalette_7);
        add(group);
        
        ConnectionCreationToolEntry entry = createConnectionCreationToolEntry(
                IArchimatePackage.eINSTANCE.getDiagramModelConnection(),
                IDiagramModelConnection.LINE_SOLID,
                Messages.SketchEditorPalette_8,
                null,
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_CONNECTION_PLAIN_16));
        group.add(entry);
        
        entry = createConnectionCreationToolEntry(
                IArchimatePackage.eINSTANCE.getDiagramModelConnection(),
                IDiagramModelConnection.ARROW_FILL_TARGET,
                Messages.SketchEditorPalette_9,
                null,
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_CONNECTION_ARROW_16));
        group.add(entry);
        
        entry = createConnectionCreationToolEntry(
                IArchimatePackage.eINSTANCE.getDiagramModelConnection(),
                IDiagramModelConnection.ARROW_FILL_TARGET | IDiagramModelConnection.LINE_DASHED,
                Messages.SketchEditorPalette_10,
                null,
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_CONNECTION_DASHED_ARROW_16));
        group.add(entry);
        
        entry = createConnectionCreationToolEntry(
                IArchimatePackage.eINSTANCE.getDiagramModelConnection(),
                IDiagramModelConnection.ARROW_FILL_TARGET | IDiagramModelConnection.LINE_DOTTED,
                Messages.SketchEditorPalette_11,
                null,
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_CONNECTION_DOTTED_ARROW_16));
        group.add(entry);

        return group;
    }
    
    private ConnectionCreationToolEntry createConnectionCreationToolEntry(EClass eClass, int type, String name, String description,
                                                                          ImageDescriptor icon) {
        ConnectionCreationToolEntry entry = new ConnectionCreationToolEntry(
                name,
                description,
                new SketchModelFactory(eClass, type),
                icon,
                icon);
        
        // Ensure Tool gets deselected
        entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        return entry;
    }

    private CombinedTemplateCreationEntry createCombinedTemplateCreationEntry(EClass eClass, String name, String description) {
        return new CombinedTemplateCreationEntry(
                name,
                description,
                new SketchModelFactory(eClass),
                ArchimateLabelProvider.INSTANCE.getImageDescriptor(eClass),
                ArchimateLabelProvider.INSTANCE.getImageDescriptor(eClass));
    }

    private PaletteEntry createStickyEntry(Color color) {
        return new CombinedTemplateCreationEntry(
                Messages.SketchEditorPalette_12,
                null,
                new SketchModelFactory(IArchimatePackage.eINSTANCE.getSketchModelSticky(), color),
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
            image = new Image(Display.getDefault(), 16, 14);
            GC gc = new GC(image);
            SWTGraphics graphics = new SWTGraphics(gc);
            graphics.setBackgroundColor(color);
            graphics.fillRectangle(0, 0, 15, 13);
            graphics.drawRectangle(0, 0, 15, 13);
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
