package com.code5150.railwayapp.camera

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.code5150.railwayapp.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraMediator(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val previewView: PreviewView,
    private val switchCameraButton: ImageButton
) {

    companion object {
        private const val TAG = "Camera"
    }

    var currentCam: Int = CameraSelector.LENS_FACING_BACK
    var flashMode: Int = ImageCapture.FLASH_MODE_AUTO

    private var preview: Preview? = null
    private var camera: Camera? = null

    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private lateinit var cameraSelector: CameraSelector
    private lateinit var imageCapture: ImageCapture

    fun startCamera(cam: Int = currentCam) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            preview = Preview.Builder()
                .build()

            imageCapture = ImageCapture.Builder()
                .setFlashMode(flashMode)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()

            cameraSelector = CameraSelector.Builder().requireLensFacing(cam).build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageCapture
                )
                preview?.setSurfaceProvider(previewView.surfaceProvider)

                //setting that new camera is being used
                currentCam = cam
            } catch (exc: Exception) {
                switchCameraButton.visibility = View.GONE
                //starting previous cam again
                startCamera(currentCam)
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }

    fun switchCamera(){
        when (currentCam) {
            CameraSelector.LENS_FACING_BACK -> startCamera(CameraSelector.LENS_FACING_FRONT)
            CameraSelector.LENS_FACING_FRONT -> startCamera(CameraSelector.LENS_FACING_BACK)
            else -> Log.d(TAG, "Unknown camera")
        }
    }

    fun takePhoto(outputDirectory: File, onSaveAction: (File)->Unit) {
        // Create timestamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(context.getString(R.string.filename_format)).format(System.currentTimeMillis()) + ".jpg"
        )
        Log.d(TAG, photoFile.toString())

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Setup image capture listener which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(context), object :
                ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.d(TAG, photoFile.toString())
                    onSaveAction(photoFile)
                }
            })
    }

    fun setFlashMode() {
        when (flashMode) {
            ImageCapture.FLASH_MODE_OFF -> {
                flashMode = ImageCapture.FLASH_MODE_ON
            }
            ImageCapture.FLASH_MODE_ON -> {
                flashMode = ImageCapture.FLASH_MODE_AUTO
            }
            ImageCapture.FLASH_MODE_AUTO -> {
                flashMode = ImageCapture.FLASH_MODE_OFF
            }
        }
        startCamera(currentCam)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setFocusListener(focusIndicator: ImageView) {
        previewView.setOnTouchListener { _, event ->
            return@setOnTouchListener when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val factory: MeteringPointFactory = SurfaceOrientedMeteringPointFactory(
                        previewView.width.toFloat(), previewView.height.toFloat()
                    )
                    val autoFocusPoint = factory.createPoint(event.x, event.y)
                    try {
                        val focused = camera?.cameraControl?.startFocusAndMetering(
                            FocusMeteringAction.Builder(
                                autoFocusPoint,
                                FocusMeteringAction.FLAG_AF
                            )
                                .disableAutoCancel()
                                .build()
                        )

                        focused?.addListener(Runnable {
//                            if (!focused.isCancelled) {
//                                try {
//                                    if (focused.get().isFocusSuccessful) {
//                                        focusIndicator.setImageDrawable(context.getDrawable(R.drawable.ic_is_focused))
//                                    } else {
//                                        focusIndicator.setImageDrawable(context.getDrawable(R.drawable.ic_no_focus))
//                                    }
//                                } catch (e: Exception) {
//                                    Log.e("ERROR", "Something went wrong", e)
//                                }
//                            } else focusIndicator.setImageDrawable(context.getDrawable(R.drawable.ic_no_focus))
                            Thread.sleep(3000L)
                            focused.cancel(false)
                            /*focusIndicator.setImageDrawable(context.getDrawable(R.drawable.ic_no_focus))*/
                        }, cameraExecutor)
                    } catch (e: CameraInfoUnavailableException) {
                        Log.e("ERROR", "Cannot access camera", e)
                    }
                    true
                }
                else -> false // Unhandled event
            }
        }
    }
}