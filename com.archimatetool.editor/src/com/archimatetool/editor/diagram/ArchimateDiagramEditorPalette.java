/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

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

import com.archimatetool.editor.diagram.tools.FormatPainterToolEntry;
import com.archimatetool.editor.diagram.tools.MagicConnectionCreationTool;
import com.archimatetool.editor.diagram.tools.MagicConnectionModelFactory;
import com.archimatetool.editor.diagram.tools.PanningSelectionExtendedTool;
import com.archimatetool.editor.model.viewpoints.IViewpoint;
import com.archimatetool.editor.ui.ArchimateLabelProvider;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * PaletteRoot for Archimate Diagram
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramEditorPalette extends AbstractPaletteRoot {
    
    private FormatPainterToolEntry formatPainterEntry;
    
    private IViewpoint fViewpoint;
    
    private PaletteContainer fRelationsGroup;
    
    private PaletteContainer fBusinessGroup, fApplicationGroup, fTechnologyGroup, fMotivationGroup, fImplementationMigrationGroup;

    public ArchimateDiagramEditorPalette() {
        add(createControlsGroup());
        add(new PaletteSeparator("")); //$NON-NLS-1$
        
        fRelationsGroup = createRelationsGroup();
        add(fRelationsGroup);
        add(new PaletteSeparator("")); //$NON-NLS-1$

        add(createExtrasGroup());
        add(new PaletteSeparator("")); //$NON-NLS-1$
        
        createArchimateGroup();
    }

    /**
     * Update the Palette depending on the Viewpoint
     * @param viewpoint
     */
    public void setViewpoint(IViewpoint viewpoint) {
        if(fViewpoint != viewpoint) {
            fViewpoint = viewpoint;
            
            remove(fRelationsGroup);
            fRelationsGroup = createRelationsGroup();
            add(1, fRelationsGroup);
            
            remove(fBusinessGroup);
            remove(fApplicationGroup);
            remove(fTechnologyGroup);
            remove(fMotivationGroup);
            remove(fImplementationMigrationGroup);
            createArchimateGroup();
        }
    }
    
    /**
     * Create the Archimate groups
     */
    private void createArchimateGroup() {
        fBusinessGroup = createBusinessLayerGroup();
        fApplicationGroup = createApplicationLayerGroup();
        fTechnologyGroup = createTechnologyLayerGroup();
        fMotivationGroup = createMotivationGroup();
        fImplementationMigrationGroup = createImplementationMigrationGroup();
        
        if(!fBusinessGroup.getChildren().isEmpty()) {
            add(fBusinessGroup);
        }
        
        if(!fApplicationGroup.getChildren().isEmpty()) {
            add(new PaletteSeparator("")); //$NON-NLS-1$
            add(fApplicationGroup);
        }
        
        if(!fTechnologyGroup.getChildren().isEmpty()) {
            add(new PaletteSeparator("")); //$NON-NLS-1$
            add(fTechnologyGroup);
        }
        
        if(!fMotivationGroup.getChildren().isEmpty()) {
            add(new PaletteSeparator("")); //$NON-NLS-1$
            add(fMotivationGroup);
        }

        if(!fImplementationMigrationGroup.getChildren().isEmpty()) {
            add(new PaletteSeparator("")); //$NON-NLS-1$
            add(fImplementationMigrationGroup);
        }
    }
    
    /**
     * Create a Group of Controls
     */
    private PaletteContainer createControlsGroup() {
        PaletteContainer group = new PaletteToolbar(Messages.ArchimateDiagramEditorPalette_0);
        
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

    /**
     * Create a Group of Controls
     */
    private PaletteContainer createExtrasGroup() {
        PaletteContainer group = new PaletteGroup(Messages.ArchimateDiagramEditorPalette_1);
        
        // Note
        PaletteEntry noteEntry = new CombinedTemplateCreationEntry(
                Messages.ArchimateDiagramEditorPalette_2,
                Messages.ArchimateDiagramEditorPalette_3,
                new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelNote()),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_NOTE_16),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_NOTE_16));
        group.add(noteEntry);
        
        // Group
        PaletteEntry groupEntry = new CombinedTemplateCreationEntry(
                Messages.ArchimateDiagramEditorPalette_4,
                Messages.ArchimateDiagramEditorPalette_5,
                new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelGroup()),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_GROUP_16),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_GROUP_16));
        group.add(groupEntry);
        
        // Note Connection
        ConnectionCreationToolEntry entry = createConnectionCreationToolEntry(
                IArchimatePackage.eINSTANCE.getDiagramModelConnection(),
                Messages.ArchimateDiagramEditorPalette_6,
                Messages.ArchimateDiagramEditorPalette_7);
        group.add(entry);
        
        return group;
    }

    /**
     * Business Palette
     */
    private PaletteContainer createBusinessLayerGroup() {
        PaletteContainer group = new PaletteGroup(Messages.ArchimateDiagramEditorPalette_8);
        
        for(EClass eClass : ArchimateModelUtils.getBusinessClasses()) {
            if(isAllowedType(eClass)) {
                PaletteEntry entry = createCombinedTemplateCreationEntry(eClass, null);
                group.add(entry);
            }
        }
        
        return group;
    }

    /**
     * Application Palette
     */
    private PaletteContainer createApplicationLayerGroup() {
        PaletteContainer group = new PaletteGroup(Messages.ArchimateDiagramEditorPalette_9);
        
        for(EClass eClass : ArchimateModelUtils.getApplicationClasses()) {
            if(isAllowedType(eClass)) {
                PaletteEntry entry = createCombinedTemplateCreationEntry(eClass, null);
                group.add(entry);
            }
        }
        
        return group;
    }

    /**
     * Technology Palette
     */
    private PaletteContainer createTechnologyLayerGroup() {
        PaletteContainer group = new PaletteGroup(Messages.ArchimateDiagramEditorPalette_10);
        
        for(EClass eClass : ArchimateModelUtils.getTechnologyClasses()) {
            if(isAllowedType(eClass)) {
                PaletteEntry entry = createCombinedTemplateCreationEntry(eClass, null);
                group.add(entry);
            }
        }
        
        return group;
    }

    /**
     * Motivation Palette
     */
    private PaletteContainer createMotivationGroup() {
        PaletteContainer group = new PaletteGroup(Messages.ArchimateDiagramEditorPalette_11);
        
        for(EClass eClass : ArchimateModelUtils.getMotivationClasses()) {
            if(isAllowedType(eClass)) {
                PaletteEntry entry = createCombinedTemplateCreationEntry(eClass, null);
                group.add(entry);
            }
        }
        
        return group;
    }

    /**
     * Implementation & Migration Palette
     */
    private PaletteContainer createImplementationMigrationGroup() {
        PaletteContainer group = new PaletteGroup(Messages.ArchimateDiagramEditorPalette_12);
        
        for(EClass eClass : ArchimateModelUtils.getImplementationMigrationClasses()) {
            if(isAllowedType(eClass)) {
                PaletteEntry entry = createCombinedTemplateCreationEntry(eClass, null);
                group.add(entry);
            }
        }
        
        return group;
    }

    /**
     * Relations Palette
     */
    private PaletteContainer createRelationsGroup() {
        PaletteContainer group = new PaletteGroup(Messages.ArchimateDiagramEditorPalette_13);
        
        ConnectionCreationToolEntry magicConnectionEntry = new ConnectionCreationToolEntry(
                Messages.ArchimateDiagramEditorPalette_14,
                Messages.ArchimateDiagramEditorPalette_15,
                new MagicConnectionModelFactory(),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_MAGIC_CONNECTION_16),
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_MAGIC_CONNECTION_16));
        
        magicConnectionEntry.setToolClass(MagicConnectionCreationTool.class);
        magicConnectionEntry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        group.add(magicConnectionEntry);
        
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            if(isAllowedType(eClass)) {
                ConnectionCreationToolEntry entry = createConnectionCreationToolEntry(eClass, null);
                group.add(entry);
            }
        }
        
        // Junctions
        PaletteStack stack = null;
        
        for(EClass eClass : ArchimateModelUtils.getConnectorClasses()) {
            if(isAllowedType(eClass)) {
                if(stack == null) {
                    stack = new PaletteStack(Messages.ArchimateDiagramEditorPalette_16, Messages.ArchimateDiagramEditorPalette_16, null);
                    group.add(stack);
                }
                PaletteEntry entry = createCombinedTemplateCreationEntry(eClass, null);
                stack.add(entry);
            }
        }
        
        return group;
    }
    
    private boolean isAllowedType(EClass eClass) {
        return fViewpoint == null || fViewpoint != null && fViewpoint.isAllowedType(eClass);
    }
    
    public void dispose() {
        formatPainterEntry.dispose();
    }
    
    // --------------------------------------------------------------------------------------------
    // Convenience methods
    // --------------------------------------------------------------------------------------------
    
    private CombinedTemplateCreationEntry createCombinedTemplateCreationEntry(EClass eClass, String description) {
        return new CombinedTemplateCreationEntry(
                ArchimateLabelProvider.INSTANCE.getDefaultShortName(eClass),
                description,
                new ArchimateDiagramModelFactory(eClass),
                ArchimateLabelProvider.INSTANCE.getImageDescriptor(eClass),
                ArchimateLabelProvider.INSTANCE.getImageDescriptor(eClass));
    }
    
    private ConnectionCreationToolEntry createConnectionCreationToolEntry(EClass eClass, String description) {
        return createConnectionCreationToolEntry(eClass, ArchimateLabelProvider.INSTANCE.getDefaultName(eClass), description);
    }
    
    private ConnectionCreationToolEntry createConnectionCreationToolEntry(EClass eClass, String name, String description) {
        ConnectionCreationToolEntry entry = new ConnectionCreationToolEntry(
                name,
                description,
                new ArchimateDiagramModelFactory(eClass),
                ArchimateLabelProvider.INSTANCE.getImageDescriptor(eClass),
                ArchimateLabelProvider.INSTANCE.getImageDescriptor(eClass));
        
        // Ensure Tool gets deselected
        entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        
        return entry;
    }
}
