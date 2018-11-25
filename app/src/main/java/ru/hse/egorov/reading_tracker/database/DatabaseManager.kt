package ru.hse.egorov.reading_tracker.database

import android.content.Context
import android.graphics.drawable.Drawable
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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

    fun addSession(session: Map<String, Any?>): Task<Void> {
        return db.collection("sessions").document(authManager.uid as String).set(session)
    }

    fun addBookToLibrary(book: Map<String, Any?>): Task<DocumentReference> {
        return db.collection("books").document("libraries").collection(authManager.uid as String)
                .add(book)
    }

    fun updateBook(book: Map<String, Any?>, bookId: String): Task<Void> {
        return db.collection("books").document("libraries").collection(authManager.uid as String)
                .document(bookId).update(book)
    }

    fun addBookCover(cover: ByteArray, bookId: String): UploadTask {
        return storageReference.child("covers").child(authManager.uid as String).child("$bookId/cover.png").putBytes(cover)
    }

    fun getBookCover(bookId: String, context: Context): GlideRequest<Drawable> {
        val path = storageReference.child("covers").child(authManager.uid as String).child("$bookId/cover.png")
        return GlideApp.with(context)
                .load(path)
    }

    fun deleteBookFromLibrary(bookId: String): Task<Void> {
        return db.collection("books").document("libraries").collection(authManager.uid as String).document(bookId).delete()
    }

    fun getLibrary() = db.collection("books").document("libraries").collection(authManager.uid as String).get()

    companion object {
        private val storageReference = FirebaseStorage.getInstance().reference
        private val authManager = FirebaseAuth.getInstance()
    }
}