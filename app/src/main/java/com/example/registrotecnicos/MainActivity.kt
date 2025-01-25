package com.example.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.registrotecnicos.presentation.navigation.NavigationNavHost
import com.example.registrotecnicos.ui.theme.RegistrotecnicosTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            RegistrotecnicosTheme {
                val navHost = rememberNavController()
                Surface(modifier = Modifier.systemBarsPadding()) {
                    NavigationNavHost(navHost)
                }
            }
        }
    }
}