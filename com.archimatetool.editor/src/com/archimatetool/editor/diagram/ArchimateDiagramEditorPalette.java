/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.tools.ExtCombinedTemplateCreationEntry;
import com.archimatetool.editor.diagram.tools.ExtConnectionCreationToolEntry;
import com.archimatetool.editor.diagram.tools.MagicConnectionCreationTool;
import com.archimatetool.editor.diagram.tools.MagicConnectionModelFactory;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimatePackage;
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
    
    private IArchimateDiagramModel fDiagramModel;
    
    private List<PaletteEntry> fElementEntries = new ArrayList<PaletteEntry>();
    private List<PaletteEntry> fSpecializationEntries = new ArrayList<PaletteEntry>();
    
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
        ToolEntry noteEntry = new ExtCombinedTemplateCreationEntry(
                Messages.ArchimateDiagramEditorPalette_2,
                Messages.ArchimateDiagramEditorPalette_3,
                new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelNote()),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_NOTE),
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_NOTE));
        group.add(noteEntry);
        
        // Group
        ToolEntry groupEntry = new ExtCombinedTemplateCreationEntry(
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
        add(new PaletteSeparator());
    }

    /**
     * Relations Types
     */
    private void createArchimateRelationsGroup() {
        PaletteGroup group = new PaletteGroup(Messages.ArchimateDiagramEditorPalette_13);
        
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
        
        add(group);
        add(new PaletteSeparator());
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
        
        if(!ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.SHOW_SPECIALIZATIONS_IN_PALETTE)) {
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
        
        // Sort Profiles into Elements, then Relations
        Collections.sort(profiles, Comparator.comparing(IProfile::getConceptClass, (c1, c2) -> {
            if((IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(c1) && IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(c2))
                    || (IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(c1) && IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(c2))) {
                return 0;
            }
            
            if(IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(c1)) {
                return -1;
            }
            
            return 1;
        }).thenComparing(IProfile::getName));
        
        PaletteGroup group = new PaletteGroup(Messages.ArchimateDiagramEditorPalette_0);
        
        for(IProfile profile : profiles) {
            EClass eClass = profile.getConceptClass();
            ImageDescriptor id = ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass);

            // Element
            if(IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(eClass) && isAllowedConceptForViewpoint(eClass)) {
                // Add image if it has one
                if(profile.getImagePath() != null) {
                    id = createImageDescriptorForSpecialization(profile);
                }
                
                ToolEntry entry = new ExtCombinedTemplateCreationEntry(
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
            add(5, group);
            PaletteSeparator sep = new PaletteSeparator();
            add(6, sep);

            fSpecializationEntries.add(group);
            fSpecializationEntries.add(sep);
        }
    }
    
    /**
     * Create an ImageDescriptor for a Specialization from its image
     * The image data is created as 16x16 or 32x32 depending on zoom.
     * The user image is scaled to fit and centred on the background image.
     * The background color is set to something unlikely to be used in the actual image so that we can set the transparent pixel
     */
    private ImageDescriptor createImageDescriptorForSpecialization(IProfile profile) {
        return new ImageDescriptor() {
            @Override
            public ImageData getImageData(int zoom) {
                // Get the Specialization image
                Image image = null;
                try {
                    IArchiveManager archiveManager = (IArchiveManager)fDiagramModel.getAdapter(IArchiveManager.class);
                    image = archiveManager.createImage(profile.getImagePath());
                    if(image == null) {
                        throw new Exception("Image was null"); //$NON-NLS-1$
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                    return ArchiLabelProvider.INSTANCE.getImageDescriptor(profile.getConceptClass()).getImageData(zoom);
                }
                
                // Image bounds
                final Rectangle imageBounds = image.getBounds();

                // Palette icon size
                final int iconSize = 16;

                // Blank icon image for background and size
                Image iconImage = new Image(Display.getDefault(), iconSize, iconSize);

                GC gc = new GC(iconImage);
                gc.setAntialias(SWT.ON);
                gc.setInterpolation(SWT.HIGH);
                
                // Set background to this color so we can make it transparent
                RGB background = new RGB(255, 255, 254);
                gc.setBackground(new Color(background));
                gc.fillRectangle(0, 0, iconSize, iconSize);
                
                // Get scaled size
                Rectangle scaledSize = ImageFactory.getScaledImageSize(image, iconSize);
                
                // Centre the image
                int x = (iconSize - scaledSize.width) / 2;
                int y = (iconSize - scaledSize.height) / 2;
                
                // Draw scaled image onto icon image
                gc.drawImage(image, 0, 0, imageBounds.width, imageBounds.height,
                        x, y, scaledSize.width, scaledSize.height);
                
                ImageData data = iconImage.getImageData(zoom);

                // Set transparent pixel to background color
                data.transparentPixel = data.palette.getPixel(background);
                
                gc.dispose();
                image.dispose();
                iconImage.dispose();
                
                return data;
            }
        };
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
                
                ToolEntry entry = createElementCreationToolEntry(eClass, null);
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
        if(!ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.VIEWPOINTS_HIDE_PALETTE_ELEMENTS)) {
            return true;
        }
        
        IViewpoint vp = ViewpointManager.INSTANCE.getViewpoint(fDiagramModel.getViewpoint());
        
        return vp == null || (vp != null && vp.isAllowedConcept(eClass));
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        fElementEntries = null;
        fSpecializationEntries = null;
        
        fDiagramModel.getArchimateModel().eAdapters().remove(eAdapter);
        fDiagramModel = null;
        eAdapter = null;
    }
    
    // --------------------------------------------------------------------------------------------
    // Convenience methods
    // --------------------------------------------------------------------------------------------
    
    private ToolEntry createElementCreationToolEntry(EClass eClass, String description) {
        ToolEntry entry = new ExtCombinedTemplateCreationEntry(
                ArchiLabelProvider.INSTANCE.getDefaultName(eClass),
                description,
                new ArchimateDiagramModelFactory(eClass),
                ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass),
                ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass));
        
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
        
        return entry;
    }
}
