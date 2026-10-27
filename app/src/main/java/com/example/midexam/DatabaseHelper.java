package com.example.midexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "students.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE = "students";
    private static final String COL_ID = "student_id";
    private static final String COL_NAME = "name";
    private static final String COL_ENROLLED = "enrolled";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " ("
                + COL_ID + " TEXT PRIMARY KEY, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_ENROLLED + " INTEGER NOT NULL DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public boolean addStudent(Student s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ID, s.getStudentId());
        cv.put(COL_NAME, s.getName());
        cv.put(COL_ENROLLED, s.isEnrolled() ? 1 : 0);
        return db.insert(TABLE, null, cv) != -1;
    }

    public boolean updateStudent(Student s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, s.getName());
        cv.put(COL_ENROLLED, s.isEnrolled() ? 1 : 0);
        return db.update(TABLE, cv, COL_ID + " = ?", new String[]{s.getStudentId()}) > 0;
    }

    public boolean deleteStudent(String studentId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE, COL_ID + " = ?", new String[]{studentId}) > 0;
    }

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE, null, null, null, null, null, COL_NAME + " ASC");
        while (c.moveToNext()) {
            list.add(new Student(
                    c.getString(c.getColumnIndexOrThrow(COL_ID)),
                    c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                    c.getInt(c.getColumnIndexOrThrow(COL_ENROLLED)) == 1));
        }
        c.close();
        return list;
    }
}