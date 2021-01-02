package com.kaushalkishore192.myfamily

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import com.kaushalkishore192.myfamily.dataBase.TableColumnObject
import com.kaushalkishore192.myfamily.dataBase.sqlitedb_connection
import com.kaushalkishore192.myfamily.utility.getImageByte
import com.kaushalkishore192.myfamily.utility.insertToSqlite
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    var PICK_IMAGE_REQUEST = 1000
    private lateinit var imageFile: File
    var CAMERA_IMAGE_REQUEST = 1
    var currentImagePath = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
    fun initialiseView(){
        save_btn.isVisible = false
    }

    fun selectFromFiles(view: View) {

        try {
            Toast.makeText(this,"selecting from files",Toast.LENGTH_LONG).show()
            val file_intent = Intent()
            file_intent.setType("image/*")
            file_intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(file_intent,PICK_IMAGE_REQUEST)
        }
        catch (e: Exception){
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun selectFromCamera(view: View) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(cameraIntent.resolveActivity(packageManager) != null){
            try {
                imageFile = getImage()
            }
            catch (e: Exception ){
                e.printStackTrace()
            }
        }

        if (imageFile != null){
//            var a = FileProvider.getUriForFile(this,"com.example.android.fileprovider",imageFile)
            val uri_image = FileProvider.getUriForFile(this,"com.kaushalkishore192.myfamily.fileprovider",imageFile)
           cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri_image)
            startActivityForResult(cameraIntent,CAMERA_IMAGE_REQUEST)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == 1000) {
                Log.d("requestCode",requestCode.toString())
                Toast.makeText(this,"resultCode == 1000",Toast.LENGTH_LONG).show()
                var imagepath = data?.data
                var image = MediaStore.Images.Media.getBitmap(contentResolver, imagepath)
                image_view_im.setImageBitmap(image)
                save_btn.isVisible = true

            }
            else if (requestCode == 1) {
                Log.d("requestCode",requestCode.toString())
                var imagepath = data?.data
                val bitmap = BitmapFactory.decodeFile(currentImagePath)
                image_view_im.setImageBitmap(bitmap)
//                image_view_im.setImageResource(currentImagePath.)
                save_btn.isVisible = true

            }

            else{
                Log.d("requestCode",requestCode.toString())
            }

        }

        catch (e: Exception){
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
        }

    }


    @RequiresApi(Build.VERSION_CODES.P)
    fun saveButtonClicked(view: View) {
        val name = image_name_tv.text.toString()
        var im = (image_view_im.drawable as BitmapDrawable).bitmap
        val imageByte = getImageByte(im)
        var columnModelList = listOf<TableColumnObject>()
        val column1 = TableColumnObject("USER_NAME","TEXT")
        val column2 = TableColumnObject("IMAGE_FILE","BLOB")
        columnModelList +=  column1
        columnModelList += column2

        var dbConnection = sqlitedb_connection(this, "FAMILY_DATA", 1,columnModelList)
        var objectContentValues : ContentValues = ContentValues()
        objectContentValues.put("USER_NAME",name)
        objectContentValues.put("IMAGE_FILE",imageByte)
        try {
            val qryResponse = insertToSqlite("FAMILY_DATA",objectContentValues,dbConnection)
            if(qryResponse != 0){
                Toast.makeText(this,"data added",Toast.LENGTH_LONG).show()
            }
        }
        catch (e: Exception){
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private  fun getImage() : File {
        val imageName = "jpg_"+"sample"+ "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val imageFile = File.createTempFile(imageName,".jpg",storageDir)
        currentImagePath = imageFile.absolutePath

        return  imageFile
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun readfromSqliteDB(){
        var columnModelList = listOf<TableColumnObject>()
        val column1 = TableColumnObject("USER_NAME","TEXT")
        val column2 = TableColumnObject("IMAGE_FILE","BLOB")
        columnModelList +=  column1
        columnModelList += column2

        var dbConnection = sqlitedb_connection(this, "FAMILY_DATA", 1,columnModelList)

        var db = dbConnection.readableDatabase
        var rs = db.rawQuery("SELECT * from FAMILY_DATA",null)
        if (rs.moveToNext())
            Toast.makeText(this,rs.getString(1),Toast.LENGTH_LONG).show()

    }




}