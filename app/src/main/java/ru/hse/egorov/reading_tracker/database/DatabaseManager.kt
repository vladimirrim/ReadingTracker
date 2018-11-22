package ru.hse.egorov.reading_tracker.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.target.ViewTarget
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import ru.hse.egorov.reading_tracker.glide.GlideApp
import ru.hse.egorov.reading_tracker.glide.GlideRequest


class DatabaseManager {
    private val db = FirebaseFirestore.getInstance(authManager.app)

    fun auth(email: String, password: String): Task<AuthResult>? {
        return authManager?.createUserWithEmailAndPassword(email, password)
    }

    fun addUserInfo(info: Map<String, Any?>): Task<Void> {
        return db.collection("users").document(authManager.uid as String).set(info)
    }

    fun isAuth(): Boolean {
        return authManager?.currentUser != null
    }

    fun signIn(email: String, password: String): Task<AuthResult>? {
        return authManager?.signInWithEmailAndPassword(email, password)
    }

    fun signUpWithGoogle(acct: GoogleSignInAccount): Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        return authManager.signInWithCredential(credential)
    }

    fun addSession(time: String): Task<Void> {
        val map = HashMap<String, List<String>>()
        map["sessions"] = listOf(time)
        return db.collection("users").document(authManager.uid as String).update("sessions",
                FieldValue.arrayUnion("${System.currentTimeMillis()} $time"))
    }

    fun addBookToLibrary(book: Map<String, Any?>): Task<DocumentReference> {
        return db.collection("books").document("libraries").collection(authManager.uid as String)
                .add(book)
    }

    fun addBookCover(cover: ByteArray, bookId: String): UploadTask {
        return storageReference.child("covers").child(authManager.uid as String).child("$bookId/cover.png").putBytes(cover)
    }

    fun getBookCover(bookId: String, context: Context): GlideRequest<Drawable> {
        return GlideApp.with(context)
                .load(storageReference.child("covers").child(authManager.uid as String).child("$bookId/cover.png"))
    }

    fun deleteBookFromLibrary(bookId: String): Task<Void> {
        return db.collection("books").document("libraries").collection(authManager.uid as String).document(bookId).delete()
    }

    fun getLibrary() = db.collection("books").document("libraries").collection(authManager.uid as String).get()

    companion object {
        private const val ONE_MEGABYTE: Long = 1024 * 1024
        private val storageReference = FirebaseStorage.getInstance().reference
        private val authManager = FirebaseAuth.getInstance()
    }
}