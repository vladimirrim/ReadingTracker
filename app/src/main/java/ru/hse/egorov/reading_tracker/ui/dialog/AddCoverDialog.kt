package ru.hse.egorov.reading_tracker.ui.dialog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.book_library.AddingBookFragment.Companion.REQUEST_IMAGE_CAMERA
import ru.hse.egorov.reading_tracker.ui.book_library.AddingBookFragment.Companion.REQUEST_IMAGE_GALLERY
import ru.hse.egorov.reading_tracker.ui.book_library.AddingBookFragment.Companion.REQUEST_SCAN_ISBN


class AddCoverDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Выберите способ")
                .setItems(R.array.add_cover_array) { _, which ->
                    when (which) {
                        CAMERA_ID -> {
                            dispatchTakePictureFromCameraIntent()
                        }
                        GALLERY_ID -> {
                            dispatchTakePictureFromGalleryIntent()
                        }
                        ISBN_ID -> {
                            dispatchScanISBNIntent()
                        }
                    }
                }
        return builder.create()
    }

    private fun dispatchTakePictureFromCameraIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                activity!!.supportFragmentManager.findFragmentById(R.id.temporaryFragment)!!.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAMERA)
            }
        }
    }

    private fun dispatchTakePictureFromGalleryIntent() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                activity!!.supportFragmentManager.findFragmentById(R.id.temporaryFragment)!!.startActivityForResult(takePictureIntent, REQUEST_IMAGE_GALLERY)
            }
        }
    }

    private fun dispatchScanISBNIntent() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                activity!!.supportFragmentManager.findFragmentById(R.id.temporaryFragment)!!.startActivityForResult(takePictureIntent, REQUEST_SCAN_ISBN)
            }
        }
    }

    companion object {
        const val GALLERY_ID = 1
        const val CAMERA_ID = 0
        const val ISBN_ID = 2
    }
}