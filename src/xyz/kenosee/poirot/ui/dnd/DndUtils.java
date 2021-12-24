package xyz.kenosee.poirot.ui.dnd;

import com.obtuse.util.Logger;
import com.obtuse.util.ObtuseUtil;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 Created by danny on 2021/12/16.
 */

public class DndUtils {

    public static Optional<Object> tryTransfer( final Transferable transferable, DataFlavor flavor ) {

        try {

            Object value = transferable
                    .getTransferData( flavor );

            Logger.logMsg( "tryTransfer:  got the " + flavor.getHumanPresentableName() + " - value" );

            return Optional.of( value );

        } catch ( UnsupportedFlavorException e ) {

            Logger.logErr(
                    "tryTransfer:  " +
                    "java.awt.datatransfer.UnsupportedFlavorException caught for flavor " +
                    flavor.getHumanPresentableName(),
                    e
            );

            return Optional.empty();

        } catch ( IOException e ) {

            Logger.logErr( "tryTransfer:  java.io.IOException caught", e );

            return Optional.empty();

        } catch ( Throwable e ) {

            if ( e instanceof java.awt.dnd.InvalidDnDOperationException ) {

                // DoDo happens - log this one fairly quietly.

                Logger.logMsg( "tryTransfer:  " + e.getClass().getCanonicalName() + " caught and ignored" );

                return Optional.empty();

            } else {

                Logger.logErr( "tryTransfer:  java.lang.Throwable caught", e );

                return Optional.empty();

            }

        }

    }

    public static String formatSupportedMimeTypes( TransferHandler.TransferSupport info ) {

        SortedSet<String> seen = new TreeSet<>();
        StringBuffer sb = new StringBuffer();

        String comma = "";
        for ( DataFlavor dataFlavor : info.getDataFlavors() ) {

            String humanPresentableName = dataFlavor.getHumanPresentableName();
            if ( !seen.contains( humanPresentableName ) ) {

                seen.add( humanPresentableName );
                sb.append( comma )
                  .append( humanPresentableName );

                comma = ", ";

            }

        }

        return "[" + sb + "]";

    }
}
