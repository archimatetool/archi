/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef;

import java.util.Map;

import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.widgets.Event;

/**
 * A <code>Tool</code> interprets Mouse and Keyboard input from an
 * {@link EditDomain} and its {@link EditPartViewer EditPartViewers}. The active
 * Tool and its state determines how the EditDomain will interpret input. Input
 * flows from a Viewer, to the EditDomain, to the EditDomain's active Tool.
 * <P>
 * <code>Tools</code> process low-level events and turn them into higher-level
 * operations. These operations are encapsulated by {@link Request Requests}.
 * The Requests are then used to communicate with EditParts in the Viewer to
 * perform the User's operation. Using Requests, Tools will:
 * <UL>
 * <LI>Ask EditParts for {@link org.eclipse.gef.commands.Command Commands} to
 * perform changes on the model.
 * <LI>Ask EditParts to show and erase feedback during an operation.
 * <LI>Ask EditParts to perform a generic function, using
 * {@link org.eclipse.gef.EditPart#performRequest(Request)}.
 * <P>
 * Tools also perform operations that do not involve the EditParts directly,
 * such as changing the Viewer's selection, scrolling the viewer, or invoking an
 * {@link org.eclipse.jface.action.IAction Action}.
 * <table>
 * <tr>
 * <td valign=top><img src="doc-files/important.gif"/>
 * <td>All feedback should be erased and temporary changes reverted prior to
 * executing any command.
 * </tr>
 * <tr>
 * <td valign=top><img src="doc-files/important.gif"/>
 * <td>Tools should process most keystrokes. For example, the DELETE key should
 * <EM>not</EM> be handled by adding a KeyListener to the Viewer's Control.
 * Doing so would mean that pressing DELETE would <EM>not</EM> be sensitive to
 * which Tool is currently active, and the state of the Tool. See
 * {@link org.eclipse.gef.KeyHandler} for how keystrokes are generally
 * processed.
 * </tr>
 * </table>
 * <p>
 * IMPORTANT: This interface is <EM>not</EM> intended to be implemented by
 * clients. Clients should inherit from
 * {@link org.eclipse.gef.tools.AbstractTool}. New methods may be added in the
 * future.
 */
@SuppressWarnings("rawtypes")
public interface Tool {

    /**
     * Called when this tool becomes the active tool for the EditDomain.
     * implementors can perform any necessary initialization here.
     * 
     * @see #deactivate()
     */
    void activate();

    /**
     * Called when another Tool becomes the active tool for the EditDomain.
     * implementors can perform state clean-up or to free resources.
     */
    void deactivate();

    /**
     * Called when a Viewer's Control gains keyboard focus.
     * 
     * @param event
     *            The SWT focus event
     * @param viewer
     *            The Viewer which gained focus
     */
    void focusGained(FocusEvent event, EditPartViewer viewer);

    /**
     * Called when a Viewer's Control loses keyboard focus.
     * 
     * @param event
     *            The SWT focus event
     * @param viewer
     *            The viewer that is losing focus
     */
    void focusLost(FocusEvent event, EditPartViewer viewer);

    /**
     * Called when a Viewer receives a key press.
     * 
     * @param keyEvent
     *            the SWT KeyEvent
     * @param viewer
     *            the Viewer which received a key press
     */
    void keyDown(KeyEvent keyEvent, EditPartViewer viewer);

    /**
     * Called when a viewer receives a key traversal. A tool can prevent the
     * traversal from occuring by setting the doit field to <code>false</code>.
     * 
     * @param event
     *            the SWT event
     * @param viewer
     *            the viewer which received the traversal
     * @since 3.1
     */
    void keyTraversed(TraverseEvent event, EditPartViewer viewer);

    /**
     * Called when a Viewer receives a key up.
     * 
     * @param keyEvent
     *            the SWT KeyEvent
     * @param viewer
     *            the Viewer which received a key up
     */
    void keyUp(KeyEvent keyEvent, EditPartViewer viewer);

    /**
     * Called when a Viewer receives a double-click.
     * 
     * @param mouseEvent
     *            the SWT mouse event
     * @param viewer
     *            the Viewer which received a double-click
     */
    void mouseDoubleClick(MouseEvent mouseEvent, EditPartViewer viewer);

    /**
     * Called when a Viewer receives a mouse down.
     * 
     * @param mouseEvent
     *            the SWT mouse event
     * @param viewer
     *            the Viewer which received a mouse down
     */
    void mouseDown(MouseEvent mouseEvent, EditPartViewer viewer);

    /**
     * Called when a Viewer receives a mouse drag. SWT does not distinguish
     * between mouse drags and mouse moves, but GEF Viewers will make this
     * distinction when dispatching events. A drag occurs if any mouse button is
     * down.
     * 
     * @param mouseEvent
     *            the SWT mouse event
     * @param viewer
     *            the Viewer which received a drag
     */
    void mouseDrag(MouseEvent mouseEvent, EditPartViewer viewer);

    /**
     * Called when a Viewer receives a mouse hover.
     * 
     * @see MouseTrackListener#mouseHover(MouseEvent)
     * @param mouseEvent
     *            the SWT mouse event
     * @param viewer
     *            the Viewer which received a mouse down
     */
    void mouseHover(MouseEvent mouseEvent, EditPartViewer viewer);

    /**
     * Called when a Viewer receives a mouse move.
     * 
     * @see #mouseDrag(MouseEvent, EditPartViewer)
     * @param mouseEvent
     *            the SWT mouse event
     * @param viewer
     *            the Viewer which received a mouse move
     */
    void mouseMove(MouseEvent mouseEvent, EditPartViewer viewer);

    /**
     * Called when a Viewer receives a mouse up.
     * 
     * @param mouseEvent
     *            the SWT mouse event
     * @param viewer
     *            the Viewer which received a mouse up
     */
    void mouseUp(MouseEvent mouseEvent, EditPartViewer viewer);

    /**
     * Called when the mouse wheel has been scrolled.
     * 
     * @param event
     *            The SWT event
     * @param viewer
     *            The source of the event
     */
    void mouseWheelScrolled(Event event, EditPartViewer viewer);

    /**
     * Called when a native drag ends on a Viewer. This event is important to
     * Tools because {@link #mouseUp(MouseEvent, EditPartViewer) mouseUp(..)}
     * will not occur once a native drag has started. The Tool should correct
     * its state to handle this lost Event.
     * 
     * @param event
     *            the SWT DragSourceEvent
     * @param viewer
     *            the Viewer on which a native drag started
     */
    void nativeDragFinished(DragSourceEvent event, EditPartViewer viewer);

    /**
     * Called when a native drag begins on a Viewer. This event is important to
     * Tools because {@link #mouseUp(MouseEvent, EditPartViewer) mouseUp(..)}
     * will not occur once a native drag has started. The Tool should correct
     * its state to handle this lost Event.
     * 
     * @param event
     *            the SWT DragSourceEvent
     * @param viewer
     *            the Viewer on which a native drag started
     */
    void nativeDragStarted(DragSourceEvent event, EditPartViewer viewer);

    /**
     * Called to set the EditDomain for this Tool. This is called right before
     * {@link #activate()}.
     * 
     * @param domain
     *            The EditDomain to which this Tool belongs
     */
    void setEditDomain(EditDomain domain);

    /**
     * Called to set the current Viewer receiving events. This method is rarely
     * called, since the Viewer is always passed along with the events
     * themselves. This method really just applies to {@link DragTracker
     * DragTrackers}.
     * 
     * @param viewer
     *            The current Viewer
     */
    void setViewer(EditPartViewer viewer);

    /**
     * Called when a Viewer receives a mouse enter.
     * 
     * @param mouseEvent
     *            the SWT mouse event
     * @param viewer
     *            the Viewer which received a mouse enter
     */
    void viewerEntered(MouseEvent mouseEvent, EditPartViewer viewer);

    /**
     * Called when a Viewer receives a mouse exit.
     * 
     * @param mouseEvent
     *            the SWT mouse event
     * @param viewer
     *            the Viewer which received a mouse exit
     */
    void viewerExited(MouseEvent mouseEvent, EditPartViewer viewer);

    /**
     * This method can be invoked to set any properties of this tool. This
     * allows clients who do not have direct access to a Tool to still be able
     * to configure that tool. The given Map should have the property's key and
     * value. This method will ignore unrecognized keys and values that are not
     * of the expected type.
     * <p>
     * This method should only be invoked once when the tool is first
     * constructed and is being initialized. Invoking this method at other times
     * may have undesired effects.
     * 
     * @param properties
     *            a mapping of the properties to be set and their new values;
     *            can be <code>null</code>
     * @since 3.1
     */
    void setProperties(Map properties);

}
