/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.diagram.AbstractPaletteRoot;
import com.archimatetool.editor.diagram.tools.ExtCombinedTemplateCreationEntry;
import com.archimatetool.editor.diagram.tools.ExtConnectionCreationToolEntry;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * PaletteRoot for Canvas Editor
 * 
 * @author Phillip Beauvoir
 */
public class CanvasEditorPalette extends AbstractPaletteRoot {
    
    public CanvasEditorPalette() {
        add(createToolsGroup());
        
        createElementsGroup();
        add(new PaletteSeparator());

        createStickiesGroup();
    }

    private PaletteContainer createElementsGroup() {
        PaletteContainer group = new PaletteGroup(Messages.CanvasEditorPalette_1);
        add(group);
        
        PaletteEntry entry = new ExtCombinedTemplateCreationEntry(
                Messages.CanvasEditorPalette_2,
                null,
                new CanvasModelFactory(ICanvasPackage.eINSTANCE.getCanvasModelBlock()),
                ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_CANVAS_BLOCK),
                ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_CANVAS_BLOCK));
        group.add(entry);
        
        entry = new ExtCombinedTemplateCreationEntry(
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
        PaletteContainer group = new PaletteGroup(Messages.CanvasEditorPalette_8);
        add(group);
        
        // Sticky Notes
        group.add(createStickyEntry(255, 255, 255));
        group.add(createStickyEntry(255, 255, 149));
        group.add(createStickyEntry(213, 255, 149));
        group.add(createStickyEntry(198, 249, 198));
        group.add(createStickyEntry(198, 249, 247));
        group.add(createStickyEntry(198, 216, 250));
        group.add(createStickyEntry(196, 196, 248));
        group.add(createStickyEntry(238, 200, 251));
        group.add(createStickyEntry(247, 196, 196));
        group.add(createStickyEntry(248, 196, 145));
        group.add(createStickyEntry(255, 160, 147));
        
        return group;
    }
    
    private ConnectionCreationToolEntry createConnectionCreationToolEntry(EClass eClass, int type, String name, String description,
                                                                          ImageDescriptor icon) {
        ConnectionCreationToolEntry entry = new ExtConnectionCreationToolEntry(
                name,
                description,
                new CanvasModelFactory(eClass, type),
                icon,
                icon);
        
        // Ensure Tool gets deselected
        entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        return entry;
    }

    private PaletteEntry createStickyEntry(int r, int g, int b) {
        ImageDescriptor id = new ImageDescriptor() {
            @Override
            public ImageData getImageData(int zoom) {
                Image image = new Image(Display.getCurrent(), 16, 16);
                
                GC gc = new GC(image);
                gc.setBackground(new Color(r, g, b));
                gc.fillRectangle(0, 0, 15, 15);
                gc.drawRectangle(0, 0, 15, 15);
                gc.dispose();
                
                ImageData id = image.getImageData(zoom);
                image.dispose();
                
                return id;
           }
        };
        
        return new ExtCombinedTemplateCreationEntry(
                Messages.CanvasEditorPalette_9,
                null,
                new CanvasModelFactory(ICanvasPackage.eINSTANCE.getCanvasModelSticky(), new RGB(r, g, b)),
                id,
                id);
    }
}
