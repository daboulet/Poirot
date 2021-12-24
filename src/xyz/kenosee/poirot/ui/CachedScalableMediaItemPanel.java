package xyz.kenosee.poirot.ui;

import com.obtuse.ui.ObtuseImageIdentifier;
import com.obtuse.ui.ObtuseImageUtils;
import com.obtuse.util.ImageIconUtils;
import com.obtuse.util.Logger;
import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 A customized {@link JLabel} for displaying images.
 */

public class CachedScalableMediaItemPanel<IID extends ObtuseImageIdentifier> extends JLabel {

    private final @NotNull ImageIcon _fullSizeImage;
    private final IID _imageIdentifier;
    private int _orientation;

    private final int _scaleGranularity;
    private final Dimension _naturalSize;
//    @NotNull private Dimension _currentSize;
    @NotNull private ImageIcon _zoomedSizeImage;

    public CachedScalableMediaItemPanel(
            @NotNull final ImageIcon fullSizeImage,
            @NotNull final IID imageIdentifier,
            int scaleGranularity,
            int initialOrientation
    ) {
        super();

        _fullSizeImage = fullSizeImage;
        _imageIdentifier = imageIdentifier;
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

        _naturalSize = ImageIconUtils.getSize( fullSizeImage );

        _zoomedSizeImage = _fullSizeImage;

    }

    @SuppressWarnings("unused")
    public CachedScalableMediaItemPanel(
            @NotNull final Image fullSizeImage,
            @NotNull final IID imageIdentifier,
            int scaleGranularity,
            int initialOrientation
    ) {
        this(
                new ImageIcon( fullSizeImage ),
                imageIdentifier,
                scaleGranularity,
                initialOrientation
        );
    }

    private int maxDimensionLength( @NotNull ImageIcon ii ) {

        return Math.max( ii.getIconWidth(), ii.getIconHeight() );

    }

    public ImageIcon getScaledImage( @NotNull final Dimension newZoomedSize ) {

        int newZoomedMaximumLength = Math.max( newZoomedSize.width, newZoomedSize.height );
        if ( maxDimensionLength( _fullSizeImage ) == newZoomedMaximumLength ) {

            return _fullSizeImage;

        }

        if ( maxDimensionLength( _zoomedSizeImage ) != newZoomedMaximumLength ) {

            _zoomedSizeImage = ImageIconUtils.getScaledImageIcon( newZoomedMaximumLength, _fullSizeImage );

        }
//        int newZoomedMaxDimensionLength = Math.max( newZoomedSize.width, newZoomedSize.height );

        return _zoomedSizeImage;

    }

    @SuppressWarnings("unused")
    public void setOrientation( int orientation ) {

        if ( _orientation != orientation ) {

            _orientation = orientation;

        }

    }

    @SuppressWarnings("unused")
    public int getScaleGranularity() {

        return _scaleGranularity;

    }

    @SuppressWarnings("unused")
    public Dimension refresh( double zoomFactor ) {

        Dimension naturalSize = ObtuseImageUtils.maybeRotateDimension( getNaturalSize(), getOrientation() );
        Dimension newCurrentSize = new Dimension(
                (int)Math.round( naturalSize.width * zoomFactor ),
                (int)Math.round( naturalSize.height * zoomFactor )
        );

        ImageIcon ii = getScaledImage( newCurrentSize );
        setIcon( ii );

        revalidate();
        repaint();

        return ImageIconUtils.getSize( _zoomedSizeImage );

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

        return ImageIconUtils.getSize( _zoomedSizeImage );

    }

    @SuppressWarnings("unused")
    @NotNull
    public ImageIcon getFullSizeImage() {

        return _fullSizeImage;

    }

    @SuppressWarnings("unused")
    @NotNull ImageIcon getCurrentSizeImage() {

        return _zoomedSizeImage;

    }

    @SuppressWarnings("unused")
    public IID getImageIdentifier() {

        return _imageIdentifier;
    }

    public String toString() {

        return "MediaItemPanel(" +
               " iid=" + _imageIdentifier +
               ", fs=" + ObtuseUtil.fDim( ImageIconUtils.getSize( _fullSizeImage ) ) +
               " )";

    }

}