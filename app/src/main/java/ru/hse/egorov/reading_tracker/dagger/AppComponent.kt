package ru.hse.egorov.reading_tracker.dagger

import dagger.Component
import ru.hse.egorov.reading_tracker.ui.MainActivity
import ru.hse.egorov.reading_tracker.ui.SplashActivity
import ru.hse.egorov.reading_tracker.ui.book_library.AddingBookFragment
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment
import ru.hse.egorov.reading_tracker.ui.login.SignInActivity
import ru.hse.egorov.reading_tracker.ui.login.SignUpEmailActivity
import ru.hse.egorov.reading_tracker.ui.session.StartOfSessionFragment
import ru.hse.egorov.reading_tracker.ui.session.session_inner_fragments.AutoSessionTimeChangeFragment
import ru.hse.egorov.reading_tracker.ui.session.session_inner_fragments.ManualSessionTimeChangeFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(fragment: AddingBookFragment)

    fun inject(activity: SplashActivity)

    fun inject(activity: MainActivity)

    fun inject(activity: SignUpEmailActivity)

    fun inject(activity: SignInActivity)

    fun inject(fragment: StartOfSessionFragment)

    fun inject(fragment: AutoSessionTimeChangeFragment)

    fun inject(fragment: ManualSessionTimeChangeFragment)

    fun inject(fragment: LibraryFragment)
}