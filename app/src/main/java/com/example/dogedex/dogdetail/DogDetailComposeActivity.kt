package com.example.dogedex.dogdetail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.annotation.ExperimentalCoilApi
import com.example.dogedex.R
import com.example.dogedex.api.ApiResponseStatus
import com.example.dogedex.dogdetail.ui.theme.DogedexTheme
import com.example.dogedex.models.Dog

@ExperimentalCoilApi
class DogDetailComposeActivity : ComponentActivity() {

    companion object {
        const val DOG_KEY = "dog"
        const val IS_RECOGNITION_KEY = "is_recognition"
    }

    private val dogDetailViewModel: DogDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dog = intent?.extras?.getParcelable<Dog>(DOG_KEY)
        val isRecognition = intent?.extras?.getBoolean(IS_RECOGNITION_KEY, false) ?: false

        if (dog == null) {
            Toast.makeText(this, R.string.error_showing_dog_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        setContent {
            val status = dogDetailViewModel.status

            if (status.value is ApiResponseStatus.Success) {
                finish()
            } else {
                DogedexTheme {
                    DogDetailScreen(dog = dog, status = status.value, onButtonClicked = {
                        onButtonClicked(isRecognition, dog.id)
                    }, onErrorDialogDissmis = {
                        resetApiResponseStatus()
                    })
                }
            }

        }
    }

    private fun resetApiResponseStatus() {
        dogDetailViewModel.resetApiResponseStatus()
    }


    private fun onButtonClicked(isRecognition: Boolean, dogId: Long) {
        if (isRecognition) {
            dogDetailViewModel.addDogToUser(dogId)
        } else {
            finish()
        }
    }
}