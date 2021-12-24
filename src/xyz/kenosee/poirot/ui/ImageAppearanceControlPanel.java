package xyz.kenosee.poirot.ui;

import com.obtuse.exceptions.HowDidWeGetHereError;
import com.obtuse.ui.MyActionListener;
import com.obtuse.ui.ObtuseImageIdentifier;
import com.obtuse.ui.ObtuseImageUtils;
import com.obtuse.ui.YesNoPopupMessageWindow;
import com.obtuse.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Optional;

/**
 Created by danny on 2019/01/30.
 */

public class ImageAppearanceControlPanel extends JPanel implements MediaItemSelectionWatcher {

    private static final TwoDimensionalTreeMap<Integer, String, ImageIcon> s_scaledIcons =
            new TwoDimensionalTreeMap<>();

    private static final String s_rotate_left_filename = "rotate_left.png";
    private static final String s_rotate_right_filename = "rotate_right.png";
    private static final String s_rotate_180_filename = "rotate_180.png";
    private static final String s_flip_vertically_filename = "flip_vertically.png";
    private static final String s_flip_horizontally_filename = "flip_horizontally.png";

    private static final String s_minify_filename = "minus_sign.png";
    private static final String s_magnify_filename = "plus_sign.png";
    private static final String s_make_fit_filename = "make_fit.png";
    private static final String s_make_fit_mode_on_filename = "make_fit_mode_on.png";
    private static final String s_fullsize_filename = "fullsize.png";
    private static final int BUTTON_ICON_SIZE = 20;
    private static final double ZOOM_GRANULARITY_FACTOR = 1.01;
    private static final double BIG_ZOOM_GRANULARITY_FACTOR = ZOOM_GRANULARITY_FACTOR *
                                                              ZOOM_GRANULARITY_FACTOR *
                                                              ZOOM_GRANULARITY_FACTOR *
                                                              ZOOM_GRANULARITY_FACTOR *
                                                              ZOOM_GRANULARITY_FACTOR;
    private final boolean _supportResizing;
    private final boolean _supportTransformations;

    public final LancotGuiAction<ImageAppearanceTarget> IMAGE_INFO_ACTION;
    public final LancotGuiAction<ImageAppearanceTarget> ROTATE_ORIENTATION_LEFT_ACTION;
    public final LancotGuiAction<ImageAppearanceTarget> ROTATE_ORIENTATION_RIGHT_ACTION;
    public final LancotGuiAction<ImageAppearanceTarget> FLIP_VERTICALLY_ACTION;
    public final LancotGuiAction<ImageAppearanceTarget> FLIP_HORIZONTALLY_ACTION;
    public final LancotGuiAction<ImageAppearanceTarget> ROTATE_180_ACTION;
    public final LancotGuiAction<ImageAppearanceTarget> OPEN_IMAGE_VIEWER_ACTION;
    public final LancotGuiAction<ImageAppearanceTarget> CLOSE_IMAGE_VIEWER_ACTION;
    public final LancotGuiAction<ImageAppearanceTarget> OPEN_OS_VIEWER_ACTION;
    public final LancotGuiAction<ImageAppearanceTarget> EXPORT_MEDIA_ITEMS_ACTION;
    public final LancotGuiAction<ImageAppearanceTarget> SELECT_ALL_ACTION;
    public final LancotGuiAction<ImageAppearanceTarget> UNSELECT_ALL_ACTION;

    @SuppressWarnings({ "FieldCanBeLocal", "unused" }) private JButton _rotateOrientationLeftButton;
    @SuppressWarnings({ "FieldCanBeLocal", "unused" }) private JButton _rotateOrientationRightButton;
    @SuppressWarnings({ "FieldCanBeLocal", "unused" }) private JButton _flipOrientationHorizontallyButton;
    @SuppressWarnings({ "FieldCanBeLocal", "unused" }) private JButton _flipOrientationVerticallyButton;
    @SuppressWarnings({ "FieldCanBeLocal", "unused" }) private JButton _rotateOrientation180Button;
    @SuppressWarnings("FieldCanBeLocal") private final JButton _minifyButton;
    @SuppressWarnings("FieldCanBeLocal") private final JButton _magnifyButton;
    private final JButton _makeFitButton;
    @SuppressWarnings("FieldCanBeLocal") private final JButton _fullsizeButton;

    private JMenu _imageMenu;
    @SuppressWarnings("FieldCanBeLocal") private JMenuItem _selectAll_MenuItem;
    @SuppressWarnings({ "FieldCanBeLocal", "unused" }) private JMenuItem _unSelectAll_MenuItem;
    private JMenuItem _openViewerMenuItem;
    private JMenuItem _openOsViewerMenuItem;
    @SuppressWarnings({ "FieldCanBeLocal", "unused" }) private JMenuItem _closeViewerMenuItem;
    private JMenuItem _exportMediaItemsMenuItem;
    @SuppressWarnings("FieldCanBeLocal") private JMenuItem _imageInfoMenuItem;
    @SuppressWarnings({ "FieldCanBeLocal", "unused" }) private JMenuItem _rotateLeftMenuItem;
    @SuppressWarnings({ "FieldCanBeLocal", "unused" }) private JMenuItem _rotateRightMenuItem;
    @SuppressWarnings({ "FieldCanBeLocal", "unused" }) private JMenuItem _flipVerticallyMenuItem;
    @SuppressWarnings({ "FieldCanBeLocal", "unused" }) private JMenuItem _flipHorizontallyMenuItem;
    @SuppressWarnings({ "FieldCanBeLocal", "unused" }) private JMenuItem _rotate180MenuItem;

    private boolean _makeFitMode = false;

    private final ImageAppearanceTarget _target;

    @Override
    public void itemSelectionMayHaveChanged(
            final boolean forceOpenOsViewerItemEnabled,
            final boolean forceOpenViewerItemEnabled,
            final boolean forceExportMediaItemsEnabled,
            final Collection<ObtuseImageIdentifier> selectedMediaItems
    ) {

        if ( forceOpenOsViewerItemEnabled != forceOpenViewerItemEnabled || forceOpenViewerItemEnabled != forceExportMediaItemsEnabled ) {

            ObtuseUtil.doNothing();

        }

        if ( _openOsViewerMenuItem != null ) {

            boolean openOsViewerItemEnabled = !selectedMediaItems.isEmpty() || forceOpenOsViewerItemEnabled;
            _openOsViewerMenuItem.setEnabled( openOsViewerItemEnabled );

        }

        if ( _openViewerMenuItem != null ) {

            boolean openViewerMenuItem = !selectedMediaItems.isEmpty() || forceOpenViewerItemEnabled;
            _openViewerMenuItem.setEnabled( openViewerMenuItem );

        }

        if ( _exportMediaItemsMenuItem != null ) {

            boolean exportMediaItemsEnabled = !selectedMediaItems.isEmpty() || forceExportMediaItemsEnabled;
            _exportMediaItemsMenuItem.setEnabled( exportMediaItemsEnabled );

        }

    }

    public enum ImageTransformation {
        ROTATE_LEFT_90,
        ROTATE_RIGHT_90,
        ROTATE_180,
        FLIP_HORIZONTALLY,
        FLIP_VERTICALLY
    }

    public interface ImageAppearanceTarget {

        void setAndSaveOrientation( @NotNull ImageTransformation imageTransformation );

        Dimension adjustZoom( double newZoomFactor, boolean force );

        void setZoomFactor( double zoomFactor );

        double getZoomFactor();

        boolean isLegalZoomFactor( double newZoom );

        void refresh();

        int getOrientation();

        @SuppressWarnings("UnusedReturnValue")
        boolean openLancotMediaViewer();

        @SuppressWarnings("UnusedReturnValue")
        boolean openOsMediaViewer();

        @SuppressWarnings("UnusedReturnValue")
        boolean closeImageWindow();

        @SuppressWarnings("UnusedReturnValue")
        boolean selectAll();

        @SuppressWarnings("UnusedReturnValue")
        boolean unSelectAll();

        Dimension getNaturalSize();

        Dimension getDrawingPanelSize();

        void viewStateChanged();

        void exportMediaItems();

    }

    public ImageAppearanceControlPanel(
            final boolean supportResizing,
            @NotNull final ImageAppearanceTarget target,
            @NotNull final String exportLabel,
            final boolean supportTransformations,
            final boolean includeOpenButton,
            final boolean includeCloseButton,
            final boolean includeSelectAllButton,
            final boolean includeUnselectAllButton
    ) {

        super();

        _target = target;

        _supportResizing = supportResizing;
        _supportTransformations = supportTransformations;

        IMAGE_INFO_ACTION = new LancotGuiAction<>(
                "Image Info",
                null,
                "Get Image Info",
                'I',
                0,
                _target,
                new MyActionListener() {

                    @Override
                    protected void myActionPerformed( final ActionEvent actionEvent ) {

                        YesNoPopupMessageWindow.doit(
                                "Show the image's info and such",
                                "Yes", "No",
                                () -> Logger.logMsg( " Human picked or defaulted to Yes" ),
                                () -> Logger.logMsg( "Human picked No" )
                        );

                    }

                }
        );

        ROTATE_ORIENTATION_LEFT_ACTION = new LancotGuiAction<>(
                "Rotate Left",
                getImageIcon( s_rotate_left_filename, BUTTON_ICON_SIZE ).orElse( null ),
                "Rotate image left 90°",
                'L',
                0,
                _target,
                new MyActionListener() {

                    @Override
                    protected void myActionPerformed( final ActionEvent actionEvent ) {

                        target.setAndSaveOrientation( ImageTransformation.ROTATE_LEFT_90 );

                        if ( _makeFitMode ) {

                            forceMakeFit();

                        } else {

                            _target.refresh();

                        }

                    }

                }
        );

        ROTATE_ORIENTATION_RIGHT_ACTION = new LancotGuiAction<>(
                "Rotate Right",
                getImageIcon( s_rotate_right_filename, BUTTON_ICON_SIZE ).orElse( null ),
                "Rotate image right 90°",
                'R',
                0,
                _target,
                new MyActionListener() {

                    @Override
                    protected void myActionPerformed( final ActionEvent actionEvent ) {

                        target.setAndSaveOrientation( ImageTransformation.ROTATE_RIGHT_90 );

                        if ( _makeFitMode ) {

                            forceMakeFit();

                        } else {

                            _target.refresh();

                        }

                    }

                }
        );

        FLIP_VERTICALLY_ACTION = new LancotGuiAction<>(
                "Flip Vertically",
                getImageIcon( s_flip_vertically_filename, BUTTON_ICON_SIZE ).orElse( null ),
                "Flip vertically",
                KeyEvent.VK_SLASH,
                InputEvent.SHIFT_DOWN_MASK,
                _target,
                new MyActionListener() {

                    @Override
                    protected void myActionPerformed( final ActionEvent actionEvent ) {

                        _target.setAndSaveOrientation( ImageTransformation.FLIP_VERTICALLY );

                        if ( _makeFitMode ) {

                            forceMakeFit();

                        } else {

                            _target.refresh();

                        }

                    }

                }
        );

        FLIP_HORIZONTALLY_ACTION = new LancotGuiAction<>(
                "Flip Horizontally",
                getImageIcon( s_flip_horizontally_filename, BUTTON_ICON_SIZE ).orElse( null ),
                "Flip horizontally",
                KeyEvent.VK_SLASH,
                0, // InputEvent.SHIFT_DOWN_MASK,
                _target,
                new MyActionListener() {

                    @Override
                    protected void myActionPerformed( final ActionEvent actionEvent ) {

                        _target.setAndSaveOrientation( ImageTransformation.FLIP_HORIZONTALLY );

                        if ( _makeFitMode ) {

                            forceMakeFit();

                        } else {

                            _target.refresh();

                        }

                        ObtuseUtil.doNothing();

                    }

                }
        );

        ROTATE_180_ACTION = new LancotGuiAction<>(
                "Rotate 180°",
                getImageIcon( s_rotate_180_filename, BUTTON_ICON_SIZE ).orElse( null ),
                "Rotate 180°",
                KeyEvent.VK_BACK_SLASH,
                InputEvent.SHIFT_DOWN_MASK,
                _target,
                new MyActionListener() {

                    @Override
                    protected void myActionPerformed( final ActionEvent actionEvent ) {

                        _target.setAndSaveOrientation( ImageTransformation.ROTATE_180 );

                        if ( _makeFitMode ) {

                            forceMakeFit();

                        } else {

                            _target.refresh();

                        }

                    }

                }
        );

        if ( includeOpenButton ) {

            OPEN_IMAGE_VIEWER_ACTION = new LancotGuiAction<>(
                    "Open Image",
                    null,
                    "Open Image",
                    KeyEvent.VK_O,
                    0,
                    _target,
                    new MyActionListener() {

                        @Override
                        protected void myActionPerformed( final ActionEvent actionEvent ) {

                            _target.openLancotMediaViewer();

                        }

                    }
            );

        } else {

            OPEN_IMAGE_VIEWER_ACTION = null;

        }

        OPEN_OS_VIEWER_ACTION = new LancotGuiAction<>(
                "Open with OS Viewer",
                null,
                "Open with OS Viewer",
                KeyEvent.VK_PERIOD,
                0,
                _target,
                new MyActionListener() {

                    @Override
                    protected void myActionPerformed( final ActionEvent actionEvent ) {

                        _target.openOsMediaViewer();

                    }

                }
        );

        EXPORT_MEDIA_ITEMS_ACTION = new LancotGuiAction<>(
                exportLabel,
                null,
                exportLabel,
                KeyEvent.VK_E,
                0,
                _target,
                new MyActionListener() {

                    @Override
                    protected void myActionPerformed( final ActionEvent actionEvent ) {

                        _target.exportMediaItems();

                    }

                }
        );

        if ( includeCloseButton ) {

            CLOSE_IMAGE_VIEWER_ACTION = new LancotGuiAction<>(
                    "Close Image Action",
                    null,
                    "Close Image",
                    KeyEvent.VK_W,
                    0,
                    _target,
                    new MyActionListener() {

                        @Override
                        protected void myActionPerformed( final ActionEvent actionEvent ) {

                            _target.closeImageWindow();

                        }

                    }
            );

        } else {

            CLOSE_IMAGE_VIEWER_ACTION = null;

        }

        if ( includeSelectAllButton ) {

            SELECT_ALL_ACTION = new LancotGuiAction<>(
                    "Select All Action",
                    null,
                    "Select All",
                    KeyEvent.VK_A,
                    0,
                    _target,
                    new MyActionListener() {

                        @Override
                        protected void myActionPerformed( final ActionEvent actionEvent ) {

                            try ( Measure ignored = new Measure( "select-all action" ) ) {

                                _target.selectAll();

                            }

                        }

                    }
            );

        } else {

            SELECT_ALL_ACTION = null;

        }

        if ( includeUnselectAllButton ) {

            UNSELECT_ALL_ACTION = new LancotGuiAction<>(
                    "Unselect All Action",
                    null,
                    "Unselect All",
                    KeyEvent.VK_U,
                    0,
                    _target,
                    new MyActionListener() {

                        @Override
                        protected void myActionPerformed( final ActionEvent actionEvent ) {

                            try ( Measure ignored = new Measure( "unselect-all action" ) ) {

                                _target.unSelectAll();

                            }

                        }

                    }
            );

        } else {

            UNSELECT_ALL_ACTION = null;

        }

        if ( _supportTransformations ) {

            // Note that we assign each initialized button to an appropriately named variable
            // strictly for debuggin' purposes. All of the 'real' work has already been done
            // inside the initializeButton method.

            _rotateOrientationLeftButton = initializeButton( ROTATE_ORIENTATION_LEFT_ACTION );
            _rotateOrientationRightButton = initializeButton( ROTATE_ORIENTATION_RIGHT_ACTION );
            _rotateOrientation180Button = initializeButton( ROTATE_180_ACTION );
            _flipOrientationHorizontallyButton = initializeButton( FLIP_HORIZONTALLY_ACTION );
            _flipOrientationVerticallyButton = initializeButton( FLIP_VERTICALLY_ACTION );

        }

        _minifyButton = initializeButton( s_minify_filename, BUTTON_ICON_SIZE, "zoom out" );
        _magnifyButton = initializeButton( s_magnify_filename, BUTTON_ICON_SIZE, "zoom in" );
        _makeFitButton = initializeButton( s_make_fit_filename, BUTTON_ICON_SIZE, "make the image fit the window" );
        _fullsizeButton = initializeButton( s_fullsize_filename, BUTTON_ICON_SIZE, "zoom to the fullsize image" );

        _minifyButton.setVisible( _supportResizing );
        _magnifyButton.setVisible( _supportResizing );
        _makeFitButton.setVisible( _supportResizing );
        _fullsizeButton.setVisible( _supportResizing );

        if ( _supportResizing ) {

            _minifyButton.addActionListener(
                    new MyActionListener() {

                        @Override
                        protected void myActionPerformed( final ActionEvent actionEvent ) {

                            Logger.logMsg( "minifyButton:  modifiers are " +
                                           Integer.toString( actionEvent.getModifiers(), 2 ) );

                            ObtuseUtil.doNothing();

                            setMakeFitMode( false );

                            if ( ( actionEvent.getModifiers() & ActionEvent.SHIFT_MASK ) == 0 ) {

                                _target.adjustZoom( _target.getZoomFactor() / ZOOM_GRANULARITY_FACTOR, false );

                            } else {

                                _target.adjustZoom( _target.getZoomFactor() / BIG_ZOOM_GRANULARITY_FACTOR, false );

                            }

                            _target.viewStateChanged();

                        }

                    }
            );

            _magnifyButton.addActionListener(
                    new MyActionListener() {

                        @Override
                        protected void myActionPerformed( final ActionEvent actionEvent ) {

                            Logger.logMsg( "magnifyButton:  modifiers are " +
                                           Integer.toString( actionEvent.getModifiers(), 2 ) );

                            ObtuseUtil.doNothing();

                            setMakeFitMode( false );

                            if ( ( actionEvent.getModifiers() & ActionEvent.SHIFT_MASK ) == 0 ) {

                                _target.adjustZoom( _target.getZoomFactor() * ZOOM_GRANULARITY_FACTOR, true );

                            } else {

                                _target.adjustZoom( _target.getZoomFactor() * BIG_ZOOM_GRANULARITY_FACTOR, true );

                            }

                        }

                    }
            );

            _fullsizeButton.addActionListener(
                    new MyActionListener() {

                        @Override
                        protected void myActionPerformed( final ActionEvent actionEvent ) {

                            setMakeFitMode( false );
                            //                        _zoomFactor = 1;
                            _target.setZoomFactor( 1 );
                            _target.adjustZoom( 1, true );

                        }

                    }
            );

            _makeFitButton.addActionListener(
                    new MyActionListener() {
                        @Override
                        protected void myActionPerformed( final ActionEvent actionEvent ) {

                            boolean oneShot = ( actionEvent.getModifiers() & ActionEvent.SHIFT_MASK ) != 0;
                            if ( !oneShot ) {

                                setMakeFitMode( !_makeFitMode );

                            }

                            if ( _makeFitMode || oneShot ) {

                                forceMakeFit();

                            }

                        }

                    }
            );

            setMakeFitMode( true );

        } else {

            _makeFitMode = false;

        }

    }

    public void enclosingComponentSizeChanged() {

        if ( _makeFitMode ) {

            makeImageFit( _target.getDrawingPanelSize() );

        }

    }

    @NotNull
    public JMenu getImageMenu() {

        if ( _imageMenu == null ) {

            _imageMenu = new JMenu( "Image" );

            _imageInfoMenuItem = new JMenuItem( IMAGE_INFO_ACTION );
            _imageInfoMenuItem.setIcon( null );
            //noinspection MagicConstant
            _imageInfoMenuItem.setAccelerator(
                    KeyStroke.getKeyStroke(
                            IMAGE_INFO_ACTION.getMnemonic(),
                            Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | IMAGE_INFO_ACTION.getModifierMask()
                    )
            );
            _imageMenu.add( _imageInfoMenuItem );

            if ( SELECT_ALL_ACTION != null ) {

                _selectAll_MenuItem = LancotGuiAction.createMenuItem( SELECT_ALL_ACTION, _imageMenu );
                _imageMenu.add( _selectAll_MenuItem );

            }

            if ( UNSELECT_ALL_ACTION != null ) {

                _unSelectAll_MenuItem = LancotGuiAction.createMenuItem( UNSELECT_ALL_ACTION, _imageMenu );

            }

            if ( OPEN_IMAGE_VIEWER_ACTION != null ) {

                _openViewerMenuItem = LancotGuiAction.createMenuItem( OPEN_IMAGE_VIEWER_ACTION, _imageMenu );

            }

            if ( CLOSE_IMAGE_VIEWER_ACTION != null ) {

                _closeViewerMenuItem = LancotGuiAction.createMenuItem( CLOSE_IMAGE_VIEWER_ACTION, _imageMenu );

            }

            _openOsViewerMenuItem = LancotGuiAction.createMenuItem( OPEN_OS_VIEWER_ACTION, _imageMenu );

            _exportMediaItemsMenuItem = LancotGuiAction.createMenuItem( EXPORT_MEDIA_ITEMS_ACTION, _imageMenu );

            if ( _supportTransformations ) {

                _rotateLeftMenuItem = LancotGuiAction.createMenuItem( ROTATE_ORIENTATION_LEFT_ACTION, _imageMenu );
                _rotateRightMenuItem = LancotGuiAction.createMenuItem( ROTATE_ORIENTATION_RIGHT_ACTION, _imageMenu );
                _flipVerticallyMenuItem = LancotGuiAction.createMenuItem( FLIP_VERTICALLY_ACTION, _imageMenu );
                _flipHorizontallyMenuItem = LancotGuiAction.createMenuItem( FLIP_HORIZONTALLY_ACTION, _imageMenu );
                _rotate180MenuItem = LancotGuiAction.createMenuItem( ROTATE_180_ACTION, _imageMenu );

            }


        }

        return _imageMenu;

    }

    public void forceMakeFit() {

        makeImageFit( _target.getDrawingPanelSize() );

    }

    @SuppressWarnings("unused")
    public boolean includeResizingButtons() {

        return _supportResizing;

    }

    private JButton initializeButton(
            @NotNull LancotGuiAction<?> lancotGuiAction
    ) {

        Optional<ImageIcon> optIcon = lancotGuiAction.getOptIcon();
        Optional<String> optName = lancotGuiAction.getOptName();

        JButton jButton;
        if ( optIcon.isEmpty() && optName.isEmpty() ) {

            throw new IllegalArgumentException( "initializeButton:  must have a name or an ImageIcon (ImageIcon takes precedence)" );

        }

        //noinspection IfStatementWithIdenticalBranches
        if ( optIcon.isPresent() ) {

            if ( lancotGuiAction.getMnemonic() == ' ' ) {

                jButton = new JButton( optIcon.get() );

            } else {

                String mouseModifiersText =
                        InputEvent.getModifiersExText(
                                InputEvent.META_DOWN_MASK | lancotGuiAction.getModifierMask()
                        ) +
                        (char)lancotGuiAction.getMnemonic();

                jButton = new JButton( mouseModifiersText, optIcon.get() );

            }

            ObtuseUtil.doNothing();

        } else {

            jButton = new JButton( optName.get() );

            ObtuseUtil.doNothing();

        }

        jButton.addActionListener(
                lancotGuiAction.getActionListener()
        );

        add( jButton );

        return jButton;

    }

    private JButton initializeButton(
            @NotNull final String buttonImageFilename,
            @SuppressWarnings("SameParameterValue") final int scaledSize,
            @Nullable final String toolTipText
    ) {

        JButton jButton;

        jButton = new JButton();
        jButton.setToolTipText( toolTipText );

        Optional<ImageIcon> optImgIcon = getImageIcon( buttonImageFilename, scaledSize );

        if ( optImgIcon.isEmpty() ) {

            throw new IllegalArgumentException(
                    "ImageAppearanceControlPanel.initializeButton:  " +
                    ObtuseUtil.enquoteToJavaString( buttonImageFilename ) +
                    " does not exist or does not contain a valid image"
            );

        }

        jButton.setIcon( optImgIcon.get() );

        if ( toolTipText != null ) {

            jButton.setToolTipText( toolTipText );

        }

        add( jButton );

        return jButton;

    }

    public static Optional<ImageIcon> getImageIcon( @NotNull final String buttonImageFilename, final int scaledSize ) {

        ImageIcon imageIcon = s_scaledIcons.computeIfAbsent(
                scaledSize,
                buttonImageFilename,
                ( imageSize, imageFilename ) -> {

                    Optional<ImageIcon> rval = ImageIconUtils.fetchIconImage(
                            imageFilename,
                            imageSize
                    );

                    return rval.orElse( null );

                }
        );

        return Optional.ofNullable( imageIcon );

    }

    private void setMakeFitMode( boolean makeFitMode ) {

        _makeFitMode = makeFitMode;

        String buttonImageFilename = _makeFitMode ? s_make_fit_mode_on_filename : s_make_fit_filename;
        ImageIcon imageIcon = getImageIcon( buttonImageFilename, BUTTON_ICON_SIZE ).orElse( null );
        _makeFitButton.setIcon( imageIcon );

    }

    public boolean isMakeFitMode() {

        return _makeFitMode;

    }

    public Dimension makeImageFit( @NotNull final Dimension desiredSize ) {

        // There are better ways of doing this but this works.
        // Also, it is true to the current goal of only zooming to powers of 1.05 such that
        // three "zoom ins" followed by three "zoom outs" gets one back to where they started.
        // I'm not sure that that is a reasonable / useful goal but it is a goal (at least for now).

        int count = 0;

        Dimension naturalSize = ObtuseImageUtils.maybeRotateDimension( _target.getNaturalSize(), _target.getOrientation() );

        double newZoom = 1;
        while ( true ) {

            if (
                    Math.round( newZoom * naturalSize.width ) <= desiredSize.width &&
                    Math.round( newZoom * naturalSize.height ) <= desiredSize.height
            ) {

                break;

            }

            count += 1;
            if ( count > 1000 ) {

                throw new HowDidWeGetHereError(
                        "PoirotImageViewerPanel.makeFit #1:  " +
                        "Tried 1,000 times and still can't figure out correct size"
                );

            }

            if ( _target.isLegalZoomFactor( newZoom / ZOOM_GRANULARITY_FACTOR ) ) {

                newZoom /= ZOOM_GRANULARITY_FACTOR;

            } else {

                break;

            }

        }

        ObtuseUtil.doNothing();

        if ( naturalSize.width <= 0 || naturalSize.height <= 0 ) {

            return new Dimension( 1, 1 );

        }

        while ( Math.round( ZOOM_GRANULARITY_FACTOR * newZoom * naturalSize.width ) <= desiredSize.width &&
                Math.round( ZOOM_GRANULARITY_FACTOR * newZoom * naturalSize.height ) <= desiredSize.height ) {

            count += 1;
            if ( count > 1000 ) {

                throw new HowDidWeGetHereError(
                        "PoirotImageViewerPanel.makeFit #2:  " +
                        "Tried 1,000 times and still can't figure out correct size"
                );

            }

            newZoom *= ZOOM_GRANULARITY_FACTOR;

        }

        Logger.logMsg( "adjusting zoom to " + newZoom );

        return _target.adjustZoom( newZoom, true );

    }

}
