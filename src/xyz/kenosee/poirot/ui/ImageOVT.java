package xyz.kenosee.poirot.ui;

import com.obtuse.ui.ObtuseImageFile;
import com.obtuse.util.ObtuseUtil;
import com.obtuse.util.ovt.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Optional;
import java.util.SortedMap;
import java.util.UUID;

/**
 Created by danny on 2021/12/01.
 */

public class ImageOVT {

    private final ObtuseImageFile _imageFile;
    private final ObtuseValuesTable _ovt;

    public ImageOVT( @NotNull final ObtuseImageFile imageFile ) throws IOException {
        super();

        _imageFile = imageFile;

        _ovt = loadOvt( imageFile );

    }

    public static String constructObtuseValuesTableName( ObtuseImageFile imageFile ) {

        return "OVT for image sn " + imageFile.getUuid();

    }

    @NotNull
    public Optional<File> serialize() throws IOException, OvtSerializationException {

        @NotNull File tmpFilePath = ObtuseImageFile.constructCachedImagRepositoryFileObject(
                _imageFile.getUuid(),
                ObtuseValuesTableDeserializer.TMP_OBTUSE_VALUES_TABLE_FILE_SUFFIX
        );

        @NotNull File permFilePath = ObtuseImageFile.constructCachedImagRepositoryFileObject(
                _imageFile.getUuid(),
                ObtuseValuesTableDeserializer.PERM_OBTUSE_VALUES_TABLE_FILE_SUFFIX
        );

//        try (
                FileWriter fileWriter = new FileWriter( tmpFilePath );
                ObtuseValuesTableSerializer serializer = new ObtuseValuesTableSerializer(
                        ImageOVT.constructObtuseValuesTableName( _imageFile ),
                        fileWriter
                );
//        ) {

            serializer.serializeTable( true, _ovt );

            if ( tmpFilePath.renameTo( permFilePath ) ) {

                return Optional.of( tmpFilePath );

            }

            throw new OvtSerializationException(
                    "close",
                    "unable to rename " +
                    ObtuseUtil.enquoteJavaObject( tmpFilePath ) +
                    " as " +
                    ObtuseUtil.enquoteJavaObject( permFilePath )
            );

//        } catch ( IOException e ) {
//
//            Logger.logErr( "java.io.IOException caught", e );
//
//            ObtuseUtil.doNothing();
//
//        } catch ( OvtSerializationException e ) {
//
//            Logger.logErr( "OvtSerializationException caught", e );
//
//            ObtuseUtil.doNothing();
//
//        }

    }

    public static ObtuseValuesTable loadOvt( @NotNull final ObtuseImageFile imageFile ) throws IOException {

        UUIDWrapperManager uuidWrapperManager = new UUIDWrapperManager(
                new UUIDWrapperFactory[]{
                        new UUIDWrapperFactory( "TableUUIDWrapper" ) {
                            @Override
                            public UUIDWrapper createWrapper( final String typeName, final UUID uuid ) {

                                return new TableUUIDWrapper( uuid );

                            }

                        },
                        new UUIDWrapperFactory( "GenericUUIDWrapper" ) {
                            @Override
                            public UUIDWrapper createWrapper( final String typeName, final UUID uuid ) {

                                return new GenericUUIDWrapper( uuid );

                            }

                        }
                }
        );

        String imageOvtName = constructObtuseValuesTableName( imageFile );
        try (
                ObtuseValuesTableDeserializer deserializer = new ObtuseValuesTableDeserializer(
                        imageOvtName,
                        ObtuseImageFile.constructCachedImagRepositoryFileObject(
                                imageFile.getUuid(),
                                ObtuseValuesTableDeserializer.PERM_OBTUSE_VALUES_TABLE_FILE_SUFFIX
                        ),
                        uuidWrapperManager
                )
        ) {

            ObtuseUtil.doNothing();

            deserializer.parse();

            ObtuseUtil.doNothing();

            SortedMap<String, ObtuseValuesTable> tablesByNameMap = deserializer.getTablesByNameMap();

            ObtuseValuesTable ovt = tablesByNameMap.get( imageOvtName );

            if ( ovt == null ) {

                throw new IllegalArgumentException(
                        "no ImageOVT found with name " + ObtuseUtil.enquoteToJavaString( imageOvtName )
                );

            }

            return ovt;

        }

    }
}
