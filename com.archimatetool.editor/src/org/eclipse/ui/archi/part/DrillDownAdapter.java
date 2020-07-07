/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.archi.part;

import java.util.Arrays;
import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.WorkbenchMessages;

/**
 * Implements a simple web style navigation metaphor for a
 * <code>TreeViewer</code>. Home, back, and "drill into" functions are supported
 * for the viewer,
 * <p>
 * To use the <code>DrillDownAdapter</code>:
 * </p>
 * <ol>
 * <li>Create an instance of <code>TreeViewer</code>.</li>
 * <li>Create a <code>DrillDownAdapter</code> for the viewer.</li>
 * <li>Create a container for your viewer with a toolbar or a popup menu. Add
 * actions for "goBack", "goHome", and "goInto" to either one by calling
 * <code>addNavigationActions</code> with the popup menu or toolbar.</li>
 * </ol>
 * <p>
 * If the input for the underlying viewer is changed by something other than the
 * adapter the <code>reset</code> method should be called. This will clear the
 * drill stack and update the navigation buttons to reflect the new state of the
 * underlying viewer.
 * </p>
 */
@SuppressWarnings( {"restriction", "rawtypes"} )
public class DrillDownAdapter implements ISelectionChangedListener {
	private TreeViewer fChildTree;

	private DrillStack fDrillStack;

	private Action homeAction;

	private Action backAction;

	private Action forwardAction;

	/**
	 * Allocates a new DrillDownTreePart.
	 *
	 * @param tree the target tree for refocusing
	 */
	public DrillDownAdapter(TreeViewer tree) {
		fDrillStack = new DrillStack();
		fChildTree = tree;
	}

	/**
	 * Adds actions for "go back", "go home", and "go into" to a menu manager.
	 *
	 * @param manager is the target manager to update
	 */
	public void addNavigationActions(IMenuManager manager) {
		createActions();
		manager.add(homeAction);
		manager.add(backAction);
		manager.add(forwardAction);
		updateNavigationButtons();
	}

	/**
	 * Adds actions for "go back", "go home", and "go into" to a tool bar manager.
	 *
	 * @param toolBar is the target manager to update
	 */
	public void addNavigationActions(IToolBarManager toolBar) {
		createActions();
		toolBar.add(homeAction);
		toolBar.add(backAction);
		toolBar.add(forwardAction);
		updateNavigationButtons();
	}

	/**
	 * Returns whether expansion is possible for the current selection. This will
	 * only be true if it has children.
	 *
	 * @param element the object to test for expansion
	 * @return <code>true</code> if expansion is possible; otherwise return
	 *         <code>false</code>
	 */
	public boolean canExpand(Object element) {
		return fChildTree.isExpandable(element);
	}

	/**
	 * Returns whether "go back" is possible for child tree. This is only possible
	 * if the client has performed one or more drilling operations.
	 *
	 * @return <code>true</code> if "go back" is possible; <code>false</code>
	 *         otherwise
	 */
	public boolean canGoBack() {
		return fDrillStack.canGoBack();
	}

	/**
	 * Returns whether "go home" is possible for child tree. This is only possible
	 * if the client has performed one or more drilling operations.
	 *
	 * @return <code>true</code> if "go home" is possible; <code>false</code>
	 *         otherwise
	 */
	public boolean canGoHome() {
		return fDrillStack.canGoHome();
	}

	/**
	 * Returns whether "go into" is possible for child tree. This is only possible
	 * if the current selection in the client has one item and it has children.
	 *
	 * @return <code>true</code> if "go into" is possible; <code>false</code>
	 *         otherwise
	 */
	public boolean canGoInto() {
		IStructuredSelection oSelection = fChildTree.getStructuredSelection();
		if (oSelection == null || oSelection.size() != 1) {
			return false;
		}
		Object anElement = oSelection.getFirstElement();
		return canExpand(anElement);
	}

	/**
	 * Create the actions for navigation.
	 *
	 */
    private void createActions() {
		// Only do this once.
		if (homeAction != null) {
			return;
		}

		// Home.
		homeAction = new Action(WorkbenchMessages.GoHome_text) {
			@Override
			public void run() {
				goHome();
			}
		};
		homeAction.setToolTipText(WorkbenchMessages.GoHome_toolTip);
		homeAction.setImageDescriptor(WorkbenchImages.getImageDescriptor(ISharedImages.IMG_ETOOL_HOME_NAV));

		// Back.
		ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
		backAction = new Action(WorkbenchMessages.GoBack_text) {
			@Override
			public void run() {
				goBack();
			}
		};
		backAction.setToolTipText(WorkbenchMessages.GoBack_toolTip);
		backAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_BACK));
		backAction.setDisabledImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_BACK_DISABLED));

		// Forward.
		forwardAction = new Action(WorkbenchMessages.GoInto_text) {
			@Override
			public void run() {
				goInto();
			}
		};
		forwardAction.setToolTipText(WorkbenchMessages.GoInto_toolTip);
		forwardAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD));
		forwardAction.setDisabledImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD_DISABLED));

		// Update the buttons when a selection change occurs.
		fChildTree.addSelectionChangedListener(this);
		updateNavigationButtons();
	}

	/**
	 * Expands the given items in the tree. The list of items passed should be
	 * derived by calling <code>getExpanded</code>.
	 *
	 * @param items is a list of items within the tree which should be expanded
	 */
	private void expand(List items) {
		fChildTree.setExpandedElements(items.toArray());
	}

	/**
	 * Returns a list of elements corresponding to expanded nodes in child tree.
	 *
	 * @return a list of expandd elements
	 */
	private List getExpanded() {
		return Arrays.asList(fChildTree.getExpandedElements());
	}

	/**
	 * Reverts the input for the tree back to the state when <code>goInto</code> was
	 * last called.
	 * <p>
	 * A frame is removed from the drill stack. Then that frame is used to reset the
	 * input and expansion state for the child tree.
	 * </p>
	 */
	public void goBack() {
		Object currentInput = fChildTree.getInput();
		DrillFrame oFrame = fDrillStack.goBack();
		Object input = oFrame.getElement();
		fChildTree.setInput(input);
		expand(oFrame.getExpansion());
		// if there was a selection, it should have been preserved,
		// but if not, select the element that was drilled into
		if (fChildTree.getSelection().isEmpty()) {
			fChildTree.setSelection(new StructuredSelection(currentInput), true);
		}
		updateNavigationButtons();
	}

	/**
	 * Reverts the input for the tree back to the state when the adapter was
	 * created.
	 * <p>
	 * All of the frames are removed from the drill stack. Then the oldest frame is
	 * used to reset the input and expansion state for the child tree.
	 * </p>
	 */
	public void goHome() {
		Object currentInput = fChildTree.getInput();
		DrillFrame oFrame = fDrillStack.goHome();
		Object input = oFrame.getElement();
		fChildTree.setInput(input);
		expand(oFrame.getExpansion());
		// if there was a selection, it should have been preserved,
		// but if not, select the element that was last drilled into
		if (fChildTree.getSelection().isEmpty()) {
			fChildTree.setSelection(new StructuredSelection(currentInput), true);
		}
		updateNavigationButtons();
	}

	/**
	 * Sets the input for the tree to the current selection.
	 * <p>
	 * The current input and expansion state are saved in a frame and added to the
	 * drill stack. Then the input for the tree is changed to be the current
	 * selection. The expansion state for the tree is maintained during the
	 * operation.
	 * </p>
	 * <p>
	 * On return the client may revert back to the previous state by invoking
	 * <code>goBack</code> or <code>goHome</code>.
	 * </p>
	 */
	public void goInto() {
		IStructuredSelection sel = fChildTree.getStructuredSelection();
		Object element = sel.getFirstElement();
		goInto(element);
	}

	/**
	 * Sets the input for the tree to a particular item in the tree.
	 * <p>
	 * The current input and expansion state are saved in a frame and added to the
	 * drill stack. Then the input for the tree is changed to be
	 * <code>newInput</code>. The expansion state for the tree is maintained during
	 * the operation.
	 * </p>
	 * <p>
	 * On return the client may revert back to the previous state by invoking
	 * <code>goBack</code> or <code>goHome</code>.
	 * </p>
	 *
	 * @param newInput the new input element
	 */
	public void goInto(Object newInput) {
		// If we can drill ..
		if (canExpand(newInput)) {
			// Save the old state.
			Object oldInput = fChildTree.getInput();
			List expandedList = getExpanded();
			fDrillStack.add(new DrillFrame(oldInput, "null", expandedList));//$NON-NLS-1$

			// Install the new state.
			fChildTree.setInput(newInput);
			expand(expandedList);
			updateNavigationButtons();
		}
	}

	/**
	 * Resets the drill down adapter.
	 * <p>
	 * This method is typically called when the input for the underlying view is
	 * reset by something other than the adapter. On return the drill stack has been
	 * cleared and the navigation buttons reflect the new state of the underlying
	 * viewer.
	 * </p>
	 */
	public void reset() {
		fDrillStack.reset();
		updateNavigationButtons();
	}

	/**
	 * Updates the navigation buttons when a selection change occurs in the tree.
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		updateNavigationButtons();
	}

	/**
	 * Updates the enabled state for each navigation button.
	 */
	protected void updateNavigationButtons() {
		if (homeAction != null) {
			homeAction.setEnabled(canGoHome());
			backAction.setEnabled(canGoBack());
			forwardAction.setEnabled(canGoInto());
		}
	}
}
