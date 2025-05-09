package com.example.run_app_rma

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.run_app_rma.data.remote.AuthRepository
import com.example.run_app_rma.sensor.tracking.LocationService
import com.example.run_app_rma.sensor.tracking.SensorService
import com.example.run_app_rma.ui.theme.Run_app_RMATheme
import com.example.run_app_rma.presentation.login.LoginScreen

class MainActivity : ComponentActivity() {

    private lateinit var locationService: LocationService
    private lateinit var sensorService: SensorService
    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Toast.makeText(this, "Location permission required!", Toast.LENGTH_LONG).show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        locationService = LocationService(this)
        sensorService = SensorService(this)

        locationPermissionRequest.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)

        setContent {
            Run_app_RMATheme {
                val authRepository = remember { AuthRepository() }
                var isLoggedIn by remember { mutableStateOf(authRepository.isLoggedIn()) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (isLoggedIn) {
                        SensorDataScreen(
                            modifier = Modifier.padding(innerPadding),
                            locationService = locationService,
                            sensorService = sensorService,
                            onLogout = {
                                authRepository.logout()
                                isLoggedIn = false
                            }
                        )
                    } else {
                        LoginScreen(authRepository = authRepository) {
                            isLoggedIn = true
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SensorDataScreen(
    modifier: Modifier = Modifier,
    locationService: LocationService,
    sensorService: SensorService,
    onLogout: () -> Unit
) {
    var locationText by remember { mutableStateOf("Waiting for location...") }
    var accelerometerText by remember { mutableStateOf("Waiting for accelerometer...") }
    var gyroscopeText by remember { mutableStateOf("Waiting for gyroscope...") }

    DisposableEffect(Unit) {
        locationService.startLocationUpdates { location ->
            locationText = "Lat: ${location.latitude}, Lng: ${location.longitude}"
        }
        sensorService.startListening(
            onAccelerometerData = { values ->
                accelerometerText = "Acc -> X:${values[0]}, Y:${values[1]}, Z:${values[2]}"
            },
            onGyroscopeData = { values ->
                gyroscopeText = "Gyro -> X:${values[0]}, Y:${values[1]}, Z:${values[2]}"
            }
        )
        onDispose {
            locationService.stopLocationUpdates()
            sensorService.stopListening()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = locationText)
        Text(text = accelerometerText)
        Text(text = gyroscopeText)

        Button(
            onClick = onLogout,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text("Logout")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Run_app_RMATheme {
        Text("Hello Android!")
    }
}
