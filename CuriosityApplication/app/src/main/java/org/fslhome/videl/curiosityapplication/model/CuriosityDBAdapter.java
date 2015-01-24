package org.fslhome.videl.curiosityapplication.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by videl on 23/01/15.
 */

public class CuriosityDBAdapter {
    private CuriosityOpenHelper dbHelper;
    private SQLiteDatabase db;
    private final Context context;

    private static final String DATABASE_NAME = "curiosity";
    private static final String DATABASE_TABLE = "curiosity";
    private static final int DATABASE_VERSION = 2;
    private static final String CREATE_TABLE =
            "CREATE TABLE " + DATABASE_TABLE + " (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "curiosity_name TEXT NOT NULL," +
            "title TEXT NOT NULL," +
            "description TEXT NOT NULL," +
            "gps_lat REAL NOT NULL," +
            "gps_lon REAL NOT NULL);";

    /**
     * The constructor for the data part. The first step needed to
     * give the context before creating/opening the database.
     * @param context The context of the application (needed by SQLiteOpenHelper)
     */
    public CuriosityDBAdapter(Context context) {
        this.context = context;
    }

    public CuriosityDBAdapter open_write() {
        this.dbHelper = new CuriosityOpenHelper(this.context);
        this.db = this.dbHelper.getWritableDatabase();

        return this;
    }

    public CuriosityDBAdapter open_read()  {
        this.dbHelper = new CuriosityOpenHelper(this.context);
        this.db = this.dbHelper.getReadableDatabase();

        return this;
    }

    public void close() {
        this.dbHelper.close();
        this.db.close();
    }

    /**
     * Add new data to the table with the Curiosities.
     *
     * @param curiosityName Name of where the Curiosity is.
     * @param curiosityTitle Title of the Curiosity.
     * @param curiosityDesc Description of the Curiosity.
     * @param gps_lat GPS Latitude coordinate of the Curiosity.
     * @param gps_lon GPS Longitude coordinate of the Curiosity.
     * @return The row of the entry, or -1 if error.
     */
    public long addNewCuriosityData(String curiosityName,
                                    String curiosityTitle,
                                    String curiosityDesc,
                                    double gps_lat,
                                    double gps_lon) {
        ContentValues initialValues = new ContentValues();

        initialValues.put("curiosity_name", curiosityName);
        initialValues.put("title", curiosityTitle);
        initialValues.put("description", curiosityDesc);
        initialValues.put("gps_lat", gps_lat);
        initialValues.put("gps_lon", gps_lon);

        return this.db.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete an entry about a Curiosity.
     *
     * @param rowId The ID of the row.
     * @return True if something has been deleted, false otherwise.
     */
    public boolean deleteCuriosityData(long rowId) {
        return this.db.delete(DATABASE_TABLE, "_id=" + rowId, null) > 0;
    }

    public void completeDatabaseErase() {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    /**
     * Fetch all Curiosities about a specific Curiosity, filtered on the 'name'
     * (it's location)
     * @param curiosityName The name of the Curiosity of which to find all the details.
     * @return Cursor on the Curiosities.
     */
    public Cursor fetchAllDataAboutOneCuriosity(String curiosityName) {
        return this.db.query(
                DATABASE_TABLE,
                new String[] {"title", "description", "gps_lat", "gps_lon"},
                "curiosity_name=\"" + curiosityName + "\"",
                null,
                null,
                null,
                null);
    }

    private class CuriosityOpenHelper extends SQLiteOpenHelper {

        CuriosityOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("CurioLog", "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

}
