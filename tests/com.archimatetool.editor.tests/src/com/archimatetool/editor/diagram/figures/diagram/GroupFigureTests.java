/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigureTests;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelGroup;



@SuppressWarnings("nls")
public class GroupFigureTests extends AbstractTextControlContainerFigureTests {
    
    private GroupFigure figure;
    private IDiagramModelGroup dmGroup;
    

    @Override
    protected GroupFigure createFigure() {
        dmGroup = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dmGroup.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dmGroup.setName("Group Test");
        dm.getChildren().add(dmGroup);
        
        editor.layoutPendingUpdates();
        
        figure = (GroupFigure)editor.findFigure(dmGroup);
        return figure;
    }

}