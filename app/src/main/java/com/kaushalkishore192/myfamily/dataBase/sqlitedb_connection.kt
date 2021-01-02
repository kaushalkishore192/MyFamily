package com.kaushalkishore192.myfamily.dataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.P)
class sqlitedb_connection (
    context: Context?,
    name: String?,
    version: Int,
    structure : List<TableColumnObject>
) : SQLiteOpenHelper(context, name,null, version) {
    val table_name = name
    val st = structure

    fun getColumns(): String {
        var columns_str = ""
        for (columns in st){
            columns_str += columns.column_name + " "
            columns_str += columns.column_type + " "
            columns_str += columns.column_constraints + " "
            columns_str += ","

        }
        return columns_str.substring(0, columns_str.length-1)
    }
    override fun onCreate(sqldb: SQLiteDatabase?) {

        val qry = "CREATE TABLE $table_name (" +
                getColumns() +
                ")"
        sqldb?.execSQL(qry)
    }

    override fun onUpgrade(sqldb: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}