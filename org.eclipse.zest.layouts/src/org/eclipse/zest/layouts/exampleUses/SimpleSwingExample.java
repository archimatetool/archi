/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts.exampleUses;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.eclipse.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutBendPoint;
import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.LayoutRelationship;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.VerticalLayoutAlgorithm;
import org.eclipse.zest.layouts.exampleStructures.SimpleNode;
import org.eclipse.zest.layouts.exampleStructures.SimpleRelationship;
import org.eclipse.zest.layouts.progress.ProgressEvent;
import org.eclipse.zest.layouts.progress.ProgressListener;

/**
 * @author Rob Lintern
 * @author Chris Bennett
 *
 * A simple example of using layout algorithms with a Swing application.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SimpleSwingExample {
    private static final Color NODE_NORMAL_COLOR = new Color(225, 225, 255);
    private static final Color NODE_SELECTED_COLOR = new Color(255, 125, 125);
    //private static final Color NODE_ADJACENT_COLOR = new Color (255, 200, 125); 
    private static final Color BORDER_NORMAL_COLOR = new Color(0, 0, 0);
    private static final Color BORDER_SELECTED_COLOR = new Color(255, 0, 0);
    //private static final Color BORDER_ADJACENT_COLOR = new Color (255, 128, 0);   
    private static final Stroke BORDER_NORMAL_STROKE = new BasicStroke(1.0f);
    private static final Stroke BORDER_SELECTED_STROKE = new BasicStroke(2.0f);
    private static final Color RELATIONSHIP_NORMAL_COLOR = Color.BLUE;
    //private static final Color RELATIONSHIP_HIGHLIGHT_COLOR = new Color (255, 200, 125); 

    public static SpringLayoutAlgorithm SPRING = new SpringLayoutAlgorithm(LayoutStyles.NONE);
    public static TreeLayoutAlgorithm TREE_VERT = new TreeLayoutAlgorithm(LayoutStyles.NONE);
    public static HorizontalTreeLayoutAlgorithm TREE_HORIZ = new HorizontalTreeLayoutAlgorithm(LayoutStyles.NONE);
    public static RadialLayoutAlgorithm RADIAL = new RadialLayoutAlgorithm(LayoutStyles.NONE);
    public static GridLayoutAlgorithm GRID = new GridLayoutAlgorithm(LayoutStyles.NONE);
    public static HorizontalLayoutAlgorithm HORIZ = new HorizontalLayoutAlgorithm(LayoutStyles.NONE);
    public static VerticalLayoutAlgorithm VERT = new VerticalLayoutAlgorithm(LayoutStyles.NONE);

    private List algorithms = new ArrayList();
    private List algorithmNames = new ArrayList();

    private static final int INITIAL_PANEL_WIDTH = 700;
    private static final int INITIAL_PANEL_HEIGHT = 500;

    private static final boolean RENDER_HIGH_QUALITY = true;

    private static final double INITIAL_NODE_WIDTH = 20;
    private static final double INITIAL_NODE_HEIGHT = 20;
    private static final int ARROW_HALF_WIDTH = 4;
    private static final int ARROW_HALF_HEIGHT = 6;
    private static final Shape ARROW_SHAPE = new Polygon(new int[] { -ARROW_HALF_HEIGHT, ARROW_HALF_HEIGHT, -ARROW_HALF_HEIGHT }, new int[] { -ARROW_HALF_WIDTH, 0, ARROW_HALF_WIDTH }, 3);
    private static final Stroke ARROW_BORDER_STROKE = new BasicStroke(0.5f);
    private static final Color ARROW_HEAD_FILL_COLOR = new Color(125, 255, 125);
    private static final Color ARROW_HEAD_BORDER_COLOR = Color.BLACK;

    public static final String DEFAULT_NODE_SHAPE = "oval"; //$NON-NLS-1$

    private long updateGUICount = 0;

    private JFrame mainFrame;
    private JPanel mainPanel;
    private List entities;
    private List relationships;
    private JToolBar toolBar;
    private JLabel lblProgress;
    private JToggleButton btnContinuous;
    private JToggleButton btnAsynchronous;
    private JButton btnStop;

    private LayoutAlgorithm currentLayoutAlgorithm;
    protected String currentLayoutAlgorithmName;
    protected SimpleNode selectedEntity;
    protected Point mouseDownPoint;
    protected Point selectedEntityPositionAtMouseDown;
    private long idCount;
    protected String currentNodeShape = DEFAULT_NODE_SHAPE; // e.g., oval, rectangle

    public SimpleSwingExample() {

    }

    protected void addAlgorithm(LayoutAlgorithm algorithm, String name, boolean animate) {
        algorithms.add(algorithm);
        algorithmNames.add(name);
    }

    public void start() {

        mainFrame = new JFrame("Simple Swing Layout Example"); //$NON-NLS-1$
        toolBar = new JToolBar();
        mainFrame.getContentPane().setLayout(new BorderLayout());
        mainFrame.getContentPane().add(toolBar, BorderLayout.NORTH);
        lblProgress = new JLabel("Progress: "); //$NON-NLS-1$
        mainFrame.getContentPane().add(lblProgress, BorderLayout.SOUTH);

        createMainPanel();
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
                mainFrame.dispose();

            }
        });

        btnContinuous = new JToggleButton("continuous", false); //$NON-NLS-1$
        btnAsynchronous = new JToggleButton("asynchronous", false); //$NON-NLS-1$

        toolBar.add(btnContinuous);
        toolBar.add(btnAsynchronous);

        btnStop = new JButton("Stop"); //$NON-NLS-1$
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });
        toolBar.add(btnStop);

        JButton btnCreateGraph = new JButton("New graph"); //$NON-NLS-1$
        btnCreateGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
                createGraph(true);
            }
        });
        toolBar.add(btnCreateGraph);
        JButton btnCreateTree = new JButton("New tree"); //$NON-NLS-1$
        btnCreateTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
                createGraph(false);
            }
        });
        toolBar.add(btnCreateTree);

        createGraph(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation((int) (screenSize.getWidth() - INITIAL_PANEL_WIDTH) / 2, (int) (screenSize.getHeight() - INITIAL_PANEL_HEIGHT) / 2);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.repaint();

        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    SPRING = new SpringLayoutAlgorithm(LayoutStyles.NONE);
                    TREE_VERT = new TreeLayoutAlgorithm(LayoutStyles.NONE);
                    TREE_HORIZ = new HorizontalTreeLayoutAlgorithm(LayoutStyles.NONE);
                    RADIAL = new RadialLayoutAlgorithm(LayoutStyles.NONE);
                    GRID = new GridLayoutAlgorithm(LayoutStyles.NONE);
                    HORIZ = new HorizontalLayoutAlgorithm(LayoutStyles.NONE);
                    VERT = new VerticalLayoutAlgorithm(LayoutStyles.NONE);

                    SPRING.setIterations(1000);
                    // initialize layouts
                    TREE_VERT.setComparator(new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            if (o1 instanceof Comparable && o2 instanceof Comparable) {
                                return ((Comparable) o1).compareTo(o2);
                            }
                            return 0;
                        }

                    });
                    GRID.setRowPadding(20);
                    addAlgorithm(SPRING, "Spring", false); //$NON-NLS-1$
                    addAlgorithm(TREE_VERT, "Tree-V", false); //$NON-NLS-1$
                    addAlgorithm(TREE_HORIZ, "Tree-H", false); //$NON-NLS-1$
                    addAlgorithm(RADIAL, "Radial", false); //$NON-NLS-1$
                    addAlgorithm(GRID, "Grid", false); //$NON-NLS-1$
                    addAlgorithm(HORIZ, "Horiz", false); //$NON-NLS-1$
                    addAlgorithm(VERT, "Vert", false); //$NON-NLS-1$

                    for (int i = 0; i < algorithms.size(); i++) {
                        final LayoutAlgorithm algorithm = (LayoutAlgorithm) algorithms.get(i);
                        final String algorithmName = (String) algorithmNames.get(i);
                        //final boolean algorithmAnimate = ((Boolean)algorithmAnimates.get(i)).booleanValue();
                        JButton algorithmButton = new JButton(algorithmName);
                        algorithmButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                currentLayoutAlgorithm = algorithm;
                                currentLayoutAlgorithmName = algorithmName;
                                algorithm.setEntityAspectRatio((double) mainPanel.getWidth() / (double) mainPanel.getHeight());
                                //animate = algorithmAnimate;
                                performLayout();
                            }
                        });
                        toolBar.add(algorithmButton);
                    }
                }
            });
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    private void stop() {
        if (currentLayoutAlgorithm != null && currentLayoutAlgorithm.isRunning()) {
            currentLayoutAlgorithm.stop();
        }
    }

    protected void performLayout() {
        stop();
        final Cursor cursor = mainFrame.getCursor();
        updateGUICount = 0;
        placeRandomly();
        final boolean continuous = btnContinuous.isSelected();
        final boolean asynchronous = btnAsynchronous.isSelected();
        ProgressListener progressListener = new ProgressListener() {
            @Override
            public void progressUpdated(final ProgressEvent e) {
                //if (asynchronous) {
                updateGUI();
                //}
                lblProgress.setText("Progress: " + e.getStepsCompleted() + " of " + e.getTotalNumberOfSteps() + " completed ..."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                lblProgress.paintImmediately(0, 0, lblProgress.getWidth(), lblProgress.getHeight());
            }

            @Override
            public void progressStarted(ProgressEvent e) {
                if (!asynchronous) {
                    mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                }
                lblProgress.setText("Layout started ..."); //$NON-NLS-1$
                lblProgress.paintImmediately(0, 0, lblProgress.getWidth(), lblProgress.getHeight());
            }

            @Override
            public void progressEnded(ProgressEvent e) {
                lblProgress.setText("Layout completed ..."); //$NON-NLS-1$
                lblProgress.paintImmediately(0, 0, lblProgress.getWidth(), lblProgress.getHeight());
                currentLayoutAlgorithm.removeProgressListener(this);
                if (!asynchronous) {
                    mainFrame.setCursor(cursor);
                }
            }
        };

        currentLayoutAlgorithm.addProgressListener(progressListener);

        try {
            final LayoutEntity[] layoutEntities = new LayoutEntity[entities.size()];
            entities.toArray(layoutEntities);
            final LayoutRelationship[] layoutRelationships = new LayoutRelationship[relationships.size()];
            relationships.toArray(layoutRelationships);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        currentLayoutAlgorithm.applyLayout(layoutEntities, layoutRelationships, 0, 0, mainPanel.getWidth(), mainPanel.getHeight(), asynchronous, continuous);
                    } catch (InvalidLayoutConfiguration e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            });
            //if (!animate) {
            updateGUI();
            //}
            // reset
            currentNodeShape = DEFAULT_NODE_SHAPE;
        } catch (StackOverflowError e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void createMainPanel() {

        mainPanel = new MainPanel(); // see below for class definition
        mainPanel.setPreferredSize(new Dimension(INITIAL_PANEL_WIDTH, INITIAL_PANEL_HEIGHT));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(null);
        mainFrame.getContentPane().add(new JScrollPane(mainPanel), BorderLayout.CENTER);

        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedEntity = null;
                for (Iterator iter = entities.iterator(); iter.hasNext() && selectedEntity == null;) {
                    SimpleNode entity = (SimpleNode) iter.next();
                    double x = entity.getX();
                    double y = entity.getY();
                    double w = entity.getWidth();
                    double h = entity.getHeight();
                    Rectangle2D.Double rect = new Rectangle2D.Double(x, y, w, h);
                    if (rect.contains(e.getX(), e.getY())) {
                        selectedEntity = entity;
                    }
                }
                if (selectedEntity != null) {
                    mouseDownPoint = e.getPoint();
                    selectedEntityPositionAtMouseDown = new Point((int) selectedEntity.getX(), (int) selectedEntity.getY());
                } else {
                    mouseDownPoint = null;
                    selectedEntityPositionAtMouseDown = null;
                }
                updateGUI();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selectedEntity = null;
                mouseDownPoint = null;
                selectedEntityPositionAtMouseDown = null;
                updateGUI();
            }
        });

        mainPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //                if (selectedEntity != null) {
                //                    //TODO: Add mouse moving
                //                    //selectedEntity.setLocationInLayout(selectedEntityPositionAtMouseDown.x + dx, selectedEntityPositionAtMouseDown.y + dy);
                //                    updateGUI();
                //                } 
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
    }

    private void createGraph(boolean addNonTreeRels) {
        entities = new ArrayList();
        relationships = new ArrayList();
        selectedEntity = null;

        createTreeGraph(2, 4, 2, 5, true, true, addNonTreeRels);
        //        createCustomGraph();

        placeRandomly();
        mainPanel.repaint();
    }

    /**
     * 
     * @param maxLevels Max number of levels wanted in tree    
     * @param maxChildren Max number of children for each node in the tree
     * @param randomNumChildren Whether or not to pick random number of levels (from 1 to maxLevels) and 
     * random number of children (from 1 to maxChildren)
     */
    private void createTreeGraph(int minChildren, int maxChildren, int minLevels, int maxLevels, boolean randomNumChildren, boolean randomLevels, boolean addNonTreeRels) {
        LayoutEntity currentParent = createSimpleNode(getNextID());
        entities.add(currentParent);
        createTreeGraphRecursive(currentParent, minChildren, maxChildren, minLevels, maxLevels, 1, randomNumChildren, randomLevels, addNonTreeRels);
    }

    private void createTreeGraphRecursive(LayoutEntity currentParentNode, int minChildren, int maxChildren, int minLevel, int maxLevel, int level, boolean randomNumChildren, boolean randomLevels, boolean addNonTreeRels) {
        if (level > maxLevel) {
            return;
        }
        if (randomLevels) {
            if (level > minLevel) {
                double zeroToOne = Math.random();
                if (zeroToOne < 0.75) {
                    return;
                }
            }
        }
        int numChildren = randomNumChildren ? Math.max(minChildren, (int) (Math.random() * maxChildren + 1)) : maxChildren;
        for (int i = 0; i < numChildren; i++) {
            LayoutEntity newNode = createSimpleNode(getNextID());
            entities.add(newNode);
            if (addNonTreeRels && entities.size() % 5 == 0) {
                int index = (int) (Math.random() * entities.size());
                LayoutRelationship rel = new SimpleRelationship((LayoutEntity) entities.get(index), newNode, false);
                relationships.add(rel);
            }
            LayoutRelationship rel = new SimpleRelationship(currentParentNode, newNode, false);
            relationships.add(rel);
            createTreeGraphRecursive(newNode, minChildren, maxChildren, minLevel, maxLevel, level + 1, randomNumChildren, randomLevels, addNonTreeRels);
        }
    }

    /**
     * Call this from createGraph in place of createTreeGraph 
     * this for debugging and testing.
     */
    /*    private void createCustomGraph() {
     LayoutEntity A = createSimpleNode("1");
     LayoutEntity B = createSimpleNode("10");
     LayoutEntity _1 = createSimpleNode("100");
     entities.add(A);
     entities.add(B);
     entities.add(_1);
     relationships.add(new SimpleRelationship (A, B, false));
     relationships.add(new SimpleRelationship (A, _1, false));
     relationships.add(new SimpleRelationship (_1, A, false));
     }
     */
    private String getNextID() {
        String id = "" + idCount; //$NON-NLS-1$
        idCount++;
        return id;
    }

    /** Places nodes randomly on the screen **/
    private void placeRandomly() {
        for (Iterator iter = entities.iterator(); iter.hasNext();) {
            SimpleNode simpleNode = (SimpleNode) iter.next();
            double x = Math.random() * INITIAL_PANEL_WIDTH - INITIAL_NODE_WIDTH;
            double y = Math.random() * INITIAL_PANEL_HEIGHT - INITIAL_NODE_HEIGHT;
            simpleNode.setLocationInLayout(x, y);
            simpleNode.setSizeInLayout(INITIAL_NODE_WIDTH, INITIAL_NODE_HEIGHT);
        }
    }

    /**
     * Creates a SimpleNode
     * @param name
     * @return
     */
    private SimpleNode createSimpleNode(String name) {
        SimpleNode simpleNode = new SimpleNode(name);
        return simpleNode;
    }

    private void updateGUI() {
        updateGUICount++;
        if (updateGUICount > 0) {
            mainPanel.paintImmediately(0, 0, mainPanel.getWidth(), mainPanel.getHeight());
        }
    }

    private static Point2D.Double getEllipseIntersectionPoint(double theta, double ellipseWidth, double ellipseHeight) {
        double nhalfw = ellipseWidth / 2.0; // half elllipse width
        double nhalfh = ellipseHeight / 2.0; // half ellipse height
        double tanTheta = Math.tan(theta);

        double a = nhalfw;
        double b = nhalfh;
        double x = (a * b) / Math.sqrt(Math.pow(b, 2) + Math.pow(a, 2) * Math.pow(tanTheta, 2));
        if ((theta > Math.PI / 2.0 && theta < 1.5 * Math.PI) || (theta < -Math.PI / 2.0 && theta > -1.5 * Math.PI)) {
            x = -x;
        }
        double y = tanTheta * x;
        Point2D.Double p = new Point2D.Double(x, y);
        return p;
    }

    public static void main(String[] args) {
        (new SimpleSwingExample()).start();
    }

    /**
     * A JPanel that provides entity and relationship rendering
     * Instead of letting Swing paint all the JPanels for us, we will just do our own painting here
     */
    private class MainPanel extends JPanel {

        private static final long serialVersionUID = 1;

        @Override
        protected void paintChildren(Graphics g) {
            if (g instanceof Graphics2D && RENDER_HIGH_QUALITY) {
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            }

            // paint the nodes
            for (Iterator iter = entities.iterator(); iter.hasNext();) {
                paintEntity((SimpleNode) iter.next(), g);
            }

            // paint the relationships 
            for (Iterator iter = relationships.iterator(); iter.hasNext();) {
                paintRelationship((LayoutRelationship) iter.next(), g);
            }
        }

        private void paintEntity(SimpleNode entity, Graphics g) {
            boolean isSelected = selectedEntity != null && selectedEntity.equals(entity);
            g.setColor(isSelected ? NODE_SELECTED_COLOR : NODE_NORMAL_COLOR);
            if (currentNodeShape.equals("rectangle")) { //$NON-NLS-1$
                g.fillRect((int) entity.getX(), (int) entity.getY(), (int) entity.getWidth(), (int) entity.getHeight());
            } else { // default 
                g.fillOval((int) entity.getX(), (int) entity.getY(), (int) entity.getWidth(), (int) entity.getHeight());
            }
            g.setColor(isSelected ? BORDER_SELECTED_COLOR : BORDER_NORMAL_COLOR);
            String name = entity.toString();
            Rectangle2D nameBounds = g.getFontMetrics().getStringBounds(name, g);
            g.drawString(name, (int) (entity.getX() + entity.getWidth() / 2.0 - nameBounds.getWidth() / 2.0), (int) (entity.getY() + entity.getHeight() / 2.0 + nameBounds.getHeight() / 2.0));//- nameBounds.getHeight() - nameBounds.getY()));
            if (g instanceof Graphics2D) {
                ((Graphics2D) g).setStroke(isSelected ? BORDER_SELECTED_STROKE : BORDER_NORMAL_STROKE);
            }
            if (currentNodeShape.equals("rectangle")) { //$NON-NLS-1$
                g.drawRect((int) entity.getX(), (int) entity.getY(), (int) entity.getWidth(), (int) entity.getHeight());
            } else { // default
                g.drawOval((int) entity.getX(), (int) entity.getY(), (int) entity.getWidth(), (int) entity.getHeight());
            }
        }

        private void paintRelationship(LayoutRelationship rel, Graphics g) {

            SimpleNode src = (SimpleNode) rel.getSourceInLayout();
            SimpleNode dest = (SimpleNode) rel.getDestinationInLayout();

            // Add bend points if required
            if (((SimpleRelationship) rel).getBendPoints() != null && ((SimpleRelationship) rel).getBendPoints().length > 0) {
                drawBendPoints(rel, g);
            } else {
                double srcX = src.getX() + src.getWidth() / 2.0;
                double srcY = src.getY() + src.getHeight() / 2.0;
                double destX = dest.getX() + dest.getWidth() / 2.0;
                double destY = dest.getY() + dest.getHeight() / 2.0;
                double dx = getLength(srcX, destX);
                double dy = getLength(srcY, destY);
                double theta = Math.atan2(dy, dx);
                drawRelationship(src, dest, theta, srcX, srcY, destX, destY, g);

                // draw an arrow in the middle of the line
                drawArrow(theta, srcX, srcY, dx, dy, g);
            }
        }

        /**
         * Draw a line from the edge of the src node to the edge of the destination node 
         */
        private void drawRelationship(SimpleNode src, SimpleNode dest, double theta, double srcX, double srcY, double destX, double destY, Graphics g) {

            double reverseTheta = theta > 0.0d ? theta - Math.PI : theta + Math.PI;

            Point2D.Double srcIntersectionP = getEllipseIntersectionPoint(theta, src.getWidth(), src.getHeight());

            Point2D.Double destIntersectionP = getEllipseIntersectionPoint(reverseTheta, dest.getWidth(), dest.getHeight());

            drawRelationship(srcX + srcIntersectionP.getX(), srcY + srcIntersectionP.getY(), destX + destIntersectionP.getX(), destY + destIntersectionP.getY(), g);
        }

        /**
         * Draw a line from specified source to specified destination
         */
        private void drawRelationship(double srcX, double srcY, double destX, double destY, Graphics g) {
            g.setColor(RELATIONSHIP_NORMAL_COLOR);
            g.drawLine((int) srcX, (int) srcY, (int) destX, (int) destY);
        }

        private void drawArrow(double theta, double srcX, double srcY, double dx, double dy, Graphics g) {
            AffineTransform tx = new AffineTransform();
            double arrX = srcX + (dx) / 2.0;
            double arrY = srcY + (dy) / 2.0;
            tx.translate(arrX, arrY);
            tx.rotate(theta);
            Shape arrowTx = tx.createTransformedShape(ARROW_SHAPE);
            if (g instanceof Graphics2D) {
                g.setColor(ARROW_HEAD_FILL_COLOR);
                ((Graphics2D) g).fill(arrowTx);
                ((Graphics2D) g).setStroke(ARROW_BORDER_STROKE);
                g.setColor(ARROW_HEAD_BORDER_COLOR);
                ((Graphics2D) g).draw(arrowTx);
            }
        }

        /**
         * Get the length of a line ensuring it is not too small to render
         * @param start
         * @param end
         * @return
         */
        private double getLength(double start, double end) {
            double length = end - start;
            // make sure dx is not zero or too small
            if (length < 0.01 && length > -0.01) {
                if (length > 0) {
                    length = 0.01;
                } else if (length < 0) {
                    length = -0.01;
                }
            }
            return length;
        }

        /**
         * Draw a line from specified source to specified destination
         */
        private void drawCurvedRelationship(double srcX, double srcY, double control1X, double control1Y, double control2X, double control2Y, double destX, double destY, Graphics g) {
            GeneralPath shape = new GeneralPath();
            shape.moveTo((float) srcX, (float) srcY);
            shape.curveTo((float) control1X, (float) control1Y, (float) control2X, (float) control2Y, (float) destX, (float) destY);
            g.setColor(RELATIONSHIP_NORMAL_COLOR);
            ((Graphics2D) g).draw(shape);
        }

        /**
         * Draws a set of lines between bendpoints, returning the last bendpoint
         * drawn. Note that this assumes the first and last bendpoints are actually
         * the source node and destination node centre points. 
         * @param relationship
         * @param bendNodes
         * @param bendEdges
         * @return the last bendpoint entity or null if there are no bendpoints
         */
        private void drawBendPoints(LayoutRelationship rel, Graphics g) {
            final String DUMMY_TITLE = "dummy"; //$NON-NLS-1$
            LayoutBendPoint bp;

            SimpleNode startEntity = (SimpleNode) rel.getSourceInLayout();
            SimpleNode destEntity = (SimpleNode) rel.getDestinationInLayout();
            double srcX = startEntity.getX();
            double srcY = startEntity.getY();

            // Transform the bendpoints to this coordinate system
            LayoutBendPoint[] bendPoints = ((SimpleRelationship) rel).getBendPoints();

            srcX = bendPoints[1].getX();
            srcY = bendPoints[1].getY();
            int bpNum = 2;
            while (bpNum < bendPoints.length - 1) { // ignore first and last bendpoints (src and dest)
                int currentBpNum = bpNum;
                bp = bendPoints[bpNum];
                if (bp.getIsControlPoint()) {
                    if (bendPoints[bpNum + 1].getIsControlPoint()) {
                        destEntity = new SimpleNode(DUMMY_TITLE, bendPoints[bpNum + 2].getX(), bendPoints[bpNum + 2].getY(), 0.01, 0.01);
                        drawCurvedRelationship(srcX, srcY, bp.getX(), bp.getY(), bendPoints[bpNum + 1].getX(), bendPoints[bpNum + 1].getY(), bendPoints[bpNum + 2].getX(), bendPoints[bpNum + 2].getY(), g);
                        bpNum += 4;
                    } else {
                        destEntity = new SimpleNode(DUMMY_TITLE, bp.getX(), bp.getY(), 0.01, 0.01);
                    }
                } else {
                    drawRelationship(srcX, srcY, bp.getX(), bp.getY(), g);
                    bpNum++;
                    destEntity = new SimpleNode(DUMMY_TITLE, bp.getX(), bp.getY(), 0.01, 0.01);
                }
                startEntity = destEntity;
                if (currentBpNum == bendPoints.length - 2) { // last point
                    // draw an arrow in the middle of the line
                    double dx = getLength(srcX, destEntity.getX());
                    double dy = getLength(srcY, destEntity.getY());
                    double theta = Math.atan2(dy, dx);
                    drawArrow(theta, srcX, srcY, dx, dy, g);
                } else {
                    srcX = startEntity.getX();
                    srcY = startEntity.getY();
                }
            }

        }
    }
}
