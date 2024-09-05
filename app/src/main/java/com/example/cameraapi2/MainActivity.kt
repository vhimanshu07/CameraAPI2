package com.example.cameraapi2

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var photoFile :File
    val FILE_NAME = "selfie.jpg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn: TextView = findViewById(R.id.clickBtn)

        btn.setOnClickListener(this)

    }

    private fun openCamera() {
        TODO("Not yet implemented")
    }

    override fun onClick(p0: View?) {

        when (p0?.id) {
            R.id.clickBtn -> takePicture()

        }
    }

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                /**
                but from data we get low quality images , to get better quality images we will save it to file
                 and then pic the image from the file
                **/
                //val takenImage = it.data?.extras?.get("data") as? Bitmap
                val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
                val image: ImageView = findViewById(R.id.textureView)
                image.setImageBitmap(takenImage)
            }
        }


    private fun takePicture() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFile(FILE_NAME)

        /**
         * This doesn't work for API>=26
         */
        // takePicture.putExtra(MediaStore.EXTRA_OUTPUT,photoFile)
        /**
         * so use this instead but for it add data in manifest file too
         */
        val fileProvider  = FileProvider.getUriForFile(this,"com.example.cameraapi2.fileprovider",photoFile)
        takePicture.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider)
        //        if(takePicture.resolveActivity(this.packageManager)!=null)
        cameraResult.launch(takePicture)
//        else
//            Toast.makeText(this,"Unable to connect to camera",Toast.LENGTH_SHORT).show()
    }

    private fun getPhotoFile(fileName: String): File {

        // we can use 'getExternalFilesDir' on context to get package specific directories
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName,".jpg",storageDir)

    }
}

