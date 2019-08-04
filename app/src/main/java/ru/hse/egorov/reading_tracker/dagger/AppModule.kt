package ru.hse.egorov.reading_tracker.dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.hse.egorov.reading_tracker.ui.adapter.LibraryAdapter
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideLibrary(): LibraryAdapter = LibraryAdapter()
}