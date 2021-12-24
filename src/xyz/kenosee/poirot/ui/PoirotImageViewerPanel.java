package xyz.kenosee.poirot.ui;

import com.obtuse.exceptions.HowDidWeGetHereError;
import com.obtuse.ui.ObtuseImageIdentifier;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 Created by danny on 2021/12/11.
 */
public class PoirotImageViewerPanel extends JPanel implements LancotWindowRefreshManager.WatchedWindow {

    private JPanel _panel1;
    private JPanel _imagePanel;

    private final JFrame _jFrame;

    public PoirotImageViewerPanel( @NotNull final ObtuseImageIdentifier imageIdentifier, @NotNull JFrame jFrame ) {
        super();

        _jFrame = jFrame;

        setLayout( new BorderLayout() );
        add( BorderLayout.CENTER, _panel1 );

    }

    public Component getImageMenu() {

        return null;

    }

    public JFrame getjFrame() {

        return _jFrame;
    }

    public void initializeMenuStates() {


    }

    @Override
    public void refreshIfVisible( @NotNull final ObtuseImageIdentifier imageIdentifier ) {

        throw new HowDidWeGetHereError( "unimplemented" );

    }

}
