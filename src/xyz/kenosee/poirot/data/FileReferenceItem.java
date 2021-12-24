package xyz.kenosee.poirot.data;

import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.kenosee.poirot.ui.FileReferenceEditorPane;
import xyz.kenosee.poirot.ui.ReferenceEditorPane;

import javax.swing.JDialog;
import java.io.File;
import java.util.Optional;

/**
 Represent a single {@link File} reference document.
 */

public class FileReferenceItem extends ReferenceItem {

    public FileReferenceItem( @NotNull final File fileValue, @Nullable final String sourceText ) {
        super( generateDescriptiveText( fileValue ), sourceText, fileValue );

    }

    private static String generateDescriptiveText( @NotNull final File fileValue ) {

        return fileValue.getName();

    }

    @NotNull
    public Optional<File> getOptFileValue() {

        @NotNull Optional<Object> optObjectValue = getOptObjectValue();

        if ( optObjectValue.isPresent() ) {

            Object objectValue = optObjectValue.get();
            if ( objectValue instanceof File ) {

                File rval = (File)objectValue;
                return Optional.of( rval );

            } else {

                throw new ClassCastException(
                        "FileReferenceItem.getOptFileValue:  " +
                        "value is not a File " +
                        "(it is a " + objectValue.getClass().getCanonicalName() + ")"
                );

            }

        } else {

            return Optional.empty();

        }

    }

    @Override
    protected @NotNull ReferenceEditorPane<FileReferenceItem> createEditDialog() {

        @SuppressWarnings("UnnecessaryLocalVariable")
        FileReferenceEditorPane fileReferenceEditorPane = new FileReferenceEditorPane( this );

        return fileReferenceEditorPane;

    }

    @Override
    protected void populateTypeSpecificEditDialog() {

        // Nothing to do here (yet?).

        ObtuseUtil.doNothing();

    }

    public Kind getKind() {

        return Kind.FILE;

    }

//    public String toString() {
//
////        return "ReferenceItem( " + ObtuseUtil.enquoteToJavaString( getDescriptiveName() ) + " )";
//
//        return getDescriptiveText();
//
//    }

}
