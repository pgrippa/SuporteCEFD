package br.ufes.cefd.suportcefd.db;

import android.provider.BaseColumns;

/**
 * Created by pgrippa on 12/10/16.
 */

public final class Contract {

    public Contract() {}

    public static abstract class ItemService implements BaseColumns {
        public static final String TABLE_NAME = "Service";
        public static final String COLUMN_PATRIMONY = "patrimony";
        public static final String COLUMN_LOCAL = "local";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_RESPONSIBLE = "responsible";
        public static final String COLUMN_ENTRYDATE = "entrydate";
        public static final String COLUMN_RESLEASEDATE = "releasedate";
        public static final String COLUMN_ACTIVE = "active";

    }

    public static abstract class ItemPerson implements BaseColumns {
        public static final String TABLE_NAME = "Person";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TELEPHONE = "telephone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_TYPE = "type";
    }
}
