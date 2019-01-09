package ru.hse.egorov.reading_tracker.ui.watcher

import android.text.Editable
import android.text.TextWatcher
import ru.hse.egorov.reading_tracker.ui.adapter.SearchCompletionAdapter


class SearchTextWatcher(private val adapter: SearchCompletionAdapter) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        adapter.getFilter().filter(s.toString().toLowerCase())
    }
}