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
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
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
    
    private PaletteContainer fStrategyGroup;
    private PaletteContainer fBusinessGroup;
    private PaletteContainer fApplicationGroup;
    private PaletteContainer fTechnologyGroup;
    private PaletteContainer fPhysicalGroup;
    private PaletteContainer fMotivationGroup;
    private PaletteContainer fImplementationMigrationGroup;
    private PaletteContainer fOtherGroup;

    public ArchimateDiagramEditorPalette() {
        add(createControlsGroup());
        add(new PaletteSeparator("")); //$NON-NLS-1$
        
        fRelationsGroup = createArchimateRelationsGroup();
        add(fRelationsGroup);
        add(new PaletteSeparator("")); //$NON-NLS-1$

        add(createExtrasGroup());
        add(new PaletteSeparator("")); //$NON-NLS-1$
        
        createArchimateElementGroups();
    }

    /**
     * Update the Palette depending on the Viewpoint
     * @param viewpoint
     */
    public void setViewpoint(IViewpoint viewpoint) {
        if(fViewpoint != viewpoint) {
            fViewpoint = viewpoint;
            
            remove(fRelationsGroup);
            fRelationsGroup = createArchimateRelationsGroup();
            add(1, fRelationsGroup);
            
            remove(fStrategyGroup);
            remove(fBusinessGroup);
            remove(fApplicationGroup);
            remove(fTechnologyGroup);
            remove(fPhysicalGroup);
            remove(fMotivationGroup);
            remove(fImplementationMigrationGroup);
            remove(fOtherGroup);
            
            createArchimateElementGroups();
        }
    }
    
    /**
     * Create the Archimate Element groups
     */
    private void createArchimateElementGroups() {
        fStrategyGroup = createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_16, ArchimateModelUtils.getStrategyClasses());
        fBusinessGroup = createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_8, ArchimateModelUtils.getBusinessClasses());
        fApplicationGroup = createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_9, ArchimateModelUtils.getApplicationClasses());
        fTechnologyGroup = createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_10, ArchimateModelUtils.getTechnologyClasses());
        fPhysicalGroup = createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_18, ArchimateModelUtils.getPhysicalClasses());
        fMotivationGroup = createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_11, ArchimateModelUtils.getMotivationClasses());
        fImplementationMigrationGroup = createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_12, ArchimateModelUtils.getImplementationMigrationClasses());
        fOtherGroup = createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_17, ArchimateModelUtils.getOtherClasses());
        
        if(!fStrategyGroup.getChildren().isEmpty()) {
            add(fStrategyGroup);
        }
        
        if(!fBusinessGroup.getChildren().isEmpty()) {
            add(new PaletteSeparator("")); //$NON-NLS-1$
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
        
        if(!fPhysicalGroup.getChildren().isEmpty()) {
            add(new PaletteSeparator("")); //$NON-NLS-1$
            add(fPhysicalGroup);
        }
        
        if(!fMotivationGroup.getChildren().isEmpty()) {
            add(new PaletteSeparator("")); //$NON-NLS-1$
            add(fMotivationGroup);
        }

        if(!fImplementationMigrationGroup.getChildren().isEmpty()) {
            add(new PaletteSeparator("")); //$NON-NLS-1$
            add(fImplementationMigrationGroup);
        }
        
        if(!fOtherGroup.getChildren().isEmpty()) {
            add(new PaletteSeparator("")); //$NON-NLS-1$
            add(fOtherGroup);
        }
    }
    
    /**
     * Strategy Types
     */
    private PaletteContainer createArchimateElementGroup(String title, EClass[] types) {
        PaletteContainer group = new PaletteGroup(title);
        
        for(EClass eClass : types) {
            if(isAllowedType(eClass)) {
                PaletteEntry entry = createCombinedTemplateCreationEntry(eClass, null);
                group.add(entry);
            }
        }
        
        return group;
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
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_NOTE),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_NOTE));
        group.add(noteEntry);
        
        // Group
        PaletteEntry groupEntry = new CombinedTemplateCreationEntry(
                Messages.ArchimateDiagramEditorPalette_4,
                Messages.ArchimateDiagramEditorPalette_5,
                new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelGroup()),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_GROUP),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_GROUP));
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
     * Relations Types
     */
    private PaletteContainer createArchimateRelationsGroup() {
        PaletteContainer group = new PaletteGroup(Messages.ArchimateDiagramEditorPalette_13);
        
        ConnectionCreationToolEntry magicConnectionEntry = new ConnectionCreationToolEntry(
                Messages.ArchimateDiagramEditorPalette_14,
                Messages.ArchimateDiagramEditorPalette_15,
                new MagicConnectionModelFactory(),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_MAGIC_CONNECTION),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_MAGIC_CONNECTION));
        
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
        for(EClass eClass : ArchimateModelUtils.getConnectorClasses()) {
            if(isAllowedType(eClass)) {
                PaletteEntry entry = createCombinedTemplateCreationEntry(eClass, null);
                group.add(entry);
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
                ArchiLabelProvider.INSTANCE.getDefaultName(eClass),
                description,
                new ArchimateDiagramModelFactory(eClass),
                ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass),
                ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass));
    }
    
    private ConnectionCreationToolEntry createConnectionCreationToolEntry(EClass eClass, String description) {
        return createConnectionCreationToolEntry(eClass, ArchiLabelProvider.INSTANCE.getDefaultName(eClass), description);
    }
    
    private ConnectionCreationToolEntry createConnectionCreationToolEntry(EClass eClass, String name, String description) {
        ConnectionCreationToolEntry entry = new ConnectionCreationToolEntry(
                name,
                description,
                new ArchimateDiagramModelFactory(eClass),
                ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass),
                ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass));
        
        // Ensure Tool gets deselected
        entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        
        return entry;
    }
}
