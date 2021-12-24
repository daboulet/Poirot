package xyz.kenosee.poirot.ui;

import com.obtuse.ui.ObtuseImageIdentifier;
import com.obtuse.ui.ObtuseImageUtils;
import com.obtuse.util.Logger;
import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 Show a single image to the human.
 %%%
 %%% This doesn't work yet(?) because the PoirotImageViewerPanel class is little more than a hollow shell.
 %%%
 */

public class ImageViewerWindow extends JFrame {

    private static Dimension _defaultImageViewerWindowSize = null;

    private final PoirotImageViewerPanel _ivp;
    private final ObtuseImageIdentifier _imageIdentifier;

    public ImageViewerWindow( @NotNull final ObtuseImageIdentifier imageIdentifier ) {
        super( "Image Viewer Window for " + imageIdentifier );

        _imageIdentifier = imageIdentifier;

        _ivp = new PoirotImageViewerPanel( getObtuseImageIdentifier(), this );

        JMenuBar menuBar = getJMenuBar();
        if ( menuBar == null ) {

            menuBar = new JMenuBar();
            setJMenuBar( menuBar );

        }

        menuBar.add( _ivp.getImageMenu() );

        _ivp.initializeMenuStates();

        LancotWindowRefreshManager.watchFrame( this, _ivp );

        if ( _defaultImageViewerWindowSize == null ) {

            Dimension screenSize = Toolkit.getDefaultToolkit()
                                          .getScreenSize();
            Insets screenInsets = Toolkit.getDefaultToolkit()
                                         .getScreenInsets( ObtuseImageUtils.getDefaultConfiguration() );
            _defaultImageViewerWindowSize =
                    new Dimension(
                            screenSize.width - ( screenInsets.left + screenInsets.right ),
                            screenSize.height - ( screenInsets.top + screenInsets.bottom )
                    );

        }

        setContentPane( _ivp );
        Logger.logMsg(
                "ivp:  " +
                "min=" + ObtuseUtil.fDim( _ivp.getMinimumSize() ) + ", " +
                "pref=" + ObtuseUtil.fDim( _ivp.getPreferredSize() ) + ", " +
                "max=" + ObtuseUtil.fDim( _ivp.getMaximumSize() )
        );

        setMinimumSize( new Dimension( 600, 600 ) );
        setPreferredSize( _defaultImageViewerWindowSize );
        pack();
        setVisible( true );

        ObtuseUtil.doNothing();

    }

    public static void rememberDefaultImageViewerWindowSize( @NotNull Dimension defaultImageViewerWindowSize ) {

        _defaultImageViewerWindowSize = defaultImageViewerWindowSize;

    }

    public ObtuseImageIdentifier getObtuseImageIdentifier() {

        return _imageIdentifier;

    }

}
