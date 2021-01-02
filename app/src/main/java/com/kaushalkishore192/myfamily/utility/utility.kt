package com.kaushalkishore192.myfamily.utility

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Debug
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.kaushalkishore192.myfamily.dataBase.sqlitedb_connection
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.lang.Exception

fun getImageByte(image : Bitmap): ByteArray {
    var byte_stream = ByteArrayOutputStream()

    try {
        image. compress(Bitmap.CompressFormat.JPEG,100,byte_stream)
    }
    catch (e: Exception){
        Log.d("Exception", e.message!!);
    }
    var image_byte = byte_stream.toByteArray()
    return image_byte

}

@RequiresApi(Build.VERSION_CODES.P)
fun insertToSqlite(table_name : String,
                   objectContentValues : ContentValues,
                   connection : sqlitedb_connection): Int {

    var checkIfQryRuns = 0
    try {
        var db = connection.writableDatabase
        checkIfQryRuns = db.insert(table_name,null,objectContentValues).toInt()

    }
    catch (e: Exception){
        Log.d("Exception", e.message!!);
    }
    return checkIfQryRuns

}