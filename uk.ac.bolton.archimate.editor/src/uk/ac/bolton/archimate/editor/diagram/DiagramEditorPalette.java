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
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
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
        add(new PaletteSeparator(""));
        
        createRelationsGroup();
        add(new PaletteSeparator(""));

        createExtrasGroup();
        add(new PaletteSeparator(""));
        
        createBusinessLayerGroup();
        add(new PaletteSeparator(""));

        createApplicationLayerGroup();
        add(new PaletteSeparator(""));
        
        createTechnologyLayerGroup();
    }

    /**
     * Create a Group of Controls
     */
    private PaletteContainer createControlsGroup() {
        PaletteContainer group = new PaletteToolbar("Tools");
        add(group);
        
        // The selection tool
        ToolEntry tool = new PanningSelectionToolEntry();
        tool.setToolClass(PanningSelectionExtendedTool.class);
        group.add(tool);

        // Use selection tool as default entry
        setDefaultEntry(tool);

        // Marquee selection tool to select nodes and connections
        MarqueeToolEntry marquee = new MarqueeToolEntry();
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                new Integer(MarqueeSelectionTool.BEHAVIOR_NODES_AND_CONNECTIONS));
        group.add(marquee);
        
        // Marquee selection tool to select connections only
        marquee = new MarqueeToolEntry();
        marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
                new Integer(MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED));
        group.add(marquee);
        
        // Format Painter
        formatPainterEntry = new FormatPainterToolEntry();
        group.add(formatPainterEntry);
        
        return group;
    }

    /**
     * Create a Group of Controls
     */
    private PaletteContainer createExtrasGroup() {
        PaletteContainer group = new PaletteGroup("View");
        add(group);
        
        // Note
        PaletteEntry noteEntry = new CombinedTemplateCreationEntry(
                "Note",
                "A Note element",
                new DiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelNote()),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_NOTE_16),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_NOTE_16));
        group.add(noteEntry);
        
        // Group
        PaletteEntry groupEntry = new CombinedTemplateCreationEntry(
                "Group",
                "Grouping Element",
                new DiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelGroup()),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_GROUP_16),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_GROUP_16));
        group.add(groupEntry);
        
        // Note Connection
        ConnectionCreationToolEntry entry = createConnectionCreationToolEntry(IArchimatePackage.eINSTANCE.getDiagramModelConnection(),
                "Note Connection");
        group.add(entry);
        
        return group;
    }

    /**
     * Business Palette
     * @return
     */
    private PaletteContainer createBusinessLayerGroup() {
        PaletteContainer group = new PaletteGroup("Business");
        add(group);
        
        for(EClass eClass : ArchimateModelUtils.getBusinessClasses()) {
            PaletteEntry entry = createCombinedTemplateCreationEntry(eClass, null);
            group.add(entry);
        }
        
        return group;
    }

    /**
     * Application Palette
     * @return
     */
    private PaletteContainer createApplicationLayerGroup() {
        PaletteContainer group = new PaletteGroup("Application");
        add(group);
        
        for(EClass eClass : ArchimateModelUtils.getApplicationClasses()) {
            PaletteEntry entry = createCombinedTemplateCreationEntry(eClass, null);
            group.add(entry);
        }
        
        return group;
    }

    /**
     * Technology Palette
     * @return
     */
    private PaletteContainer createTechnologyLayerGroup() {
        PaletteContainer group = new PaletteGroup("Technology");
        add(group);
        
        for(EClass eClass : ArchimateModelUtils.getTechnologyClasses()) {
            PaletteEntry entry = createCombinedTemplateCreationEntry(eClass, null);
            group.add(entry);
        }
        
        return group;
    }

    /**
     * Relations Palette
     * @return
     */
    private PaletteContainer createRelationsGroup() {
        PaletteContainer group = new PaletteGroup("Relations");
        add(group);
        
        ConnectionCreationToolEntry magicConnectionEntry = new ConnectionCreationToolEntry(
                "Magic Connector",
                "Create Connections automatically",
                new MagicConnectionModelFactory(),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_MAGIC_CONNECTION_16),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_MAGIC_CONNECTION_16));
        
        magicConnectionEntry.setToolClass(MagicConnectionCreationTool.class);
        magicConnectionEntry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        group.add(magicConnectionEntry);
        
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            ConnectionCreationToolEntry entry = createConnectionCreationToolEntry(eClass, null);
            group.add(entry);
        }
        
        // Junctions
        PaletteStack stack = new PaletteStack("Junctions", "Junctions", null);
        group.add(stack);
        
        for(EClass eClass : ArchimateModelUtils.getConnectorClasses()) {
            PaletteEntry entry = createCombinedTemplateCreationEntry(eClass, null);
            stack.add(entry);
        }
        
        return group;
    }
    
    public void dispose() {
        formatPainterEntry.dispose();
    }
    
    // --------------------------------------------------------------------------------------------
    // Convenience methods
    // --------------------------------------------------------------------------------------------
    
    private CombinedTemplateCreationEntry createCombinedTemplateCreationEntry(EClass eClass, String description) {
        return new CombinedTemplateCreationEntry(
                ArchimateNames.getDefaultShortName(eClass),
                description,
                new DiagramModelFactory(eClass),
                ImageFactory.getImageDescriptor(eClass),
                ImageFactory.getImageDescriptor(eClass));
    }
    
    private ConnectionCreationToolEntry createConnectionCreationToolEntry(EClass eClass, String description) {
        return createConnectionCreationToolEntry(eClass, ArchimateNames.getDefaultName(eClass), description);
    }
    
    private ConnectionCreationToolEntry createConnectionCreationToolEntry(EClass eClass, String name, String description) {
        ConnectionCreationToolEntry entry = new ConnectionCreationToolEntry(
                name,
                description,
                new DiagramModelFactory(eClass),
                ImageFactory.getImageDescriptor(eClass),
                ImageFactory.getImageDescriptor(eClass));
        
        // Ensure Tool gets deselected
        entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        
        return entry;
    }
}
