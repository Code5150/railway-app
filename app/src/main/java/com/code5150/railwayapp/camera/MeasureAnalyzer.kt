package com.code5150.railwayapp.camera

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.util.*


class MeasureAnalyzer(private val onAnalysisFinished: (Int, Int) -> Unit): ImageAnalysis.Analyzer {
    private var rand = Random()

    override fun analyze(image: ImageProxy) {

        val width = (1510..1550).shuffled().first()
        val angle = (-140..140).shuffled().first()

        onAnalysisFinished(width, angle)
    }
}