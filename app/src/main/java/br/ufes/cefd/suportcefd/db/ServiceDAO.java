package br.ufes.cefd.suportcefd.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.ufes.cefd.suportcefd.domain.Service;

/**
 * Created by pgrippa on 12/10/16.
 */

public class ServiceDAO {

    SQLiteDatabase db;
    ServiceHelper mDbHelper;

    public ServiceDAO(Context context){
        mDbHelper = new ServiceHelper(context);
    }

    public void open(String mode){
        if(mode.equals("write")) {
            db = mDbHelper.getWritableDatabase();
        }else{
            db = mDbHelper.getReadableDatabase();
        }
    }

    public void close(){
        db.close();
    }

    public long putService(Service s){
        open("write");
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Contract.ItemService.COLUMN_PATRIMONY, s.getPatrimony());
        values.put(Contract.ItemService.COLUMN_LOCAL, s.getLocal());
        values.put(Contract.ItemService.COLUMN_TYPE,s.getType());
        values.put(Contract.ItemService.COLUMN_RESPONSIBLE,s.getResponsible());
        values.put(Contract.ItemService.COLUMN_TELEPHONE,s.getTelephone());
        values.put(Contract.ItemService.COLUMN_EMAIL,s.getEmail());
        values.put(Contract.ItemService.COLUMN_DESCRIPTION,s.getDescription());
        values.put(Contract.ItemService.COLUMN_ENTRYDATE,s.getEntryDate());
        values.put(Contract.ItemService.COLUMN_RESLEASEDATE,s.getReleaseDate());


        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                Contract.ItemService.TABLE_NAME,
                null,
                values);

        close();

        return  newRowId;
    }

    public Cursor getServices(){
        String[] projection = {
                Contract.ItemService._ID,
                Contract.ItemService.COLUMN_PATRIMONY,
                Contract.ItemService.COLUMN_LOCAL,
                Contract.ItemService.COLUMN_TYPE,
                Contract.ItemService.COLUMN_RESPONSIBLE,
                Contract.ItemService.COLUMN_TELEPHONE,
                Contract.ItemService.COLUMN_EMAIL,
                Contract.ItemService.COLUMN_DESCRIPTION,
                Contract.ItemService.COLUMN_ENTRYDATE,
                Contract.ItemService.COLUMN_RESLEASEDATE
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                Contract.ItemService.COLUMN_PATRIMONY + " ASC";

        Cursor c = db.query(
                Contract.ItemService.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        return c;
    }

    public void updateService(int rowId, Service s){
        open("write");
        ContentValues values = new ContentValues();
        values.put(Contract.ItemService.COLUMN_PATRIMONY, s.getPatrimony());
        values.put(Contract.ItemService.COLUMN_LOCAL, s.getLocal());
        values.put(Contract.ItemService.COLUMN_TYPE,s.getType());
        values.put(Contract.ItemService.COLUMN_RESPONSIBLE,s.getResponsible());
        values.put(Contract.ItemService.COLUMN_TELEPHONE,s.getTelephone());
        values.put(Contract.ItemService.COLUMN_EMAIL,s.getEmail());
        values.put(Contract.ItemService.COLUMN_DESCRIPTION,s.getDescription());
        values.put(Contract.ItemService.COLUMN_ENTRYDATE,s.getEntryDate());
        values.put(Contract.ItemService.COLUMN_RESLEASEDATE,s.getReleaseDate());

        String selection = Contract.ItemService._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(rowId) };

        int count = db.update(
                Contract.ItemService.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        close();
    }

    public void clean()
    {
        db.execSQL("delete from " + Contract.ItemService.TABLE_NAME);
    }

}
