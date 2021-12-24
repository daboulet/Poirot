package xyz.kenosee.poirot.main;

import com.obtuse.util.Clicks;
import com.obtuse.util.ImageIconUtils;
import com.obtuse.util.ObtuseUtil;
import xyz.kenosee.poirot.config.PoirotConfig;
import xyz.kenosee.poirot.data.ReferenceItemEditDialogManager;
import xyz.kenosee.poirot.ui.dnd.PoirotJListTransferHandler;
import xyz.kenosee.poirot.ui.dnd.PoirotTransferActionListener;
import xyz.kenosee.poirot.data.ReferenceItem;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.*;
import java.io.File;

public class PoirotMainWindow extends JDialog {

    private static final ImageIcon s_copyAndPasteIconImage;

    static {

        String absolutePath = new File(
                new File( ImageIconUtils.getDefaultResourceBaseDirectory() ),
                PoirotConfig.COPY_AND_PASTE_TARGET_ICON_FILE
        ).getAbsolutePath();

        s_copyAndPasteIconImage = new ImageIcon(
                absolutePath
        );

        ObtuseUtil.doNothing();

    }

    private final DefaultListModel<ReferenceItem> _listModel;

    private JPanel contentPane;
    private JPanel _topSpace;
    private JPanel _centerSpace;
    private JPanel _bottomSpace;
    private JButton _button1;
    private JButton _button2;
    private JButton _saveButton;
    private JButton _discardButton;
    private JScrollPane _referenceDocumentsScrollPane;
    private JList<ReferenceItem> _referenceItemsJList;
    private JLabel _copyAndPasteIconJLabel;

    private static DataFlavor s_supportedDataFlavors[] = {
            DataFlavor.stringFlavor,
            DataFlavor.javaFileListFlavor
    };

    private final PoirotJListTransferHandler _poirotJListTransferHandler = new PoirotJListTransferHandler(
//            new PoirotDataFlavorHandler[]{
//                    PoirotDataFlavorHandler.stringFlavor,
//                    PoirotDataFlavorHandler.javaFileListFlavor,
//                    PoirotDataFlavorHandler.imageFlavor
//            }
    );

    class MyCellRenderer extends JLabel implements ListCellRenderer<Object> {
        static final ImageIcon longIcon = new ImageIcon("long.gif");
        static final ImageIcon shortIcon = new ImageIcon("short.gif");

        // This is the only method defined by ListCellRenderer.
        // We just reconfigure the JLabel each time we're called.

        public Component getListCellRendererComponent(
                JList<?> list,           // the list
                Object value,            // value to display
                int index,               // cell index
                boolean isSelected,      // is the cell selected
                boolean cellHasFocus)    // does the cell have focus
        {

            if ( value instanceof ReferenceItem ) {

                ReferenceItem referenceItem = (ReferenceItem)value;
                JLabel label = referenceItem.getJListLine();

                setText( label.getText() );
                setIcon( label.getIcon() );

            } else {

                setText( "unknown thing (" + value + ")" );
                setIcon( null );

            }

//                String s = value.toString();
//                setText( s );
//                ImageIcon icon;
//                if ( value instanceof Image ) {
//
//                    icon = new ImageIcon( (Image)value );
//
//                } else if ( value instanceof ImageIcon ) {
//
//                    icon = (ImageIcon)value;
//
//                } else {
//
//                    icon = null;
//
//                }
//
//                if ( icon == null ) {
//
//                    setIcon( ( s.length() > 10 ) ? longIcon : shortIcon );
//
//                } else {
//
//                    setIcon( icon );
//
//                }

            if ( isSelected ) {
                setBackground( list.getSelectionBackground() );
                setForeground( list.getSelectionForeground() );
            } else {
                setBackground( list.getBackground() );
                setForeground( list.getForeground() );
            }
            setEnabled( list.isEnabled() );
            setFont( list.getFont() );
            setOpaque( true );

            return this;

        }

    }

    public PoirotMainWindow() {

        setTitle( "Poirot" );

        setLayout( new BorderLayout() );

        setJMenuBar( createMenuBar() );

        setMinimumSize( new Dimension( 500, 800 ) );
        setPreferredSize( new Dimension( 500, 800 ) );

        getRootPane().setPreferredSize( new Dimension( 500, 300 ) );
        getRootPane().setMinimumSize( new Dimension( 500, 300 ) );

        _referenceItemsJList.addMouseListener(

                new MouseAdapter() {

                    public void mouseClicked( MouseEvent e ) {

                        handleJListMouseClicks( e );

                    }

                }


        );

        _referenceItemsJList.setCellRenderer(new MyCellRenderer());

//        _list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        JScrollPane sp1 = new JScrollPane(_list1);
//        sp1.setPreferredSize(new Dimension(400,100));
//        _list1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        _referenceItemsJList.setDragEnabled( true);
        _listModel = new DefaultListModel<>();
//        _listModel.addElement( "first" );
//        _listModel.addElement( "second" );
//        _listModel.addElement( "third" );
        _referenceItemsJList.setModel( _listModel );
//        _list1.setFocusable( true );

        _referenceItemsJList.setTransferHandler( _poirotJListTransferHandler );
//        _list1.setDropMode(DropMode.ON);
//        _list1.setDropMode( DropMode.INSERT );
        setMappings( _referenceItemsJList );
////        JScrollPane scrollPane = new JScrollPane(_list1);
////        scrollPane.setPreferredSize(new Dimension(400,100));
//
//        _list1.setDragEnabled(true);
//        _list1.setDropMode( DropMode.INSERT );
//        _list1.setTransferHandler(new PoirotJListTransferHandler());

//        _copyAndPasteIconJLabel.setText( "" );
//        _copyAndPasteIconJLabel.setIcon( s_copyAndPasteIconImage );

        setContentPane( contentPane );
        setModal( true );
//        getRootPane().setDefaultButton( buttonOK );
//
//        buttonOK.addActionListener(
//                e -> onOK()
//        );
//
//        buttonCancel.addActionListener(
//                e -> onCancel()
//        );

        // call onCancel() when cross is clicked
        setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
        addWindowListener(

                new WindowAdapter() {

                    public void windowClosing( WindowEvent e ) {

                        onCancel();

                    }

                }

        );

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
    }

    private void handleJListMouseClicks( final MouseEvent e ) {

        if ( Clicks.isLeftClick( e ) ) {

            if ( e.getClickCount() == 2 ) {

                int index = _referenceItemsJList.locationToIndex( e.getPoint() );

                ReferenceItemEditDialogManager.launchOrShowItemEditDialog(
                        PoirotMainWindow.this,
                        _referenceItemsJList,
                        index
                );

            } else {

                ObtuseUtil.doNothing();

            }

        }

    }

    private void setMappings( JList<ReferenceItem> list ) {
        ActionMap map = list.getActionMap();
        map.put(TransferHandler.getCutAction().getValue(Action.NAME),
                TransferHandler.getCutAction());
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME),
                TransferHandler.getCopyAction());
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME),
                TransferHandler.getPasteAction());

    }

    /**
     * Create an Edit menu to support cut/copy/paste.
     */
    public JMenuBar createMenuBar() {

        JMenuItem menuItem = null;
        JMenuBar menuBar = new JMenuBar();
        JMenu mainMenu = new JMenu("Edit");
        mainMenu.setMnemonic(KeyEvent.VK_E);
        PoirotTransferActionListener actionListener = new PoirotTransferActionListener();

        menuItem = new JMenuItem("Cut");
        menuItem.setActionCommand((String)TransferHandler.getCutAction().
                                                         getValue(Action.NAME));
        menuItem.addActionListener(actionListener);
        menuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        menuItem.setMnemonic(KeyEvent.VK_T);
        mainMenu.add(menuItem);

        menuItem = new JMenuItem("Copy");
        menuItem.setActionCommand((String)TransferHandler.getCopyAction().
                                                         getValue(Action.NAME));
        menuItem.addActionListener(actionListener);
        menuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menuItem.setMnemonic(KeyEvent.VK_C);
        mainMenu.add(menuItem);

        menuItem = new JMenuItem("Paste");
        menuItem.setActionCommand((String)TransferHandler.getPasteAction().
                                                         getValue(Action.NAME));
        menuItem.addActionListener(actionListener);
        menuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        menuItem.setMnemonic(KeyEvent.VK_P);
        mainMenu.add(menuItem);

        menuBar.add(mainMenu);

        return menuBar;

    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main( String[] args ) {

        PoirotMainWindow dialog = new PoirotMainWindow();
        dialog.pack();
        dialog.setVisible( true );
        System.exit( 0 );
    }

    private void createUIComponents() {

        // TODO: place custom component creation code here

//        _copyAndPasteIconJLabel = new JLabel(
//                s_copyAndPasteIconImage
//        );

    }

}
