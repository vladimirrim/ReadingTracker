package ru.hse.egorov.reading_tracker.ui.pager

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent


class FragmentViewPager : ViewPager {

    private var isPagingEnabled: Boolean = false

    constructor(context: Context) : super(context) {
        isPagingEnabled = false
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        isPagingEnabled = false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return isPagingEnabled && super.onTouchEvent(event)
    }

    override fun executeKeyEvent(event: KeyEvent): Boolean {
        return isPagingEnabled && super.executeKeyEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return isPagingEnabled && super.onInterceptTouchEvent(event)
    }

    fun setPagingEnabled(enabled: Boolean) {
        isPagingEnabled = enabled
    }
}