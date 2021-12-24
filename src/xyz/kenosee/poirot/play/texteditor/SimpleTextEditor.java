package xyz.kenosee.poirot.play.texteditor;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jude
 */
// Java Program to create a text editor using java
import com.obtuse.exceptions.HowDidWeGetHereError;
import com.obtuse.util.OSLevelCustomizations;
import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;

public class SimpleTextEditor extends JTextArea implements ActionListener {
    // Text component
    JTextArea _textArea;

    // Frame
    private JFrame _frame;
    private JDialog _dialog;

    public SimpleTextEditor() {
        super();
    }

    // Constructor
    public SimpleTextEditor( @NotNull final JFrame frame ) {

        super();

        setOwner( frame );

    }

    public SimpleTextEditor( @NotNull final JDialog dialog ) {
        super();

        setOwner( dialog );

    }

    public void setOwner( JFrame frame ) {

        configureWindow( frame, null );

//        if ( _frame != null || _dialog != null ) {
//
//            throw new HowDidWeGetHereError(
//                    "SimpleTextEditor:  cannot attach to a JFrame because we are already attached to a " +
//                    ( _frame == null ? "JDialog" : "JFrame" )
//            );
//
//        }
//
//        _frame = frame;
//        _dialog = null;
//
//        configureWindow();

    }

    public void setOwner( JDialog dialog ) {

        configureWindow( null, dialog );

//        if ( _frame != null || _dialog != null ) {
//
//            throw new HowDidWeGetHereError(
//                    "SimpleTextEditor:  cannot attach to a JDialog because we are already attached to a " +
//                    ( _frame == null ? "JDialog" : "JFrame" )
//            );
//
//        }
//
//        _frame = null;
//        _dialog = dialog;
//
//        configureWindow();

    }

    private Window w() {

        return _frame == null ? _dialog : _frame;

    }

    private JRootPane rp() {

        return _frame == null ? _dialog.getRootPane() : _frame.getRootPane();

    }

    private void configureWindow( @Nullable final JFrame frame, @Nullable final JDialog dialog ) {

        if ( _frame != null || _dialog != null ) {

            throw new HowDidWeGetHereError(
                    "SimpleTextEditor:  already attached to a " +
                    ( _frame == null ? "JDialog" : "JFrame" ) +
                    " and configured"
            );

        }

        if ( ( frame == null ) == ( dialog == null ) ) {

            throw new HowDidWeGetHereError(
                    "SimpleTextEditor:  exactly one of frame or dialog must be specified" +
                    " (" + ( frame == null ? "neither is specified" : "both are specified" ) + ")"
            );

        }

        _frame = frame;
        _dialog = dialog;

        // Create a frame
//        _frame = new JFrame( "editor");

//        try {
//            // Set metal look and feel
//            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//
//            // Set theme to ocean
//            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
//        }
//        catch (Exception e) {
//        }

        // Text component
//        _textArea = new JTextArea();

        _textArea = this;

        // Create a menubar if our frame/dialog doesn't already have one.
        JMenuBar mb = rp().getJMenuBar();
        if ( mb == null ) {
            
            mb = new JMenuBar();
            rp().setJMenuBar( mb );
            
        }

        // Create a menu for menu
        JMenu m1 = getOrCreateMenu( mb, "File" );

//        JMenu mx = new JMenu("File");

        // Create menu items
        JMenuItem mi1 = new JMenuItem("New");
        JMenuItem mi2 = new JMenuItem("Open");
        JMenuItem mi3 = new JMenuItem("Save");
//        JMenuItem mi9 = new JMenuItem("Print");
        JMenuItem mi9 = createMenuItem(
                "Print!",
                new AbstractAction( "Print?" ) {
                    @Override
                    public void actionPerformed( final ActionEvent e ) {
                        doPrint( "abstract action");
                    }
                },
                KeyEvent.VK_P
        );

        // Add action listener
        mi1.addActionListener(this);
        mi2.addActionListener(this);
        mi3.addActionListener(this);
        mi9.addActionListener(this);

        m1.add(mi1);
        m1.add(mi2);
        m1.add(mi3);
        m1.add(mi9);

        // Create our "Edit" menu
        JMenu m2 = getOrCreateMenu( mb, "Edit" );
//        JMenu m2 = new JMenu("Edit");

        // Create menu items
        JMenuItem mi4 = createMenuItem( "Cut", TransferHandler.getCutAction(), KeyEvent.VK_X );
        JMenuItem mi5 = createMenuItem( "Copy", TransferHandler.getCopyAction(), KeyEvent.VK_C );
        JMenuItem mi6 = createMenuItem( "Paste", TransferHandler.getPasteAction(), KeyEvent.VK_V );

        //        JMenuItem mi4 = new JMenuItem("Cut");
//        mi4.setActionCommand( (String)TransferHandler.getCutAction().getValue( Action.NAME ) );
//        JMenuItem mi5 = new JMenuItem("Copy");
//        mi5.setActionCommand( (String)TransferHandler.getCopyAction().getValue( Action.NAME ) );
//        JMenuItem mi6 = new JMenuItem("Paste");
//        mi6.setActionCommand( (String)TransferHandler.getPasteAction().getValue( Action.NAME ) );

        // Add action listener
        mi4.addActionListener(this);
        mi5.addActionListener(this);
        mi6.addActionListener(this);

        m2.add(mi4);
        m2.add(mi5);
        m2.add(mi6);

//        JMenuItem mc = new JMenuItem("close");

//        mc.addActionListener(this);

        mb.add(m1);
        mb.add(m2);
//        mb.add(mc);

//        rp().setJMenuBar( mb );

//        w().add( _textArea );
//        w().setSize( 500, 500);
//        w().setVisible( true );

    }

    private JMenu getOrCreateMenu( JMenuBar mb, final String menuName ) {

        JMenu m = null;
        for ( int ix = 0; ix < mb.getMenuCount(); ix += 1 ) {

            JMenu tmp = mb.getMenu( ix );
            System.out.println( "menu[" + ix + "] is " + ObtuseUtil.enquoteJavaObject( tmp.getText() ) );
            System.out.flush();
            if ( tmp.getText().equals( menuName ) ) {

                m = tmp;

                return m;

            }

        }

//        if ( m == null ) {

            m = new JMenu( menuName );

            showMenuBar( "before", mb );
            mb.add( m );
            showMenuBar( "after", mb );

//        }

        return m;

    }

    private void showMenuBar( String who, JMenuBar mb ) {

        System.out.println( "showing menu bar for " + who );
        for ( int ix = 0; ix < mb.getMenuCount(); ix += 1 ) {

            JMenu tmp = mb.getMenu( ix );
            System.out.println( "mb[" + ix + "] == " + ObtuseUtil.enquoteJavaObject( tmp.getText() ) );

        }
    }

    private JMenuItem createMenuItem( String name, Action action, Integer kv ) {

        int mask = OSLevelCustomizations.onMacOsX() ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;
        Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

        JMenuItem jmi = new JMenuItem( name );
        if ( action != null ) {

            jmi.setActionCommand( (String)action.getValue( Action.NAME ) );

        }

        if ( kv != null ) {

            jmi.setAccelerator( KeyStroke.getKeyStroke( kv, mask ) );
            jmi.setMnemonic( kv );

        }

        return jmi;

    }

    // If a button is pressed
    public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        System.out.println( "action command is " + ObtuseUtil.enquoteToJavaString( s ) );
        switch ( s ) {
            case "Cut":
                doCut();
                break;
            case "Copy":
                doCopy();
                break;
            case "Paste":
                doPaste();
                break;
            case "Save": {
                doSave();
                break;
            }
            case "Print":
                doPrint( "action performed" );
                break;
            case "Open": {
                doOpen();
                break;
            }
            case "New":
                doNew();
                break;
            case "close":
                doClose();
                break;
            default:
                System.out.println( "unknown command" );
                ObtuseUtil.doNothing();
        }
    }

    private void doClose() {

        _frame.setVisible( false );
    }

    private void doNew() {

        _textArea.setText( "" );
    }

    private void doOpen() {
        // Create an object of JFileChooser class
        JFileChooser j = new JFileChooser( "f:" );

        // Invoke the showsOpenDialog function to show the save dialog
        int r = j.showOpenDialog( null );

        // If the user selects a file
        if ( r == JFileChooser.APPROVE_OPTION ) {
            // Set the label to the path of the selected directory
            File fi = new File( j.getSelectedFile()
                                 .getAbsolutePath() );

            try {
                // String
                String s1 = "", sl = "";

                // File reader
                FileReader fr = new FileReader( fi );

                // Buffered reader
                BufferedReader br = new BufferedReader( fr );

                // Initialize sl
                sl = br.readLine();

                // Take the input from the file
                while ( ( s1 = br.readLine() ) != null ) {
                    sl = sl + "\n" + s1;
                }

                // Set the text
                _textArea.setText( sl );
            } catch ( Exception evt ) {
                JOptionPane.showMessageDialog( _frame, evt.getMessage() );
            }
        }
        // If the user cancelled the operation
        else
            JOptionPane.showMessageDialog( _frame, "the user cancelled the operation" );
    }

    private void doPrint( String who ) {

        System.out.println( who + " is doing print" );
        ObtuseUtil.doNothing();

        if ( false )
        try {
            // print the file
            _textArea.print();
        } catch ( Exception evt ) {
            JOptionPane.showMessageDialog( _frame, evt.getMessage() );
        }
    }

    private void doSave() {
        // Create an object of JFileChooser class
        JFileChooser j = new JFileChooser( "f:" );

        // Invoke the showsSaveDialog function to show the save dialog
        int r = j.showSaveDialog( null );

        if ( r == JFileChooser.APPROVE_OPTION ) {

            // Set the label to the path of the selected directory
            File fi = new File( j.getSelectedFile()
                                 .getAbsolutePath() );

            try {
                // Create a file writer
                FileWriter wr = new FileWriter( fi, false );

                // Create buffered writer to write
                BufferedWriter w = new BufferedWriter( wr );

                // Write
                w.write( _textArea.getText() );

                w.flush();
                w.close();
            } catch ( Exception evt ) {
                JOptionPane.showMessageDialog( _frame, evt.getMessage() );
            }
        }
        // If the user cancelled the operation
        else
            JOptionPane.showMessageDialog( _frame, "the user cancelled the operation" );
    }

    private void doPaste() {

        _textArea.paste();
    }

    private void doCopy() {

        _textArea.copy();
    }

    private void doCut() {

        _textArea.cut();
    }

    // Main class
    public static void main(String args[])
    {
        SimpleTextEditor e = new SimpleTextEditor( new JFrame() );
    }
}