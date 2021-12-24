package xyz.kenosee.poirot.ui.dnd;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.kenosee.poirot.data.FileReferenceItem;
import xyz.kenosee.poirot.data.ReferenceItem;

import java.awt.*;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 Created by danny on 2021/12/21.
 */
public class FileDataCarrier extends DataCarrier {

    private final File _file;
    private final boolean _isSupportedImageType;
    private WeakReference<Image> _weakImageReference;

    public FileDataCarrier( @NotNull final File file, final boolean isSupportedImageType ) {

        super();

        _file = file;

        _isSupportedImageType = isSupportedImageType;

    }

//        public boolean isSupportedImageType() {
//
//            return _isSupportedImageType;
//
//        }

////            FormattingList<File> fileList = new FormattingList<>();
////            int ix = 0;
////            for ( Object obj : javaList ) {
//
//                if ( obj instanceof File file ) {
//
//                    fileList.add( file );
//
//                } else if ( obj == null ) {
//
//                    throw new IllegalArgumentException(
//                            "FileListDataCarrier:  element #" + ix + " is null"
//                    );
//
//                } else {
//
//                    throw new IllegalArgumentException(
//                            "FileListDataCarrier:  " +
//                            "element #" + ix + " is something unexpected (" + obj.getClass() + ")"
//                    );
//
//                }
//
//            }
//
//            _fileList = fileList;
//
//        }

    @Nullable
    public File getOptionalData() {

        return _file;

    }

    @NotNull
    public File getMandatoryData() {

        return _file;

    }

    @Override
    public void createReferenceItem( final List<ReferenceItem> referenceItems ) {

        File file = getMandatoryData();

//            if ( isSupportedImageType() ) {
//
//                try {
//
//                    Image image = getOrFetchImage();
//                    ImageIcon icon = new ImageIcon( image );
//                    if ( icon.getImageLoadStatus() == MediaTracker.COMPLETE ) {
//
//                        optImageReferenceItem = createAndSaveImageReferenceItem(
//                                referenceItems,
//                                icon,
//                                file.getAbsolutePath()
//                        );
//
//                    } else {
//
//                        boom!
//
//                    }
//
//                } catch ( ImageFetchFailedException e ) {
//
//                    Logger.logMsg(
//                            "PoirotJListTransferHandler.getReferenceItems:  " +
//                            ObtuseUtil.enquoteJavaObject( file ) + " does not contain a valid image " +
//                            "(informational,fc=" + e.getFailureCause() + ")"
//                    );
//
//                    ObtuseUtil.doNothing();
//
//                }
//
//                //                            if ( optImageReferenceItem.isEmpty() ) {
//                //
//                //                                FileReferenceItem fileReferenceItem = new FileReferenceItem(
//                //                                        file,
//                //                                        "DnD or CnP"
//                //                                );
//                //
//                //                                referenceItems.add( fileReferenceItem );
//                //
//                //                            }
//
//            }
//
//             %%% We need to save it as a file reference if it doesn't contain an image.
//
//            if ( optImageReferenceItem.isEmpty() ) {

        FileReferenceItem fileReferenceItem = new FileReferenceItem(
                file,
                "DnD or CnP"
        );

        referenceItems.add( fileReferenceItem );

//            }

    }

    @NotNull
    public DataClass getDataClass() {

        return DataClass.FILE;

    }

}
