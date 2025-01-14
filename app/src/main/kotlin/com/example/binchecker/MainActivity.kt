package com.example.binchecker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import com.example.binchecker.ui.navigation.NavScreen
import com.example.binchecker.ui.theme.BinCheckerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BinCheckerTheme {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        lightScrim = MaterialTheme.colorScheme.onPrimary.toArgb(),
                        darkScrim = MaterialTheme.colorScheme.onPrimary.toArgb()
                    )
                )

                NavScreen(Modifier.fillMaxSize())
            }
        }
    }
}