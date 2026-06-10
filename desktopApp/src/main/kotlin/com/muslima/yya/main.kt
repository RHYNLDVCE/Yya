package com.muslima.yya

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.muslima.yya.di.platformModule
import com.muslima.yya.di.sharedModule
import com.muslima.yya.presentation.AdminScreen
import com.muslima.yya.presentation.AdminViewModel
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin

fun main() {
    startKoin {
        modules(sharedModule, platformModule)
    }

    application {
        val state = androidx.compose.ui.window.rememberWindowState(placement = androidx.compose.ui.window.WindowPlacement.Maximized)
        Window(
            onCloseRequest = ::exitApplication,
            title = "Yya Admin Desktop",
            state = state
        ) {
            val adminViewModel = getKoin().get<AdminViewModel>()
            AdminScreen(adminViewModel)
        }
    }
}