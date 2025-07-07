package com.example.kamerasistemleri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kamerasistemleri.ui.theme.KamerasistemleriTheme


// Camera VIEWMODEL
class CameraViewModel : ViewModel() {
    var cameraList = mutableStateListOf<Camera>()
        private set

    fun addCamera(camera: Camera) {
        cameraList.add(camera)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KamerasistemleriTheme {
                KameraUygulamasi()
            }
        }
    }
}

@Composable
fun KameraUygulamasi(cameraViewModel: CameraViewModel = viewModel()) {
    var showAddScreen by remember { mutableStateOf(false) }

    if (showAddScreen) {
        KameraEkleScreen(
            onCameraAdded = { camera ->
                cameraViewModel.addCamera(camera)
                showAddScreen = false
            },
            onCancel = { showAddScreen = false }
        )
    } else {
        KameraListesiScreen(
            cameras = cameraViewModel.cameraList,
            onAddClick = { showAddScreen = true },
            onCameraClick = { camera ->
                // Canlı izleme ekranına buradan yönlendirebilirsin
            }
        )
    }
}

@Composable
fun KameraListesiScreen(
    cameras: List<Camera>,
    onAddClick: () -> Unit,
    onCameraClick: (Camera) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Kameralarım") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (cameras.isEmpty()) {
                Text("Henüz eklenmiş kamera yok.")
            } else {
                cameras.forEach { camera ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onCameraClick(camera) }
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = camera.name, style = MaterialTheme.typography.titleLarge)
                            Text(text = "IP: ${camera.ip}:${camera.port}")
                            if (camera.username.isNotBlank()) Text("Kullanıcı: ${camera.username}")
                            if (camera.streamUrl.isNotBlank()) Text("Stream: ${camera.streamUrl}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KameraEkleScreen(
    onCameraAdded: (Camera) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ip by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var streamUrl by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Kamera Ekle", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(
            value = name, onValueChange = { name = it }, label = { Text("Kamera Adı") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = ip, onValueChange = { ip = it }, label = { Text("IP Adresi") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = port, onValueChange = { port = it }, label = { Text("Port") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = username, onValueChange = { username = it }, label = { Text("Kullanıcı Adı") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it }, label = { Text("Şifre") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = streamUrl, onValueChange = { streamUrl = it }, label = { Text("RTSP/HTTP Stream URL") }, modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.padding(top = 16.dp)) {
            Button(
                onClick = {
                    if (
                        name.isNotBlank() &&
                        ip.isNotBlank() &&
                        port.isNotBlank() &&
                        streamUrl.isNotBlank()
                    ) {
                        onCameraAdded(
                            Camera(
                                name = name,
                                ip = ip,
                                port = port.toIntOrNull() ?: 554,
                                username = username,
                                password = password,
                                streamUrl = streamUrl
                            )
                        )
                    }
                },
                enabled = name.isNotBlank() && ip.isNotBlank() && port.isNotBlank() && streamUrl.isNotBlank()
            ) {
                Text("Kaydet")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = onCancel) {
                Text("İptal")
            }
        }
    }
}
