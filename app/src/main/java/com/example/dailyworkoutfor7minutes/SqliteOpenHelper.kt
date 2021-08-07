package com.example.dailyworkoutfor7minutes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteOpenHelper(context: Context,factory:SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME,factory, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1 // The Database Version
        private const val DATABASE_NAME = "SevenMinutesWorkout.db" // Name of Database
        private const val TABLE_HISTORY = "history" //Table name
        private const val COLUMN_ID = "_id" //Column id
        private const val COLUMN_COMPLETED_DATE = "completed_date" //Column for completed date
    }

    override fun onCreate(db: SQLiteDatabase?) {  // Here we will create a table
        // CREATE TABLE history (_id INTEGER PRIMARY KEY, completed_date TEXT )

        val CREATE_EXERCISE_TABLE = ("CREATE TABLE " + TABLE_HISTORY + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
        + COLUMN_COMPLETED_DATE + " TEXT" + ")")
        db?.execSQL(CREATE_EXERCISE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY)
        onCreate(db)
    }

    fun addDate(date:String){
        val values= ContentValues()
        values.put(COLUMN_COMPLETED_DATE,date)
        val db = this.writableDatabase // Allows us to read and write from the database
        db.insert(TABLE_HISTORY, null, values)
        db.close()
    }

    fun getAllCompletedDatesList():ArrayList<String>{
        val list = ArrayList<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_HISTORY",null)

        while (cursor.moveToNext()){
            val dateValue = (cursor.getString(cursor.getColumnIndex(COLUMN_COMPLETED_DATE)))
            list.add(dateValue)
        }
        cursor.close()
        return list
    }
}