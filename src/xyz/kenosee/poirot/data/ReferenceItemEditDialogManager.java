package xyz.kenosee.poirot.data;

import com.obtuse.exceptions.HowDidWeGetHereError;
import com.obtuse.util.Logger;
import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.kenosee.poirot.main.PoirotMainWindow;
import xyz.kenosee.poirot.ui.CommonReferenceItemEditDialog;
import xyz.kenosee.poirot.ui.ReferenceEditorPane;

import javax.swing.*;
import java.awt.*;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 Created by danny on 2021/12/05.
 */

public class ReferenceItemEditDialogManager {

    private static final SortedMap<UUID, CommonReferenceItemEditDialog<?>> s_dialogs = new TreeMap<>();

    public static void launchOrShowItemEditDialog(
            @NotNull final PoirotMainWindow poirotMainWindow,
            @NotNull JList<ReferenceItem> jList,
            final int index
    ) {

        ListModel<ReferenceItem> listModel = jList.getModel();
        if ( index < 0 || index >= listModel.getSize() ) {

            throw new HowDidWeGetHereError(
                    "PoirotMainWindow.launchOrShowItemEditDialog:  JList has " +
                    listModel.getSize() + " but we've been asked to launch/show element " + index
            );

        }

        ReferenceItem item = listModel.getElementAt( index );

        launchOrShowItemEditDialog( poirotMainWindow, item );

    }

    public static void launchOrShowItemEditDialog(
            @Nullable final PoirotMainWindow poirotMainWindow,
            @NotNull final ReferenceItem item
    ) {

//        CommonReferenceItemEditDialog itemEditDialog = s_dialogs.get( item.getUuid() );
//        if ( itemEditDialog == null ) {

        CommonReferenceItemEditDialog<?> itemEditDialog = s_dialogs.get( item.getUuid() );
        if ( itemEditDialog == null ) {

            itemEditDialog = createEditDialog( poirotMainWindow, item );
            s_dialogs.put( item.getUuid(), itemEditDialog );

        }

//        }

        String doing = itemEditDialog.isVisible() ? "making dialog visible" : "editing dialog";
        Logger.logMsg( doing + " for " + item.getUuid() + " (" + ObtuseUtil.enquoteJavaObject( item ) + ")" );


        itemEditDialog.setVisible( true );
        itemEditDialog.toFront();

//        itemEditDialog.setExtendedState(JFrame.ICONIFIED);
//        frame.setExtendedState(fullscreen ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);


//        if(itemEditDialog.getState()!=Frame.NORMAL) { setState(Frame.NORMAL); }
//        toFront();
//        repaint();

//        final CommonReferenceItemEditDialog finalEditDialog = itemEditDialog;
//
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                finalEditDialog.toFront();
//                finalEditDialog.repaint();
//            }
//        });

    }

    /**
     Create a {@link CommonReferenceItemEditDialog}.
     <p>This method actually creates the universal parts of an item editor and uses a 'helper'
     in the {@link ReferenceItem} to create the item-type-specific parts.</p>
     @return the dialog.
     @param poirotMainWindow the {@link PoirotMainWindow} instance that is driving this car.
     @param item the {@link ReferenceItem}.
     */

    @NotNull
    private static CommonReferenceItemEditDialog<?> createEditDialog(
            @NotNull final PoirotMainWindow poirotMainWindow,
            final ReferenceItem item
    ) {

        @NotNull ReferenceEditorPane<?> typeSpecificEditPanel = item.createEditDialog();

        @NotNull CommonReferenceItemEditDialog<?> editDialog =
                new CommonReferenceItemEditDialog<>( poirotMainWindow, typeSpecificEditPanel );

        editDialog.populateDialog();

        editDialog.pack();

        Dimension minimumSize = new Dimension( editDialog.getMinimumSize().width, editDialog.getMinimumSize().height );
        Logger.logMsg( "setting dialog minimum size to " + ObtuseUtil.fDim( minimumSize ) );
        editDialog.setMinimumSize( minimumSize );

        return editDialog;

    }

}
