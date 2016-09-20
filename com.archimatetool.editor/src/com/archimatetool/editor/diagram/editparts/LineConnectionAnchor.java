/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.AbstractPointListShape;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;


/**
 * Anchor for connections
 * 
 * @author Phillip Beauvoir
 */
public class LineConnectionAnchor extends AbstractConnectionAnchor {

    protected LineConnectionAnchor() {
    }

    /**
     * Constructs a LineConnectionAnchor with the given <i>owner</i> figure.
     * 
     * @param owner The owner figure
     */
    public LineConnectionAnchor(IFigure owner) {
        super(owner);
    }

    @Override
    public Point getLocation(Point reference) {
        AbstractPointListShape shape = (AbstractPointListShape)getOwner();
        Point pt = shape.getPoints().getMidpoint();
        getOwner().translateToAbsolute(pt); // Gotta do this thing!
        return pt;
    }
    
    /**
     * Returns <code>true</code> if the other anchor has the same owner and box.
     * 
     * @param obj the other anchor
     * @return <code>true</code> if equal
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LineConnectionAnchor) {
            LineConnectionAnchor other = (LineConnectionAnchor)obj;
            return other.getOwner() == getOwner();
        }
        return false;
    }

    /**
     * The owning figure's hashcode is used since equality is approximately
     * based on the owner.
     * 
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        if(getOwner() != null) {
            return getOwner().hashCode();
        }
        else return super.hashCode();
    }

}
