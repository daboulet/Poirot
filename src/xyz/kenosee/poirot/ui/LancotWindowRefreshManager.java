package xyz.kenosee.poirot.ui;

import com.obtuse.ui.ObtuseImageIdentifier;
import com.obtuse.util.Logger;
import com.obtuse.util.ObtuseUtil;
import com.obtuse.util.SimpleUniqueIntegerIdGenerator;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 Created by danny on 2019/02/19.
 */

public class LancotWindowRefreshManager {

    public interface WatchedWindow {

        void refreshIfVisible( @NotNull ObtuseImageIdentifier imageIdentifier );

    }

    public static final SimpleUniqueIntegerIdGenerator
            ID_GENERATOR = new SimpleUniqueIntegerIdGenerator( "WindowRefresh id generator" );

    private static final Set<WatchedWindow> s_watchedWindows = new HashSet<>();

    private LancotWindowRefreshManager() {

        super();

    }

    public static void forgetWatchedWindow( @NotNull final WatchedWindow watchedWindow ) {

        s_watchedWindows.remove( watchedWindow );

    }

    public static void rememberWatchedWindow( @NotNull final WatchedWindow watchedWindow ) {

        s_watchedWindows.add( watchedWindow );

    }

    public static void refreshIfVisible( @NotNull final ObtuseImageIdentifier imageIdentifier ) {

        List<WatchedWindow> browserPanels = new ArrayList<>( s_watchedWindows );
        for ( WatchedWindow watchedWindow : browserPanels ) {

            watchedWindow.refreshIfVisible( imageIdentifier );

        }

    }

    public static void watchFrame( @NotNull JFrame jFrame, @NotNull WatchedWindow watchedWindow ) {

        jFrame.addWindowListener(
                new WindowAdapter() {

                    public void windowClosing( final WindowEvent e ) {

                        onClosing( jFrame, watchedWindow, e );

                    }

                    public void windowClosed( final WindowEvent e ) {

                        onClosed( jFrame, watchedWindow, e );

                    }

                    public void windowActivated( final WindowEvent e ) {

                        onWindowActivated( jFrame, watchedWindow, e );

                    }

                }
        );

    }

    private static void onClosing( JFrame jFrame, WatchedWindow watchedWindow, WindowEvent e ) {

        Logger.logMsg( "window for " + ObtuseUtil.enquoteToJavaString( jFrame.getName() ) + " is closing:  " + e );

        jFrame.dispose();

    }

    private static void onClosed( JFrame jFrame, WatchedWindow watchedWindow, WindowEvent e ) {

        Logger.logMsg( "window for " + ObtuseUtil.enquoteToJavaString( jFrame.getName() ) + " is closed:  " + e );

        jFrame.dispose();

        LancotWindowRefreshManager.forgetWatchedWindow( watchedWindow );

    }

    private static void onWindowStateChanged( JFrame jFrame, WatchedWindow watchedWindow, WindowEvent e ) {

        Logger.logMsg( "window for " + ObtuseUtil.enquoteToJavaString( jFrame.getName() ) + " is changing state:  " + e );

        jFrame.dispose();

    }

    private static void onWindowActivated( JFrame jFrame, WatchedWindow watchedWindow, WindowEvent e ) {

        Logger.logMsg( "window for " + ObtuseUtil.enquoteToJavaString( jFrame.getName() ) + " activated:  " + e );

        LancotWindowRefreshManager.rememberWatchedWindow( watchedWindow );

    }

}
