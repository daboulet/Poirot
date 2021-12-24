package xyz.kenosee.poirot.config;

import java.io.File;

/**
 Created by danny on 2021/11/27.
 */
public class PoirotConfig {

//    public static final String POIROT_FILES_DIRECTORY = System.getProperty( "user.home" ) + "/PoirotConfig";
//
//    public static final String CACHED_WORK_FILES_DIRECTORY = POIROT_FILES_DIRECTORY + "/WorkFiles";

    public static final String COPY_AND_PASTE_TARGET_ICON_FILE = "Copy_and_Paste_target.png";

    public static final File POIROT_IMAGE_REPOSITORY = new File(
            new File( System.getProperty( "user.home" ) ),
            "PoirotImageRepository"
    );

    public static final String ICON_OF_LAST_RESORT = "icon_of_last_resort.png";
}
