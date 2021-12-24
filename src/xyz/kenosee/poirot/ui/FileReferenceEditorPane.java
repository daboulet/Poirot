package xyz.kenosee.poirot.ui;

import com.obtuse.util.BasicProgramConfigInfo;
import com.obtuse.util.ObtuseUtil;
import xyz.kenosee.poirot.data.FileReferenceItem;
import xyz.kenosee.poirot.data.ReferenceItem;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 Created by danny on 2021/12/05.
 */

public class FileReferenceEditorPane extends ReferenceEditorPane<FileReferenceItem> {

    private JPanel _panel1;
    private JTextField _filenameTextField;
    private JComboBox _filetypeComboBox;

    public FileReferenceEditorPane( FileReferenceItem fri ) {
        super( fri );

        setLayout( new BorderLayout() );
        add( _panel1, BorderLayout.CENTER );

    }

//    public static void main( String[] args ) {
//
//        BasicProgramConfigInfo.init( "Kenosee", "Poirot", "testing" );
//
//        String pathname = "/Users/danny/Junk/poirot text file.txt";
//        FileReferenceItem fri = new FileReferenceItem(
//                new File( pathname ),
//                ReferenceItem.UNKNOWN_SOURCE
//        );
//        FileReferenceEditorPane editorPane = new FileReferenceEditorPane( fri );
//        CommonReferenceItemEditDialog<FileReferenceItem> dialog = new CommonReferenceItemEditDialog<>( null, editorPane );
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
    public void populate( final CommonReferenceItemEditDialog<FileReferenceItem> mainEditDialog ) {

    }

    public String toString() {

        return "FileReferenceEditorPane( " + getReferenceItem() + " )";

    }

}
