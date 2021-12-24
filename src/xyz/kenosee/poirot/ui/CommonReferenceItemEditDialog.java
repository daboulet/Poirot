package xyz.kenosee.poirot.ui;

import org.jetbrains.annotations.NotNull;
import xyz.kenosee.poirot.data.ReferenceItem;
import xyz.kenosee.poirot.main.PoirotMainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CommonReferenceItemEditDialog<RI extends ReferenceItem> extends JDialog {

    private final ReferenceEditorPane<RI> _typeSpecificEditPanel;
    private JPanel _contentPane;
    private JButton _OkButton;
    private JButton _cancelButton;
    private JTextField _shortDescriptionTextField;
    private JTextField _sourceTextField;
    private JPanel _typeSpecificFieldsPanel;

    public CommonReferenceItemEditDialog( @NotNull PoirotMainWindow owner, ReferenceEditorPane<RI> typeSpecificEditPanel ) {
        super( owner );

        setContentPane( _contentPane );
        setModal( false );
        getRootPane().setDefaultButton( _OkButton );

        _typeSpecificEditPanel = typeSpecificEditPanel;

        _typeSpecificFieldsPanel.setLayout( new BorderLayout() );
        _typeSpecificFieldsPanel.add( typeSpecificEditPanel, BorderLayout.CENTER );

        _typeSpecificEditPanel.setPoirotMainWindow( owner );

        _OkButton.addActionListener(
                e -> onOK()
        );

        _cancelButton.addActionListener(
                e -> onCancel()
        );

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
        _contentPane.registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );

    }

    @SuppressWarnings("unused")
    public void makeVisible() {

        _typeSpecificEditPanel.aboutToMakeVisible();

    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void populateDialog() {

        String descriptiveName = _typeSpecificEditPanel.getReferenceItem()
                                                       .getDescriptiveName();
        _shortDescriptionTextField.setText( descriptiveName );

        String sourceText = _typeSpecificEditPanel.getReferenceItem()
                                                  .getOptSourceText()
                                                  .orElse( "" );
        _sourceTextField.setText( sourceText );

        _typeSpecificEditPanel.populate( this );

    }

}
