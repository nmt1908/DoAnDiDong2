package vn.tdc.edu.fooddelivery.dal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import vn.tdc.edu.fooddelivery.models.UserModel;

public class DatabaseLayer extends SQLiteOpenHelper {
    private static String DB_NAME = "users";

    private static int DB_VERSION = 1;

    private static Activity context;

    private String USER_TABLE_NAME = "user";

    private static String USER_ID = "_id";

    private static String USER_NAME = "full_name";

    private static String USER_EMAIL = "email";

    private static String USER_IMAGE = "image";

    private static String USER_ROLES = "roles";

    public DatabaseLayer(Activity context) {
        super(context, DB_NAME, null, DB_VERSION);
        DatabaseLayer.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (db != null) {
            // SQL Statement
            String sql = "CREATE TABLE " + USER_TABLE_NAME + "("
                    + USER_ID + " INTEGER, "
                    + USER_NAME + " TEXT, "
                    + USER_EMAIL + " TEXT, "
                    + USER_IMAGE + " TEXT, "
                    + USER_ROLES + " TEXT);";
            // Execute the SQL Statement
            try {
                db.execSQL(sql);
                Log.d("database", "created user table");
            } catch (Exception exception) {
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO when the databse version changes
    }

    // 1. Save persons to Person Database
    public boolean savePerson(UserModel person) {
        boolean ok = false;
        SQLiteDatabase database = getWritableDatabase();
        if (database != null) {
            ContentValues values = new ContentValues();
            values.put(USER_ID, person.getId());
            values.put(USER_NAME, person.getFullName());
            values.put(USER_EMAIL, person.getEmail());
            values.put(USER_IMAGE, person.getImageName());
            values.put(USER_ROLES, String.join(";", person.getRoleCodes()));
            try {
                database.insert(USER_TABLE_NAME, null, values);
            } catch (Exception exception) {
            }
            database.close();
        }
        return ok;
    }

    public UserModel getPerson() {
        UserModel person = null;
        SQLiteDatabase database = getWritableDatabase();
        if (database != null) {
            String[] selectionColunm = new String[] { USER_ID, USER_NAME, USER_EMAIL, USER_IMAGE, USER_ROLES };
            String whereCondition = null;
            String[] whereAgr = null;
            String groupBy = null;
            String having = null;
            Cursor cursor = database.query(USER_TABLE_NAME, selectionColunm, whereCondition, whereAgr, groupBy, having,
                    USER_NAME + " ASC");
            if (cursor.moveToFirst()) {
                @SuppressLint("Range")
                String id = cursor.getString(cursor.getColumnIndex(USER_ID));
                @SuppressLint("Range")
                String name = cursor.getString(cursor.getColumnIndex(USER_NAME));
                @SuppressLint("Range")
                String email = cursor.getString(cursor.getColumnIndex(USER_EMAIL));
                @SuppressLint("Range")
                String image = cursor.getString(cursor.getColumnIndex(USER_IMAGE));
                @SuppressLint("Range")
                String roles = cursor.getString(cursor.getColumnIndex(USER_ROLES));
                // Bin data for object
                person = new UserModel();
                person.setId(Integer.valueOf(id));
                person.setFullName(name);
                person.setEmail(email);
                person.setImageName(image);
                person.setRolesString(roles);
            }
            database.close();
        }
        return person;
    }

    public boolean updatePerson(UserModel newPerson) {
        boolean ok = false;
        SQLiteDatabase database = getWritableDatabase();
        if (database != null) {
            ContentValues values = new ContentValues();
            String where = USER_ID + " =?";
            String[] whereArgs = new String[] { String.valueOf(newPerson.getId()) };

            if (newPerson.getFullName() != null && !newPerson.getFullName().isEmpty()) {
                values.put(USER_NAME, newPerson.getFullName());
            }

            if (newPerson.getEmail() != null && !newPerson.getEmail().isEmpty()) {
                values.put(USER_EMAIL, newPerson.getEmail());
            }

            if (newPerson.getImageName() != null && !newPerson.getImageName().isEmpty()) {
                values.put(USER_IMAGE, newPerson.getImageName());
            }

            int bool_Update = database.update(USER_TABLE_NAME, values, where, whereArgs);

            if (bool_Update == 1) {
                ok = true;
            } else {
                ok = false;
            }
            database.close();
        }
        return ok;
    }

    public boolean deletePerson(UserModel person) {
        boolean ok = false;
        SQLiteDatabase database = getWritableDatabase();
        if (database != null) {

            String where = USER_ID + " =?";
            String[] whereArgs = new String[] { String.valueOf(person.getId()) };
            int bool_delete = database.delete(USER_TABLE_NAME, where, whereArgs);

            // TODO Show dialog to user
            if (bool_delete > 0) {
                ok = true;
            }
            database.close();
        }
        return ok;
    }

    public boolean deleteTable() {
        boolean ok = false;
        SQLiteDatabase database = getWritableDatabase();
        if (database != null) {
            String sql = "DROP TABLE IF EXISTS " + USER_TABLE_NAME;
            try {
                database.execSQL(sql);
                ok = true;
            } catch (Exception exception) {
                ok = false;
            }
            database.close();
        }
        return ok;
    }
}
