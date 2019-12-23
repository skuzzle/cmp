package de.skuzzle.cmp.common.table;

import java.util.Arrays;

public interface RowData {
    
    static RowData of(Object...values) {
        String[] columns = Arrays.stream(values).map(Object::toString).toArray(i -> new String[i]);
        return column -> columns[column];
    }

    String getData(int column);
}
