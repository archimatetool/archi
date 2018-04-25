/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.ui.FigureImagePreviewFactory;
import com.archimatetool.editor.ui.factory.IArchimateElementUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;



/**
 * Section to change the type of figure for a diagram object
 * 
 * @author Phillip Beauvoir
 */
public class DiagramFigureTypeSection extends AbstractECorePropertySection {

    private static final String HELP_ID = "com.archimatetool.help.diagramFigureTypeSection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            if(object instanceof IDiagramModelArchimateObject) {
                IArchimateElementUIProvider provider = (IArchimateElementUIProvider)ObjectUIFactory.INSTANCE.getProvider((IDiagramModelArchimateObject)object);
                return provider.hasAlternateFigure();
            }
            return false;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelArchimateObject.class;
        }
    }
    
    private ImageFigure figure1, figure2;

    @Override
    protected void createControls(Composite parent) {
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
        
        figure1 = new ImageFigure(parent);
        figure2 = new ImageFigure(parent);
    }
    
    @Override
    protected void update() {
        IDiagramModelArchimateObject diagramObject = (IDiagramModelArchimateObject)getFirstSelectedObject();
        
        IArchimateElement element = diagramObject.getArchimateElement();
        
        Image image1 = FigureImagePreviewFactory.getFigurePreviewImageForClass(element.eClass());
        Image image2 = FigureImagePreviewFactory.getAlternateFigurePreviewImageForClass(element.eClass());
        
        figure1.setImage(image1);
        figure2.setImage(image2);
        
        figure1.getParent().layout();

        int type = diagramObject.getType();
        figure1.setSelected(type == 0);
        figure2.setSelected(type == 1);
    }

    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE) {
                update();
            }
        }
    }

    @Override
    public boolean shouldUseExtraSpace() {
        // Need this so different image heights draw correctly
        return true;
    }
    
    private class ImageFigure extends Composite {
        boolean selected;
        Label label;

        public ImageFigure(Composite parent) {
            super(parent, SWT.NULL);
            setBackgroundMode(SWT.INHERIT_DEFAULT);
            GridLayout gridLayout = new GridLayout();
            gridLayout.marginWidth = 3;
            gridLayout.marginHeight = 3;
            setLayout(gridLayout);
            
            addPaintListener(new PaintListener() {
                @Override
                public void paintControl(PaintEvent e) {
                    if(selected) {
                        GC graphics = e.gc;
                        graphics.setForeground(ColorConstants.blue);
                        graphics.setLineWidth(2);
                        Rectangle bounds = getBounds();
                        graphics.drawRectangle(1, 1, bounds.width - 2, bounds.height - 2);
                    }
                }
            });
            
            label = new Label(this, SWT.NULL);
            getWidgetFactory().adapt(this);
            
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseDown(MouseEvent e) {
                    IDiagramModelArchimateObject diagramObject = (IDiagramModelArchimateObject)getFirstSelectedObject();
                    
                    if(!selected && isAlive(diagramObject)) {
                        int newType = diagramObject.getType() == 0 ? 1 : 0;
                        
                        executeCommand(new EObjectFeatureCommand(Messages.DiagramFigureTypeSection_0, diagramObject,
                                IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE, newType));
                    }
                }
            });
        }
        
        void setImage(Image image) {
            label.setImage(image);
        }
        
        void setSelected(boolean set) {
            selected = set;
            redraw();
        }
    }

}
