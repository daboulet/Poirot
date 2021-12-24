package xyz.kenosee.poirot.data;

import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.kenosee.poirot.ui.ReferenceEditorPane;
import xyz.kenosee.poirot.ui.TextReferenceEditorPane;
import xyz.kenosee.poirot.ui.dnd.TextReferenceEditorPane2;

import java.util.Optional;

/**
 Represent a single text (i.e. {@link String}) reference document.
 */

public class TextReferenceItem extends ReferenceItem {

    public TextReferenceItem( @NotNull final String descriptiveName, @Nullable String sourceText, @NotNull final String textValue ) {
        super( descriptiveName, sourceText, textValue );

    }

    public final void setObjectValue( @Nullable final Object value ) {

        super.setObjectValue( value );

    }

    @NotNull
    public Optional<String> getOptTextValue() {

        @NotNull Optional<Object> optObjectValue = getOptObjectValue();

        if ( optObjectValue.isPresent() ) {

            Object objectValue = optObjectValue.get();
            if ( objectValue instanceof String ) {

                @SuppressWarnings("PatternVariableCanBeUsed") String rval = (String)objectValue;
                return Optional.of( rval );

            } else {

                throw new ClassCastException(
                        "TextReferenceItem.getOptStringValue:  " +
                        "value is not text " +
                        "(it is a " + objectValue.getClass().getCanonicalName() + ")"
                );

            }

        } else {

            return Optional.empty();

        }

    }

    @Override
    protected @NotNull ReferenceEditorPane<TextReferenceItem> createEditDialog() {

        @SuppressWarnings("UnnecessaryLocalVariable")
        TextReferenceEditorPane2 textReferenceEditorPane = new TextReferenceEditorPane2( this );

        return textReferenceEditorPane;

    }

    @Override
    protected void populateTypeSpecificEditDialog() {

        // Nothing to do here (yet?).

        ObtuseUtil.doNothing();

    }

    public Kind getKind() {

        return Kind.TEXT;

    }

//    public String toString() {
//
////        return "ReferenceItem( " + ObtuseUtil.enquoteToJavaString( getDescriptiveName() ) + " )";
//
//        return getDescriptiveText();
//
//    }

}
