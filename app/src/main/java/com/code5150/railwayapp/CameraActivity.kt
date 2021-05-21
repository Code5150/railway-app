package com.code5150.railwayapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.code5150.railwayapp.camera.CameraMediator
import com.code5150.railwayapp.databinding.ActivityCameraBinding
import com.code5150.railwayapp.model.AnalysisResultParams
import java.io.File

class CameraActivity : AppCompatActivity() {

    companion object {
        const val PHOTO_URI: String = "PHOTO_URI"
        private const val CURRENT_CAMERA = "CUR_CAMERA"
        private const val FLASH_MODE = "FLASH_MODE"
        private const val PREF = "TASK6_PREF"
        private const val WIDTH = "WIDTH"
        private const val ANGLE = "ANGLE"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraMediator: CameraMediator

    private var width: Int = 0
    private var angle: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraMediator = CameraMediator(this, this, binding.previewView, onAnalysisSuccess)

        if (allPermissionsGranted()) {
            cameraMediator.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        binding.takePhoto.setOnClickListener {
            if (width >= 1510) {
                /*val intent =
                    Intent(this@CameraActivity, RecognitionResultsActivity::class.java)
                intent.putExtra(WIDTH, width)
                intent.putExtra(ANGLE, angle)
                startActivity(intent)*/
                Log.d(CURRENT_CAMERA, "Width: $width, Angle: $angle")
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraMediator.startCamera()
            } else {
                Log.d("Task6", "Permission isn't granted by the user")
                finish()
            }
        }
    }

    private val onAnalysisSuccess = { w: Int, a: Int ->
        width = w
        angle = a
    }
}