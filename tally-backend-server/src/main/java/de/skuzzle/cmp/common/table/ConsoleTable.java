package de.skuzzle.cmp.common.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Strings;

public class ConsoleTable {

    private final List<String> headers;
    private final List<RowData> rows = new ArrayList<>();

    private ConsoleTable(List<String> headers) {
        this.headers = headers;
    }

    public static ConsoleTable withHeaders(String... headers) {
        return new ConsoleTable(Arrays.asList(headers));
    }

    public ConsoleTable addRow(RowData data) {
        this.rows.add(data);
        return this;
    }

    @Override
    public String toString() {
        final int[] maxWidthPerColumn = maxWidthPerColumn();
        final StringBuilder b = new StringBuilder();
        for (int column = 0; column < headers.size(); ++column) {
            final String header = headers.get(column);
            final String paddedHeader = Strings.padEnd(header, maxWidthPerColumn[column], ' ');
            b.append(paddedHeader);
        }
        b.append("\n");
        for (final RowData row : rows) {
            for (int column = 0; column < headers.size(); ++column) {
                final String data = row.getData(column);
                final String paddedData = Strings.padEnd(data, maxWidthPerColumn[column], ' ');
                b.append(paddedData);
            }
            b.append("\n");
        }
        return b.toString();
    }

    private int[] maxWidthPerColumn() {
        final int[] maxWidthPerColumn = new int[headers.size()];

        for (int column = 0; column < headers.size(); ++column) {
            maxWidthPerColumn[column] = Math.max(maxWidthPerColumn[column], headers.get(column).length()+ 4);
            for (RowData data : rows) {
                maxWidthPerColumn[column] = Math.max(maxWidthPerColumn[column], data.getData(column).length()+ 4) ;
            }
        }
        return maxWidthPerColumn;
    }
}
