package xyz.kenosee.poirot.ui;

import com.obtuse.ui.ObtuseImageIdentifier;
import org.jetbrains.annotations.NotNull;

/**
 Created by danny on 2021/11/27.
 */
@SuppressWarnings("rawtypes")
public class ScaledImageId<IID extends ObtuseImageIdentifier> implements Comparable {

    private final int _scaleGranularity;
    private final IID _imageID;
    private final int _orientation;

    public ScaledImageId( @NotNull final IID imageID, final int orientation, final int scaleGranularity ) {

        super();

        _imageID = imageID;
        _scaleGranularity = scaleGranularity;
        _orientation = orientation;

    }

    @SuppressWarnings("unused")
    @NotNull
    public IID getImageIdentifier() {

        return _imageID;

    }

    @NotNull
    public String format() {

        return "imageIdentifier=" + _imageID.format();

    }

    @Override
    public int compareTo( @NotNull final Object rhs ) {

        @SuppressWarnings("unchecked") ScaledImageId<IID>
                rhsAsId = (ScaledImageId<IID>)rhs;
        int rval = _imageID.compareTo( rhsAsId._imageID );
        if ( rval == 0 ) {

            rval = Integer.compare( _scaleGranularity, rhsAsId._scaleGranularity );
            if ( rval == 0 ) {

                rval = Integer.compare( _orientation, rhsAsId._orientation );

            }

        }

        return rval;

    }

    public boolean equals( Object obj ) {

        return obj instanceof CachedScalableMediaItemPanel && compareTo( obj ) == 0;

    }

    public int hashCode() {

        return _imageID.hashCode();

    }

    public int getScaleGranularity() {

        return _scaleGranularity;

    }

    public int getOrientation() {

        return _orientation;

    }

    public String toString() {

        return "ScaledImageId( " +
               format() + ", " +
               "scaleGranularity=" + getScaleGranularity() + ", " +
               "orientation=" + getOrientation() +
               " )";

    }

}
