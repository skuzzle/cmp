package de.skuzzle.cmp.common.table;

import com.google.common.base.Strings;

public enum Align {
    LEFT {

        @Override
        String pad(String original, int width) {
            return Strings.padEnd(original, width, ' ');
        }
    },
    RIGHT {
        @Override
        String pad(String original, int width) {
            return Strings.padStart(original, width, ' ');
        }
    };

    abstract String pad(String original, int width);
}
