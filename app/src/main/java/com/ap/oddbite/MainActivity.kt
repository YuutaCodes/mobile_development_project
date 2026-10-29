package com.ap.oddbite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.ap.oddbite.ui.navigation.OddBiteNavHost
import com.ap.oddbite.ui.theme.OddBiteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OddBiteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OddBiteNavHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
