package xyz.kenosee.poirot.data;

import com.obtuse.ui.ObtuseImageUtils;
import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.kenosee.poirot.ui.FileReferenceEditorPane;
import xyz.kenosee.poirot.ui.ImageReferenceEditorPane;
import xyz.kenosee.poirot.ui.ReferenceEditorPane;
import xyz.kenosee.poirot.ui.dnd.ImageFetchFailedException;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

/**
 Represent a single {@link Image} reference document.
 */

public class ImageReferenceItem extends ReferenceItem {

    public ImageReferenceItem( @NotNull final ImageIcon imageIcon, @Nullable final String sourceText )
            throws ImageFetchFailedException {
        super( generateDescriptiveText( imageIcon ), sourceText, imageIcon );

        if ( imageIcon.getImageLoadStatus() != MediaTracker.COMPLETE ) {

            throw new ImageFetchFailedException(
                    "ImageIcon's load status is not COMPLETE (code=" + imageIcon.getImageLoadStatus() + ")",
                    ImageFetchFailedException.FailureCause.CANNOT_READ_IMAGE_FILE
            );

        }

    }

    private static String generateDescriptiveText( @NotNull final ImageIcon imageIcon ) {

        return imageIcon.toString();

    }

    @NotNull
    public Optional<ImageIcon> getOptImageIconValue() {

        @NotNull Optional<Object> optObjectValue = getOptObjectValue();

        if ( optObjectValue.isPresent() ) {

            Object objectValue = optObjectValue.get();
            if ( objectValue instanceof ImageIcon ) {

                ImageIcon rval = (ImageIcon)objectValue;
                return Optional.of( rval );

            } else {

                throw new ClassCastException(
                        "ImageReferenceItem.getOptImageValue:  " +
                        "value is not an Image " +
                        "(it is a " + objectValue.getClass().getCanonicalName() + ")"
                );

            }

        } else {

            return Optional.empty();

        }

    }

    public JLabel getJListLine() {

        JLabel label = new JLabel();

        @NotNull Optional<ImageIcon> optImageIcon = getOptImageIconValue();
        if ( optImageIcon.isPresent() ) {

            @NotNull Optional<ImageIcon> optScaledIcon = ObtuseImageUtils.maybeRegenerateThumbnail(
                    getDescriptiveName(),
                    optImageIcon.get(),
                    null,
                    25
            );

            if ( ImageReferenceEditorPane.createThumbnailImage( label, optScaledIcon.orElse( null ) ) ) {

                label.setText( getDescriptiveName() );

            } else {

                label.setText( toString() );

            }

            ObtuseUtil.doNothing();

        } else {

            ObtuseUtil.doNothing();

        }

        return label;

    }

    @Override
    protected @NotNull ReferenceEditorPane<ImageReferenceItem> createEditDialog() {

        @SuppressWarnings("UnnecessaryLocalVariable")
        ImageReferenceEditorPane imageReferenceEditorPane = new ImageReferenceEditorPane( this );

        return imageReferenceEditorPane;

    }

    @Override
    protected void populateTypeSpecificEditDialog() {

        // Nothing to do here (yet?).

        ObtuseUtil.doNothing();

    }

    public Kind getKind() {

        return Kind.IMAGE;

    }

//    public String toString() {
//
////        return "ReferenceItem( " + ObtuseUtil.enquoteToJavaString( getDescriptiveName() ) + " )";
//
//        return getDescriptiveText();
//
//    }

}
