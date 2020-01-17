package de.skuzzle.cmp.collaborativeorder.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

public class ConsoleTable {

    private final List<Column> columns;
    private final List<RowData> rows = new ArrayList<>();

    private ConsoleTable(List<Column> columns) {
        this.columns = columns;
    }

    public static ConsoleTable withHeaders(String... headers) {
        Preconditions.checkArgument(headers != null, "headers must not be null");
        Preconditions.checkArgument(headers.length != 0, "there must be at least one table header");
        return new ConsoleTable(Arrays.stream(headers).map(Column::leftAlignedWithName).collect(Collectors.toList()));
    }

    public static ConsoleTable withColumns(Column... columns) {
        Preconditions.checkArgument(columns != null, "columns must not be null");
        Preconditions.checkArgument(columns.length != 0, "there must be at least one column");
        return new ConsoleTable(Arrays.asList(columns));
    }

    public ConsoleTable addRow(RowData data) {
        Preconditions.checkArgument(data != null, "data must not be null");
        Preconditions.checkArgument(data.columns() == columns.size(), "columns and headers differ in size");

        this.rows.add(data);
        return this;
    }

    @Override
    public String toString() {
        final int[] maxWidthPerColumn = maxWidthPerColumn();
        final StringBuilder b = new StringBuilder();
        for (int column = 0; column < columns.size(); ++column) {
            final Column col = columns.get(column);
            final int maxColumnWidth = maxWidthPerColumn[column];

            final String paddedHeader = col.getAlignedHeader(maxColumnWidth);
            b.append(paddedHeader);
        }

        b.append("\n");

        for (final RowData row : rows) {
            for (int column = 0; column < columns.size(); ++column) {
                final Column col = columns.get(column);
                final int maxColumnWidth = maxWidthPerColumn[column];

                final String data = row.getData(column);
                final String paddedData = col.getAlignedCellContent(data, maxColumnWidth);
                b.append(paddedData);
            }
            b.append("\n");
        }
        return b.toString();
    }

    private int[] maxWidthPerColumn() {
        final int[] maxWidthPerColumn = new int[columns.size()];

        for (int column = 0; column < columns.size(); ++column) {
            maxWidthPerColumn[column] = Math.max(maxWidthPerColumn[column], columns.get(column).headerLength() + 4);
            for (final RowData data : rows) {
                maxWidthPerColumn[column] = Math.max(maxWidthPerColumn[column], data.getData(column).length() + 4);
            }
        }
        return maxWidthPerColumn;
    }
}
