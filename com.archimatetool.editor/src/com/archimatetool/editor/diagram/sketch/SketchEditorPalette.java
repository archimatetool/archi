/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
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

import com.archimatetool.editor.diagram.AbstractPaletteRoot;
import com.archimatetool.editor.diagram.tools.ExtCombinedTemplateCreationEntry;
import com.archimatetool.editor.diagram.tools.ExtConnectionCreationToolEntry;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * PaletteRoot for Sketch
 * 
 * @author Phillip Beauvoir
 */
public class SketchEditorPalette extends AbstractPaletteRoot {
    
    public SketchEditorPalette() {
        add(createToolsGroup());
        
        add(createElementsGroup());
        add(new PaletteSeparator());
        
        add(createStickiesGroup());
    }

    private PaletteContainer createElementsGroup() {
        PaletteContainer group = new PaletteGroup(Messages.SketchEditorPalette_1);
        
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
        
        // Connections
        
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
    
    private PaletteContainer createStickiesGroup() {
        PaletteContainer group = new PaletteGroup(Messages.SketchEditorPalette_6);
        
        // Sticky Notes
        group.add(createStickyEntry(255, 255, 181));
        group.add(createStickyEntry(181, 255, 255));
        group.add(createStickyEntry(201, 231, 183));
        group.add(createStickyEntry(255, 196, 0));
        group.add(createStickyEntry(255, 255, 0));
        group.add(createStickyEntry(96, 255, 96));
        group.add(createStickyEntry(127, 127, 255));
        group.add(createStickyEntry(255, 255, 255));
        
        return group;
    }
    
    private ConnectionCreationToolEntry createConnectionCreationToolEntry(EClass eClass, int type, String name, String description,
                                                                          ImageDescriptor icon) {
        ConnectionCreationToolEntry entry = new ExtConnectionCreationToolEntry(
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
        return new ExtCombinedTemplateCreationEntry(
                name,
                description,
                new SketchModelFactory(eClass),
                ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass),
                ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass));
    }

    private PaletteEntry createStickyEntry(int r, int g, int b) {
        ImageDescriptor id = new ImageDescriptor() {
            @Override
            public ImageData getImageData(int zoom) {
                Image image = new Image(Display.getDefault(), 16, 16);
                
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
                Messages.SketchEditorPalette_12,
                null,
                new SketchModelFactory(IArchimatePackage.eINSTANCE.getSketchModelSticky(), new RGB(r, g, b)),
                id,
                id);
    }
}
