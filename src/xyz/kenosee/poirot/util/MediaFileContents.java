package xyz.kenosee.poirot.util;

import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Optional;

/**
 Something that carries around the contents of a specified media file or describes why the media file could not be loaded.
 */

public class MediaFileContents {

    private final byte @Nullable[] _imageFileBytes;
    private final @Nullable String _fileFormat;
    private final boolean _barelySupportedImageFormat;

    private final @Nullable AsyncStreamCaptor _stdoutOutput;
    private final @Nullable AsyncStreamCaptor _stderrOutput;

    private final boolean _worked;

    private final @Nullable String _msg;
    private final File _inputMediaFile;

    /**
     Describe a failure to load a media file.
     @param inputMediaFile the file that we were trying to load from. {@code null} if not available (for example, we are
     'loading' the image from an in-memory array).
     @param msg what went wrong.
     @param stdoutOutput what we got from the loader's {@code stdout}. {@code null} if the failure occurred before the loader was launched.
     @param stderrOutput what we got from the loader's {@code stderr}. {@code null} if the failure occurred before the loader was launched.
     */

    public MediaFileContents(
            @Nullable final File inputMediaFile,
            @NotNull final String msg,
            @Nullable final AsyncStreamCaptor stdoutOutput,
            @Nullable final AsyncStreamCaptor stderrOutput
    ) {
        super();

        _inputMediaFile = inputMediaFile;

        _imageFileBytes = null;

        _fileFormat = null;

        _barelySupportedImageFormat = false;

        _msg = msg;

        _stdoutOutput = stdoutOutput;
        _stderrOutput = stderrOutput;

        _worked = false;

    }

    public MediaFileContents(
            @Nullable final File inputMediaFile,
            final byte @NotNull[] imageFileBytes,
            @NotNull final String fileFormat,
            final boolean barelySupportedImageFormat,
            @Nullable final AsyncStreamCaptor stdoutOutput,
            @Nullable final AsyncStreamCaptor stderrOutput
    ) {
        super();

        _inputMediaFile = inputMediaFile;

        _imageFileBytes = imageFileBytes;

        _fileFormat = fileFormat;

        _barelySupportedImageFormat = barelySupportedImageFormat;

        _msg = null;

        _stdoutOutput = stdoutOutput;
        _stderrOutput = stderrOutput;

        _worked = true;

    }

    @NotNull
    public Optional<File> getOptInputMediaFile() {

        return Optional.ofNullable( _inputMediaFile );

    }

    @SuppressWarnings("unused")
    public boolean isBarelySupportedImageFormat() {

        return _barelySupportedImageFormat;

    }

    public boolean worked() {

        return _worked;

    }

    @SuppressWarnings("unused")
    @NotNull Optional<AsyncStreamCaptor> getStdoutOutput() {

        return Optional.ofNullable( _stdoutOutput );

    }

    @SuppressWarnings("unused")
    @NotNull Optional<AsyncStreamCaptor> getStderrOutput() {

        return Optional.ofNullable( _stderrOutput );

    }

    @SuppressWarnings("unused")
    @NotNull
    public String getMandatoryFileFormat() {

        if ( _fileFormat == null ) {

            throw new IllegalArgumentException(
                    "MediaFileFetchResult.getMandatoryFileFormat:  no file format (fetch failed)"
            );

        }

        return _fileFormat;

    }

    public byte@NotNull[] getImageFileBytes() {

        if ( _imageFileBytes == null ) {

            throw new IllegalArgumentException(
                    "MediaFileFetchResult.getMandatoryFileFormat:  no image file bytes (fetch failed)"
            );

        }

        return _imageFileBytes;

    }

    @SuppressWarnings("unused")
    @NotNull
    public String getMandatoryErrorMessage() {

        if ( _msg == null ) {

            throw new IllegalArgumentException(
                    "MediaFileFetchResult.getMandatoryFileFormat:  no error message (fetch worked)"
            );

        }

        return _msg;

    }

    public String toString() {

        return "MediaFileContents( " +
               "msg=" + ObtuseUtil.enquoteJavaObject( _msg ) + ", " +
               "mediaFile=" + ObtuseUtil.enquoteJavaObject( _inputMediaFile ) + ", " +
               ( _fileFormat == null ? "" : "fileFormat=" + _fileFormat + ", " ) +
               ( _imageFileBytes == null ? "no" : _imageFileBytes.length ) + " imageBytes, " +
               "fileFormat=" + _fileFormat + ", " +
               (
                       _barelySupportedImageFormat
                               ?
                               "barely supported format"
                               :
                               "fully supported format"
               ) +
               " )";

    }

}
