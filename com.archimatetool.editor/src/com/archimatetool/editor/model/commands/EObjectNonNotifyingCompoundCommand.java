/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.commands;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.CompoundCommand;


/**
 * Compound Command that will send an ECore Notification to listeners to warn them that a series of Notifications is coming
 * related to a given EObject.
 * Used where many commands might cause excessive amounts of UI refreshes in clients listening to model changes. 
 * 
 * @author Phillip Beauvoir
 */
public class EObjectNonNotifyingCompoundCommand extends CompoundCommand {

    public static final int START = 999;
    public static final int END = 1000;

    protected EObject eObject;
    protected Notification msgStart, msgEnd;

    public EObjectNonNotifyingCompoundCommand(EObject eObject) {
        this(eObject, null);
    }

    public EObjectNonNotifyingCompoundCommand(EObject eObject, String label) {
        super(label);
        this.eObject = eObject;
        msgStart = new NotificationImpl(START, null, eObject);
        msgEnd = new NotificationImpl(END, null, eObject);
    }

    @Override
    public void execute() {
        eObject.eNotify(msgStart);
        super.execute();
        eObject.eNotify(msgEnd);
    }

    @Override
    public void undo() {
        eObject.eNotify(msgStart);
        super.undo();
        eObject.eNotify(msgEnd);
    }

    @Override
    public void redo() {
        eObject.eNotify(msgStart);
        super.redo();
        eObject.eNotify(msgEnd);
    }
}
