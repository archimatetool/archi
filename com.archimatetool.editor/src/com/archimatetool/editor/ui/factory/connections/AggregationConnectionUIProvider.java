/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.connections;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.connections.AggregationConnectionEditPart;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Aggregation Connection UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class AggregationConnectionUIProvider extends AbstractConnectionUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getAggregationRelationship();
    }
    
    @Override
    public EditPart createEditPart() {
        return new AggregationConnectionEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.AggregationConnectionUIProvider_0;
    }

    @Override
    public String getDefaultShortName() {
        return Messages.AggregationConnectionUIProvider_1;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_AGGREGATION_CONNECTION_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_AGGREGATION_CONNECTION_16);
    }
}
