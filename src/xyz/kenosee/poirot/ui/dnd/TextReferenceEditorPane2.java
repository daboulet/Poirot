package xyz.kenosee.poirot.ui.dnd;

import com.obtuse.util.ObtuseUtil;
import xyz.kenosee.poirot.data.TextReferenceItem;
import xyz.kenosee.poirot.play.texteditor.SimpleTextEditor;
import xyz.kenosee.poirot.ui.CommonReferenceItemEditDialog;
import xyz.kenosee.poirot.ui.ReferenceEditorPane;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;
import java.awt.*;

/**
 Created by danny on 2021/12/23.
 */
public class TextReferenceEditorPane2 extends ReferenceEditorPane<TextReferenceItem> {

    private JPanel _innerPanel;

    private SimpleTextEditor _textEditorPane;
    private JScrollPane _textEditorScrollPane;

    private final RTFEditorKit _rtfEditorKit = new RTFEditorKit();
    private Document _rtfDocument;
    private SimpleTextEditor _simpleTextEditor;

    public TextReferenceEditorPane2( TextReferenceItem sri ) {
        super( sri );

//        setLayout( new BorderLayout() );
//        add( _panel1, BorderLayout.CENTER );

        setMinimumSize( new Dimension( 300, 200 ) );

//        SimpleTextEditor simpleTextEditor = new SimpleTextEditor();
//        simpleTextEditor.setOwner( jDialog );
//        _textEditorScrollPane.setViewportView( simpleTextEditor );

    }

//    public static void main( String[] args ) {
//
//        BasicProgramConfigInfo.init( "Kenosee", "Poirot", "testing" );
//
//        String wordsGoHere = "words go here";
//        TextReferenceItem sri = new TextReferenceItem(
//                wordsGoHere,
//                ReferenceItem.UNKNOWN_SOURCE
//        );
//        ReferenceEditorPane editorPane = new TextReferenceEditorPane( sri );
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
    public void populate(
            final CommonReferenceItemEditDialog<TextReferenceItem> mainEditDialog
    ) {

        String itemText = getReferenceItem().getOptTextValue()
                                            .orElse( "" );

        //            try {

        _simpleTextEditor = new SimpleTextEditor( mainEditDialog );
        _simpleTextEditor.setText( itemText );
        _textEditorScrollPane = new JScrollPane( _simpleTextEditor );
//        _textEditorScrollPane.setViewportView( _simpleTextEditor );
        setLayout( new BorderLayout() );
        add( BorderLayout.CENTER, _textEditorScrollPane );
//                _rtfDocument = _rtfEditorKit.createDefaultDocument();
//                _textEditorPane.setEditorKit( _rtfEditorKit );
//
//                StringReader reader = new StringReader( itemText );
//                _rtfEditorKit.read( reader, _rtfDocument, 0 );

        ObtuseUtil.doNothing();

//            } catch ( IOException e ) {
//
//                Logger.logErr( "java.io.IOException caught", e );
//
//                ObtuseUtil.doNothing();
//
//            } catch ( BadLocationException e ) {
//
//                Logger.logErr( "javax.swing.text.BadLocationException caught", e );
//
//                ObtuseUtil.doNothing();
//
//            }

        ObtuseUtil.doNothing();

    }

//    private void example1( String itemText ) {
//
//        JEditorPane _txtHelp = _textEditorPane;
//        final RTFEditorKit RTF_KIT = _rtfEditorKit;
//        _txtHelp.setContentType("text/rtf");
//        final InputStream inputStream = new ByteArrayInputStream( itemText.getBytes() );
//        final DefaultStyledDocument styledDocument = new DefaultStyledDocument( new StyleContext());
//        try {
//
//            RTF_KIT.read(inputStream, styledDocument, 0);
//
//            ObtuseUtil.doNothing();
//
//        } catch ( IOException e ) {
//
//            Logger.logErr( "java.io.IOException caught", e );
//
//            ObtuseUtil.doNothing();
//
//        } catch ( BadLocationException e ) {
//
//            Logger.logErr( "javax.swing.text.BadLocationException caught", e );
//
//            ObtuseUtil.doNothing();
//
//        }
//
//        _txtHelp.setDocument(styledDocument);
//
//        ObtuseUtil.doNothing();
//
//    }
//
//    public void example2( String itemText ) {
//
//        _textEditorPane.setContentType( "text/html" );
//        ObtuseUtil.doNothing();
//        _textEditorPane.setText( "<html><body><h1>Hello</h1></body></html>" );
//        ObtuseUtil.doNothing();
//    }
//
//    public void example3( String itemText ) {
//
//        _textEditorPane.setContentType( "text/rtf" );
//        ObtuseUtil.doNothing();
//        _textEditorPane.setText( "<html><body><h1>Hello</h1></body></html>" );
//        ObtuseUtil.doNothing();
//    }

    public String toString() {

        return "TextReferenceEditorPane( " + getReferenceItem() + " )";

    }

}
