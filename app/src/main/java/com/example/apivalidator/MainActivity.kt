package com.example.apivalidator

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class MainActivity : AppCompatActivity() {

    private val PICK_IMAGE = 100
    var  imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        browse.setOnClickListener {
            datta.visibility = View.GONE
            openGallery()
        }

    }


    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }

    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var imageServer: MultipartBody.Part? = null
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data?.data
            imagee.setImageURI(imageUri)
            progressBar.visibility= View.VISIBLE
            //val file = imageUri?.toFile()
            //val path: String? = imageUri?.let { getRealPathFromURI(applicationContext, it) }
            //val path = imageUri?.let { getPathFromUri(this, it) }
            Log.i("Ayush", "ayush: $imageUri")
            val z = imageUri!!.path

            val path:String? = getRealPath(this, imageUri!!)
            val file = File(path)

            var rFile : RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            imageServer = MultipartBody.Part.createFormData("Image", file.name, rFile)
            Log.i("Ayush", "URI: " + imageUri.toString() + " Real: " + z)
        }

        var x = RetrofitServiceBuilder.api.uploadData(imageServer!!)
        x.enqueue(object : Callback<dataReturned> {
            override fun onResponse(call: Call<dataReturned>, response: Response<dataReturned>) {

                progressBar.visibility = View.GONE

                //Snackbar.make(browse,response.body()!!.status,Snackbar.LENGTH_LONG)
                Log.i("Ayush", "Successful Upload: "+response.body().toString())

                datta.text = response.body()!!.Predicted_Class

                datta.visibility = View.VISIBLE

            }

            override fun onFailure(call: Call<dataReturned>, t: Throwable) {
                progressBar.visibility = View.GONE
                Snackbar.make(browse, t.message.toString(), Snackbar.LENGTH_LONG)
                Log.i("Ayush", "Error: " + t.message.toString().toString())
            }

        })
    }





    private fun  getRealPath(context: Context, uri: Uri):String?
    {
        val resolver = contentResolver
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var path:String? = null
        val cursor: Cursor? = resolver.query(uri, null, null, null, null)
        val column_index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        Log.i("Ayush", "Count: " + cursor?.count.toString())
        cursor?.moveToNext()
        path = column_index?.let { cursor?.getString(it) }
        cursor?.close()
        return path
    }



    override fun onResume() {
        super.onResume()
        datta.text = ""
    }

}