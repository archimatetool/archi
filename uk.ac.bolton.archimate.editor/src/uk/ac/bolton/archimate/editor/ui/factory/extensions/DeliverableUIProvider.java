/*******************************************************************************
 * Copyright (c) 2012 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.ui.factory.extensions;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.editparts.extensions.DeliverableEditPart;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.factory.AbstractElementUIProvider;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * Deliverable UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class DeliverableUIProvider extends AbstractElementUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getDeliverable();
    }
    
    @Override
    public EditPart createEditPart() {
        return new DeliverableEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.DeliverableUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_DELIVERABLE_FILLED_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_DELIVERABLE_FILLED_16);
    }

    @Override
    public Color getDefaultColor() {
        return ColorFactory.get(255, 224, 224);
    }
}
