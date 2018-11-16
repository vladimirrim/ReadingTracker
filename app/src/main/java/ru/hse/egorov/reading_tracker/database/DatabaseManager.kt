package ru.hse.egorov.reading_tracker.database

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference


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

    fun deleteBookFromLibrary(bookId: String): Task<Void> {
        return db.collection("books").document("libraries").collection(authManager.uid as String).document(bookId).delete()
    }

    fun getLibrary() = db.collection("books").document("libraries").collection(authManager.uid as String).get()

    companion object {
        private val authManager = FirebaseAuth.getInstance()
    }
}