package com.stochitacatalin.betterprogrammer.Database;

import android.provider.BaseColumns;

public final class SectionComponentContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SectionComponentContract() {}

    /* Inner class that defines the table contents */
    public static class SectionComponentEntry implements BaseColumns {
        public static final String TABLE_NAME = "section_components";
        public static final String COLUMN_NAME_SECTION = "section";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_DATA = "data";
        public static final String COLUMN_NAME_ORDER = "`order`";
    }

}