package com.sarayrah.abdallah.uploadimage1

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var pathUri: Uri? = null

    var storageReference: StorageReference? = null

    val databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storageReference = FirebaseStorage.getInstance().reference

        button2.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 1)
        }

        button3.setOnClickListener {
            val contentResolver = contentResolver

            val fileExtension = MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(contentResolver.getType(pathUri))

            val storageReference2nd = storageReference?.child("Images/" +
                    System.currentTimeMillis().toString() + "." + fileExtension)

            storageReference2nd?.putFile(this.pathUri!!)?.addOnSuccessListener { taskSnapshot ->
                databaseReference?.child(databaseReference.push().key)?.setValue(
                        "image", taskSnapshot.downloadUrl.toString())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            pathUri = data?.data

            imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(contentResolver, pathUri))
        }
    }
}
