package ru.hse.egorov.reading_tracker.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_sign_in.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.statistics.StatisticsManager
import ru.hse.egorov.reading_tracker.ui.MainActivity
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment
import ru.hse.egorov.reading_tracker.ui.statistics.BooksStatisticsFragment
import ru.hse.egorov.reading_tracker.ui.statistics.OverallStatisticsFragment
import ru.hse.egorov.reading_tracker.ui.statistics.SessionsStatisticsFragment
import java.util.*

class SignInActivity : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private val dbManager = DatabaseManager()
    private val statsManager = StatisticsManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ACTION_BAR_TITLE

        signInEmail.setOnClickListener {
            if (email.text.toString() != "" && password.text.toString() != "") {
                it.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                dbManager.signIn(email.text.toString(), password.text.toString())?.addOnCompleteListener(this
                ) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        setUserDataAndStart()
                    } else {
                        progressBar.visibility = View.GONE
                        it.visibility = View.VISIBLE
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Snackbar.make(findViewById(android.R.id.content), "Authentication Failed.Reason:${task.exception?.localizedMessage}"
                                , Snackbar.LENGTH_SHORT).show()
                    }
                }
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Please fill all fields."
                        , Snackbar.LENGTH_SHORT).show()
            }
        }

        signInGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            googleSignInClient = GoogleSignIn.getClient(this, gso)
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        dbManager.signUpWithGoogle(acct).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "signInWithCredential:success")
                Snackbar.make(findViewById(android.R.id.content), "Authentication succeeded.", Snackbar.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                Snackbar.make(findViewById(android.R.id.content), "Authentication Failed.Reason:${task.exception?.localizedMessage}",
                        Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
                Snackbar.make(findViewById(android.R.id.content), "Authentication succeeded.", Snackbar.LENGTH_SHORT).show()
                setUserDataAndStart()
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun setUserDataAndStart() {
        OverallStatisticsFragment.getAllSessions().clear()
        LibraryFragment.getAdapter().clear()
        OverallStatisticsFragment.getSessionsForPeriod().clear()
        SessionsStatisticsFragment.getAdapter().clear()
        BooksStatisticsFragment.getAdapter().clear()

        dbManager.getLibrary().addOnSuccessListener {
            val bookMap = HashMap<String, Pair<String, String>>()
            val libraryAdapter = LibraryFragment.getAdapter()
            for (book in it.documents) {
                libraryAdapter.add(LibraryFragment.Book(book["author"] as String?, book["title"] as String, book.id,
                        book["media"] as String, null, book["last updated"] as Date, (book["pageCount"] as Long?)?.toInt()))
                bookMap[book.id] = Pair(book["author"] as String, book["title"] as String)
            }
            statsManager.setUpSessions(bookMap) {
                val intent = Intent(this,
                        MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            libraryAdapter.sortByLastUpdated()
        }
    }

    companion object {
        private const val RC_SIGN_IN = 1
        private const val TAG = "Sign in"
        private const val ACTION_BAR_TITLE = "Вход"
    }
}
