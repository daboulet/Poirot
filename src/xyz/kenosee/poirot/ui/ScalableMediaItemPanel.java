package xyz.kenosee.poirot.ui;

import com.obtuse.ui.ObtuseImageIdentifier;
import com.obtuse.ui.ObtuseImageUtils;
import com.obtuse.util.ImageIconUtils;
import com.obtuse.util.Logger;
import com.obtuse.util.lrucache.CachedThing;
import com.obtuse.util.lrucache.LruCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

/**
 A customized {@link JLabel} for displaying images.
 */

public class ScalableMediaItemPanel<IID extends ObtuseImageIdentifier> extends JLabel {

//    private final LruCache<ScaledImageId<IID>, ImageIcon> _imageIconCache;

    private final @Nullable ImageIcon _fullSizeImage;
    private final IID _imageIdentifier;
    private int _orientation;

    private final int _scaleGranularity;
    private final Dimension _naturalSize;
    @NotNull private Dimension _currentSize;
    @Nullable private final ImageIcon _currentSizeImage;

    public ScalableMediaItemPanel(
            @Nullable final ImageIcon fullSizeImage,
            @NotNull final IID imageIdentifier,
            int scaleGranularity,
            int initialOrientation
    ) {
        super();

        _fullSizeImage = fullSizeImage;
        _imageIdentifier = imageIdentifier;
//        _imageIconCache = scaledImageIconCache;
        _orientation = initialOrientation;

        Logger.logMsg( "initial orientation is " + _orientation );

        setHorizontalAlignment( SwingConstants.LEFT );
        setVerticalAlignment( SwingConstants.TOP );

        if ( scaleGranularity <= 0 ) {

            throw new IllegalArgumentException( "SimpleMediaItemPanelBackup:  invalid scaleGranularity=" +
                                                scaleGranularity +
                                                " (must be a positive value)" );

        }

        _scaleGranularity = scaleGranularity;
        if ( fullSizeImage == null ) {

            _naturalSize = new Dimension( 100, 100 );

        } else {

            _naturalSize = new Dimension( fullSizeImage.getIconWidth(), fullSizeImage.getIconHeight() );

        }

        _currentSizeImage = fullSizeImage;
        _currentSize = new Dimension( _naturalSize );

    }

    @SuppressWarnings("unused")
    public ScalableMediaItemPanel(
            @Nullable final Image fullSizeImage,
            @NotNull final IID imageIdentifier,
            int scaleGranularity,
            int initialOrientation
    ) {
        this(
                fullSizeImage == null ? null : new ImageIcon( fullSizeImage ),
                imageIdentifier,
                scaleGranularity,
                initialOrientation
        );
    }

    public void setOrientation( int orientation, @SuppressWarnings("unused") final double zoomFactor ) {

        if ( _orientation != orientation ) {

            _orientation = orientation;

        }

    }

    @SuppressWarnings("unused")
    public int getScaleGranularity() {

        return _scaleGranularity;

    }

    public Dimension refresh( double zoomFactor ) {

        Dimension naturalSize = ObtuseImageUtils.maybeRotateDimension( getNaturalSize(), getOrientation() );
        @SuppressWarnings("UnnecessaryLocalVariable") Dimension newCurrentSize = new Dimension(
                (int)Math.round( naturalSize.width * zoomFactor ),
                (int)Math.round( naturalSize.height * zoomFactor )
        );

        _currentSize = newCurrentSize;

        Optional<ImageIcon> ii = getScaledImage();
        ii.ifPresent( this::setIcon );

        revalidate();
        repaint();

        return _currentSize;

    }

    public int getOrientation() {

        return _orientation;

    }

    @NotNull
    public Dimension getNaturalSize() {

        return new Dimension( _naturalSize );

    }

    @SuppressWarnings("unused")
    @NotNull
    public Dimension getCurrentSize() {

        return new Dimension( _currentSize );

    }

    @SuppressWarnings("unused")
    public boolean hasActualImage() {

        return _fullSizeImage != null;

    }

    @SuppressWarnings("unused")
    @NotNull
    public Optional<ImageIcon> getOptFullSizeImage() {

        return Optional.ofNullable( _fullSizeImage );

    }

    @SuppressWarnings("unused")
    @NotNull Optional<ImageIcon> getOptCurrentSizeImage() {

        return Optional.ofNullable( _currentSizeImage );

    }

    //    public abstract ImageIcon getImageIcon();

    public Optional<ImageIcon> getScaledImage() {

        if ( _fullSizeImage == null ) {

            return Optional.empty();

        }

        ImageIcon scaledImageIcon = ImageIconUtils.getScaledImageIcon(
                Math.max( _currentSize.width, _currentSize.height ),
                _fullSizeImage
        );

        return Optional.of( scaledImageIcon );

//        Optional<CachedThing<ScaledImageId<IID>, ImageIcon>> optScaledImage = _imageIconCache.getOptional(
//                new ScaledImageId<>(
//                        _imageIdentifier,
//                        _orientation,
//                        Math.max( _currentSize.width, _currentSize.height )
//                )
//        );
//
//        return optScaledImage.map( CachedThing::getThing );

    }

}