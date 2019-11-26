/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Drop Down Action
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractDropDownAction extends Action implements IMenuCreator {

    protected Menu fMenu;
    protected List<IContributionItem> fItems = new ArrayList<IContributionItem>();
    
    public AbstractDropDownAction() {
        this(null);
    }
    
    public AbstractDropDownAction(String text) {
        setMenuCreator(this);
        setText(text);
        setToolTipText(text);
    }

    @Override
    public Menu getMenu(Menu parent) {
        if(fMenu == null) {
            fMenu = new Menu(parent);
            fill();
        }

        return fMenu;
    }

    @Override
    public Menu getMenu(Control parent) {
        if(fMenu == null) {
            fMenu = new Menu(parent);
            fill();
        }

        return fMenu;
    }
    
    /**
     * Show the sub-menu when the main tool item is clicked
     */
    @Override
    public void runWithEvent(Event event) {
        ToolItem ti = (ToolItem)event.widget;
        Rectangle bounds = ti.getBounds();
        Control control = ti.getParent();
        Menu menu = getMenu(control);
        Point point = control.toDisplay(new Point(bounds.x, bounds.height));
        menu.setLocation(point);
        menu.setVisible(true);
    }
    
    private void fill() {
        for(IContributionItem item : fItems) {
            item.fill(fMenu, -1);
        }
    }
    
    public void add(IAction action) {
        add(new ActionContributionItem(action));
    }
    
    public void add(IContributionItem item) {
        fItems.add(item);
    }
    
    @Override
    public void dispose() {
        if(fMenu != null) {
            fMenu.dispose();
            fMenu = null;
        }
        // Don't dispose of items because they might be re-used
    }
}