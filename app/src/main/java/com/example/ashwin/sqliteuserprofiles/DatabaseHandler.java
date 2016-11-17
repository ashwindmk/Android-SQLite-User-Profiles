package com.example.ashwin.sqliteuserprofiles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by ashwin on 3/11/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables

    private static final String TAG = "DatabaseHandler";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Users";

    // Users table name
    private static final String TABLE_USERS = "UserProfiles";

    // Users Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE_URL = "imageurl";
    private static final String KEY_IMAGE = "image";

    private long insertResult;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_IMAGE_URL + " TEXT,"
                + KEY_IMAGE + " BLOB NOT NULL" + ");";
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    // adding new user
    public long addUser(User user)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // convert byte[] to bitmap
        Bitmap bitmap = user.getBitmapimage();
        int bytes = bitmap.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buffer);
        //byte[] imageBytes =  buffer.array();
        byte[] imageBytes = getBitmapAsByteArray(bitmap);

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getId());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_IMAGE_URL, user.getImageurl());
        values.put(KEY_IMAGE, imageBytes);

        // inserting Row
        insertResult = sqLiteDatabase.insert(TABLE_USERS, null, values);
        sqLiteDatabase.close();
        return insertResult;
    }

    // retreive single user
    public User getUser(int id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        User user = null;

        if( checkIsDataAlreadyInDBorNot( TABLE_USERS, KEY_ID, id ) )
        {
            Cursor cursor = sqLiteDatabase.query(TABLE_USERS, new String[] { KEY_ID, KEY_NAME, KEY_IMAGE_URL, KEY_IMAGE }, KEY_ID + "=?",
                    new String[] { String.valueOf(id) }, null, null, null, null);

            if (cursor != null)
            {
                cursor.moveToFirst();
            }

            byte[] imageBytes = cursor.getBlob(3);

            // convert byte[] to bitmap
            Bitmap bitmapImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), bitmapImage);
        }

        // return user
        return user;
    }

    // retrieve all users
    public ArrayList<User> getAllUsers()
    {
        ArrayList<User> usersList = new ArrayList<User>();

        // select all query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                byte[] imageBytes = cursor.getBlob(3);

                // convert byte[] to bitmap
                Bitmap bitmapImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), bitmapImage);

                // adding user to list
                usersList.add(user);
            }
            while (cursor.moveToNext());
        }

        // return users list
        return usersList;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }


    public void clearAll()
    {
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        sqLiteDatabase.delete(TABLE_USERS, null, null);
    }


    public void deleteRecord(int id)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        try {
            sqLiteDatabase.delete(TABLE_USERS, KEY_ID + "=" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean checkIsDataAlreadyInDBorNot(String TableName,
                                                      String dbfield, int fieldValue) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue +";";
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkIsStringAlreadyInDBorNot(String TableName,
                                                 String dbfield, String fieldValue) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " = '" + fieldValue +"'";
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public User getUserFromAnyField(String tableName, String fieldName, String fieldValue)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        User user = null;

        if( checkIsStringAlreadyInDBorNot( tableName, fieldName, fieldValue ) )
        {
            Cursor cursor = sqLiteDatabase.query(tableName, new String[] { KEY_ID, KEY_NAME, KEY_IMAGE_URL, KEY_IMAGE }, fieldName + "=?",
                    new String[] { fieldValue }, null, null, null, null);

            if (cursor != null)
            {
                cursor.moveToFirst();
            }

            byte[] imageBytes = cursor.getBlob(3);

            // convert byte[] to bitmap
            Bitmap bitmapImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), bitmapImage);
        }

        // return user
        return user;
    }

}
