package ru.hse.egorov.reading_tracker.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.bitmap.BitmapEncoder
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment
import ru.hse.egorov.reading_tracker.ui.login.SignUpSignInActivity


class SplashActivity : AppCompatActivity(),BitmapEncoder {
    private val db = DatabaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashScreenTheme)
        super.onCreate(savedInstanceState)

        chooseActivity()
        finish()
    }

    private fun chooseActivity() {

        if (db.isAuth()) {
            db.getLibrary().addOnSuccessListener {
                val libraryAdapter = LibraryFragment.getAdapter()
                for (book in it.documents) {
                    libraryAdapter.add(LibraryFragment.Book(book["author"] as String,book["title"] as String,book.id,0,
                            getBitmap(baseContext, R.drawable.ic_stab_cover)))
                }
            }
            val intent = Intent(this,
                    MainActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this,
                    SignUpSignInActivity::class.java)
            startActivity(intent)
        }
    }
}
