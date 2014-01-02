/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.ui.IArchimateImages;


/**
 * PoofAnimater
 * 
 * @author Phillip Beauvoir
 */
class PoofAnimater {
    private static Image[] image = new Image[5];
    private static int width, height;

    private FigureCanvas canvas;
    private int x, y;
    private int new_x, new_y;
    private Point viewPortLocation;
    private int index;
    
    private PaintListener listener = new PaintListener() {
        @Override
        public void paintControl(PaintEvent event) {
            event.gc.drawImage(image[index], new_x, new_y);
        }
    };

    PoofAnimater(FigureCanvas canvas, int x, int y) {
        this.canvas = canvas;
        viewPortLocation = canvas.getViewport().getViewLocation();
        this.x = x;
        this.y = y;
    }

    void animate(boolean forward) {
        // In this case, the user closed the view between undo/redo
        // TODO - re-acquire the current canvas
        if(canvas == null || canvas.isDisposed()) {
            return;
        }
        
        if(image[0] == null) {
            loadImages();
        }
        
        // Compensate for user might have scrolled the viewport since last poof
        Point ptViewPort = canvas.getViewport().getViewLocation();
        new_x = x - (ptViewPort.x - viewPortLocation.x);
        new_y = y - (ptViewPort.y - viewPortLocation.y);
        
        index = forward ? 0 : 4;
        canvas.redraw();
        Display.getCurrent().update();
        
        canvas.addPaintListener(listener);
        
        for(int i = 0; i < 5; i++) {
            canvas.redraw(new_x, new_y, width, height, false);
            Display.getCurrent().update();
            
            try { Thread.sleep(30); } catch(InterruptedException ex) { }
            
            if(forward) {
                index ++;
            }
            else {
                index--;
            }
        }

        canvas.removePaintListener(listener);
        canvas.redraw();
        Display.getCurrent().update();
    }
    
    private void loadImages() {
        image[0] = IArchimateImages.ImageFactory.getImage("img/poof1.png"); //$NON-NLS-1$
        image[1] = IArchimateImages.ImageFactory.getImage("img/poof2.png"); //$NON-NLS-1$
        image[2] = IArchimateImages.ImageFactory.getImage("img/poof3.png"); //$NON-NLS-1$
        image[3] = IArchimateImages.ImageFactory.getImage("img/poof4.png"); //$NON-NLS-1$
        image[4] = IArchimateImages.ImageFactory.getImage("img/poof5.png"); //$NON-NLS-1$
        width = image[0].getImageData().width;
        height = image[0].getImageData().height;
    }
}