package com.archimatetool.editor.ui.components;

import java.lang.reflect.Method;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.internal.TrimUtil;

import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.ThemeUtils;


/**
 * The Heap Status control, which shows the heap usage statistics.
 * Rejigged from org.eclipse.ui.internal.HeapStatus
 * 
 * To show this set System Property to VM arguments as "-Dshowheap=true"
 */
@SuppressWarnings("restriction")
public class HeapStatusWidget extends Composite {
    
    /**
     * ContributionItem for a Toolbar
     */
    public static class HeapStatusWidgetToolBarContributionItem extends ContributionItem {
        ToolItem toolitem;
        
        @Override
        public void fill(ToolBar parent, int index) {
            toolitem = new ToolItem(parent, SWT.SEPARATOR, index);
            HeapStatusWidget hs = new HeapStatusWidget(parent);
            toolitem.setControl(hs);
            int width = hs.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x;
            toolitem.setWidth(width);
            hs.addDisposeListener(new DisposeListener() {
                @Override
                public void widgetDisposed(DisposeEvent e) {
                    toolitem.dispose();
                }
            });
        }
    };
    
    /**
     * ContributionItem for a Status Line
     */
    public static class HeapStatusWidgetStatusLineContributionItem extends StatusLineContributionItem {
        public HeapStatusWidgetStatusLineContributionItem() {
            super("heapStatus"); //$NON-NLS-1$
            // Bug - have to set text to something for the widget to show
            setText(" "); //$NON-NLS-1$
        }

        @Override
        public void fill(Composite parent) {
            super.fill(parent);
            new HeapStatusWidget(parent);
        }
    };

    
	private boolean armed;
	private Image gcImage;
	private Image disabledGcImage;
	private Color bgCol, usedMemCol, lowMemCol, freeMemCol, topLeftCol, bottomRightCol, sepCol, textCol, markCol, armCol;  
    private Canvas canvas;
	private int updateInterval = 500;
	private boolean showMax = true;
    private long totalMem;
    private long prevTotalMem = -1L;
    private long prevUsedMem = -1L;
    private boolean hasChanged;
    private long usedMem;
    private long mark = -1;
    // start with 12x12
	private Rectangle imgBounds = new Rectangle(0,0,12,12);
	private long maxMem = Long.MAX_VALUE;
	private boolean maxMemKnown;
	private float lowMemThreshold = 0.05f;
	private boolean showLowMemThreshold = true;
	private boolean updateTooltip = false;

	protected volatile boolean isInGC = false;

    private final Runnable timer = new Runnable() {
        @Override
        public void run() {
            if (!isDisposed()) {
                updateStats();
                if (hasChanged) {
                	if (updateTooltip) {
                		updateToolTip();
                	}
                    redraw();
                    hasChanged = false;
                }
                getDisplay().timerExec(updateInterval, this);
            }
        }
    };
    
    /**
     * Creates a new heap status control with the given parent, and using
     * the given preference store to obtain settings such as the refresh
     * interval.
     * 
     * @param parent the parent composite
     */
	public HeapStatusWidget(Composite parent) {
		super(parent, SWT.NONE);

		maxMem = getMaxMem();
		maxMemKnown = maxMem != Long.MAX_VALUE;

		canvas = new Canvas(this, SWT.NONE);
		canvas.setToolTipText("Run Garbage Collector"); //$NON-NLS-1$
        
		ImageDescriptor imageDesc = IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_TRASH);
		Display display = getDisplay();
		gcImage = imageDesc.createImage();
		if (gcImage != null) {
			imgBounds = gcImage.getBounds();
			disabledGcImage = new Image(display, gcImage, SWT.IMAGE_DISABLE);
		}
		usedMemCol = new Color(display, 160, 160, 160); // gray
		lowMemCol = new Color(display, 255, 70, 70);  // medium red 
		freeMemCol = new Color(display, 255, 190, 125);  // light orange
		sepCol = topLeftCol = armCol = usedMemCol;
		bgCol = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		bottomRightCol = display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
        markCol = display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
        textCol = ThemeUtils.isDarkTheme() ? ColorFactory.get(255, 255, 255) : markCol;
		
		createContextMenu();
		
        Listener listener = new Listener() {

            @Override
            public void handleEvent(Event event) {
                switch (event.type) {
                case SWT.Dispose:
                	doDispose();
                    break;
                case SWT.Resize:
                    Rectangle rect = getClientArea();
                    canvas.setBounds(rect.width - imgBounds.width - 1, 1, imgBounds.width, rect.height - 2);
                    break;
                case SWT.Paint:
                    if (event.widget == HeapStatusWidget.this) {
                    	paintComposite(event.gc);
                    }
                    else if (event.widget == canvas) {
                        paintButton(event.gc);
                    }
                    break;
                case SWT.MouseUp:
                    if (event.button == 1) {
						if (!isInGC) {
							arm(false);
							gc(); 
						}
                    }
                    break;
                case SWT.MouseDown:
                    if (event.button == 1) {
	                    if (event.widget == HeapStatusWidget.this) {
							setMark();
						} else if (event.widget == canvas) {
							if (!isInGC)
								arm(true);
						}
                    }
                    break;
                case SWT.MouseEnter:
                	HeapStatusWidget.this.updateTooltip = true;
                	updateToolTip();
                	break;
                case SWT.MouseExit:
                    if (event.widget == HeapStatusWidget.this) {
                    	HeapStatusWidget.this.updateTooltip = false;
					} else if (event.widget == canvas) {
						arm(false);
					}
                    break;
                }
            }

        };
        addListener(SWT.Dispose, listener);
        addListener(SWT.MouseDown, listener);
        addListener(SWT.Paint, listener);
        addListener(SWT.Resize, listener);
        addListener(SWT.MouseEnter, listener);
        addListener(SWT.MouseExit, listener);
        canvas.addListener(SWT.MouseDown, listener);
        canvas.addListener(SWT.MouseExit, listener);
        canvas.addListener(SWT.MouseUp, listener);
        canvas.addListener(SWT.Paint, listener);

		// make sure stats are updated before first paint
		updateStats();

        getDisplay().asyncExec(new Runnable() {
			@Override
            public void run() {
				if (!isDisposed()) {
					getDisplay().timerExec(updateInterval, timer);
				}
			}
		});
   	}

    @Override
    public void setBackground(Color color) {
        bgCol = color;
        canvas.redraw();
    }

    @Override
    public void setForeground(Color color) {
        if (color == null) {
            markCol = getDisplay().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
        } else {
            markCol = color;
        }
        
        textCol = ThemeUtils.isDarkTheme() ? ColorFactory.get(255, 255, 255) : markCol;

        canvas.redraw();
    }

    @Override
    public Color getForeground() {
        if (usedMemCol != null) {
            return usedMemCol;
        }
        return getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND);
    }

    /**
	 * Returns the maximum memory limit, or Long.MAX_VALUE if the max is not known.
	 */
	private long getMaxMem() {
		long max = Long.MAX_VALUE;
		try {
			// Must use reflect to allow compilation against JCL/Foundation
			Method maxMemMethod = Runtime.class.getMethod("maxMemory", new Class[0]); //$NON-NLS-1$
			Object o = maxMemMethod.invoke(Runtime.getRuntime(), new Object[0]);
			if (o instanceof Long) {
				max = ((Long) o).longValue();
			}
		}
		catch (Exception e) {
			// ignore if method missing or if there are other failures trying to determine the max
		}
		return max;
	}
	
	private void doDispose() {
    	if (gcImage != null) {
			gcImage.dispose();
		}
		if (disabledGcImage != null) {
			disabledGcImage.dispose();
		}
       
        if (lowMemCol != null) {
			lowMemCol.dispose();
		}
        if (freeMemCol != null) {
			freeMemCol.dispose();
		}
	}

    @Override
    public Point computeSize(int wHint, int hHint, boolean changed) {
        GC gc = new GC(this);
        Point p = gc.textExtent("MMMMMMMMMMMM"); //$NON-NLS-1$
        int height = imgBounds.height;
        // choose the largest of 
        // 	- Text height + margins
        //	- Image height + margins
        //	- Default Trim heightin 
        height = Math.max(height, p.y) + 4;
        height = Math.max(TrimUtil.TRIM_DEFAULT_HEIGHT, height);
        gc.dispose();
        return new Point(p.x + 15, height);
	}
	
    private void arm(boolean armed) {
        if (this.armed == armed) {
			return;
		}
        this.armed = armed;
        canvas.redraw();
        canvas.update();
    }

	private void gcRunning(boolean isInGC) {
		if (this.isInGC == isInGC) {
			return;
		}
		this.isInGC = isInGC;
		canvas.redraw();
		canvas.update();
	}

    /**
     * Creates the context menu
     */
    private void createContextMenu() {
        MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
			@Override
            public void menuAboutToShow(IMenuManager menuMgr) {
				fillMenu(menuMgr);
			}
		});
        Menu menu = menuMgr.createContextMenu(this);
        setMenu(menu);
    }
    
    private void fillMenu(IMenuManager menuMgr) {
        menuMgr.add(new SetMarkAction());
        menuMgr.add(new ClearMarkAction());
        menuMgr.add(new ShowMaxAction());
        menuMgr.add(new CloseHeapStatusAction());
//        if (isKyrsoftViewAvailable()) {
//        	menuMgr.add(new ShowKyrsoftViewAction());
//        }
    }

    /**
     * Sets the mark to the current usedMem level. 
     */
    private void setMark() {
    	updateStats();  // get up-to-date stats before taking the mark
        mark = usedMem;
        hasChanged = true;
        redraw();
    }

    /**
     * Clears the mark. 
     */
    private void clearMark() {
        mark = -1;
        hasChanged = true;
        redraw();
    }
    
    private void gc() {
		gcRunning(true);
		Thread t = new Thread() {
			@Override
            public void run() {
				busyGC();
				getDisplay().asyncExec(new Runnable() {
					@Override
                    public void run() {
						if (!isDisposed()) {
							gcRunning(false);
						}
					}
				});
			}
		};
		t.start();
    }

    private void busyGC() {
        for (int i = 0; i < 2; ++i) {
	        System.gc();
	        System.runFinalization();
        }
    }
    
    private void paintButton(GC gc) {
        Rectangle rect = canvas.getClientArea();
		if (isInGC) {
			if (disabledGcImage != null) {
				int buttonY = (rect.height - imgBounds.height) / 2 + rect.y;
				gc.drawImage(disabledGcImage, rect.x, buttonY);
			}
			return;
		}
        if (armed) {
            gc.setBackground(armCol);
            gc.fillRectangle(rect.x, rect.y, rect.width, rect.height);
        }
        if (gcImage != null) {
			int by = (rect.height - imgBounds.height) / 2 + rect.y; // button y
			gc.drawImage(gcImage, rect.x, by);
        }
    }

    private void paintComposite(GC gc) {
		if (showMax && maxMemKnown) {
			paintCompositeMaxKnown(gc);
		} else {
			paintCompositeMaxUnknown(gc);
		}
    }
    
    private void paintCompositeMaxUnknown(GC gc) {
        Rectangle rect = getClientArea();
        int x = rect.x;
        int y = rect.y;
        int w = rect.width;
        int h = rect.height;
        int bw = imgBounds.width; // button width
        int dx = x + w - bw - 2; // divider x
        int sw = w - bw - 3; // status width 
        int uw = (int) (sw * usedMem / totalMem); // used mem width
        int ux = x + 1 + uw; // used mem right edge
        
        if (bgCol != null) {
            //gc.setBackground(bgCol);
        }
        gc.fillRectangle(rect);
        gc.setForeground(sepCol);
		gc.drawLine(dx, y, dx, y + h);
		gc.drawLine(ux, y, ux, y + h);
        gc.setForeground(topLeftCol);
        gc.drawLine(x, y, x+w, y);
		gc.drawLine(x, y, x, y+h);
		gc.setForeground(bottomRightCol);
        gc.drawLine(x+w-1, y, x+w-1, y+h);
		gc.drawLine(x, y+h-1, x+w, y+h-1);
		
		gc.setBackground(usedMemCol);
        gc.fillRectangle(x + 1, y + 1, uw, h - 2);
        
        String s = convertToMegString(usedMem) + " of " + convertToMegString(totalMem); //$NON-NLS-1$
        Point p = gc.textExtent(s);
        int sx = (rect.width - 15 - p.x) / 2 + rect.x + 1;
        int sy = (rect.height - 2 - p.y) / 2 + rect.y + 1;
        gc.setForeground(textCol);
        gc.drawString(s, sx, sy, true);
        
        // draw an I-shaped bar in the foreground colour for the mark (if present)
        if (mark != -1) {
            int ssx = (int) (sw * mark / totalMem) + x + 1;
            paintMark(gc, ssx, y, h);
        }
    }

    private void paintCompositeMaxKnown(GC gc) {
        Rectangle rect = getClientArea();
        int x = rect.x;
        int y = rect.y;
        int w = rect.width;
        int h = rect.height;
        int bw = imgBounds.width; // button width
        int dx = x + w - bw - 2; // divider x
        int sw = w - bw - 3; // status width 
        int uw = (int) (sw * usedMem / maxMem); // used mem width
        int ux = x + 1 + uw; // used mem right edge
        int tw = (int) (sw * totalMem / maxMem); // current total mem width
        int tx = x + 1 + tw; // current total mem right edge
        
        if(bgCol != null) {
            //gc.setBackground(bgCol);
        }
        gc.fillRectangle(rect);
        gc.setForeground(sepCol);
		gc.drawLine(dx, y, dx, y + h);
		gc.drawLine(ux, y, ux, y + h);
		gc.drawLine(tx, y, tx, y + h);
        gc.setForeground(topLeftCol);
        gc.drawLine(x, y, x+w, y);
		gc.drawLine(x, y, x, y+h);
		gc.setForeground(bottomRightCol);
        gc.drawLine(x+w-1, y, x+w-1, y+h);
		gc.drawLine(x, y+h-1, x+w, y+h-1);
		
        if (lowMemThreshold != 0 && ((double)(maxMem - usedMem) / (double)maxMem < lowMemThreshold)) {
            gc.setBackground(lowMemCol);
        } else {
            gc.setBackground(usedMemCol);
        }
        gc.fillRectangle(x + 1, y + 1, uw, h - 2);
        
        gc.setBackground(freeMemCol);
        gc.fillRectangle(ux + 1, y + 1, tx - (ux + 1), h - 2);

        // paint line for low memory threshold
        if (showLowMemThreshold && lowMemThreshold != 0) {
            gc.setForeground(lowMemCol);
            int thresholdX = x + 1 + (int) (sw * (1.0 - lowMemThreshold));
            gc.drawLine(thresholdX, y + 1, thresholdX, y + h - 2);
        }

        String s = convertToMegString(usedMem) + " of " + convertToMegString(totalMem); //$NON-NLS-1$
        Point p = gc.textExtent(s);
        int sx = (rect.width - 15 - p.x) / 2 + rect.x + 1;
        int sy = (rect.height - 2 - p.y) / 2 + rect.y + 1;
        gc.setForeground(textCol);
        gc.drawString(s, sx, sy, true);
        
        // draw an I-shaped bar in the foreground colour for the mark (if present)
        if (mark != -1) {
            int ssx = (int) (sw * mark / maxMem) + x + 1;
            paintMark(gc, ssx, y, h);
        }
    }

	private void paintMark(GC gc, int x, int y, int h) {
        gc.setForeground(markCol);
		gc.drawLine(x, y+1, x, y+h-2);
		gc.drawLine(x-1, y+1, x+1, y+1);
		gc.drawLine(x-1, y+h-2, x+1, y+h-2);
	}

    private void updateStats() {
        Runtime runtime = Runtime.getRuntime();
        totalMem = runtime.totalMemory();
        long freeMem = runtime.freeMemory();
        usedMem = totalMem - freeMem;

        if (convertToMeg(prevUsedMem) != convertToMeg(usedMem)) {
            prevUsedMem = usedMem;
            this.hasChanged = true;
        }
        
        if (prevTotalMem != totalMem) {
            prevTotalMem = totalMem;
            this.hasChanged = true;
        }
    }

    private void updateToolTip() {
    	String usedStr = convertToMegString(usedMem);
    	String totalStr = convertToMegString(totalMem);
    	String maxStr = maxMemKnown ? convertToMegString(maxMem) : "<unknown>"; //$NON-NLS-1$
    	String markStr = mark == -1 ? "<none>" : convertToMegString(mark); //$NON-NLS-1$
    	String toolTip = "Heap size: " + usedStr + " of total: "  + totalStr + " max: " + maxStr + " mark: " + markStr; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        if(!toolTip.equals(getToolTipText())) {
            setToolTipText(toolTip);
        }
    }
	
    /**
     * Converts the given number of bytes to a printable number of megabytes (rounded up).
     */
    private String convertToMegString(long numBytes) {
        return Long.valueOf(convertToMeg(numBytes)) + "M"; //$NON-NLS-1$
    }

    /**
     * Converts the given number of bytes to the corresponding number of megabytes (rounded up).
     */
	private long convertToMeg(long numBytes) {
		return (numBytes + (512 * 1024)) / (1024 * 1024);
	}


    class SetMarkAction extends Action {
        SetMarkAction() {
            super("&Set Mark"); //$NON-NLS-1$
        }
        
        @Override
        public void run() {
            setMark();
        }
    }
    
    class ClearMarkAction extends Action {
        ClearMarkAction() {
            super("&Clear Mark"); //$NON-NLS-1$
        }
        
        @Override
        public void run() {
            clearMark();
        }
    }

    class ShowMaxAction extends Action {
    	ShowMaxAction() {
            super("Show &Max Heap", IAction.AS_CHECK_BOX); //$NON-NLS-1$
            setEnabled(maxMemKnown);
            setChecked(showMax);
        }
        
        @Override
        public void run() {
            showMax = !showMax;
            redraw();
        }
    }

    class CloseHeapStatusAction extends Action{
    	
    	CloseHeapStatusAction(){
    		super("&Close"); //$NON-NLS-1$
    	}
    	
    	@Override
        public void run(){
    		dispose();
    	}
    }
}

