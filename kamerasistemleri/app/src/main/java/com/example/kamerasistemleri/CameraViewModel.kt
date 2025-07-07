package com.example.kamerasistemleri


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.kamerasistemleri.Camera

class CameraViewModel : ViewModel() {
    // Uygulama açıkken kameraları tutan bir liste
    var cameraList = mutableStateListOf<Camera>()
        private set

    fun addCamera(camera: Camera) {
        cameraList.add(camera)
    }
}