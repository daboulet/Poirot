package xyz.kenosee.poirot.ui.dnd;

import com.obtuse.exceptions.HowDidWeGetHereError;
import com.obtuse.util.FormattingList;
import com.obtuse.util.Logger;
import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;
import xyz.kenosee.poirot.util.ImageMediaFileFetcher;
import xyz.kenosee.poirot.util.MediaFileContents;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 Created by danny on 2021/12/21.
 */

public class DataCarrierUtils {

    @NotNull
//    public abstract Optional<Object> getData();

    public static Optional<List<DataCarrier>> getDataCarriers( @NotNull final Transferable transferable ) {

        Logger.logMsg(
                "DataCarrierUtils.getDataCarriers:  " +
                "transferable claims to support " + Arrays.toString( transferable.getTransferDataFlavors() )
        );

        // Is there an image in the Transferable?

        // First try to get it as an image.
        // If that doesn't work, try to get it as a file list.
        // If that doesn't work, there is no plan C so bail out.

        Optional<Object> optValue;

//        Logger.logMsg( "contemplating trying DataFlavor.imageFlavor" );
        if ( transferable.isDataFlavorSupported( DataFlavor.imageFlavor ) ) {

            return getImageDatum( transferable );

        } else if ( transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor ) ) {

            return getFileListDatum( transferable );

        } else if ( transferable.isDataFlavorSupported( DataFlavor.stringFlavor ) ) {

            return getStringDatum( transferable );

        } else {

            // I have no idea what this Transferable contains.
            // This should be impossible if canImport() is checking for the same sort of data as we are
            // capable of handling in this meth.

            throw new HowDidWeGetHereError(
                    "DataCarrierUtils.getDataCarriers:  transferable doesn't contain anything we can handle - " +
                    "it claims to support " + Arrays.toString( transferable.getTransferDataFlavors() )
            );

        }

    }

    @NotNull
    private static Optional<List<DataCarrier>> getImageDatum( final @NotNull Transferable transferable ) {

        Optional<Object> optValue;
        Logger.logMsg( "DataFlavor.imageFlavor should work" );

        optValue = DndUtils.tryTransfer( transferable, DataFlavor.imageFlavor );

        Logger.logMsg( "imageFlavor got " + optValue );

        // If that worked then we're done.

        if ( optValue.isPresent() ) {

            Object receivedObject = optValue.get();
            if ( receivedObject instanceof Image image ) {

                Optional<List<DataCarrier>> rval = Optional.of( new FormattingList<>( new ImageDataCarrier( image ) ) );
                System.out.println( "rval = " + rval );
                return rval;

            }

            throw new HowDidWeGetHereError(
                    "DataCarrierUtils.getDataCarriers:  " +
                    "request for an image worked but we didn't actually get an image, we got a " +
                    receivedObject.getClass().getCanonicalName()
            );

        }

        throw new HowDidWeGetHereError( "DataCarrierUtils.getDataCarriers:  expected an Image, got nothing" );

    }

    @NotNull
    private static Optional<List<DataCarrier>> getFileListDatum( final @NotNull Transferable transferable ) {

        Optional<Object> optValue;
        // There's a file list in the Transferable.

//            Logger.logMsg( "contemplating trying DataFlavor.javaFileListFlavor" );
//        if ( transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor ) ) {

        Logger.logMsg( "DataFlavor.javaFileListFlavor should work" );

        optValue = DndUtils.tryTransfer( transferable, DataFlavor.javaFileListFlavor );

        Logger.logMsg( "javaFileListFlavor got " + optValue );

//        Optional<List<FileListDataCarrier>> rval;
        if ( optValue.isPresent() ) {

            List<DataCarrier> dataCarriers = new FormattingList<>();

            Object receivedObject = optValue.get();
            if ( receivedObject instanceof List list ) {

                int ix = -1;
                for ( Object element : list ) {

                    ix += 1;

                    if ( element instanceof File file ) {

                        final DataCarrier carrier;
                        @NotNull MediaFileContents mediaFileContents;
                        if (
                                ImageMediaFileFetcher.hasSupportedImageFileSuffix( file )
                                && (
                                    mediaFileContents = ImageMediaFileFetcher.fetchMediaFile( file )
                                ).worked()

                        ) {

//                            boolean gotMediaFile = false;


//                            if ( mediaFileContents.worked() ) {

                                ImageIcon icon = new ImageIcon(
                                        mediaFileContents.getImageFileBytes()
                                );

                                if ( icon.getImageLoadStatus() == MediaTracker.COMPLETE ) {

//                                    gotMediaFile = true;

                                    carrier = new ImageDataCarrier( icon.getImage(), file );

                                    ObtuseUtil.doNothing();

                                } else {

                                    carrier = new FileDataCarrier( file, true );

                                    ObtuseUtil.doNothing();

                                }

//                                dataCarriers.add( carrier );

//                                        ImageReferenceItem imageReferenceItem = new ImageReferenceItem(
//                                                icon,
//                                                file.getAbsolutePath()
//                                        );
//
//                                        referenceItems.add( imageReferenceItem );

//                                    } else {
//
//                                        FileDataCarrier carrier = new FileDataCarrier( file, false );
//
//                                        dataCarriers.add( carrier );
////                                        throw new HowDidWeGetHereError(
////                                                "DataCarrierUtils.getDataCarriers:  " +
////                                                "unable to load image from " + mediaFileContents
////                                        );
//
////                                        FileReferenceItem fileReferenceItem = new FileReferenceItem(
////                                                file,
////                                                "DnD or CnP"
////                                        );
////
////                                        referenceItems.add( fileReferenceItem );

//                                }

                        } else {

                            // It either had no image type suffix
                            // or it had an image type suffix but did not contain an actual image.

                            // Treat it as a run-of-the-mill file.

                            carrier = new FileDataCarrier( file, true );

                            ObtuseUtil.doNothing();

                        }

//                            // If we didn't get an image out of the file then record it as a run-of-the-mill file.
//
//                            if ( !gotMediaFile ) {
//
//                                dataCarriers.add(
//                                        new FileDataCarrier( file, false )
//                                );
//
//                            }

//                        } else {
//
//                            carrier = new FileDataCarrier( file, true );
//
//                            ObtuseUtil.doNothing();
//
//                        }

                        dataCarriers.add( carrier );

                    } else if ( element == null ) {

                        throw new IllegalArgumentException(
                                "DataCarrierUtils.getFileListDatum:  element " + ix + " of javaFileListFlavor list is null"
                        );

                    } else {

                        throw new IllegalArgumentException(
                                "DataCarrierUtils.getFileListDatum:  " +
                                "element " + ix + " is something unexpected (" + element.getClass() + ")"
                        );

                    }

                }

                @SuppressWarnings("UnnecessaryLocalVariable")
//                    Optional<List<DataCarrier>> rval = Optional.of( new FormattingList<>( new FileListDataCarrier( list ) ) );
                Optional<List<DataCarrier>> rval = Optional.ofNullable( dataCarriers.isEmpty() ? null : dataCarriers );

                return rval;

            }

            throw new HowDidWeGetHereError(
                    "DataCarrierUtils.getFileListDatum:  " +
                    "request for a javaFileListFlavor worked but we didn't actually get a List, we got a " +
                    receivedObject.getClass().getCanonicalName()
            );

        }

        throw new HowDidWeGetHereError( "DataCarrierUtils.getFileListDatum:  expected a List, got nothing" );

    }

    @NotNull
    private static Optional<List<DataCarrier>> getStringDatum( final @NotNull Transferable transferable ) {

        Optional<Object> optValue;
        // Is there a string in the Transferable?

//            Logger.logMsg( "contemplating trying DataFlavor.stringFlavor" );

        Logger.logMsg( "DataFlavor.stringFlavor should work" );

        optValue = DndUtils.tryTransfer( transferable, DataFlavor.stringFlavor );

        Logger.logMsg( "stringFlavor got " + optValue );

        // If that worked then we're done.

        if ( optValue.isPresent() ) {

            Object receivedObject = optValue.get();
            if ( receivedObject instanceof String string ) {

                @SuppressWarnings("UnnecessaryLocalVariable")
                Optional<List<DataCarrier>> rval =
                        Optional.of( new FormattingList<>( new TextDataCarrier( "descriptive name", string ) ) );

                return rval;

            }

            throw new HowDidWeGetHereError(
                    "DataCarrierUtils.getDataCarriers:  " +
                    "request for a stringFlavor worked but we didn't actually get a String, we got a " +
                    receivedObject.getClass()
                                  .getCanonicalName()
            );

        }

        throw new HowDidWeGetHereError( "DataCarrierUtils.getDataCarriers:  expected a String, got nothing" );

    }
}
