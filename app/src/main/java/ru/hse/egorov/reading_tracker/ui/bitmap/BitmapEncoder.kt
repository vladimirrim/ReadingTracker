package ru.hse.egorov.reading_tracker.ui.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.support.v4.content.ContextCompat

interface BitmapEncoder {
    fun getBitmap(vectorDrawable: VectorDrawable): Bitmap {
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    fun getBitmap(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        return when (drawable) {
            is BitmapDrawable -> BitmapFactory.decodeResource(context.resources, drawableId)
            is VectorDrawable -> getBitmap((drawable as VectorDrawable?)!!)
            else -> throw IllegalArgumentException("unsupported drawable type")
        }
    }
}