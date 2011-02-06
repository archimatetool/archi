/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.MarqueeSelectionTool;

import uk.ac.bolton.archimate.editor.diagram.tools.FormatPainterToolEntry;
import uk.ac.bolton.archimate.editor.diagram.tools.MagicConnectionCreationTool;
import uk.ac.bolton.archimate.editor.diagram.tools.MagicConnectionModelFactory;
import uk.ac.bolton.archimate.editor.diagram.tools.PanningSelectionExtendedTool;
import uk.ac.bolton.archimate.editor.ui.ArchimateNames;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.ImageFactory;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.util.ArchimateModelUtils;


/**
 * PaletteRoot for Diagram
 * 
 * @author Phillip Beauvoir
 */
public class DiagramEditorPalette extends PaletteRoot {
    
    private FormatPainterToolEntry formatPainterEntry;
    
    public DiagramEditorPalette() {
        createControlsGroup();
        createRelationsGroup();
        createExtrasGroup();
        createBusinessLayerGroup();
        createApplicationLayerGroup();
        createTechnologyLayerGroup();
    }

    /**
     * Create a Group of Controls
     */
    private PaletteContainer createControlsGroup() {
        PaletteToolbar toolBar = new PaletteToolbar("Controls");
        add(toolBar);
        
        // The selection tool
        ToolEntry tool = new PanningSelectionToolEntry();
        tool.setToolClass(PanningSelectionExtendedTool.class);
        toolBar.add(tool);

        // Use selection tool as default entry
        setDefaultEntry(tool);

        // Marquee selection tool to select nodes and connections
        MarqueeToolEntry marquee = new MarqueeToolEntry();
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                new Integer(MarqueeSelectionTool.BEHAVIOR_NODES_AND_CONNECTIONS));
        toolBar.add(marquee);
        
        // Marquee selection tool to select connections only
        marquee = new MarqueeToolEntry();
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                new Integer(MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED));
        toolBar.add(marquee);
        
        // Format Painter
        formatPainterEntry = new FormatPainterToolEntry();
        toolBar.add(formatPainterEntry);
        
        return toolBar;
    }

    /**
     * Create a Group of Controls
     */
    private PaletteContainer createExtrasGroup() {
        PaletteDrawer drawer = new PaletteDrawer("View");
        add(drawer);
        
        // Note
        PaletteEntry noteEntry = new CombinedTemplateCreationEntry(
                "Note",
                null,
                new DiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelNote()),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_NOTE_16),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_NOTE_16));
        drawer.add(noteEntry);
        
        // Group
        PaletteEntry groupEntry = new CombinedTemplateCreationEntry(
                "Group",
                null,
                new DiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelGroup()),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_GROUP_16),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_GROUP_16));
        drawer.add(groupEntry);
        
        return drawer;
    }

    /**
     * Business Palette
     * @return
     */
    private PaletteContainer createBusinessLayerGroup() {
        PaletteDrawer drawer = new PaletteDrawer("Business");
        add(drawer);
        
        for(EClass eClass : ArchimateModelUtils.getBusinessClasses()) {
            PaletteEntry entry = createCombinedTemplateCreationEntry(eClass);
            drawer.add(entry);
        }
        
        return drawer;
    }

    /**
     * Application Palette
     * @return
     */
    private PaletteContainer createApplicationLayerGroup() {
        PaletteDrawer drawer = new PaletteDrawer("Application");
        add(drawer);
        
        for(EClass eClass : ArchimateModelUtils.getApplicationClasses()) {
            PaletteEntry entry = createCombinedTemplateCreationEntry(eClass);
            drawer.add(entry);
        }
        
        return drawer;
    }

    /**
     * Technology Palette
     * @return
     */
    private PaletteContainer createTechnologyLayerGroup() {
        PaletteDrawer drawer = new PaletteDrawer("Technology");
        add(drawer);
        
        for(EClass eClass : ArchimateModelUtils.getTechnologyClasses()) {
            PaletteEntry entry = createCombinedTemplateCreationEntry(eClass);
            drawer.add(entry);
        }
        
        return drawer;
    }

    /**
     * Relations Palette
     * @return
     */
    private PaletteContainer createRelationsGroup() {
        PaletteDrawer drawer = new PaletteDrawer("Relations");
        add(drawer);
        
        PaletteToolbar toolBar = new PaletteToolbar("Relations");
        drawer.add(toolBar);
        
        ConnectionCreationToolEntry magicConnectionEntry = new ConnectionCreationToolEntry(
                "Magic Connector",
                null,
                new MagicConnectionModelFactory(),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_MAGIC_CONNECTION_16),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_MAGIC_CONNECTION_16));
        
        magicConnectionEntry.setToolClass(MagicConnectionCreationTool.class);
        magicConnectionEntry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        toolBar.add(magicConnectionEntry);
        
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            ConnectionCreationToolEntry entry = createConnectionCreationToolEntry(eClass);
            toolBar.add(entry);
        }
        
        // Junctions
        PaletteStack stack = new PaletteStack("Junctions", "Junctions", null);
        toolBar.add(stack);
        
        for(EClass eClass : ArchimateModelUtils.getConnectorClasses()) {
            PaletteEntry entry = createCombinedTemplateCreationEntry(eClass);
            stack.add(entry);
        }
        
        return drawer;
    }
    
    public void dispose() {
        formatPainterEntry.dispose();
    }
    
    // --------------------------------------------------------------------------------------------
    // Convenience methods
    // --------------------------------------------------------------------------------------------
    
    private CombinedTemplateCreationEntry createCombinedTemplateCreationEntry(EClass eClass) {
        return new CombinedTemplateCreationEntry(
                ArchimateNames.getDefaultShortName(eClass),
                null,
                new DiagramModelFactory(eClass),
                ImageFactory.getImageDescriptor(eClass),
                ImageFactory.getImageDescriptor(eClass));
    }
    
    private ConnectionCreationToolEntry createConnectionCreationToolEntry(EClass eClass) {
        ConnectionCreationToolEntry entry = new ConnectionCreationToolEntry(
                ArchimateNames.getDefaultName(eClass),
                null,
                new DiagramModelFactory(eClass),
                ImageFactory.getImageDescriptor(eClass),
                ImageFactory.getImageDescriptor(eClass));
        
        // Ensure Tool gets deselected
        entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        
        return entry;
    }
}
