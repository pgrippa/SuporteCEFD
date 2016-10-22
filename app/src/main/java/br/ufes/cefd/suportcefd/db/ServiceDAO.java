package br.ufes.cefd.suportcefd.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import br.ufes.cefd.suportcefd.domain.Service;

/**
 * Created by pgrippa on 12/10/16.
 */

public class ServiceDAO {

    SQLiteDatabase db;
    ServiceHelper mDbHelper;

    public ServiceDAO(Context context) {
        mDbHelper = new ServiceHelper(context);
    }

    public void open(String mode) {
        if (mode.equals("write")) {
            db = mDbHelper.getWritableDatabase();
        } else {
            db = mDbHelper.getReadableDatabase();
        }
    }

    public void close() {
        db.close();
    }

    public long putService(Service s) {
        open("write");
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Contract.ItemService.COLUMN_PATRIMONY, s.getPatrimony());
        values.put(Contract.ItemService.COLUMN_LOCAL, s.getLocal());
        values.put(Contract.ItemService.COLUMN_TYPE, s.getType());
        values.put(Contract.ItemService.COLUMN_DESCRIPTION, s.getDescription());
        values.put(Contract.ItemService.COLUMN_ENTRYDATE, s.getEntryDate());
        values.put(Contract.ItemService.COLUMN_RESLEASEDATE, s.getReleaseDate());
        values.put(Contract.ItemService.COLUMN_RESPONSIBLE, s.getIdResp());
        values.put(Contract.ItemService.COLUMN_ACTIVE, s.getActive());


        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                Contract.ItemService.TABLE_NAME,
                null,
                values);

        close();

        return newRowId;
    }

    public ArrayList<Service> getServices() {
        open("read");
        String[] projection = {
                Contract.ItemService._ID,
                Contract.ItemService.COLUMN_PATRIMONY,
                Contract.ItemService.COLUMN_LOCAL,
                Contract.ItemService.COLUMN_TYPE,
                Contract.ItemService.COLUMN_DESCRIPTION,
                Contract.ItemService.COLUMN_ENTRYDATE,
                Contract.ItemService.COLUMN_RESLEASEDATE,
                Contract.ItemService.COLUMN_RESPONSIBLE,
                Contract.ItemService.COLUMN_ACTIVE
        };

        String sortOrder =
                Contract.ItemService.COLUMN_ACTIVE + " DESC";

        Cursor c = db.query(
                Contract.ItemService.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c.getCount() == 0) {
            return null;
        }

        ArrayList<Service> services = new ArrayList<>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Service s = new Service(c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getLong(7));
            s.setId(c.getLong(0));
            s.setEntryDate(c.getString(5));
            s.setReleaseDate(c.getString(6));
            s.setActive(c.getInt(8));
            services.add(s);
        }

        close();

        return services;
    }

    public ArrayList<Service> getActiveServices(boolean active) {
        open("read");
        String[] projection = {
                Contract.ItemService._ID,
                Contract.ItemService.COLUMN_PATRIMONY,
                Contract.ItemService.COLUMN_LOCAL,
                Contract.ItemService.COLUMN_TYPE,
                Contract.ItemService.COLUMN_DESCRIPTION,
                Contract.ItemService.COLUMN_ENTRYDATE,
                Contract.ItemService.COLUMN_RESLEASEDATE,
                Contract.ItemService.COLUMN_RESPONSIBLE,
                Contract.ItemService.COLUMN_ACTIVE
        };

        String selection = Contract.ItemService.COLUMN_ACTIVE + " LIKE ?";
        String[] selectionArgs = {String.valueOf(active ? 1 : 0)};
        Cursor c = db.query(
                Contract.ItemService.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if (c.getCount() == 0) {
            return null;
        }

        ArrayList<Service> services = new ArrayList<>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Service s = new Service(c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getLong(7));
            s.setId(c.getLong(0));
            s.setEntryDate(c.getString(5));
            s.setReleaseDate(c.getString(6));
            s.setActive(c.getInt(8));
            services.add(s);
        }

        close();

        return services;
    }

    public ArrayList<Service> getPersonAllServices(long personid) {
        open("read");
        String[] projection = {
                Contract.ItemService._ID,
                Contract.ItemService.COLUMN_PATRIMONY,
                Contract.ItemService.COLUMN_LOCAL,
                Contract.ItemService.COLUMN_TYPE,
                Contract.ItemService.COLUMN_DESCRIPTION,
                Contract.ItemService.COLUMN_ENTRYDATE,
                Contract.ItemService.COLUMN_RESLEASEDATE,
                Contract.ItemService.COLUMN_RESPONSIBLE,
                Contract.ItemService.COLUMN_ACTIVE
        };

        String sortOrder =
                Contract.ItemService.COLUMN_ACTIVE + " DESC";
        String selection = Contract.ItemService.COLUMN_RESPONSIBLE + " LIKE ?";
        String[] selectionArgs = {String.valueOf(personid)};

        Cursor c = db.query(
                Contract.ItemService.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c.getCount() == 0) {
            return null;
        }

        ArrayList<Service> services = new ArrayList<>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Service s = new Service(c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getLong(7));
            s.setId(c.getLong(0));
            s.setEntryDate(c.getString(5));
            s.setReleaseDate(c.getString(6));
            s.setActive(c.getInt(8));
            services.add(s);
        }

        close();

        return services;
    }

    public ArrayList<Service> getPersonServices(long personid, boolean active) {
        open("read");
        String[] projection = {
                Contract.ItemService._ID,
                Contract.ItemService.COLUMN_PATRIMONY,
                Contract.ItemService.COLUMN_LOCAL,
                Contract.ItemService.COLUMN_TYPE,
                Contract.ItemService.COLUMN_DESCRIPTION,
                Contract.ItemService.COLUMN_ENTRYDATE,
                Contract.ItemService.COLUMN_RESLEASEDATE,
                Contract.ItemService.COLUMN_RESPONSIBLE,
                Contract.ItemService.COLUMN_ACTIVE
        };

        String selection = Contract.ItemService.COLUMN_RESPONSIBLE + " LIKE ? AND "+Contract.ItemService.COLUMN_ACTIVE + " LIKE ?";
        String[] selectionArgs = {String.valueOf(personid),String.valueOf(active ? 1 : 0)};

        Cursor c = db.query(
                Contract.ItemService.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if (c.getCount() == 0) {
            return null;
        }

        ArrayList<Service> services = new ArrayList<>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Service s = new Service(c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getLong(7));
            s.setId(c.getLong(0));
            s.setEntryDate(c.getString(5));
            s.setReleaseDate(c.getString(6));
            s.setActive(c.getInt(8));
            services.add(s);
        }

        close();

        return services;
    }

    public ArrayList<Service> searchService(String query) {
        open("read");
        String[] projection = {
                Contract.ItemService._ID,
                Contract.ItemService.COLUMN_PATRIMONY,
                Contract.ItemService.COLUMN_LOCAL,
                Contract.ItemService.COLUMN_TYPE,
                Contract.ItemService.COLUMN_DESCRIPTION,
                Contract.ItemService.COLUMN_ENTRYDATE,
                Contract.ItemService.COLUMN_RESLEASEDATE,
                Contract.ItemService.COLUMN_RESPONSIBLE,
                Contract.ItemService.COLUMN_ACTIVE
        };

        String selection = Contract.ItemService.COLUMN_PATRIMONY + " LIKE '"+query+"%'";
        String[] selectionArgs = {query};

        String sortOrder =
                Contract.ItemService.COLUMN_ACTIVE + " DESC";

        Cursor c = db.query(
                Contract.ItemService.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        /*
        Cursor c = db.rawQuery("SELECT * FROM "+Contract.ItemService.TABLE_NAME+" WHERE "
                +Contract.ItemService.COLUMN_PATRIMONY+" MATCH "+query+" ORDER BY "
                +Contract.ItemService.COLUMN_ACTIVE+" DESC",null);*/





        if (c.getCount() == 0) {
            return null;
        }

        ArrayList<Service> services = new ArrayList<>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Service s = new Service(c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getLong(7));
            s.setId(c.getLong(0));
            s.setEntryDate(c.getString(5));
            s.setReleaseDate(c.getString(6));
            s.setActive(c.getInt(8));
            services.add(s);
        }

        close();

        return services;
    }

    public void updateService(long rowId, Service s) {
        open("write");
        ContentValues values = new ContentValues();
        values.put(Contract.ItemService.COLUMN_PATRIMONY, s.getPatrimony());
        values.put(Contract.ItemService.COLUMN_LOCAL, s.getLocal());
        values.put(Contract.ItemService.COLUMN_TYPE, s.getType());
        values.put(Contract.ItemService.COLUMN_DESCRIPTION, s.getDescription());
        values.put(Contract.ItemService.COLUMN_ENTRYDATE, s.getEntryDate());
        values.put(Contract.ItemService.COLUMN_RESLEASEDATE, s.getReleaseDate());
        values.put(Contract.ItemService.COLUMN_RESPONSIBLE, s.getIdResp());
        values.put(Contract.ItemService.COLUMN_ACTIVE, s.getActive());

        String selection = Contract.ItemService._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(rowId)};

        int count = db.update(
                Contract.ItemService.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        close();
    }

    public void clean() {
        db.execSQL("delete from " + Contract.ItemService.TABLE_NAME);
    }

}
