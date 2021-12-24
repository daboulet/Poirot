package xyz.kenosee.poirot.ui.dnd;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by danny on 2021/12/19.
 */

public class ImageFetchFailedException extends Exception {

    public enum FailureCause {
        WORKED,
        CANNOT_READ_IMAGE_FILE,
        NOT_AN_IMAGE
    }

    @NotNull
    private final FailureCause _failureCause;

    public ImageFetchFailedException( @NotNull String message, @NotNull FailureCause failureCause ) {
        super( message + " (" + failureCause + ")" );

        _failureCause = failureCause;

    }

    public ImageFetchFailedException( @NotNull String message, @NotNull FailureCause failureCause, @Nullable Throwable e ) {
        super( message + " (" + failureCause + ")", e );

        _failureCause = failureCause;

    }

    @NotNull
    public FailureCause getFailureCause() {

        return _failureCause;

    }

}
