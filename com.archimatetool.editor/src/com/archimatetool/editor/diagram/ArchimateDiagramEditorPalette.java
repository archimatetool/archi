/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.jface.resource.ImageDescriptor;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.tools.ExtConnectionCreationToolEntry;
import com.archimatetool.editor.diagram.tools.MagicConnectionCreationTool;
import com.archimatetool.editor.diagram.tools.MagicConnectionModelFactory;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.model.util.LightweightEContentAdapter;
import com.archimatetool.model.viewpoints.IViewpoint;
import com.archimatetool.model.viewpoints.ViewpointManager;



/**
 * PaletteRoot for Archimate Diagram
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramEditorPalette extends AbstractPaletteRoot {
    
    private static int RELATIONS_GROUP = 1;
    private static int SPECIALIZATIONS_GROUP = 5;
    
    private IArchimateDiagramModel fDiagramModel;
    
    private List<PaletteEntry> fElementEntries = new ArrayList<>();
    private List<PaletteEntry> fRelationshipEntries = new ArrayList<>();
    private List<PaletteEntry> fSpecializationEntries = new ArrayList<>();
    
    /**
     * Adapter to listen to Model's Profile changes
     */
    private LightweightEContentAdapter eAdapter = new LightweightEContentAdapter((msg) -> {
        if(msg.getFeature() == IArchimatePackage.Literals.ARCHIMATE_MODEL__PROFILES) {
            createSpecializationsGroup();
        }
    }, IProfile.class);

    public ArchimateDiagramEditorPalette(IArchimateDiagramModel dm) {
        fDiagramModel = dm;
        
        add(createToolsGroup());
        createArchimateRelationsGroup();
        createExtrasGroup();
        createSpecializationsGroup();
        createArchimateElementGroups();
    }

    /**
     * Update the Palette contents depending on the Viewpoint
     */
    void updateViewpoint() {
        createArchimateRelationsGroup();
        createSpecializationsGroup();
        createArchimateElementGroups();
    }
    
    /**
     * Update the Palette contents to show or not show Specializations
     */
    void updateSpecializations() {
        createSpecializationsGroup();
    }
    
    /**
     * Create a Group of Controls
     */
    private void createExtrasGroup() {
        PaletteContainer group = new PaletteGroup(Messages.ArchimateDiagramEditorPalette_1);
        
        // Note
        ToolEntry noteEntry = new CombinedTemplateCreationEntry(
                ArchiLabelProvider.INSTANCE.getDefaultName(IArchimatePackage.eINSTANCE.getDiagramModelNote()),
                Messages.ArchimateDiagramEditorPalette_3,
                new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelNote()),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_NOTE),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_NOTE));
        group.add(noteEntry);
        PaletteKeyHandler.setKeyBinding(noteEntry, IArchimatePackage.eINSTANCE.getDiagramModelNote().getName());
        
        // Group
        ToolEntry groupEntry = new CombinedTemplateCreationEntry(
                ArchiLabelProvider.INSTANCE.getDefaultName(IArchimatePackage.eINSTANCE.getDiagramModelGroup()),
                Messages.ArchimateDiagramEditorPalette_5,
                new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelGroup()),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_GROUP),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_GROUP));
        group.add(groupEntry);
        PaletteKeyHandler.setKeyBinding(groupEntry, IArchimatePackage.eINSTANCE.getDiagramModelGroup().getName());
        
        // Legend
        ToolEntry legendEntry = new CombinedTemplateCreationEntry(
                Messages.ArchimateDiagramEditorPalette_2,
                Messages.ArchimateDiagramEditorPalette_4,
                new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelNote(), IDiagramModelNote.LEGEND_MODEL_NAME),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_LEGEND),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_LEGEND));
        group.add(legendEntry);
        PaletteKeyHandler.setKeyBinding(legendEntry, IDiagramModelNote.LEGEND_MODEL_NAME);

        // Connection
        ToolEntry entry = createConnectionCreationToolEntry(
                IArchimatePackage.eINSTANCE.getDiagramModelConnection(),
                Messages.ArchimateDiagramEditorPalette_7);
        group.add(entry);
        
        add(group);
        add(new PaletteSeparator());
    }

    /**
     * Relations Types
     */
    private void createArchimateRelationsGroup() {
        // Remove all Archimate Relations
        for(PaletteEntry entry : fRelationshipEntries) {
            remove(entry);
        }
        fRelationshipEntries.clear();

        PaletteGroup group = new PaletteGroup(Messages.ArchimateDiagramEditorPalette_13);
        add(RELATIONS_GROUP, group);
        fRelationshipEntries.add(group);
        
        // Magic Connector
        ConnectionCreationToolEntry magicConnectionEntry = new ConnectionCreationToolEntry(
                Messages.ArchimateDiagramEditorPalette_14,
                Messages.ArchimateDiagramEditorPalette_15,
                new MagicConnectionModelFactory(),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_MAGIC_CONNECTION),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_MAGIC_CONNECTION));

        magicConnectionEntry.setToolClass(MagicConnectionCreationTool.class);
        magicConnectionEntry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        PaletteKeyHandler.setKeyBinding(magicConnectionEntry, "MagicConnector"); //$NON-NLS-1$
        group.add(magicConnectionEntry);

        // Relations
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            if(isAllowedConceptForViewpoint(eClass)) {
                ToolEntry entry = createConnectionCreationToolEntry(eClass, Messages.ArchimateDiagramEditorPalette_6);
                group.add(entry);
            }
        }

        // Junctions
        for(EClass eClass : ArchimateModelUtils.getConnectorClasses()) {
            ToolEntry entry = createElementCreationToolEntry(eClass, Messages.ArchimateDiagramEditorPalette_19);
            group.add(entry);
        }
        
        PaletteSeparator sep = new PaletteSeparator();
        add(RELATIONS_GROUP + 1, sep);
        fRelationshipEntries.add(sep);
    }
    
    /**
     * Create the Archimate Element groups
     */
    private void createArchimateElementGroups() {
        // Remove all Archimate Elements
        for(PaletteEntry entry : fElementEntries) {
            remove(entry);
        }
        fElementEntries.clear();

        createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_17, ArchimateModelUtils.getOtherClasses());
        createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_16, ArchimateModelUtils.getStrategyClasses());
        createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_8, ArchimateModelUtils.getBusinessClasses());
        createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_9, ArchimateModelUtils.getApplicationClasses());
        createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_10, ArchimateModelUtils.getTechnologyClasses());
        createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_18, ArchimateModelUtils.getPhysicalClasses());
        createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_11, ArchimateModelUtils.getMotivationClasses());
        createArchimateElementGroup(Messages.ArchimateDiagramEditorPalette_12, ArchimateModelUtils.getImplementationMigrationClasses());
    }
    
    private void createSpecializationsGroup() {
        // Remove existing ones
        for(PaletteEntry entry : fSpecializationEntries) {
            remove(entry);
        }
        fSpecializationEntries.clear();
        
        if(!ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.SHOW_SPECIALIZATIONS_IN_PALETTE)) {
            fDiagramModel.getArchimateModel().eAdapters().remove(eAdapter);
            return;
        }
        
        // Add eAdapter to listen to changes in Specializations
        if(!fDiagramModel.getArchimateModel().eAdapters().contains(eAdapter)) {
            fDiagramModel.getArchimateModel().eAdapters().add(eAdapter);
        }
        
        List<IProfile> profiles = new ArrayList<>(fDiagramModel.getArchimateModel().getProfiles());
        if(profiles.isEmpty()) {
            return;
        }
        
        // Sort Profiles into Elements, then Relations and by name
        ArchimateModelUtils.sortProfiles(profiles, Collator.getInstance());
        
        PaletteGroup group = new PaletteGroup(Messages.ArchimateDiagramEditorPalette_0);
        
        for(IProfile profile : profiles) {
            EClass eClass = profile.getConceptClass();
            ImageDescriptor id = ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass);

            // Element
            if(IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(eClass) && isAllowedConceptForViewpoint(eClass)) {
                // Add image if it has one
                if(profile.getImagePath() != null) {
                    id = ArchiLabelProvider.INSTANCE.getImageDescriptorForSpecialization(profile);
                }
                
                ToolEntry entry = new CombinedTemplateCreationEntry(
                        profile.getName(),
                        ArchiLabelProvider.INSTANCE.getDefaultName(eClass),
                        new ArchimateDiagramModelFactory(eClass, profile),
                        id,
                        id);
                
                group.add(entry);
            }
            // Relationship
            else if(IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(eClass)) {
                ToolEntry entry = new ExtConnectionCreationToolEntry(
                        profile.getName(),
                        ArchiLabelProvider.INSTANCE.getDefaultName(eClass),
                        new ArchimateDiagramModelFactory(eClass, profile),
                        id,
                        id);
                
                // Ensure Tool gets deselected
                entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
                
                group.add(entry);
            }
        }
        
        if(!group.getChildren().isEmpty()) {
            add(SPECIALIZATIONS_GROUP, group);
            fSpecializationEntries.add(group);
            
            PaletteSeparator sep = new PaletteSeparator();
            add(SPECIALIZATIONS_GROUP + 1, sep);
            fSpecializationEntries.add(sep);
        }
    }
    
    /**
     * ArchiMate Types Group
     */
    private void createArchimateElementGroup(String title, EClass[] types) {
        PaletteContainer group = null;
        
        for(EClass eClass : types) {
            if(isAllowedConceptForViewpoint(eClass)) {
                if(group == null) {
                    group = new PaletteGroup(title);
                    add(group);
                    fElementEntries.add(group);
                }
                
                ToolEntry entry = createElementCreationToolEntry(eClass, Messages.ArchimateDiagramEditorPalette_20);
                group.add(entry);
            }
        }
        
        if(group != null) {
            PaletteSeparator sep = new PaletteSeparator();
            add(sep);
            fElementEntries.add(sep);
        }
    }
    
    private boolean isAllowedConceptForViewpoint(EClass eClass) {
        // Preference to show all pallette elements regardless of Viewpoint
        if(!ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.VIEWPOINTS_HIDE_PALETTE_ELEMENTS)) {
            return true;
        }
        
        IViewpoint vp = ViewpointManager.INSTANCE.getViewpoint(fDiagramModel.getViewpoint());
        
        return vp == null || (vp != null && vp.isAllowedConcept(eClass));
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        fElementEntries = null;
        fRelationshipEntries = null;
        fSpecializationEntries = null;
        
        fDiagramModel.getArchimateModel().eAdapters().remove(eAdapter);
        fDiagramModel = null;
        eAdapter = null;
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
        
        PaletteKeyHandler.setKeyBinding(entry, eClass.getName());
        
        return entry;
    }
    
    private ToolEntry createConnectionCreationToolEntry(EClass eClass, String description) {
        ToolEntry entry = new ExtConnectionCreationToolEntry(
                ArchiLabelProvider.INSTANCE.getDefaultName(eClass),
                description,
                new ArchimateDiagramModelFactory(eClass),
                ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass),
                ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass));
        
        // Ensure Tool gets deselected
        entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        PaletteKeyHandler.setKeyBinding(entry, eClass.getName());
        
        return entry;
    }
}
