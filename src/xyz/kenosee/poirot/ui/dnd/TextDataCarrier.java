package xyz.kenosee.poirot.ui.dnd;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.kenosee.poirot.data.ReferenceItem;
import xyz.kenosee.poirot.data.TextReferenceItem;

import java.util.List;

/**
 Created by danny on 2021/12/21.
 */
public class TextDataCarrier extends DataCarrier {

    private String _text;
    private String _descriptiveName;

    public TextDataCarrier( @NotNull final String descriptiveName, @NotNull final String text ) {
        super();

        _descriptiveName = descriptiveName;
        _text = text;

    }

    @NotNull
    public String getMandatoryData() {

        return _text;

    }

    @Override
    @Nullable
    public String getOptionalData() {

        return _text;

    }

    @NotNull
    public DataClass getDataClass() {

        return DataClass.TEXT;

    }

    public void createReferenceItem(
            final List<ReferenceItem> referenceItems
    ) {

        String text = getMandatoryData();

        TextReferenceItem textReferenceItem = new TextReferenceItem(
                _descriptiveName,
                "DnD or CnP",
                _text
        );

        referenceItems.add( textReferenceItem );

    }

}
