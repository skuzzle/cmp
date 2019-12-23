package de.skuzzle.cmp.common.table;

public interface RowData {

    static RowData of(Object... values) {
        return new RowData() {

            @Override
            public String getData(int column) {
                return values[column].toString();
            }

            @Override
            public int columns() {
                return values.length;
            }
        };
    }

    int columns();

    String getData(int column);
}
