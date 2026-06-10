package com.muslima.yya.di

import com.muslima.yya.database.DatabaseDriverFactory
import org.koin.dsl.module

actual val platformModule = module {
    single { DatabaseDriverFactory(get()) }
    factory { com.muslima.yya.presentation.StudentViewModel(get()) }
}
