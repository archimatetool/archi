/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch;

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

import com.archimatetool.editor.diagram.AbstractPaletteRoot;
import com.archimatetool.editor.diagram.tools.FormatPainterToolEntry;
import com.archimatetool.editor.diagram.tools.PanningSelectionExtendedTool;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * PaletteRoot for Sketch
 * 
 * @author Phillip Beauvoir
 */
public class SketchEditorPalette extends AbstractPaletteRoot {
    
    private FormatPainterToolEntry formatPainterEntry;
    
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
        group.add(createStickyEntry(ColorFactory.get(255, 255, 181)));
        group.add(createStickyEntry(ColorFactory.get(181, 255, 255)));
        group.add(createStickyEntry(ColorFactory.get(201, 231, 183)));
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
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_CONNECTION_PLAIN));
        group.add(entry);
        
        entry = createConnectionCreationToolEntry(
                IArchimatePackage.eINSTANCE.getDiagramModelConnection(),
                IDiagramModelConnection.ARROW_FILL_TARGET,
                Messages.SketchEditorPalette_9,
                null,
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_CONNECTION_ARROW));
        group.add(entry);
        
        entry = createConnectionCreationToolEntry(
                IArchimatePackage.eINSTANCE.getDiagramModelConnection(),
                IDiagramModelConnection.ARROW_FILL_TARGET | IDiagramModelConnection.LINE_DASHED,
                Messages.SketchEditorPalette_10,
                null,
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_CONNECTION_DASHED_ARROW));
        group.add(entry);
        
        entry = createConnectionCreationToolEntry(
                IArchimatePackage.eINSTANCE.getDiagramModelConnection(),
                IDiagramModelConnection.ARROW_FILL_TARGET | IDiagramModelConnection.LINE_DOTTED,
                Messages.SketchEditorPalette_11,
                null,
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_CONNECTION_DOTTED_ARROW));
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
                ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass),
                ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass));
    }

    private PaletteEntry createStickyEntry(Color color) {
        ImageDescriptor id = new ImageDescriptor() {
            @Override
            public ImageData getImageData(int zoom) {
                Image image = new Image(Display.getDefault(), 16, 14);
                
                GC gc = new GC(image);
                gc.setBackground(color);
                gc.fillRectangle(0, 0, 15, 13);
                gc.drawRectangle(0, 0, 15, 13);
                gc.dispose();
                
                ImageData id = image.getImageData(zoom);
                image.dispose();
                
                return id;
           }
        };

        return new CombinedTemplateCreationEntry(
                Messages.SketchEditorPalette_12,
                null,
                new SketchModelFactory(IArchimatePackage.eINSTANCE.getSketchModelSticky(), color),
                id,
                id);
    }
    
    void dispose() {
        formatPainterEntry.dispose();
    }
}
