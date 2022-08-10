package com.example.dogedex.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.dogedex.LABEL_PATH
import com.example.dogedex.MODEL_PATH
import com.example.dogedex.api.ApiResponseStatus
import com.example.dogedex.api.ApiServiceInterceptor
import com.example.dogedex.auth.LoginActivity
import com.example.dogedex.databinding.ActivityMainBinding
import com.example.dogedex.dogdetail.DogDetailComposeActivity.Companion.DOG_KEY
import com.example.dogedex.dogdetail.DogDetailComposeActivity.Companion.IS_RECOGNITION_KEY
import com.example.dogedex.dogdetail.DogDetailComposeActivity
import com.example.dogedex.doglist.DogListActivity
import com.example.dogedex.machinelearning.Classifier
import com.example.dogedex.machinelearning.DogRecognition
import com.example.dogedex.models.Dog
import com.example.dogedex.models.User
import com.example.dogedex.settings.SettingsActivity
import org.tensorflow.lite.support.common.FileUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                setUpCamera()
            } else {
                Toast.makeText(
                    this,
                    "You need to accept camera permission to use camera",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var classifier: Classifier
    private val mainViewModel: MainViewModel by viewModels()


    private var isCameraReady = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = User.getLoggedInUser(this)
        if (user == null) {
            openLoginActivity()
            return
        } else {
            ApiServiceInterceptor.setSessionToken(user.authenticationToken)
        }

        binding.dogSettingsFab.setOnClickListener {
            openSettingsActivity()
        }

        binding.dogListFab.setOnClickListener {
            openDogListActivity()
        }

        mainViewModel.status.observe(this) { status ->
            when (status) {
                is ApiResponseStatus.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, status.message, Toast.LENGTH_SHORT).show()
                }
                is ApiResponseStatus.Loading -> binding.progressBar.visibility = View.VISIBLE

                is ApiResponseStatus.Success -> binding.progressBar.visibility = View.GONE
            }
        }

        mainViewModel.dog.observe(this) { dog ->
            if (dog != null) {
                openDogDetailActivity(dog)
            }
        }

        mainViewModel.dogRecognition.observe(this) {
            enableTakePhotoButton(it)

        }

        requestCameraPermission()
    }

    private fun openDogDetailActivity(dog: Dog) {
        val intent = Intent(this, DogDetailComposeActivity::class.java)
        intent.putExtra(DOG_KEY, dog)
        intent.putExtra(IS_RECOGNITION_KEY, true)
        startActivity(intent)

    }

    override fun onStart() {
        super.onStart()
        mainViewModel.setUpClassifier(
            FileUtil.loadMappedFile(this@MainActivity, MODEL_PATH), FileUtil.loadLabels(
                this@MainActivity,
                LABEL_PATH
            )
        )
    }

    private fun setUpCamera() {
        binding.previewView.post {
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.previewView.display.rotation)
                .build()

            // Initialize our background executor
            cameraExecutor = Executors.newSingleThreadExecutor()
            startCamera()
            isCameraReady = true
        }

    }

    private fun requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    setUpCamera()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
                -> {
                    AlertDialog.Builder(this)
                        .setTitle("Aceptame por favor")
                        .setMessage("Acepta la camara")
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                        .setNegativeButton(android.R.string.cancel) { _, _ ->
                        }

                }
                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
            }
        } else {
            setUpCamera()
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview: Preview = Preview.Builder()
                .build()

            preview.setSurfaceProvider(binding.previewView.surfaceProvider)

            val cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA


            val imageAnalysis = ImageAnalysis.Builder()
                // enable the following line if RGBA output is needed.
                // .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                mainViewModel.recognizeImage(imageProxy)


                // enableTakePhotoButton(dogRecognition)


            }


            // Bind use case to camera
            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalysis
            )
        }, ContextCompat.getMainExecutor(this))
    }

    private fun enableTakePhotoButton(dogRecognition: DogRecognition) {
        if (dogRecognition.confidence > 40.0) {
            binding.takePhotoTab.alpha = 1f
            binding.takePhotoTab.setOnClickListener {
                mainViewModel.getDogByMlId(dogRecognition.id)
            }
        } else {
            binding.takePhotoTab.alpha = 0.2f
            binding.takePhotoTab.setOnClickListener(null)
        }

    }

    private fun openDogListActivity() {
        startActivity(Intent(this, DogListActivity::class.java))
    }

    private fun openSettingsActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun openLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::cameraExecutor.isInitialized) {
            cameraExecutor.shutdown()

        }
    }


}