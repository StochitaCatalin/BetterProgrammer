package com.stochitacatalin.betterprogrammer.Database;

import android.provider.BaseColumns;

public final class SectionContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SectionContract() {}

    /* Inner class that defines the table contents */
    public static class SectionEntry implements BaseColumns {
        public static final String TABLE_NAME = "sections";
        public static final String COLUMN_NAME_CHAPTER = "chapter";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_COMPLETED = "completed";
        public static final String COLUMN_NAME_ORDER = "`order`";
    }

}