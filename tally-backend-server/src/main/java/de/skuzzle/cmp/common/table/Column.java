package de.skuzzle.cmp.common.table;

import com.google.common.base.Preconditions;

public class Column {

    private final String header;
    private final Align align;

    private Column(String header, Align align) {
        Preconditions.checkArgument(header != null, "header must not be null");
        Preconditions.checkArgument(align != null, "align must not be null");
        this.header = header;
        this.align = align;
    }

    public static Column leftAlignedWithName(String header) {
        return new Column(header, Align.LEFT);
    }

    public static Column rightAlignedWithName(String header) {
        return new Column(header, Align.RIGHT);
    }

    int headerLength() {
        return header.length();
    }

    public String getAlignedHeader(int columnWidth) {
        return align.pad(header, columnWidth);
    }

    public String getAlignedCellContent(String data, int columnWidth) {
        return align.pad(data, columnWidth);
    }
}
