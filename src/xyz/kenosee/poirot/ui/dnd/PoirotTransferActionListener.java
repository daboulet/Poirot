/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package xyz.kenosee.poirot.ui.dnd;

/*
 * PoirotTransferActionListener.java is used by the ListCutPaste example.
 */

import com.obtuse.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;

/**
 * A class that tracks the focused component.  This is necessary
 * to delegate the menu cut/copy/paste commands to the right
 * component.  An instance of this class is listening and
 * when the user fires one of these commands, it calls the
 * appropriate action on the currently focused component.
 */
public class PoirotTransferActionListener implements ActionListener,
                                                     PropertyChangeListener {
    private JComponent focusOwner = null;
    
    public PoirotTransferActionListener() {
        KeyboardFocusManager manager = KeyboardFocusManager.
           getCurrentKeyboardFocusManager();
        manager.addPropertyChangeListener("permanentFocusOwner", this);
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        Logger.logMsg( "PoirotTransferActionListener:  PropertyChangeEvent " + e );
        Object o = e.getNewValue();
        if (o instanceof JComponent) {
            Logger.logMsg( "PoirotTransferActionListener:  focus is now on " + o );
            focusOwner = (JComponent)o;
        } else {
            Logger.logMsg( "PoirotTransferActionListener:  nothing is in focus" );
            focusOwner = null;
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        Logger.logMsg( "PoirotTransferActionListener:  action performed - " + e );

        if (focusOwner == null)
            return;
        String action = (String)e.getActionCommand();
        Action a = focusOwner.getActionMap().get(action);
        if (a != null) {
            a.actionPerformed(new ActionEvent(focusOwner,
                                              ActionEvent.ACTION_PERFORMED,
                                              null));
        }
    }
}
