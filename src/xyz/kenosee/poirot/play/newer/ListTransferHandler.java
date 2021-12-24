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

package xyz.kenosee.poirot.play.newer;

/*
 * PoirotJListTransferHandler.java is used by the ListCutPaste example.
 */

import com.obtuse.util.Logger;
import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

class ListTransferHandler<T> extends TransferHandler {

    @SuppressWarnings("FieldMayBeFinal") private DataFlavor[] _supportedFlavors;

    public ListTransferHandler( @NotNull final DataFlavor[] supportedFlavors ) {

        super();

        _supportedFlavors = Arrays.copyOf( supportedFlavors, supportedFlavors.length );

    }

    /**
     Perform the actual data import.
     */
    public boolean importData( TransferSupport info ) {

        //If we can't handle the import, bail now.
        if ( !canImport( info ) ) {

            return false;

        }

        if ( !( info.getComponent() instanceof JList ) ) {

            throw new IllegalArgumentException( "PoirotJListTransferHandler.importData:  " +
                                                "we (currently) only support JLists but you gave me a " +
                                                info.getComponent().getClass()
            );

        }

        @SuppressWarnings("unchecked") JList<String> list = (JList<String>)info.getComponent();
        DefaultListModel<String> model = (DefaultListModel<String>)list.getModel();

        //Fetch the data -- bail if this fails

        Object data;

        try {

            data = info.getTransferable()
                       .getTransferData( DataFlavor.stringFlavor );

        } catch ( UnsupportedFlavorException ufe ) {

            System.out.println( "PoirotJListTransferHandler.importData: unsupported data flavor" );

            return false;

        } catch ( IOException ioe ) {

            System.out.println( "PoirotJListTransferHandler.importData: I/O exception" );

            return false;

        }

        //noinspection IfStatementWithIdenticalBranches
        if ( info.isDrop() ) { //This is a drop

            JList.DropLocation dl = (JList.DropLocation)info.getDropLocation();

            int index = dl.getIndex();
            //noinspection IfStatementWithIdenticalBranches
            if ( dl.isInsert() ) {

                model.add( index, data.toString() );

                ObtuseUtil.doNothing();

            } else {

                model.set( index, data.toString() );

                ObtuseUtil.doNothing();

            }

            ObtuseUtil.doNothing();

        } else { // this is a paste

            int index = list.getSelectedIndex();

            // if there is a valid selection,
            // insert data after the selection

            if ( index >= 0 ) {

                model.add( list.getSelectedIndex() + 1, data.toString() );

            } else { // else append to the end of the list

                model.addElement( data.toString() );

            }

            ObtuseUtil.doNothing();

        }

        return true;

    }

    /**
     Bundle up the data for export.
     */
    protected Transferable createTransferable( JComponent c ) {

        @SuppressWarnings("unchecked") JList<T> list = (JList<T>)c;
        @SuppressWarnings("unused") int index = list.getSelectedIndex();
        String value = (String)list.getSelectedValue();
        return new StringSelection( value );
    }

    /**
     The list handles both copy and move actions.
     */
    public int getSourceActions( JComponent c ) {

        return COPY_OR_MOVE;
    }

    /**
     When the export is complete, remove the old list entry if the
     action was a move.
     */
    protected void exportDone( JComponent c, Transferable data, int action ) {

        if ( action != MOVE ) {
            return;
        }
        @SuppressWarnings("unchecked") JList<T> list = (JList<T>)c;
        DefaultListModel<T> model = (DefaultListModel<T>)list.getModel();
        int index = list.getSelectedIndex();
        model.remove( index );
    }

    /**
     We only support importing strings.
     */
    public boolean canImport( TransferSupport info ) {

//        return canImport( support, _supportedFlavors );
//
//    }
//
//    private boolean canImport(
//            @NotNull final TransferHandler.TransferSupport info,
//            @NotNull final DataFlavorHandler[] supportedFlavors
//    ) {

        for ( DataFlavor flavor : _supportedFlavors ) {

            if ( info.isDataFlavorSupported( flavor ) ) {

                return true;

            }

        }

        return false;

    }

}
