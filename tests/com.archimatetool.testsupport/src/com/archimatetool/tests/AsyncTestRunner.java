/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.tests;

import org.eclipse.swt.widgets.Display;


/**
 * Run a Unit Test asyncronously.
 * Sub-classes must call stop() as the first command in their run() implementation
 * 
   Example:
           AsyncTestRunner runner = new AsyncTestRunner() {
               @Override
                public void run() {
                    stop();

                    // Run some code
                    someCondition = ...;
                    assertNotNull(someCondition);
                }
           };
           
           runner.start();

 *
 * @author Phillip Beauvoir
 */
public abstract class AsyncTestRunner implements Runnable {
    
    boolean waiting = true;
    
    @Override
    public void run() {
        stop();
    }
    
    public void start() {
        Display.getCurrent().asyncExec(this);
        
        while(waiting) {
            if(!Display.getCurrent().readAndDispatch()) {
                Display.getCurrent().sleep();
            }
        }
    }

    public void stop() {
        waiting = false;
    }

}
