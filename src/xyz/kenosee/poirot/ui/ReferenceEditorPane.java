package xyz.kenosee.poirot.ui;

import com.obtuse.exceptions.HowDidWeGetHereError;
import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;
import xyz.kenosee.poirot.data.ReferenceItem;
import xyz.kenosee.poirot.main.PoirotMainWindow;

import javax.swing.*;
import java.util.Optional;

/**
 Created by danny on 2021/12/05.
 */

public abstract class ReferenceEditorPane<RI extends ReferenceItem> extends JPanel {

    private final RI _item;
    private PoirotMainWindow _owner;

    protected ReferenceEditorPane( @NotNull final RI item ) {
        super();

        _item = item;

    }

    @NotNull
    public RI getReferenceItem() {

        return _item;

    }

    public String toString() {

        return "ReferenceEditorPane( " + _item + " )";

    }

    public abstract void aboutToMakeVisible();

    public void setPoirotMainWindow( @NotNull final PoirotMainWindow owner ) {

        if ( _owner == null ) {

            _owner = owner;

        } else {

            throw new HowDidWeGetHereError(
                    "ReferenceEditorPane.setPoirotMainWindow:  owner already set to " +
                    ObtuseUtil.enquoteJavaObject( _owner )
            );

        }

    }

    public Optional<PoirotMainWindow> getOptPoirotMainWindow() {

        return Optional.ofNullable( _owner );

    }

    public abstract void populate( final CommonReferenceItemEditDialog<RI> mainEditDialog );

}
