package helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tivachkov.reservations.reservations.Customer;
import com.tivachkov.reservations.reservations.Table;

import java.util.ArrayList;

import utils.Utils;

/**
 * Created by tivachkov on 11/21/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION_V1 = 1;
    private static final String DATABASE_NAME = "QuandooReservations";


    public DatabaseHandler(Context context) {
        super(context, DatabaseHandler.DATABASE_NAME, null, DatabaseHandler.DATABASE_VERSION_V1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.createReservationsTable(db);
        this.createTablesTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO implement later when needed
    }

    public int getDBTableCount(String tableName) {
        int count = -1;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM " + tableName, null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
        }
        db.close();

        return count;
    }

    public void createReservationsTable(SQLiteDatabase db) {
        Log.d("===| DBHandler class", "Creating Reservations Table....................");
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS Reservations(" +
                        "ID INTEGER NOT NULL PRIMARY KEY, " + //No autoincrement, because the IDs of the users/customers should be assigned by Quandoo platform to each user/customer.
                        "Name TEXT, " +
                        "Surname TEXT" +
                        ");"
        );
    }

    public void dropReservationsTableIfExists(SQLiteDatabase db) {
        Log.d("===| DBHandler class", "Dropping Reservations Table....................");
        db.execSQL(
                "DROP TABLE IF EXISTS Reservations;"
        );
    }

    public void addReservation(Customer customer) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", customer.getId());
        values.put("Name", customer.getName());
        values.put("Surname", customer.getSurname());

        db.insert("Reservations", null, values);
        db.close();
    }

    public void deleteReservation(int id) {
        //Utils.printAlarmsList(getAllAlarms());
        //Log.d("===| DBHandler", "===================| Before - Then |==========================");
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Reservations", "ID=?", (new String[] {String.valueOf(id)}));
        db.close();
        //Utils.printAlarmsList(getAllAlarms());
    }

    public void deleteAllReservations() {
        SQLiteDatabase db = getWritableDatabase();
        dropReservationsTableIfExists(db);
        createReservationsTable(db);
        db.close();
    }

    public ArrayList<Customer> getAllReservations() {
        ArrayList<Customer> reservations = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Reservations", null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    Customer customer = new Customer(cursor.getString(1), cursor.getString(2), cursor.getInt(0));
                    reservations.add(customer);

                } while (cursor.moveToNext());
            }
        }
        db.close();

        return reservations;
    }

    //--------------------------| Tables |------------------------------

    public void createTablesTable(SQLiteDatabase db) {
        Log.d("===| DBHandler class", "Creating Tables Table....................");
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS Tables(" +
                        "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + //No autoincrement, because the IDs of the users/customers should be assigned by Quandoo platform to each user/customer.
                        "Available INTEGER " +
                        ");"
        );
    }

    public ArrayList<Table> getTables() {
        ArrayList<Table> tables = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Tables", null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    int availableInt = cursor.getInt(1);
                    Table table;
                    if (availableInt == 0) {
                        table = new Table(false);
                    } else {
                        table = new Table(true);
                    }
                    tables.add(table);

                } while (cursor.moveToNext());
            }
        }
        db.close();


        return tables;
    }

    public void addTable(Table table) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put("ID", customer.getId());
        //values.put("Name", customer.getName());
        values.put("Available", table.isAvailable());

        db.insert("Tables", null, values);
        db.close();
    }

    public void setTableAvailability(boolean available, int id) {

        //Utils.printAlarmsList(getAllAlarms());
        //Log.d("===| DBHandler", "===================| Before - Then |==========================");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        int availableInt;
        if (available) availableInt = 1;
        else availableInt = 0;

        values.put("Available", availableInt);
        db.update("Tables", values, "ID=?", new String[] {String.valueOf(id+1)}); // id+1 because the IDs in the DB tables Tables start from 1 and in the gridview start from 0.
        db.close();
        //Utils.printAlarmsList(getAllAlarms());

    }

    public void deleteAllTables() {
        SQLiteDatabase db = getWritableDatabase();
        dropTablesTableIfExists(db);
        createTablesTable(db);
        db.close();
    }

    private void dropTablesTableIfExists(SQLiteDatabase db) {
        Log.d("===| DBHandler class", "Dropping Tables Table....................");
        db.execSQL(
                "DROP TABLE IF EXISTS Tables;"
        );
    }


}