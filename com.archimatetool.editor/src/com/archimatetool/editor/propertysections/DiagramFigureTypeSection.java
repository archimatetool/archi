/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IFilter;
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

import com.archimatetool.editor.diagram.editparts.IArchimateEditPart;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.ui.FigureChooser;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Section to change the type of figure for a diagram object
 * 
 * @author Phillip Beauvoir
 */
public class DiagramFigureTypeSection extends AbstractArchimatePropertySection {

    private static final String HELP_ID = "com.archimatetool.help.diagramFigureTypeSection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter implements IFilter {
        @Override
        public boolean select(Object object) {
           if(object instanceof IArchimateEditPart) {
               IArchimateElement element = (IArchimateElement)((IArchimateEditPart)object).getAdapter(IArchimateElement.class);
               return FigureChooser.hasAlternateFigure(element);
           }
           return false;
        }
    }
    
    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Model event (Undo/Redo and here!)
            if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE) {
                refreshControls();
            }
        }
    };
    
    private IDiagramModelArchimateObject fDiagramObject;
    
    private ImageFigure figure1, figure2;

    @Override
    protected void createControls(Composite parent) {
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
        
        figure1 = new ImageFigure(parent);
        figure2 = new ImageFigure(parent);
    }
    
    protected void refreshControls() {
        IArchimateElement element = fDiagramObject.getArchimateElement();
        
        Image[] images = FigureChooser.getFigurePreviewImagesForElement(element);
        
        figure1.setImage(images[0]);
        figure2.setImage(images[1]);
        
        int type = fDiagramObject.getType();
        figure1.setSelected(type == 0);
        figure2.setSelected(type == 1);
    }

    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fDiagramObject;
    }

    @Override
    protected void setElement(Object element) {
        if(element instanceof IArchimateEditPart) {
            fDiagramObject = (IDiagramModelArchimateObject)((IAdaptable)element).getAdapter(IDiagramModelObject.class);
        }
        if(fDiagramObject == null) {
            System.err.println("Diagram Object was null in " + getClass()); //$NON-NLS-1$
        }
        
        refreshControls();
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
                    if(!selected && isAlive()) {
                        int newType = fDiagramObject.getType() == 0 ? 1 : 0;
                        getCommandStack().execute(new EObjectFeatureCommand(Messages.DiagramFigureTypeSection_0, getEObject(),
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
