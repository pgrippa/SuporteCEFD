package br.ufes.cefd.suportcefd.db;

/**
 * Created by novaes on 09/10/16.
 */
public class SqlStrings {

    public static final String TEXT_TYPE = " TEXT";

    public static final String DATE_TYPE = " DATE";

    public static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_SERVICE_ENTRIES =
            "CREATE TABLE " + Contract.ItemService.TABLE_NAME + " (" +
                    Contract.ItemService._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    Contract.ItemService.COLUMN_PATRIMONY + TEXT_TYPE + COMMA_SEP +
                    Contract.ItemService.COLUMN_TYPE + TEXT_TYPE + COMMA_SEP +
                    Contract.ItemService.COLUMN_LOCAL + TEXT_TYPE + COMMA_SEP +
                    Contract.ItemService.COLUMN_RESPONSIBLE + TEXT_TYPE + COMMA_SEP +
                    Contract.ItemService.COLUMN_TELEPHONE + TEXT_TYPE + COMMA_SEP +
                    Contract.ItemService.COLUMN_EMAIL + TEXT_TYPE + COMMA_SEP +
                    Contract.ItemService.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    Contract.ItemService.COLUMN_ENTRYDATE + TEXT_TYPE + COMMA_SEP +
                    Contract.ItemService.COLUMN_RESLEASEDATE + TEXT_TYPE +
             " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Contract.ItemService.TABLE_NAME;

}