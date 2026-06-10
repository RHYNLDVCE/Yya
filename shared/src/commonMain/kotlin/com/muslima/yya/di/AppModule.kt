package com.muslima.yya.di

import com.muslima.yya.data.remote.QuizClient
import com.muslima.yya.data.repository.QuizRepositoryImpl
import com.muslima.yya.database.DatabaseDriverFactory
import com.muslima.yya.database.QuizDatabase
import com.muslima.yya.domain.repository.QuizRepository
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { QuizDatabase(get<DatabaseDriverFactory>().createDriver()) }
    single<QuizRepository> { QuizRepositoryImpl(get()) }
    single { QuizClient() }
}
