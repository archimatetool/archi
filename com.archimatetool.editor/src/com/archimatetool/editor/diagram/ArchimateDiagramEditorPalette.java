/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import java.util.ArrayList;
import java.util.List;

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
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.model.viewpoints.IViewpoint;



/**
 * PaletteRoot for Archimate Diagram
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramEditorPalette extends AbstractPaletteRoot {
    
    private FormatPainterToolEntry formatPainterEntry;
    
    private IViewpoint fViewpoint;
    
    List<PaletteEntry> fEntries = new ArrayList<PaletteEntry>();
    
    public ArchimateDiagramEditorPalette() {
        createControlsGroup();
        createExtrasGroup();
    }

    /**
     * Update the Palette depending on the Viewpoint
     * @param viewpoint
     */
    public void setViewpoint(IViewpoint viewpoint) {
        fViewpoint = viewpoint;
        
        // Remove 'em all
        for(PaletteEntry entry : fEntries) {
            remove(entry);
        }

        // Re-Create Archimate Relations Group
        createArchimateRelationsGroup();
        
        // Re-Create Archimate Groups
        createArchimateElementGroups();
    }
    
    /**
     * Create a Group of Controls
     */
    private void createControlsGroup() {
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
        
        add(group);
        
        // Relations group will be inserted before this
        add(new PaletteSeparator("relations")); //$NON-NLS-1$
    }

    /**
     * Create a Group of Controls
     */
    private void createExtrasGroup() {
        PaletteContainer group = new PaletteGroup(Messages.ArchimateDiagramEditorPalette_1);
        
        // Note
        ToolEntry noteEntry = new CombinedTemplateCreationEntry(
                Messages.ArchimateDiagramEditorPalette_2,
                Messages.ArchimateDiagramEditorPalette_3,
                new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelNote()),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_NOTE),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_NOTE));
        group.add(noteEntry);
        
        // Group
        ToolEntry groupEntry = new CombinedTemplateCreationEntry(
                Messages.ArchimateDiagramEditorPalette_4,
                Messages.ArchimateDiagramEditorPalette_5,
                new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelGroup()),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_GROUP),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_GROUP));
        group.add(groupEntry);
        
        // Note Connection
        ToolEntry entry = createConnectionCreationToolEntry(
                IArchimatePackage.eINSTANCE.getDiagramModelConnection(),
                Messages.ArchimateDiagramEditorPalette_7);
        group.add(entry);
        
        add(group);
        
        add(new PaletteSeparator("extras")); //$NON-NLS-1$
    }

    /**
     * Relations Types
     */
    private void createArchimateRelationsGroup() {
        PaletteGroup group = new PaletteGroup(Messages.ArchimateDiagramEditorPalette_13);
        add(1, group);
        fEntries.add(group);
        
        // Magic Connector
        ConnectionCreationToolEntry magicConnectionEntry = new ConnectionCreationToolEntry(
                Messages.ArchimateDiagramEditorPalette_14,
                Messages.ArchimateDiagramEditorPalette_15,
                new MagicConnectionModelFactory(),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_MAGIC_CONNECTION),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_MAGIC_CONNECTION));

        magicConnectionEntry.setToolClass(MagicConnectionCreationTool.class);
        magicConnectionEntry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        group.add(magicConnectionEntry);

        // Relations
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            ToolEntry entry = createConnectionCreationToolEntry(eClass, null);
            group.add(entry);
        }

        // Junctions
        for(EClass eClass : ArchimateModelUtils.getConnectorClasses()) {
            ToolEntry entry = createElementCreationToolEntry(eClass, null);
            group.add(entry);
        }
    }
    
    /**
     * Create the Archimate Element groups
     */
    private void createArchimateElementGroups() {
        createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_17, ArchimateModelUtils.getOtherClasses());
        createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_16, ArchimateModelUtils.getStrategyClasses());
        createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_8, ArchimateModelUtils.getBusinessClasses());
        createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_9, ArchimateModelUtils.getApplicationClasses());
        createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_10, ArchimateModelUtils.getTechnologyClasses());
        createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_18, ArchimateModelUtils.getPhysicalClasses());
        createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_11, ArchimateModelUtils.getMotivationClasses());
        createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_12, ArchimateModelUtils.getImplementationMigrationClasses());
    }
    
    /**
     * ArchiMate Types Group
     */
    private void createArchimateElementGroup(String title, EClass[] types) {
        PaletteContainer group = null;
        
        for(EClass eClass : types) {
            if(isAllowedElement(eClass)) {
                if(group == null) {
                    group = new PaletteGroup(title);
                    add(group);
                    fEntries.add(group);
                }
                
                ToolEntry entry = createElementCreationToolEntry(eClass, null);
                group.add(entry);
            }
        }
        
        if(group != null) {
            PaletteSeparator sep = new PaletteSeparator();
            add(sep);
            fEntries.add(sep);
        }
    }
    
    private boolean isAllowedElement(EClass eClass) {
        // Preference to show all pallette elements regardless of Viewpoint
        if(!Preferences.STORE.getBoolean(IPreferenceConstants.VIEWPOINTS_HIDE_PALETTE_ELEMENTS)) {
            return true;
        }
        
        return fViewpoint == null || fViewpoint != null && fViewpoint.isAllowedConcept(eClass);
    }
    
    public void dispose() {
        formatPainterEntry.dispose();
    }
    
    // --------------------------------------------------------------------------------------------
    // Convenience methods
    // --------------------------------------------------------------------------------------------
    
    private ToolEntry createElementCreationToolEntry(EClass eClass, String description) {
        ToolEntry entry = new CombinedTemplateCreationEntry(
                ArchiLabelProvider.INSTANCE.getDefaultName(eClass),
                description,
                new ArchimateDiagramModelFactory(eClass),
                ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass),
                ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass));
        
        return entry;
    }
    
    private ToolEntry createConnectionCreationToolEntry(EClass eClass, String description) {
        ToolEntry entry = new ConnectionCreationToolEntry(
                ArchiLabelProvider.INSTANCE.getDefaultName(eClass),
                description,
                new ArchimateDiagramModelFactory(eClass),
                ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass),
                ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass));
        
        // Ensure Tool gets deselected
        entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        
        return entry;
    }
}
