package xyz.kenosee.poirot.ui;

import com.obtuse.ui.ObtuseImageIdentifier;

/**
 Something that is interested in when a {@link LancotScrollableImagesPanel}'s collection of selected items may have changed.
 */

public interface MediaItemSelectionWatcher {

    void itemSelectionMayHaveChanged(
            final boolean forceOpenOsViewerItemEnabled,
            final boolean forceOpenViewerItemEnabled,
            final boolean forceExportMediaItemsEnabled,
            final java.util.Collection<ObtuseImageIdentifier> selectedMediaItems
    );

}
