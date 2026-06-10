package com.muslima.yya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.muslima.yya.di.platformModule
import com.muslima.yya.di.sharedModule
import com.muslima.yya.presentation.StudentScreen
import com.muslima.yya.presentation.StudentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        try {
            startKoin {
                androidContext(this@MainActivity)
                modules(sharedModule, platformModule)
            }
        } catch (e: Exception) {
            // Koin already started
        }

        setContent {
            val studentViewModel = getKoin().get<StudentViewModel>()
            StudentScreen(studentViewModel)
        }
    }
}