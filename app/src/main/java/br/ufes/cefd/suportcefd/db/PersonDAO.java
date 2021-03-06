package br.ufes.cefd.suportcefd.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import br.ufes.cefd.suportcefd.domain.Person;

/**
 * Created by pgrippa on 12/10/16.
 */

public class PersonDAO {

    SQLiteDatabase db;
    PersonHelper mDbHelper;

    public PersonDAO(Context context){
        mDbHelper = new PersonHelper(context);
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

    public long putPerson(Person p){
        open("write");
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Contract.ItemPerson._ID, p.getId());
        values.put(Contract.ItemPerson.COLUMN_NAME, p.getName());
        values.put(Contract.ItemPerson.COLUMN_TELEPHONE,p.getTelephone());
        values.put(Contract.ItemPerson.COLUMN_EMAIL,p.getEmail());
        values.put(Contract.ItemPerson.COLUMN_PASSWORD,p.getPassword());
        values.put(Contract.ItemPerson.COLUMN_TYPE,p.getType());


        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insertWithOnConflict(
                Contract.ItemPerson.TABLE_NAME,
                null,
                values,SQLiteDatabase.CONFLICT_REPLACE);

        close();

        return  newRowId;
    }

    public Person getPersonByEmail(String email){
        open("read");
        String[] projection = {
                Contract.ItemPerson._ID,
                Contract.ItemPerson.COLUMN_NAME,
                Contract.ItemPerson.COLUMN_TELEPHONE,
                Contract.ItemPerson.COLUMN_EMAIL,
                Contract.ItemPerson.COLUMN_PASSWORD,
                Contract.ItemPerson.COLUMN_TYPE
        };


        String selection = Contract.ItemPerson.COLUMN_EMAIL + " LIKE ?";
        String[] selectionArgs = { email };

        Cursor c = db.query(
                Contract.ItemPerson.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if(c.getCount()==0){
            return null;
        }
        Person person = new Person(c);

        close();

        return person;
    }

    public Person getPersonById(long id){
        open("read");
        String[] projection = {
                Contract.ItemPerson._ID,
                Contract.ItemPerson.COLUMN_NAME,
                Contract.ItemPerson.COLUMN_TELEPHONE,
                Contract.ItemPerson.COLUMN_EMAIL,
                Contract.ItemPerson.COLUMN_PASSWORD,
                Contract.ItemPerson.COLUMN_TYPE
        };


        String selection = Contract.ItemPerson._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor c = db.query(
                Contract.ItemPerson.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if(c.getCount()==0){
            return null;
        }
        Person person = new Person(c);

        close();

        return person;
    }

    /*public Cursor getPersonList(){
        open("read");
        String[] projection = {
                Contract.ItemPerson._ID,
                Contract.ItemPerson.COLUMN_NAME,
                Contract.ItemPerson.COLUMN_TELEPHONE,
                Contract.ItemPerson.COLUMN_EMAIL,
                Contract.ItemPerson.COLUMN_PASSWORD,
                Contract.ItemPerson.COLUMN_TYPE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Contract.ItemPerson.COLUMN_NAME + " ASC";

        Cursor c = db.query(
                Contract.ItemPerson.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        close();
        return c;
    }*/

    public ArrayList<Person> getPersonList(){
        open("read");
        String[] projection = {
                Contract.ItemPerson._ID,
                Contract.ItemPerson.COLUMN_NAME,
                Contract.ItemPerson.COLUMN_TELEPHONE,
                Contract.ItemPerson.COLUMN_EMAIL,
                Contract.ItemPerson.COLUMN_PASSWORD,
                Contract.ItemPerson.COLUMN_TYPE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Contract.ItemPerson.COLUMN_NAME + " ASC";

        Cursor c = db.query(
                Contract.ItemPerson.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        ArrayList<Person> list = new ArrayList<>();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            Person p = new Person(c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5));
            p.setId(c.getLong(0));
            list.add(p);
        }

        close();
        return list;
    }

    public void updatePerson(int rowId, Person p){
        open("write");
        ContentValues values = new ContentValues();
        values.put(Contract.ItemPerson.COLUMN_NAME, p.getName());
        values.put(Contract.ItemPerson.COLUMN_TELEPHONE,p.getTelephone());
        values.put(Contract.ItemPerson.COLUMN_EMAIL,p.getEmail());
        values.put(Contract.ItemPerson.COLUMN_PASSWORD,p.getPassword());
        values.put(Contract.ItemPerson.COLUMN_TYPE,p.getType());

        String selection = Contract.ItemPerson._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(rowId) };

        int count = db.update(
                Contract.ItemPerson.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        close();
    }

    public void clean()
    {
        if(db !=null) {
            db.execSQL("delete from " + Contract.ItemPerson.TABLE_NAME);
        }
    }

}
