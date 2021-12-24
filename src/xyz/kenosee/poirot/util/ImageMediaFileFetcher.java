package xyz.kenosee.poirot.util;

import com.obtuse.exceptions.HowDidWeGetHereError;
import com.obtuse.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 Load image media files.
 */

public class ImageMediaFileFetcher {

    public static final Set<String> FULLY_SUPPORTED_IMAGE_FILE_SUFFIXES = Set.of( "jpg", "jpeg", "png", "gif" );
    public static final Set<String> BARELY_SUPPORTED_IMAGE_FILE_SUFFIXES = Set.of( "tif", "tiff", "heic" );
    public static final Set<String> SUPPORTED_IMAGE_FILE_SUFFIXES = Set.of(
            ObtuseSets.union(
                              FULLY_SUPPORTED_IMAGE_FILE_SUFFIXES,
                              BARELY_SUPPORTED_IMAGE_FILE_SUFFIXES
                      )
                      .toArray( new String[0] ) );

    @NotNull
    public static MediaFileContents fetchMediaFile(
            @NotNull final File inputMediaFileContentHandle
    ) {

        @NotNull MediaFileContents rval = fetchMediaFile(
                inputMediaFileContentHandle,
                null,
                null
        );

        return rval;

    }

    @NotNull
    public static MediaFileContents fetchMediaFile(
            @NotNull final File inputMediaFile,
            @Nullable final String inputMediaFileFormat,
            @Nullable final String outputMediaFileFormat
    ) {

        Process proc = null;
        byte[] imageFileBytes;

        String presumedInputFileSuffix;
        String presumedOutputFileSuffix;

        AsyncStreamCaptor stdoutReader = null;
        AsyncStreamCaptor stderrReader = null;

        try ( Measure ignored1 = new Measure( "external scaling" ) ) {

            try ( Measure ignored = new Measure( "async imageFetch" ) ) {

                // Figure out what the input file's actual or presumed format is called (jpg, png, tif, etc).

                final boolean inputFileSuffixOverridden = inputMediaFileFormat != null;
                if ( inputFileSuffixOverridden ) {

                    presumedInputFileSuffix = inputMediaFileFormat.toLowerCase();

                } else {

                    String inputFileName = inputMediaFile.getName();
                    if ( inputFileName.contains( "." ) ) {

                        presumedInputFileSuffix =
                                inputFileName.substring( 1 + inputFileName.lastIndexOf( '.' ) ).toLowerCase();
                        Logger.logMsg(
                                "actual input file suffix is " +
                                ObtuseUtil.enquoteToJavaString( presumedInputFileSuffix )
                        );

                    } else {

                        //noinspection ConstantConditions
                        return new MediaFileContents(
                                inputMediaFile,
                                "no override input file suffix provided and input file has no suffix",
                                stdoutReader,
                                stderrReader
                        );

                    }

                }

                // Make sure that the presumed input file suffix is supported.

                if ( !isSupportedImageFileSuffix( presumedInputFileSuffix ) ) {

                    //noinspection ConstantConditions
                    return new MediaFileContents(
                            inputMediaFile,
                            ( inputFileSuffixOverridden ? "override " : "actual " ) +
                            "input file format " +
                            ObtuseUtil.enquoteToJavaString( presumedInputFileSuffix ) +
                            " is not supported",
                            stdoutReader,
                            stderrReader
                    );

                }

                // Figure out what the output file's format is called.

                @SuppressWarnings("unused") final boolean outputFileSuffixOverridden = outputMediaFileFormat != null;
                if ( outputMediaFileFormat == null ) {

                    if ( isSupportedImageFileSuffix( presumedInputFileSuffix ) ) {

                        if ( isBarelySupportedImageFileSuffix( presumedInputFileSuffix ) ) {

                            presumedOutputFileSuffix = "jpg";
                            Logger.logMsg(
                                    "output file suffix presumed to be " +
                                    ObtuseUtil.enquoteToJavaString( presumedInputFileSuffix ) +
                                    " because presumed input file suffix is barely supported image format"
                            );

                        } else {

                            presumedOutputFileSuffix = presumedInputFileSuffix;
                            Logger.logMsg(
                                    "output file suffix presumed to be " +
                                    ObtuseUtil.enquoteToJavaString( presumedOutputFileSuffix ) +
                                    " because presumed input file suffix is a fully supported image format"
                            );

                        }

//                    } else if ( LancotFiles.isSupportedVideoFileSuffix( presumedInputFileSuffix ) ) {
//
//                        presumedOutputFileSuffix = "jpg";
//                        Logger.logMsg(
//                                "output file suffix presumed to be " +
//                                ObtuseUtil.enquoteToJavaString( presumedOutputFileSuffix ) +
//                                " because presumed input file suffix is a supported video format"
//                        );

                    } else {

                        Logger.logMsg(
                                "output file suffix cannot be presumed due to insufficient information"
                        );

                        //noinspection ConstantConditions
                        return new MediaFileContents(
                                inputMediaFile,
                                "unsupported output file format " +
                                ObtuseUtil.enquoteToJavaString( presumedInputFileSuffix ),
                                stdoutReader,
                                stderrReader
                        );

                    }

                } else {

                    presumedOutputFileSuffix = outputMediaFileFormat;

                }

                // Make sure that the presumed output file suffix is supported.

                if ( !isSupportedImageFileSuffix( presumedOutputFileSuffix ) ) {

                    //noinspection ConstantConditions
                    return new MediaFileContents(
                            inputMediaFile,
                            "unsupported output file format " +
                            ObtuseUtil.enquoteToJavaString( presumedOutputFileSuffix ),
                            stdoutReader,
                            stderrReader
                    );

                }

                // We use ImageMagick if the input format is any supported image format.

                ProcessBuilder pbFetch;

                if (
                        isSupportedImageFileSuffix( presumedInputFileSuffix )
                        /*&&
                        !isBarelySupportedImageFileSuffix( presumedInputFileSuffix )*/
                ) {

                    String kludgedOutputFileFormat =
                            "jpeg".equals( presumedOutputFileSuffix )
                                    ?
                                    "jpg"
                                    :
                                    presumedOutputFileSuffix;

                    String[] magickParams = {
                            "magick",
                            "convert",
                            /*kludgedInputFileFormat + ":" +*/ inputMediaFile.getPath(),
                            kludgedOutputFileFormat + ":-"
                    };

                    Logger.logMsg( "launching " + Arrays.toString( magickParams ) );
                    pbFetch = new ProcessBuilder( magickParams );

//                } else if ( LancotFiles.isSupportedVideoFileSuffix( presumedInputFileSuffix ) ) {
//
//                    // We use ffmpeg if the input format is a video format.
//                    // or a barely supported image format (e.g. HEIC).
//
//                    String[] ffmpegParams = {
//                            "ffmpeg",
//                            "-i",
//                            inputMediaFile.getPath(),
//                            "-nostdin",
//                            "-ss",
//                            "00:00:00.000",
//                            "-f",
//                            "image2pipe",
//                            "-vcodec",
//                            "png",
//                            "-vframes",
//                            "1",
//                            "-"
//                    };
//
//                    Logger.logMsg( "launching " + Arrays.toString( ffmpegParams ) );
//                    pbFetch = new ProcessBuilder( ffmpegParams );

                } else {

                    throw new HowDidWeGetHereError(
                            "LancotFiles.fetchMediaFile:  " +
                            "input file suffix " + ObtuseUtil.enquoteToJavaString( presumedInputFileSuffix ) +
                            " is not any of our supported image or video formats (should have been caught earlier)"
                    );

                }

                proc = pbFetch.start();

                // Let's read the stdout and stderr streams (in the background to avoid deadlocks and SIGPIPEs).

                stderrReader = new AsyncStreamCaptor(
                        "stderr reader",
                        proc.getErrorStream(),
                        null,
                        "stderr reader for " + inputMediaFile.getPath(),
                        "stderr reader",
                        1024 * 1024,
                        true,
                        true,
                        true
                );

                stdoutReader = new AsyncStreamCaptor(
                        "stdout reader",
                        proc.getInputStream(),
                        null,
                        "stdout reader for " + inputMediaFile.getPath(),
                        "stdout reader",
                        100 * 1024 * 1024,
                        true,
                        true,
                        true
                );

                stdoutReader.waitUntilDone();
                stderrReader.waitUntilDone();

                ObtuseUtil.doNothing();

                if ( !stdoutReader.wasAllDataCaptured() ) {

                    String msg = "some of the data was not captured from " + inputMediaFile.getPath() +
                                 " for " + ObtuseUtil.enquoteJavaObject( inputMediaFile );
                    Logger.logErr( msg );

                    return new MediaFileContents( inputMediaFile, msg, stdoutReader, stderrReader );

                }

                imageFileBytes = stdoutReader.getCapturedStream()
                                             .toByteArray();

                ObtuseUtil.doNothing();

            }

        } catch ( IOException e ) {

            String msg = "fetch image via " + inputMediaFile.getPath() + " of " +
                         ObtuseUtil.enquoteJavaObject( inputMediaFile ) + ":  " + e;
            Logger.logErr(
                    msg,
                    e
            );

            //noinspection ConstantConditions
            return new MediaFileContents( inputMediaFile, msg, stdoutReader, stderrReader );

        } finally {

            if ( proc == null ) {

                Logger.logMsg(
                        "LancotMediaItem.fetchFrame:  " +
                        inputMediaFile.getPath() + " failed (process never created) " +
                        " for " + ObtuseUtil.enquoteJavaObject( inputMediaFile )
                );

                ObtuseUtil.doNothing();

            } else {

                try {

                    int exitStatus = proc.waitFor();
                    if ( exitStatus == 0 ) {

                        ObtuseUtil.doNothing();

                    } else {

                        imageFileBytes = null;

                        Logger.logMsg(
                                "LancotMediaItem.fetchFrame:  " +
                                inputMediaFile.getPath() + " failed " +
                                "(status=" + exitStatus + "(" +
                                UnixSignals.explainExitStatus( exitStatus ) + "), " +
                                " of " + ObtuseUtil.enquoteJavaObject( inputMediaFile )
                        );

                        if ( stderrReader != null && stderrReader.getCapturedStream().size() != 0 ) {

                            System.out.println(
                                    "STDERR from " + inputMediaFile.getPath() + ":\n" +
                                    stderrReader.getCapturedStream()
                                                .toString()
                            );

                            ObtuseUtil.doNothing();

                        }

                        ObtuseUtil.doNothing();

                    }

                } catch ( InterruptedException e ) {

                    Logger.logErr( "LancotMediaItem.scaleImageFile:  waitFor interrupted (ignored)", e );

                    ObtuseUtil.doNothing();

                }

            }

        }

        if ( imageFileBytes == null || imageFileBytes.length == 0 ) {

            String msg = "LancotFiles.fetchMediaFile:  got to the end without any image file bytes";

            return new MediaFileContents( inputMediaFile, msg, stdoutReader, stderrReader );

        }

        return new MediaFileContents(
                inputMediaFile,
                imageFileBytes,
                presumedOutputFileSuffix,
                isBarelySupportedImageFileSuffix( presumedInputFileSuffix ),
                stdoutReader,
                stderrReader
        );

    }

    public static boolean hasSupportedImageFileSuffix( @NotNull final File inputFile ) {

        String inputFileName = inputFile.getName();
        int lastPeriodOffset = inputFileName.lastIndexOf( '.' );
        if ( lastPeriodOffset > 1 ) {

            String suffix = inputFileName.substring( lastPeriodOffset + 1 );
            if ( isSupportedImageFileSuffix( suffix ) ) {

                return true;

            } else {

                return false;

            }

        }

        return false;

    }

    private static boolean isSupportedImageFileSuffix( @NotNull final String inputFileSuffix ) {

        return SUPPORTED_IMAGE_FILE_SUFFIXES.contains( inputFileSuffix );

    }

    private static boolean isBarelySupportedImageFileSuffix( @NotNull final String inputFileSuffix ) {

        return BARELY_SUPPORTED_IMAGE_FILE_SUFFIXES.contains( inputFileSuffix );

    }

    public static void main( String[] args ) {

        BasicProgramConfigInfo.init( "Kenosee", "Poirot", "testing" );

        long start = System.currentTimeMillis();
        MediaFileContents fileFrame = fetchMediaFile(
                new File( "testImage.jpg" ),
                null,
                null
        );
        long end = System.currentTimeMillis();
        System.out.println( "\"testImage.jpg\" took " + DateUtils.formatDuration( end - start ) );

        Logger.logMsg( "back from fetchMediaFile, fileFrame is " + fileFrame );

        ObtuseUtil.doNothing();

        start = System.currentTimeMillis();
        fileFrame = fetchMediaFile(
                new File( "Fermat's hard drive - model label.heic" ),
                null,
                null
        );
        end = System.currentTimeMillis();
        System.out.println( "\"Fermat's hard drive - model label.heic\" took " + DateUtils.formatDuration( end - start ) );

        Logger.logMsg( "back from fetchMediaFile, fileFrame is " + fileFrame );

        ObtuseUtil.doNothing();

    }

}
