package ru.hse.egorov.reading_tracker.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.PopupMenu
import kotlinx.android.synthetic.main.activity_main.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.book_library.AddingBookActivity
import ru.hse.egorov.reading_tracker.ui.book_library.BookLibraryActivity
import ru.hse.egorov.reading_tracker.ui.session.ReadingSessionActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startSessionButton.setOnClickListener {
            val intent = Intent(this, ReadingSessionActivity::class.java)
            startActivity(intent)
        }

        popupMenuButton.setOnClickListener {
            showPopupMenu()
        }
    }

    private fun showPopupMenu() {
        val popup = PopupMenu(this, popupMenuButton)
        popup.menuInflater
                .inflate(R.menu.main_activity_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.toLibrary -> {
                    val intent = Intent(this, BookLibraryActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.toBookAdding -> {
                    val intent = Intent(this, AddingBookActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        popup.show()
    }
}
