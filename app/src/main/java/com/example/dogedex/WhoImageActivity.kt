package com.example.dogedex

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import coil.load
import com.example.dogedex.databinding.ActivityWhoImageBinding
import java.io.File

class WhoImageActivity : AppCompatActivity() {

    private lateinit var bindig: ActivityWhoImageBinding

    companion object {
        const val PHOTO_URI_KEY = "photo_uri"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindig = ActivityWhoImageBinding.inflate(layoutInflater)
        setContentView(bindig.root)

        val photoUri = intent.extras?.getString(PHOTO_URI_KEY)
        val uri = Uri.parse(photoUri)

        if (uri == null) {
            Toast.makeText(this, "Error showing image", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        bindig.wholeImage.load(uri.path?.let { File(it) })
    }
}