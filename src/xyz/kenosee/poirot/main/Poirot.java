package xyz.kenosee.poirot.main;

import com.obtuse.util.BasicProgramConfigInfo;
import com.obtuse.util.ImageIconUtils;
import com.obtuse.util.Logger;

import java.util.UUID;

public class Poirot {

    public static void main(String[] args) {

        BasicProgramConfigInfo.init( "Kenosee", "Poirot", "main" );

//        ImageIconUtils.exploreImageIconApiErrors();

        ImageIconUtils.setDefaultResourcesDirectory( "./src/xyz/kenosee/poirot/config/resources" );

        Logger.logMsg( "hello world" );

        UUID uuid = UUID.randomUUID();
        Logger.logMsg( "uuid=" + uuid.toString() );

        PoirotMainWindow pmw = new PoirotMainWindow();
        pmw.setVisible( true );

    }

}
