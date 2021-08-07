package com.example.dailyworkoutfor7minutes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteOpenHelper(context: Context,factory:SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME,factory, DATABASE_VERSION) {

    companion object{
        private val DATABASE_VERSION = 1 // The Database Version
        private val DATABASE_NAME = "SevenMinutesWorkout.db" // Name of Database
        private val TABLE_HISTORY = "history" //Table name
        private val COLUMN_ID = "_id" //Column id
        private val COLUMN_COMPLETED_DATE = "Completed date" //Column for completed date
    }

    override fun onCreate(db: SQLiteDatabase?) {  // Here we will create a table
        // CREATE TABLE history (_id INTEGER PRIMARY KEY, completed_date TEXT )

        val CREATE_EXERCISE_TABLE = ("CREATE TABLE" + TABLE_HISTORY + "(" + COLUMN_ID + "INTEGER PRIMARY KEY,"
        + COLUMN_COMPLETED_DATE + " TEXT)")
        db?.execSQL(CREATE_EXERCISE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS" + TABLE_HISTORY)
        onCreate(db)
    }

    fun addDate(date:String){
        val values= ContentValues()
        values.put(COLUMN_COMPLETED_DATE,date)
        val db = this.writableDatabase // Allows us to read and write from the database
        db.insert(TABLE_HISTORY, null,values)
        db.close()
    }
}