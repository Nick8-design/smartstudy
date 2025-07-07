package com.nickdieda.smartstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nickdieda.smartstudy.presentation.dashboard.DashboardScreen
import com.nickdieda.smartstudy.presentation.theme.SmartStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartStudyTheme {
                DashboardScreen()
                }
            }
        }
    }

