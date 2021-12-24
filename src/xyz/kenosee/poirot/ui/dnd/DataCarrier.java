package xyz.kenosee.poirot.ui.dnd;

import com.obtuse.exceptions.HowDidWeGetHereError;
import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.kenosee.poirot.data.ReferenceItem;

import java.util.List;

/**
 Things that carry data.
 The data is either received via DnD and CnP, or fetched from our 'library'.
 */

public abstract class DataCarrier {

    public enum DataClass {
        IMAGE,
        FILE,
        TEXT
    }

    public abstract void createReferenceItem(
            final List<ReferenceItem> referenceItems
    );

    protected DataCarrier() {
        super();

    }

    @Nullable
    public abstract Object getOptionalData();

    @NotNull
    public abstract DataClass getDataClass();

    private String _toString = null;

    public final String toString() {

        if ( _toString == null ) {

            Object optionalData = getOptionalData();
            if ( optionalData instanceof List list ) {

                throw new HowDidWeGetHereError(
                        "DataCarrier.toString:  getData() returned a List!!!"
                );

            } else {

                _toString = "<" +
                            (
                                    optionalData instanceof String
                                            ?
                                            ObtuseUtil.enquoteToJavaString( (String)optionalData )
                                            :
                                            ObtuseUtil.enquoteJavaObject( optionalData )
                            ) +
                            ">";

            }

        }

        return _toString;

    }

}
