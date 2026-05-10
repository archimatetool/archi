/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImagePrintFigureOperation;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.swt.graphics.AutoscalingMode;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.ui.factory.IArchimateElementUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;


/**
 * Factory class to create preview images of Figures
 * If the Figure has an alternate figures it will return that
 * 
 * @author Phillip Beauvoir
 */
public class FigureImagePreviewFactory {
    
    private static final int FIGURE_WIDTH = 120;
    private static final int FIGURE_HEIGHT = 55;
    
    /**
     * @return A preview image of the graphical Figure used by eClass of type
     */
    public static Image getPreviewImage(EClass eClass, int type) {
        // Only two types
        if(type < 0 || type > 1) {
            return null;
        }
        
        if(!(ObjectUIFactory.INSTANCE.getProviderForClass(eClass) instanceof IArchimateElementUIProvider uiProvider)) {
            return null;
        }
        
        // No alternate figure
        if(type > 0 && !uiProvider.hasAlternateFigure()) {
            return null;
        }
        
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateElement((IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass));
        dmo.setName(uiProvider.getDefaultName());
        dmo.setType(type);
        dmo.setDeriveElementLineColor(false);

        // Special case for text alignment
        if(eClass == IArchimatePackage.eINSTANCE.getGrouping() && type == 1) {
            dmo.setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_LEFT);
        }

        // Special cases for text position on icon type figures
        if(type == 1) {
            switch(eClass.getClassifierID()) {
                case IArchimatePackage.ASSESSMENT,
                     IArchimatePackage.BUSINESS_ACTOR,
                     IArchimatePackage.CAPABILITY,
                     IArchimatePackage.COMMUNICATION_NETWORK,
                     IArchimatePackage.COURSE_OF_ACTION,
                     IArchimatePackage.DISTRIBUTION_NETWORK,
                     IArchimatePackage.DRIVER,
                     IArchimatePackage.EQUIPMENT,
                     IArchimatePackage.FACILITY,
                     IArchimatePackage.GAP,
                     IArchimatePackage.GOAL,
                     IArchimatePackage.LOCATION,
                     IArchimatePackage.MATERIAL,
                     IArchimatePackage.OUTCOME,
                     IArchimatePackage.PLATEAU,
                     IArchimatePackage.PRINCIPLE,
                     IArchimatePackage.SYSTEM_SOFTWARE,
                     IArchimatePackage.WORK_PACKAGE
                                                 -> dmo.setTextPosition(ITextPosition.TEXT_POSITION_BOTTOM);
            }
        }

        // Use inbuilt default colours
        dmo.setFillColor(ColorFactory.convertColorToString(ColorFactory.getInbuiltDefaultFillColor(dmo)));
        dmo.setLineColor(ColorFactory.convertColorToString(ColorFactory.getInbuiltDefaultLineColor(dmo)));

        GraphicalEditPart editPart = (GraphicalEditPart)uiProvider.createEditPart();
        editPart.setModel(dmo);

        IDiagramModelObjectFigure figure = (IDiagramModelObjectFigure)editPart.getFigure();
        figure.setSize(new Dimension(FIGURE_WIDTH, FIGURE_HEIGHT));
        figure.refreshVisuals();

        return PlatformUtils.isWindows() ? createImageFromImagePrintFigureOperation(figure) : createImage(figure);
    }
    
    /**
     * Create the image using a ImageGcDrawer.
     * This is safe to use on Mac and Linux.
     */
    private static Image createImage(IFigure previewFigure) {
        previewFigure.validate(); // Call this here
        
        return new Image(Display.getDefault(), (gc, width, height) -> {
            SWTGraphics graphics = new SWTGraphics(gc);
            previewFigure.paint(graphics);
            graphics.dispose();
        }, FIGURE_WIDTH, FIGURE_HEIGHT);
    }

    /**
     * Create the image using a ImagePrintFigureOperation. This is needed on Windows.
     */
    private static Image createImageFromImagePrintFigureOperation(IFigure previewFigure) {
        Shell shell = new Shell();
        Image image;
        
        try {
            previewFigure.validate(); // Call this here
            ImagePrintFigureOperation imagePrintOperation = new ImagePrintFigureOperation(shell, previewFigure);
            image = imagePrintOperation.run();
        }
        finally {
            shell.dispose();
        }

        return image;
    }
    
    /**
     * Create the image using a GraphicalViewerImpl
     * The viewer's control has AutoscalingMode enabled which stops text clipping on Windows on the right of text
     * but not on letters with a descender like "g".
     */
    @SuppressWarnings("unused")
    private static Image createImageFromGraphicalViewer(IFigure previewFigure) {
        Shell shell = new Shell();
        Image image;
        
        try {
            GraphicalViewer viewer = new GraphicalViewerImpl();
            viewer.createControl(shell).setAutoscalingMode(AutoscalingMode.ENABLED); // Stops text clipping on Windows

            viewer.setContents(new AbstractGraphicalEditPart() {
                @Override
                protected IFigure createFigure() {
                    return previewFigure;
                }

                @Override
                protected void createEditPolicies() {
                }
            });
            
            // Call validate() here. Don't call viewer.flush() because the wrong figure size is used
            previewFigure.validate();

            image = new Image(Display.getDefault(), (gc, width, height) -> {
                SWTGraphics graphics = new SWTGraphics(gc);
                previewFigure.paint(graphics);
                graphics.dispose();
            }, FIGURE_WIDTH, FIGURE_HEIGHT);
        }
        finally {
            shell.dispose();
        }
        
        return image;
    }
}
