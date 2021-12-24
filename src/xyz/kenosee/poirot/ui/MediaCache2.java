package xyz.kenosee.poirot.ui;

import com.obtuse.exceptions.HowDidWeGetHereError;
import com.obtuse.ui.ObtuseImageIdentifier;
import com.obtuse.util.lrucache.CachedThing;
import com.obtuse.util.lrucache.LruCache;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Optional;
import java.util.UUID;

/**
 An image cache.
 */

public class MediaCache2 extends LruCache<UUID,ImageIcon> {

    private static final MediaCache2 s_instance = new MediaCache2();

    private static final Fetcher<UUID, ImageIcon> s_fetcher = new Fetcher<UUID, ImageIcon>() {
        @Override
        public @NotNull Optional<CachedThing<UUID, ImageIcon>> fetch(
                final @NotNull UUID key,
                final boolean nullOk
        ) {

            // Try to load the image. If this fails, the ImageIcon will not have an image reference.
            // Return an empty Optional if the load of the image failed.
            // Return an Optional containing the ImageIcon if the load of the image worked.

            @NotNull ImageIcon imageIcon = fetchMediaItem( key );
            if ( imageIcon.getImage() == null ) {

                return Optional.empty();

            } else {

                return Optional.of( new CachedThing<>( key, imageIcon ) );

            }

        }

        private ImageIcon fetchMediaItem( final UUID key ) {

            return null;

        }

    };

    /**
     This is a utility class.
     */

    private MediaCache2() {
        super(
                "MediaCache2",
                s_fetcher
        );

    }

    public static MediaCache2 getInstance() {

        return s_instance;

    }

    public String toString() {

        return super.toString();

    }

}
