package ru.hse.egorov.reading_tracker.database

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class DatabaseManager {

    fun auth(email: String, password: String): Task<AuthResult>? {
        return authManager?.createUserWithEmailAndPassword(email, password)
    }

    fun addUserInfo(info: Map<String, Any?>): Task<DocumentReference> {
        return db.collection("users").add(info)
    }

    fun isAuth(): Boolean {
        return authManager?.currentUser != null
    }

    fun signIn(email:String, password: String): Task<AuthResult>? {
        return authManager?.signInWithEmailAndPassword(email, password)
    }

    companion object {
        private val db = FirebaseFirestore.getInstance()
        private val authManager = FirebaseAuth.getInstance()
    }
}