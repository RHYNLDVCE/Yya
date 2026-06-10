package com.muslima.yya.di

import com.muslima.yya.database.DatabaseDriverFactory
import com.muslima.yya.presentation.AdminViewModel
import com.muslima.yya.server.QuizServer
import org.koin.dsl.module

actual val platformModule = module {
    single { DatabaseDriverFactory() }
    single { QuizServer(get()) }
    factory { AdminViewModel(get(), get()) }
}
