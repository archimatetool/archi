/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import java.util.Objects;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
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
            if(object instanceof IDiagramModelArchimateObject dmo) {
                IArchimateElementUIProvider provider = (IArchimateElementUIProvider)ObjectUIFactory.INSTANCE.getProvider(dmo);
                return provider.hasAlternateFigure();
            }
            return false;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelArchimateObject.class;
        }
    }
    
    /**
     * Singleton Filter instance
     */
    private static final Filter FILTER = new Filter();
    
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
        // Ensure we have selected the same type of figure
        for(int i = 0; i < getEObjects().size() - 1; i++) {
            IDiagramModelArchimateObject first = (IDiagramModelArchimateObject)getEObjects().get(i);
            IDiagramModelArchimateObject next = (IDiagramModelArchimateObject)getEObjects().get(i + 1);
            if(first.getArchimateConcept().eClass() != next.getArchimateConcept().eClass()) {
                figure1.setVisible(false);
                figure2.setVisible(false);
                return;
            }
        }
        
        figure1.setVisible(true);
        figure2.setVisible(true);
        
        IDiagramModelArchimateObject firstSelected = (IDiagramModelArchimateObject)getFirstSelectedObject();
        IArchimateElement element = firstSelected.getArchimateElement();
        
        figure1.update(element.eClass(), 0);
        figure2.update(element.eClass(), 1);
        
        figure1.getParent().layout();

        int type = firstSelected.getType();
        figure1.setSelected(type == 0);
        figure2.setSelected(type == 1);
    }

    @Override
    protected IObjectFilter getFilter() {
        return FILTER;
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        Object feature = msg.getFeature();

        if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE) {
            update();
        }
    }

    @Override
    public boolean shouldUseExtraSpace() {
        // Need this so different image heights draw correctly
        return true;
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        if(figure1 != null) {
            figure1.disposeImage();
        }
        if(figure2 != null) {
            figure2.disposeImage();
        }
    }
    
    private class ImageFigure extends Composite {
        boolean selected;
        Label label;
        EClass eClass;
        Image image;

        public ImageFigure(Composite parent, int value) {
            super(parent, SWT.NULL);
            setBackgroundMode(SWT.INHERIT_DEFAULT);
            getWidgetFactory().adapt(this);
            GridLayoutFactory.swtDefaults().margins(4, 4).applyTo(this);
            
            addPaintListener(new PaintListener() {
                @Override
                public void paintControl(PaintEvent e) {
                    if(selected) {
                        GC graphics = e.gc;
                        graphics.setForeground(ColorConstants.blue);
                        graphics.setLineWidth(2);
                        graphics.drawRectangle(1, 1, e.width - 2, e.height - 2);
                    }
                }
            });
            
            label = new Label(this, SWT.NONE);
            
            label.addListener(SWT.MouseDown, e -> {
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
            });
        }
        
        void update(EClass eClass, int type) {
            if(!Objects.equals(eClass, this.eClass)) {
                disposeImage();
                image = FigureImagePreviewFactory.getPreviewImage(eClass, type);
                label.setImage(image);
            }
            
            this.eClass = eClass;
        }

        void disposeImage() {
            if(image != null) {
                image.dispose();
            }
        }
        
        void setSelected(boolean set) {
            selected = set;
            redraw();
        }
    }

}
