package com.example.desenvolvimentoaplicacoes.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID

class NotesDataBaseHelper(context: Context):SQLiteOpenHelper(context,"databaseNotes",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_NOTES (" +
                    "$_ID INTEGER NOT NULL PRIMARY KEY" +
                    "$TITLE_NOTES TEXT NOT NULL"+
                    "$DESCRIPTION_NOTE TEXT NOT NULL"
                )

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    companion object{
        const val TABLE_NOTES : String = "Notes"
        const val TITLE_NOTES: String = "title"
        const val DESCRIPTION_NOTE: String = "description"
    }

}