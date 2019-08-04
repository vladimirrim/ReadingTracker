package ru.hse.egorov.reading_tracker

import android.app.Application
import ru.hse.egorov.reading_tracker.dagger.AppComponent
import ru.hse.egorov.reading_tracker.dagger.AppModule
import ru.hse.egorov.reading_tracker.dagger.DaggerAppComponent

class ReadingTrackerApplication: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)
    }

    private fun initDagger(app: Application): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()
}