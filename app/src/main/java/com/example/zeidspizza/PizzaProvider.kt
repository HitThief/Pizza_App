package com.example.zeidspizza
import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import java.lang.IllegalArgumentException
import java.util.HashMap
import kotlin.math.PI

class PizzaProvider() : ContentProvider() {
    //public
    companion object {
        val PROVIDER_NAME = "com.example.MyApplication.PizzasProvider"
        val URL = "content://" + PROVIDER_NAME + "/pizzas"
        val CONTENT_URI = Uri.parse(URL)
        val _ID = "_id"
        val PIZZA_NAME = "name"
        val PIZZA_PRICE = "price"
        private val PIZZAS_PROJECTION_MAP: HashMap<String, String>? = null
        val PIZZAS = 1
        val PIZZA_ID = 2
        val uriMatcher: UriMatcher? = null
        val DATABASE_NAME = "orders"
        val PIZZAS_TABLE_NAME = "pizzas"
        val DATABASE_VERSION = 1
        val CREATE_DB_TABLE = " CREATE TABLE " + PIZZAS_TABLE_NAME +        " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + " name TEXT NOT NULL, " +
                " price INTEGER NOT NULL);"
        private var sUriMatcher = UriMatcher(UriMatcher.NO_MATCH);
        init
        {
            sUriMatcher.addURI(PROVIDER_NAME, "pizzas", PIZZAS);
            sUriMatcher.addURI(PROVIDER_NAME, "pizzas/#", PIZZA_ID);
        }
    }
    private var db: SQLiteDatabase? = null  //private
    //internal class
    private class DatabaseHelper internal constructor(context: Context?) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_DB_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS " + PIZZAS_TABLE_NAME)
            onCreate(db)
        }
    }

    //6 functions
    override fun onCreate(): Boolean {
        val context = context
        val dbHelper = DatabaseHelper(context)
        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.  */
        db = dbHelper.writableDatabase
        return if (db == null) false else true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {

        val rowID = db!!.insert(PIZZAS_TABLE_NAME, "", values)

        if (rowID > 0) {
            val _uri = ContentUris.withAppendedId(CONTENT_URI, rowID)
            context!!.contentResolver.notifyChange(_uri, null)
            return _uri
        }
        throw SQLException("Failed to add a record into $uri")
    }

    override fun query(
        uri: Uri, projection: Array<String>?,
        selection: String?, selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        var sortOrder = sortOrder
        val qb = SQLiteQueryBuilder()
        qb.tables = PIZZAS_TABLE_NAME
        if (uriMatcher != null) {
            when (uriMatcher.match(uri)) {
                 PIZZAS -> qb.projectionMap =
                    PIZZAS_PROJECTION_MAP
                PIZZA_ID -> qb.appendWhere(_ID + "=" + uri.pathSegments[1])
                else -> {null
                }
            }
        }


        if (sortOrder == null || sortOrder === "") {
            /**
             * By default sort on student names
             */
            sortOrder = PIZZA_NAME
        }
        val c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder)
        /**
         * register to watch a content URI for changes  */
        c.setNotificationUri(context!!.contentResolver, uri)
        return c
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var count = 0
        if(selection == null && selectionArgs == null)
        {db?.execSQL("DELETE FROM pizzas")
            return 0}
        when (uriMatcher!!.match(uri)) {
            PIZZAS -> count = db!!.delete(
                PIZZAS_TABLE_NAME, selection,
                selectionArgs
            )
            PIZZA_ID -> {
                val id = uri.pathSegments[1]
                count = db!!.delete(
                    PIZZAS_TABLE_NAME,
                    _ID + " = " + id +
                            if (!TextUtils.isEmpty(selection)) " AND ($selection)" else "",
                    selectionArgs
                )
            }
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?  ): Int {
        var count = 0
        when (uriMatcher!!.match(uri)) {
            PIZZAS -> count = db!!.update(
                PIZZAS_TABLE_NAME, values, selection,
                selectionArgs
            )
            PIZZA_ID -> count = db!!.update(
                PIZZAS_TABLE_NAME,
                values,
                _ID + " = " + uri.pathSegments[1] + (if (!TextUtils.isEmpty(selection)) " AND ($selection)" else ""),
                selectionArgs
            )
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun getType(uri: Uri): String? {
        when (uriMatcher!!.match(uri)) {
            PIZZAS -> return "These info about pizzas"
            PIZZA_ID -> return "This info about specific pizza"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }
}