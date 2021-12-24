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
 * PoirotJListTransferHandler.java is used by the ListCutPaste example.
 */

import com.obtuse.exceptions.HowDidWeGetHereError;
import com.obtuse.util.Logger;
import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;
import xyz.kenosee.poirot.data.ReferenceItem;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.*;
import java.util.List;

public class PoirotJListTransferHandler extends TransferHandler {

//    @SuppressWarnings("FieldMayBeFinal") private PoirotDataFlavorHandler[] _supportedFlavors;

    public PoirotJListTransferHandler( /* @NotNull final PoirotDataFlavorHandler[] supportedFlavors */ ) {
        super();

//        _supportedFlavors = Arrays.copyOf( supportedFlavors, supportedFlavors.length );

    }

    /**
     Perform the actual data import.
     */

    public boolean importData( TransferHandler.TransferSupport info ) {

        try {

            Logger.logMsg( "PoirotJListTransferHandler.importData . . ." );

            //If we can't handle the import, bail now.
            if ( !canImport( info ) ) {

                Logger.logMsg( "importData:  bailing because canImport cannot find a mutually agreeable DataFlavor" );

                return false;

            }

            if ( !( info.getComponent() instanceof JList ) ) {

                Logger.logMsg( "throwing IAE" );

                throw new IllegalArgumentException( "PoirotJListTransferHandler.importData:  " +
                                                    "we (currently) only support JLists but you gave me a " +
                                                    info.getComponent().getClass()
                );

            }

            Transferable transferable = info.getTransferable();

            @NotNull Optional<List<ReferenceItem>> optReferenceItems = ImageDataCarrier.getReferenceItems( transferable );

            if ( optReferenceItems.isPresent() ) {

                @SuppressWarnings("unchecked") JList<ReferenceItem> jList = (JList<ReferenceItem>)info.getComponent();
                DefaultListModel<ReferenceItem> model = (DefaultListModel<ReferenceItem>)jList.getModel();
                processTransfer( info, optReferenceItems.get(), jList, model );

                return true;

            } else {

                Logger.logErr( "PoirotJListTransferHandler: No usable objects in DnD/CnP operation" );

                return false;

            }

        } catch ( HowDidWeGetHereError e ) {

            Logger.logErr( "com.obtuse.exceptions.HowDidWeGetHereError caught", e );

            ObtuseUtil.doNothing();

            return false;

        } catch ( Throwable e ) {

            Logger.logErr( "java.lang.IllegalArgumentException caught", e );

            ObtuseUtil.doNothing();

            return false;

        }

//            @NotNull Optional<PoirotDataFlavorHandler> optHandler = PoirotDataFlavorHandler.findDataFlavorHandler( support );
//            if ( optHandler.isEmpty() ) {
//
//                Logger.logMsg( "throwing HowDidWeGetHereError" );
//
//                throw new HowDidWeGetHereError(
//                        "PoirotJListTransferHandler.importData:  no handler for " +
//                        formatSupportedMimeTypes( support )
//                );
//
//            }
//
//            PoirotDataFlavorHandler handler = optHandler.get();
//
//            @SuppressWarnings("unchecked") JList<ReferenceItem> jList = (JList<ReferenceItem>)support.getComponent();
//            DefaultListModel<ReferenceItem> model = (DefaultListModel<ReferenceItem>)jList.getModel();
//
//            List<ReferenceItem> rimList;
//            try {
//
//                Optional<List<? extends ReferenceItem>> optRim;
//                Logger.logMsg( "doing the magic via " + handler.getFlavorName() + " - " + handler.getSupportedDataFlavor() );
//                optRim = handler.createReferenceItemManager( support );
//                if ( optRim.isPresent() ) {
//
//                    rimList = new ArrayList<>( optRim.get() );
//
//                    processTransfer( support, rimList, jList, model );
//
//                    Logger.logMsg( "we think that it worked" );
//
//                    ObtuseUtil.doNothing();
//
//                    return true;
//
//                } else {
//
//                    Logger.logMsg( "PoirotJListTransferHandler.importData:  nothing to import" );
//
//                    ObtuseUtil.doNothing();
//
//                    return false;
//
//                }
//
//
//            } catch ( IOException e ) {
//
//                Logger.logErr( "PoirotJListTransferHandler.importData:  java.io.IOException caught", e );
//
//                ObtuseUtil.doNothing();
//
//                return false;
//
//            } catch ( UnsupportedFlavorException e ) {
//
//                Logger.logErr( "PoirotJListTransferHandler.importData:  java.awt.datatransfer.UnsupportedFlavorException caught", e );
//
//                ObtuseUtil.doNothing();
//
//                return false;
//
//            }
//
//        } catch ( HowDidWeGetHereError e ) {
//
//            Logger.logErr( "com.obtuse.exceptions.HowDidWeGetHereError caught", e );
//
//            ObtuseUtil.doNothing();
//
//            return false;
//
//        } catch ( Throwable e ) {
//
//            Logger.logErr( "java.lang.IllegalArgumentException caught", e );
//
//            ObtuseUtil.doNothing();
//
//            return false;
//
//        }

    }

//    public boolean oldImportData( TransferHandler.TransferSupport info ) {
//
//        try {
//            Logger.logMsg( "PoirotJListTransferHandler.importData . . ." );
//
//            //If we can't handle the import, bail now.
//            if ( !canImport( info ) ) {
//
//                Logger.logMsg( "importData:  bailing because canImport cannot find a mutually agreeable DataFlavor" );
//
//                return false;
//
//            }
//
//            if ( !( info.getComponent() instanceof JList ) ) {
//
//                Logger.logMsg( "throwing IAE" );
//
//                throw new IllegalArgumentException( "PoirotJListTransferHandler.importData:  " +
//                                                    "we (currently) only support JLists but you gave me a " +
//                                                    info.getComponent().getClass()
//                );
//
//            }
//
//            @NotNull Optional<PoirotDataFlavorHandler> optHandler = PoirotDataFlavorHandler.findDataFlavorHandler( support );
//            if ( optHandler.isEmpty() ) {
//
//                Logger.logMsg( "throwing HowDidWeGetHereError" );
//
//                throw new HowDidWeGetHereError(
//                        "PoirotJListTransferHandler.importData:  no handler for " +
//                        formatSupportedMimeTypes( support )
//                );
//
//            }
//
//            PoirotDataFlavorHandler handler = optHandler.get();
//
//            @SuppressWarnings("unchecked") JList<ReferenceItem> jList = (JList<ReferenceItem>)support.getComponent();
//            DefaultListModel<ReferenceItem> model = (DefaultListModel<ReferenceItem>)jList.getModel();
//
//            List<ReferenceItem> rimList;
//            try {
//
//                Optional<List<? extends ReferenceItem>> optRim;
//                Logger.logMsg( "doing the magic via " + handler.getFlavorName() + " - " + handler.getSupportedDataFlavor() );
//                optRim = handler.createReferenceItemManager( support );
//                if ( optRim.isPresent() ) {
//
//                    rimList = new ArrayList<>( optRim.get() );
//
//                    processTransfer( support, rimList, jList, model );
//
//                    Logger.logMsg( "we think that it worked" );
//
//                    ObtuseUtil.doNothing();
//
//                    return true;
//
//                } else {
//
//                    Logger.logMsg( "PoirotJListTransferHandler.importData:  nothing to import" );
//
//                    ObtuseUtil.doNothing();
//
//                    return false;
//
//                }
//
//
//            } catch ( IOException e ) {
//
//                Logger.logErr( "PoirotJListTransferHandler.importData:  java.io.IOException caught", e );
//
//                ObtuseUtil.doNothing();
//
//                return false;
//
//            } catch ( UnsupportedFlavorException e ) {
//
//                Logger.logErr( "PoirotJListTransferHandler.importData:  java.awt.datatransfer.UnsupportedFlavorException caught", e );
//
//                ObtuseUtil.doNothing();
//
//                return false;
//
//            }
//
//        } catch ( HowDidWeGetHereError e ) {
//
//            Logger.logErr( "com.obtuse.exceptions.HowDidWeGetHereError caught", e );
//
//            ObtuseUtil.doNothing();
//
//            return false;
//
//        } catch ( Throwable e ) {
//
//            Logger.logErr( "java.lang.IllegalArgumentException caught", e );
//
//            ObtuseUtil.doNothing();
//
//            return false;
//
//        }
//
////        info.getTransferable().getTransferData(  )
////        ReferenceItem rim =  new ReferenceItem( info,  )
////        @SuppressWarnings("unchecked") JList<ReferenceItem> list = (JList<ReferenceItem>)info.getComponent();
////
////        //Fetch the data -- bail if this fails
////
////        Object data;
////
////        try {
////
////            data = info.getTransferable()
////                       .getTransferData( DataFlavor.stringFlavor );
////
////        } catch ( UnsupportedFlavorException ufe ) {
////
////            System.out.println( "PoirotJListTransferHandler.importData: unsupported data flavor" );
////
////            return false;
////
////        } catch ( IOException ioe ) {
////
////            System.out.println( "PoirotJListTransferHandler.importData: I/O exception" );
////
////            return false;
////
////        }
//
////        //noinspection IfStatementWithIdenticalBranches
////        processTransfer( info, rimList, jList, model );
////
////        return true;
//
//    }

    private void processTransfer(
            final TransferSupport info,
            final List<ReferenceItem> rimList,
            final JList<ReferenceItem> list,
            final DefaultListModel<ReferenceItem> model
    ) {

        int index;
        if ( info.isDrop() ) { //This is a drop

            JList.DropLocation dl = (JList.DropLocation)info.getDropLocation();

            index = dl.getIndex();
            if ( dl.isInsert() ) {

                Logger.logMsg( "doing DnD insert" );

////                String descriptiveName = data.toString();
////                ReferenceItem rim = new ReferenceItem( descriptiveName );
//                int i = index;
//                for ( ReferenceItem rim : rimList ) {
//
//                    model.add( i, rim );
//
//                    i += 1;
//
//                }

                ObtuseUtil.doNothing();

            } else {

                throw new HowDidWeGetHereError(
                        "PoirotJListTransferHandler.importData:  we don't do drops that are not inserts (" + dl + " )"
                );

//                model.set( index, rim.toString() );
//
//                ObtuseUtil.doNothing();

            }

            ObtuseUtil.doNothing();

        } else { // this is a paste

            index = list.getSelectedIndex();

        }

            // if there is a valid selection,
            // insert data after the selection

//            int i = index;
//            for ( ReferenceItem rim : rimList ) {
//
//                model.add( i, rim );
//
//                i += 1;
//
//            }

//            String descriptiveName = data.toString();
//            ReferenceItem rim = new ReferenceItem( descriptiveName );

//        if ( index >= 0 ) {

        int i = index < 0 ? model.size() : index;
        for ( ReferenceItem rim : rimList ) {

            model.add( i, rim );

            i += 1;

        }

//        } else { // else append to the end of the list
//
//            for ( ReferenceItem rim : rimList ) {
//
//                model.addElement( rim );
//
//            }
//
//        }

        ObtuseUtil.doNothing();

    }

    /**
     Bundle up the data for export.
     */

    protected Transferable createTransferable( JComponent c ) {

        @SuppressWarnings("unchecked") JList<ReferenceItem> list = (JList<ReferenceItem>)c;
        @SuppressWarnings("unused") int index = list.getSelectedIndex();
        ReferenceItem rim = list.getSelectedValue();
        return new StringSelection( rim.getDescriptiveName() );
    }

    /**
     The list handles both copy and move actions.
     */
    public int getSourceActions( JComponent c ) {

        return COPY_OR_MOVE;
    }

    /**
     When the export is complete, don't do anything other than get grumpy if it was a MOVE request.
     */

    protected void exportDone( @NotNull JComponent c, Transferable data, int action ) {

        if ( action == MOVE ) {

            throw new HowDidWeGetHereError(
                    "PoirotJListTransferHandler.exportDone:  action=MOVE but we don't do MOVEs via either DnD or CnP"
            );

//            if ( c instanceof JList ) {
//
//                @SuppressWarnings("unchecked")
//                JList<ReferenceItem> list = (JList<ReferenceItem>)c;
//                DefaultListModel<ReferenceItem> model = (DefaultListModel<ReferenceItem>)list.getModel();
//                int index = list.getSelectedIndex();
//                model.remove( index );
//
//            } else {
//
//                throw new IllegalArgumentException(
//                        "PoirotJListTransferHandler.exportDone:  " +
//                        "exporting component should have been a JList, it was a " +
//                        c.getClass().getName()
//                );
//
//            }

        }

    }

    /**
     Determine if the transfer support at least one of the typical flavors.
     <p>The 'typical flavors' are defined to be:
     <ul>
     <li>DataFlavor.imageFlavor</li>
     <li>DataFlavor.javaFileListFlavor</li>
     <li>DataFlavor.stringFlavor</li>
     </ul>
     </p>
     */

    public boolean canImport( TransferHandler.TransferSupport info ) {

        ObtuseUtil.doNothing();

        for (
                DataFlavor candidateDataFlavor
                :
                new DataFlavor[] {
                        DataFlavor.imageFlavor,
                        DataFlavor.javaFileListFlavor,
                        DataFlavor.stringFlavor
                }
        ) {

            if ( info.isDataFlavorSupported( candidateDataFlavor ) ) {

                Logger.logMsg(
                        "PoirotJListTransferHandler.canImport:  " +
                        "transfer supports " + candidateDataFlavor
                );

                return true;

            }

        }

        Logger.logMsg(
                "PoirotDataFlavorHandler.findDataFlavorHandler:  " +
                "transfer does not support any of the typical flavors " +
                "(DataFlavor.{imageFlavor, javaFileListFlavor, or stringFlavor})"
        );

        return false;

////        Logger.logMsg( "PoirotJListTransferHandler.canImport . . ." );
//
////        return canImport( info, _supportedFlavors );
////
////    }
////
////    private boolean canImport(
////            @NotNull final TransferHandler.TransferSupport info,
////            @NotNull final PoirotDataFlavorHandler[] supportedFlavors
////    ) {
//
//        Optional<DataFlavor> optHandler = findDataFlavorHandler( info );
//
//        if ( optHandler.isPresent() ) {
//
//            Logger.logMsg( "PoirotJListTransferHandler.canImport:  transfer supports " + formatSupportedMimeTypes( info ) );
//
//            return true;
//
//        }
//
//        Logger.logMsg( "transfer does not support " + formatSupportedMimeTypes( info ) );
//
//        return false;

    }

//    @NotNull
//    public static Optional<DataFlavor> findDataFlavorHandler(
//            @NotNull final TransferHandler.TransferSupport info
//    ) {
//
////        Logger.logMsg( "PoirotDataFlavorHandler.findDataFlavorHandler:  support says it can handle " + PoirotJListTransferHandler.formatSupportedMimeTypes( info ) );
////        Logger.logMsg( "PoirotDataFlavorHandler.findDataFlavorHandler:  checking if we are being handed something it support" );
//
//        PoirotDataFlavorHandler firstHandler = null;
//
////        for ( PoirotDataFlavorHandler poirotDataFlavorHandler : s_supportedDataFlavors ) {
////
////        }
//
//        for ( PoirotDataFlavorHandler poirotDataFlavorHandler : s_supportedDataFlavors ) {
//
//            if ( s_verbose_find ) {
//                Logger.logMsg( "---" );
//                Logger.logMsg( "looking at flavor " + poirotDataFlavorHandler );
//                Logger.logMsg(
//                        "PoirotDataFlavorHandler.findDataFlavorHandler:  " +
//                        poirotDataFlavorHandler.getFlavorName() +
//                        " handler " +
//                        "is " + poirotDataFlavorHandler
//                );
//            }
//
//            DataFlavor dataFlavor = poirotDataFlavorHandler.getSupportedDataFlavor();
//
////            Logger.logMsg( "PoirotDataFlavorHandler.findDataFlavorHandler:  checking " + dataFlavor.getHumanPresentableName() + " with " + poirotDataFlavorHandler.getFlavorName() + " w.r.t. " + PoirotJListTransferHandler.formatSupportedMimeTypes( info ) );
//
//            if ( info.isDataFlavorSupported( dataFlavor ) ) {
//
//                Logger.logMsg(
//                        "PoirotDataFlavorHandler.findDataFlavorHandler:  " +
//                        "found " + dataFlavor.getHumanPresentableName() + " which is " + dataFlavor
//                );
//
//                if ( s_verbose_find ) {
//
//                    if ( firstHandler == null ) {
//
//                        firstHandler = poirotDataFlavorHandler;
//
//                    } else {
//
//                        ObtuseUtil.doNothing();
//
//                    }
//
//                } else {
//
//                    return Optional.of( poirotDataFlavorHandler );
//
//                }
//
//            }
//
//        }
//
//        if ( firstHandler != null ) {
//
//            Logger.logMsg(
//                    "PoirotDataFlavorHandler.findDataFlavorHandler:  returning " + firstHandler.getFlavorName() + " which supports " +
//                    PoirotJListTransferHandler.formatSupportedMimeTypes( info ) );
//
//            Logger.logMsg( "!!!" );
//
//            return Optional.of( firstHandler );
//
//        }
//
//        Logger.logMsg( "PoirotDataFlavorHandler.findDataFlavorHandler:  we do not support any of the provided flavors" );
//
//        Logger.logMsg( "???" );
//
//        return Optional.empty();
//
//    }

//    /**
//     We only support importing strings.
//     */
//
//    public boolean canImport( TransferHandler.TransferSupport info ) {
//
////        Logger.logMsg( "PoirotJListTransferHandler.canImport . . ." );
//
////        return canImport( info, _supportedFlavors );
////
////    }
////
////    private boolean canImport(
////            @NotNull final TransferHandler.TransferSupport info,
////            @NotNull final PoirotDataFlavorHandler[] supportedFlavors
////    ) {
//
//        Optional<PoirotDataFlavorHandler> optHandler = PoirotDataFlavorHandler.findDataFlavorHandler( info );
//
//        if ( optHandler.isPresent() ) {
//
//            Logger.logMsg( "PoirotJListTransferHandler.canImport:  transfer supports " + formatSupportedMimeTypes( info ) );
//
//            return true;
//
//        }
//
//        Logger.logMsg( "transfer does not support " + formatSupportedMimeTypes( info ) );
//
//        return false;
//
//    }

}
