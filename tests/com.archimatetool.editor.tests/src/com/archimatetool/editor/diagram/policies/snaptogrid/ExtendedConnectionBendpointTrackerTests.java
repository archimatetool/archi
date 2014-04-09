/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies.snaptogrid;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.draw2d.geometry.Point;
import org.junit.Before;
import org.junit.Test;

public class ExtendedConnectionBendpointTrackerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ExtendedConnectionBendpointTrackerTests.class);
    }
    
    private ExtendedConnectionBendpointTracker tracker;
    
    @Before
    public void runOnceBeforeEachTest() {
        tracker = new ExtendedConnectionBendpointTracker();
    }
    
    @Test
    public void testNearestSetSnapPoint() {
        Point pt = new Point(101, 139);
        tracker.setNearestSnapPoint(pt, 10);
        assertEquals(new Point(100, 140), pt);
        
        pt = new Point(101, 139);
        tracker.setNearestSnapPoint(pt, 12);
        assertEquals(new Point(96, 144), pt);
        
        pt = new Point(101, 139);
        tracker.setNearestSnapPoint(pt, 21);
        assertEquals(new Point(105, 147), pt);
        
        pt = new Point(37, 56);
        tracker.setNearestSnapPoint(pt, 9);
        assertEquals(new Point(36, 54), pt);
    }    
}