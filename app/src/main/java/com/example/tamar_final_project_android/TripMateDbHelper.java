package com.example.tamar_final_project_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * AI-generated / assisted class structure (documented for Ministry of Education guidelines).
 * שכבת בסיס נתונים מקומית (SQLite) לניהול משתמשים, תחביבים, התאמות והודעות.
 */
public class TripMateDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tripmate.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_HOBBIES = "hobbies";

    private static final String TABLE_PARTNERS = "partners";
    private static final String COL_PARTNER_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_AGE = "age";
    private static final String COL_CITY = "city";
    private static final String COL_BIO = "bio";
    private static final String COL_P_HOBBIES = "hobbies";
    private static final String COL_LIKED = "is_liked";

    private static final String TABLE_MESSAGES = "messages";
    private static final String COL_MSG_ID = "id";
    private static final String COL_SENDER = "sender_username";
    private static final String COL_RECIPIENT_NAME = "recipient_name";
    private static final String COL_MSG_BODY = "message_body";

    public TripMateDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL, " +
                COL_HOBBIES + " TEXT DEFAULT '')";

        String createPartners = "CREATE TABLE " + TABLE_PARTNERS + " (" +
                COL_PARTNER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_AGE + " INTEGER NOT NULL, " +
                COL_CITY + " TEXT NOT NULL, " +
                COL_BIO + " TEXT NOT NULL, " +
                COL_P_HOBBIES + " TEXT NOT NULL, " +
                COL_LIKED + " INTEGER DEFAULT 0)";

        String createMessages = "CREATE TABLE " + TABLE_MESSAGES + " (" +
                COL_MSG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SENDER + " TEXT NOT NULL, " +
                COL_RECIPIENT_NAME + " TEXT NOT NULL, " +
                COL_MSG_BODY + " TEXT NOT NULL)";

        db.execSQL(createUsers);
        db.execSQL(createPartners);
        db.execSQL(createMessages);

        seedInitialPartners(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTNERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void seedInitialPartners(SQLiteDatabase db) {
        insertSeedPartner(db, "נועה", 24, "תל אביב",
                "אוהבת טיולים, מוזיקה וכלבים", "טיולים,מוזיקה,בעלי חיים", 0);
        insertSeedPartner(db, "מאיה", 23, "חיפה",
                "מחפשת פרטנרית לטרקים בדרום אמריקה וצילום שקיעות", "טיולים,קריאה,כושר", 0);
        insertSeedPartner(db, "אורי", 25, "ירושלים",
                "מנגנת בגיטרה, חובבת סרטים וסדרות וטיולי יום", "מוזיקה,סרטים וסדרות,טיולים", 0);
        insertSeedPartner(db, "תמר", 22, "רמת גן",
                "רצה בוקר, קוראת ספרים ומוכנה להרפתקה ארוכה במזרח", "כושר,קריאה,טיולים", 0);
        insertSeedPartner(db, "ליאל", 24, "באר שבע",
                "אוהבת בעלי חיים, פלייליסטים ארוכים לדרך וסדרות מתח", "בעלי חיים,מוזיקה,סרטים וסדרות", 0);
    }

    private void insertSeedPartner(SQLiteDatabase db, String name, int age, String city,
                                   String bio, String hobbies, int liked) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_AGE, age);
        values.put(COL_CITY, city);
        values.put(COL_BIO, bio);
        values.put(COL_P_HOBBIES, hobbies);
        values.put(COL_LIKED, liked);
        db.insert(TABLE_PARTNERS, null, values);
    }

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username.trim());
        values.put(COL_PASSWORD, password);
        values.put(COL_HOBBIES, "");
        long rowId = db.insert(TABLE_USERS, null, values);
        return rowId != -1;
    }

    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username.trim(), password},
                null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public void updateUserHobbies(String username, String hobbiesCsv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_HOBBIES, hobbiesCsv);
        db.update(TABLE_USERS, values, COL_USERNAME + "=?", new String[]{username.trim()});
    }

    public String getUserHobbies(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_HOBBIES},
                COL_USERNAME + "=?",
                new String[]{username.trim()},
                null, null, null);
        String hobbies = "";
        if (cursor.moveToFirst()) {
            hobbies = cursor.getString(0);
        }
        cursor.close();
        return hobbies != null ? hobbies : "";
    }

    public List<TravelPartner> getAllPartners() {
        List<TravelPartner> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PARTNERS, null, null, null, null, null, COL_PARTNER_ID + " ASC");

        int[] avatars = {
                android.R.drawable.sym_def_app_icon,
                android.R.drawable.ic_menu_gallery,
                android.R.drawable.ic_menu_compass,
                android.R.drawable.ic_menu_camera,
                android.R.drawable.ic_menu_myplaces
        };

        int idx = 0;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PARTNER_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
            int age = cursor.getInt(cursor.getColumnIndexOrThrow(COL_AGE));
            String city = cursor.getString(cursor.getColumnIndexOrThrow(COL_CITY));
            String bio = cursor.getString(cursor.getColumnIndexOrThrow(COL_BIO));
            String hobbies = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_HOBBIES));
            boolean liked = cursor.getInt(cursor.getColumnIndexOrThrow(COL_LIKED)) == 1;

            int avatar = avatars[idx % avatars.length];
            idx++;
            list.add(new TravelPartner(id, name, age, city, bio, hobbies, avatar, liked));
        }
        cursor.close();
        return list;
    }

    public void setPartnerLiked(int partnerId, boolean isLiked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_LIKED, isLiked ? 1 : 0);
        db.update(TABLE_PARTNERS, values, COL_PARTNER_ID + "=?", new String[]{String.valueOf(partnerId)});
    }

    public void insertMessage(String senderUsername, String recipientName, String messageBody) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SENDER, senderUsername);
        values.put(COL_RECIPIENT_NAME, recipientName);
        values.put(COL_MSG_BODY, messageBody);
        db.insert(TABLE_MESSAGES, null, values);
    }
}