package ru.hse.egorov.reading_tracker.database

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class DatabaseManager {
    private val db = FirebaseFirestore.getInstance()
    private val authManager: FirebaseAuth? = FirebaseAuth.getInstance()

    fun auth(email: String, password: String): Task<AuthResult>? {
        return authManager?.createUserWithEmailAndPassword(email, password)
    }

    fun addUserInfo(info: Map<String, String>): Task<DocumentReference> {
        val data = HashMap<String, Any?>()
        for (item in info.iterator()) {
            data[item.key] = item.value
        }

        return db.collection("users").add(data)
    }
}