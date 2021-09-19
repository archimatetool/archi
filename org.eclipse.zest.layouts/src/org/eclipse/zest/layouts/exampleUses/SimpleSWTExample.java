/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.layouts.exampleUses;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
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
 * @author Ian Bull
 * A simple example of using layout algorithms with a SWT application.
 */
@SuppressWarnings({"rawtypes", "unchecked", "nls"})
public class SimpleSWTExample {

    private static final Color BLACK = new Color(0, 0, 0);
    private static final Color NODE_NORMAL_COLOR = new Color(225, 225, 255);
    private static final Color NODE_SELECTED_COLOR = new Color(255, 125, 125);
    private static final Color NODE_ADJACENT_COLOR = new Color(255, 200, 125);
    private static final Color BORDER_NORMAL_COLOR = new Color(0, 0, 0);
    private static final Color BORDER_SELECTED_COLOR = new Color(255, 0, 0);
    private static final Color BORDER_ADJACENT_COLOR = new Color(255, 128, 0);
    private static final Color RELATIONSHIP_COLOR = new Color(192, 192, 225);
    private static final Color RELATIONSHIP_HIGHLIGHT_COLOR = new Color(255, 200, 125);

    private static final String[] NAMES = new String[] { "Peggy", "Rob", "Ian", "Chris", "Simon", "Wendy", "Steven", "Kim", "Neil", "Dave", "John", "Suzanne", "Jody", "Casey", "Bjorn", "Peter", "Erin", "Lisa", "Jennie", "Liz", "Bert", "Ryan", "Nick", "Amy", "Lee", "Me", "You", "Max", "NCI", "OWL",
            "Ed", "Jamie", "Protege", "Matt", "Bryan", "Pete", "Sam", "Bob", "Katie", "Bill", "Josh", "Davor", "Ken", "Jacob", "Norm", "Jim", "Maya", "Jill", "Kit", "Jo", "Joe", "Andrew", "Charles", "Pat", "Patrick", "Jeremy", "Mike", "Michael", "Patricia", "Marg", "Terry", "Emily", "Ben", "Holly",
            "Joanna", "Joanne", "Evan", "Tom", "Dan", "Eric", "Corey", "Meghan", "Kevin", "Nina", "Ron", "Daniel", "David", "Jeff", "Nathan", "Amanda", "Phil", "Tricia", "Steph", "Stewart", "Stuart", "Bull", "Lintern", "Callendar", "Thompson", "Rigby", "Adam", "Judith", "Cynthia", "Sarah", "Sara",
            "Roger", "Andy", "Kris", "Mark", "Shane", "Spence", "Ivy", "Ivanna", "Julie", "Justin", "Emile", "Toby", "Robin", "Rich", "Kathy", "Cathy", "Nicky", "Ricky", "Danny", "Anne", "Ann", "Jen", "Robert", "Calvin", "Alvin", "Scott", "Kumar" };

    //private static final boolean RENDER_HIGH_QUALITY = true;

    private static final int INITIAL_PANEL_WIDTH = 800;
    private static final int INITIAL_PANEL_HEIGHT = 600;
    private static final double INITIAL_NODE_WIDTH = 20;
    private static final double INITIAL_NODE_HEIGHT = 15;

    protected static ArrayList algorithms = new ArrayList();
    {
        algorithms.add(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
        algorithms.add(new TreeLayoutAlgorithm(LayoutStyles.NONE));
        algorithms.add(new HorizontalTreeLayoutAlgorithm(LayoutStyles.NONE));
        algorithms.add(new RadialLayoutAlgorithm(LayoutStyles.NONE));
        algorithms.add(new GridLayoutAlgorithm(LayoutStyles.NONE));
        algorithms.add(new HorizontalLayoutAlgorithm(LayoutStyles.NONE));
        algorithms.add(new VerticalLayoutAlgorithm(LayoutStyles.NONE));
    }

    protected static ArrayList algorithmNames = new ArrayList();
    {
        algorithmNames.add("Spring");
        algorithmNames.add("Fade");
        algorithmNames.add("Tree - V");
        algorithmNames.add("Tree - H");
        algorithmNames.add("Radial");
        algorithmNames.add("Grid");
        algorithmNames.add("Horizontal");
        algorithmNames.add("Vertical");
    }

    protected static ArrayList algorithmAnimates = new ArrayList();
    {
        algorithmAnimates.add(Boolean.TRUE);
        algorithmAnimates.add(Boolean.TRUE);
        algorithmAnimates.add(Boolean.FALSE);
        algorithmAnimates.add(Boolean.FALSE);
        algorithmAnimates.add(Boolean.FALSE);
        algorithmAnimates.add(Boolean.FALSE);
        algorithmAnimates.add(Boolean.FALSE);
        algorithmAnimates.add(Boolean.FALSE);
    }

    //private long updateGUICount = 0;
    private boolean animate = true;
    private static boolean continuous = false;
    private static boolean asynchronously = false;

    private Shell mainShell;
    private Composite mainComposite;
    private List entities;
    private List relationships;

    private ToolBar toolBar;
    private Label lblProgress;

    private LayoutAlgorithm currentLayoutAlgorithm;
    protected SimpleNode selectedEntity;
    protected SimpleNode hoverEntity;

    protected Point mouseDownPoint;
    protected Point selectedEntityPositionAtMouseDown;
    private long idCount = 0;

    public SimpleSWTExample(Display display) {
        mainShell = new Shell(display);
        mainShell.addControlListener(new ControlListener() {
            @Override
            public void controlMoved(ControlEvent e) {
            }

            @Override
            public void controlResized(ControlEvent e) {
                mainShell.layout(true);
            }
        });
        GridLayout gridLayout = new GridLayout(1, true);
        mainShell.setLayout(gridLayout);
        GridData toolbarGridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER, GridData.VERTICAL_ALIGN_BEGINNING, true, true);
        toolBar = new ToolBar(mainShell, SWT.HORIZONTAL);
        toolBar.setLayoutData(toolbarGridData);
        toolBar.setLayout(new FillLayout(SWT.HORIZONTAL));

        GridData progressGridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER, GridData.VERTICAL_ALIGN_END, true, false);
        progressGridData.widthHint = 300;
        lblProgress = new Label(mainShell, SWT.NONE);
        lblProgress.setLayoutData(progressGridData);
        lblProgress.setText("Progress: ");

        for (int i = 0; i < algorithms.size(); i++) {
            final LayoutAlgorithm algorithm = (LayoutAlgorithm) algorithms.get(i);
            String algorithmName = (String) algorithmNames.get(i);
            final boolean algorithmAnimate = ((Boolean) algorithmAnimates.get(i)).booleanValue();
            ToolItem algorithmButton = new ToolItem(toolBar, SWT.PUSH);
            algorithmButton.setText(algorithmName);

            new ToolItem(toolBar, SWT.SEPARATOR);

            algorithmButton.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                    currentLayoutAlgorithm = algorithm;
                    algorithm.setEntityAspectRatio((double) mainComposite.getClientArea().width / (double) mainComposite.getClientArea().height);
                    animate = algorithmAnimate;
                    performLayout(false);
                }

                @Override
                public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                }
            });
        }

        ToolItem redrawButton = new ToolItem(toolBar, SWT.PUSH);
        redrawButton.setText("Redraw");
        redrawButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mainComposite.redraw();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        ToolItem stopButton = new ToolItem(toolBar, SWT.PUSH);
        stopButton.setText("Stop");
        stopButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                currentLayoutAlgorithm.stop();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        ToolItem continuousButton = new ToolItem(toolBar, SWT.CHECK);
        continuousButton.setText("Continuous");
        continuousButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setContinuous();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        ToolItem asynchronousButton = new ToolItem(toolBar, SWT.CHECK);
        asynchronousButton.setText("Asynchronous");
        asynchronousButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setAsynchronously();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        createMainPanel();
        SimpleNode.setNodeColors(NODE_NORMAL_COLOR, BORDER_NORMAL_COLOR, NODE_SELECTED_COLOR, NODE_ADJACENT_COLOR, BORDER_SELECTED_COLOR, BORDER_ADJACENT_COLOR);
        SimpleRelationship.setDefaultColor(RELATIONSHIP_COLOR);
        SimpleRelationship.setDefaultHighlightColor(RELATIONSHIP_HIGHLIGHT_COLOR);
        createTreeGraph(4, 3, false);
        mainShell.pack();
        //mainShell.setSize(INITIAL_PANEL_WIDTH + 100, INITIAL_PANEL_HEIGHT + 200);
    }

    public void setAsynchronously() {
        if (asynchronously) {
            asynchronously = false;
        } else {
            asynchronously = true;
        }

    }

    public void setContinuous() {
        if (continuous) {
            continuous = false;
        } else {
            continuous = true;
        }
    }

    IProgressMonitor progressMonitor = null;
    ProgressMonitorDialog pmd = null;
    boolean GUI_UPDATING = false;

    private void performLayout(boolean placeRandomly) {

        if (!continuous) {
        }

        if (currentLayoutAlgorithm.isRunning()) {
            throw new RuntimeException("Layout is already running");
        }
        if (placeRandomly) {
            placeRandomly();
        }
        ProgressListener progressListener = new ProgressListener() {

            int lastStep = 0;

            class progressSync implements Runnable {

                public static final int PROGRESS_UPDATED = 1;
                public static final int PROGRESS_STARTED = 2;
                public static final int PROGRESS_ENDED = 3;
                public static final int UPDATE_GUI = 4;

                private int progressState = -1;
                private ProgressEvent e;

                public progressSync(int progressState, final ProgressEvent e) {
                    this.progressState = progressState;
                    this.e = e;

                }

                @Override
                public void run() {

                    switch (progressState) {
                    case PROGRESS_STARTED:
                        if (!continuous) {
                            pmd = new ProgressMonitorDialog(getShell());
                            progressMonitor = pmd.getProgressMonitor();
                            pmd.open();
                            progressMonitor.beginTask("Layout Running...", e.getTotalNumberOfSteps());
                        }
                        break;

                    case PROGRESS_UPDATED:
                        if (!continuous) {
                            progressMonitor.worked(e.getStepsCompleted() - lastStep);
                            lastStep = e.getStepsCompleted();
                        }
                        break;

                    case PROGRESS_ENDED:
                        if (!continuous) {
                            progressMonitor.done();
                            pmd.close();
                        }
                        updateGUI();
                        mainShell.redraw();
                        break;
                    case UPDATE_GUI:
                        updateGUI();
                        GUI_UPDATING = false;
                        break;
                    }
                    mainComposite.redraw();

                }

            }

            @Override
            public void progressUpdated(final ProgressEvent e) {
                if (asynchronously) {
                    if (!mainComposite.isDisposed()) {
                        Display.getDefault().asyncExec(new progressSync(progressSync.PROGRESS_UPDATED, e));
                        if (!GUI_UPDATING) {
                            GUI_UPDATING = true;
                            Display.getDefault().asyncExec(new progressSync(progressSync.UPDATE_GUI, e));
                        }
                    }
                } else {
                    if (!mainComposite.isDisposed()) {
                        new progressSync(progressSync.PROGRESS_UPDATED, e).run();
                    }
                }

            }

            @Override
            public void progressStarted(ProgressEvent e) {
                if (asynchronously) {
                    if (!mainComposite.isDisposed()) {
                        Display.getDefault().asyncExec(new progressSync(progressSync.PROGRESS_STARTED, e));
                    }
                } else {
                    if (!mainComposite.isDisposed()) {
                        new progressSync(progressSync.PROGRESS_STARTED, e).run();
                    }
                }

            }

            @Override
            public void progressEnded(ProgressEvent e) {
                if (asynchronously) {
                    if (!mainComposite.isDisposed()) {
                        Display.getDefault().asyncExec(new progressSync(progressSync.PROGRESS_ENDED, e));
                    }
                } else {
                    if (!mainComposite.isDisposed()) {
                        new progressSync(progressSync.PROGRESS_ENDED, e).run();
                    }
                }
                currentLayoutAlgorithm.removeProgressListener(this);
                Display.getDefault().asyncExec(new progressSync(progressSync.PROGRESS_UPDATED, e));
            }
        };
        currentLayoutAlgorithm.addProgressListener(progressListener);

        try {
            LayoutEntity[] layoutEntities = new LayoutEntity[entities.size()];
            entities.toArray(layoutEntities);
            LayoutRelationship[] layoutRelationships = new LayoutRelationship[relationships.size()];
            relationships.toArray(layoutRelationships);
            currentLayoutAlgorithm.applyLayout(layoutEntities, layoutRelationships, 0, 0, mainComposite.getClientArea().width - 30, mainComposite.getClientArea().height - 17, asynchronously, continuous);
            if (!animate) {
                updateGUI();
            }
        } catch (InvalidLayoutConfiguration e) {
            e.printStackTrace();
        }
    }

    private Shell getShell() {
        return mainShell;
    }

    private void createMainPanel() {
        mainComposite = new Canvas(mainShell, SWT.NO_BACKGROUND);
        GridData mainGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_FILL, true, true);
        mainGridData.widthHint = INITIAL_PANEL_WIDTH;
        mainGridData.heightHint = INITIAL_PANEL_HEIGHT;
        mainComposite.setLayoutData(mainGridData);
        mainComposite.addPaintListener(new GraphPaintListener());

        mainComposite.setBackground(new Color(255, 255, 255));
        mainComposite.setLayout(null);

        mainComposite.addMouseMoveListener(new MouseMoveListener() {

            @Override
            public void mouseMove(MouseEvent e) {

                if (selectedEntity == null) {
                    // Nothing selected, lets use a mouse hover
                    SimpleNode oldEntity = hoverEntity;
                    hoverEntity = null;

                    for (Iterator iter = entities.iterator(); iter.hasNext() && selectedEntity == null;) {
                        SimpleNode entity = (SimpleNode) iter.next();
                        double x = entity.getX();
                        double y = entity.getY();
                        double w = entity.getWidth();
                        double h = entity.getHeight();
                        Rectangle rect = new Rectangle((int) x, (int) y, (int) w, (int) h);
                        if (rect.contains(e.x, e.y)) {
                            hoverEntity = entity;
                            hoverEntity.ignoreInLayout(true);
                            hoverEntity.setSelected();
                            break;
                        }
                    }
                    if (oldEntity != null && oldEntity != hoverEntity) {
                        oldEntity.ignoreInLayout(false);
                        oldEntity.setUnSelected();
                    }
                }

            }

        });
        mainComposite.addMouseListener(new MouseListener() {

            @Override
            public void mouseDoubleClick(MouseEvent e) {
            }

            @Override
            public void mouseDown(MouseEvent e) {
                selectedEntity = null;
                hoverEntity = null;
                for (Iterator iter = entities.iterator(); iter.hasNext() && selectedEntity == null;) {
                    SimpleNode entity = (SimpleNode) iter.next();
                    double x = entity.getX();
                    double y = entity.getY();
                    double w = entity.getWidth();
                    double h = entity.getHeight();
                    Rectangle rect = new Rectangle((int) x, (int) y, (int) w, (int) h);
                    if (rect.contains(e.x, e.y)) {
                        selectedEntity = entity;
                    }
                }
                if (selectedEntity != null) {
                    mouseDownPoint = new Point(e.x, e.y);
                    selectedEntityPositionAtMouseDown = new Point((int) selectedEntity.getX(), (int) selectedEntity.getY());
                    selectedEntity.ignoreInLayout(true);
                    selectedEntity.setSelected();
                } else {
                    mouseDownPoint = null;
                    selectedEntityPositionAtMouseDown = null;
                }
            }

            @Override
            public void mouseUp(MouseEvent e) {
                if (selectedEntity != null) {
                    selectedEntity.ignoreInLayout(false);
                    selectedEntity.setUnSelected();
                    List relatedNodes = selectedEntity.getRelatedEntities();
                    for (Iterator iter = relatedNodes.iterator(); iter.hasNext();) {
                        SimpleNode element = (SimpleNode) iter.next();
                        element.setUnSelected();
                    }
                    SimpleRelationship[] rels = selectedEntity.getRelationships();
                    for (int i = 0; i < rels.length; i++) {
                        rels[i].resetLineWidth();
                    }
                }
                selectedEntity = null;
                mouseDownPoint = null;
                selectedEntityPositionAtMouseDown = null;
            }
        });

        // stops the algorithm when the window is closed
        mainComposite.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                if (currentLayoutAlgorithm != null) {
                    currentLayoutAlgorithm.stop();
                }
            }
        });

        mainComposite.addMouseMoveListener(new MouseMoveListener() {

            @Override
            public void mouseMove(MouseEvent e) {
                if (selectedEntity != null && mouseDownPoint != null) {
                    double dx = e.x - mouseDownPoint.x;
                    double dy = e.y - mouseDownPoint.y;

                    selectedEntity.setLocation(selectedEntityPositionAtMouseDown.x + dx, selectedEntityPositionAtMouseDown.y + dy);
                    mainComposite.redraw();
                }
            }
        });
    }

    static int lastUpdateCall = 0;

    /**
     * 
     * @param maxLevels Max number of levels wanted in tree    
     * @param maxChildren Max number of children for each node in the tree
     * @param random Whether or not to pick random number of levels (from 1 to maxLevels) and 
     * random number of children (from 1 to maxChildren)
     */
    private void createTreeGraph(int maxLevels, int maxChildren, boolean random) {
        entities = new ArrayList();
        relationships = new ArrayList();

        // ccallendar - testing out having 2 roots 
        SimpleNode root = createSimpleNode(getNextID());
        entities.add(root);
        SimpleNode root2 = createSimpleNode(getNextID());
        entities.add(root2);
        // end

        SimpleNode currentParent = createSimpleNode(getNextID());
        entities.add(currentParent);

        // ccallendar - adding relationships from the parent to the 2 roots
        SimpleRelationship rel = new SimpleRelationship(root, currentParent, false);
        root.addRelationship(rel);
        currentParent.addRelationship(rel);
        relationships.add(rel);
        rel = new SimpleRelationship(root2, currentParent, false);
        root2.addRelationship(rel);
        currentParent.addRelationship(rel);
        relationships.add(rel);
        // end 

        int levels = random ? (int) (Math.random() * maxLevels + 1) : maxLevels;
        createTreeGraphRecursive(currentParent, maxChildren, levels, 1, random);
    }

    private void createTreeGraphRecursive(SimpleNode currentParentNode, int maxChildren, int maxLevel, int level, boolean random) {
        if (level > maxLevel) {
            return;
        }

        int numChildren = random ? (int) (Math.random() * maxChildren + 1) : maxChildren;
        for (int child = 0; child < numChildren; child++) {
            SimpleNode childNode = createSimpleNode(getNextID());
            entities.add(childNode);
            SimpleRelationship rel = new SimpleRelationship(currentParentNode, childNode, false);
            childNode.addRelationship(rel);
            currentParentNode.addRelationship(rel);
            relationships.add(rel);
            SimpleRelationship.setDefaultSize(2);
            createTreeGraphRecursive(childNode, maxChildren, maxLevel, level + 1, random);
        }
    }

    private int repeats = 0;

    /**
     * Gets the next name from the names list.
     * Once all the names have been used up the names are
     * repeated with a '1' after the name.
     * @return String name
     */
    private String getNextID() {
        if (idCount >= NAMES.length) {
            idCount = 0;
            repeats++;
        }
        String id = NAMES[(int) idCount];
        if (repeats > 0) {
            id += "_" + repeats;
        }
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
        }
    }

    /**
     * Creates a SimpleNode
     * @param name
     * @return SimpleNode
     */
    private SimpleNode createSimpleNode(String name) {
        SimpleNode simpleNode = new SimpleNode(name);
        int w = name.length() * 8; // an initial approximation of the width
        simpleNode.setSizeInLayout(Math.max(w, INITIAL_NODE_WIDTH), INITIAL_NODE_HEIGHT);
        return simpleNode;
    }

    private void updateGUI() {
        if (!mainComposite.isDisposed()) {
            mainComposite.redraw();
            //mainComposite.update();
        }
    }

    static Display display = null;

    public static void main(String[] args) {
        display = Display.getDefault();
        SimpleSWTExample simpleSWTExample = new SimpleSWTExample(display);
        Shell shell = simpleSWTExample.getShell();
        //shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    /**
     * Implements a paint listener to display nodes and edges  
     */
    private class GraphPaintListener implements PaintListener {

        long lastPaint;

        @Override
        public void paintControl(PaintEvent e) {
            Date date = new Date();
            long currentTime = date.getTime();
            if (currentTime - lastPaint < 40) {
                return;
            } else {
                lastPaint = currentTime;
            }
            if (Display.getDefault() == null || e.width == 0 || e.height == 0) {
                return;
            }
            long startTime = date.getTime();

            // do a bit of our own double-buffering to stop flickering
            Image imageBuffer;

            try {
                imageBuffer = new Image(Display.getDefault(), e.width, e.height);
            } catch (SWTError noMoreHandles) {
                imageBuffer = null;
                noMoreHandles.printStackTrace();
                return;
            } catch (IllegalArgumentException tooBig) {
                imageBuffer = null;
                tooBig.printStackTrace();
                return;
            }

            GC gcBuffer = new GC(imageBuffer);

            // paint the relationships 
            for (Iterator iter = relationships.iterator(); iter.hasNext();) {
                SimpleRelationship rel = (SimpleRelationship) iter.next();
                SimpleNode src = (SimpleNode) rel.getSourceInLayout();
                SimpleNode dest = (SimpleNode) rel.getDestinationInLayout();

                // highlight the adjacent nodes if one of the nodes is selected
                if (src.equals(selectedEntity)) {
                    dest.setAdjacent();
                    rel.setSelected();
                } else if (dest.equals(selectedEntity)) {
                    src.setAdjacent();
                    rel.setSelected();
                } else {
                    rel.setUnSelected();
                }

                // Add bend points if required
                if ((rel).getBendPoints() != null && (rel).getBendPoints().length > 0) {
                    src = drawBendPoints(rel, gcBuffer); // change source to last bendpoint
                }

                double srcX = src.getX() + src.getWidth() / 2.0;
                double srcY = src.getY() + src.getHeight() / 2.0;
                double destX = dest.getX() + dest.getWidth() / 2.0;
                double destY = dest.getY() + dest.getHeight() / 2.0;
                drawEdge(srcX, srcY, destX, destY, rel, gcBuffer);

            }

            // paint the nodes
            for (Iterator iter = entities.iterator(); iter.hasNext();) {
                SimpleNode entity = (SimpleNode) iter.next();

                String name = entity.toString();
                Point textSize = gcBuffer.stringExtent(name);
                int entityX = (int) entity.getX();
                int entityY = (int) entity.getY();
                //TODO: What about resize from the layout algorithm
                int entityWidth = Math.max((int) entity.getWidth(), textSize.x + 8);
                int entityHeight = Math.max((int) entity.getHeight(), textSize.y + 2);

                gcBuffer.setBackground((Color) entity.getColor());
                gcBuffer.fillRoundRectangle(entityX, entityY, entityWidth, entityHeight, 8, 8);

                // position the text in the middle of the node
                int x = (int) (entityX + (entityWidth / 2.0)) - (textSize.x / 2);
                gcBuffer.setForeground(BLACK);
                gcBuffer.drawString(name, x, entityY);
                gcBuffer.setForeground((Color) entity.getBorderColor());
                gcBuffer.setLineWidth(entity.getBorderWidth());
                gcBuffer.drawRoundRectangle(entityX, entityY, entityWidth, entityHeight, 8, 8);
            }

            e.gc.drawImage(imageBuffer, 0, 0);
            imageBuffer.dispose();
            gcBuffer.dispose();

            long time = date.getTime() - startTime;
            if (time > 200) {
            }
        }

        /**
         * Draw an edge
         * @param gcBuffer
         * @param srcX
         * @param srcY
         * @param destX
         * @param destY
         * @param rel
         */
        private void drawEdge(double srcX, double srcY, double destX, double destY, SimpleRelationship rel, GC gcBuffer) {
            gcBuffer.setForeground((Color) rel.getColor());
            gcBuffer.setLineWidth(rel.getLineWidth());
            gcBuffer.drawLine((int) srcX, (int) srcY, (int) destX, (int) destY);
        }

        /**
         * Draws a set of lines between bendpoints
         * TODO - This does not always draw outside the node.
         * @param relationship
         * @param bendNodes
         * @param bendEdges
         * @return the last bendpoint entity or null if there are no bendpoints
         */
        private SimpleNode drawBendPoints(SimpleRelationship rel, GC gcBuffer) {
            final String DUMMY_TITLE = "dummy";
            LayoutBendPoint[] bendPoints = (rel).getBendPoints();
            LayoutBendPoint bp;
            SimpleNode startEntity = (SimpleNode) rel.getSourceInLayout();
            SimpleNode destEntity = null;

            double srcX = startEntity.getX() + startEntity.getWidth() / 2.0;
            double srcY = startEntity.getY() + startEntity.getHeight() / 2.0;
            for (int i = 1; i < bendPoints.length - 1; i++) {
                bp = bendPoints[i];
                destEntity = new SimpleNode(DUMMY_TITLE, bp.getX(), bp.getY(), 0.01, 0.01);
                drawEdge(srcX, srcY, bp.getX(), bp.getY(), rel, gcBuffer);
                startEntity = destEntity;
                srcX = startEntity.getX();
                srcY = startEntity.getY();
            }
            return destEntity;
        }

    }

}
