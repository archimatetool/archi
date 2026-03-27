/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.internal.ui.palette.editparts.PaletteContainerFlowLayout;
import org.eclipse.gef.internal.ui.palette.editparts.ToolbarEditPart;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.ui.palette.PaletteEditPartFactory;

/**
 * CustomPaletteEditPartFactory
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("restriction")
public class CustomPaletteEditPartFactory extends PaletteEditPartFactory {
    
    private static CustomPaletteEditPartFactory instance = new CustomPaletteEditPartFactory();

    public static CustomPaletteEditPartFactory getInstance() {
        return instance;
    }
    
    private CustomPaletteEditPartFactory() {}
    
    /**
     * Custom ToolbarEditPart so we can have a bigger margin and spacing
     */
    private static class CustomToolbarEditPart extends ToolbarEditPart {
        public CustomToolbarEditPart(PaletteToolbar model) {
            super(model);
        }
        
        @Override
        public IFigure createFigure() {
            IFigure figure = super.createFigure();
            figure.setBorder(new MarginBorder(3, 4, 3, 1));
            return figure;
        }
        
        /**
         * super.refreshVisuals() is redundant as {@link #getLayoutSetting()} returns PaletteViewerPreferences.LAYOUT_ICONS
         * and so only the following code is needed, and we've set the border above.
         */
        @Override
        protected void refreshVisuals() {
            if(getContentPane().getLayoutManager() == null) {
                PaletteContainerFlowLayout flow = new PaletteContainerFlowLayout();
                //flow.setMajorSpacing(4);
                flow.setMinorSpacing(6);
                getContentPane().setLayoutManager(flow);
            }
        }
    }
    
    @Override
    protected EditPart createToolbarEditPart(EditPart parentEditPart, Object model) {
        return new CustomToolbarEditPart((PaletteToolbar) model);
    }
}
