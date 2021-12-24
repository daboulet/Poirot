package xyz.kenosee.poirot.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.kenosee.poirot.ui.ReferenceEditorPane;

import javax.swing.*;
import java.util.Optional;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.Function;

/**
 Represent a single reference document.
 */

public abstract class ReferenceItem {

    public static final String UNKNOWN_SOURCE = "<<unknown source>>";

    public JLabel getJListLine() {

        return new JLabel( getKind() + " - " + getDescriptiveName() );

    }

    public enum Kind {
        IMAGE,
        FILE,
        TEXT
    }

    public static WeakHashMap<ReferenceItem,ReferenceEditorPane> s_knownEditPanels = new WeakHashMap<>();

    @Nullable
    private Object _objectValue;

    @NotNull
    private String _descriptiveName;

    @Nullable
    private String _sourceText;

    @NotNull
    private final UUID _uuid = UUID.randomUUID();

    protected ReferenceItem(
            @NotNull final String descriptiveText,
            @Nullable final String sourceText,
            @Nullable final Object objectValue
    ) {
        super();

        _descriptiveName = descriptiveText;
        _objectValue = objectValue;
        _sourceText = sourceText;

    }

//    @NotNull
//    public ReferenceEditorPane getEditDialog() {
//
//        ReferenceEditorPane editPanel = s_knownEditPanels.computeIfAbsent(
//                this,
////                ReferenceItem::createEditDialog
//                new Function<ReferenceItem, ReferenceEditorPane>() {
//                    @Override
//                    public ReferenceEditorPane apply( final ReferenceItem referenceItem ) {
//
//                        return new
//                    }
//                }
//
//        );
//
//        return editPanel;
//
//    }

    protected abstract @NotNull ReferenceEditorPane createEditDialog();

    protected abstract void populateTypeSpecificEditDialog();

    @NotNull
    public UUID getUuid() {

        return _uuid;

    }

    public abstract Kind getKind();

    protected void updateDescriptiveName( @NotNull final String newDescriptiveName ) {

        _descriptiveName = newDescriptiveName;

    }

    protected void updateSourceText( @NotNull final String newSourceText ) {

        _sourceText = newSourceText;

    }

    public void setObjectValue( @Nullable final Object value ) {

        _objectValue = value;

    }

    @NotNull
    public final String getDescriptiveName() {

        return _descriptiveName;

    }

    @NotNull
    public final Optional<String> getOptSourceText() {

        return Optional.ofNullable( _sourceText );

    }

    @NotNull
    public Optional<Object> getOptObjectValue() {

        return Optional.ofNullable( _objectValue );

    }



    public final String toString() {

//        return "ReferenceItem( " + ObtuseUtil.enquoteToJavaString( getDescriptiveName() ) + " )";

        return getKind() + " - " + getDescriptiveName();

    }

}
