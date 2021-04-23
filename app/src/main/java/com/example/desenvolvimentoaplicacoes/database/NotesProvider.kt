package com.example.desenvolvimentoaplicacoes.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.media.UnsupportedSchemeException
import android.net.Uri
import android.provider.BaseColumns

class NotesProvider : ContentProvider() {
    private lateinit var mUriMatcher: UriMatcher
    private lateinit var dbHelper: NotesDataBaseHelper
    override fun onCreate(): Boolean {

        mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        mUriMatcher.addURI(AUTHORITY,"notes", NOTES)
        mUriMatcher.addURI(AUTHORITY,"notes/#", NOTES_BY_ID)
        if(context != null){
                dbHelper = NotesDataBaseHelper(context as Context)
        }
        return true
    }
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        if(mUriMatcher.match(uri) == NOTES_BY_ID){
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val LinesAffect = db.delete(NotesDataBaseHelper.TABLE_NOTES,"${BaseColumns._ID} =?", arrayOf(uri.lastPathSegment))
            db.close()
            context?.contentResolver?.notifyChange(uri,null)
            return LinesAffect
        }else
            throw UnsupportedSchemeException("Uri inválida para exclusão!")

    }

    override fun getType(uri: Uri): String? = throw UnsupportedSchemeException("URI não implementado")


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if(mUriMatcher.match(uri) == NOTES){
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val id: Long = db.insert(NotesDataBaseHelper.TABLE_NOTES, null, values)
            val insertUri: Uri = Uri.withAppendedPath(BASE_URI,id.toString())
            db.close()
            context?.contentResolver?.notifyChange(uri,null)
            return insertUri
        }else{
            throw UnsupportedSchemeException("Uri inválida para inserção!")
        }
    }


    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when{
            mUriMatcher.match(uri) == NOTES -> {
                val db: SQLiteDatabase = dbHelper.writableDatabase
                val cursor: Cursor = db.query(NotesDataBaseHelper.TABLE_NOTES,projection,selection,selectionArgs,null,null,sortOrder)
                cursor.setNotificationUri(context?.contentResolver,uri)
                cursor
            }
            mUriMatcher.match(uri) == NOTES_BY_ID ->{
                val db: SQLiteDatabase = dbHelper.writableDatabase
                val cursor: Cursor = db.query(NotesDataBaseHelper.TABLE_NOTES,projection,"${BaseColumns._ID} = ?",
                    arrayOf(uri.lastPathSegment),null,null,sortOrder)
                cursor.setNotificationUri((context as Context).contentResolver,uri)
                cursor
            }
            else -> {
                throw  UnsupportedSchemeException("Uri não implementada")
            }
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        if (mUriMatcher.match(uri) == NOTES_BY_ID){
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val lineAffect : Int = db.update(NotesDataBaseHelper.TABLE_NOTES,values,"${BaseColumns._ID} = ?", arrayOf(uri.lastPathSegment))
            db.close()
            context?.contentResolver?.notifyChange(uri,null)
            return lineAffect
        }else {
            throw  UnsupportedSchemeException("Uri não implementada")
        }
    }

    companion object{
        const val AUTHORITY = "com.example.desenvolvimentoaplicacoes.provider"
        val BASE_URI = Uri.parse("content://$AUTHORITY")
        val URI_NOTES = Uri.withAppendedPath(BASE_URI,"notes")
        const val NOTES = 1
        const val NOTES_BY_ID = 2
    }
}