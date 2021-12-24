package xyz.kenosee.poirot.ui;

import com.obtuse.exceptions.HowDidWeGetHereError;
import com.obtuse.ui.ObtuseImageFile;
import com.obtuse.util.Logger;
import com.obtuse.util.lrucache.CachedThing;
import com.obtuse.util.lrucache.LruCache;
import org.jetbrains.annotations.NotNull;
import xyz.kenosee.poirot.config.PoirotConfig;

import javax.swing.*;
import java.io.File;
import java.util.*;

// Poirot needs to be designed to work with a relatively modest number of images.
// Specifically, the goal should be to be able to cache all the images in memory at once
// in not more than a few gigabytes of memory.

/**
 Created by danny on 2021/11/27.
 */

public class MediaCache {

//    public static class Key implements ObtuseImageIdentifier {
//
//        private final UUID _uuid;
//        private final int _orientation;
//        private final int _hashCode;
//
//        public Key( @NotNull final UUID uuid, final int orientation ) {
//            super();
//
//            _uuid = uuid;
//            _orientation = orientation;
//
//            _hashCode = _uuid.hashCode() ^ Integer.hashCode( _orientation );
//
//        }
//
//        public int getOrientation() {
//
//            return _orientation;
//
//        }
//
//        public UUID getUuid() {
//
//            return _uuid;
//
//        }
//
//        @Override
//        public String format() {
//
//            return "IdAndOrientation";
//
//        }
//
//        @Override
//        public int compareTo( @NotNull final ObtuseImageIdentifier o ) {
//
//            int comparison = _uuid.compareTo( ((Key)o)._uuid );
//            if ( comparison == 0 ) {
//
//                comparison = Integer.compare( _orientation, ((Key)o)._orientation );
//
//                ObtuseUtil.doNothing();
//
//            } else {
//
//                ObtuseUtil.doNothing();
//
//            }
//
//            return comparison;
//
//        }
//
//        public int hashCode() {
//
//            return _hashCode;
//
//        }
//
//        public boolean equals( Object rhs ) {
//
//            return rhs instanceof Key && compareTo( (Key)rhs ) == 0;
//
//        }
//
//        public String toString() {
//
//            return "MediaCache.Key( " + _uuid + ", " + _orientation + " )";
//
//        }
//
//    }

    private static SortedMap<UUID, ObtuseImageFile> s_knownImageFiles = new TreeMap<>();

//    public static class ImagesBySize {
//
//        public static final int ICON_SIZE = 64;
//        private final int _onDiskSize;
//        private final SortedSet<Integer> _supportedSizes = new TreeSet<>();
//        private final SortedMap<Integer,ImageIcon> _imagesBySize = new TreeMap<>();
//
//        public ImagesBySize( final int onDiskSize, @NotNull final ImageIcon imageIcon ) {
//            super();
//
//            _onDiskSize = onDiskSize;
//            _imagesBySize.put( onDiskSize, imageIcon );
//
//        }
//
//        public Optional<ImageIcon> getImageBySize( int size, boolean mandatory ) {
//
//            ImageIcon imageIcon = _imagesBySize.get( size );
//        }
//    }


    /**
     The image cache contains images in the orientation that they have on disk.
     <p>We will probably eventually need to provide a way for the user to rotate and/or flip the
     on-disk copy of the image.</p>
     */

    @SuppressWarnings("FieldNamingConvention")
    private static final LruCache<UUID, ImageIcon> s_imageCache;

//    private static final LruCache.Fetcher<Key, ImageIcon> s_rotatedImageFetcher;
    private static final LruCache.Fetcher<UUID, ImageIcon> s_imageFetcher;

    static {

        ObtuseImageFile.setImageRepositoryFile(
                PoirotConfig.POIROT_IMAGE_REPOSITORY,
                true
        );

        s_imageFetcher = new LruCache.Fetcher<>() {

            @Override
            public @NotNull Optional<CachedThing<UUID,ImageIcon>> fetch(
                    final @NotNull UUID uuid,
                    final boolean nullOk
            ) {

                ObtuseImageFile oif = s_knownImageFiles.get( uuid );
                if ( oif == null ) {

                    return Optional.empty();

                }

                File imageBinfoFile = ObtuseImageFile.constructCachedImagRepositoryFileObject(
                        uuid,
                        ObtuseImageFile.PERM_OBTUSE_IMAGE_INFO_FILENAME_SUFFIX
                );

//                String imageFileBaseName =
//                        ObtuseImageFile.constructCachedImagRepositoryFileObject(
//                                key,
//                                ""
//                        ).getAbsolutePath();

                for ( String suffix : new String[] { "jpg", "png", "tif" } ) {

//                    fullSizeImageFile = new File( imageFileBaseName + "." + suffix );
                    File fullSizeImageFile = ObtuseImageFile.constructCachedImagRepositoryFileObject(
                            uuid,
                            "." + suffix
                    );

                    if ( fullSizeImageFile.isFile() ) {

                        ImageIcon fullSizeImage = new ImageIcon( fullSizeImageFile.getAbsolutePath() );

                        if ( fullSizeImage.getImage() == null ) {

                            throw new HowDidWeGetHereError( "need to figure out what to do if we cannot load the image file" );

                        }

                        @SuppressWarnings("UnnecessaryLocalVariable")
                        Optional<CachedThing<UUID, ImageIcon>> rval = Optional.of(
                                new CachedThing<>(
                                        uuid,
                                        fullSizeImage
                                )
                        );

                        return rval;

                    }

                }

                return Optional.empty();

//                @NotNull Optional<CachedThing<Key, ImageIcon>> optFullSizeImage = s_imageFetcher.fetch( key, nullOk );
//                if ( optFullSizeImage.isPresent() ) {
//
//                    CachedThing<Key, ImageIcon> cachedThing = optFullSizeImage.get();
//                    ImageIcon fullSizeImage = cachedThing.getThing();
//                    ImageIcon rotatedImage;
//                    if ( key.getOrientation() == 1 ) {
//
//                        rotatedImage = fullSizeImage;
//
//                    } else {
//
//                        rotatedImage = ObtuseImageUtils.rotateImage(
//                                fullSizeImage,
//                                key.getOrientation()
//                        );
//
//                    }
//
//                    Optional<ImageIcon> optScaledImageIcon = ObtuseImageUtils.getScaledImageIcon(
//                            "PoirotImageViewerPanel.LruCache.fetcher",
//                            rotatedImage,
//                            key.getScaleGranularity()
//                    );
//
//                    return optScaledImageIcon.map( imageIcon -> new CachedThing<>( key, imageIcon ) );
//
//                } else {
//
//                    return Optional.empty();
//
//                }

            }

        };

//        s_unrotatedImageFetcher = new LruCache.Fetcher<>() {
//            @Override
//            public @NotNull Optional<CachedThing<Key, ImageIcon>> fetch(
//                    final @NotNull Key key,
//                    final boolean nullOk
//            ) {
//
//                @NotNull Optional<CachedThing<Key, ImageIcon>> optFullSizeImage = s_unrotatedImageFetcher.fetch( key, nullOk );
//                if ( optFullSizeImage.isPresent() ) {
//
//                    CachedThing<Key, ImageIcon> cachedThing = optFullSizeImage.get();
//                    ImageIcon fullSizeImage = cachedThing.getThing();
//                    ImageIcon rotatedImage;
//                    if ( key.getOrientation() == 1 ) {
//
//                        rotatedImage = fullSizeImage;
//
//                    } else {
//
//                        rotatedImage = ObtuseImageUtils.rotateImage(
//                                fullSizeImage,
//                                key.getOrientation()
//                        );
//
//                    }
//
//                    Optional<ImageIcon> optScaledImageIcon = ObtuseImageUtils.getScaledImageIcon(
//                            "PoirotImageViewerPanel.LruCache.fetcher",
//                            rotatedImage,
//                            key.getScaleGranularity()
//                    );
//
//                    return optScaledImageIcon.map( imageIcon -> new CachedThing<>( key, imageIcon ) );
//
//                } else {
//
//                    return Optional.empty();
//
//                }
//
//            }
//
//        };

        s_imageCache = new LruCache<>(
                "image cache for PoirotImageViewerPanel class",
                100,
                s_imageFetcher
        );

//        LruCache.Fetcher<Key, ImageIcon> unrotatedImageFetcher = new LruCache.Fetcher<>() {
//            @Override
//            public @NotNull Optional<CachedThing<Key, ImageIcon>> fetch(
//                    final @NotNull Key key,
//                    final boolean nullOk
//            ) {
//
//                Optional<CachedThing<Key, ImageIcon>> optFullSizeImage =
//                        s_imageCache.getOptional( key );
//                if ( optFullSizeImage.isPresent() ) {
//
//                    CachedThing<ObtuseImageIdentifier, ImageIcon> cachedThing = optFullSizeImage.get();
//                    ImageIcon fullSizeImage = cachedThing.getThing();
//                    ImageIcon rotatedImage;
//                    if ( key.getOrientation() == 1 ) {
//
//                        rotatedImage = fullSizeImage;
//
//                    } else {
//
//                        rotatedImage = ObtuseImageUtils.rotateImage(
//                                fullSizeImage,
//                                key.getOrientation()
//                        );
//
//                    }
//
//                    Optional<ImageIcon> optScaledImageIcon = ObtuseImageUtils.getScaledImageIcon(
//                            "PoirotImageViewerPanel.LruCache.fetcher",
//                            rotatedImage,
//                            key.getScaleGranularity()
//                    );
//
//                    return optScaledImageIcon.map( imageIcon -> new CachedThing<>( key, imageIcon ) );
//
//                } else {
//
//                    return Optional.empty();
//
//                }
//
//            }
//
//        };
//
//        s_imageCache = new LruCache<>(
//                "image cache for PoirotImageViewerPanel class",
//                100,
//                fetcher
//        );

    }

    public static Optional<ObtuseImageFile> getOptObtuseImageFile( int key ) {

        ObtuseImageFile oif = s_knownImageFiles.get( key );

        if ( oif == null ) {

            return Optional.empty();

        } else {

            Logger.logMsg( "MediaCache.getOptObtuseImageFile:  got the binfo file for sn " + key );

            return Optional.of( oif );

        }

    }

    public static Optional<ImageIcon> getOptImageIcon( @NotNull UUID uuid ) {

        Optional<CachedThing<UUID, ImageIcon>> rval = s_imageCache.getOptional( uuid );
        if ( rval.isPresent() ) {

            Logger.logMsg( "MediaCache.getOptImageIcon:  got the image icon for sn " + uuid );

            return Optional.of( rval.get().getThing() );

        } else {

            return Optional.empty();

        }

    }

}
