package xyz.kenosee.poirot.ui.dnd;

import com.obtuse.exceptions.HowDidWeGetHereError;
import com.obtuse.util.FormattingList;
import com.obtuse.util.Logger;
import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.kenosee.poirot.data.ImageReferenceItem;
import xyz.kenosee.poirot.data.ReferenceItem;
import xyz.kenosee.poirot.data.TextReferenceItem;
import xyz.kenosee.poirot.util.ImageMediaFileFetcher;
import xyz.kenosee.poirot.util.MediaFileContents;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;

/**
 Created by danny on 2021/12/21.
 */
public class ImageDataCarrier extends DataCarrier {

    // We hold a strong reference to images that arrived via DnD or CnP.
    // We hold a weak reference to images that came from files since we can get them again later if we need them.
    // If _strongImageReference is null then neither _weakImageReference nor _optFile will be null (note that
    // the non-null _weakImageReference might contain a null reference).
    // If _strongImageReference is not null then both _weakImageReference and _optFile will be null.

    private final Image _strongImageReference;

    private WeakReference<Image> _weakImageReference;

    /**
     If the image came from a file then we keep track of the file.
     */

    private final File _optFile;

    public ImageDataCarrier( @NotNull final Image image ) {

        super();

        _strongImageReference = image;
        _optFile = null;

    }

    public ImageDataCarrier( @NotNull final Image image, @NotNull final File file ) {

        super();

        _strongImageReference = null;
        cacheWeakImageReference( image );

        _optFile = file;

    }

    public void cacheWeakImageReference( @Nullable final Image image ) {

        _weakImageReference = new WeakReference<>( image );

    }

    @Nullable
    public Image getOptionalData() {

        if ( _strongImageReference == null ) {

            Image tmpImageReference = _weakImageReference.get();
            if ( tmpImageReference == null ) {

                ObtuseUtil.doNothing();

                return null;

            } else {

                ObtuseUtil.doNothing();

                return tmpImageReference;

            }

        } else {

            return _strongImageReference;

        }

    }

    /**
     Do what it takes to get and return this file's image.

     @return This carrier's image.
     @throws ImageFetchFailedException if the image is not already loaded and could not be loaded on demand.
     */

    @NotNull
    public Image getOrFetchImage() throws ImageFetchFailedException {

        if ( _strongImageReference == null ) {

            Image tmpImageReference = _weakImageReference.get();

            if ( tmpImageReference == null ) {

                @NotNull MediaFileContents mediaFileContents =
                        ImageMediaFileFetcher.fetchMediaFile( _optFile );

                if ( mediaFileContents.worked() ) {

                    ImageIcon icon = new ImageIcon(
                            mediaFileContents.getImageFileBytes()
                    );

                    if ( icon.getImageLoadStatus() == MediaTracker.COMPLETE ) {

                        tmpImageReference = icon.getImage();
                        _weakImageReference = new WeakReference<>( tmpImageReference );

                    } else {

                        // The contents of the media file are not a valid image.

                        throw new ImageFetchFailedException(
                                "ImageDataCarrier.getOrFetchImage:  " + ObtuseUtil.enquoteJavaObject( _optFile ) +
                                " does not contain a valid image",
                                ImageFetchFailedException.FailureCause.NOT_AN_IMAGE
                        );

                    }

                } else {

                    // We could not fetch the contents of the media file.

                    throw new ImageFetchFailedException(
                            "FileDataCarrier:  unable to load contents of " +
                            ObtuseUtil.enquoteJavaObject( _optFile ) +
                            " (" + mediaFileContents.getMandatoryErrorMessage() + ")",
                            ImageFetchFailedException.FailureCause.CANNOT_READ_IMAGE_FILE
                    );

                }

            }

            return tmpImageReference;

        } else {

            return _strongImageReference;

        }

    }

    private static Optional<ReferenceItem> createAndSaveImageReferenceItem(
            @NotNull final java.util.List<ReferenceItem> referenceItems,
            @NotNull final ImageIcon icon,
            @NotNull final String sourceText
    ) {

        try {

            ImageReferenceItem imageReferenceItem = new ImageReferenceItem(
                    icon,
                    sourceText
                    //                    file.getAbsolutePath()
            );

            referenceItems.add( imageReferenceItem );

            return Optional.of( imageReferenceItem );

        } catch ( ImageFetchFailedException e ) {

            Logger.logMsg(
                    "PoirotJListTransferHandler.getReferenceItems:  " +
                    ObtuseUtil.enquoteToJavaString( sourceText ) + " does not contain a valid image " +
                    "(informational,fc=" + e.getFailureCause() + ")"
            );

            ObtuseUtil.doNothing();

            return Optional.empty();

        }

    }

    public void createReferenceItem(
            final java.util.List<ReferenceItem> referenceItems
    ) {

        Image image = null;
        try {

            image = getOrFetchImage();

        } catch ( ImageFetchFailedException e ) {

            // This will do for now . . .

            // I have a feeling that we are headed towards images always being loaded
            // if they have a reference item instance. 2012-12-21

            throw new HowDidWeGetHereError( "xyz.kenosee.poirot.ui.dnd.ImageFetchFailedException caught", e );

        }

        ImageIcon icon = new ImageIcon( image );
        if ( icon.getImageLoadStatus() == MediaTracker.COMPLETE ) {

            Optional<ReferenceItem> optImageReferenceItem = createAndSaveImageReferenceItem(
                    referenceItems,
                    icon,
                    "DnD or CnP"
            );

            ObtuseUtil.doNothing();

            if ( optImageReferenceItem.isEmpty() ) {

                TextReferenceItem textReferenceItem = new TextReferenceItem(
                        "<unknown>",
                        "DnD or CnP",
                        "see createReferenceItem in ImageDataCarrier"
                );

                referenceItems.add( textReferenceItem );

            }

        } else {

            throw new HowDidWeGetHereError(
                    "ImageDataCarrier.createDataCarrier:  we were given an image that we could not wrap in an " +
                    "IconImage" );

        }

    }

    @NotNull
    public static Optional<java.util.List<ReferenceItem>> getReferenceItems( @NotNull final Transferable transferable ) {

        Optional<java.util.List<DataCarrier>> optDataCarriers = DataCarrierUtils.getDataCarriers( transferable );

        if ( optDataCarriers.isPresent() ) {

            java.util.List<DataCarrier> dataCarriers = optDataCarriers.get();
            List<ReferenceItem> referenceItems = new FormattingList<>();

            for ( DataCarrier dc : dataCarriers ) {

                switch ( dc.getDataClass() ) {

                    case IMAGE -> {

                        //                        @SuppressWarnings("unchecked") List<Image> images = (List<Image>)dc
                        //                        .getData();
                        //                        for ( Image image : images ) {

                        Optional<ReferenceItem> optImageReferenceItem = Optional.empty();

                        xyz.kenosee.poirot.ui.dnd.ImageDataCarrier idc = (xyz.kenosee.poirot.ui.dnd.ImageDataCarrier)dc;

                        dc.createReferenceItem( referenceItems );

                        //                        try {
                        //
                        //                            ImageReferenceItem imageReferenceItem =
                        //                                    new ImageReferenceItem(
                        //                                            new ImageIcon( image ),
                        //                                            "DnD or CnP"
                        //                                    );
                        //
                        //                            referenceItems.add( imageReferenceItem );
                        //
                        //                        } catch ( ImageFetchFailedException e ) {
                        //
                        //                            Logger.logErr( "PoirotJListTransferHandler.getReferenceItems:
                        //                            unable to create ImageIcon", e );
                        //
                        //                            ObtuseUtil.doNothing();
                        //
                        //                        }

                    }

                    case FILE -> {

                        Optional<ReferenceItem> optImageReferenceItem = Optional.empty();

                        FileDataCarrier fdc = (FileDataCarrier)dc;

                        dc.createReferenceItem( referenceItems );

                    }

                    case TEXT -> {


                        dc.createReferenceItem( referenceItems );

                    }

                }

            }

            return Optional.of( referenceItems );

        }

        return Optional.empty();

    }

//        @NotNull
//        public Optional<Image> getData() {
//
//            return _image;
//
//        }

    @NotNull
    public DataClass getDataClass() {

        return DataClass.IMAGE;

    }

}
