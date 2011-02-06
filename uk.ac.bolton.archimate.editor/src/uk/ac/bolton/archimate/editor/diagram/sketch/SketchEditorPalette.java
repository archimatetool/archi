/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.sketch;

import java.util.Hashtable;
import java.util.Map.Entry;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.MarqueeSelectionTool;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import uk.ac.bolton.archimate.editor.diagram.sketch.editparts.SketchEditPartFactory;
import uk.ac.bolton.archimate.editor.diagram.tools.FormatPainterToolEntry;
import uk.ac.bolton.archimate.editor.diagram.tools.PanningSelectionExtendedTool;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.ui.ImageFactory;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * PaletteRoot for Sketch
 * 
 * @author Phillip Beauvoir
 */
public class SketchEditorPalette extends PaletteRoot {
    
    private FormatPainterToolEntry formatPainterEntry;
    
    private Hashtable<Color, StickyImageDescriptor> fImageTable = new Hashtable<Color, StickyImageDescriptor>();
    
    public SketchEditorPalette() {
        createControlsGroup();
        createElementsGroup();
        createStickiesGroup();
        createConnectionsGroup();
    }

    /**
     * Create a Group of Controls
     */
    private PaletteContainer createControlsGroup() {
        PaletteToolbar toolbar = new PaletteToolbar("Controls");
        add(toolbar);
        
        // The selection tool
        ToolEntry tool = new PanningSelectionToolEntry();
        tool.setToolClass(PanningSelectionExtendedTool.class);
        toolbar.add(tool);

        // Use selection tool as default entry
        setDefaultEntry(tool);

        // Marquee selection tool to select nodes and connections
        MarqueeToolEntry marquee = new MarqueeToolEntry();
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                new Integer(MarqueeSelectionTool.BEHAVIOR_NODES_AND_CONNECTIONS));
        toolbar.add(marquee);
        
        // Marquee selection tool to select connections only
        marquee = new MarqueeToolEntry();
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                new Integer(MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED));
        toolbar.add(marquee);
        
        // Format Painter
        formatPainterEntry = new FormatPainterToolEntry();
        toolbar.add(formatPainterEntry);

        return toolbar;
    }

    private PaletteContainer createElementsGroup() {
        PaletteDrawer drawer = new PaletteDrawer("Elements");
        add(drawer);
        
        PaletteToolbar toolBar = new PaletteToolbar("Elements");
        drawer.add(toolBar);
        
        toolBar.add(createCombinedTemplateCreationEntry(IArchimatePackage.eINSTANCE.getSketchModelActor(), "Actor"));
    
        return drawer;
    }
    
    private PaletteContainer createStickiesGroup() {
        PaletteDrawer drawer = new PaletteDrawer("Stickies");
        add(drawer);
        
        PaletteToolbar toolBar = new PaletteToolbar("Stickies");
        drawer.add(toolBar);
        
        // Sticky Notes
        toolBar.add(createStickyEntry(ColorFactory.COLOR_BUSINESS));
        toolBar.add(createStickyEntry(ColorFactory.COLOR_APPLICATION));
        toolBar.add(createStickyEntry(ColorFactory.COLOR_TECHNOLOGY));
        toolBar.add(createStickyEntry(ColorConstants.orange));
        toolBar.add(createStickyEntry(ColorConstants.yellow));
        toolBar.add(createStickyEntry(ColorConstants.lightGreen));
        toolBar.add(createStickyEntry(ColorConstants.lightBlue));
        toolBar.add(createStickyEntry(ColorConstants.white));
        
        return drawer;
    }
    
    private PaletteContainer createConnectionsGroup() {
        PaletteDrawer drawer = new PaletteDrawer("Connections");
        add(drawer);
        
        PaletteToolbar toolBar = new PaletteToolbar("Connections");
        drawer.add(toolBar);

        ConnectionCreationToolEntry entry = createConnectionCreationToolEntry(
                IArchimatePackage.eINSTANCE.getDiagramModelConnection(),
                null,
                "Line Connection");
        toolBar.add(entry);
        
        entry = createConnectionCreationToolEntry(
                IArchimatePackage.eINSTANCE.getDiagramModelConnection(),
                SketchEditPartFactory.CONNECTION_ARROW,
                "Arrow Connection");
        toolBar.add(entry);
        
        entry = createConnectionCreationToolEntry(
                IArchimatePackage.eINSTANCE.getDiagramModelConnection(),
                SketchEditPartFactory.CONNECTION_DASHED_ARROW,
                "Dashed Connection");
        toolBar.add(entry);
        
        return drawer;
    }
    
    private ConnectionCreationToolEntry createConnectionCreationToolEntry(EClass eClass, String type, String name) {
        ConnectionCreationToolEntry entry = new ConnectionCreationToolEntry(
                name,
                null,
                new SketchModelFactory(eClass, type),
                ImageFactory.getImageDescriptor(eClass, type),
                ImageFactory.getImageDescriptor(eClass, type));
        
        // Ensure Tool gets deselected
        entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        return entry;
    }

    private CombinedTemplateCreationEntry createCombinedTemplateCreationEntry(EClass eClass, String name) {
        return new CombinedTemplateCreationEntry(
                name,
                null,
                new SketchModelFactory(eClass),
                ImageFactory.getImageDescriptor(eClass),
                ImageFactory.getImageDescriptor(eClass));
    }

    private PaletteEntry createStickyEntry(Color color) {
        return new CombinedTemplateCreationEntry(
                "Sticky",
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
