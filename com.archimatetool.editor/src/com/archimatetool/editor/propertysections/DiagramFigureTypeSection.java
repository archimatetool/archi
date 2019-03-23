/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
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
        
        figure1 = new ImageFigure(parent, 0);
        figure2 = new ImageFigure(parent, 1);
    }
    
    @Override
    protected void update() {
        figure1.setVisible(false);
        figure2.setVisible(false);
        
        // Ensure we have the same type of figure
        for(int i = 0; i < getEObjects().size() - 1; i++) {
            IDiagramModelArchimateObject first = (IDiagramModelArchimateObject)getEObjects().get(i);
            IDiagramModelArchimateObject next = (IDiagramModelArchimateObject)getEObjects().get(i + 1);
            if(first.getArchimateConcept().eClass() != next.getArchimateConcept().eClass()) {
                return;
            }
        }
        
        figure1.setVisible(true);
        figure2.setVisible(true);
        
        IDiagramModelArchimateObject firstSelected = (IDiagramModelArchimateObject)getFirstSelectedObject();
        IArchimateElement element = firstSelected.getArchimateElement();
        
        Image image1 = FigureImagePreviewFactory.getPreviewImage(element.eClass(), 0);
        Image image2 = FigureImagePreviewFactory.getPreviewImage(element.eClass(), 1);
        
        figure1.setImage(image1);
        figure2.setImage(image2);
        
        figure1.getParent().layout();

        int type = firstSelected.getType();
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

        public ImageFigure(Composite parent, int value) {
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
                    CompoundCommand result = new CompoundCommand();

                    for(EObject eObject : getEObjects()) {
                        if(isAlive(eObject) && !isLocked(eObject)) {
                            Command cmd = new EObjectFeatureCommand(Messages.DiagramFigureTypeSection_0, eObject,
                                    IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE, value);
                            if(cmd.canExecute()) {
                                result.add(cmd);
                            }
                        }
                    }

                    executeCommand(result.unwrap());
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
