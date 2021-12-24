package xyz.kenosee.poirot.ui.dnd.old;

import com.obtuse.exceptions.HowDidWeGetHereError;
import com.obtuse.util.Logger;
import com.obtuse.util.ObtuseCollections;
import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;
import xyz.kenosee.poirot.data.FileReferenceItem;
import xyz.kenosee.poirot.data.ImageReferenceItem;
import xyz.kenosee.poirot.data.ReferenceItem;
import xyz.kenosee.poirot.data.TextReferenceItem;
import xyz.kenosee.poirot.ui.dnd.DndUtils;
import xyz.kenosee.poirot.ui.dnd.ImageFetchFailedException;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 A class that provides support for importing data of various flavours.
 @deprecated
 */

@Deprecated
abstract class PoirotDataFlavorHandler {

    private static final List<PoirotDataFlavorHandler> s_supportedDataFlavors = new ArrayList<>();

    public static PoirotDataFlavorHandler oldImageFlavor =
            new PoirotDataFlavorHandler( DataFlavor.imageFlavor, "imageFlavor" ) {

                public Optional<List<? extends ReferenceItem>> createReferenceItemManager(
                        final TransferHandler.TransferSupport info
                ) throws IOException, UnsupportedFlavorException {

                    Transferable transferable = info.getTransferable();
//                    explainPlease( this, transferable );

                    // This is a bit wonky (actually, it is a LOT wonky) but . . .

                    // First try to get it as an image.
                    // If that doesn't work, try to get it as a file list.
                    // If that doesn't work, there is no plan C so bail out.

                    Optional<Object> value;

                    Logger.logMsg( "trying DataFlavor.imageFlavor" );
                    Logger.logMsg(
                            "DataFlavor.imageFlavor should " +
                            ( transferable.isDataFlavorSupported( DataFlavor.imageFlavor ) ? "" : "not " ) +
                            "work"
                    );
                    value = DndUtils.tryTransfer( transferable, DataFlavor.imageFlavor );
                    Logger.logMsg( "imageFlavor got " + value );

                    if ( value.isPresent() ) {

                        if ( value.get() instanceof Image imageValue ) {

                            ArrayList<ImageReferenceItem> imageReferenceItems = null;
                            try {
                                imageReferenceItems = ObtuseCollections.arrayList(
                                        new ImageReferenceItem( new ImageIcon( imageValue ), "DnD or CnP" )
                                );
                            } catch ( ImageFetchFailedException e ) {

                                Logger.logErr( "xyz.kenosee.poirot.ui.dnd.ImageFetchFailedException caught", e );

                                ObtuseUtil.doNothing();

                            }

                            return Optional.of( imageReferenceItems );

                        } else {

                            throw new HowDidWeGetHereError(
                                    "PoirotDataFlavorHandler.imageFlavor.createReferenceItemManager:  " +
                                    "got strange thing from tryTransfer - " + value.get()
                            );

                        }

                    }

                    if ( ObtuseUtil.always() ) {

                        return PoirotDataFlavorHandler.oldJavaFileListFlavor.createReferenceItemManager( info );

                    }

                    Logger.logMsg( "trying DataFlavor.javaFileListFlavor" );
                    Logger.logMsg(
                            "DataFlavor.javaFileListFlavor should " +
                            ( transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor ) ? "" : "not " ) +
                            "work"
                    );
                    value = DndUtils.tryTransfer( transferable, DataFlavor.javaFileListFlavor );
                    Logger.logMsg( "javaFileListFlavor got " + value );





                    Logger.logMsg( "trying DataFlavor.stringFlavor" );
                    Logger.logMsg(
                            "DataFlavor.stringFlavor should " +
                            ( transferable.isDataFlavorSupported( DataFlavor.stringFlavor ) ? "" : "not " ) +
                            "work"
                    );
                    value = DndUtils.tryTransfer( transferable, DataFlavor.stringFlavor );
                    Logger.logMsg( "stringFlavor got " + value );

                    value = DndUtils.tryTransfer( transferable, DataFlavor.imageFlavor );

                    if ( value.isPresent() && value.get() instanceof Image imageValue ) {

                        ArrayList<ImageReferenceItem> imageReferenceItems = null;
                        try {
                            imageReferenceItems = ObtuseCollections.arrayList(
                                    new ImageReferenceItem( new ImageIcon( imageValue ), "DnD or CnP" )
                            );
                        } catch ( ImageFetchFailedException e ) {

                            Logger.logErr( "xyz.kenosee.poirot.ui.dnd.ImageFetchFailedException caught", e );

                            ObtuseUtil.doNothing();

                        }

                        return Optional.of( imageReferenceItems );

                    }

                    throw new HowDidWeGetHereError(
                            "PoirotDataFlavorHandler.imageFlavor:  " +
                            "expected to be able to get an Image from handler which supports " +
                            DndUtils.formatSupportedMimeTypes( info )
                    );

                }


            };

    //    static {
//
//        for ( DataFlavor supportedFlavor : transferDataFlavors ) {
//
//            Logger.logMsg(
//                    "    supported data flavour:  " + supportedFlavor +
//                    " (" + supportedFlavor.getHumanPresentableName() + ")" +
//                    " (" + supportedFlavor.getRepresentationClass() + ")" );
//
//            ObtuseUtil.doNothing();
//
//            if ( "text/uri-list".equals( supportedFlavor.getHumanPresentableName() ) ) {
//
//                Class<?> representationClass = supportedFlavor.getRepresentationClass();
//                if ( String.class.equals( representationClass ) ) {
//                    Logger.logMsg( "hello String class" );
//                } else if ( String[].class.equals( representationClass ) ) {
//                    Logger.logMsg( "hello String array class" );
//                }if ( Character.class.equals( representationClass ) ) {
//                    Logger.logMsg( "hello Character class" );
//                } else if ( Character[].class.equals( representationClass ) ) {
//                    Logger.logMsg( "hello Character array class" );
//                }
//                Logger.logMsg( "representation of " + "text/uri-list" + " is " + representationClass );
//
//                if ( String.class.equals( representationClass ) ) {
//
//                    try {
//                        Logger.logMsg( "trying to get URI list as a string array" );
//                        Object xxx = transferable.getTransferData( supportedFlavor );
//                        Logger.logMsg( "xxx is a " + xxx.getClass() );
//
//                        ObtuseUtil.doNothing();
//
//                    } catch ( UnsupportedFlavorException e ) {
//
//                        Logger.logErr( "java.awt.datatransfer.UnsupportedFlavorException caught", e );
//
//                        ObtuseUtil.doNothing();
//
//                    } catch ( IOException e ) {
//
//                        Logger.logErr( "java.io.IOException caught", e );
//
//                        ObtuseUtil.doNothing();
//
//                    } catch ( Throwable e ) {
//
//                        Logger.logErr( "java.lang.Throwable caught", e );
//
//                        ObtuseUtil.doNothing();
//
//                    }
//
//                }
//
//            }
//
//        }
//
//    }

    /**
     Explore what sort of things the three {@link DataFlavor}s of interest to Poirot can provide.
     <p>It turns out that the answer is fairly simple:
     <ul>
     <li>If the source gave us a single image then our first handler will be the
     {@link PoirotDataFlavorHandler#oldImageFlavor} version and it will contain the provided image.</li>
     <li>If the source gave us a single image then our first handler will be the
     {@link PoirotDataFlavorHandler#oldImageFlavor} version and it will contain the provided image.</li>
     </ul>
     <ul>
     <li>{@link DataFlavor#stringFlavor} represents and provides representations of simple strings.
     Getting involved with its variant flavours seems to be a total waste of time.</li>
     <li>{@link DataFlavor#imageFlavor} only provides representations of simple strings.
     Getting fancy with its variant flavours seems to be a total waste of time.</li>
     <li>{@link DataFlavor#stringFlavor} only provides representations of simple strings.
     Getting fancy with its variant flavours seems to be a total waste of time.</li>
     application/x-java-file-list provides a List
     </ul>
     </p>
     @param poirotDataFlavorHandler
     @param transferable
     @throws UnsupportedFlavorException
     @throws IOException
     */

    private static void explainPlease(
            final PoirotDataFlavorHandler poirotDataFlavorHandler,
            final Transferable transferable
    ) throws UnsupportedFlavorException, IOException {

        Logger.logMsg( "#" );
        Logger.logMsg( "#" );
        Logger.logMsg( "#" );
        Logger.logMsg( "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" );

        try {

            DataFlavor[] transferDataFlavors = transferable.getTransferDataFlavors();
            Logger.logMsg(
                    poirotDataFlavorHandler.getFlavorName() + ".transferable data flavours are " +
                    Arrays.toString( transferDataFlavors )
            );

            for ( DataFlavor supportedFlavor : transferDataFlavors ) {

                Logger.logMsg( "--" );

                Logger.logMsg(
                        "    supported data flavour:  " + supportedFlavor +
                        " (" + supportedFlavor.getHumanPresentableName() + ")" +
                        " (" + supportedFlavor.getRepresentationClass() + ")" );

                ObtuseUtil.doNothing();

                if ( true /*"text/uri-list".equals( supportedFlavor.getHumanPresentableName() )*/ ) {

                    Class<?> representationClass = supportedFlavor.getRepresentationClass();
                    if ( String.class.equals( representationClass ) ) {
                        Logger.logMsg( "hello String class" );
                    } else if ( String[].class.equals( representationClass ) ) {
                        Logger.logMsg( "hello String array class" );
                    }if ( Character.class.equals( representationClass ) ) {
                        Logger.logMsg( "hello Character class" );
                    } else if ( Character[].class.equals( representationClass ) ) {
                        Logger.logMsg( "hello Character array class" );
                    } else if (
//                            representationClass.isAssignableFrom( Collection.class )
//                            ||
                            Collection.class.isAssignableFrom( representationClass )
                    ) {

                        Logger.logMsg(
                                "hello Collection class (" +
                                representationClass.isAssignableFrom( Collection.class ) +
                                "," +
                                Collection.class.isAssignableFrom( representationClass ) +
                                ")"
                        );
                        Object list = transferable.getTransferData( supportedFlavor );
                        Logger.logMsg( "collection is a " + list.getClass() );
                        for ( Object obj : (Collection)list ) {

                            Logger.logMsg(
                                    "element is " +
                                    ( obj == null ? "null" : ( "a " + obj.getClass() + ", value=" + obj ) )
                            );

                        }

                    } else {

                        Logger.logMsg( "other " + representationClass );

                    }

                    Logger.logMsg( "representation of " + supportedFlavor.getHumanPresentableName() + " is " + representationClass );

                    if ( String.class.equals( representationClass ) ) {

                        try {

                            Logger.logMsg( "trying to get URI list as a string array" );
                            Object xxx = transferable.getTransferData( supportedFlavor );
                            Logger.logMsg( "xxx is a " + xxx.getClass() );

                            ObtuseUtil.doNothing();

                        } catch ( UnsupportedFlavorException e ) {

                            Logger.logErr(
                                    "tryTransfer:  " +
                                    "java.awt.datatransfer.UnsupportedFlavorException caught for flavor " +
                                    supportedFlavor.getHumanPresentableName(),
                                    e
                            );

                            ObtuseUtil.doNothing();

                        } catch ( IOException e ) {

                            Logger.logErr( "java.io.IOException caught", e );

                            ObtuseUtil.doNothing();

                        } catch ( Throwable e ) {

                            Logger.logErr( "java.lang.Throwable caught", e );

                            ObtuseUtil.doNothing();

                        }

                    }

                }

            }

        } finally {

            Logger.logMsg( ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" );
            Logger.logMsg( "#" );
            Logger.logMsg( "#" );
            Logger.logMsg( "#" );

        }

//        DataFlavor xxx = DataFlavor.selectBestTextFlavor(  )
//        if ( transferable.isDataFlavorSupported( DataFlavor ))

// This sometimes explodes with an UnsupportedFlavorException
//        Object v1 = transferable.getTransferData( DataFlavor.stringFlavor );
//        Logger.logMsg( "image.v1=" + ObtuseUtil.enquoteJavaObject( v1 ) );

        ObtuseUtil.doNothing();

    }

//    public static PoirotDataFlavorHandler imageFlavor =
//            new PoirotDataFlavorHandler( DataFlavor.imageFlavor, "imageFlavor" ) {
//
//        public Optional<List<? extends ReferenceItem>> createReferenceItemManager( final TransferHandler.TransferSupport info )
//                        throws IOException, UnsupportedFlavorException {
//
//            throw new HowDidWeGetHereError( "unsupported " + DataFlavor.imageFlavor );
//
////            Object value = info.getTransferable()
////                               .getTransferData( DataFlavor.imageFlavor );
////
////            if ( value instanceof String ) {
////
////                String stringValue = value.toString();
////                return new ReferenceItem( stringValue );
////
////            }
////
////            throw new HowDidWeGetHereError(
////                    "PoirotDataFlavorHandler.stringFlavor:  " +
////                    "expected to be able to get a String from handler which supports " +
////                    PoirotJListTransferHandler.formatSupportedMimeTypes( info )
////            );
//
//        }
//
//    };

    public static PoirotDataFlavorHandler oldJavaFileListFlavor =
            new PoirotDataFlavorHandler( DataFlavor.javaFileListFlavor, "javaFileListFlavor" ) {

                public Optional<List<? extends ReferenceItem>> createReferenceItemManager( final TransferHandler.TransferSupport info )
                        throws IOException, UnsupportedFlavorException {

                    Transferable transferable = info.getTransferable();
//                    explainPlease( this, transferable );
                    Object value = transferable.getTransferData( DataFlavor.javaFileListFlavor );

                    ObtuseUtil.doNothing();

                    if ( value instanceof Collection<?> inBoundCollection ) {

                        ArrayList<File> files = new ArrayList<>(); // new ArrayList<>( (Collection<File>)value );
                        for ( Object inBoundObject : inBoundCollection ) {

                            if ( inBoundObject instanceof File ) {

                                files.add( (File)inBoundObject );

                            } else {

                                throw new ClassCastException(
                                        "PoirotDataFlavorHandler.javaFileListFlavor.createReferenceItemManager:  " +
                                        "inbound javaFileListFlavor contains a non-File " +
                                        "(it is a " + inBoundObject.getClass().getCanonicalName() + ")"
                                );

                            }

                        }

                        ArrayList<FileReferenceItem> fileReferenceItems = new ArrayList<>();
                        for ( File file : files ) {

                            FileReferenceItem item = new FileReferenceItem(
                                    file,
                                    "DnD or CnP"
                            );
                            fileReferenceItems.add( item );

                        }

                        return Optional.of( fileReferenceItems );

                    }

                    throw new HowDidWeGetHereError( "unexpected " + DataFlavor.javaFileListFlavor );

//                    if ( value instanceof String ) {
//
//                        String stringValue = value.toString();
//                        return Optional.of( ObtuseCollections.arrayList( new ReferenceItem( stringValue ) ) );
//
//                    }
//
//                    throw new HowDidWeGetHereError(
//                            "PoirotDataFlavorHandler.stringFlavor:  " +
//                            "expected to be able to get a String from handler which supports " +
//                            PoirotJListTransferHandler.formatSupportedMimeTypes( info )
//                    );

                }

            };

    public static PoirotDataFlavorHandler oldStringFlavor =
            new PoirotDataFlavorHandler( DataFlavor.stringFlavor, "stringFlavor" ) {

                public Optional<List<? extends ReferenceItem>> createReferenceItemManager(
                        final TransferHandler.TransferSupport info
                ) throws IOException, UnsupportedFlavorException {

                    Transferable transferable = info.getTransferable();
//                    explainPlease( this, transferable );
                    Object value = transferable
                        .getTransferData( DataFlavor.stringFlavor );

                    if ( value instanceof String stringValue ) {

                        ArrayList<TextReferenceItem> textReferenceItems = ObtuseCollections.arrayList(
                                new TextReferenceItem( "see oldStringFlavor in PoirotDataFlavorHandler", stringValue, "DnD or CnP" )
                        );

                        return Optional.of( textReferenceItems );

                    }

                    throw new HowDidWeGetHereError(
                            "PoirotDataFlavorHandler.stringFlavor:  " +
                            "expected to be able to get a String from handler which supports " +
                            DndUtils.formatSupportedMimeTypes( info )
                    );

                }

            };

    private final DataFlavor _supportedDataFlavor;
    private final String _flavorName;

    public PoirotDataFlavorHandler(
            @NotNull final DataFlavor supportedDataFlavor,
            final String flavorName
    ) {
        super();

        _supportedDataFlavor = supportedDataFlavor;
        _flavorName = flavorName;

        s_supportedDataFlavors.add( this );

    }

    public DataFlavor getSupportedDataFlavor() {

        return _supportedDataFlavor;

    }

    private static boolean s_verbose_find = false;

    @NotNull
    public static Optional<PoirotDataFlavorHandler> findDataFlavorHandler(
            @NotNull final TransferHandler.TransferSupport info
    ) {

//        Logger.logMsg( "PoirotDataFlavorHandler.findDataFlavorHandler:  support says it can handle " + PoirotJListTransferHandler.formatSupportedMimeTypes( support ) );
//        Logger.logMsg( "PoirotDataFlavorHandler.findDataFlavorHandler:  checking if we are being handed something it support" );

        PoirotDataFlavorHandler firstHandler = null;

//        for ( PoirotDataFlavorHandler poirotDataFlavorHandler : s_supportedDataFlavors ) {
//
//        }

        for ( PoirotDataFlavorHandler poirotDataFlavorHandler : s_supportedDataFlavors ) {

            if ( s_verbose_find ) {
                Logger.logMsg( "---" );
                Logger.logMsg( "looking at flavor " + poirotDataFlavorHandler );
                Logger.logMsg(
                        "PoirotDataFlavorHandler.findDataFlavorHandler:  " +
                        poirotDataFlavorHandler.getFlavorName() +
                        " handler " +
                        "is " + poirotDataFlavorHandler
                );
            }

            DataFlavor dataFlavor = poirotDataFlavorHandler.getSupportedDataFlavor();

//            Logger.logMsg( "PoirotDataFlavorHandler.findDataFlavorHandler:  checking " + dataFlavor.getHumanPresentableName() + " with " + poirotDataFlavorHandler.getFlavorName() + " w.r.t. " + PoirotJListTransferHandler.formatSupportedMimeTypes( support ) );

            if ( info.isDataFlavorSupported( dataFlavor ) ) {

                Logger.logMsg(
                        "PoirotDataFlavorHandler.findDataFlavorHandler:  " +
                        "found " + dataFlavor.getHumanPresentableName() + " which is " + dataFlavor
                );

                if ( s_verbose_find ) {

                    if ( firstHandler == null ) {

                        firstHandler = poirotDataFlavorHandler;

                    } else {

                        ObtuseUtil.doNothing();

                    }

                } else {

                    return Optional.of( poirotDataFlavorHandler );

                }

            }

        }

        if ( firstHandler != null ) {

            Logger.logMsg(
                    "PoirotDataFlavorHandler.findDataFlavorHandler:  returning " + firstHandler.getFlavorName() + " which supports " +
                    DndUtils.formatSupportedMimeTypes( info ) );

            Logger.logMsg( "!!!" );

            return Optional.of( firstHandler );

        }

        Logger.logMsg( "PoirotDataFlavorHandler.findDataFlavorHandler:  we do not support any of the provided flavors" );

        Logger.logMsg( "???" );

        return Optional.empty();

    }

    public String toString() {

        return "PoirotDataFlavorHandler( " + _supportedDataFlavor.getHumanPresentableName() + " )";

    }

    public abstract Optional<List<? extends ReferenceItem>> createReferenceItemManager(
            final TransferHandler.TransferSupport info
    ) throws IOException, UnsupportedFlavorException;

    public String getFlavorName() {

        return _flavorName;

    }

}
