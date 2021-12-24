package xyz.kenosee.poirot.ui;

import com.obtuse.ui.ObtuseImageUtils;
import com.obtuse.util.BasicProgramConfigInfo;
import com.obtuse.util.ImageIconUtils;
import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.kenosee.poirot.config.PoirotConfig;
import xyz.kenosee.poirot.data.ImageReferenceItem;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Optional;

/**
 Created by danny on 2021/12/05.
 */

public class ImageReferenceEditorPane extends ReferenceEditorPane<ImageReferenceItem> {

    public static final int THUMBNAIL_SIZE = 100;
    private static Font s_labelFont;

    private static ImageIcon s_copyAndPasteIconImage;
    private ImageIcon _thumbnailImageIcon;

    private JPanel _panel1;
    private JButton _makePreviewWindowVisibleButton;
    private JLabel _thumbnailImageJLabel;

    public ImageReferenceEditorPane( ImageReferenceItem iri ) {
        super( iri );

        setLayout( new BorderLayout() );
        add( _panel1, BorderLayout.CENTER );
        @NotNull Optional<ImageIcon> optImageIcon = iri.getOptImageIconValue();
        if ( optImageIcon.isPresent() ) {

            @NotNull Optional<ImageIcon> optScaledIcon = ObtuseImageUtils.maybeRegenerateThumbnail(
                    iri.getDescriptiveName(),
                    optImageIcon.get(),
                    null,
                    THUMBNAIL_SIZE
            );

            createThumbnailImage( _thumbnailImageJLabel, optScaledIcon.orElse( null ) );

            ObtuseUtil.doNothing();

        } else {

            ObtuseUtil.doNothing();

        }

    }

    public static boolean createThumbnailImage( @NotNull final JLabel jLabel, final @Nullable ImageIcon optScaledIcon ) {
        //            _thumbnailImageIcon = ImageIconUtils.getScaledImageIcon( THUMBNAIL_SIZE, optImageIcon.get() );

        if ( optScaledIcon == null ) {

            String iconFile = new File(
                    new File( ImageIconUtils.getDefaultResourceBaseDirectory() ),
                    PoirotConfig.ICON_OF_LAST_RESORT
            ).getAbsolutePath();
//            ImageIcon iconOfLastResort = new ImageIcon( iconFile );
            if ( s_labelFont == null ) {

                s_labelFont = jLabel.getFont().deriveFont( 60f );

            }
            jLabel.setFont( s_labelFont );
//                ObtuseImageUtils.maybeRegenerateThumbnail(  )
            jLabel.setText( "???" );
//                jLabel.setIcon( iconOfLastResort );

//                String iconFile = new File(
//                        new File( ImageIconUtils.getDefaultResourceBaseDirectory() ),
//                        PoirotConfig.ICON_OF_LAST_RESORT
//                ).getAbsolutePath();
//                ImageIcon iconOfLastResort = new ImageIcon( iconFile );
//                jLabel.setText( "???" );
//                jLabel.setIcon( iconOfLastResort );

            return false;

        } else {

            jLabel.setIcon( optScaledIcon );
            jLabel.setText( null );

            return true;

        }

    }

//    public static void main( String[] args ) {
//
//        BasicProgramConfigInfo.init( "Kenosee", "Poirot", "testing" );
//        ImageIconUtils.setDefaultResourcesDirectory( "./src/xyz/kenosee/poirot/config/resources" );
//
//        String absolutePath = new File(
//                new File( ImageIconUtils.getDefaultResourceBaseDirectory() ),
//                PoirotConfig.COPY_AND_PASTE_TARGET_ICON_FILE
//        ).getAbsolutePath();
//
//        s_copyAndPasteIconImage = new ImageIcon( absolutePath );
//
//        ObtuseUtil.doNothing();
//        ImageReferenceItem iri = new ImageReferenceItem( s_copyAndPasteIconImage.getImage(), "<<testing>>" );
//        ReferenceEditorPane editorPane = new ImageReferenceEditorPane( iri );
//        CommonReferenceItemEditDialog dialog = new CommonReferenceItemEditDialog( null, editorPane );
//        dialog.pack();
//        dialog.setVisible( true );
//        System.exit( 0 );
//
//    }

    @Override
    public void aboutToMakeVisible() {

        // Don't need anything done here (yet(?)).

        ObtuseUtil.doNothing();

    }

    @Override
    public void populate( final CommonReferenceItemEditDialog<ImageReferenceItem> mainEditDialog ) {

    }

    public String toString() {

        return "ImageReferenceEditorPane( " + getReferenceItem() + " )";

    }


}
